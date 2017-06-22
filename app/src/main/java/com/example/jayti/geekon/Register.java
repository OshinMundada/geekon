package com.example.jayti.geekon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Register extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    public static final String DATA = "contact_file";
    private static final int INTENT_PASS_STATE = 1;
    private Button done;
    Button setmap;
    Spinner countrySpinner, stateSpinner;
    ArrayList<String> countries = new ArrayList<String>();
    ArrayList<String> states = new ArrayList<String>();
    private EditText nickname, city, latitude, longitude, password, year, email;
    String ACountryName, AState, ANickname, APassword;
    String userNickName;
    FirebaseAuth auth;
    String email_ip, password_ip;
    DatabaseReference mReference, mReference2;

    public Register() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button button = (Button) findViewById(R.id.done_button);
        button.setOnClickListener(this);


        nickname = (EditText) findViewById(R.id.nickname_input);
        nickname.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        password = (EditText) findViewById(R.id.password_input);
        password.setOnFocusChangeListener(this);
        email = (EditText) findViewById(R.id.email_input);
        email.setOnFocusChangeListener(this);
        fetchCountries();
        countrySpinner = (Spinner) findViewById(R.id.country_input);
        stateSpinner = (Spinner) findViewById(R.id.state_input);

        auth = FirebaseAuth.getInstance();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_button:
//                validate();
                email_ip = email.getText().toString();
                password_ip = password.getText().toString().trim();
                auth.createUserWithEmailAndPassword(email_ip, password_ip)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                Toast.makeText(getActivity(), "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Registered new user",Toast.LENGTH_SHORT).show();
                                    addUser(task.getResult().getUser(),ACountryName,AState);
                                    Intent intent = new Intent(getBaseContext(),UserHome.class);
                                    startActivity(intent);

                                }
                            }
                        });

                Toast.makeText(getApplicationContext(),"Done!",Toast.LENGTH_SHORT);


                break;

        }

    }

    public void addUser(FirebaseUser user, String country, String state){
        String  nickname_ip = nickname.getText().toString().trim();
        addDataToServer(user.getUid(),nickname_ip,email_ip,country,state);


    }
    public void addDataToServer(String uid,String nickname,String email,String country,String state){
        User user=new User(nickname,email,country,state);
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geekon-3c734.firebaseio.com/userDb");
        mReference.child(uid).setValue(user);

    }

    @Override
    public void onFocusChange(View view, boolean Focus) {
        switch(view.getId()){
            case R.id.nickname_input:
                if(!Focus){
                    if( nickname.getText().toString().length()==0 ){
                        nickname.setError( "Please enter Nickname!" );
                    }
                    else{
                        userNickName=nickname.getText().toString();
                        dupCheckNickName();
                    }
                }
                break;


            case R.id.password_input:
                if (!Focus) {
                    if ( password.getText().toString().length() == 0) {
                        password.setError("Please enter Password!");
                    }
                    else if (password.getText().toString().length() < 3){
                        password.setError("Password should contain atleast 3 characters!");
                    }
                }
                break;



        }
    }
    public void fetchCountries() {
        String url ="http://bismarck.sdsu.edu/hometown/countries";
        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                countries.add("Select Country");
                if (response != null) {
                    for (int i=0;i<response.length();i++){
                        try {
                            countries.add(response.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                countrySpinner = (Spinner)findViewById(R.id.country_input);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,countries);

                countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ACountryName = parent.getSelectedItem().toString();

                        if (ACountryName.equals("Select Country")) {
                            states.add("Select State");
                            stateSpinner = (Spinner) findViewById(R.id.state_input);
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,states);

                            stateSpinner.setAdapter(dataAdapter);
                            fetchStates();

                        } else {

                            fetchStates();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                countrySpinner.setAdapter(dataAdapter);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        JsonArrayRequest getRequest = new JsonArrayRequest( url, success, failure);
        VolleyQueue.instance(getApplicationContext()).add(getRequest);
    }

    public void fetchStates() {
        states.clear();
        states.add("Select State");
        String url ="http://bismarck.sdsu.edu/hometown/states?country="+ACountryName;
        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i=0;i<response.length();i++){
                        try {
                            states.add(response.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                stateSpinner = (Spinner) findViewById(R.id.state_input);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,states);

                stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //                  Toast.makeText(getApplicationContext(),parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        AState=parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                stateSpinner.setAdapter(dataAdapter);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        JsonArrayRequest getRequest = new JsonArrayRequest( url, success, failure);
        VolleyQueue.instance(getApplicationContext()).add(getRequest);
    }

    public void dupCheckNickName() {

        String url ="http://bismarck.sdsu.edu/hometown/nicknameexists?name="+userNickName;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        if(response.startsWith("true")){
                            nickname.setError( "Nickname Already Exists!" );
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyQueue.instance(getApplicationContext()).add(stringRequest);
    }


    public String viewToString(View v){
        EditText view = (EditText) v;
        return view.getText().toString();
    }

    public int viewToInt(View v){
        EditText view = (EditText) v;
        return Integer.parseInt(view.getText().toString());
    }

    public void validate(){
        if (viewToString(nickname).length() == 0) {
            nickname.requestFocus();
        }
        else if (viewToString(password).length() == 0) {
            password.requestFocus();
        } else if (viewToString(year).length() == 0) {
            year.requestFocus();
        } else if (countrySpinner.getSelectedItemPosition() == 0) {
            TextView i = (TextView) countrySpinner.getSelectedView();
            i.setError("Select Country");
            countrySpinner.requestFocus();
        } else if (stateSpinner.getSelectedItemPosition() == 0) {
            TextView i = (TextView) stateSpinner.getSelectedView();
            i.setError("Select State");
            stateSpinner.requestFocus();
        }else if (viewToString(year)!=null) {
            if (viewToInt(year) < 1970 || viewToInt(year) > 2017) {
                year.setError("Enter year between 1970 and 2017!");
            }
            else if(nickname.getError()== null && password.getError()== null && city.getError()== null
                    && year.getError()== null){
            }
            else {
                Toast.makeText(getApplicationContext(), "Enter valid data!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

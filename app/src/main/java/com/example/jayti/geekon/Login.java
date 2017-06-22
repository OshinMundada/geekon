package com.example.jayti.geekon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity implements ValueEventListener, View.OnClickListener {
    private FirebaseAuth auth;
    EditText emailInput,passwordInput;
    Button login,signup;
    String email_ip,nName,email;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = (EditText) findViewById(R.id.inputEmail);
        passwordInput = (EditText) findViewById(R.id.inputPassword);
        login = (Button) findViewById(R.id.loginBtn);
        login.setOnClickListener(this);
        signup=(Button)findViewById(R.id.signUpBtn);
        signup.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        email_ip=emailInput.getText().toString();
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geekon-3c734.firebaseio.com/userDb");
        Query q = mReference.orderByKey();
        q.addValueEventListener(this);

    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                login_user();
                break;
            case R.id.signUpBtn:
                Intent i=new Intent(this,Register.class);
                startActivity(i);
                break;

        }
    }

    public void login_user() {
        final String emailText = emailInput.getText().toString();
        final String passwordText = passwordInput.getText().toString();

        auth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Wrong username or password!"+emailText+" "+passwordText,Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"Logged In!",Toast.LENGTH_SHORT).show();

                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            auth = FirebaseAuth.getInstance();

                            FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    email=user.getEmail();

                                }
                            };
                            Intent intent = new Intent(Login.this, UserHome.class);
                            intent.putExtra("email",emailText);
                            startActivity(intent);
                            finish();

                        }
                    }
                });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            User post = postSnapshot.getValue(User.class);
            if(post.email==email)
                nName=post.nickname;
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


}

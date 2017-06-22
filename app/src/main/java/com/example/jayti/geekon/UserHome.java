package com.example.jayti.geekon;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserHome extends AppCompatActivity implements View.OnClickListener {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    Button cat1,cat2,cat3,cat4;
    String  nName,user_email;
    FirebaseAuth auth;
    Intent i;
    FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        auth = FirebaseAuth.getInstance();

        cat1=(Button)findViewById(R.id.button);
        cat1.setOnClickListener(this);

        cat2=(Button)findViewById(R.id.button2);
        cat2.setOnClickListener(this);

        cat3=(Button)findViewById(R.id.button3);
        cat3.setOnClickListener(this);

        cat4=(Button)findViewById(R.id.button4);
        cat4.setOnClickListener(this);

        user_email=getIntent().getStringExtra("email");
        //Log.i("UH",user_email);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(UserHome.this, Login.class));
                    finish();
                }
            }
        };
    }

    private void addDrawerItems() {
        final String[] osArray = { "Choose topic", "Profile", "Previous Games", "Pending challenges","Leaderboard","Signout" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UserHome.this, "You've chosen:" +osArray[position], Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        i=new Intent(UserHome.this,UserHome.class);
                        i.putExtra("email",user_email);
                        startActivity(i);
                        break;
                    case 1:
                        i=new Intent(UserHome.this,Profile.class);
                        i.putExtra("email",user_email);
                        startActivity(i);
                        break;

                    case 2:
                        i=new Intent(UserHome.this,Previous.class);
                        i.putExtra("email",user_email);
                        startActivity(i);
                        break;

                    case 3:
                        i=new Intent(UserHome.this,Pending.class);
                        i.putExtra("email",user_email);
                        startActivity(i);
                        break;
                    case 4:
                        i=new Intent(UserHome.this,Leaderboard.class);
                        i.putExtra("email",user_email);
                        startActivity(i);
                        break;
                    case 5:
                        signOut();
                }


            }
        });
    }

    private void signOut() {
        auth.signOut();
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public void onClick(View v) {
        Bundle b=new Bundle();
        switch (v.getId()){
            case R.id.button3:
                i=new Intent(this,DisplayUsers.class);
                i.putExtra("category","Java");
                i.putExtra("email",user_email);
                Log.i("info","Java selected");
                Log.i("info",user_email);

                startActivity(i);
                break;
            case R.id.button:
                i=new Intent(this,DisplayUsers.class);
                i.putExtra("category","HTML-CSS");
                i.putExtra("email",user_email);
                startActivity(i);

                Log.i("info","HTML selected");

                break;
            case R.id.button2:
                i=new Intent(this,DisplayUsers.class);
                i.putExtra("category","C");
                i.putExtra("email",user_email);
                startActivity(i);
                Log.i("info","C++ selected");
                break;
            case R.id.button4:
                i=new Intent(this,DisplayUsers.class);
                i.putExtra("category","Android");
                i.putExtra("email",user_email);
                startActivity(i);

                Log.i("info","Android selected");
                break;


        }
    }

}
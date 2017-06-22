package com.example.jayti.geekon;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Pending extends AppCompatActivity implements AdapterView.OnItemClickListener {
    DatabaseReference mReference, pendingRef;
    Firebase reference1;
    String email;
    String nName;
    List pending;
    ArrayAdapter<String> dataAdapter;
    ListView pendingGames;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    //Button cat1,cat2,cat3,cat4;
    //String  nName,user_email;
    FirebaseAuth auth;
    Intent i;
    FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        email = getIntent().getStringExtra("email");


        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        auth = FirebaseAuth.getInstance();


        pending=new ArrayList<String>();
        pendingGames=(ListView)findViewById(R.id.displayChallenges);

        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geekon-3c734.firebaseio.com/userDb");
        pendingRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geekon-3c734.firebaseio.com/Matches");

        pendingGames.setOnItemClickListener(this);
        Query q = mReference.orderByKey();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User post = postSnapshot.getValue(User.class);
                    if (post.email.equals(email))
                        nName = post.nickname;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query q1 = pendingRef.orderByKey();
        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("Score")) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String key = d.getKey().toString();
                        String[] parts = key.split("_");
                        String val = parts[0];
                        String receiver = parts[1];
                        if (val.equals(nName)){
                            if(!pending.contains(val + "_" + receiver))
                                pending.add(val + "_" + receiver);
                            if(pending.contains(val + "_" + receiver) && dataSnapshot.hasChild("Score"))
                                pending.remove(val + "_" + receiver);
                        }
                    }
                }
                try {
                    dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,pending) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.WHITE);
                            return view;
                        }
                    };
                    pendingGames.setAdapter(dataAdapter);
                }catch (Exception e){

                }

                Log.i("pending",pending.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                    startActivity(new Intent(Pending.this, Login.class));
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
                Toast.makeText(Pending.this, "You've chosen:" +osArray[position], Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        i=new Intent(Pending.this,UserHome.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        break;
                    case 1:
                        i=new Intent(Pending.this,Profile.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        break;

                    case 2:
                        i=new Intent(Pending.this,Previous.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        break;

                    case 3:
                        i=new Intent(Pending.this,Pending.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        break;
                    case 4:
                        i=new Intent(Pending.this,Leaderboard.class);
                        i.putExtra("email",email);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String vs=parent.getItemAtPosition(position).toString();
        String[] parts=vs.split("_");
        String val=parts[0];
        String opp=parts[1];
        pending=new ArrayList();
        Intent i=new Intent(this,Game.class);
        Log.i("Game1","inside game");
        i.putExtra("nickname",val);
        i.putExtra("opponent",opp);
        i.putExtra("category","Pending");
        i.putExtra("email",email);
        startActivity(i);
    }
}

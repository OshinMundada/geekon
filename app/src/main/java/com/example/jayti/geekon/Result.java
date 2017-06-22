package com.example.jayti.geekon;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Result extends AppCompatActivity implements ValueEventListener {

    int points;
    DatabaseReference mReference;
    TextView res,head;
    String email,nName;
    String oppo,nick, score1="", score2="", newdb, status;
    DatabaseReference dbRef;
    Firebase fbRef1, fbRef2;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    FirebaseAuth auth;
    Intent i;
    FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        email=getIntent().getStringExtra("email");

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        auth = FirebaseAuth.getInstance();

        res= (TextView) findViewById(R.id.res);
        head= (TextView) findViewById(R.id.header);

        points=getIntent().getIntExtra("points",-1);
        nick=getIntent().getStringExtra("nickname");
        oppo=getIntent().getStringExtra("opponent");
        status=getIntent().getStringExtra("status");

        head.setText(nick + " v/s " + oppo);
        res.setText("" + points);
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geekon-3c734.firebaseio.com/userDb");
        Query q = mReference.orderByKey();
        q.addListenerForSingleValueEvent(this);
        Firebase.setAndroidContext(this);
        fbRef1 = new Firebase("https://geekon-3c734.firebaseio.com/Matches/" + nick + "_"+oppo);
//        dbRef.child("Matches").child().setValue(user);

        fbRef1.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                score1 = dataSnapshot.child("Score").getValue().toString();
                Log.i("osh",score1);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        if(status.equals("Pending")) {
            fbRef2 = new Firebase("https://geekon-3c734.firebaseio.com/Matches/" + oppo + "_" + nick);

            fbRef2.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                @Override
                public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                    score2 = dataSnapshot.child("Score").getValue().toString();
                    Log.i("osh1", score2);

                    dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geekon-3c734.firebaseio.com/FinalScore");
                    Log.i("osh", score2 + "_" + score1);
                    newdb = oppo + "_" + nick;
                    FinalScore finalsc = new FinalScore(nick, oppo, score1, score2);
                    dbRef.child(newdb).setValue(finalsc);

                    fbRef1.removeValue();
                    fbRef2.removeValue();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
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
                    startActivity(new Intent(Result.this, Login.class));
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
                Toast.makeText(Result.this, "You've chosen:" +osArray[position], Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        i=new Intent(Result.this,UserHome.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        break;
                    case 1:
                        i=new Intent(Result.this,Profile.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        break;

                    case 2:
                        i=new Intent(Result.this,Previous.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        break;

                    case 3:
                        i=new Intent(Result.this,Pending.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        break;
                    case 4:
                        i=new Intent(Result.this,Leaderboard.class);
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
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            User post = postSnapshot.getValue(User.class);
            int total =Integer.parseInt(postSnapshot.child("totalPoints").getValue().toString())+points;
            if(post.nickname.equals(nick)){
                postSnapshot.getRef().child("totalPoints").setValue(total);
                Log.i("total"," "+total);
                break;
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

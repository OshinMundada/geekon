package com.example.jayti.geekon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    String email;
    String nName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);

        if (auth.getCurrentUser() != null) {
            email = auth.getCurrentUser().getEmail();
            Intent i = new Intent(this, UserHome.class);
            i.putExtra("email",email);
            startActivity(i);
            finish();
        }
        else{
            auth = FirebaseAuth.getInstance();
            Intent i = new Intent(this, Login.class);
            startActivity(i);
        }

    }
}

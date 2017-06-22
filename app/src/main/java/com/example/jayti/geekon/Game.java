package com.example.jayti.geekon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game extends AppCompatActivity implements View.OnClickListener {
    String cat,nName,opp,email;
    DatabaseReference mReference,quesRef,mReference1;
    Firebase reference1,reference2;
    Firebase final1,final2;
    List<Question> quesList;
    int gamepoints;
    Question q;
    int i=0;
    int score=0;
    Question map;

    private static final long SPLASH_DISPLAY_LENGTH = 3000;
    String ques,ans,op1,op2,op3,op4;
    Button qu,a,b,c,d,next;
    int points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);


        qu=(Button)findViewById(R.id.question);
        a= (Button) findViewById(R.id.op1);
        b= (Button) findViewById(R.id.op2);
        c= (Button) findViewById(R.id.op3);
        d= (Button) findViewById(R.id.op4);
        next=(Button)findViewById(R.id.next);


        cat=getIntent().getStringExtra("category");
        email=getIntent().getStringExtra("email");
        nName=getIntent().getStringExtra("nickname");
        opp=getIntent().getStringExtra("opponent");
        points=0;

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://geekon-3c734.firebaseio.com/Matches/" + nName + "_"+opp);
        reference2 = new Firebase("https://geekon-3c734.firebaseio.com/Matches/" + opp + "_" + nName);

        if(!cat.equals("Pending"))
            final1 = new Firebase("https://geekon-3c734.firebaseio.com/FinalScore/" + nName + "_"+opp);
        else
            final1 = new Firebase("https://geekon-3c734.firebaseio.com/FinalScore/" + opp + "_"+nName);

        quesList=new ArrayList<Question>();
        quesRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://geekon-3c734.firebaseio.com/Matches/" + nName + "_"+opp);

        quesRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("Score") && cat.equals("Pending")){

                            for (DataSnapshot ds:dataSnapshot.getChildren()){
                                q=ds.getValue(Question.class);

                                Log.i("Q:",q.toString());

                                Log.i("osh", ""+((Boolean) quesList.contains(q)));
                                if(!(quesList.contains(q))) {
                                    quesList.add(q);
                                    Log.i("osh", "" + quesList.get(i).toString());
                                }
                                //else break;

                                Log.i("osh","ques list size in second "+quesList.size());
                            }

                            i=0;
                            map=quesList.get(i);
                            qu.setText(map.getQ().toString());
                            a.setText(map.getOption1());
                            b.setText(map.getOption2());
                            c.setText(map.getOption3());
                            d.setText(map.getOption4());
                            ans=map.getAns();
                            a.setOnClickListener(Game.this);
                            b.setOnClickListener(Game.this);
                            c.setOnClickListener(Game.this);
                            d.setOnClickListener(Game.this);
                            next.setOnClickListener(Game.this);
                        }


                else if(cat!="Pending" && !dataSnapshot.hasChild("Score")){
                    mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://geekon-3c734.firebaseio.com/Categories/"+cat);
                    Query q = mReference.orderByKey();
                    q.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds:dataSnapshot.getChildren()){

                                Question q=ds.getValue(Question.class);

                                quesList.add(q);
                                Log.i("osh",q.getQ());

                            }

                            Log.i("osh","ques list size"+String.valueOf(quesList.size()));
                            i=0;
                            map=quesList.get(i);
                            reference1.push().setValue(map);
                            reference2.push().setValue(map);
                            qu.setText(map.getQ().toString());
                            a.setText(map.getOption1());
                            b.setText(map.getOption2());
                            c.setText(map.getOption3());
                            d.setText(map.getOption4());
                            ans=map.getAns();
                            a.setOnClickListener(Game.this);
                            b.setOnClickListener(Game.this);
                            c.setOnClickListener(Game.this);
                            d.setOnClickListener(Game.this);
                            next.setOnClickListener(Game.this);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    protected void setQues() {
        Log.i("osh",map.getQ());

        a.setBackgroundColor(Color.parseColor("#BDBDBD"));
        b.setBackgroundColor(Color.parseColor("#BDBDBD"));
        c.setBackgroundColor(Color.parseColor("#BDBDBD"));
        d.setBackgroundColor(Color.parseColor("#BDBDBD"));
        qu.setText(map.getQ().toString());
        a.setText(map.getOption1());
        b.setText(map.getOption2());
        c.setText(map.getOption3());
        d.setText(map.getOption4());
        ans=map.getAns();





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.op1:
                if(ans.equals(a.getText()) || ans.equals(b.getText()) || ans.equals(c.getText()) || ans.equals(d.getText())) {

            }

                if(ans.equals(a.getText())){
                    Toast.makeText(this,"A",Toast.LENGTH_SHORT).show();
                    a.setBackgroundColor(Color.GREEN);
                    gamepoints+=10;
                }
                else {
                    a.setBackgroundColor(Color.RED);
                    if(ans.equals(b.getText())) b.setBackgroundColor(Color.GREEN);
                    if(ans.equals(c.getText())) c.setBackgroundColor(Color.GREEN);
                    if(ans.equals(d.getText())) d.setBackgroundColor(Color.GREEN);

                }
                break;
            case R.id.op2:
                if(ans.equals(b.getText())){
                    Toast.makeText(this,"B",Toast.LENGTH_SHORT).show();
                    gamepoints+=10;
                    b.setBackgroundColor(Color.GREEN);
                }
                else {
                    b.setBackgroundColor(Color.RED);
                    if(ans.equals(a.getText())) a.setBackgroundColor(Color.GREEN);
                    if(ans.equals(c.getText())) c.setBackgroundColor(Color.GREEN);
                    if(ans.equals(d.getText())) d.setBackgroundColor(Color.GREEN);

                }

                break;
            case R.id.op3:
                Toast.makeText(this,"C",Toast.LENGTH_SHORT).show();
                if(ans.equals(c.getText())){
                    gamepoints+=10;
                    c.setBackgroundColor(Color.GREEN);
                }
                else {
                    c.setBackgroundColor(Color.RED);
                    if(ans.equals(b.getText())) b.setBackgroundColor(Color.GREEN);
                    if(ans.equals(a.getText())) a.setBackgroundColor(Color.GREEN);
                    if(ans.equals(d.getText())) d.setBackgroundColor(Color.GREEN);

                }
                break;
            case R.id.op4:
                Toast.makeText(this,"D",Toast.LENGTH_SHORT).show();
                if(ans.equals(d.getText())){
                    gamepoints+=10;
                    d.setBackgroundColor(Color.GREEN);
                }
                else {
                    d.setBackgroundColor(Color.RED);
                    if(ans.equals(b.getText())) b.setBackgroundColor(Color.GREEN);
                    if(ans.equals(c.getText())) c.setBackgroundColor(Color.GREEN);
                    if(ans.equals(a.getText())) a.setBackgroundColor(Color.GREEN);
                }

                break;

            case R.id.next:
                i++;

                if(i<quesList.size()) {
                    map=quesList.get(i);
                    if(cat!="Pending") {
                        reference1.push().setValue(map);
                        reference2.push().setValue(map);

                    }
                    setQues();
                    Log.i("osh","CALL ME " + i);

                }

                else {
                    Map<String, String> m = new HashMap<String, String>();
                    m.put("Score", String.valueOf(gamepoints));
                    reference1.child("Score").setValue(gamepoints);
                    Intent result=new Intent(Game.this,Result.class);
                    result.putExtra("points",gamepoints);
                    result.putExtra("email",email);
                    result.putExtra("nickname",nName);
                    result.putExtra("opponent",opp);
                    result.putExtra("status",cat);
                    startActivity(result);
                }
        }
    }

}

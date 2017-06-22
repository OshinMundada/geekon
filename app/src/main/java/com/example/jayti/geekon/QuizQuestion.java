package com.example.jayti.geekon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QuizQuestion extends AppCompatActivity implements View.OnClickListener {
    private static final long SPLASH_DISPLAY_LENGTH = 10000;
    String ques,ans,op1,op2,op3,op4;
    Button q,a,b,c,d,next;
    int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);
        points=getIntent().getIntExtra("points",-1);
        q=(Button)findViewById(R.id.question);
        a= (Button) findViewById(R.id.op1);
        b= (Button) findViewById(R.id.op2);
        c= (Button) findViewById(R.id.op3);
        d= (Button) findViewById(R.id.op4);
        next=(Button)findViewById(R.id.next);
        ques=getIntent().getStringExtra("question");
        Log.i("infy",ques);
        ans=getIntent().getStringExtra("answer");
        op1=getIntent().getStringExtra("option1");
        op2=getIntent().getStringExtra("option2");
        op3=getIntent().getStringExtra("option3");
        op4=getIntent().getStringExtra("option4");

        q.setText(ques);
        a.setText(op1);
        b.setText(op2);
        c.setText(op3);
        d.setText(op4);

        a.setOnClickListener(this);
        b.setOnClickListener(this);
        c.setOnClickListener(this);
        d.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.op1:
                if(ans.equals(a.getText())){
                    Toast.makeText(this,"A",Toast.LENGTH_SHORT).show();
                    a.setBackgroundColor(Color.GREEN);
                    points+=10;
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
                else
                    c.setBackgroundColor(Color.RED);

                break;
            case R.id.op2:
                if(ans.equals(b.getText())){
                    Toast.makeText(this,"B",Toast.LENGTH_SHORT).show();
                    points+=10;
                    b.setBackgroundColor(Color.GREEN);

                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {

                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
                else
                    c.setBackgroundColor(Color.RED);

                break;
            case R.id.op3:
                Toast.makeText(this,"C",Toast.LENGTH_SHORT).show();
                if(ans.equals(c.getText())){
                    points+=10;
                    c.setBackgroundColor(Color.GREEN);
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {

                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
                else
                    c.setBackgroundColor(Color.RED);
                break;
            case R.id.op4:
                Toast.makeText(this,"D",Toast.LENGTH_SHORT).show();
                if(ans.equals(d.getText())){
                    points+=10;
                    d.setBackgroundColor(Color.GREEN);
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {

                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
                else
                    c.setBackgroundColor(Color.RED);

                break;

            case R.id.next:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",String.valueOf(points));
                setResult(Activity.RESULT_OK,returnIntent);
                this.finish();
        }
    }
}

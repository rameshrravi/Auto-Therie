package com.auto.autotherieneu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TheoryQuestionsBlockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory_questions_block);
    }
    public void backPressed(View view){
        onBackPressed();
    }
    public void questionsPage(View view){

    }

}
package com.auto.autotherieneu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Window window;
    ImageView iv_lets_start;
    SharedPreferences preferences;
    Boolean isLogin=false;
    String languageID="3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        isLogin=preferences.getBoolean("isLogin",false);
        languageID=preferences.getString(StringConstants.prefLanguageID,"3");

        if(isLogin){
            if(languageID.equals("1")){
                Intent i=new Intent(getApplicationContext(),HomepageGermanActivity.class);
                startActivity(i);
                finish();
            }
            if(languageID.equals("2")){
                Intent i=new Intent(getApplicationContext(),HomepageFrenchActivity.class);
                startActivity(i);
                finish();
            }
            if(languageID.equals("3")){
                Intent i=new Intent(getApplicationContext(),HomepageActivity.class);
                startActivity(i);
                finish();
            }
            if(languageID.equals("4")){
                Intent i=new Intent(getApplicationContext(),HomepageItalianActivity.class);
                startActivity(i);
                finish();
            }
        }
        iv_lets_start=findViewById(R.id.image_lets_start);


        iv_lets_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
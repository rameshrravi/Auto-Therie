package com.auto.autotherieneu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppLanguagesActivity extends AppCompatActivity {

    TextView tv_english,tv_french;
    LinearLayout layout_dutch,layout_french,layout_english,layout_italian;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_languages);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        tv_english=findViewById(R.id.text_english);
        tv_french=findViewById(R.id.text_french);

        layout_dutch=findViewById(R.id.layout_deutsch);
        layout_english=findViewById(R.id.layout_english);
        layout_french=findViewById(R.id.layout_french);
        layout_italian=findViewById(R.id.layout_italiensch);

        layout_dutch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString(StringConstants.prefLanguageID,"1");
                editor.apply();
                editor.commit();
                Intent i  = new Intent(getApplicationContext(),HomepageGermanActivity.class);
                startActivity(i);
                finish();
            }
        });
        layout_french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString(StringConstants.prefLanguageID,"2");
                editor.apply();
                editor.commit();
                Intent i  = new Intent(getApplicationContext(),HomepageFrenchActivity.class);
                startActivity(i);
                finish();
            }
        });
        layout_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString(StringConstants.prefLanguageID,"3");
                editor.apply();
                editor.commit();
                Intent i  = new Intent(getApplicationContext(),HomepageActivity.class);
                startActivity(i);
                finish();
            }
        });
        layout_italian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString(StringConstants.prefLanguageID,"4");
                editor.apply();
                editor.commit();
                Intent i  = new Intent(getApplicationContext(),HomepageItalianActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    public void backPressed(View view){
        onBackPressed();
    }

}
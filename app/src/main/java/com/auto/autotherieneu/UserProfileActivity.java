package com.auto.autotherieneu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    LinearLayout layout_app_language,layout_change_password,layout_logout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    LinearLayout layout_learning,layout_rules,layout_exam,layout_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        layout_learning=findViewById(R.id.layout_learning);
        layout_rules=findViewById(R.id.layout_rules);
        layout_exam=findViewById(R.id.layout_exams);
        layout_profile=findViewById(R.id.layout_profile);


        layout_app_language=findViewById(R.id.layout_app_language);
        layout_change_password=findViewById(R.id.layout_change_password);
        layout_logout=findViewById(R.id.layout_logout);

        layout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {
                    final Dialog dialog = new Dialog(UserProfileActivity.this,R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    // Setting dialogview
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);

                    dialog.setContentView(R.layout.layout_logout_dialog);
                    TextView tv_close=dialog.findViewById(R.id.text_cancel);
                    TextView tv_no=dialog.findViewById(R.id.text_no);
                    TextView tv_yes=dialog.findViewById(R.id.text_yes);

                    tv_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                        }
                    });
                    tv_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                        }
                    });
                    tv_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            editor.clear();
                            editor.apply();
                            editor.commit();
                            dialog.dismiss();
                            Intent i=new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                    dialog.show();


                }
            }
        });

        layout_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(i);
            }
        });
        layout_app_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), AppLanguagesActivity.class);
                startActivity(i);
            }
        });

        layout_learning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),LearningNewActivity.class);
                startActivity(i);

            }
        });
        layout_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),RulesActivity.class);
                startActivity(i);
            }
        });
        layout_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),PracticeActivity.class);
                startActivity(i);

            }
        });
        layout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),UserProfileActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void backPressed(View view){
        onBackPressed();
    }

}
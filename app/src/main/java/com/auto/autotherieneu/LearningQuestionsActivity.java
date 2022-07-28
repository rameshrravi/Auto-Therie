package com.auto.autotherieneu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearningQuestionsActivity extends AppCompatActivity {

    List<QuestionsModel> questionsModelList = new ArrayList<>();
    QuestionsModel questionsModel = new QuestionsModel();
    QuestionsModel questionsModel1 = new QuestionsModel();
    String token;
    int position1 = 0;
    String answer = "";
    String type = "";
    String categoryID,CategoryIDD;
    String userID;
    String language_id = "";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    String fromScreen = "";
    TextView tv_previous, tv_next;
    ChapterAdapter notificationAdapter;
    private TextView tv_question, tv_answer1, tv_answer2, tv_answer3, tv_answer4;
    LinearLayout linearLayoutContainer;
    ImageView iv_question;
    LinearLayout layout_answer1, layout_answer2, layout_answer3, layout_answer4;
    TextView tv_question_number;
    ImageView iv_answer1, iv_answer2, iv_answer3, iv_answer4;
    MyDatabaseHelper myDataBaseHelper;
    int liner1=0,liner2=0,liner3=0,liner4=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_questions);

        myDataBaseHelper = new MyDatabaseHelper(LearningQuestionsActivity.this);

        preferences = getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        token = preferences.getString(StringConstants.prefToken, "");
        userID = preferences.getString(StringConstants.prefUserID, "");
        language_id = preferences.getString(StringConstants.prefLanguageID, "");

        categoryID = getIntent().getStringExtra("CategoryID");
        CategoryIDD = getIntent().getStringExtra("CategoryIDD");
        fromScreen = getIntent().getStringExtra("FromScreen");
        type = getIntent().getStringExtra("Type");

        recyclerView = findViewById(R.id.recyclerview_questions);
        tv_question_number = findViewById(R.id.text_question_number);

        tv_question = (TextView) findViewById(R.id.text_question);
        tv_answer1 = (TextView) findViewById(R.id.text_answer1);
        tv_answer2 = (TextView) findViewById(R.id.text_answer2);
        tv_answer3 = (TextView) findViewById(R.id.text_answer3);
        tv_answer4 = (TextView) findViewById(R.id.text_answer4);
        iv_question = (ImageView) findViewById(R.id.image_question);
        layout_answer1 = (LinearLayout) findViewById(R.id.layout_answer1);
        layout_answer2 = (LinearLayout) findViewById(R.id.layout_answer2);
        layout_answer3 = (LinearLayout) findViewById(R.id.layout_answer3);
        layout_answer4 = (LinearLayout) findViewById(R.id.layout_answer4);

        iv_answer1 = (ImageView) findViewById(R.id.image_answer1);
        iv_answer2 = (ImageView) findViewById(R.id.image_answer2);
        iv_answer3 = (ImageView) findViewById(R.id.image_answer3);
        iv_answer4 = (ImageView) findViewById(R.id.image_answer4);

        tv_previous = findViewById(R.id.text_previous);
        tv_next = findViewById(R.id.text_next);

        layout_answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionsModelList.get(position1).getUseranswered().length() > 0) {
                  return;
                }
                liner1=1;
                if(answer.equals("")){
                    answer = "answer1";
                }else {
                    answer = answer + "," + "answer1";
                }

                iv_answer1.setVisibility(View.VISIBLE);
                iv_answer2.setVisibility(View.VISIBLE);
                iv_answer3.setVisibility(View.VISIBLE);
                iv_answer4.setVisibility(View.VISIBLE);
                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                    iv_answer1.setImageResource(R.drawable.correct);
                } else {
                    iv_answer1.setImageResource(R.drawable.wrong);
                }
                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                    iv_answer2.setImageResource(R.drawable.correct);
                } else {
                    iv_answer2.setImageResource(R.drawable.wrong);
                }

                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                    iv_answer3.setImageResource(R.drawable.correct);
                } else {
                    iv_answer3.setImageResource(R.drawable.wrong);
                }

                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                    iv_answer4.setImageResource(R.drawable.correct);
                } else {
                    iv_answer4.setImageResource(R.drawable.wrong);
                }
                if (liner2==1) {
                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                }else {
                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }

                if (liner3==1) {
                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));

                }else {
                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }
                if (liner4==1) {
                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                }else {
                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                /*layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));*/
                questionsModelList.get(position1).setUserAnswer("answer1");
                questionsModelList.get(position1).setIsAnswered("yes");

                questionsModel1 = questionsModelList.get(position1);
                // updateAnswers();
            }
        });
        layout_answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionsModelList.get(position1).getUseranswered().length() > 0) {
                    return;
                }
                if(answer.equals("")){
                    answer = "answer2";
                }else {

                    answer = answer + "," + "answer2";
                }

              /*  if(questionsModelList.get(position1).getCorrectAnswer().equals("answer2")){
                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                }
                else {
                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_red));
                    if(questionsModelList.get(position1).getCorrectAnswer().equals("answer1")){
                        layout_answer1.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                    }
                    if(questionsModelList.get(position1).getCorrectAnswer().equals("answer3")){
                        layout_answer3.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                    }
                    if(questionsModelList.get(position1).getCorrectAnswer().equals("answer4")){
                        layout_answer4.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                    }

                }*/
                liner2=1;
                iv_answer1.setVisibility(View.VISIBLE);
                iv_answer2.setVisibility(View.VISIBLE);
                iv_answer3.setVisibility(View.VISIBLE);
                iv_answer4.setVisibility(View.VISIBLE);

                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                    iv_answer1.setImageResource(R.drawable.correct);
                    // layout_answer1.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                } else {
                    iv_answer1.setImageResource(R.drawable.wrong);
                }
                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                    iv_answer2.setImageResource(R.drawable.correct);
                } else {
                    iv_answer2.setImageResource(R.drawable.wrong);
                }

                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                    iv_answer3.setImageResource(R.drawable.correct);
                    // layout_answer2.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                } else {
                    iv_answer3.setImageResource(R.drawable.wrong);
                }

                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                    iv_answer4.setImageResource(R.drawable.correct);
                    // layout_answer2.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                } else {
                    iv_answer4.setImageResource(R.drawable.wrong);
                }
                if (liner1==1) {
                    layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                }else {
                    layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }

                if (liner3==1) {
                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));

                }else {
                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }
                if (liner4==1) {
                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                }else {
                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));

                questionsModelList.get(position1).setUserAnswer("answer2");
                questionsModelList.get(position1).setIsAnswered("yes");

                questionsModel1 = questionsModelList.get(position1);
                // updateAnswers();
            }
        });
        layout_answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionsModelList.get(position1).getUseranswered().length() > 0) {
                    return;
                }

                if(answer.equals("")){
                    answer = "answer3";
                }else {
                    answer = answer + "," + "answer3";
                }
                /*if(questionsModelList.get(position1).getCorrectAnswer().equals("answer3")){
                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                }
                else {
                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_red));
                    if(questionsModelList.get(position1).getCorrectAnswer().equals("answer2")){
                        layout_answer2.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                    }
                    if(questionsModelList.get(position1).getCorrectAnswer().equals("answer1")){
                        layout_answer1.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                    }
                    if(questionsModelList.get(position1).getCorrectAnswer().equals("answer4")){
                        layout_answer4.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                    }

                }*/
                iv_answer1.setVisibility(View.VISIBLE);
                iv_answer2.setVisibility(View.VISIBLE);
                iv_answer3.setVisibility(View.VISIBLE);
                iv_answer4.setVisibility(View.VISIBLE);
                liner3=1;
                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                    iv_answer1.setImageResource(R.drawable.correct);
                    // layout_answer1.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                } else {
                    iv_answer1.setImageResource(R.drawable.wrong);
                }
                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                    iv_answer2.setImageResource(R.drawable.correct);
                    // layout_answer2.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                } else {
                    iv_answer2.setImageResource(R.drawable.wrong);
                }

                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                    iv_answer3.setImageResource(R.drawable.correct);


                } else {
                    iv_answer3.setImageResource(R.drawable.wrong);


                }

                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                    iv_answer4.setImageResource(R.drawable.correct);
                    // layout_answer2.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                } else {
                    iv_answer4.setImageResource(R.drawable.wrong);
                }
                if (liner2==1) {
                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                }else {
                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }

                if (liner1==1) {
                    layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));

                }else {
                    layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }
                if (liner4==1) {
                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                }else {
                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                /*layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));*/
                questionsModelList.get(position1).setUserAnswer("answer3");
                questionsModelList.get(position1).setIsAnswered("yes");

                questionsModel1 = questionsModelList.get(position1);
                // updateAnswers();
            }
        });
        layout_answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionsModelList.get(position1).getUseranswered().length() > 0) {
                    return;
                }
                if(answer.equals("")){
                    answer = "answer4";
                }else {
                    answer = answer + "," + "answer4";
                }



               /* if(questionsModelList.get(position1).getCorrectAnswer().equals("answer4")){
                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                }
                else {
                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_red));
                    if(questionsModelList.get(position1).getCorrectAnswer().equals("answer2")){
                        layout_answer2.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                    }
                    if(questionsModelList.get(position1).getCorrectAnswer().equals("answer3")){
                        layout_answer3.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                    }
                    if(questionsModelList.get(position1).getCorrectAnswer().equals("answer1")){
                        layout_answer1.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                    }

                }*/

                iv_answer1.setVisibility(View.VISIBLE);
                iv_answer2.setVisibility(View.VISIBLE);
                iv_answer3.setVisibility(View.VISIBLE);
                iv_answer4.setVisibility(View.VISIBLE);
                liner4=1;
                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                    iv_answer1.setImageResource(R.drawable.correct);
                    // layout_answer1.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                } else {
                    iv_answer1.setImageResource(R.drawable.wrong);
                }
                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                    iv_answer2.setImageResource(R.drawable.correct);
                    // layout_answer2.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                } else {
                    iv_answer2.setImageResource(R.drawable.wrong);
                }

                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                    iv_answer3.setImageResource(R.drawable.correct);
                    // layout_answer2.setBackground(getResources().getDrawable(R.drawable.rounded_cornered_edittext_color_green));
                } else {
                    iv_answer3.setImageResource(R.drawable.wrong);
                }

                if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                    iv_answer4.setImageResource(R.drawable.correct);
                } else {
                    iv_answer4.setImageResource(R.drawable.wrong);
                }
                //  layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                if (liner2==1) {
                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                }else {
                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }

                if (liner1==1) {
                    layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));

                }else {
                    layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }
                if (liner3==1)  {
                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                }else {
                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                }
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                /*layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));*/
                questionsModelList.get(position1).setUserAnswer("answer4");
                questionsModelList.get(position1).setIsAnswered("yes");

                questionsModel1 = questionsModelList.get(position1);
                //updateAnswers();
            }
        });

        tv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                position1 = position1 - 1;
                if (position1 == 0) {
                    tv_previous.setVisibility(View.GONE);
                }
                if (questionsModelList.get(position1).getUseranswered().length() > 0) {
                    tv_question_number.setText(String.valueOf(position1 + 1) + " / " + String.valueOf(questionsModelList.size()));
                    userAnserMethod();
                    if (position1 >= 0) {
                        tv_next.setText("NEXT");
                        questionsModel1 = questionsModelList.get(position1);
                        tv_question_number.setText(String.valueOf(position1 + 1) + " / " + String.valueOf(questionsModelList.size()));
                        tv_question.setText(questionsModel1.getQuestion());
                        tv_answer1.setText(questionsModel1.getAnswer1());
                        tv_answer2.setText(questionsModel1.getAnswer2());
                        tv_answer3.setText(questionsModel1.getAnswer3());
                        tv_answer4.setText(questionsModel1.getAnswer4());
                        if (questionsModel1.getUserAnswer().equals("answer1")) {
                            iv_answer1.setVisibility(View.VISIBLE);
                            iv_answer2.setVisibility(View.VISIBLE);
                            iv_answer3.setVisibility(View.VISIBLE);
                            iv_answer4.setVisibility(View.VISIBLE);
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                iv_answer1.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer1.setImageResource(R.drawable.wrong);
                            }
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer2.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer2.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer3.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer3.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer4.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer4.setImageResource(R.drawable.wrong);
                            }

                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                        } else if (questionsModel1.getUserAnswer().equals("answer2")) {
                            iv_answer1.setVisibility(View.VISIBLE);
                            iv_answer2.setVisibility(View.VISIBLE);
                            iv_answer3.setVisibility(View.VISIBLE);
                            iv_answer4.setVisibility(View.VISIBLE);
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                iv_answer1.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer1.setImageResource(R.drawable.wrong);
                            }
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                iv_answer2.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer2.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer3.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer3.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer4.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer4.setImageResource(R.drawable.wrong);
                            }

                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                        } else if (questionsModel1.getUserAnswer().equals("answer3")) {
                            iv_answer1.setVisibility(View.VISIBLE);
                            iv_answer2.setVisibility(View.VISIBLE);
                            iv_answer3.setVisibility(View.VISIBLE);
                            iv_answer4.setVisibility(View.VISIBLE);
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                iv_answer1.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer1.setImageResource(R.drawable.wrong);
                            }
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                iv_answer2.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer2.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer3.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer3.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer4.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer4.setImageResource(R.drawable.wrong);
                            }

                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                        } else if (questionsModel1.getUserAnswer().equals("answer4")) {
                            iv_answer1.setVisibility(View.VISIBLE);
                            iv_answer2.setVisibility(View.VISIBLE);
                            iv_answer3.setVisibility(View.VISIBLE);
                            iv_answer4.setVisibility(View.VISIBLE);
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                iv_answer1.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer1.setImageResource(R.drawable.wrong);
                            }
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                iv_answer2.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer2.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer3.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer3.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer4.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer4.setImageResource(R.drawable.wrong);
                            }

                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                        } else {

                           /* iv_answer1.setVisibility(View.GONE);
                            iv_answer2.setVisibility(View.GONE);
                            iv_answer3.setVisibility(View.GONE);
                            iv_answer4.setVisibility(View.GONE);

                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));*/
                        }
                        Glide.with(getApplicationContext())
                                .load(questionsModel1.getImage())
                                .into(iv_question);

                    }
                } else {

                    if (position1 >= 0) {
                        tv_next.setText("NEXT");
                        questionsModel1 = questionsModelList.get(position1);
                        tv_question_number.setText(String.valueOf(position1 + 1) + " / " + String.valueOf(questionsModelList.size()));
                        tv_question.setText(questionsModel1.getQuestion());
                        tv_answer1.setText(questionsModel1.getAnswer1());
                        tv_answer2.setText(questionsModel1.getAnswer2());
                        tv_answer3.setText(questionsModel1.getAnswer3());
                        tv_answer4.setText(questionsModel1.getAnswer4());
                        if (questionsModel1.getUserAnswer().equals("answer1")) {
                            iv_answer1.setVisibility(View.VISIBLE);
                            iv_answer2.setVisibility(View.VISIBLE);
                            iv_answer3.setVisibility(View.VISIBLE);
                            iv_answer4.setVisibility(View.VISIBLE);
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                iv_answer1.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer1.setImageResource(R.drawable.wrong);
                            }
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer2.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer2.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer3.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer3.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer4.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer4.setImageResource(R.drawable.wrong);
                            }

                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                        } else if (questionsModel1.getUserAnswer().equals("answer2")) {
                            iv_answer1.setVisibility(View.VISIBLE);
                            iv_answer2.setVisibility(View.VISIBLE);
                            iv_answer3.setVisibility(View.VISIBLE);
                            iv_answer4.setVisibility(View.VISIBLE);
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                iv_answer1.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer1.setImageResource(R.drawable.wrong);
                            }
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                iv_answer2.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer2.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer3.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer3.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer4.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer4.setImageResource(R.drawable.wrong);
                            }

                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                        } else if (questionsModel1.getUserAnswer().equals("answer3")) {
                            iv_answer1.setVisibility(View.VISIBLE);
                            iv_answer2.setVisibility(View.VISIBLE);
                            iv_answer3.setVisibility(View.VISIBLE);
                            iv_answer4.setVisibility(View.VISIBLE);
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                iv_answer1.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer1.setImageResource(R.drawable.wrong);
                            }
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                iv_answer2.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer2.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer3.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer3.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer4.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer4.setImageResource(R.drawable.wrong);
                            }

                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                        } else if (questionsModel1.getUserAnswer().equals("answer4")) {
                            iv_answer1.setVisibility(View.VISIBLE);
                            iv_answer2.setVisibility(View.VISIBLE);
                            iv_answer3.setVisibility(View.VISIBLE);
                            iv_answer4.setVisibility(View.VISIBLE);
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                iv_answer1.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer1.setImageResource(R.drawable.wrong);
                            }
                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                iv_answer2.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer2.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer3.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer3.setImageResource(R.drawable.wrong);
                            }

                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                iv_answer4.setImageResource(R.drawable.correct);
                            } else {
                                iv_answer4.setImageResource(R.drawable.wrong);
                            }

                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                        } else {

                            iv_answer1.setVisibility(View.GONE);
                            iv_answer2.setVisibility(View.GONE);
                            iv_answer3.setVisibility(View.GONE);
                            iv_answer4.setVisibility(View.GONE);

                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                        }
                        Glide.with(getApplicationContext())
                                .load(questionsModel1.getImage())
                                .into(iv_question);

                    }
                }

            }
        });


        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_next.getText().toString().equals("NEXT")) {

                    if(answer.length()>0){
                        updateAnswers();
                    }else {
                        int incrpos=position1+1;
                        if (questionsModelList.get(incrpos).getUseranswered().length() > 0) {
                            position1 = position1 + 1;
                            tv_question_number.setText(String.valueOf(position1 + 1) + " / " + String.valueOf(questionsModelList.size()));
                            userAnserMethod();
                            tv_previous.setVisibility(View.VISIBLE);
                            if (position1 < questionsModelList.size()) {
                                if (position1 == questionsModelList.size() - 1) {
                                    tv_next.setText("SUBMIT");
                                }

                                tv_question_number.setText(String.valueOf(position1 + 1) + " / " + String.valueOf(questionsModelList.size()));
                                questionsModel1 = questionsModelList.get(position1);
                                questionsModel1.setTotalNoOfQuestions(String.valueOf(questionsModelList.size()));
                                questionsModel1.setBlockID(questionsModel1.getCategoryID());
                                questionsModel1.setQuestionID(questionsModel1.getId());
                                questionsModel1.setCategoryType(type);
                                tv_question.setText(questionsModel1.getQuestion());
                                tv_answer1.setText(questionsModel1.getAnswer1());
                                tv_answer2.setText(questionsModel1.getAnswer2());
                                tv_answer3.setText(questionsModel1.getAnswer3());
                                tv_answer4.setText(questionsModel1.getAnswer4());
                                if (questionsModel1.getUserAnswer().equals("answer1")) {
                                    iv_answer1.setVisibility(View.VISIBLE);
                                    iv_answer2.setVisibility(View.VISIBLE);
                                    iv_answer3.setVisibility(View.VISIBLE);
                                    iv_answer4.setVisibility(View.VISIBLE);
                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                        iv_answer1.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer1.setImageResource(R.drawable.wrong);
                                    }
                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                        iv_answer2.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer2.setImageResource(R.drawable.wrong);
                                    }

                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                        iv_answer3.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer3.setImageResource(R.drawable.wrong);
                                    }

                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                        iv_answer4.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer4.setImageResource(R.drawable.wrong);
                                    }

                                    layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                } else if (questionsModel1.getUserAnswer().equals("answer2")) {
                                    iv_answer1.setVisibility(View.VISIBLE);
                                    iv_answer2.setVisibility(View.VISIBLE);
                                    iv_answer3.setVisibility(View.VISIBLE);
                                    iv_answer4.setVisibility(View.VISIBLE);
                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                        iv_answer1.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer1.setImageResource(R.drawable.wrong);
                                    }
                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                        iv_answer2.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer2.setImageResource(R.drawable.wrong);
                                    }

                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                        iv_answer3.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer3.setImageResource(R.drawable.wrong);
                                    }

                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                        iv_answer4.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer4.setImageResource(R.drawable.wrong);
                                    }

                                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                    layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                } else if (questionsModel1.getUserAnswer().equals("answer3")) {
                                    iv_answer1.setVisibility(View.VISIBLE);
                                    iv_answer2.setVisibility(View.VISIBLE);
                                    iv_answer3.setVisibility(View.VISIBLE);
                                    iv_answer4.setVisibility(View.VISIBLE);
                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                        iv_answer1.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer1.setImageResource(R.drawable.wrong);
                                    }
                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                        iv_answer2.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer2.setImageResource(R.drawable.wrong);
                                    }

                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                        iv_answer3.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer3.setImageResource(R.drawable.wrong);
                                    }

                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                        iv_answer4.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer4.setImageResource(R.drawable.wrong);
                                    }

                                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                    layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                } else if (questionsModel1.getUserAnswer().equals("answer4")) {
                                    iv_answer1.setVisibility(View.VISIBLE);
                                    iv_answer2.setVisibility(View.VISIBLE);
                                    iv_answer3.setVisibility(View.VISIBLE);
                                    iv_answer4.setVisibility(View.VISIBLE);
                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                        iv_answer1.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer1.setImageResource(R.drawable.wrong);
                                    }
                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                        iv_answer2.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer2.setImageResource(R.drawable.wrong);
                                    }

                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                        iv_answer3.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer3.setImageResource(R.drawable.wrong);
                                    }

                                    if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                        iv_answer4.setImageResource(R.drawable.correct);
                                    } else {
                                        iv_answer4.setImageResource(R.drawable.wrong);
                                    }

                                    layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                    layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                } /*else {
                                iv_answer1.setVisibility(View.GONE);
                                iv_answer2.setVisibility(View.GONE);
                                iv_answer3.setVisibility(View.GONE);
                                iv_answer4.setVisibility(View.GONE);
                                layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                            }*/
                                Glide.with(getApplicationContext())
                                        .load(questionsModel1.getImage())
                                        .into(iv_question);


                            }
                        }
                        else {
                            liner1=0;
                            liner2=0;
                            liner3=0;
                            liner4=0;

                            tv_previous.setVisibility(View.VISIBLE);
                            if(answer.equals("")){
                                tv_question_number.setText(String.valueOf(position1 + 1) + " / " + String.valueOf(questionsModelList.size()));
                                position1 = position1 + 1;
                                if (position1 < questionsModelList.size()) {
                                    if (position1 == questionsModelList.size() - 1) {
                                        tv_next.setText("SUBMIT");
                                    }

                                    tv_question_number.setText(String.valueOf(position1 + 1) + " / " + String.valueOf(questionsModelList.size()));
                                    questionsModel1 = questionsModelList.get(position1);
                                    questionsModel1.setTotalNoOfQuestions(String.valueOf(questionsModelList.size()));
                                    questionsModel1.setBlockID(questionsModel1.getCategoryID());
                                    questionsModel1.setQuestionID(questionsModel1.getId());
                                    questionsModel1.setCategoryType(type);
                                    tv_question.setText(questionsModel1.getQuestion());
                                    tv_answer1.setText(questionsModel1.getAnswer1());
                                    tv_answer2.setText(questionsModel1.getAnswer2());
                                    tv_answer3.setText(questionsModel1.getAnswer3());
                                    tv_answer4.setText(questionsModel1.getAnswer4());
                                    if (questionsModel1.getUserAnswer().equals("answer1")) {
                                        iv_answer1.setVisibility(View.VISIBLE);
                                        iv_answer2.setVisibility(View.VISIBLE);
                                        iv_answer3.setVisibility(View.VISIBLE);
                                        iv_answer4.setVisibility(View.VISIBLE);
                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                            iv_answer1.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer1.setImageResource(R.drawable.wrong);
                                        }
                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                            iv_answer2.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer2.setImageResource(R.drawable.wrong);
                                        }

                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                            iv_answer3.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer3.setImageResource(R.drawable.wrong);
                                        }

                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                            iv_answer4.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer4.setImageResource(R.drawable.wrong);
                                        }

                                        layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                        layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    } else if (questionsModel1.getUserAnswer().equals("answer2")) {
                                        iv_answer1.setVisibility(View.VISIBLE);
                                        iv_answer2.setVisibility(View.VISIBLE);
                                        iv_answer3.setVisibility(View.VISIBLE);
                                        iv_answer4.setVisibility(View.VISIBLE);
                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                            iv_answer1.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer1.setImageResource(R.drawable.wrong);
                                        }
                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                            iv_answer2.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer2.setImageResource(R.drawable.wrong);
                                        }

                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                            iv_answer3.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer3.setImageResource(R.drawable.wrong);
                                        }

                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                            iv_answer4.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer4.setImageResource(R.drawable.wrong);
                                        }

                                        layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                        layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    } else if (questionsModel1.getUserAnswer().equals("answer3")) {
                                        iv_answer1.setVisibility(View.VISIBLE);
                                        iv_answer2.setVisibility(View.VISIBLE);
                                        iv_answer3.setVisibility(View.VISIBLE);
                                        iv_answer4.setVisibility(View.VISIBLE);
                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                            iv_answer1.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer1.setImageResource(R.drawable.wrong);
                                        }
                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                            iv_answer2.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer2.setImageResource(R.drawable.wrong);
                                        }

                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                            iv_answer3.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer3.setImageResource(R.drawable.wrong);
                                        }

                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                            iv_answer4.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer4.setImageResource(R.drawable.wrong);
                                        }

                                        layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                        layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    } else if (questionsModel1.getUserAnswer().equals("answer4")) {
                                        iv_answer1.setVisibility(View.VISIBLE);
                                        iv_answer2.setVisibility(View.VISIBLE);
                                        iv_answer3.setVisibility(View.VISIBLE);
                                        iv_answer4.setVisibility(View.VISIBLE);
                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                            iv_answer1.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer1.setImageResource(R.drawable.wrong);
                                        }
                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                            iv_answer2.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer2.setImageResource(R.drawable.wrong);
                                        }

                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                            iv_answer3.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer3.setImageResource(R.drawable.wrong);
                                        }

                                        if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                            iv_answer4.setImageResource(R.drawable.correct);
                                        } else {
                                            iv_answer4.setImageResource(R.drawable.wrong);
                                        }

                                        layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                        layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    } else {
                                        iv_answer1.setVisibility(View.GONE);
                                        iv_answer2.setVisibility(View.GONE);
                                        iv_answer3.setVisibility(View.GONE);
                                        iv_answer4.setVisibility(View.GONE);
                                        layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                    }
                                    Glide.with(getApplicationContext())
                                            .load(questionsModel1.getImage())
                                            .into(iv_question);


                                }

                            }else {
                                updateAnswers();

                            }


                        }
                    }

                } else {
                    if(answer.equals("")){
                        Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                        i.putExtra("questionModelList", (Serializable) questionsModelList);
                        i.putExtra("ScreenFrom", fromScreen);
                        i.putExtra("CategoryID", categoryID);
                        i.putExtra("blockID", CategoryIDD);
                        i.putExtra("type", type);
                        startActivity(i);
                    }else {
                        questionsModel1 = questionsModelList.get(position1);
                        updateAnswersFinalUpdate();
                    }


                }

            }
        });


        if (fromScreen.equals("Learning")) {
            getChapters();
        }
        if (fromScreen.equals("Rules")) {
            getRulesQuestions();
        }
        if (fromScreen.equals("Practice")) {
            getPracticeQuestions();
        }
        if (fromScreen.equals("Theory")) {
            getTheroyQuestions();
        }


    }

    public void updateAnswers() {
        final ProgressDialog pDialog = new ProgressDialog(LearningQuestionsActivity.this);
        pDialog.setMessage("Updating..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(LearningQuestionsActivity.this);
        requestQueue.getCache().clear();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.mainUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response", response);

                try {

                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.has("response")) {

                        JSONArray responseArray = jsonObject.getJSONArray("response");

                        if (responseArray.length() > 0) {
                            JSONObject object = responseArray.getJSONObject(0);
                            if (object.has("status")) {
                                String status = object.getString("status");
                                if (status.equals("success")) {
                                    liner1=0;
                                    liner2=0;
                                    liner3=0;
                                    liner4=0;
                                   position1 =position1+1;
                                    answer="";
                                    if (position1 < questionsModelList.size()) {
                                        if (position1 == questionsModelList.size() - 1) {
                                            tv_next.setText("SUBMIT");
                                        }

                                        tv_question_number.setText(String.valueOf(position1 + 1) + " / " + String.valueOf(questionsModelList.size()));
                                        questionsModel1 = questionsModelList.get(position1);
                                        questionsModel1.setTotalNoOfQuestions(String.valueOf(questionsModelList.size()));
                                        questionsModel1.setBlockID(questionsModel1.getCategoryID());
                                        questionsModel1.setQuestionID(questionsModel1.getId());
                                        questionsModel1.setCategoryType(type);
                                        tv_question.setText(questionsModel1.getQuestion());
                                        tv_answer1.setText(questionsModel1.getAnswer1());
                                        tv_answer2.setText(questionsModel1.getAnswer2());
                                        tv_answer3.setText(questionsModel1.getAnswer3());
                                        tv_answer4.setText(questionsModel1.getAnswer4());
                                        if (questionsModel1.getUserAnswer().equals("answer1")) {
                                            iv_answer1.setVisibility(View.VISIBLE);
                                            iv_answer2.setVisibility(View.VISIBLE);
                                            iv_answer3.setVisibility(View.VISIBLE);
                                            iv_answer4.setVisibility(View.VISIBLE);
                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                                iv_answer1.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer1.setImageResource(R.drawable.wrong);
                                            }
                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                                iv_answer2.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer2.setImageResource(R.drawable.wrong);
                                            }

                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                                iv_answer3.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer3.setImageResource(R.drawable.wrong);
                                            }

                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                                iv_answer4.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer4.setImageResource(R.drawable.wrong);
                                            }

                                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        } else if (questionsModel1.getUserAnswer().equals("answer2")) {
                                            iv_answer1.setVisibility(View.VISIBLE);
                                            iv_answer2.setVisibility(View.VISIBLE);
                                            iv_answer3.setVisibility(View.VISIBLE);
                                            iv_answer4.setVisibility(View.VISIBLE);
                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                                iv_answer1.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer1.setImageResource(R.drawable.wrong);
                                            }
                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                                iv_answer2.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer2.setImageResource(R.drawable.wrong);
                                            }

                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                                iv_answer3.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer3.setImageResource(R.drawable.wrong);
                                            }

                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                                iv_answer4.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer4.setImageResource(R.drawable.wrong);
                                            }

                                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        } else if (questionsModel1.getUserAnswer().equals("answer3")) {
                                            iv_answer1.setVisibility(View.VISIBLE);
                                            iv_answer2.setVisibility(View.VISIBLE);
                                            iv_answer3.setVisibility(View.VISIBLE);
                                            iv_answer4.setVisibility(View.VISIBLE);
                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                                iv_answer1.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer1.setImageResource(R.drawable.wrong);
                                            }
                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                                iv_answer2.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer2.setImageResource(R.drawable.wrong);
                                            }

                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                                iv_answer3.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer3.setImageResource(R.drawable.wrong);
                                            }

                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                                iv_answer4.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer4.setImageResource(R.drawable.wrong);
                                            }

                                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        } else if (questionsModel1.getUserAnswer().equals("answer4")) {
                                            iv_answer1.setVisibility(View.VISIBLE);
                                            iv_answer2.setVisibility(View.VISIBLE);
                                            iv_answer3.setVisibility(View.VISIBLE);
                                            iv_answer4.setVisibility(View.VISIBLE);
                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                                                iv_answer1.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer1.setImageResource(R.drawable.wrong);
                                            }
                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                                                iv_answer2.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer2.setImageResource(R.drawable.wrong);
                                            }

                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                                iv_answer3.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer3.setImageResource(R.drawable.wrong);
                                            }

                                            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                                                iv_answer4.setImageResource(R.drawable.correct);
                                            } else {
                                                iv_answer4.setImageResource(R.drawable.wrong);
                                            }

                                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        } else {
                                            iv_answer1.setVisibility(View.GONE);
                                            iv_answer2.setVisibility(View.GONE);
                                            iv_answer3.setVisibility(View.GONE);
                                            iv_answer4.setVisibility(View.GONE);
                                            layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                            layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                                        }
                                        Glide.with(getApplicationContext())
                                                .load(questionsModel1.getImage())
                                                .into(iv_question);


                                    }
                                } else {
                                    showAlertDialog(object.getString("message"));
                                }
                            } else {
                                showAlertDialog(object.getString("message"));
                            }
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                pDialog.dismiss();
                String errorMessage = StringConstants.ErrorMessage(error);

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("method", "add_block_result");
                MyData.put("token", token);
                MyData.put("category_id", categoryID);
                MyData.put("type", type);
                MyData.put("block_id", questionsModel1.getBlockID());
                MyData.put("language_id", language_id);
                MyData.put("user_id", userID);
                MyData.put("question_id", questionsModel1.getId());
              //  MyData.put("answer", questionsModel1.getUserAnswer());
                MyData.put("answer", answer);
                MyData.put("datetime", currentDate + " " + currentTime);
                Log.i("ResulrtPOst", MyData.toString());
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }
    public void updateAnswersFinalUpdate() {
        final ProgressDialog pDialog = new ProgressDialog(LearningQuestionsActivity.this);
        pDialog.setMessage("Updating..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(LearningQuestionsActivity.this);
        requestQueue.getCache().clear();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.mainUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response", response);

                try {

                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.has("response")) {

                        JSONArray responseArray = jsonObject.getJSONArray("response");

                        if (responseArray.length() > 0) {
                            JSONObject object = responseArray.getJSONObject(0);
                            if (object.has("status")) {
                                String status = object.getString("status");
                                if (status.equals("success")) {
                                    Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                                    i.putExtra("questionModelList", (Serializable) questionsModelList);
                                    i.putExtra("ScreenFrom", fromScreen);
                                    i.putExtra("CategoryID", categoryID);
                                    i.putExtra("blockID", CategoryIDD);
                                    i.putExtra("type", type);
                                    startActivity(i);

                                } else {
                                    showAlertDialog(object.getString("message"));
                                }
                            } else {
                                showAlertDialog(object.getString("message"));
                            }
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                pDialog.dismiss();
                String errorMessage = StringConstants.ErrorMessage(error);

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("method", "add_block_result");
                MyData.put("token", token);
                MyData.put("category_id", categoryID);
                MyData.put("type", type);
                MyData.put("block_id", questionsModel1.getBlockID());
                MyData.put("language_id", language_id);
                MyData.put("user_id", userID);
                MyData.put("question_id", questionsModel1.getId());
                //  MyData.put("answer", questionsModel1.getUserAnswer());
                MyData.put("answer", answer);
                MyData.put("datetime", currentDate + " " + currentTime);
                Log.i("ResulrtPOst", MyData.toString());
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void getChapters() {
        final ProgressDialog pDialog = new ProgressDialog(LearningQuestionsActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(LearningQuestionsActivity.this);
        requestQueue.getCache().clear();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.mainUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response", response);

                try {

                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.has("response")) {

                        JSONArray responseArray = jsonObject.getJSONArray("response");

                        if (responseArray.length() > 0) {
                            JSONObject object = responseArray.getJSONObject(0);
                            if (object.has("status")) {
                                String status = object.getString("status");
                                if (status.equals("success")) {
                                    JSONArray array = object.getJSONArray("block_question_details");
                                    questionsModelList = new ArrayList<>();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject1 = array.getJSONObject(i);
                                        questionsModel = new QuestionsModel();
                                        questionsModel.setId(jsonObject1.getString("id"));
                                        questionsModel.setCategoryID(jsonObject1.getString("block_id"));
                                        questionsModel.setBlockID(jsonObject1.getString("block_id"));
                                        questionsModel.setImage(jsonObject1.getString("image"));
                                        questionsModel.setCorrectAnswer(jsonObject1.getString("correct_answer"));
                                        questionsModel.setIsAnswered("no");
                                        questionsModel.setUserAnswer("");
                                        questionsModel.setQuestion(jsonObject1.getString("question"));
                                        questionsModel.setAnswer1(jsonObject1.getString("answer1"));
                                        questionsModel.setAnswer2(jsonObject1.getString("answer2"));
                                        questionsModel.setAnswer3(jsonObject1.getString("answer3"));
                                        questionsModel.setAnswer4(jsonObject1.getString("answer4"));
                                        questionsModel.setUseranswered(jsonObject1.getString("useranswered"));

                                        questionsModelList.add(questionsModel);
                                    }

                                    if (questionsModelList.size() > 0) {

                                        tv_question.setVisibility(View.VISIBLE);
                                        layout_answer1.setVisibility(View.VISIBLE);
                                        layout_answer2.setVisibility(View.VISIBLE);
                                        layout_answer3.setVisibility(View.VISIBLE);
                                        layout_answer4.setVisibility(View.VISIBLE);
                                        tv_question_number.setVisibility(View.VISIBLE);
                                        iv_question.setVisibility(View.VISIBLE);
                                        tv_next.setVisibility(View.VISIBLE);
                                        questionsModel1 = questionsModelList.get(0);
                                        tv_question.setText(questionsModel1.getQuestion());
                                        tv_answer1.setText(questionsModel1.getAnswer1());
                                        tv_answer2.setText(questionsModel1.getAnswer2());
                                        tv_answer3.setText(questionsModel1.getAnswer3());
                                        tv_answer4.setText(questionsModel1.getAnswer4());
                                        tv_question_number.setText("1 / " + String.valueOf(questionsModelList.size()));
                                        Glide.with(getApplicationContext())
                                                .load(questionsModel1.getImage())
                                                .into(iv_question);

                                    } else {
                                        tv_question.setVisibility(View.GONE);
                                        layout_answer1.setVisibility(View.GONE);
                                        layout_answer2.setVisibility(View.GONE);
                                        layout_answer3.setVisibility(View.GONE);
                                        layout_answer4.setVisibility(View.GONE);
                                        tv_question_number.setText(R.string.app_name);
                                        iv_question.setVisibility(View.GONE);
                                        tv_next.setVisibility(View.GONE);
                                    }

                                    if (questionsModelList.size() == 1) {
                                        tv_next.setText("SUBMIT");
                                    }

                                    notificationAdapter = new ChapterAdapter(LearningQuestionsActivity.this, questionsModelList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                                            getApplicationContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                    );
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setAdapter(notificationAdapter);
                                    userAnserMethod();

                                } else {
                                    showAlertDialog(object.getString("message"));
                                }
                            } else {
                                showAlertDialog(object.getString("message"));
                            }
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                pDialog.dismiss();
                String errorMessage = StringConstants.ErrorMessage(error);

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("method", "block_questions");
                MyData.put("token", token);
                MyData.put("block_id", CategoryIDD);
                MyData.put("type", type);
                MyData.put("user_id", userID);
                MyData.put("reg_datetime", currentDate + " " + currentTime);
                Log.i("BlockQuestion", MyData.toString());
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void getRulesQuestions() {
        final ProgressDialog pDialog = new ProgressDialog(LearningQuestionsActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(LearningQuestionsActivity.this);
        requestQueue.getCache().clear();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.mainUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response", response);

                try {

                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.has("response")) {

                        JSONArray responseArray = jsonObject.getJSONArray("response");

                        if (responseArray.length() > 0) {
                            JSONObject object = responseArray.getJSONObject(0);
                            if (object.has("status")) {
                                String status = object.getString("status");
                                if (status.equals("success")) {
                                    JSONArray array = object.getJSONArray("rules_que_details");
                                    questionsModelList = new ArrayList<>();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject1 = array.getJSONObject(i);
                                        questionsModel = new QuestionsModel();
                                        questionsModel.setId(jsonObject1.getString("id"));
                                        questionsModel.setCategoryID(jsonObject1.getString("category_id"));
                                        questionsModel.setImage(jsonObject1.getString("image"));
                                        questionsModel.setIsAnswered("no");
                                        questionsModel.setUserAnswer("");
                                        questionsModel.setCorrectAnswer(jsonObject1.getString("correct_answer"));
                                        questionsModel.setQuestion(jsonObject1.getString("question"));
                                        questionsModel.setAnswer1(jsonObject1.getString("answer1"));
                                        questionsModel.setAnswer2(jsonObject1.getString("answer2"));
                                        questionsModel.setAnswer3(jsonObject1.getString("answer3"));
                                        questionsModel.setAnswer4(jsonObject1.getString("answer4"));

                                        questionsModelList.add(questionsModel);
                                    }

                                    if (questionsModelList.size() > 0) {

                                        questionsModel1 = questionsModelList.get(0);
                                        tv_question.setText(questionsModel1.getQuestion());
                                        tv_answer1.setText(questionsModel1.getAnswer1());
                                        tv_answer2.setText(questionsModel1.getAnswer2());
                                        tv_answer3.setText(questionsModel1.getAnswer3());
                                        tv_answer4.setText(questionsModel1.getAnswer4());
                                        tv_question_number.setText("1 / " + String.valueOf(questionsModelList.size()));
                                        Glide.with(getApplicationContext())
                                                .load(questionsModel1.getImage())
                                                .into(iv_question);

                                    }

                                    if (questionsModelList.size() == 1) {
                                        tv_next.setText("SUBMIT");
                                    }

                                    notificationAdapter = new ChapterAdapter(LearningQuestionsActivity.this, questionsModelList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                                            getApplicationContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                    );
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setAdapter(notificationAdapter);

                                } else {
                                    showAlertDialog(object.getString("message"));
                                }
                            } else {
                                showAlertDialog(object.getString("message"));
                            }
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                pDialog.dismiss();
                String errorMessage = StringConstants.ErrorMessage(error);

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("method", "rules_questions_category_wise");
                MyData.put("token", token);
                MyData.put("category_id", categoryID);
                MyData.put("reg_datetime", currentDate + " " + currentTime);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void getPracticeQuestions() {
        final ProgressDialog pDialog = new ProgressDialog(LearningQuestionsActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(LearningQuestionsActivity.this);
        requestQueue.getCache().clear();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.mainUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response", response);

                try {

                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.has("response")) {

                        JSONArray responseArray = jsonObject.getJSONArray("response");

                        if (responseArray.length() > 0) {
                            JSONObject object = responseArray.getJSONObject(0);
                            if (object.has("status")) {
                                String status = object.getString("status");
                                if (status.equals("success")) {
                                    JSONArray array = object.getJSONArray("practice_que_details");
                                    questionsModelList = new ArrayList<>();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject1 = array.getJSONObject(i);
                                        questionsModel = new QuestionsModel();
                                        questionsModel.setId(jsonObject1.getString("id"));
                                        questionsModel.setCategoryID(jsonObject1.getString("category_id"));
                                        questionsModel.setImage(jsonObject1.getString("image"));
                                        questionsModel.setIsAnswered("no");
                                        questionsModel.setUserAnswer("");
                                        questionsModel.setCorrectAnswer(jsonObject1.getString("correct_answer"));
                                        questionsModel.setQuestion(jsonObject1.getString("question"));
                                        questionsModel.setAnswer1(jsonObject1.getString("answer1"));
                                        questionsModel.setAnswer2(jsonObject1.getString("answer2"));
                                        questionsModel.setAnswer3(jsonObject1.getString("answer3"));
                                        questionsModel.setAnswer4(jsonObject1.getString("answer4"));

                                        questionsModelList.add(questionsModel);
                                    }

                                    if (questionsModelList.size() > 0) {

                                        questionsModel1 = questionsModelList.get(0);
                                        tv_question.setText(questionsModel1.getQuestion());
                                        tv_answer1.setText(questionsModel1.getAnswer1());
                                        tv_answer2.setText(questionsModel1.getAnswer2());
                                        tv_answer3.setText(questionsModel1.getAnswer3());
                                        tv_answer4.setText(questionsModel1.getAnswer4());
                                        tv_question_number.setText("1 / " + String.valueOf(questionsModelList.size()));
                                        Glide.with(getApplicationContext())
                                                .load(questionsModel1.getImage())
                                                .into(iv_question);

                                    }
                                    if (questionsModelList.size() == 1) {
                                        tv_next.setText("SUBMIT");
                                    }


                                    notificationAdapter = new ChapterAdapter(LearningQuestionsActivity.this, questionsModelList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                                            getApplicationContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                    );
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setAdapter(notificationAdapter);

                                } else {
                                    showAlertDialog(object.getString("message"));
                                }
                            } else {
                                showAlertDialog(object.getString("message"));
                            }
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                pDialog.dismiss();
                String errorMessage = StringConstants.ErrorMessage(error);

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("method", "practice_questions_category_wise");
                MyData.put("token", token);
                MyData.put("category_id", categoryID);
                MyData.put("reg_datetime", currentDate + " " + currentTime);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void getTheroyQuestions() {
        final ProgressDialog pDialog = new ProgressDialog(LearningQuestionsActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(LearningQuestionsActivity.this);
        requestQueue.getCache().clear();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.mainUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response", response);

                try {

                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.has("response")) {

                        JSONArray responseArray = jsonObject.getJSONArray("response");

                        if (responseArray.length() > 0) {
                            JSONObject object = responseArray.getJSONObject(0);
                            if (object.has("status")) {
                                String status = object.getString("status");
                                if (status.equals("success")) {
                                    JSONArray array = object.getJSONArray("theories_que_details");
                                    questionsModelList = new ArrayList<>();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject1 = array.getJSONObject(i);
                                        questionsModel = new QuestionsModel();
                                        questionsModel.setId(jsonObject1.getString("id"));
                                        questionsModel.setCategoryID(jsonObject1.getString("category_id"));
                                        questionsModel.setImage(jsonObject1.getString("image"));
                                        questionsModel.setIsAnswered("no");
                                        questionsModel.setUserAnswer("");
                                        questionsModel.setCorrectAnswer(jsonObject1.getString("correct_answer"));
                                        questionsModel.setQuestion(jsonObject1.getString("question"));
                                        questionsModel.setAnswer1(jsonObject1.getString("answer1"));
                                        questionsModel.setAnswer2(jsonObject1.getString("answer2"));
                                        questionsModel.setAnswer3(jsonObject1.getString("answer3"));
                                        questionsModel.setAnswer4(jsonObject1.getString("answer4"));

                                        questionsModelList.add(questionsModel);
                                    }


                                    if (questionsModelList.size() > 0) {

                                        questionsModel1 = questionsModelList.get(0);
                                        tv_question.setText(questionsModel1.getQuestion());
                                        tv_answer1.setText(questionsModel1.getAnswer1());
                                        tv_answer2.setText(questionsModel1.getAnswer2());
                                        tv_answer3.setText(questionsModel1.getAnswer3());
                                        tv_answer4.setText(questionsModel1.getAnswer4());
                                        tv_question_number.setText("1 / " + String.valueOf(questionsModelList.size()));
                                        Glide.with(getApplicationContext())
                                                .load(questionsModel1.getImage())
                                                .into(iv_question);

                                    }

                                    if (questionsModelList.size() == 1) {
                                        tv_next.setText("SUBMIT");
                                    }


                                    notificationAdapter = new ChapterAdapter(LearningQuestionsActivity.this, questionsModelList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                                            getApplicationContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                    );
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setAdapter(notificationAdapter);

                                } else {
                                    showAlertDialog(object.getString("message"));
                                }
                            } else {
                                showAlertDialog(object.getString("message"));
                            }
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                pDialog.dismiss();
                String errorMessage = StringConstants.ErrorMessage(error);

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("method", "theories_questions_category_wise");
                MyData.put("token", token);
                MyData.put("category_id", categoryID);
                MyData.put("reg_datetime", currentDate + " " + currentTime);
                Log.i("theories_quest",MyData.toString());
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void showAlertDialog(String message) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LearningQuestionsActivity.this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("Auto Therie neu");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void backPressed(View view) {
        onBackPressed();
    }

    public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {

        private List<QuestionsModel> questionsModelList;


        Context context;
        int row_index = -1;
        String answer = "";
        int position1 = 0;

        public ChapterAdapter(Context context, List<QuestionsModel> questionsModelList) {
            this.questionsModelList = questionsModelList;
            this.context = context;


        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_questions_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
            final QuestionsModel questionsModel = questionsModelList.get(position);
            holder.tv_question.setText(questionsModel.getQuestion());
            holder.tv_answer1.setText(questionsModel.getAnswer1());
            holder.tv_answer2.setText(questionsModel.getAnswer2());
            holder.tv_answer3.setText(questionsModel.getAnswer3());
            holder.tv_answer4.setText(questionsModel.getAnswer4());
            holder.layout_answer1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    answer = "1";

                    holder.layout_answer1.setBackground(context.getResources().getDrawable(R.drawable.bbbbbb));
                    holder.layout_answer2.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                    holder.layout_answer3.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                    holder.layout_answer4.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                }
            });
            holder.layout_answer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    answer = "2";

                    holder.layout_answer2.setBackground(context.getResources().getDrawable(R.drawable.bbbbbb));
                    holder.layout_answer1.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                    holder.layout_answer3.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                    holder.layout_answer4.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                }
            });
            holder.layout_answer3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    answer = "3";

                    holder.layout_answer3.setBackground(context.getResources().getDrawable(R.drawable.bbbbbb));
                    holder.layout_answer1.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                    holder.layout_answer2.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                    holder.layout_answer4.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                }
            });
            holder.layout_answer4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    answer = "4";

                    holder.layout_answer4.setBackground(context.getResources().getDrawable(R.drawable.bbbbbb));
                    holder.layout_answer1.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                    holder.layout_answer2.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                    holder.layout_answer3.setBackground(context.getResources().getDrawable(R.drawable.groupbox));
                }
            });

            holder.tv_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    position1 = position1 - 1;

                    if (position1 > 0) {
                        final QuestionsModel questionsModel = questionsModelList.get(position1);
                        holder.tv_question.setText(questionsModel.getQuestion());
                        holder.tv_answer1.setText(questionsModel.getAnswer1());
                        holder.tv_answer2.setText(questionsModel.getAnswer2());
                        holder.tv_answer3.setText(questionsModel.getAnswer3());
                        holder.tv_answer4.setText(questionsModel.getAnswer4());
                    }
                }
            });
            holder.tv_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    position1 = position1 + 1;
                    if (position1 < questionsModelList.size() - 1) {
                        final QuestionsModel questionsModel = questionsModelList.get(position1);
                        holder.tv_question.setText(questionsModel.getQuestion());
                        holder.tv_answer1.setText(questionsModel.getAnswer1());
                        holder.tv_answer2.setText(questionsModel.getAnswer2());
                        holder.tv_answer3.setText(questionsModel.getAnswer3());
                        holder.tv_answer4.setText(questionsModel.getAnswer4());
                    }
                }
            });
            Glide.with(context)
                    .load(questionsModel.getImage())
                    .into(holder.iv_question);


        }

        @Override
        public int getItemCount() {
            return questionsModelList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_question, tv_answer1, tv_answer2, tv_answer3, tv_answer4;
            LinearLayout linearLayoutContainer;
            ImageView iv_question;
            LinearLayout layout_answer1, layout_answer2, layout_answer3, layout_answer4;
            TextView tv_previous, tv_next;

            public MyViewHolder(View view) {
                super(view);

                tv_question = (TextView) view.findViewById(R.id.text_question);
                tv_answer1 = (TextView) view.findViewById(R.id.text_answer1);
                tv_answer2 = (TextView) view.findViewById(R.id.text_answer2);
                tv_answer3 = (TextView) view.findViewById(R.id.text_answer3);
                tv_answer4 = (TextView) view.findViewById(R.id.text_answer4);
                iv_question = (ImageView) view.findViewById(R.id.image_question);
                layout_answer1 = (LinearLayout) view.findViewById(R.id.layout_answer1);
                layout_answer2 = (LinearLayout) view.findViewById(R.id.layout_answer2);
                layout_answer3 = (LinearLayout) view.findViewById(R.id.layout_answer3);
                layout_answer4 = (LinearLayout) view.findViewById(R.id.layout_answer4);

                tv_previous = (TextView) view.findViewById(R.id.text_previous);
                tv_next = (TextView) view.findViewById(R.id.text_next);

            }
        }

    }

    public void userAnserMethod() {
        String userAnswered = questionsModelList.get(position1).getUseranswered();
        String correctAnswer = questionsModelList.get(position1).getCorrectAnswer();
        if (userAnswered.length() > 0) {
            Log.i("djbdjfbdfd", userAnswered);
            if (questionsModelList.get(position1).getUseranswered().equals("answer1")) {
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
            }
            if (questionsModelList.get(position1).getUseranswered().equals("answer2")) {
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
            }
            if (questionsModelList.get(position1).getUseranswered().equals("answer3")) {
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
            }
            if (questionsModelList.get(position1).getUseranswered().equals("answer4")) {
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
            }
            if (questionsModelList.get(position1).getUseranswered().equals("answer1,answer2")||questionsModelList.get(position1).getUseranswered().equals("answer2,answer1")) {
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
            }

            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3")||questionsModelList.get(position1).getUseranswered().equals("answer3,answer1")) {
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
            }
            if (questionsModelList.get(position1).getUseranswered().equals("answer1,answer4")||questionsModelList.get(position1).getUseranswered().equals("answer4,answer1")) {
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
            } if (questionsModelList.get(position1).getUseranswered().equals("answer2,answer3")||questionsModelList.get(position1).getUseranswered().equals("answer3,answer2")) {
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
            } if (questionsModelList.get(position1).getUseranswered().equals("answer2,answer4")||questionsModelList.get(position1).getUseranswered().equals("answer4,answer2")) {
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
            } if (questionsModelList.get(position1).getUseranswered().equals("answer3,answer2")||questionsModelList.get(position1).getUseranswered().equals("answer3,answer2")) {
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.groupbox));
            } if (questionsModelList.get(position1).getUseranswered().equals("answer3,answer4")||questionsModelList.get(position1).getUseranswered().equals("answer4,answer3")) {
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
            }if (questionsModelList.get(position1).getUseranswered().equals("answer4,answer2")||questionsModelList.get(position1).getUseranswered().equals("answer2,answer4")) {
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
            }if (questionsModelList.get(position1).getUseranswered().equals("answer4,answer3")||questionsModelList.get(position1).getUseranswered().equals("answer3,answer4")) {
                layout_answer2.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
                layout_answer1.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer3.setBackground(getResources().getDrawable(R.drawable.groupbox));
                layout_answer4.setBackground(getResources().getDrawable(R.drawable.bbbbbb));
            }

            iv_answer1.setVisibility(View.VISIBLE);
            iv_answer2.setVisibility(View.VISIBLE);
            iv_answer3.setVisibility(View.VISIBLE);
            iv_answer4.setVisibility(View.VISIBLE);
            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer1") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4")) {
                iv_answer1.setImageResource(R.drawable.correct);
            } else {
                iv_answer1.setImageResource(R.drawable.wrong);
            }
            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer2") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4")) {
                iv_answer2.setImageResource(R.drawable.correct);
            } else {
                iv_answer2.setImageResource(R.drawable.wrong);
            }

            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer3") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                iv_answer3.setImageResource(R.drawable.correct);
            } else {
                iv_answer3.setImageResource(R.drawable.wrong);
            }

            if (questionsModelList.get(position1).getCorrectAnswer().equals("answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer1,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer2,answer4") || questionsModelList.get(position1).getCorrectAnswer().equals("answer3,answer4")) {
                iv_answer4.setImageResource(R.drawable.correct);
            } else {
                iv_answer4.setImageResource(R.drawable.wrong);
            }


        }


    }


}
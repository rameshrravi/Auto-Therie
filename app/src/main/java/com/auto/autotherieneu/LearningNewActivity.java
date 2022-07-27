package com.auto.autotherieneu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LearningNewActivity extends AppCompatActivity {
    String token;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    String languageID="3";
    TextView tv_theory_questions_text,tv_theory_questions_mixed_text,tv_old_theory_questions_text;
    TextView tv_text_questions,tv_situation_questions,tv_photo_questions,tv_marked_questions,tv_random_questions,tv_old_theory_questions,tv_questions_without_picture,tv_signal_questions;
    LinearLayout layout_text_questions,layout_situation_questions,layout_photo_questions,layout_marked_questions,layout_random_questions,layout_old_theory_questions,layout_questions_without_pictures,layout_signal_questions;
    String textQuestionsID,situationQuestionsID,photoQuestionsID,markedQuestionsID,randomQuestionsID,oldTheoryQuestionsID,questionsWithoutPicturesID,signalQuestionsID;
    TextView text_questions_text,situation_questions_text,photo_questions_text,common_questions_text,old_theory_questions_text,questions_without_picture_text,signal_questions_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_new);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        token = preferences.getString(StringConstants.prefToken,"");
        languageID = preferences.getString(StringConstants.prefLanguageID,"3");


        tv_text_questions=findViewById(R.id.text_text_questions);
        tv_situation_questions=findViewById(R.id.text_situation_questions);
        tv_photo_questions=findViewById(R.id.text_photo_questions);
        tv_marked_questions=findViewById(R.id.text_marked_questions);
        tv_random_questions=findViewById(R.id.text_random_questions);
        tv_old_theory_questions=findViewById(R.id.text_old_theory_questions);
        tv_questions_without_picture=findViewById(R.id.text_questions_without_pictures);
        tv_signal_questions=findViewById(R.id.text_signal_questions);

        text_questions_text=findViewById(R.id.text_questions_text);
        situation_questions_text=findViewById(R.id.situation_questions_text);
        photo_questions_text=findViewById(R.id.photo_questions_text);
        common_questions_text=findViewById(R.id.common_questions_text);
        old_theory_questions_text=findViewById(R.id.old_theory_questions_text);
       questions_without_picture_text=findViewById(R.id.questions_without_picture_text);
        signal_questions_text=findViewById(R.id.signal_questions_text);

        layout_text_questions=findViewById(R.id.layout_text_questions);
        layout_situation_questions=findViewById(R.id.layout_situation_questions);
        layout_photo_questions=findViewById(R.id.layout_photo_questions);
        layout_marked_questions=findViewById(R.id.layout_marked_questions);
        layout_random_questions=findViewById(R.id.layout_random_questions);
        layout_old_theory_questions=findViewById(R.id.layout_old_theory_questions);
        layout_questions_without_pictures=findViewById(R.id.layout_questions_without_picture);
        layout_signal_questions=findViewById(R.id.layout_signal_questions);

        tv_theory_questions_text = findViewById(R.id.text_theory_questions_text);
        tv_theory_questions_mixed_text = findViewById(R.id.text_theory_questions_mixed_text);
        tv_old_theory_questions_text = findViewById(R.id.text_old_theory_questions_text);

        if(languageID.equals("1")){
            tv_theory_questions_text.setText("Theoriefragen");
            tv_theory_questions_mixed_text.setText("Theoriefragen gemischt");
            tv_old_theory_questions_text.setText("Ältere Theoriefragen");

            text_questions_text.setText("Textfragen");
            situation_questions_text.setText("Situationsfragen");
            photo_questions_text.setText("Fragen zum Foto");
            common_questions_text.setText("Häufige Fragen");
            old_theory_questions_text.setText("Fragen der alten Theorie");
            questions_without_picture_text.setText("Fragen ohne Bilder");
            signal_questions_text.setText("Fragen signalisieren");
        }
        if(languageID.equals("2")){
            tv_theory_questions_text.setText("Questions Theoriques");
            tv_theory_questions_mixed_text.setText("Questions Theoriques Mixtes");
            tv_old_theory_questions_text.setText("Vieilles Questions Theoriques");

            text_questions_text.setText("Questions textuelles");
            situation_questions_text.setText("Questions de situation");
            photo_questions_text.setText("Questions sur les photos");
            common_questions_text.setText("Questions courantes");
            old_theory_questions_text.setText("Vieilles questions théoriques");
            questions_without_picture_text.setText("Questions sans images");
            signal_questions_text.setText("Signaler des questions");
        }
        if(languageID.equals("3")){
            tv_theory_questions_text.setText("Theory Questions");
            tv_theory_questions_mixed_text.setText("Mixed Theory Questions");
            tv_old_theory_questions_text.setText("Old Theory Questions");

            text_questions_text.setText("Text Questions");
            situation_questions_text.setText("Situation Questions");
            photo_questions_text.setText("Photo Questions");
            common_questions_text.setText("Common Questions");
            old_theory_questions_text.setText("Old theory questions");
            questions_without_picture_text.setText("Questions without pictures");
            signal_questions_text.setText("Signals questions");
        }

        if(languageID.equals("4")){
            tv_theory_questions_text.setText("Domande Di Teoria");
            tv_theory_questions_mixed_text.setText("Domande Di Teoria Mista");
            tv_old_theory_questions_text.setText("Domande Di Vecchia Teoria");

            text_questions_text.setText("Domande di testo");
            situation_questions_text.setText("Domande sulla situazione");
            photo_questions_text.setText("Domande sulla foto");
            common_questions_text.setText("Domande comuni");
            old_theory_questions_text.setText("Domande di vecchia teoria");
            questions_without_picture_text.setText("Domande senza immagini");
            signal_questions_text.setText("Domande di segnalazione");
        }

        layout_text_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),LearningBlocksActivity.class);
                i.putExtra("CategoryID",textQuestionsID);
                i.putExtra("Category","Text Questions");
                i.putExtra("Type","asa");
                startActivity(i);
            }
        });
        layout_situation_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),LearningBlocksActivity.class);
                i.putExtra("CategoryID",situationQuestionsID);
                i.putExtra("Category","Situation Questions");
                i.putExtra("Type","asa");
                startActivity(i);
            }
        });
        layout_photo_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),LearningBlocksActivity.class);
                i.putExtra("CategoryID",photoQuestionsID);
                i.putExtra("Category","Photo Questions");
                i.putExtra("Type","asa");
                startActivity(i);
            }
        });
        layout_marked_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),LearningBlocksActivity.class);
                i.putExtra("CategoryID",markedQuestionsID);
                i.putExtra("Category","Common Questions");
                i.putExtra("Type","mixed");
                startActivity(i);
            }
        });
        layout_old_theory_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),LearningBlocksActivity.class);
                i.putExtra("CategoryID",oldTheoryQuestionsID);
                i.putExtra("Category","Old Theory Questions");
                i.putExtra("Type","older");
                startActivity(i);
            }
        });
        layout_questions_without_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),LearningBlocksActivity.class);
                i.putExtra("CategoryID",questionsWithoutPicturesID);
                i.putExtra("Category","Questions Without Pictures");
                i.putExtra("Type","older");
                startActivity(i);
            }
        });
        layout_signal_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),LearningBlocksActivity.class);
                i.putExtra("CategoryID",signalQuestionsID);
                i.putExtra("Category","Signal Questions");
                i.putExtra("Type","older");
                startActivity(i);
            }
        });


        getChapters();


    }
    public void getChapters(){
        final ProgressDialog pDialog=new ProgressDialog(LearningNewActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(LearningNewActivity.this);
        requestQueue.getCache().clear();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.mainUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response",response);

                try {

                    JSONObject jsonObject=new JSONObject(response.trim());
                    if(jsonObject.has("response")){

                        JSONArray responseArray=jsonObject.getJSONArray("response");

                        if(responseArray.length()>0){
                            JSONObject object=responseArray.getJSONObject(0);
                            if(object.has("status")){
                                String status = object.getString("status");
                                if(status.equals("success")){
                                   tv_text_questions.setText(object.getString("test_question"));
                                   tv_situation_questions.setText(object.getString("situation_question"));
                                   tv_photo_questions.setText(object.getString("photo_question"));
                                   tv_marked_questions.setText(object.getString("qman_question"));
//                                   tv_random_questions.setText(object.getString("random_questions"));
                                   tv_old_theory_questions.setText(object.getString("old_asa_theory_questions"));
                                   tv_questions_without_picture.setText(object.getString("without_pic_theory_questions"));
                                   tv_signal_questions.setText(object.getString("signal_theory_questions"));

                                    textQuestionsID=object.getString("test_category_id");
                                    situationQuestionsID=object.getString("situationt_category_id");
                                    photoQuestionsID=object.getString("photo_category_id");
                                    markedQuestionsID=object.getString("qman_category_id");
                                    oldTheoryQuestionsID=object.getString("old_asa_category_id");
                                    questionsWithoutPicturesID=object.getString("without_pic_category_id");
                                    signalQuestionsID=object.getString("signal_category_id");






                                }else {
                                    showAlertDialog(object.getString("message"));
                                }
                            }else {
                                showAlertDialog(object.getString("message"));
                            }
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                pDialog.dismiss();
                String errorMessage=StringConstants.ErrorMessage(error);

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("method", "theory_question_count");
                MyData.put("token", token);
                MyData.put("language_id", languageID);
                MyData.put("reg_datetime", currentDate+" "+currentTime);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }
    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LearningNewActivity.this);
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

    public void backPressed(View view){
        onBackPressed();
    }
}
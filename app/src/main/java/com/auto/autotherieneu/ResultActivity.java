package com.auto.autotherieneu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    TextView tv_correct_answers, related_text;
    TextView tv_total_questions, tv_answered_questions, tv_correct_answer, tv_incorrect_answer;

    List<QuestionsModel> questionsModelList = new ArrayList<>();
    int answeredQuestions = 0;
    int correctAnswers = 0;
    String pageFrom;
    String type = "";
    String categoryID, blockID;
    String userID;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String answer = "";
    String token;
    RecyclerView recyclerView;
    List<ChapterModel> chapterModelList = new ArrayList<>();
    ChapterModel chapterModel = new ChapterModel();
    String language_id;
    String allTye = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        preferences = getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        token = preferences.getString(StringConstants.prefToken, "");
        userID = preferences.getString(StringConstants.prefUserID, "");

        language_id = preferences.getString(StringConstants.prefLanguageID, "");
        tv_correct_answers = findViewById(R.id.text_correct_answers);

        tv_total_questions = findViewById(R.id.text_total_questions);
        tv_answered_questions = findViewById(R.id.text_answered_questions);
        tv_correct_answer = findViewById(R.id.text_correct_answer);
        tv_incorrect_answer = findViewById(R.id.text_incorrect_answer);
        related_text = findViewById(R.id.related_text);

        recyclerView = findViewById(R.id.recyclerview_chapters);

        questionsModelList = (List<QuestionsModel>) getIntent().getSerializableExtra("questionModelList");
        pageFrom = getIntent().getStringExtra("ScreenFrom");
        categoryID = getIntent().getStringExtra("CategoryID");
        if (getIntent().getStringExtra("blockID") != null) {
            blockID = getIntent().getStringExtra("blockID");
        }
        if (getIntent().getStringExtra("type") != null) {
            allTye = getIntent().getStringExtra("type");
        }


        if (pageFrom.equals("Learning")) {
            type = "learning";
        }
        if (pageFrom.equals("Practice")) {
            type = "practice";
        }
        if (pageFrom.equals("Theory")) {
            type = "theories";
        }

        tv_total_questions.setText(String.valueOf(questionsModelList.size()));

        for (int i = 0; i < questionsModelList.size(); i++) {

            /*if(questionsModelList.get(i).getCorrectAnswer().contains(questionsModelList.get(i).getUserAnswer())){

            }*/
            if (!questionsModelList.get(i).getUserAnswer().equals("")) {
                correctAnswers = correctAnswers + 1;
            }

/*
            if(questionsModelList.get(i).getUserAnswer().equals(questionsModelList.get(i).getCorrectAnswer())){
                correctAnswers=correctAnswers+1;
            }
*/
            if (questionsModelList.get(i).getIsAnswered().equals("yes")) {
                answeredQuestions = answeredQuestions + 1;
            }

            answer = answer + "," + questionsModelList.get(i).getId() + "-" + questionsModelList.get(i).getUserAnswer();
        }

        answer = answer.substring(1);


        //  tv_incorrect_answer.setText(String.valueOf(correctAnswers-answeredQuestions));

        //getChapters();
        getExamResult();
    }

    public void getChapters() {
        final ProgressDialog pDialog = new ProgressDialog(ResultActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);
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

                                    JSONArray array = object.getJSONArray("category_list");
                                    chapterModelList = new ArrayList<>();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject1 = array.getJSONObject(i);
                                        chapterModel = new ChapterModel();
                                        chapterModel.setId(jsonObject1.getString("id"));
                                        chapterModel.setChapterName(jsonObject1.getString("category_name"));
                                        chapterModel.setImage(jsonObject1.getString("image"));
                                        chapterModel.setNumberOfQuestions(jsonObject1.getString("no_of_questions"));

                                        chapterModelList.add(chapterModel);
                                    }

                                    ChapterAdapter notificationAdapter = new ChapterAdapter(ResultActivity.this, chapterModelList);
                                    LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getApplicationContext());
                                    recyclerView.setLayoutManager(horizontalLayoutManager1);
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
                MyData.put("method", "result");
                MyData.put("token", token);
                MyData.put("category_id", categoryID);
                MyData.put("type", type);
                MyData.put("language_id", language_id);
                MyData.put("user_id", userID);
                MyData.put("no_of_questions", tv_total_questions.getText().toString().trim());
                MyData.put("no_of_correct_answer", tv_correct_answer.getText().toString().trim());
                MyData.put("no_of_wrong_answer", tv_incorrect_answer.getText().toString().trim());
                MyData.put("queid_answer", answer);
                MyData.put("datetime", currentDate + " " + currentTime);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void showAlertDialog(String message) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResultActivity.this);
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

    public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {

        private List<ChapterModel> chapterModelList;


        Context context;
        int row_index = -1;

        public ChapterAdapter(Context context, List<ChapterModel> chapterModelList) {
            this.chapterModelList = chapterModelList;
            this.context = context;


        }

        @NonNull
        @Override
        public ChapterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_chapter_row, parent, false);

            return new ChapterAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ChapterAdapter.MyViewHolder holder, int position) {
            final ChapterModel chapterModel = chapterModelList.get(position);
            holder.tv_title.setText(chapterModel.getChapterName());
            holder.tv_questions.setText("No.of Questions : " + chapterModel.getNumberOfQuestions());
            Glide.with(context)
                    .load(chapterModel.getImage())
                    .into(holder.iv_chapter);
            if (position % 2 == 1) {
                holder.linearLayoutContainer.setBackground(getResources().getDrawable(R.drawable.rectangle_home1));
            } else {
                holder.linearLayoutContainer.setBackground(getResources().getDrawable(R.drawable.rectangle_home));
            }
            holder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, ExamInstructionsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("CategoryID", chapterModel.getId());
                    i.putExtra("CategoryName", chapterModel.getChapterName());
                    i.putExtra("FromScreen", "Learning");
                    context.startActivity(i);
                }
            });


        }

        @Override
        public int getItemCount() {
            return chapterModelList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_title, tv_questions;
            LinearLayout linearLayoutContainer;
            ImageView iv_chapter;

            public MyViewHolder(View view) {
                super(view);

                tv_title = (TextView) view.findViewById(R.id.text_name);
                tv_questions = (TextView) view.findViewById(R.id.text_no_of_questions);
                iv_chapter = (ImageView) view.findViewById(R.id.image_chapter);
                linearLayoutContainer = (LinearLayout) view.findViewById(R.id.layout_container);

            }
        }

    }


    public void backPressed(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(language_id.equals("1")){
            Intent i = new Intent(getApplicationContext(), HomepageGermanActivity.class);
            startActivity(i);
            finish();

        }else if(language_id.equals("2")){
            Intent i = new Intent(getApplicationContext(), HomepageGermanActivity.class);
            startActivity(i);
            finish();

        } else if(language_id.equals("3")){
            Intent i = new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(i);
            finish();

        } else if(language_id.equals("4")){
            Intent i = new Intent(getApplicationContext(), HomepageItalianActivity.class);
            startActivity(i);
            finish();
        }

    }

    public void getExamResult() {
        final ProgressDialog pDialog = new ProgressDialog(ResultActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);
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
                        JSONObject object = responseArray.getJSONObject(0);
                        if (responseArray.length() > 0) {

                            if (object.has("status")) {
                                String status = object.getString("status");
                                if (status.equals("success")) {

                                    JSONArray array = object.getJSONArray("practice_category_details");
                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject jsonObject1 = array.getJSONObject(i);
                                            chapterModel = new ChapterModel();
                                            chapterModel.setId(jsonObject1.getString("id"));
                                            chapterModel.setChapterName(jsonObject1.getString("category_name"));
                                            chapterModel.setImage(jsonObject1.getString("image"));
                                            chapterModel.setNumberOfQuestions(jsonObject1.getString("no_of_questions"));

                                            chapterModelList.add(chapterModel);
                                        }

                                        ChapterAdapter notificationAdapter = new ChapterAdapter(ResultActivity.this, chapterModelList);
                                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getApplicationContext());
                                        recyclerView.setLayoutManager(horizontalLayoutManager1);
                                        recyclerView.setAdapter(notificationAdapter);
                                    }else

                                        related_text.setVisibility(View.GONE);
                                    }
                                    String totalnoofquestion = object.getString("totalnoofquestion");
                                    String attend_questions = object.getString("attend_questions");
                                    String not_attend_questions = object.getString("not_attend_questions");
                                    String correct_answer = object.getString("correct_answer");
                                    String wrong_answer = object.getString("wrong_answer");
                                    tv_total_questions.setText(totalnoofquestion);
                                    tv_correct_answer.setText(correct_answer);
                                    tv_answered_questions.setText(attend_questions);
                                    tv_incorrect_answer.setText(wrong_answer);
                                } else {
                                    showAlertDialog(object.getString("message"));
                                }
                            } else {
                                showAlertDialog(object.getString("message"));
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
                MyData.put("method", "exam_list_and_result");
                MyData.put("token", token);
                MyData.put("category_id", categoryID);
                MyData.put("type", allTye);
                MyData.put("language_id", language_id);
                MyData.put("user_id", userID);
                ;
                MyData.put("block_id", blockID);
                Log.i("exam_list_and_result", MyData.toString());
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }
}
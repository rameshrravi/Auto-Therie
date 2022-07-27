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

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesQuestionsActivity extends AppCompatActivity {


    List<QuestionsModel> questionsModelList = new ArrayList<>();
    QuestionsModel questionsModel = new QuestionsModel();
    QuestionsModel questionsModel1 = new QuestionsModel();
    String token;
    int position1 = 0;
    String answer ="";
    String categoryID;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    RecyclerView recyclerView;
    String fromScreen="";
    TextView tv_question;
    TextView tv_question_number;
    TextView tv_previous,tv_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_questions);

        tv_previous=findViewById(R.id.text_previous);
        tv_next=findViewById(R.id.text_next);
        tv_question_number=findViewById(R.id.text_question_number);
        tv_question = (TextView) findViewById(R.id.text_question);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        token = preferences.getString(StringConstants.prefToken,"");

        categoryID = getIntent().getStringExtra("CategoryID");
        fromScreen = getIntent().getStringExtra("FromScreen");

        recyclerView=findViewById(R.id.recyclerview_questions);

        tv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                position1 = position1-1;
                if(position1==0){
                    tv_previous.setVisibility(View.GONE);
                }
                if(position1>=0){
                    tv_next.setVisibility(View.VISIBLE);
                    questionsModel1 = questionsModelList.get(position1);
                    tv_question_number.setText(String.valueOf(position1+1)+" / "+String.valueOf(questionsModelList.size()));
                    tv_question.setText(questionsModel1.getQuestion());


                }
            }
        });
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tv_next.getText().toString().equals("NEXT")){
                    tv_previous.setVisibility(View.VISIBLE);
                    position1 = position1+1;
                    if(position1<questionsModelList.size()){
                        if(position1==questionsModelList.size()-1){
                            tv_next.setVisibility(View.GONE);
                        }

                        tv_question_number.setText(String.valueOf(position1+1)+" / "+String.valueOf(questionsModelList.size()));
                        questionsModel1 = questionsModelList.get(position1);
                        tv_question.setText(questionsModel1.getQuestion());


                    }
                }

            }
        });


        getRulesQuestions();
    }

    public void getRulesQuestions(){
        final ProgressDialog pDialog=new ProgressDialog(RulesQuestionsActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(RulesQuestionsActivity.this);
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
                                    JSONArray array= object.getJSONArray("rules_que_details");
                                    questionsModelList=new ArrayList<>();
                                    for(int i=0;i<array.length();i++){
                                        JSONObject jsonObject1=array.getJSONObject(i);
                                        questionsModel=new QuestionsModel();
                                        questionsModel.setId(jsonObject1.getString("id"));
                                        questionsModel.setCategoryID(jsonObject1.getString("category_id"));
                                        questionsModel.setQuestion(jsonObject1.getString("question"));

                                        questionsModelList.add(questionsModel);
                                    }

                                    if(questionsModelList.size()>0){
                                        questionsModel1 = questionsModelList.get(0);
                                        tv_question.setText(questionsModel1.getQuestion());
                                        tv_question_number.setText("1 / "+String.valueOf(questionsModelList.size()));

                                    }


                                    ChapterAdapter notificationAdapter = new ChapterAdapter(RulesQuestionsActivity.this, questionsModelList);
                                    LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getApplicationContext());
                                    recyclerView.setLayoutManager(horizontalLayoutManager1);
                                    recyclerView.setAdapter(notificationAdapter);



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
                MyData.put("method", "rules_questions_category_wise");
                MyData.put("token", token);
                MyData.put("category_id", categoryID);
                MyData.put("reg_datetime", currentDate+" "+currentTime);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }
    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RulesQuestionsActivity.this);
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

    public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {

        private List<QuestionsModel> questionsModelList;


        Context context;
        int row_index=-1;
        String answer="";
        int position1 = 0;

        public ChapterAdapter(Context context, List<QuestionsModel> questionsModelList){
            this.questionsModelList = questionsModelList;
            this.context = context;


        }
        @NonNull
        @Override
        public ChapterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_rules_questions_row, parent, false);

            return new ChapterAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ChapterAdapter.MyViewHolder holder, int position) {
            final QuestionsModel questionsModel = questionsModelList.get(position);
            holder.tv_question.setText(questionsModel.getQuestion());

        }

        @Override
        public int getItemCount() {
            return questionsModelList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_question;

            public MyViewHolder(View view) {
                super(view);

                tv_question = (TextView) view.findViewById(R.id.text_question);

            }
        }

    }

}
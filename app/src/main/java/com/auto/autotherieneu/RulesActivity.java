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

public class RulesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ChapterModel> chapterModelList = new ArrayList<>();
    ChapterModel chapterModel=new ChapterModel();
    String token;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    LinearLayout layout_learning,layout_rules,layout_exam,layout_profile;
    String language_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        layout_learning=findViewById(R.id.layout_learning);
        layout_rules=findViewById(R.id.layout_rules);
        layout_exam=findViewById(R.id.layout_exams);
        layout_profile=findViewById(R.id.layout_profile);
        language_id = preferences.getString(StringConstants.prefLanguageID, "");
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
                finish();
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
            }
        });


        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        token = preferences.getString(StringConstants.prefToken,"");

        recyclerView = findViewById(R.id.recyclerview_rules);

        getChapters();

    }

    public void backPressed(View view){
        onBackPressed();
    }

    public void getChapters(){
        final ProgressDialog pDialog=new ProgressDialog(RulesActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(RulesActivity.this);
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
                                    JSONArray array= object.getJSONArray("rules_category_details");
                                    chapterModelList=new ArrayList<>();
                                    for(int i=0;i<array.length();i++){
                                        JSONObject jsonObject1=array.getJSONObject(i);
                                        chapterModel=new ChapterModel();
                                        chapterModel.setId(jsonObject1.getString("id"));
                                        chapterModel.setChapterName(jsonObject1.getString("category_name"));
                                        chapterModel.setImage(jsonObject1.getString("image"));
                                        chapterModel.setNumberOfQuestions(jsonObject1.getString("no_of_questions"));

                                        chapterModelList.add(chapterModel);
                                    }

                                    ChapterAdapter notificationAdapter = new ChapterAdapter(RulesActivity.this, chapterModelList);
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
                MyData.put("method", "rules_category");
                MyData.put("token", token);
                MyData.put("language_id", language_id);
                MyData.put("reg_datetime", currentDate+" "+currentTime);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RulesActivity.this);
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
        int row_index=-1;

        public ChapterAdapter(Context context, List<ChapterModel> chapterModelList){
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
            holder.tv_questions.setText("No.of Questions : "+chapterModel.getNumberOfQuestions());
            Glide.with(context)
                    .load(chapterModel.getImage())
                    .into(holder.iv_chapter);

            if(position%2==1){
                holder.linearLayoutContainer.setBackground(getResources().getDrawable(R.drawable.rectangle_home1));
            }else {
                holder.linearLayoutContainer.setBackground(getResources().getDrawable(R.drawable.rectangle_home));
            }
            holder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context,RulesQuestionsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("CategoryID",chapterModel.getId());
                    i.putExtra("CategoryName",chapterModel.getChapterName());
                    i.putExtra("FromScreen","Rules");
                    context.startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return chapterModelList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_title,tv_questions;
            LinearLayout linearLayoutContainer;
            ImageView iv_chapter;

            public MyViewHolder(View view) {
                super(view);

                tv_title = (TextView) view.findViewById(R.id.text_name);
                tv_questions = (TextView) view.findViewById(R.id.text_no_of_questions);
                iv_chapter= (ImageView) view.findViewById(R.id.image_chapter);
                linearLayoutContainer=(LinearLayout) view.findViewById(R.id.layout_container);
            }
        }

    }


}
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
import android.database.Cursor;
import android.graphics.Color;
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

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class LearningBlocksActivity extends AppCompatActivity {

    TextView tv_category_name;
    String categoryID;
    RecyclerView recyclerView;
    List<ChapterModel> chapterModelList = new ArrayList<>();
    ChapterModel chapterModel=new ChapterModel();
    String token;
    String language_id="3";
    String user_id="";
    String type;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    PieChartView pieChartView;
    TextView tv_reset;
    List<SliceValue> pieData = new ArrayList<>();
    TextView tv_correct_answers,tv_wrong_answers,tv_unanswered_questions,tv_partially_right;
    MyDatabaseHelper myDataBaseHelper;
    boolean shouldResume=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_blocks);

        myDataBaseHelper = new MyDatabaseHelper(LearningBlocksActivity.this);

        tv_reset = findViewById(R.id.text_reset);
        tv_category_name = findViewById(R.id.text_category_name);

        tv_correct_answers = findViewById(R.id.text_correct_answers);
        tv_partially_right = findViewById(R.id.text_partially_right);
        tv_wrong_answers = findViewById(R.id.text_wrong_answers);
        tv_unanswered_questions = findViewById(R.id.text_unanswered_questions);


        tv_category_name.setText(getIntent().getStringExtra("Category"));

        categoryID = getIntent().getStringExtra("CategoryID");
        type = getIntent().getStringExtra("Type");

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        token = preferences.getString(StringConstants.prefToken,"");
        language_id = preferences.getString(StringConstants.prefLanguageID,"");
        user_id = preferences.getString(StringConstants.prefUserID,"");

        recyclerView = findViewById(R.id.recyclerview_learning_details);

        pieChartView = findViewById(R.id.chart);


        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               resetAnswers();

            }
        });



        getChapters();

    }

    public void backPressed(View view){
        onBackPressed();
    }

    public void getChapters(){
        final ProgressDialog pDialog=new ProgressDialog(LearningBlocksActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(LearningBlocksActivity.this);
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

                                    if(object.has("not_attend_questions")){
                                        tv_unanswered_questions.setText(object.getString("not_attend_questions"));
                                    }
                                    if(object.has("correct_answer")){
                                        tv_correct_answers.setText(object.getString("correct_answer"));
                                    }
                                    if(object.has("wrong_answer")){
                                        tv_wrong_answers.setText(object.getString("wrong_answer"));
                                    }

                                    pieData=new ArrayList<>();
                                    pieData.add(new SliceValue(Integer.parseInt(object.getString("correct_answer")), getResources().getColor(R.color.green)));
                                    pieData.add(new SliceValue(0, getResources().getColor(R.color.orange)));
                                    pieData.add(new SliceValue(Integer.parseInt(object.getString("wrong_answer")), getResources().getColor(R.color.red)));
                                    pieData.add(new SliceValue(Integer.parseInt(object.getString("not_attend_questions")), getResources().getColor(R.color.gray)));

                                    PieChartData pieChartData = new PieChartData(pieData);
                                    pieChartView.setPieChartData(pieChartData);


                                    JSONArray array= object.getJSONArray("block_details");
                                    chapterModelList=new ArrayList<>();
                                    for(int i=0;i<array.length();i++){
                                        JSONObject jsonObject1=array.getJSONObject(i);
                                        chapterModel=new ChapterModel();
                                        chapterModel.setId(jsonObject1.getString("id"));
                                        chapterModel.setChapterName(jsonObject1.getString("block"));
                                        chapterModel.setNumberOfQuestions(jsonObject1.getString("description"));

                                        chapterModelList.add(chapterModel);
                                    }



                                    ChapterAdapter notificationAdapter = new ChapterAdapter(LearningBlocksActivity.this, chapterModelList);
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
                MyData.put("method", "block_details");
                MyData.put("token", token);
                MyData.put("language_id", language_id);
                MyData.put("user_id", user_id);
                MyData.put("type", type);
                MyData.put("category_id", categoryID);
                MyData.put("reg_datetime", currentDate+" "+currentTime);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(shouldResume){
            recreate();
        }
    }

    public void resetAnswers(){
        final ProgressDialog pDialog=new ProgressDialog(LearningBlocksActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(LearningBlocksActivity.this);
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

                                    getChapters();

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
                MyData.put("method", "reset_result");
                MyData.put("token", token);
                MyData.put("language_id", language_id);
                MyData.put("user_id", user_id);
                MyData.put("type", type);
                MyData.put("category_id", categoryID);
                MyData.put("reg_datetime", currentDate+" "+currentTime);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LearningBlocksActivity.this);
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
                    .inflate(R.layout.layout_learning_row, parent, false);

            return new ChapterAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ChapterAdapter.MyViewHolder holder, int position) {
            final ChapterModel chapterModel = chapterModelList.get(position);
            holder.tv_title.setText(chapterModel.getChapterName());
            holder.tv_slno.setText(String.valueOf(position+1));
            holder.tv_questions.setText(chapterModel.getNumberOfQuestions());


            holder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    shouldResume = true;
                    Intent i = new Intent(context,LearningQuestionsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("CategoryID",categoryID);
                    i.putExtra("CategoryIDD",chapterModel.getId());
                    i.putExtra("CategoryName",chapterModel.getChapterName());
                    i.putExtra("FromScreen","Learning");

                    i.putExtra("Type",type);
                    context.startActivity(i);
                }
            });


        }

        @Override
        public int getItemCount() {
            return chapterModelList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_title,tv_questions,tv_slno;
            LinearLayout linearLayoutContainer;
            ImageView iv_chapter;

            public MyViewHolder(View view) {
                super(view);

                tv_title = (TextView) view.findViewById(R.id.text_name);
                tv_slno = (TextView) view.findViewById(R.id.text_slno);
                tv_questions = (TextView) view.findViewById(R.id.text_no_of_questions);

                linearLayoutContainer=(LinearLayout)view.findViewById(R.id.layout_container);

            }
        }

    }


}
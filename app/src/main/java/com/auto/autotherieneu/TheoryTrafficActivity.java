package com.auto.autotherieneu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
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
import androidx.core.text.HtmlCompat;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheoryTrafficActivity extends AppCompatActivity {

    List<EasyCarTheoryModel> easyCarTheoryModelList = new ArrayList<>();
    EasyCarTheoryModel easyCarTheoryModel = new EasyCarTheoryModel();
    TextView tv_category_name;
    RecyclerView recyclerView;
    String categoryName="";
    String categoryID="";
    String token;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory_questions1);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();
        token = preferences.getString(StringConstants.prefToken,"");

        recyclerView = findViewById(R.id.recyclerview_questions);
        tv_category_name = findViewById(R.id.text_category_name);

        categoryID = getIntent().getStringExtra("CategoryID");
        categoryName = getIntent().getStringExtra("CategoryName");
        tv_category_name.setText(categoryName);
        Log.i("sdsdsds",categoryName);
        getChapters();

    }
    public void backPressed(View view){
        onBackPressed();
    }

    public void getChapters(){
        final ProgressDialog pDialog=new ProgressDialog(TheoryTrafficActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(TheoryTrafficActivity.this);
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
                                    JSONArray array= object.getJSONArray("easycontant_details");
                                    easyCarTheoryModelList=new ArrayList<>();
                                    for(int i=0;i<array.length();i++){
                                        JSONObject jsonObject1=array.getJSONObject(i);
                                        easyCarTheoryModel=new EasyCarTheoryModel();
                                        easyCarTheoryModel.setId(jsonObject1.getString("id"));
                                        easyCarTheoryModel.setCategoryID(jsonObject1.getString("category_id"));
                                        easyCarTheoryModel.setImage(jsonObject1.getString("image"));
                                        easyCarTheoryModel.setDescription(jsonObject1.getString("content"));

                                        easyCarTheoryModelList.add(easyCarTheoryModel);
                                    }


                                    ChapterAdapter notificationAdapter = new ChapterAdapter(TheoryTrafficActivity.this, easyCarTheoryModelList);
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
                MyData.put("method", "easycontent_category_wise");
                MyData.put("token", token);
                MyData.put("category_id", categoryID);
                MyData.put("reg_datetime", currentDate+" "+currentTime);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TheoryTrafficActivity.this);
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

        private List<EasyCarTheoryModel> easyCarTheoryModelList;


        Context context;
        int row_index=-1;

        public ChapterAdapter(Context context, List<EasyCarTheoryModel> easyCarTheoryModelList){
            this.easyCarTheoryModelList = easyCarTheoryModelList;
            this.context = context;


        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_traffic, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
            final EasyCarTheoryModel chapterModel = easyCarTheoryModelList.get(position);
           // holder.tv_description.setText(chapterModel.getDescription());
            //holder.tv_description.setText((Html.fromHtml(Html.fromHtml(chapterModel.getDescription()).toString())));
            holder.tv_description.setText(HtmlCompat.fromHtml(chapterModel.getDescription(), 0));
            holder.tv_description1.setText(HtmlCompat.fromHtml(chapterModel.getDescription(), 0));
           // holder.tv_description1.setText(chapterModel.getDescription());
            //holder.tv_description1.setText((Html.fromHtml(Html.fromHtml(chapterModel.getDescription()).toString())));

            Glide.with(context)
                    .load(chapterModel.getImage())
                    .into(holder.iv_question);
            Glide.with(context)
                    .load(chapterModel.getImage())
                    .into(holder.iv_question1);


            if(!chapterModel.getDescription().isEmpty()&&!chapterModel.getImage().isEmpty()){
                holder.linearLayoutContent.setVisibility(View.VISIBLE);
                holder.iv_question.setVisibility(View.GONE);
                holder.tv_description.setVisibility(View.GONE);
            }
            if(!chapterModel.getDescription().isEmpty()&&chapterModel.getImage().isEmpty()){
                holder.linearLayoutContent.setVisibility(View.GONE);
                holder.iv_question.setVisibility(View.GONE);
                holder.tv_description.setVisibility(View.VISIBLE);
            }
            if(chapterModel.getDescription().isEmpty()&&!chapterModel.getImage().isEmpty()){
                holder.linearLayoutContent.setVisibility(View.GONE);
                holder.iv_question.setVisibility(View.VISIBLE);
                holder.tv_description.setVisibility(View.GONE);
            }

            if(categoryName.equals("Traffic control")){
                holder.linearLayoutContent.setVisibility(View.VISIBLE);
                holder.iv_question1.setVisibility(View.GONE);
                holder.iv_question.setVisibility(View.GONE);
                holder.tv_description.setVisibility(View.GONE);
                holder.image_questionfull.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(chapterModel.getImage())
                        .into(holder.image_questionfull);
            }
        }

        @Override
        public int getItemCount() {
            return easyCarTheoryModelList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_description,tv_description1;
            LinearLayout linearLayoutContent;
            ImageView iv_question,iv_question1,image_questionfull;

            public MyViewHolder(View view) {
                super(view);

                tv_description = (TextView) view.findViewById(R.id.text_description);
                tv_description1 = (TextView) view.findViewById(R.id.text_description1);
                iv_question=(ImageView)view.findViewById(R.id.image_question);
                iv_question1=(ImageView)view.findViewById(R.id.image_question1);
                image_questionfull=(ImageView)view.findViewById(R.id.image_questionfull);

                linearLayoutContent=(LinearLayout)view.findViewById(R.id.layout_image_content);

            }
        }

    }



}
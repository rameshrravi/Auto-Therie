package com.auto.autotherieneu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class SignalDescriptionActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ChapterModel> chapterModelList = new ArrayList<>();
    ChapterModel chapterModel=new ChapterModel();
    String token;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    String categoryID="";
    TextView tv_description;
    TextView tv_signal_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_description);

        tv_description=findViewById(R.id.text_descriptions);
        tv_signal_name=findViewById(R.id.text_signal_name);
        recyclerView=findViewById(R.id.recyclerview_signal_images);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        token = preferences.getString(StringConstants.prefToken,"");

        categoryID  = getIntent().getStringExtra("CategoryID");
        tv_signal_name.setText(getIntent().getStringExtra("CategoryName"));

        getChapters();
    }
    public void backPressed(View view){
        onBackPressed();
    }

    public void getChapters(){
        final ProgressDialog pDialog=new ProgressDialog(SignalDescriptionActivity.this);
        pDialog.setMessage("Getting Details..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(SignalDescriptionActivity.this);
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
                                    //tv_description.setText(object.getString("description"));
                                    tv_description.setText((Html.fromHtml(Html.fromHtml(object.getString("description")).toString())));
                                    JSONArray array= object.getJSONArray("signals_images");
                                    chapterModelList=new ArrayList<>();
                                    for(int i=0;i<array.length();i++){
                                        JSONObject jsonObject1=array.getJSONObject(i);
                                        chapterModel=new ChapterModel();
                                        chapterModel.setId(jsonObject1.getString("id"));
                                        chapterModel.setImage(jsonObject1.getString("image"));
                                        chapterModel.setChapterName(jsonObject1.getString("signal_name"));
                                        chapterModelList.add(chapterModel);
                                    }

                                    ChapterAdapter notificationAdapter = new ChapterAdapter(SignalDescriptionActivity.this, chapterModelList);
                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SignalDescriptionActivity.this, 2);
                                    recyclerView.setLayoutManager(mLayoutManager);
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
                MyData.put("method", "signals_description");
                MyData.put("token", token);
                MyData.put("category_id", categoryID);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignalDescriptionActivity.this);
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
                    .inflate(R.layout.layout_signas_row, parent, false);

            return new ChapterAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ChapterAdapter.MyViewHolder holder, int position) {
            final ChapterModel chapterModel = chapterModelList.get(position);
            holder.tv_title.setText(chapterModel.getChapterName());
            Glide.with(context)
                    .load(chapterModel.getImage())
                    .into(holder.iv_chapter);



        }

        @Override
        public int getItemCount() {
            return chapterModelList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_title;
            ImageView iv_chapter;

            public MyViewHolder(View view) {
                super(view);

                tv_title = (TextView) view.findViewById(R.id.text_name);
                iv_chapter= (ImageView) view.findViewById(R.id.image_chapter);
            }
        }

    }


}
package com.auto.autotherieneu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText et_email_id,et_password;
    TextView tv_login,tv_forgot_password,tv_register;
    String s_email_id,s_password;
    Window window;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    Boolean isLogin=false;
    String languageID="3";
    String  token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_login);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        isLogin=preferences.getBoolean("isLogin",false);
        languageID=preferences.getString(StringConstants.prefLanguageID,"3");


        if(isLogin){

            if(languageID.equals("1")){
                Intent i=new Intent(getApplicationContext(),HomepageGermanActivity.class);
                startActivity(i);
                finish();
            }
            if(languageID.equals("2")){
                Intent i=new Intent(getApplicationContext(),HomepageFrenchActivity.class);
                startActivity(i);
                finish();
            }
            if(languageID.equals("3")){
                Intent i=new Intent(getApplicationContext(),HomepageActivity.class);
                startActivity(i);
                finish();
            }
            if(languageID.equals("4")){
                Intent i=new Intent(getApplicationContext(),HomepageItalianActivity.class);
                startActivity(i);
                finish();
            }

        }


        et_email_id=findViewById(R.id.edittext_email_id);
        et_password=findViewById(R.id.edittext_password);
        tv_login=findViewById(R.id.text_login);
        tv_forgot_password=findViewById(R.id.text_forgot_password);
        tv_register=findViewById(R.id.text_register);

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_email_id=et_email_id.getText().toString().trim();
                s_password=et_password.getText().toString().trim();

                if(!s_email_id.isEmpty()){
                    if(!s_password.isEmpty()){

                       login();

                    }else {
                        showAlertDialog("Please enter password");
                    }
                }else {
                    showAlertDialog("Please enter email ID");
                }

            }
        });

        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("TAG", msg);
                        // Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        finish();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());


    }

    public void login(){
        final ProgressDialog pDialog=new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Authenticating..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
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
                                    editor.putString(StringConstants.prefUserID,object.getString("id"));
                                    editor.putString(StringConstants.prefName,object.getString("name"));
                                    editor.putString(StringConstants.prefEmailID,object.getString("email"));
                                    editor.putString(StringConstants.prefToken,object.getString("token"));
                                    editor.putString(StringConstants.prefPassword,s_password);
                                    editor.putBoolean("isLogin",true);
                                    editor.apply();
                                    editor.commit();

                                    Intent i =new Intent(getApplicationContext(),HomepageActivity.class);
                                    startActivity(i);
                                    finish();
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
                MyData.put("method", "login");
                MyData.put("email", s_email_id);
                MyData.put("password", s_password);
                MyData.put("fcm_token", token);
                MyData.put("reg_datetime", currentDate+" "+currentTime);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
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

}
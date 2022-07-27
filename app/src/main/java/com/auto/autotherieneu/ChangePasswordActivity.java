package com.auto.autotherieneu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText et_old_password,et_new_password,et_confirm_password;
    TextView tv_update;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    Boolean isLogin=false;
    String  token="";
    String userID="";
    String s_old_password,s_new_password,s_confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();
        token=preferences.getString(StringConstants.prefToken,"");

        et_old_password=findViewById(R.id.edittext_old_password);
        et_new_password=findViewById(R.id.edittext_new_password);
        et_confirm_password=findViewById(R.id.edittext_confirm_password);
        tv_update=findViewById(R.id.text_update);

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_old_password=et_old_password.getText().toString().trim();
                s_new_password=et_new_password.getText().toString().trim();
                s_confirm_password=et_confirm_password.getText().toString().trim();

                if(!s_old_password.isEmpty()){
                    if(!s_new_password.isEmpty()){
                        if(!s_confirm_password.isEmpty()){
                            if(s_confirm_password.equals(s_new_password)){

                                login();
                            }else {
                                showAlertDialog("Password doesn't match");
                            }
                        }else {
                            showAlertDialog("Please enter confirm password");
                        }
                    }else {
                        showAlertDialog("Please enter new password");
                    }
                }else {
                    showAlertDialog("Please enter old password");
                }


            }
        });
    }

    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChangePasswordActivity.this);
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

    public void login(){
        final ProgressDialog pDialog=new ProgressDialog(ChangePasswordActivity.this);
        pDialog.setMessage("Updating..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(ChangePasswordActivity.this);
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

                                    editor.putString(StringConstants.prefPassword,s_new_password);
                                    editor.putBoolean("isLogin",true);
                                    editor.apply();
                                    editor.commit();
                                    Toast.makeText(getApplicationContext(),"Updated successfully",Toast.LENGTH_SHORT).show();


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
                MyData.put("method", "change_password");
                MyData.put("new_password", s_new_password);
                MyData.put("old_password", s_old_password);
                MyData.put("token", token);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

}
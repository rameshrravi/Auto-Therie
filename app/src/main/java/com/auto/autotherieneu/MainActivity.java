package com.auto.autotherieneu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;

public class MainActivity extends AppCompatActivity {

    Window window;
    ImageView iv_lets_start;
    SharedPreferences preferences;
    Boolean isLogin=false;
    String languageID="3";
    String version ="";
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
        setContentView(R.layout.activity_main);
        preferences =getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        isLogin=preferences.getBoolean("isLogin",false);
        languageID=preferences.getString(StringConstants.prefLanguageID,"3");

        /*try{
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            if (isNetworkAvailable(this)) {
                FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(5).build();
                remoteConfig.setConfigSettingsAsync(remoteConfigSettings);
                remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Boolean> task) {

                        if (task.isSuccessful()) {

                            String name = remoteConfig.getString("new_version");
                            if (Float.valueOf(version.replace(".","")) < Float.valueOf(name.replace(".",""))) {

                            }

                        }
                    }
                });
            }
        }catch (Exception e){

        }*/
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
        iv_lets_start=findViewById(R.id.image_lets_start);


        iv_lets_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            try {
//                Log.e("Test starrt ", " here ");
//                Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
//                int returnVal = p1.waitFor();
//                boolean reachable = (returnVal==0);
//                Log.e("Test endddd ", " here ");
                return true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        } else
            return false;
    }
}
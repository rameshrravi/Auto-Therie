package com.auto.autotherieneu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.auto.autotherieneu.ui.home.HomeFragmentItalian;
import com.auto.autotherieneu.ui.home.HomeFrenchFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.auto.autotherieneu.databinding.ActivityHomepageItalianBinding;

public class HomepageItalianActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    boolean isFragment=false;
    public boolean getMyFlag() {
        return isFragment;
    }
    public void setMyFlag(boolean myFlag) {
        this.isFragment = myFlag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homepage_italian);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragmentItalian homeFragment=new HomeFragmentItalian();
        insertFragment(homeFragment);

    }

    private void insertFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack("mainstack");
        transaction.commitAllowingStateLoss();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getMyFlag()) {
         /*  HomeFragment homeFragment=new HomeFragment();
            insertFragment(homeFragment);*/
            Intent i = new Intent(getApplicationContext(), HomepageGermanActivity.class);
            startActivity(i);
            finish();

        }else {
            {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomepageItalianActivity.this);
                builder.setMessage("Do you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                finish();
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.nav_easy_car_theries) {
            Intent i=new Intent(getApplicationContext(),EasyCarTheoryGermanActivity.class);
            startActivity(i);
        }
        if (id == R.id.nav_learning) {
            Intent i=new Intent(getApplicationContext(),LearningNewActivity.class);
            startActivity(i);
        }
        if (id == R.id.nav_exam) {
            Intent i=new Intent(getApplicationContext(),ExamsGermanyActivity.class);
            startActivity(i);
        }
        if (id == R.id.nav_language) {
            Intent i=new Intent(getApplicationContext(),AppLanguagesActivity.class);
            startActivity(i);
        }
        if (id == R.id.nav_share) {
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Auto Theorie Nue");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Download Auto Theorie Nue Android App to get started learning the Traffic rules and sign.");
            startActivity(Intent.createChooser(i, "Share via"));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}
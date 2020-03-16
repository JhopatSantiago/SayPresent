package com.example.saypresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class about_us extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayoutAbout;
    private ActionBarDrawerToggle actionBarDrawerToggleAbout;
    private Toolbar toolbarAbout;
    private NavigationView navigationViewAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        toolbarAbout = findViewById(R.id.toolbarAboutUs);
        drawerLayoutAbout = findViewById(R.id.drawer_layout_aboutus);
        navigationViewAbout = findViewById(R.id.nav_view_aboutus);
        setSupportActionBar(toolbarAbout);
        getSupportActionBar().setTitle(null);

        //side menu navigation
        navigationViewAbout.bringToFront();
        actionBarDrawerToggleAbout = new ActionBarDrawerToggle(this,drawerLayoutAbout,toolbarAbout,R.string.open,R.string.close);
        drawerLayoutAbout.addDrawerListener(actionBarDrawerToggleAbout);
        actionBarDrawerToggleAbout.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggleAbout.syncState();
        navigationViewAbout.setNavigationItemSelectedListener(this);
        navigationViewAbout.setCheckedItem(R.id.AboutUs);
        //
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.dashboard:
                Intent intentdashboard = new Intent (about_us.this,Dashboard.class);
                intentdashboard.putExtra("organizer_key", organizer_key);
                drawerLayoutAbout.closeDrawers();
                startActivity(intentdashboard);
                break;
            case R.id.addEvent:
                Intent intent = new Intent (about_us.this,addEvent.class);
                intent.putExtra("organizer_key", organizer_key);
                drawerLayoutAbout.closeDrawers();
                startActivity(intent);
                break;
            case R.id.viewEvent:
                Intent newintent = new Intent (about_us.this,EventActivity.class);
                newintent.putExtra("organizer_key", organizer_key);
                drawerLayoutAbout.closeDrawers();
                startActivity(newintent);
                break;
            case R.id.Logout:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Going Away?")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent (about_us.this,LoginPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                about_us.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            case R.id.AboutUs:
                break;
        }
        return true;
    }
}

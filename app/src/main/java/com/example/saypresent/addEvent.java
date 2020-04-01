package com.example.saypresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.saypresent.controller.EventController;
import com.example.saypresent.model.Event;
import com.example.saypresent.utils.CreateEventInterface;
import com.example.saypresent.LoadingDialog;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Creating Events
 */
public class addEvent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String organizer_key;
    private EditText event_name_field;
    private EditText event_location_field;
    private EditText event_description_field;
    private EditText event_date_field;
    private Event event;
    private EventController eventController;
    private CreateEventInterface createEventInterface;
    private LoadingDialog loadingDialog = new LoadingDialog(addEvent.this);
    private DrawerLayout drawerLayoutAddEvent;
    private ActionBarDrawerToggle actionBarDrawerToggleAddEvent;
    private Toolbar toolbarAddEvent;
    private NavigationView navigationViewAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //side menu navigation
        toolbarAddEvent = findViewById(R.id.toolbarAddEvent);
        drawerLayoutAddEvent = findViewById(R.id.drawer_layoutAddEvent);
        navigationViewAddEvent = findViewById(R.id.nav_view_addEvent);
        navigationViewAddEvent.bringToFront();
        setSupportActionBar(toolbarAddEvent);
        getSupportActionBar().setTitle(null);


        actionBarDrawerToggleAddEvent = new ActionBarDrawerToggle(this,drawerLayoutAddEvent,toolbarAddEvent,R.string.open,R.string.close);
        drawerLayoutAddEvent.addDrawerListener(actionBarDrawerToggleAddEvent);
        actionBarDrawerToggleAddEvent.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggleAddEvent.syncState();
        navigationViewAddEvent.setNavigationItemSelectedListener(this);
        navigationViewAddEvent.setCheckedItem(R.id.addEvent);
        //

        final Calendar myCalendar = Calendar.getInstance();
        event_name_field = (EditText) findViewById(R.id.event_name);
        event_location_field = (EditText) findViewById(R.id.event_location);
        event_description_field = (EditText) findViewById(R.id.event_description);
        event_date_field = (EditText) findViewById(R.id.event_date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            private void updateLabel(){
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                event_date_field.setText(sdf.format(myCalendar.getTime()));
            }
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        event_date_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(addEvent.this, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Intent intent = getIntent();
        this.organizer_key = intent.getStringExtra("organizer_key");
    }

    public void saveEvent(View v){
        String event_name = event_name_field.getText().toString();
        String event_location = event_location_field.getText().toString();
        String event_description = event_description_field.getText().toString();
        String event_date = event_date_field.getText().toString();

        event = new Event(event_name, event_location, event_description, event_date, "");

        createEventInterface = new CreateEventInterface() {
            @Override
            public void onCreateEvent(boolean success) {
                if(success){
                    loadingDialog.dismissDialog();
                    Log.i("success", "Successfully created application");
                    showMessage("Successfully created event!");
                    clearFields();
                    Intent todashboard = new Intent (addEvent.this,Dashboard.class);
                    todashboard.putExtra("organizer_key", organizer_key);
                    startActivity(todashboard);
                }else{
                    showMessage("Failure in creating event!");
                }
            }
        };
        loadingDialog.startLoadingDialog();

        eventController = new EventController();
        eventController.CreateEvent(organizer_key, event, createEventInterface);
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void clearFields(){
        event_name_field.setText("");
        event_location_field.setText("");
        event_date_field.setText("");
        event_description_field.setText("");
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.dashboard:
                Intent dashboardIntent = new Intent (addEvent.this,Dashboard.class);
                dashboardIntent.putExtra("organizer_key", organizer_key);
                System.out.println(organizer_key);
                drawerLayoutAddEvent.closeDrawers();
                startActivity(dashboardIntent);
                break;
            case R.id.addEvent:
                break;
            case R.id.viewEvent:
                Intent newintent = new Intent (addEvent.this,EventActivity.class);
                newintent.putExtra("organizer_key", organizer_key);
                drawerLayoutAddEvent.closeDrawers();
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
                                Intent intent = new Intent (addEvent.this,LoginPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                addEvent.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
        }
        return true;
    }
}


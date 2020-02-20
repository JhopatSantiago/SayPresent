package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserTypeActivity extends AppCompatActivity {

    private Button organizer_btn;
    private Button attendee_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        organizer_btn = (Button) findViewById(R.id.organizer_button);
        attendee_btn = (Button) findViewById(R.id.attendee_button);

        organizer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class );
                intent.putExtra("user_type", "organizer");
                startActivity(intent);
            }
        });

        attendee_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                intent.putExtra("user_type", "attendee");
                startActivity(intent);
            }
        });
    }
}

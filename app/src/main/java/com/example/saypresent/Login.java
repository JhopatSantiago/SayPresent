package com.example.saypresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * First Activity after Splash Screen
 * Choose if sign in/sign up
 */
public class Login extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    int mCurrentViewPosition;
    Button Login;
    Button Signup;
    TextView LearnAboutUs;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initialize();

    }
    @Override
    protected void onPause(){
        super.onPause();
        mCurrentViewPosition = mMediaPlayer.getCurrentPosition();
        mVideoView.pause();
    }
    @Override
    protected  void onResume(){
        super.onResume();
        mVideoView.start();
        initialize();
    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
    private void checkNetworkConnection()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please check/turn on your Internet Connection");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which)
            {
                initialize();
            }
        });


        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    private void isNetworkConnectionIsAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork !=null && activeNetwork.isConnected();

        if (isConnected)
        {
            Log.d("Network","Connected");
//            return true;
        }
        else
        {
            checkNetworkConnection();
            Log.d("Network","Not Connected");
//            return false;
        }
    }

    private void initialize(){
    	isNetworkConnectionIsAvailable();
        Login = findViewById(R.id.Login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Login.this,LoginPage.class);
//                Intent intent = new Intent(Login.this, TestingActivity.class);
                startActivity(intent);
            }
        });

        Signup = findViewById(R.id.signUp);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Login.this,UserTypeActivity.class);
                startActivity(intent);
            }

        });

        //Video Looping
        mVideoView = (VideoView) findViewById(R.id.BackgroundLoop);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video_loop);
        mVideoView.setVideoURI(uri);
        mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                mMediaPlayer.setLooping(true);
                if (mCurrentViewPosition !=0) {
                    mMediaPlayer.seekTo(mCurrentViewPosition);
                    mMediaPlayer.start();
                }
            }
        });
        TextView textView = findViewById(R.id.LearnAbout);

        String text = "Learn more about us";
        SpannableString ss = new SpannableString(text);
        ClickableSpan aboutUs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intentabout = new Intent (Login.this,about_us.class);
                startActivity(intentabout);
            }
        };
        ss.setSpan(aboutUs,0,19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    }


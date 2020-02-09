package com.example.saypresent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
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
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

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
                Intent intent = new Intent (Login.this,SignUp.class);
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
    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}

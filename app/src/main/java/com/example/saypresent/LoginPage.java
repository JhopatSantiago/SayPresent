package com.example.saypresent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.saypresent.controller.LoginController;
import com.example.saypresent.controller.OrganizerController;
import com.example.saypresent.datastore.DataStore;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.AuthInterface;
import com.example.saypresent.utils.LoginInterface;

public class LoginPage extends AppCompatActivity {
    private DataStore dataStore = new DataStore();
    private OrganizerController organizerController;
    private AuthInterface authInterface;
    private LoginController loginController;
    private LoginInterface loginInterface;

    private EditText email_field;
    private EditText password_field;
    private LoadingDialog loadingDialog = new LoadingDialog(LoginPage.this);

    private Button LoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //background animation
        ConstraintLayout constraintLayout = findViewById(R.id.Layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        email_field = (EditText) findViewById(R.id.email);
        password_field = (EditText) findViewById(R.id.password);

        /*LoginButton = findViewById(R.id.Button);*/
//        final LoadingDialog loadingDialog = new LoadingDialog(LoginPage.this);

        /*LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                },5000);
            }
        });*/
    }
    private void clearFields(){
        email_field.setText(null);
        password_field.setText(null);
    }

    public void SignIn(View v){

        organizerController = new OrganizerController();
        organizerController = new OrganizerController();

        String email = email_field.getText().toString();
        String password = password_field.getText().toString();

        boolean formOk = true;

        if(TextUtils.isEmpty(email)){
            email_field.setError("Email is required!");
            if(formOk){
                formOk = false;
            }
        }

        if (TextUtils.isEmpty(password)){
            password_field.setError("Password is required!");
            if (formOk){
                formOk = false;
            }
        }

        if(!formOk){
            return;
        }
        loadingDialog.startLoadingDialog();
        loginInterface = new LoginInterface() {
            @Override
            public void onLogin(String user_type, final String key) {
                if(key != null){
                    if(user_type.equals("organizer")){
                        clearFields();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                                Intent intent = new Intent(LoginPage.this,Dashboard.class);
                                intent.putExtra("organizer_key", key);
                                intent.putExtra("finish",true);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        },3000);
                    }else if(user_type.equals("attendee")){
                        clearFields();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                                //redirect to the qr code activity for users
                                Intent intent = new Intent(LoginPage.this,DisplayQRActivity.class);
                                intent.putExtra("attendee_key", key);
//                                intent.putExtra("finish",true);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        },3000);
                    }
                }else{
                    loadingDialog.dismissDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                            Log.i("spinnner", "hide");
                            AlertDialog.Builder Alert = new AlertDialog.Builder(LoginPage.this);
                            Alert.setTitle("Oops!");
                            Alert.setMessage("User does not exist.");
                            Alert.setPositiveButton("OK",null);
                            Alert.show();
                        }
                    }, 200);
                    clearFields();
                }
            }
        };

        loginController = new LoginController();
        loginController.onLogin(email, password, loginInterface);
    }

    //PAPALITAN AKO NG MANGYAYARI DITO SA SUCCESS FUNCTION PRE
    public void showSuccess(String message){
        Log.i("success", message);
    }

    //DITO DIN PAPALITAN AKO PRE.
    public void showFailure(String message){
        Log.i("failure", message);
    }


}

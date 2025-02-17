package com.example.saypresent;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

class LoadingDialog {
    Activity activity;
    AlertDialog dialog;

    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoadingDialog(){
        try {

            AlertDialog.Builder builder = new  AlertDialog.Builder(activity);

            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.custom_dialog,null));
            builder.setCancelable(true);

            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void  dismissDialog(){
        dialog.dismiss();
    }
}

package com.example.saypresent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saypresent.controller.AttendeeController;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.utils.GetAttendeeInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.EnumMap;
import java.util.Map;

public class DisplayQRActivity extends AppCompatActivity {
    private String attendee_key;
    private ImageView imageHolder;
    private Bitmap qrImage;
    private TextView attendeeNameField;
    private AttendeeController attendeeController = new AttendeeController();
    private GetAttendeeInterface getAttendeeInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qr);


        /**
         * ACTUAL CODE
         */
        Intent intent = getIntent();
        this.attendee_key = intent.getStringExtra("attendee_key");

        /**
         * DUMMY DATA - FOR SAMPLE
         */
        imageHolder = (ImageView) findViewById(R.id.qr_image);
        attendeeNameField = (TextView) findViewById(R.id.attendeeName);

        getAttendeeInterface = new GetAttendeeInterface() {
            @Override
            public void onGetAttendee(Attendee attendee) {
                if(attendee != null){
                    String name = attendee.getFirst_name() + " " +  attendee.getLast_name();
                    attendeeNameField.setText(name);
                }else{
                    //Attendee not found!
                }
            }
        };

        attendeeController.getAttendee(attendee_key, getAttendeeInterface);

        generateQRCode();
    }

    private void alert(String message){
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setTitle("QRCode Generator")
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dlg.show();
    }

    private void generateQRCode(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = 260;
                Log.i("attendee_key", attendee_key);
                Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
                hintMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
                hintMap.put(EncodeHintType.MARGIN, 1);

                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try{
                    BitMatrix bitMatrix = qrCodeWriter.encode(attendee_key, BarcodeFormat.QR_CODE, size, size, hintMap);
                    int height = bitMatrix.getHeight();
                    int width = bitMatrix.getWidth();
                    qrImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

                    for (int x=0; x<width; x++){
                        for(int y=0; y<height; y++){
                            qrImage.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showImage(qrImage);

                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                    alert(e.getMessage());
                }

            }
        }).start();
    }

    private void showImage(Bitmap bitmap) {
        if (bitmap == null) {
            imageHolder.setImageResource(android.R.color.transparent);
            qrImage = null;
        } else {
            imageHolder.setImageBitmap(bitmap);
        }
    }

}

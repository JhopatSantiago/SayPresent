package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.StateSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saypresent.controller.AttendeeController;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.utils.AddAttendeeInterface;
import com.example.saypresent.utils.GetAttendeeInterface;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * Scan QR code to add ATTENDEE TO LIST OF EVENT ATTENDEES
 * ATTENDEE -> EVENT
 */
public class QRScannerActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private TextView barcodeTxt;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private String eventKey;
    private String tmp_eventKey;

    private String organizer_key;
    private String event_key;

    private AttendeeController attendeeController = new AttendeeController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        barcodeTxt = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);

        Intent intent = getIntent();

        organizer_key = intent.getStringExtra("organizer_key");
        event_key = intent.getStringExtra("event_key");

    }

    private void initialiseDetector(){
        Toast.makeText(getApplicationContext(), "Barcode Scanner Started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector
                .Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920,1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(surfaceView.getHolder());
                    }else{
                        ActivityCompat.requestPermissions(QRScannerActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "scanner stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0){
                    barcodeTxt.post(new Runnable() {
                        @Override
                        public void run() {
                            eventKey = barcodes.valueAt(0).displayValue;
                            setBarcodeTxt(eventKey);
                        }
                    });
                }
            }
        });
    }

    private void setBarcodeTxt(String data){
        if (!data.equals(this.tmp_eventKey)){
            tmp_eventKey = data;

            attendeeController.getAttendee(data, new GetAttendeeInterface() {
                @Override
                public void onGetAttendee(Attendee attendee) {
                    String name = attendee.getFirst_name() + " " + attendee.getLast_name();
                    barcodeTxt.setText(name);
                    addAttendeeOnEvent(attendee);
                }
            });

        }
    }

    private void addAttendeeOnEvent(Attendee attendee){
        attendeeController.addAttendeeOnEvent(organizer_key, event_key, attendee, new AddAttendeeInterface() {
            @Override
            public void onAddAttendee(boolean success) {
                if(success){
                    Log.i("success", "success");
                }else{
                    Log.i("failed", "failed");
                }
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume(){
        super.onResume();
        initialiseDetector();
    }
}

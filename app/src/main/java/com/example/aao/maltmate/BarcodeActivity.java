package com.example.aao.maltmate;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 *  @author Askil Olsen
 *  @version 1.0
 *  @since 2017-11-15
 *
 */

/**
 * Activity that opens camera and when button is pressed it scans for a barcode
 *  starts singledisplayactivity if scan is successful
 */
public class BarcodeActivity extends AppCompatActivity {
    SurfaceView cameraView;
    BarcodeDetector barcode;
    CameraSource cameraSource;
    SurfaceHolder holder;
    Button scanbutton;
    TextView result;

    public static final String KEY_BARCODE="keyBarcode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        cameraView = (SurfaceView) findViewById(R.id.cameraView);
        cameraView.setZOrderMediaOverlay(true);
        holder = cameraView.getHolder();
        barcode = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        result = (TextView) findViewById(R.id.result);
        scanbutton = (Button) findViewById(R.id.scanbutton);

        //little frame on top of the surfaceview purely for looks
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.overlay);

        //Ask user for permission to use camera


        if(!barcode.isOperational()){
            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
            this.finish();
        }
        /*
        * Sets up the camera
        * and if enabled autofocus is on*/
        cameraSource = new CameraSource.Builder(this, barcode)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(getPackageManager().hasSystemFeature(PackageManager
                        .FEATURE_CAMERA_AUTOFOCUS))
                .setRequestedPreviewSize(1920,1024)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

            //on the surfaceview if permission is granted the camera is started
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ContextCompat.checkSelfPermission(BarcodeActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(cameraView.getHolder());
                    }
                }
                catch (IOException e){
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
        scanbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                barcode.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {

                    }

                    /*
                    * on click it scans for a barcode
                    * if a barcode is deteced it starts singleWhiskeyDisplay activity
                    * with the barcode id needed to display the whiskey*/
                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {

                        final SparseArray<Barcode> barcodes =  detections.getDetectedItems();
                        if(barcodes.size() > 0){
                            String code = barcodes.valueAt(0).displayValue;
                           Intent intent = new Intent(BarcodeActivity.this, SingleWhiskyDisplayActivity.class);
                            intent.putExtra(KEY_BARCODE, code);
                            intent.putExtra("searchtype","barcode");
                            Log.d("DEBUGTAG", code);
                            barcode.release();
                            startActivity(intent);
                            finish();


                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.back:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

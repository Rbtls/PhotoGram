package com.example.photogram;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import android.os.Bundle;
import android.opengl.GLSurfaceView;


public class MainActivity extends Activity implements OnFocusListener {
    private Camera mCamera;
    private CameraPreview mPreview;
    private int currentCameraId;
    private GLSurfaceView gLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);

        if (getIntent().hasExtra("camera_id")) {
            currentCameraId = getIntent().getIntExtra("camera_id", Camera.CameraInfo.CAMERA_FACING_BACK);
        } else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = new MyGLSurfaceView(this);
        FrameLayout gl_view = (FrameLayout) findViewById(R.id.gl_view);
        gl_view.addView(gLView);

        Button focusBtn = (Button) this.findViewById(R.id.focusBtn);
        Button addColorBtn = (Button) this.findViewById(R.id.addBtn);

        focusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addColorBtn.setEnabled(false);
                focusBtn.setEnabled(false);
                Toast toast = Toast.makeText(getApplicationContext(), "Press on the screen to focus", Toast.LENGTH_SHORT);
                toast.setGravity(0,0,120);
                toast.show();

                mPreview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View b, MotionEvent event) {
                        if (event.getAction() != MotionEvent.ACTION_DOWN)
                            return true;
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                            float x = event.getX();
                            float y = event.getY();

                            Rect touchRect = new Rect(
                                    (int) (x - 100),
                                    (int) (y - 100),
                                    (int) (x + 100),
                                    (int) (y + 100));

                            final Rect targetFocusRect = new Rect(
                                    touchRect.left * 2000 / b.getWidth() - 1000,
                                    touchRect.top * 2000 / b.getHeight() - 1000,
                                    touchRect.right * 2000 / b.getWidth() - 1000,
                                    touchRect.bottom * 2000 / b.getHeight() - 1000);

                            mPreview.doTouchFocus(targetFocusRect);

                            addColorBtn.setEnabled(true);
                            focusBtn.setEnabled(true);

                            //mPreview.performClick();
                            return true;
                        }
                        return true;
                    }
                });
                //v.performClick();
            }
        });

       addColorBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                focusBtn.setEnabled(false);
                addColorBtn.setEnabled(false);
                Toast toast = Toast.makeText(getApplicationContext(), "Pick a color", Toast.LENGTH_SHORT);
                toast.setGravity(0,0,120);
                toast.show();

                mPreview.setOnTouchListener( new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View n, MotionEvent event) {
                        if (event.getAction() != MotionEvent.ACTION_DOWN)
                            return true;
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                            final int x = (int) event.getX();
                            final int y = (int) event.getY();
                            //final int sourceColor = v.getDrawingCache().getPixel((int) x, (int) y);
                            //v.setBackgroundColor(sourceColor);
                            //ImageView img = ((ImageView)n);
                            //Bitmap imgbmp = Bitmap.createBitmap(img.getDrawingCache());
                            //img.setDrawingCacheEnabled(false);
                            /*Bitmap imgbmp = Bitmap.createBitmap(mPreview.getDrawingCache());

                            try {
                                int pixel = imgbmp.getPixel(x, y);
                                int redValue = Color.red(pixel);
                                int blueValue = Color.blue(pixel);
                                int greenValue = Color.green(pixel);

                                Toast toast1 = Toast.makeText(getApplicationContext(), redValue, Toast.LENGTH_SHORT);
                                toast1.show();
                            }catch (Exception ignore){
                            }
                            imgbmp.recycle();*/
                            final int sourceColor = mPreview.getDrawingCache().getPixel((int) x, (int) y);
                            Toast toast1 = Toast.makeText(getApplicationContext(), Color.red(sourceColor), Toast.LENGTH_SHORT);
                            toast1.show();

                            //Toast toast1 = Toast.makeText(getApplicationContext(), redValue, Toast.LENGTH_SHORT);
                            //toast1.show();
                            focusBtn.setEnabled(true);
                            addColorBtn.setEnabled(true);
                            //mPreview.performClick();

                            return true;
                        }
                        return true;
                    }
                });
                //v.performClick();
            }
        });


    }

    private Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    @Override
    public void onFocused() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1500);
    }

}
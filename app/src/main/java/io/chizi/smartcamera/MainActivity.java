package io.chizi.smartcamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

public class MainActivity extends AppCompatActivity {

    private CameraView cameraView;
    private ImageView flashButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = (CameraView) findViewById(R.id.camera);
        flashButton = (ImageView) findViewById(R.id.flashButton);

        flashButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        if (cameraView.getFlash() == CameraKit.Constants.FLASH_OFF) {
                            cameraView.setFlash(CameraKit.Constants.FLASH_AUTO);
                            flashButton.setImageResource(R.drawable.ic_flash_auto);
                        } else if (cameraView.getFlash() == CameraKit.Constants.FLASH_AUTO) {
                            cameraView.setFlash(CameraKit.Constants.FLASH_ON);
                            flashButton.setImageResource(R.drawable.ic_flash_on);
                        } else {
                            cameraView.setFlash(CameraKit.Constants.FLASH_OFF);
                            flashButton.setImageResource(R.drawable.ic_flash_off);
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }
    
    public void takePicture(View view) {
//        captureStartTime = System.currentTimeMillis();

        cameraView.captureImage(new CameraKitEventCallback<CameraKitImage>() {
            @Override
            public void callback(CameraKitImage cameraKitImage) {
                imageCaptured(cameraKitImage);
            }
        });

    }

    public void imageCaptured(CameraKitImage image) {
        byte[] jpeg = image.getJpeg();
        long callbackTime = System.currentTimeMillis();

        // Create a bitmap
        Bitmap result = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
        Toast.makeText(MainActivity.this, "拍照成功！", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

}

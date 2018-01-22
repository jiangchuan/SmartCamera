package io.chizi.smartcamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

public class MainActivity extends AppCompatActivity {

    CameraView camera;
    private int cameraMethod = CameraKit.Constants.METHOD_STANDARD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = (CameraView) findViewById(R.id.camera);

        camera.setMethod(cameraMethod);



    }

    public void takePicture(View view) {
//        captureStartTime = System.currentTimeMillis();

        camera.captureImage(new CameraKitEventCallback<CameraKitImage>() {
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
        camera.start();
    }

    @Override
    protected void onPause() {
        camera.stop();
        super.onPause();
    }

}

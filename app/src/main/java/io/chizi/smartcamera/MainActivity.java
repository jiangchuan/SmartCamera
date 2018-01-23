package io.chizi.smartcamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final float HORIZONTAL_RATIO_W = 4.3f;
    private static final float HORIZONTAL_RATIO_H = 6.0f;
    private static final float RATIO_ENLARGE2 = 2.0f;

    //    private static final long THREE_MINUTES = 3 * 60 * 1000;
    private static final long THREE_MINUTES = 10 * 1000;

    private String homePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ticketcamera";

    long lastTimeMilis = 0;
    long timeMilis = 0;

    String todayPath;
    String ticketPath;


    String dayTimeStr;
    String minuteTimeStr;
    String secondTimeStr;

    SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat minuteSdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
    SimpleDateFormat secondSdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

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

    private void getCurrentTime() {
        Calendar now = Calendar.getInstance();
        dayTimeStr = daySdf.format(now.getTime());
        minuteTimeStr = minuteSdf.format(now.getTime());
        secondTimeStr = secondSdf.format(now.getTime());
        timeMilis = now.getTimeInMillis();
    }

    private void makeTodayDir() {
        todayPath = homePath + "/" + dayTimeStr;
        File dir = new File(todayPath);
        dir.mkdirs();
    }

    private void makeTicketDir() {
        ticketPath = todayPath + "/" + minuteTimeStr;
        File dir = new File(ticketPath);
        dir.mkdirs();
    }

    public void takePicture(View view) {
        getCurrentTime();
        if (timeMilis - lastTimeMilis > THREE_MINUTES) {
            makeTodayDir();
            makeTicketDir();
        }
        lastTimeMilis = timeMilis;

        cameraView.captureImage(new CameraKitEventCallback<CameraKitImage>() {
            @Override
            public void callback(CameraKitImage cameraKitImage) {
                imageCaptured(cameraKitImage);
            }
        });

    }

    public void imageCaptured(CameraKitImage image) {
        byte[] jpeg = image.getJpeg();

        // Write the image in a file (in jpeg format)
        String imageName = ticketPath + "/" + secondTimeStr + ".jpg";
        try {
            FileOutputStream fos = new FileOutputStream(imageName);
            fos.write(jpeg);
            fos.close();
        } catch (java.io.IOException e) {
            Log.e("SmartCamera", "Exception in photoCallback", e);
        }


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

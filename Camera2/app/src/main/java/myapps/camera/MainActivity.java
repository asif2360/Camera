package myapps.camera;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {


    Camera camera;
    Button but_cap, but_capture;
    SurfaceView camView;
    SurfaceHolder holder;
    //MediaPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camView = (SurfaceView) findViewById(R.id.surfaceView);
        holder = camView.getHolder();
        holder.addCallback(this);
        holder.setType(holder.SURFACE_TYPE_PUSH_BUFFERS);

        but_cap = (Button) findViewById(R.id.capImage);

        but_capture = (Button) findViewById(R.id.capture);

        but_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeviceSupportcamera()) {
                    Toast.makeText(MainActivity.this, "Sorry the device does not support camera", Toast.LENGTH_SHORT).show();

                }

                start_camera();
            }
        });

        but_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  player=MediaPlayer.create(MainActivity.this,R.raw.akkad);
               // player.start();

                camera.takePicture(null, null, null, mPictureCallback);
            }
        });

    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera c) {

            FileOutputStream outStream = null;
            try {

                outStream = new FileOutputStream("/sdcard/AndroidCodec_" +
                        System.currentTimeMillis() + ".jpg");
                outStream.write(data);
                outStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }

        }
    };

    private void start_camera() {
        try {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to capture image", Toast.LENGTH_SHORT).show();
        }
        try {

            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to set preview", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isDeviceSupportcamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
       // player.stop();
    }


}

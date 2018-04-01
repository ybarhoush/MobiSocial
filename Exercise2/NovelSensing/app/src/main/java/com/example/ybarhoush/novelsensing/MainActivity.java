package com.example.ybarhoush.novelsensing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mMagnet;

    private int threshold = 95;

    private TextView textView_onSensorValue;
    private TextView textView_onSensorStatus;

    MediaPlayer metal_sound;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagnet = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        textView_onSensorValue = findViewById(R.id.textView_onSensorValue);
        textView_onSensorStatus = findViewById(R.id.textView_onSensorStatus);

        metal_sound = MediaPlayer.create(this, R.raw.metal_sound_short);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // Get sensor values from each axis
        float magnet_X = event.values[0];
        float magnet_Y = event.values[1];
        float magnet_Z = event.values[2];
        double magnetoMagnitude = Math.sqrt((Math.pow(magnet_X, 2)) + (Math.pow(magnet_Y, 2)) +
                (Math.pow(magnet_Z, 2)));

        Log.d("myTag", String.valueOf(magnetoMagnitude));
        textView_onSensorValue.setText(String.format("%.2f", magnetoMagnitude) + " \u00B5Tesla");

        //update metal detection text according to the threshold
        if (magnetoMagnitude > threshold) {
        // update detection text and background color and play beep sound
            textView_onSensorStatus.setText("Higher then threshold!");
            textView_onSensorStatus.setBackgroundColor(Color.BLUE);
            textView_onSensorStatus.setTextColor(Color.RED);
            metal_sound.start();
        } else {
        // update detection text and background color
            textView_onSensorStatus.setText("Lower then threshold :(");
            textView_onSensorStatus.setBackgroundColor(Color.RED);
            textView_onSensorStatus.setTextColor(Color.BLUE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mMagnet, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
package com.nire.medx.diary.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.nire.medx.R;

public class PulseMeterActivity extends AppCompatActivity {

    private int pulse;
    int seconds;
    private boolean isProcess = false;

    ConstraintLayout startPulseLayout;
    ConstraintLayout processPulseLayout;

    TextView pulseMeterCounter;
    TextView pulseMeterSeconds;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_meter);
        this.startPulseLayout = findViewById(R.id.startPulseLayout);
        this.processPulseLayout = findViewById(R.id.processPulseLayout);
        this.pulseMeterCounter = findViewById(R.id.pulseMeterCounter);
        this.pulseMeterSeconds = findViewById(R.id.pulseMeterSeconds);
        this.handler = new Handler();
    }

    public void onClickStartPulseMeter(View view) {
        startPulseLayout.setVisibility(View.INVISIBLE);
        processPulseLayout.setVisibility(View.VISIBLE);
        isProcess = true;
        pulse = 0;
        seconds = 60;
        new SecondsThread().start();
    }

    public void onClickAddPulse(View view) {
        if (isProcess) {
            pulse++;
            pulseMeterCounter.setText(String.format("Пульс: %s", pulse));
        }
    }

    class SecondsThread extends Thread {
        @Override
        public void run() {
            for (int i = seconds; i > 0; i--) {
                int finalI = i;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pulseMeterSeconds.setText(String.format("Время: %s", finalI));
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isProcess = false;
            setResult(RESULT_OK, new Intent().putExtra("pulse", pulse));
            finish();
        }
    }
}
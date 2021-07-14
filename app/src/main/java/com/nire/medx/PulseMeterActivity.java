package com.nire.medx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PulseMeterActivity extends AppCompatActivity {

    private int pulse;
    int seconds;
    private boolean isProcess = false;
//    String upperPressure;
//    String lowerPressure;
//    String note;

    ConstraintLayout startPulseLayout;
    ConstraintLayout processPulseLayout;

    TextView pulseMeterCounter;
    TextView pulseMeterSeconds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_meter);
        this.startPulseLayout = findViewById(R.id.startPulseLayout);
        this.processPulseLayout = findViewById(R.id.processPulseLayout);
        this.pulseMeterCounter = findViewById(R.id.pulseMeterCounter);
        this.pulseMeterSeconds = findViewById(R.id.pulseMeterSeconds);

//        Intent intent =getIntent();
//        this.upperPressure = intent.getStringExtra("upperPressure");
//        this.lowerPressure = intent.getStringExtra("lowerPressure");
//        this.note = intent.getStringExtra("note");
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
                pulseMeterSeconds.setText(String.format("Время: %s", i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isProcess = false;
//            Intent intent = new Intent(getApplicationContext(), CreateEntryActivity.class);
//            intent.putExtra("pulse", pulse);
//            intent.putExtra("upperPressure", upperPressure);
//            intent.putExtra("lowerPressure", lowerPressure);
//            intent.putExtra("note", note);
            setResult(RESULT_OK, new Intent().putExtra("pulse", pulse));
            finish();
        }
    }
}
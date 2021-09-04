package com.nire.medx;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CreateEntryActivity extends AppCompatActivity {

    EditText note;
    EditText upperPressure;
    EditText lowerPressure;
    EditText pulse;
    Realm realm;

    ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result) -> {
            if (result.getResultCode() == RESULT_OK) {
                pulse.setText("" + result.getData().getIntExtra("pulse", 0));
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);
        this.note = findViewById(R.id.note);
        this.upperPressure = findViewById(R.id.upperPressure);
        this.lowerPressure = findViewById(R.id.lowerPressure);
        this.pulse = findViewById(R.id.pulse);
        Intent intent = getIntent();
        pulse.setText("" + intent.getIntExtra("pulse", 0));
        upperPressure.setText(intent.getStringExtra("upperPressure"));
        lowerPressure.setText(intent.getStringExtra("lowerPressure"));
        note.setText(intent.getStringExtra("note"));
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        this.realm = Realm.getInstance(config);
        ConstraintLayout layout = findViewById(R.id.create_activity_constraint);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                layout.setMinHeight(layout.getHeight());
            }
        });

    }

    public void onClickSaveEntry(View view) {
        if (upperPressure.getText().toString().equals("") || lowerPressure.getText().toString().equals("") || pulse.getText().toString().equals("")) {
            Toast.makeText(this, "Все поля кроме заметки обязательны", Toast.LENGTH_SHORT).show();
        } else {

            realm.beginTransaction();
            DiaryRealmObject diaryRealmObject = realm.createObject(DiaryRealmObject.class);
            Date date = Calendar.getInstance().getTime();
            diaryRealmObject.setDatetime(date);
            diaryRealmObject.setUpperPressure(Integer.parseInt(upperPressure.getText().toString()));
            diaryRealmObject.setLowerPressure(Integer.parseInt(lowerPressure.getText().toString()));
            diaryRealmObject.setNote(note.getText().toString());
            diaryRealmObject.setPulse(Integer.parseInt(pulse.getText().toString()));
            realm.commitTransaction();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void onClickOpenPulseMeter(View view) {
        Intent intent = new Intent(this, PulseMeterActivity.class);
        intentActivityResultLauncher.launch(intent);
    }
}
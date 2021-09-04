package com.nire.medx;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EditEntryActivity extends AppCompatActivity {

    EditText note;
    EditText upperPressure;
    EditText lowerPressure;
    EditText pulse;
    Button button;

    Realm realm;

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        this.realm = Realm.getInstance(config);

        setContentView(R.layout.activity_edit_entry);
        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Date entryDatetime = (Date) intent.getSerializableExtra("entryDatetime");
        realm.beginTransaction();
        DiaryRealmObject result = realm.where(DiaryRealmObject.class).equalTo("datetime", entryDatetime).findFirst();
        realm.cancelTransaction();
        assert result != null;

        upperPressure = findViewById(R.id.upperPressure);
        lowerPressure = findViewById(R.id.lowerPressure);
        pulse = findViewById(R.id.pulse);
        note = findViewById(R.id.note);
        button = findViewById(R.id.button);


        upperPressure.setText((Integer.toString(result.getUpperPressure())));
        lowerPressure.setText((Integer.toString(result.getLowerPressure())));
        pulse.setText((Integer.toString(result.getPulse())));
        note.setText((result.getNote()));

        button.setOnClickListener(view -> {
            realm.beginTransaction();
            DiaryRealmObject editingEntry = realm.where(DiaryRealmObject.class).equalTo("datetime", entryDatetime).findFirst();
            editingEntry.setUpperPressure(Integer.parseInt(upperPressure.getText().toString()));
            editingEntry.setLowerPressure(Integer.parseInt(lowerPressure.getText().toString()));
            editingEntry.setNote(note.getText().toString());
            editingEntry.setPulse(Integer.parseInt(pulse.getText().toString()));
            realm.commitTransaction();
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        });
    }
}
package com.nire.medx.diary.edit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.nire.medx.R;
import com.nire.medx.diary.list.MainActivity;
import com.nire.medx.diary.DiaryRealmObject;

import java.util.Date;

public class EditEntryActivity extends AppCompatActivity {

    EditText note;
    EditText upperPressure;
    EditText lowerPressure;
    EditText pulse;
    Button button;

    EditPresenter presenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.close();
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new EditPresenter();

        setContentView(R.layout.activity_edit_entry);
        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Date entryDatetime = (Date) intent.getSerializableExtra("entryDatetime");
        DiaryRealmObject result = presenter.retrieveEntry(entryDatetime);
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
            presenter.saveEntry(
                    entryDatetime,
                    Integer.parseInt(upperPressure.getText().toString()),
                    Integer.parseInt(lowerPressure.getText().toString()),
                    note.getText().toString(),
                    Integer.parseInt(pulse.getText().toString())
                    );
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        });
    }
}
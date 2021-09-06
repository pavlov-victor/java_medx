package com.nire.medx.diary.edit;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import com.nire.medx.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CreateEntryActivity extends AppCompatActivity {

    EditText note;
    EditText upperPressure;
    EditText lowerPressure;
    EditText pulse;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;

    EditPresenter presenter;

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
        presenter.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result) -> {
            if (result.getResultCode() == RESULT_OK) {
                pulse.setText("" + result.getData().getIntExtra("pulse", 0));
            }
        });

        super.onCreate(savedInstanceState);
        presenter = new EditPresenter();
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

            presenter.saveEntry(
                    null,
                    Integer.parseInt(upperPressure.getText().toString()),
                    Integer.parseInt(lowerPressure.getText().toString()),
                    note.getText().toString(),
                    Integer.parseInt(pulse.getText().toString())
            );
            finish();
        }
    }

    public void onClickOpenPulseMeter(View view) {
        Intent intent = new Intent(this, PulseMeterActivity.class);
        intentActivityResultLauncher.launch(intent);
    }
}
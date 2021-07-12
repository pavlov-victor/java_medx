package com.nire.medx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    Button addEntryButton;
    ListView diaryListView;
    List<DiaryRealmObject> realmEntries = Collections.emptyList();
    ArrayAdapter<DiaryRealmObject> realmAdapter;
    Realm realm;


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.addEntryButton = findViewById(R.id.addEntryButton);
        this.diaryListView = findViewById(R.id.diaryListView);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        this.realm = Realm.getInstance(config);

        realmEntries = realm.where(DiaryRealmObject.class).sort("datetime", Sort.DESCENDING).findAll();

        this.realmAdapter = new ArrayAdapter<DiaryRealmObject>(this, R.layout.entry, new ArrayList<>(realmEntries)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                DiaryRealmObject entry = getItem(position);
                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.entry, null, false);
                }
                TextView entryUpperPressure =
                        (TextView) convertView.findViewById(R.id.upperPressure);
                TextView entryLowerPressure =
                        (TextView) convertView.findViewById(R.id.lowerPressure);
                TextView entryPulse =
                        (TextView) convertView.findViewById(R.id.pulse);
                TextView entryNote =
                        (TextView) convertView.findViewById(R.id.note);
                TextView entryDatetime =
                        (TextView) convertView.findViewById(R.id.datetime);

                entryUpperPressure.setText(String.format(getResources().getConfiguration().locale,"%d", entry.getUpperPressure()));
                entryLowerPressure.setText("" + entry.getLowerPressure());
                entryPulse.setText("" + entry.getPulse());
                entryNote.setText("" + entry.getNote());
                entryDatetime.setText("" + entry.getDatetime());
                return convertView;
            }
        };

        diaryListView.setAdapter(realmAdapter);
        diaryListView.setOnItemLongClickListener((AdapterView<?> adapterView, View view, int i, long l) -> {
            DiaryRealmObject entry = realmEntries.get(i);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Удаление записи");
            builder.setMessage("Вы хотите удалить запись от " + entry.getDatetime().toString() + "?");
            builder.setPositiveButton("Удалить", (dialogInterface, i1) -> {
                realm.beginTransaction();
                RealmResults<DiaryRealmObject> result = realm.where(DiaryRealmObject.class).equalTo("datetime", entry.getDatetime()).findAll();
                result.deleteAllFromRealm();
                realm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Удалено", Toast.LENGTH_SHORT).show();
                realmEntries = realm.where(DiaryRealmObject.class).sort("datetime", Sort.DESCENDING).findAll();
                realmAdapter.clear();
                realmAdapter.addAll(realmEntries);
                realmAdapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("отменить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "Вы отменили действие", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        });
    }


    public void onClickCreateEntry(View view) {
        Intent intent = new Intent(this, CreateEntryActivity.class);
        startActivity(intent);
    }
}
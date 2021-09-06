package com.nire.medx.diary.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nire.medx.diary.edit.CreateEntryActivity;
import com.nire.medx.diary.edit.EditEntryActivity;
import com.nire.medx.R;
import com.nire.medx.diary.DiaryRealmObject;
import com.nire.medx.utils.RealmBaseAdapter;

import java.util.Collections;
import java.util.List;

import io.realm.OrderedRealmCollection;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addEntryButton;
    ListView diaryListView;
    OrderedRealmCollection<DiaryRealmObject> realmEntries;
    DiaryListPresenter presenter;

    @Nullable
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            presenter.close();
            presenter = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.addEntryButton = findViewById(R.id.addEntryButton);
        this.diaryListView = findViewById(R.id.diaryListView);

        presenter = (DiaryListPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new DiaryListPresenter();
        }

        realmEntries = presenter.getDiaries();

        RealmBaseAdapter<DiaryRealmObject> realmAdapter = new RealmBaseAdapter<DiaryRealmObject>(realmEntries) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                DiaryRealmObject entry = getItem(position);
                DiaryHolder holder;
                if (convertView == null) {
                    holder = new DiaryHolder(parent);
                    holder.root.setTag(holder);
                } else {
                    holder = (DiaryHolder) convertView.getTag();
                }
                assert entry != null;
                holder.entryUpperPressure.setText(String.format(getResources().getConfiguration().locale, "%d", entry.getUpperPressure()));
                holder.entryLowerPressure.setText(String.format("%s", entry.getLowerPressure()));
                holder.entryPulse.setText(String.format("%s", entry.getPulse()));
                holder.entryNote.setText(entry.getNote());
                holder.entryDatetime.setText(entry.getDatetime().toString());
                return holder.root;
            }
        };

        diaryListView.setAdapter(realmAdapter);
        diaryListView.setOnItemLongClickListener((AdapterView<?> adapterView, View view, int i, long l) -> {
            DiaryRealmObject entry = realmEntries.get(i);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Удаление записи");
            builder.setMessage("Вы хотите удалить запись от " + entry.getDatetime().toString() + "?");
            builder.setPositiveButton("Удалить", (dialogInterface, i1) -> {
                presenter.deleteDiary(entry);
                Toast.makeText(getApplicationContext(), "Удалено", Toast.LENGTH_SHORT).show();
                realmAdapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("Редактировать", (dialogInterface, i12) -> {
                Toast.makeText(getApplicationContext(), "Вы открыли редактирование", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, EditEntryActivity.class);
                intent.putExtra("entryDatetime", entry.getDatetime());
                startActivity(intent);
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
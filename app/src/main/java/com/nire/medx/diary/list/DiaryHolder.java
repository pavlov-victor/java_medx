package com.nire.medx.diary.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nire.medx.R;

public class DiaryHolder {
    public final View root;
    public final TextView entryUpperPressure;
    public final TextView entryLowerPressure;
    public final TextView entryPulse;
    public final TextView entryNote;
    public final TextView entryDatetime;


    public DiaryHolder(ViewGroup parent) {
        Context context = parent.getContext();
        root = LayoutInflater.from(context)
                .inflate(R.layout.entry, parent, false);
        entryUpperPressure = root.findViewById(R.id.upperPressure);
        entryLowerPressure = root.findViewById(R.id.lowerPressure);
        entryPulse = root.findViewById(R.id.pulse);
        entryNote = root.findViewById(R.id.note);
        entryDatetime = root.findViewById(R.id.datetime);
    }

}

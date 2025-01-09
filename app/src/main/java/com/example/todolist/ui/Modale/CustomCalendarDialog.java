package com.example.todolist.ui.Modale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.devmobile.todolistBertaudLeroi.R;

public class CustomCalendarDialog extends Dialog {

    private OnDateSelectedListener onDateSelectedListener;

    public interface OnDateSelectedListener {
        void onDateSelected(int day, int month, int year);
    }

    public CustomCalendarDialog(@NonNull Context context, int theme, OnDateSelectedListener listener) {
        super(context,theme);
        this.onDateSelectedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_modale);

        DatePicker datePicker = findViewById(R.id.datePicker);
        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnConfirm = findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                if (onDateSelectedListener != null) {
                    onDateSelectedListener.onDateSelected(day, month, year);
                }
                dismiss();
            }
        });
    }
}

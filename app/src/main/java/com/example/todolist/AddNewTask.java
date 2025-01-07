package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devmobile.todolistBertaudLeroi.R;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper;
import com.example.todolist.services.FirebaseService;
import com.example.todolist.ui.Modale.CustomCalendarDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    private EditText mEditText;
    private Button mSaveButton;
    private Button buttonCalendar;
    private TextView textViewDate;


    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_new_task, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText= view.findViewById(R.id.editText);
        mSaveButton = view.findViewById(R.id.addButton);
        buttonCalendar = view.findViewById(R.id.buttonCalendar);
        textViewDate = view.findViewById(R.id.textViewDate);

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);

            if(task.length() > 0) {
                mSaveButton.setEnabled(false);
            }
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")) {
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                } else {
                    mSaveButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                FirebaseService fbs = new FirebaseService(getContext());
                if(finalIsUpdate) {
                    Log.d(TAG, "onClick: ");
                    fbs.getTask(bundle.getString("Id"), new FirebaseService.OnTaskCompleteListener() {
                        @Override
                        public void onSuccess(ToDoModel task) {
                            // Handle the retrieved task here
                            fbs.updateTask(task,text);
                            dismiss();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            // Handle the failure case here
                            Toast.makeText(getContext(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;

                } else {
                    ToDoModel item;
                    if (textViewDate.getText().toString().isEmpty()){
                        item = new ToDoModel(text,fbs.getCurrentUser().getEmail(), 0,0);
                    } else{
                        item = new ToDoModel(text,textViewDate.getText().toString(),fbs.getCurrentUser().getEmail(), 0,0);
                    }
                    fbs.addTask(item);
                }


                dismiss();
            }
        });

        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomCalendarDialog dialog = new CustomCalendarDialog(v.getContext(), new CustomCalendarDialog.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int day, int month, int year) {
                        if (month < 10){
                            String selectedDate = day + "/0" + (month + 1) + "/" + year;
                            textViewDate.setText(selectedDate);
                        } else{
                            String selectedDate = day + "/" + (month + 1) + "/" + year;
                            textViewDate.setText(selectedDate);
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}

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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    private EditText mEditText;
    private Button mSaveButton;
    private Button buttonCalendar;
    private TextView textViewDate;
    public Context context;



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
        context = this.getContext();

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);
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
                if (text.isEmpty()) {
                    Toast.makeText(getContext(), "Veuillez entrer une tâche", Toast.LENGTH_SHORT).show();
                    return;
                }
                String date = textViewDate.getText().toString();
                FirebaseService fbs =  FirebaseService.getInstance(getContext());

                if(finalIsUpdate) {
                    Log.d(TAG, "onClick: ");
                    fbs.getTask(bundle.getString("Id"), new FirebaseService.OnTaskCompleteListener() {
                        @Override
                        public void onSuccess(ToDoModel task) {
                            fbs.updateTask(task, text, date);
                            sendDiscordWebhook("Tâche mise à jour : " + text + " (Date : " + date + ")");
                            NotificationService.SendNotification("Tâche mise à jour",text,context);
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
                    if (textViewDate.getText().toString().isEmpty()) {
                        item = new ToDoModel(text, fbs.getCurrentUser().getEmail(), 0, 0);
                    } else {
                        item = new ToDoModel(text, textViewDate.getText().toString(), fbs.getCurrentUser().getEmail(), 0, 0);
                    }
                    fbs.addTask(item);
                    sendDiscordWebhook("Nouvelle tâche ajoutée : " + text + " (Date : " + (date.isEmpty() ? "Non spécifiée" : date) + ")");
                    NotificationService.SendNotification("Nouvelle tache",text,context);
                    Log.d(TAG, "Appel de saveTaskToNotion (create)");
                    saveTaskToNotion(text);
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

    private void sendDiscordWebhook(String message) {
        String webhookUrl = "https://discord.com/api/webhooks/1326202033996566590/L4jPUB5Kt8rIgPr9TLXlCx-wHMKVtrFwAJ-Eli65iCW0HvqYTJje9nXgTvmeO3SICA4T";

        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.get("application/json; charset=utf-8"); // Ajout du type MIME
            String json = "{\"content\": \"" + message + "\"}";

            RequestBody body = RequestBody.create(json, JSON); // Utilisation de MediaType
            Request request = new Request.Builder()
                    .url(webhookUrl)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.d(TAG, "Message envoyé à Discord avec succès : " + message);
                } else {
                    Log.e(TAG, "Échec lors de l'envoi du message à Discord : " + response.message());
                }
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de l'envoi au webhook Discord", e);
            }
        }).start();
    }

    private void saveTaskToNotion(String taskName) {
        String notionApiUrl = "https://api.notion.com/v1/pages";
        String databaseId = "1764883f69588035b86aefd7abdda153"; // Utilisez votre propre database_id
        String token = "Bearer secret_N401BJTyDoYu1xAwRfrX6Mn5Nnvkb8uAByDyHjwWJru"; // Remplacez par votre propre token

        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.get("application/json; charset=utf-8");

            // JSON formaté selon la documentation officielle
            String json = "{\n" +
                    "  \"parent\": { \"type\": \"database_id\", \"database_id\": \"" + databaseId + "\" },\n" +
                    "  \"properties\": {\n" +
                    "    \"content\": {\n" +
                    "      \"type\": \"title\",\n" +
                    "      \"title\": [\n" +
                    "        { \"type\": \"text\", \"text\": { \"content\": \"" + taskName + "\" } }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";

            Log.d(TAG, "JSON généré pour Notion : " + json);

            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .url(notionApiUrl)
                    .post(body)
                    .addHeader("Authorization", token)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Notion-Version", "2022-06-28")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.d(TAG, "Tâche ajoutée à Notion avec succès : " + taskName);
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Réponse vide";
                    Log.e(TAG, "Échec lors de l'ajout à Notion : Code HTTP " + response.code());
                    Log.e(TAG, "Détails de l'erreur : " + errorBody);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception lors de l'envoi à Notion", e);
            }
        }).start();
    }



}

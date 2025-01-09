package com.example.todolist.services;

import static com.example.todolist.AddNewTask.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCallsService   {


    public static void sendDiscordWebhook(String message, Context context) {
        String webhookUrl = "https://discord.com/api/webhooks/1326202033996566590/L4jPUB5Kt8rIgPr9TLXlCx-wHMKVtrFwAJ-Eli65iCW0HvqYTJje9nXgTvmeO3SICA4T";

        SharedPreferences pref = context.getSharedPreferences("com.devmobile.todolistBertaudLeroi_preferences", Context.MODE_PRIVATE);
        boolean bNOtifDiscord =pref.getBoolean("SendNotifDiscord",false);
        if (!bNOtifDiscord){
           return;
        }

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

    public static void saveTaskToNotion(String taskName, Context context) {
        SharedPreferences pref = context.getSharedPreferences("com.devmobile.todolistBertaudLeroi_preferences", Context.MODE_PRIVATE);
        boolean bSyncNotion = pref.getBoolean("SyncNotion", false);
        if (!bSyncNotion) {
            return;
        }

        String notionApiUrl = "https://api.notion.com/v1/pages";
        String databaseId = pref.getString("lienNotion", "");
        String token = "Bearer "+pref.getString("cleApiNotion", "");

        Handler handler = new Handler(Looper.getMainLooper()); // Ensure Toast runs on the main thread

        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
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

            Log.d("TAG", "JSON généré pour Notion : " + json);

            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .url(notionApiUrl)
                    .post(body)
                    .addHeader("Authorization", token)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Notion-Version", "2022-06-28")
                    .build();

            boolean success = true;

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Réponse vide";
                    Log.e("TAG", "Échec lors de l'ajout à Notion : Code HTTP " + response.code());
                    Log.e("TAG", "Détails de l'erreur : " + errorBody);
                    success = false;
                }
            } catch (Exception e) {
                Log.e("TAG", "Exception lors de la requête Notion : ", e);
                success = false;
            }

            // Pass the Toast back to the main thread
            boolean finalSuccess = success;
            handler.post(() -> {
                if (!finalSuccess) {
                    Toast.makeText(context, "Erreur lors de l'envoi à Notion : vérifiez votre clé API et votre lien de base de données", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Tâche ajoutée à Notion avec succès", Toast.LENGTH_SHORT).show();
                }
            });

        }).start();
    }

}

package com.example.todolist.Model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;

import com.devmobile.todolistBertaudLeroi.R;
import com.example.todolist.services.NotificationService;

import java.util.Map;

public class MyCompatActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //NotificationService service = new NotificationService(this);
        //startService(new Intent(this, NotificationService.class));
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},4
            );
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        setTheme();
        super.onResume();
    }

    @Override
    protected void onStop() {
        //NotificationService service = new NotificationService(this);
        //startService(new Intent(this, NotificationService.class));
        super.onStop();
    }

    private  void setTheme(){
        SharedPreferences pref = getSharedPreferences("com.devmobile.todolistBertaudLeroi_preferences", MODE_PRIVATE);
        String theme =pref.getString("Theme","");
        switch (theme){

            case "Dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "System":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }
}

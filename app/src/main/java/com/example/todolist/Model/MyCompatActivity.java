package com.example.todolist.Model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;

import com.devmobile.todolistBertaudLeroi.R;

import java.util.Map;

public class MyCompatActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        setTheme();
        super.onResume();
    }


    private  void setTheme(){
        SharedPreferences pref = getSharedPreferences("com.devmobile.todolistBertaudLeroi_preferences", MODE_PRIVATE);
        String theme =pref.getString("Theme","");
         Map<String, ?> t = pref.getAll();
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

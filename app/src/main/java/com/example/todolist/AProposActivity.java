package com.example.todolist;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;

import com.devmobile.todolistBertaudLeroi.R;
import com.example.todolist.Model.MyCompatActivity;

public class AProposActivity extends MyCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
        ImageView icon = findViewById(R.id.iconAPropos);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            icon.setImageResource(R.drawable.icon_white);
        } else{
            icon.setImageResource(R.drawable.icon);
        }


    }


}

package com.example.todolist.Model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import com.example.todolist.services.FirebaseService;

public class BackConfirmActivity extends MyCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BackConfirmActivity.this);
                alertDialogBuilder.setMessage("Voulez vous vraiment quitter l'application ?");
                alertDialogBuilder.setCancelable(true)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish();
                            }
                        })
                        .setNegativeButton("non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void finish() {
        SharedPreferences pref = getSharedPreferences("com.devmobile.todolistBertaudLeroi_preferences", MODE_PRIVATE);
        boolean bConnect =pref.getBoolean("stayConnected",true);
        if (!bConnect){
            FirebaseService.getInstance(this).logOut();
        }

        super.finish();

    }
}

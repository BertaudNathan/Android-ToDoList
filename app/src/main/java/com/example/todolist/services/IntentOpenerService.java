package com.example.todolist.services;

import android.content.Context;
import android.content.Intent;

import com.example.todolist.LoginRegisterActivity;
import com.example.todolist.MainActivity;
import com.example.todolist.PreferenceActivity;

public  class IntentOpenerService {

    public static void OpenMainActivity(Context ctx,Boolean RemoveFromBackStack){
        Intent intent = new Intent(ctx, MainActivity.class);
        if (RemoveFromBackStack){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        ctx.startActivity(intent);
    }

    public static void OpenPreferenceActivity(Context ctx,Boolean RemoveFromBackStack){
        Intent intent = new Intent(ctx, PreferenceActivity.class);
        if (RemoveFromBackStack){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        ctx.startActivity(intent);
    }

    public static void OpenLoginActivity(Context ctx,Boolean RemoveFromBackStack){
        Intent intent = new Intent(ctx, LoginRegisterActivity.class);
        if (RemoveFromBackStack){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        ctx.startActivity(intent);
    }

}

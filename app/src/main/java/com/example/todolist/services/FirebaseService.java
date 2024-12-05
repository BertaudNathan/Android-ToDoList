package com.example.todolist.services;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseService {

    private FirebaseAuth mAuth;

    public FirebaseService(){
        this.mAuth = FirebaseAuth.getInstance();

    }

    public boolean createUser(String email, String password){
        final FirebaseUser[] connUser = {null};
        return mAuth.createUserWithEmailAndPassword(email, password).isSuccessful();
    }
}




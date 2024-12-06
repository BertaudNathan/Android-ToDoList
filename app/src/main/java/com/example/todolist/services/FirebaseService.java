package com.example.todolist.services;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class FirebaseService {

    private FirebaseAuth mAuth;

    private Context context;

    public FirebaseService(Context ctx){
        this.mAuth = FirebaseAuth.getInstance();
        this.context = ctx;

    }

    public boolean tryLogIn(String email,String password){
        final boolean result = false;
        Task task = mAuth.signInWithEmailAndPassword(email, password);
        if (task.getException() == null) {
            return true;
        } else {
            try {
                throw task.getException();
            }  catch (FirebaseAuthInvalidCredentialsException e) {
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public boolean createUser(String email, String password){
        final FirebaseUser[] connUser = {null};
        Task task = mAuth.createUserWithEmailAndPassword(email, password);
        if (task.getException() == null) {
            return true;
        }
        else {
            try {
                throw task.getException();
            }
            catch (FirebaseAuthWeakPasswordException e) {
                Toast.makeText(context, "Passwor needs to be more than 6 characters", Toast.LENGTH_LONG).show();
            }
            catch (FirebaseAuthEmailException e){
                Toast.makeText(context, "Invalid Email format", Toast.LENGTH_LONG).show();
            }
            catch (FirebaseAuthException e){
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}




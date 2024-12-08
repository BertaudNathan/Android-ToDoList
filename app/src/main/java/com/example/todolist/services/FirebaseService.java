package com.example.todolist.services;

import android.content.Context;
import android.widget.Toast;

import com.example.todolist.Model.ToDoModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FirebaseService {

    private FirebaseAuth mAuth;

    private Context context;

    public FirebaseService(Context ctx){
        this.mAuth = FirebaseAuth.getInstance();
        this.context = ctx;

    }

    public ToDoModel getTask(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ToDoModel task = database.getReference("tasks").child(id).get().getResult().getValue(ToDoModel.class);
        return task;
    }

    public boolean addTask(ToDoModel task){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(task.getUuid()).setValue(task);
        return true;
    }

    public boolean updateTask(ToDoModel task,String newText){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(task.getUuid()).child("task").setValue(newText);
        return true;
    }

    public boolean logOut(){
        mAuth.signOut();
        return true;
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

    public List getAllTasks() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        List tasks = database.getReference("tasks").get().getResult().getValue(List.class);
        return tasks;
    }
}




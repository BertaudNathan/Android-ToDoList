package com.example.todolist.services;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Model.callbacks.CreateUserCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseService {

    private static FirebaseService instance;
    private final FirebaseAuth mAuth;
    private final Context context;

    private FirebaseService(Context context) {
        this.mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public static synchronized FirebaseService getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseService(context.getApplicationContext());
        }
        return instance;
    }

    public void getTask(String uuid, OnTaskCompleteListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(uuid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        ToDoModel taskModel = task.getResult().getValue(ToDoModel.class);
                        listener.onSuccess(taskModel);
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public boolean addTask(ToDoModel task) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(task.getUuid()).setValue(task);
        return true;
    }

    public boolean updateTask(ToDoModel task, String newText, String newDate) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(task.getUuid()).child("task").setValue(newText);
        database.getReference("tasks").child(task.getUuid()).child("date").setValue(newDate);
        return true;
    }

    public boolean deleteTask(ToDoModel task) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(task.getUuid()).removeValue();
        return true;
    }

    public void logOut() {
        mAuth.signOut();
    }

    public void tryLogIn(String email, String password,CreateUserCallback callback) {
        Task<AuthResult> task = mAuth.signInWithEmailAndPassword(email, password) .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    callback.onResult("success");
                } else {
                    callback.onResult("le mot de passe doit faire au minimum 6 caractères");
                }
            }
        });

    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void createUser(String email, String password, CreateUserCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onResult("success");
                        } else {
                            callback.onResult("le mot de passe doit faire au minimum 6 caractères");
                        }
                    }
                });
    }

    public void changeTaskStatus(ToDoModel item, boolean b) {
        item.setStatus(b ? 1 : 0);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(item.getUuid()).child("status").setValue(item.getStatus());
    }

    public interface ToDoCallback {
        void onSuccess(List<ToDoModel> tasks);

        void onFailure(String errorMessage);
    }

    public void getTasksForCurrentUser(ToDoCallback callback) {
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (currentUserEmail == null) {
            callback.onFailure("User not logged in");
            return;
        }

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("tasks");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ToDoModel> userTasks = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ToDoModel task = snapshot.getValue(ToDoModel.class);

                    if (task != null && task.getUserEmail() != null && task.getUserEmail().equals(currentUserEmail)) {
                        userTasks.add(task);
                    }

                }
                callback.onSuccess(userTasks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    public interface OnTaskCompleteListener {
        void onSuccess(ToDoModel task);

        void onFailure(Exception e);
    }
}
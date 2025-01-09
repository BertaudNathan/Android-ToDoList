package com.example.todolist.services;

import android.content.Context;
import android.widget.Toast;

import com.example.todolist.Model.ToDoModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseService {

    private FirebaseAuth mAuth;

    private Context context;

    public FirebaseService(Context ctx){
        this.mAuth = FirebaseAuth.getInstance();
        this.context = ctx;

    }

    public void getTask(String uuid, OnTaskCompleteListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(uuid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        ToDoModel taskModel = task.getResult().getValue(ToDoModel.class);
                        listener.onSuccess(taskModel); // Pass the result to the callback
                    } else {
                        listener.onFailure(task.getException()); // Handle any errors
                    }
                });
    }

    public boolean addTask(ToDoModel task){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(task.getUuid()).setValue(task);
        return true;
    }

    public boolean updateTask(ToDoModel task,String newText, String newDate){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(task.getUuid()).child("task").setValue(newText);
        database.getReference("tasks").child(task.getUuid()).child("date").setValue(newDate);
        return true;
    }

    public boolean deleteTask(ToDoModel task){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("tasks").child(task.getUuid()).removeValue();
        return true;
    }

    public boolean logOut(){
        mAuth.signOut();
        return true;
    }

    public boolean tryLogIn(String email,String password){
        final boolean result = false;
        Task<AuthResult> task = mAuth.signInWithEmailAndPassword(email, password);
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
        Task<AuthResult> task = mAuth.createUserWithEmailAndPassword(email, password);
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
                List<ToDoModel>  userTasks = new ArrayList<>();
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







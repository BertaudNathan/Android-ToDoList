package com.example.todolist.Model;

import android.util.Log;

import java.util.UUID;

public class ToDoModel {

    private String task,userEmail,uuid;
    private int id, status;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ToDoModel() {

    }

    public ToDoModel(String task,String userEmail ,int id, int status) {
        this.task = task;
        this.id = id;
        this.status = status;
        this.userEmail = userEmail;
        this.uuid = UUID.randomUUID().toString();
        Log.d("UUID",uuid);
    }


}

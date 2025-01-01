package com.example.todolist;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.devmobile.todolistBertaudLeroi.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.todolist.Model.MyCompatActivity;

import com.devmobile.todolistBertaudLeroi.R;
import com.example.todolist.Adapter.ToDoAdapter;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper;
import com.example.todolist.services.IntentOpenerService;
import com.example.todolist.ui.LogReg.RegisterFragment;
import com.example.todolist.services.FirebaseService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmobile.todolistBertaudLeroi.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends MyCompatActivity implements OnDialogCloseListener {

    private ActivityMainBinding binding;


    RecyclerView recyclerView;
    FloatingActionButton addButton;
    DataBaseHelper myDB;

    private List mList;
    private ToDoAdapter adapter;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();




        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseService firebaseService = new FirebaseService(this);
        FirebaseUser user = firebaseService.getCurrentUser();

        if (user == null) {
            IntentOpenerService.OpenLoginActivity(this, true);
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);
        myDB = new DataBaseHelper(MainActivity.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(myDB, MainActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        firebaseService.getTasksForCurrentUser(new FirebaseService.ToDoCallback() {
            @Override
            public void onSuccess(List<ToDoModel> tasks) {
                for (ToDoModel task : tasks) {
                    mList.add(task);
                }
                Collections.reverse(mList);
                adapter.setTasks(mList);
            }

            @Override
            public void onFailure(String errorMessage) {
                System.err.println("Error: " + errorMessage);
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        binding.sidenavbar.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_account) {
                IntentOpenerService.OpenMainActivity(this, false);
            }
            if (item.getItemId() == R.id.nav_settings) {
                IntentOpenerService.OpenPreferenceActivity(this, false);
            }

            if (item.getItemId() == R.id.nav_logout) {
                firebaseService.logOut();
                this.recreate();
            }
            return true;
        });
        Toast.makeText(this, "Vous etes connecté en tant que " + user.getEmail(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

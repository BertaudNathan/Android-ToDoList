package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.devmobile.todolistBertaudLeroi.R;
import com.example.todolist.ui.LogReg.RegisterFragment;
import com.example.todolist.services.FirebaseService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.devmobile.todolistBertaudLeroi.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        FirebaseService firebaseService = new FirebaseService(this);
        FirebaseUser user = firebaseService.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(this, RegisterFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //on enleve le register de la back stack
            startActivity(intent);
        }
        Toast.makeText(this,"Vous etes connecté en tant que "+user.getEmail(),Toast.LENGTH_SHORT).show();
    }

}
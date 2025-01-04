package com.example.todolist;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.devmobile.todolistBertaudLeroi.R;
import com.devmobile.todolistBertaudLeroi.databinding.ActivityLoginRegisterBinding;
import com.example.todolist.Model.BackConfirmActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class LoginRegisterActivity extends BackConfirmActivity {
    private ActivityLoginRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view_logReg);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_login, R.id.navigation_register)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_logReg);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewLogReg, navController);
    }
}
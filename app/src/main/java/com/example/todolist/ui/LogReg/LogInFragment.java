package com.example.todolist.ui.LogReg;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devmobile.todolistBertaudLeroi.R;
import com.example.todolist.MainActivity;
import com.example.todolist.services.FirebaseService;
import com.devmobile.todolistBertaudLeroi.databinding.FragmentLoginBinding;

public class LogInFragment extends Fragment {
    private FragmentLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Utilisez uniquement le binding pour gérer la vue
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // Configurer le gestionnaire d'événements pour le bouton
        binding.buttonLogin.setOnClickListener(this::LogIn_Click);

        return binding.getRoot(); // Retourner la vue du binding
    }

    public void LogIn_Click(View view){
        Log.d("ezdzfzfz", "LogIn_Click: erzefzefezzef");
        FirebaseService fbs = new FirebaseService(this.getContext());
        TextView email =  binding.editTextTextEmailAddress;
        TextView pwd =  binding.editTextTextPassword;
        if (email.getText().toString().isEmpty() || pwd.getText().toString().isEmpty()){
            return ;
        }
        if (fbs.tryLogIn( email.getText().toString(),  pwd.getText().toString())){
            Intent intent = new Intent(this.getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            Toast.makeText(this.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
        }
    }

    public void Register_Click(View view) {

    }
}
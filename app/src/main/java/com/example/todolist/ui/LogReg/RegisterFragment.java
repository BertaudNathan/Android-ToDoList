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
import com.example.todolist.Model.callbacks.CreateUserCallback;
import com.devmobile.todolistBertaudLeroi.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Utilisez uniquement le binding pour gérer la vue
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        // Configurer le gestionnaire d'événements pour le bouton
        binding.buttonRegister.setOnClickListener(this::Register_Click);

        return binding.getRoot(); // Retourner la vue du binding
    }

    public void Register_Click(View view){
        FirebaseService fbs = FirebaseService.getInstance(this.getContext());
        TextView email =  binding.editTextTextEmailAddress;
        TextView pwd =  binding.editTextTextPassword;
        if (email.getText().toString().isEmpty() || pwd.getText().toString().isEmpty()){
            return ;
        }
        fbs.createUser( email.getText().toString(),  pwd.getText().toString(), new CreateUserCallback() {
            @Override
            public void onResult(String result) {
                if ("success".equals(result)) {
                    email.setText("");
                    pwd.setText("");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    email.setText("");
                    pwd.setText("");
                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}


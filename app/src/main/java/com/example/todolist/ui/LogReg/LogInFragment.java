package com.example.todolist.ui.LogReg;

import android.content.Context;
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
import com.example.todolist.Model.callbacks.CreateUserCallback;
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
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.buttonLogin.setOnClickListener(this::LogIn_Click);

        return binding.getRoot();
    }

    public void LogIn_Click(View view){
        FirebaseService fbs = FirebaseService.getInstance(this.getContext());
        TextView email =  binding.editTextTextEmailAddress;
        TextView pwd =  binding.editTextTextPassword;
        if (email.getText().toString().isEmpty() || pwd.getText().toString().isEmpty()){
            return ;
        }
        Context t = getContext();
        fbs.tryLogIn( email.getText().toString(),  pwd.getText().toString(), new CreateUserCallback() {
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
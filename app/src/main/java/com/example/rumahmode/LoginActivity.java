package com.example.rumahmode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.rumahmode.adapters.DatabaseAdapter;


public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        databaseAdapter = new DatabaseAdapter(LoginActivity.this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (username.isEmpty()) {
                editTextUsername.setError("Username is required");
                editTextUsername.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                editTextPassword.setError("Password is required");
                editTextPassword.requestFocus();
                return;
            }

            login(username, password);
        });
    }

    private void login(String username, String password) {
        // Temp solution create account
        long user_id = databaseAdapter.insertUser(username, "Jalan Mawar 3 no. 1, Cimahi", password);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
        finish(); // remove from backstack
    }
}

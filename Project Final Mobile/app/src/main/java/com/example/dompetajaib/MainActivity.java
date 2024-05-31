package com.example.dompetajaib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView loginNIM, loginPassword;
    private Button btn_login, btn_register;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginNIM = findViewById(R.id.tv_nim);
        loginPassword = findViewById(R.id.tv_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        sharedPreferences = getSharedPreferences("registUser", Context.MODE_PRIVATE);

        String nim = sharedPreferences.getString("NIM", "");
        String pass = sharedPreferences.getString("pass", "");

        btn_login.setOnClickListener(v -> {
            if (loginNIM.getText().toString().trim().isEmpty()) {
                loginNIM.setError("Please fill this field");
                return;
            }
            if (loginPassword.getText().toString().trim().isEmpty()) {
                loginPassword.setError("Please fill this field");
                return;
            }
            String nimInput = loginNIM.getText().toString();
            String passInput = loginPassword.getText().toString();
            if (nimInput.equals(nim) && passInput.equals(pass)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("NIM", nimInput);
                editor.apply();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Username atau Password salah", Toast.LENGTH_SHORT).show();
            }
        });

        btn_register.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    }
}

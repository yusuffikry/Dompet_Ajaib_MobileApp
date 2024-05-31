package com.example.dompetajaib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private TextView registNim, registPassword;
    private Button regis_btn;
    private SharedPreferences sharedPreferences;
    private ImageView btn_backtoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registNim = findViewById(R.id.nim_regis);
        registPassword = findViewById(R.id.password_regis);
        regis_btn = findViewById(R.id.btn_regis2);
        btn_backtoLogin = findViewById(R.id.back_login);

        sharedPreferences = getSharedPreferences("registUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        btn_backtoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        regis_btn.setOnClickListener(v -> {
            if (registNim.getText().toString().trim().isEmpty()) {
                registNim.setError("Please fill this field");
                return;
            }
            if (registPassword.getText().toString().trim().isEmpty()) {
                registPassword.setError("Please fill this field");
                return;
            } else {
                editor.putString("NIM", registNim.getText().toString());
                editor.putString("pass", registPassword.getText().toString());
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Berhasil Membuat akun", Toast.LENGTH_SHORT).show();
            }
        });


    }
}

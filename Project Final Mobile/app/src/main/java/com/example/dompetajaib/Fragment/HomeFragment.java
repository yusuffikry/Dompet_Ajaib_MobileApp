package com.example.dompetajaib.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dompetajaib.DatabaseHelper;
import com.example.dompetajaib.MainActivity;
import com.example.dompetajaib.R;
import com.example.dompetajaib.Transaction;
import com.example.dompetajaib.Adapter.TransactionAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList;
    private TextView home;
    private ImageView btn_logout;
    private SharedPreferences sharedPreferences;
    private TextView textTotal, textPemasukan, textPengeluaran;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        databaseHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        textTotal = view.findViewById(R.id.textTotal);
        textPemasukan = view.findViewById(R.id.textPemasukan);
        textPengeluaran = view.findViewById(R.id.textPengeluaran);
        home = view.findViewById(R.id.tv_name);
        btn_logout = view.findViewById(R.id.btn_logout);

        sharedPreferences = getActivity().getSharedPreferences("registUser", Context.MODE_PRIVATE);
        String helloUsername = sharedPreferences.getString("NIM", "");
        home.setText("Selamat Datang " + helloUsername);

        btn_logout.setOnClickListener(v -> showLogoutConfirmationDialog());

        loadData();

        return view;
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah anda yakin ingin keluar?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void loadData() {
        transactionList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllData();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String jenis = cursor.getString(cursor.getColumnIndexOrThrow("jenis"));
            int jumlah = cursor.getInt(cursor.getColumnIndexOrThrow("jumlah"));
            String catatan = cursor.getString(cursor.getColumnIndexOrThrow("catatan"));
            String kategori = cursor.getString(cursor.getColumnIndexOrThrow("kategori"));

            Transaction transaction = new Transaction(id, jenis, jumlah, catatan, kategori);
            transactionList.add(transaction);
        }

        adapter = new TransactionAdapter(getContext(), transactionList, databaseHelper, this);
        recyclerView.setAdapter(adapter);
        updateSaldo();
    }

    public void updateSaldo() {
        int totalPemasukan = 0;
        int totalPengeluaran = 0;

        for (Transaction transaction : transactionList) {
            if (transaction.getJenis().equals("Pemasukan")) {
                totalPemasukan += transaction.getJumlah();
            } else {
                totalPengeluaran += transaction.getJumlah();
            }
        }

        NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
        textPemasukan.setText("Rp " + formatter.format(totalPemasukan));
        textPengeluaran.setText("Rp " + formatter.format(totalPengeluaran));
        textTotal.setText("Rp " + formatter.format(totalPemasukan - totalPengeluaran));
    }
}

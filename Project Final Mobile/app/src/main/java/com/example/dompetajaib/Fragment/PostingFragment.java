package com.example.dompetajaib.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dompetajaib.DatabaseHelper;
import com.example.dompetajaib.R;

public class PostingFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private EditText editTextJumlah, editTextCatatan;
    private RadioGroup radioGroup;
    private Spinner spinnerKategori;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);

        databaseHelper = new DatabaseHelper(getContext());
        radioGroup = view.findViewById(R.id.radioGroup);
        editTextJumlah = view.findViewById(R.id.editTextJumlah);
        editTextCatatan = view.findViewById(R.id.editTextCatatan);
        spinnerKategori = view.findViewById(R.id.spinnerKategori);
        Button buttonSimpan = view.findViewById(R.id.buttonSimpan);

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        return view;
    }

    private void saveData() {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId != -1) { // Cek apakah ada RadioButton yang dipilih
            RadioButton radioButton = getView().findViewById(selectedId);

            String jenis = radioButton.getText().toString();
            String jumlahStr = editTextJumlah.getText().toString();
            String catatan = editTextCatatan.getText().toString();
            String kategori = spinnerKategori.getSelectedItem().toString();

            if (!TextUtils.isEmpty(jumlahStr) && !TextUtils.isEmpty(catatan)) {
                int jumlah = Integer.parseInt(jumlahStr);

                // Mengambil total pemasukan dan pengeluaran dari database
                int totalPemasukan = databaseHelper.getTotal("Pemasukan");
                int totalPengeluaran = databaseHelper.getTotal("Pengeluaran");

                // Menghitung saldo saat ini
                int saldo = totalPemasukan - totalPengeluaran;

                // Memeriksa apakah pengeluaran yang dimasukkan melebihi saldo yang tersedia
                if (jenis.equals("Pengeluaran") && jumlah > saldo) {
                    Toast.makeText(getContext(), "Saldo Anda tidak mencukupi", Toast.LENGTH_SHORT).show();
                    return; // Hentikan proses penyimpanan
                }

                boolean isInserted = databaseHelper.insertData(jenis, jumlah, catatan, kategori);

                if (isInserted) {
                    Toast.makeText(getContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    editTextJumlah.setText("");
                    editTextCatatan.setText("");
                    radioGroup.clearCheck();
                    spinnerKategori.setSelection(0);
                } else {
                    Toast.makeText(getContext(), "Data gagal disimpan", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Jumlah dan catatan harus diisi", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Pilih jenis transaksi", Toast.LENGTH_SHORT).show();
        }
    }

}

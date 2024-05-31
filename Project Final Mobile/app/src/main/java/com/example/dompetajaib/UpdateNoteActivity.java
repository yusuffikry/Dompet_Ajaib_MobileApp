package com.example.dompetajaib;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateNoteActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextJumlahUpdate, editTextCatatanUpdate;
    private RadioGroup radioGroupUpdate;
    private Spinner spinnerKategoriUpdate;
    private int transactionId;
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        databaseHelper = new DatabaseHelper(this);

        radioGroupUpdate = findViewById(R.id.radioGroupUpdate);
        editTextJumlahUpdate = findViewById(R.id.editTextJumlahUpdate);
        editTextCatatanUpdate = findViewById(R.id.editTextCatatanUpdate);
        spinnerKategoriUpdate = findViewById(R.id.spinnerKategoriUpdate);
        btn_back = findViewById(R.id.btn_back);
        Button buttonSimpanUpdate = findViewById(R.id.buttonSimpanUpdate);

        transactionId = getIntent().getIntExtra("TRANSACTION_ID", -1);
        String jenis = getIntent().getStringExtra("JENIS");
        int jumlah = getIntent().getIntExtra("JUMLAH", 0);
        String catatan = getIntent().getStringExtra("CATATAN");
        String kategori = getIntent().getStringExtra("KATEGORI");

        if (jenis.equals("Pemasukan")) {
            radioGroupUpdate.check(R.id.radioPemasukanUpdate);
        } else {
            radioGroupUpdate.check(R.id.radioPengeluaranUpdate);
        }

        editTextJumlahUpdate.setText(String.valueOf(jumlah));
        editTextCatatanUpdate.setText(catatan);

        // Set spinner selection based on the category
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kategori_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategoriUpdate.setAdapter(adapter);
        if (kategori != null) {
            int spinnerPosition = adapter.getPosition(kategori);
            spinnerKategoriUpdate.setSelection(spinnerPosition);
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonSimpanUpdate.setOnClickListener(v -> updateData());
    }

    private void updateData() {
        int selectedId = radioGroupUpdate.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        String jenis = radioButton.getText().toString();
        String jumlahStr = editTextJumlahUpdate.getText().toString();
        String catatan = editTextCatatanUpdate.getText().toString();
        String kategori = spinnerKategoriUpdate.getSelectedItem().toString();

        if (!TextUtils.isEmpty(jumlahStr) && !TextUtils.isEmpty(catatan)) {
            int jumlah = Integer.parseInt(jumlahStr);

            if (jumlah <= 0) {
                Toast.makeText(this, "Jumlah harus lebih besar dari nol", Toast.LENGTH_SHORT).show();
                return;
            }

            // Additional check for saldo
            int currentTotalPemasukan = databaseHelper.getTotal("Pemasukan");
            int currentTotalPengeluaran = databaseHelper.getTotal("Pengeluaran");
            int currentSaldo = currentTotalPemasukan - currentTotalPengeluaran;

            if (jenis.equals("Pengeluaran") && currentSaldo - jumlah < 0) {
                Toast.makeText(this, "Saldo anda tidak mencukupi", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isUpdated = databaseHelper.updateData(transactionId, jenis, jumlah, catatan, kategori);

            if (isUpdated) {
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();

                // Update the view directly
                Intent intent = new Intent();
                intent.putExtra("TRANSACTION_ID", transactionId);
                intent.putExtra("JENIS", jenis);
                intent.putExtra("JUMLAH", jumlah);
                intent.putExtra("CATATAN", catatan);
                intent.putExtra("KATEGORI", kategori);
                setResult(RESULT_OK, intent);

                finish();
            } else {
                Toast.makeText(this, "Data gagal diperbarui", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Jumlah dan catatan harus diisi", Toast.LENGTH_SHORT).show();
        }
    }

}

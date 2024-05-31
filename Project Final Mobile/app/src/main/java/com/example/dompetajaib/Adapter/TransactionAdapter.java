package com.example.dompetajaib.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dompetajaib.HomeActivity;
import com.example.dompetajaib.MainActivity;
import com.example.dompetajaib.R;
import com.example.dompetajaib.Transaction;
import com.example.dompetajaib.UpdateNoteActivity;
import com.example.dompetajaib.DatabaseHelper;
import com.example.dompetajaib.Fragment.HomeFragment;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private List<Transaction> transactionList;
    private DatabaseHelper databaseHelper;
    private HomeFragment homeFragment;

    public TransactionAdapter(Context context, List<Transaction> transactionList, DatabaseHelper databaseHelper, HomeFragment homeFragment) {
        this.context = context;
        this.transactionList = transactionList;
        this.databaseHelper = databaseHelper;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.bind(transaction);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateNoteActivity.class);
            intent.putExtra("TRANSACTION_ID", transaction.getId());
            intent.putExtra("JENIS", transaction.getJenis());
            intent.putExtra("JUMLAH", transaction.getJumlah());
            intent.putExtra("CATATAN", transaction.getCatatan());
            intent.putExtra("KATEGORI", transaction.getKategori());
            ((HomeActivity) context).startActivityForResult(intent, HomeActivity.REQUEST_UPDATE_NOTE);
        });

        holder.deletePost.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah anda yakin ingin menghapusnya?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        databaseHelper.deleteData(transaction.getId());
                        transactionList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, transactionList.size());
                        homeFragment.updateSaldo(); // Update saldo setelah menghapus
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textJenis, textJumlah, textCatatan, textKategori;
        CardView deletePost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textJenis = itemView.findViewById(R.id.textViewJenis);
            textJumlah = itemView.findViewById(R.id.textViewJumlah);
            textCatatan = itemView.findViewById(R.id.textViewCatatan);
            textKategori = itemView.findViewById(R.id.textViewKategori);
            deletePost = itemView.findViewById(R.id.deletepost);
        }

        public void bind(Transaction transaction) {
            textJenis.setText(transaction.getJenis());
            NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
            textJumlah.setText("Rp " + formatter.format(transaction.getJumlah()));
            textCatatan.setText(transaction.getCatatan());
            textKategori.setText(transaction.getKategori());

            if (transaction.getJenis().equals("Pemasukan")) {
                textJenis.setTextColor(Color.GREEN);
            } else {
                textJenis.setTextColor(Color.RED);
            }
        }
    }
}

package com.develite.pro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import androidx.appcompat.app.AlertDialog;
import java.text.NumberFormat;
import java.util.Locale;

public class ProyekAdapter extends RecyclerView.Adapter<ProyekAdapter.ViewHolder> {
    private List<Integer> idList;
    private List<Proyek> dataList;
    private Context context;
    private DatabaseHelper dbHelper;

    public ProyekAdapter(Context context, List<Integer> idList, List<Proyek> dataList, DatabaseHelper dbHelper) {
        this.context = context;
        this.idList = idList;
        this.dataList = dataList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyek, parent, false);
        return new ViewHolder(view);
    }
    
    private String formatRupiah(double number) {
     NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    return formatRupiah.format(number);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      Proyek proyek = dataList.get(position);
    holder.tvNama.setText(proyek.getNama());
    // holder.tvBiaya.setText(formatRupiah(proyek.getBiaya()));
      
        // Tombol edit
        holder.btnEdit.setOnClickListener(v -> {
            showEditDialog(idList.get(position), position);
        });

        // Tombol delete dengan AsyncTask
        holder.btnDelete.setOnClickListener(v -> {
            new DeleteProyekTask(idList.get(position), position).execute();
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama;
      
        ImageView btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_proyek_nama);
            
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    private void showEditDialog(int id, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Proyek");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_proyek, null);
        EditText etNama = view.findViewById(R.id.et_nama);
        EditText etBiaya = view.findViewById(R.id.et_biaya);

        etNama.setText(dataList.get(position).getNama());
       etBiaya.setText(String.valueOf(dataList.get(position).getBiaya()));


        builder.setView(view);
        builder.setPositiveButton("Update", (dialog, which) -> {
            String namaBaru = etNama.getText().toString();
            double biayaBaru = Double.parseDouble(etBiaya.getText().toString());

            dbHelper.updateProyek(id, namaBaru, biayaBaru);
            dataList.get(position).setNama(namaBaru);
            dataList.get(position).setBiaya(biayaBaru);

            notifyItemChanged(position);
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    // Definisi AsyncTask untuk penghapusan
    private class DeleteProyekTask extends AsyncTask<Void, Void, Boolean> {
        private int id;
        private int position;

        public DeleteProyekTask(int id, int position) {
            this.id = id;
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // Lakukan operasi penghapusan proyek di background
            return dbHelper.deleteProyek(id);  // Pastikan deleteProyek mengembalikan boolean (true/false)
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {  // Hanya hapus dari list jika penghapusan berhasil
                dataList.remove(position);
                idList.remove(position);

                notifyDataSetChanged();
            }
        }
    }
}

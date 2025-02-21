package com.develite.pro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.TextView;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import java.util.regex.Pattern;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import android.widget.Toast;

public class HomeFragment extends Fragment  implements OnUpdate{
    private FloatingActionButton fabMain;
    private TextView text_title;
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ProyekAdapter adapter;
    private TextView totalBiaya;
    private List<Integer> idList = new ArrayList<>();
   private List<Proyek> listProyek = new ArrayList<>();
   @Override
    public void onBiayaUpdated() {
        getTotalBiaya(); 
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // Inisialisasi FAB dengan view.findViewById
        fabMain = view.findViewById(R.id.fab_main);
         totalBiaya = view.findViewById(R.id.value);
        recyclerView = view.findViewById(R.id.rv_proyek);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
       getTotalBiaya();
        
        dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM estimasi", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String namaProyek = cursor.getString(cursor.getColumnIndexOrThrow("nama_proyek"));
                double biayaProyek = cursor.getDouble(cursor.getColumnIndexOrThrow("biaya"));

                idList.add(id);
                listProyek.add(new Proyek(id, namaProyek, biayaProyek));
              
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Pasang adapter ke RecyclerView
        ProyekAdapter adapter = new ProyekAdapter(requireContext(), idList, listProyek, this, dbHelper);
        recyclerView.setAdapter(adapter);

        fabMain.setOnClickListener(v -> showAddDialog());
      
        return view;
    }
    
    
    private void getTotalBiaya() {
    double total = 0;

    dbHelper = new DatabaseHelper(requireContext());
   SQLiteDatabase db = dbHelper.getWritableDatabase();
    Cursor cursor = db.rawQuery("SELECT biaya FROM estimasi", null);

    if (cursor != null) {
        if (cursor.moveToFirst()) {
            do {
                int indexBiaya = cursor.getColumnIndex("biaya");
                if (indexBiaya != -1) {
                    total += cursor.getDouble(indexBiaya);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    db.close();

    // Pastikan totalBiaya sudah diinisialisasi
    if (totalBiaya != null) {
        totalBiaya.setText(format_ui_Rupiah(total));
    } else {
        Toast.makeText(getContext(), "TextView totalBiaya belum diinisialisasi!", Toast.LENGTH_LONG).show();
    }
}

  
    private boolean isValidRupiah(String amount) {
      String regex = "^(Rp ?)?\\d{1,3}(\\.\\d{3})*$";
      return Pattern.matches(regex, amount);
    }
    
    private String format_ui_Rupiah(double number) {
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    formatRupiah.setMinimumFractionDigits(2);
    formatRupiah.setMaximumFractionDigits(2);
    return formatRupiah.format(number);
}
    
    private double format_database_Rupiah(String input) {
    if (input.isEmpty()) return 0; 
    String cleaned = input.replace("Rp", "").replace(" ", "").replace(".", "").replace(",", "."); 

    return Double.parseDouble(cleaned); 
}

    
    private void showAddDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
    builder.setTitle("Add Proyek");

    View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_proyek, null);
    EditText etNama = view.findViewById(R.id.et_nama);
    EditText etBiaya = view.findViewById(R.id.et_biaya);

    builder.setView(view);
    builder.setPositiveButton("Save", (dialog, which) -> {
    String namaBaru = etNama.getText().toString().trim();
    String biayaBaruString = etBiaya.getText().toString().trim();

    double biayaBaru = 0; 
    try {
        biayaBaru = format_database_Rupiah(biayaBaruString); 
    } catch (NumberFormatException e) {
        etBiaya.setError("Format tidak valid");
    }

    addData(namaBaru, biayaBaru); 
    getTotalBiaya();
    
    adapter.notifyDataSetChanged();
});


    builder.setNegativeButton("Batal", null);
    builder.show();
}

private void addData(String nama, double biaya) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("nama_proyek", nama);
    values.put("biaya", biaya);
    db.insert("estimasi", null, values);
    db.close();

    // Ambil ulang data
    List<Integer> idList = new ArrayList<>();
    List<Proyek> listProyek = new ArrayList<>();
    SQLiteDatabase readDb = dbHelper.getReadableDatabase();
    Cursor cursor = readDb.rawQuery("SELECT * FROM estimasi", null);

    if (cursor.moveToFirst()) {
        do {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String namaProyek = cursor.getString(cursor.getColumnIndexOrThrow("nama_proyek"));
            double biayaProyek = cursor.getDouble(cursor.getColumnIndexOrThrow("biaya"));

            idList.add(id);
            listProyek.add(new Proyek(id, namaProyek, biayaProyek));
        } while (cursor.moveToNext());
    }
    cursor.close();
    readDb.close();

    // Update adapter
    adapter = new ProyekAdapter(requireContext(), idList, listProyek, this, dbHelper);
    getTotalBiaya();
    recyclerView.setAdapter(adapter);
    adapter.notifyDataSetChanged();
}

    
}

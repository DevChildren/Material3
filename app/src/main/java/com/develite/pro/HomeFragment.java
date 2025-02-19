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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;


public class HomeFragment extends Fragment {
   
    private FloatingActionButton fabMain, fabAdd, fabEdit, fabEstimasi, fabMaterial;
    private TextView text_title;
    private boolean isMenuOpen = false;
    private BarChart groupedBarChart;
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ProyekAdapter adapter;
    private List<Integer> idList = new ArrayList<>();
   private List<Proyek> listProyek = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        groupedBarChart = view.findViewById(R.id.groupedBarChart);

        setupGroupedBarChart();

        // Inisialisasi FAB dengan view.findViewById
        fabMain = view.findViewById(R.id.fab_main);
        fabAdd = view.findViewById(R.id.fab_add_proyek);
        fabEdit = view.findViewById(R.id.fab_edit_proyek);
        fabEstimasi = view.findViewById(R.id.fab_estimasi);
        fabMaterial = view.findViewById(R.id.fab_material);
         text_title = view.findViewById(R.id.text_title);
        recyclerView = view.findViewById(R.id.rv_proyek);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
                
                if (listProyek.size() == 0) {
             text_title.setVisibility(View.GONE);
              }

              
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Pasang adapter ke RecyclerView
        ProyekAdapter adapter = new ProyekAdapter(requireContext(), idList, listProyek, dbHelper);
        recyclerView.setAdapter(adapter);



        fabMain.setOnClickListener(v -> toggleFabMenu());
        
        fabAdd.setOnClickListener(v -> addData());

        return view;
    }
    
    private void addData() {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("nama_proyek", "Proyek Rumah");
    values.put("biaya", 5000000);
    db.insert("estimasi", null, values);
    db.close();

    // Ambil ulang data
    List<Integer> idList = new ArrayList<>();
    List<Proyek> listProyek = new ArrayList<>();
    Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM estimasi", null);

    if (cursor.moveToFirst()) {
        do {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String namaProyek = cursor.getString(cursor.getColumnIndexOrThrow("nama_proyek"));
            double biayaProyek = cursor.getInt(cursor.getColumnIndexOrThrow("biaya"));

            idList.add(id);
            listProyek.add(new Proyek(id, namaProyek, biayaProyek));
        } while (cursor.moveToNext());
    }
    cursor.close();

    // Update adapter
    adapter = new ProyekAdapter(requireContext(), idList, listProyek, dbHelper);
    recyclerView.setAdapter(adapter);
    adapter.notifyDataSetChanged();
}


    private void toggleFabMenu() {
        if (isMenuOpen) {
            hideFab(fabAdd);
            hideFab(fabEdit);
            hideFab(fabEstimasi);
            hideFab(fabMaterial);
            fabMain.setImageResource(R.drawable.ic_add);
            fabMain.setBackgroundColor(R.color.md_theme_primary);
        } else {
            fabMain.setBackgroundColor(R.color.md_theme_secondary);
            showFab(fabAdd, 1);
            showFab(fabEdit, 2);
            showFab(fabEstimasi, 3);
            showFab(fabMaterial, 4);
            fabMain.setImageResource(R.drawable.ic_close);
        }
        isMenuOpen = !isMenuOpen;
    }

    private void showFab(View view, int position) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 100f * position, 0f);
        anim.setDuration(200);
        anim.start();
    }

    private void hideFab(View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0f, 100f);
        anim.setDuration(200);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
           
     view.setVisibility(View.GONE);
            }
        });
    }
    
    private void setupGroupedBarChart() {
        float groupSpace = 0.4f; // Jarak antar grup
        float barSpace = 0.05f; // Jarak antar bar dalam grup
        float barWidth = 0.1f; // Lebar tiap bar

        ArrayList<BarEntry> biayaEntries = new ArrayList<>();
        ArrayList<BarEntry> materialEntries = new ArrayList<>();
        ArrayList<BarEntry> proyekEntries = new ArrayList<>();
        ArrayList<BarEntry> progressEntries = new ArrayList<>();

        // Data untuk tiap kategori dalam grup
        biayaEntries.add(new BarEntry(1, 5000)); // Estimasi Biaya
        materialEntries.add(new BarEntry(1, 7000)); // Kalkulator Material
        proyekEntries.add(new BarEntry(1, 6000)); // Manajemen Proyek
        progressEntries.add(new BarEntry(1, 4000)); // Progress

        biayaEntries.add(new BarEntry(2, 6000));
        materialEntries.add(new BarEntry(2, 5000));
        proyekEntries.add(new BarEntry(2, 8000));
        progressEntries.add(new BarEntry(2, 3000));
        
        
        biayaEntries.add(new BarEntry(3, 6000));
        materialEntries.add(new BarEntry(3, 5000));
        proyekEntries.add(new BarEntry(3, 8000));
        progressEntries.add(new BarEntry(3, 3000));

        BarDataSet biayaDataSet = new BarDataSet(biayaEntries, "Estimasi");
        biayaDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet materialDataSet = new BarDataSet(materialEntries, "Material");
        materialDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
       
        BarDataSet proyekDataSet = new BarDataSet(proyekEntries, "Proyek");
        proyekDataSet.setColors(ColorTemplate.PASTEL_COLORS);
      
        BarDataSet progressDataSet = new BarDataSet(progressEntries, "Progress");
        progressDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(biayaDataSet);
        dataSets.add(materialDataSet);
        dataSets.add(proyekDataSet);
        dataSets.add(progressDataSet);

        BarData data = new BarData(dataSets);
        data.setBarWidth(barWidth);

        groupedBarChart.setData(data);
        groupedBarChart.groupBars(0, groupSpace, barSpace);
        groupedBarChart.invalidate();
    }
}

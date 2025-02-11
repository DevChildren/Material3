package com.develite.pro;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

public class KalkulatorFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       
        View view = inflater.inflate(R.layout.fragment_kalkulator, container, false);
        
        RecyclerView recyclerView = view.findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        List<MenuItemModel> menuList = new ArrayList<>();
        menuList.add(new MenuItemModel("Kalkulasi Beton", R.drawable.ic_beton));
        menuList.add(new MenuItemModel("Kalkulasi Bata", R.drawable.ic_bata));
        menuList.add(new MenuItemModel("Kalkulasi Pasir", R.drawable.ic_pasir));
        menuList.add(new MenuItemModel("Kalkulasi Cat", R.drawable.ic_cat));
        menuList.add(new MenuItemModel("Kalkulasi Kaso", R.drawable.ic_kayu));
        menuList.add(new MenuItemModel("Kalkulasi Besi", R.drawable.ic_besi));
        menuList.add(new MenuItemModel("Kalkulasi Cat", R.drawable.ic_crot));
        menuList.add(new MenuItemModel("Kalkulasi Plafond", R.drawable.ic_plafond));
        menuList.add(new MenuItemModel("Kalkulasi Cat", R.drawable.ic_crot));
        menuList.add(new MenuItemModel("Kalkulasi Keramik", R.drawable.ic_keramik));
        menuList.add(new MenuItemModel("Kalkulasi Bata Tumpuk", R.drawable.ic_bata_t));
        menuList.add(new MenuItemModel("Kalkulasi Beton tangga", R.drawable.ic_beton_tangga));
        menuList.add(new MenuItemModel("Kalkulasi Beton Lingar", R.drawable.ic_beton_lingkar));
        menuList.add(new MenuItemModel("Kalkulasi Beton Kubus", R.drawable.ic_beton_kubus));
        menuList.add(new MenuItemModel("Kalkulasi Beton Persegi panjng", R.drawable.ic_beton_persegi_panjang));
        menuList.add(new MenuItemModel("Kalkulasi Beton Kerucut", R.drawable.ic_beton_kerucut));
        menuList.add(new MenuItemModel("Kalkulasi Beton Balok", R.drawable.ic_beton_balok));
        menuList.add(new MenuItemModel("Kalkulasi Beton Segitiga", R.drawable.ic_beton_segitiga));

        MenuAdapter adapter = new MenuAdapter(getContext(), menuList, item -> {
                
          BottomSheetDialog bottomSheet = new BottomSheetDialog();
          bottomSheet.show(getParentFragmentManager(), "MyBottomSheetDialog");
          
            // Handle klik item
            // Toast.makeText(getContext(), "Dipilih: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}

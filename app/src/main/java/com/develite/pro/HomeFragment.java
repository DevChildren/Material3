package com.develite.pro;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnKalkulator = view.findViewById(R.id.btn_kalkulator);
        Button btnBiaya = view.findViewById(R.id.btn_biaya);
        Button btnProyek = view.findViewById(R.id.btn_proyek);

        btnKalkulator.setOnClickListener(v -> navigateToFragment(new KalkulatorFragment()));
        btnBiaya.setOnClickListener(v -> navigateToFragment(new BiayaFragment()));
        btnProyek.setOnClickListener(v -> navigateToFragment(new ProyekFragment()));

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

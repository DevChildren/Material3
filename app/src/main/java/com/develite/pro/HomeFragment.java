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

public class HomeFragment extends Fragment {
   
    private FloatingActionButton fabMain, fabAdd, fabEdit, fabEstimasi, fabMaterial;
    private boolean isMenuOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // Inisialisasi FAB dengan view.findViewById
        fabMain = view.findViewById(R.id.fab_main);
        fabAdd = view.findViewById(R.id.fab_add_proyek);
        fabEdit = view.findViewById(R.id.fab_edit_proyek);
        fabEstimasi = view.findViewById(R.id.fab_estimasi);
        fabMaterial = view.findViewById(R.id.fab_material);

        fabMain.setOnClickListener(v -> toggleFabMenu());

        return view;
    }

    private void toggleFabMenu() {
        if (isMenuOpen) {
            hideFab(fabAdd);
            hideFab(fabEdit);
            hideFab(fabEstimasi);
            hideFab(fabMaterial);
            fabMain.setImageResource(R.drawable.ic_add);
        } else {
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
}
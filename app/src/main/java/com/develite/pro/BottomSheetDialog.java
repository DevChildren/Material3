package com.develite.pro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class BottomSheetDialog extends BottomSheetDialogFragment {
  private String title;
 
  public static BottomSheetDialog newInstance(String title) {
        BottomSheetDialog fragment = new BottomSheetDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @NonNull View view, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        TextInputEditText txtTitle = view.findViewById(R.id.title);
        txtTitle.setText(title);
      
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        
      TextInputEditText inputPanjang = view.findViewById(R.id.input_panjang);
        TextInputEditText inputLebar = view.findViewById(R.id.input_lebar);
        MaterialButton btnHitung = view.findViewById(R.id.btn_hitung_material);
        MaterialTextView tvHasil = view.findViewById(R.id.tv_hasil_material);
      btnHitung.setOnClickListener(v -> {
            String panjangStr = inputPanjang.getText().toString();
            String lebarStr = inputLebar.getText().toString();

            if (panjangStr.isEmpty() || lebarStr.isEmpty()) {
                Toast.makeText(getContext(), "Mohon isi semua bidang", Toast.LENGTH_SHORT).show();
                return;
            }

            double panjang = Double.parseDouble(panjangStr);
            double lebar = Double.parseDouble(lebarStr);
            double hasil = panjang * lebar;

            tvHasil.setText("Luas: " + hasil + " m²");
        });
    

        // Atur behavior agar bisa ditarik ke atas dan ke bawah
        View parent = (View) view.getParent();
        if (parent != null) {
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED); // Mulai dalam keadaan terbuka
            behavior.setSkipCollapsed(false); // Bisa dikompresi (collapse)
            behavior.setHideable(true); // Bisa ditutup dengan swipe ke bawah
        }
    
      
    }
    
}

package com.develite.pro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProyekFragment extends Fragment {
    private EditText inputTanggalMulai;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proyek, container, false);

        inputTanggalMulai = view.findViewById(R.id.input_tanggal_mulai);

        inputTanggalMulai.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Pilih Tanggal")
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                String tanggal = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(new Date(selection));
                inputTanggalMulai.setText(tanggal);
            });

            datePicker.show(getParentFragmentManager(), "DATE_PICKER");
        });

        return view;
    }
}

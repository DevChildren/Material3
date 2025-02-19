package com.develite.pro;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import android.view.Window;
import android.view.WindowInsetsController;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      
       Window window = getWindow();
       window.setStatusBarColor(getColor(R.color.md_theme_surfaceContainer));
       window.getInsetsController().setSystemBarsAppearance(
       WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);

        drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        setSupportActionBar(topAppBar);
        
        topAppBar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        // Menangani item menu di drawer
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_history) {
                // Aksi untuk menu History
            } else if (id == R.id.nav_settings) {
                // Aksi untuk menu Settings
            } else if (id == R.id.nav_about) {
                // Aksi untuk menu About
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(this::navigateFragment);

        // Set default fragment saat aplikasi dibuka
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
        }
    
    }
    

     // Tangani tombol back untuk menutup drawer
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean navigateFragment(MenuItem item) {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (item.getItemId() == R.id.nav_kalkulator) {
            selectedFragment = new KalkulatorFragment();
        } else if (item.getItemId() == R.id.nav_biaya) {
            selectedFragment = new BiayaFragment();
        } else if (item.getItemId() == R.id.nav_proyek) {
            selectedFragment = new ProyekFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
        }

        return true;
    }
    
}

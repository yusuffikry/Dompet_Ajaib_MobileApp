package com.example.dompetajaib;

import android.content.Intent;
import android.os.Bundle;
import com.example.dompetajaib.Fragment.HomeFragment;
import com.example.dompetajaib.Fragment.InfoFragment;
import com.example.dompetajaib.Fragment.PostingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


public class HomeActivity extends AppCompatActivity {

    public static final int REQUEST_UPDATE_NOTE = 1;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navView = findViewById(R.id.bottomNavView);
        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
                homeFragment = (HomeFragment) selectedFragment;
            } else if (itemId == R.id.navigation_posting) {
                selectedFragment = new PostingFragment();
            } else if (itemId == R.id.navigation_info) {
                selectedFragment = new InfoFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, selectedFragment).commit();
            }
            return true;
        });

        // Set default selection
        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (homeFragment != null) {
                homeFragment.loadData();
            }
        }
    }
}
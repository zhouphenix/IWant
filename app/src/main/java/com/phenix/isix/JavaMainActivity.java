package com.phenix.isix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.phenix.isix.ui.javamain.JavaMainFragment;

public class JavaMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, JavaMainFragment.newInstance())
                    .commitNow();
        }
    }
}
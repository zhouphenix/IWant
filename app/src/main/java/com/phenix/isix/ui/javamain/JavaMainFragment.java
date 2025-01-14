package com.phenix.isix.ui.javamain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.phenix.isix.R;

public class JavaMainFragment extends Fragment {

    private JavaMainViewModel mViewModel;

    public static JavaMainFragment newInstance() {
        return new JavaMainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(JavaMainViewModel.class);
        // TODO: Use the ViewModel
        throw new RuntimeException("=========");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_java_main, container, false);
    }

}
package com.example.myapplication.ui.DailyTask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentHomeBinding;

public class DailyTaskFragment extends Fragment {

    private DailyTaskViewModel dailyTaskModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dailyTaskModel =
                new ViewModelProvider(this).get(DailyTaskViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
           // @Override
           // public void onChanged(@Nullable String s) {
               /// textView.setText(s);
        //  }
       // });
       return root;
   // }

   // @Override
   // public void onDestroyView() {
   //     super.onDestroyView();
   //     binding = null;
  }
}
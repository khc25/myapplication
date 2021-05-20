package com.example.myapplication.ui.home;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Alert;
import com.example.myapplication.R;
import com.example.myapplication.SharingSytemActivity;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.editProfieActivity;
import com.example.myapplication.ui.DailyTaskActivity;
import com.example.myapplication.ui.HelpActivity;
import com.example.myapplication.ui.ReminderActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.ViewCache;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private @NonNull FragmentHomeBinding binding;
    ActivityMainBinding activityMainBinding;
    private CardView cardView;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
            binding = FragmentHomeBinding.inflate(getLayoutInflater());
            return binding.getRoot();

        }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.cardDailyTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DailyTaskActivity.class));
                //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_content_main,new DailyTaskFragment(), null).commit();
            }
        });
        binding.cardReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), ReminderActivity.class));
            }
        });
        binding.cardSharingSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SharingSytemActivity.class));
            }
        });
        binding.cardChatBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HelpActivity.class));
            }
        });
        binding.cardSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), editProfieActivity.class));
            }
        });
        binding.cardCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String msg = "你真的要打電話嗎?";
                builder.setMessage(msg).setCancelable(
                        false).setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                user = firebaseAuth.getCurrentUser();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Uers");
                                Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            String ppl = "" + ds.child("emergancyppl").getValue();
                                            if (!ppl.equals("")) {
                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + ppl.toString()));
                                                startActivity(callIntent);
                                            } else {
                                                Toast.makeText(getActivity(), "你沒有緊急聯絡人", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
            }


        });


}}
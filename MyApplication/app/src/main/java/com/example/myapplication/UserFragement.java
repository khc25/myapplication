package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.bean.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserFragement extends Fragment {
    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUsers> usersList;
    List<Contact> contactlist;
    SearchView searchView;
    public UserFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_fragement, container, false);
        recyclerView=view.findViewById(R.id.users_recylerview);

        checkContactPermission();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        usersList=new ArrayList<>();
        searchView=view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    searchUser(query);
                }else {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText.trim())){
                    searchUser(newText);
                }else {
                    getAllUsers();
                }
                return false;
            }
        });


        return view;
    }

    private void searchUser(String query) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        for (Contact contact : contactlist) {
                            String con="";
                        ModelUsers modelUsers = ds.getValue(ModelUsers.class);
                    if(!modelUsers.getUid().equals(fUser.getUid())){
                        if (modelUsers.getPhoneno().equals(contact.getPhonenumber())) {
                            usersList.add(modelUsers);
                            con=contact.getName();
                        }
                        if (modelUsers.getName().toLowerCase().contains(query.toLowerCase())||modelUsers.getEmail().toLowerCase().contains(query.toLowerCase())||modelUsers.getPhoneno().equals(query)) {
                            usersList.add(modelUsers);
                            con=contact.getName();
                        }
                        adapterUsers = new AdapterUsers(getActivity(), usersList,con);
                        recyclerView.setAdapter(adapterUsers);
                    }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllUsers() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String con="";
                        for (Contact contact : contactlist) {
                        ModelUsers modelUsers = ds.getValue(ModelUsers.class);

                        if (modelUsers.getPhoneno().equals(contact.getPhonenumber())&& !modelUsers.getUid().equals(fUser.getUid())) {
                            usersList.add(modelUsers);
                            con=contact.getName();
                        }

                        adapterUsers = new AdapterUsers(getActivity(), usersList,con);
                        recyclerView.setAdapter(adapterUsers);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void checkContactPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                !=(PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},100);
        }else{
            contactlist=getContactList();
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100) {
            if (!(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                contactlist=getContactList();
                Toast.makeText(getActivity(), R.string.Internet_tip, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private List<Contact> getContactList() {
        Uri uri= ContactsContract.Contacts.CONTENT_URI;
        String sort=ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Cursor cursor=getActivity().getApplicationContext().getContentResolver().query(
                uri,null,null,null,sort
        );
        List<Contact> contactlists = null;
        while (cursor.moveToNext()){
            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contact c =new Contact(name,phoneNumber);
            contactlists.add(c);
        }
        return contactlists;
    }

}
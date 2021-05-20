package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import static android.app.Activity.RESULT_OK;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/*
 */
public class EidtInfoFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String storagePath="Users_Profile_Image";
    ImageView eImg;
    TextView eUsername,eEmail,ePhoneno,eAddress,eRole,eEmppl;
    Button editconfrim;
    Uri image_uri;

    //permission constants
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY_CODE=300;
    private static final int IMAGE_PICK_CAMERA_CODE=400;
    //arrays of permissions to be requested
    String cameraPermisssions[];
    String storagePermissions[];


    public EidtInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_eidt_info, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        cameraPermisssions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init view
        eImg = view.findViewById(R.id.editavataeIv);
        eUsername = view.findViewById(R.id.editprofile_username);
        eEmail = view.findViewById(R.id.editprofile_email);
        ePhoneno = view.findViewById(R.id.editprofile_phoneno);
        eAddress = view.findViewById(R.id.editprofile_address);
        eRole = view.findViewById(R.id.editprofile_role);
        eEmppl =view.findViewById(R.id.editprofile_emergancyppl);
        editconfrim =(Button)view.findViewById(R.id.editconfirm_btn);
        Query query = databaseReference.orderByChild("email").equalTo(user.getDisplayName());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String ds_name = "" + ds.child("name").getValue();
                    Log.d("", "result" + ds_name);
                    String ds_email = "電郵: " + ds.child("email").getValue();
                    String ds_phone = "電話: " + ds.child("phoneno").getValue();
                    String ds_image = "" + ds.child("image").getValue();
                    String ds_address = "地址: " + ds.child("address").getValue();
                    String ds_emergancyppl = "緊急電話: " + ds.child("emergancyppl").getValue();
                    String ds_role = "角色: " + ds.child("role").getValue();
                    eUsername.setText(ds_name);
                    eEmail.setText(ds_email);
                    ePhoneno.setText(ds_phone);
                    eAddress.setText(ds_address);
                    eEmppl.setText(ds_emergancyppl);
                    eRole.setText(ds_role);
                    try {
                        Picasso.get().load(ds_image).into(eImg);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_add_image).into(eImg);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        editconfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=eEmail.getText().toString().trim();
                String username=eUsername.getText().toString().trim();
                String phoneno=ePhoneno.getText().toString().trim();
                String address=eAddress.getText().toString().trim();
                String emergancyppl=eEmppl.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    eUsername.setError("名字不能為空");
                    eUsername.setFocusable(true);
                }else if(TextUtils.isEmpty(phoneno)){
                    ePhoneno.setError("電話不能為空");
                    ePhoneno.setFocusable(true);
                }else if(TextUtils.isEmpty(address)){
                    eAddress.setError("地址不能為空");
                    eAddress.setFocusable(true);
                }else if(TextUtils.isEmpty(emergancyppl)){
                    eEmppl.setError("緊急聯絡人不能為空");
                    eEmppl.setFocusable(true);
                }else {

                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("name",username);
                    hashMap.put("phoneno",phoneno);
                    hashMap.put("address",address);
                    hashMap.put("emergancyppl",emergancyppl);
                    databaseReference.child(user.getUid()).child("name").setValue(username);
                    databaseReference.child(user.getUid()).child("phoneno").setValue(phoneno);
                    databaseReference.child(user.getUid()).child("address").setValue(address);
                    databaseReference.child(user.getUid()).child("emergancyppl").setValue(emergancyppl);
                            /*.addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "updated\n"+user.getEmail(),
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), InfoFragment.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });*/

                }
            }
        });
        eImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String options[] = {"相機", "相簿"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("使用相片由..");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if(!checkCameraPermission()){
                                requestCameraPermission();
                            }else{
                                pickFromCamera();
                            }
                        } else if (which == 1) {
                            if(!checkStoragePermission()){
                                requestStoragePermission();
                            }else{
                                pickFromGallery();                            }
                        }
                    }
                });
                builder.create().show();
            }
        });



        return view;
    }
    private boolean checkStoragePermission(){
        boolean result= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(getActivity(),storagePermissions,STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermission(){
        boolean result= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                ==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(getActivity(),cameraPermisssions,CAMERA_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }else{
                        Toast.makeText(getActivity(), "請許可相機和儲存權根!.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE:{

                if(grantResults.length>0){
                    boolean writeStorageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickFromGallery();
                    }else{
                        Toast.makeText(getActivity(), "請許可儲存權根!.",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                uploadProfileCoverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        String filename=storagePath+"_"+user.getUid();
        StorageReference storageReference2=storageReference.child(filename);
        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri dlUri= uriTask.getResult();
                        //check if uploaded
                        if(uriTask.isSuccessful()){
                            HashMap<String,Object> results= new HashMap<>();
                            results.put("image",dlUri.toString());
                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(),"圖片已上傳",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),"上傳錯誤: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(getActivity(),"上傳錯誤",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickFromCamera() {
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp descpition");

        image_uri=getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }


    private void pickFromGallery() {
        Intent galleryIntent =new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("Image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }



}
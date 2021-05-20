package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.bean.Contact;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    EditText addcomment;
    ImageButton voice_btn,post_btn;

    String postid;
    String uId,uName;
    FirebaseUser user;
    ProgressDialog pd;
    String pimg;
    List<Contact> contactlist;
    List<ModelComment> commentList;
    AdapterComment adapterComment;
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("留言");

        //enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addcomment=findViewById(R.id.add_comment);
        post_btn=findViewById(R.id.post);
        Intent intent= getIntent();
        pimg=intent.getStringExtra("pImage");
        postid=intent.getStringExtra("postid");
        uName=intent.getStringExtra("uName");
        loadComments();

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addcomment.getText().toString().trim().equals("")) {
                    Toast.makeText(CommentActivity.this, "請輸入留言 ",
                            Toast.LENGTH_SHORT).show();
                }else {
                    addComment(addcomment.getText().toString().trim());
                }
            }


        });
    }
    private void addComment(String comment) {
        pd = new ProgressDialog(this);
        pd.setMessage("上載留言中..");
        user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            uId=user.getUid();
        }else{
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        if(TextUtils.isEmpty(comment)){
            Toast.makeText(CommentActivity.this, "請輸入留言 ",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String timeStamp=String.valueOf(System.currentTimeMillis());
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts").child(postid).child("Comments");
        HashMap<String,Object> hashMap =new HashMap<>();
        hashMap.put("uid",uId);
        hashMap.put("uimg",pimg);
        hashMap.put("uName",uName);
        hashMap.put("ucomment",comment);
        hashMap.put("timestamp",timeStamp);
        reference.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(CommentActivity.this, "上載留言中... ",
                        Toast.LENGTH_SHORT).show();
                addcomment.setText("");
                updateCommentCount();
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(CommentActivity.this, "上載留言失敗: "+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    boolean mComment=false;
    private void updateCommentCount() {
        mComment=true;
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts").child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mComment){
                    String comments=""+snapshot.child("pComments");
                    int newCommentno=Integer.parseInt(comments)+1;
                    reference.child("pComments").setValue(""+newCommentno);
                    mComment=false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void loadComments() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelComment modelComment=ds.getValue(ModelComment.class);
                    commentList.add(modelComment);
                    adapterComment= new AdapterComment(CommentActivity.this,commentList);
                    recyclerView.setAdapter(adapterComment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommentActivity.this, "db出現錯誤"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
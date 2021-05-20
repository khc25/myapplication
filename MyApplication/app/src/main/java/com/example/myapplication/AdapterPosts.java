package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {
    Context context;
    List<ModelPost> postList;

    String myUid;
    private DatabaseReference likesRef;
    private DatabaseReference postsRef;
    private DatabaseReference commentsRef;
    boolean mcomments=false;
    boolean mlike=false;
    public AdapterPosts(Context context,List<ModelPost> postList){
        this.context=context;
        this.postList=postList;
        myUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef= FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRef= FirebaseDatabase.getInstance().getReference().child("Posts");
        commentsRef= FirebaseDatabase.getInstance().getReference().child("Comments");
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_posts,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        String uid =postList.get(i).getUid();
        String uEmail =postList.get(i).getuEmail();
        String uDp =postList.get(i).getuDp();
        String uName =postList.get(i).getuName();
        String pId =postList.get(i).getpId();
        String pTitle =postList.get(i).getpTitle();
        String pDescr =postList.get(i).getpDescr();
        String pImage =postList.get(i).getpImage();
        String pTimeStamp =postList.get(i).getpTime();
        String pLikes =postList.get(i).getpLikes();

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String pTime = format.format(calendar);

        holder.uNameTv.setText(uName);
        holder.pTimeTv.setText(pTime);
        holder.pTitleTv.setText(pTitle);
        holder.pDescriptionTv.setText(pDescr);
        holder.pDescriptionTv.setText(pLikes+" 讚");
        setLikes(holder,pId);

        try{
            Picasso.get().load(uDp).placeholder(R.drawable.ic_default_img).into(holder.uPictureIv);
        }catch (Exception e){

        }

        if(pImage.equals("")){
            holder.pImageIv.setVisibility(View.GONE);
        }else{

        try{
            Picasso.get().load(pImage).into(holder.pImageIv);
        }catch (Exception e) {
        }
        }

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pLikes=Integer.parseInt(postList.get(i).getpLikes());
                mlike=true;
                final String postIde=postList.get(i).getpId();
                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(mlike){
                            if (snapshot.child(postIde).hasChild(myUid)){
                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes-1));
                                postsRef.child(postIde).child("myUid").removeValue();
                                mlike=false;
                            }else {
                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes+1));
                                postsRef.child(postIde).child("myUid").setValue("Liked");
                                mlike=true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pComments=Integer.parseInt(postList.get(i).getpComments());
                mcomments=true;
                final String postIde=postList.get(i).getpId();
                commentsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(mlike){
                            if (snapshot.child(postIde).hasChild(myUid)){
                                postsRef.child(postIde).child("pComments").setValue(""+(pComments-1));
                                postsRef.child(postIde).child("myUid").removeValue();
                                mlike=false;
                            }else {
                                postsRef.child(postIde).child("pComments").setValue(""+(pComments+1));
                                postsRef.child(postIde).child("myUid").setValue("Liked");
                                mlike=true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("postid",pId);
                intent.putExtra("pImage",pImage);
                intent.putExtra("uName",uName);
                context.startActivity(intent);;
            }
        });


    }

    private void setLikes(MyHolder holder, String postkey) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postkey).hasChild(myUid)){
                    holder.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_liked,0,0,0);
                    holder.likeBtn.setText("已讚好");
                }else{
                    holder.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_like_black,0,0,0);
                    holder.likeBtn.setText("讚好");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView uPictureIv,pImageIv;
        TextView uNameTv,pTimeTv,pTitleTv,pDescriptionTv,pLikesTv;
        Button likeBtn,commentBtn;

        public MyHolder(@NonNull View itemView){
            super(itemView);

            uPictureIv=itemView.findViewById(R.id.uPictureIv);
            pImageIv=itemView.findViewById(R.id.pImageIv);
            uNameTv=itemView.findViewById(R.id.uNameTv);
            pTimeTv=itemView.findViewById(R.id.pTimeTv);
            pTitleTv=itemView.findViewById(R.id.pTitleTv);
            pDescriptionTv=itemView.findViewById(R.id.pDescriptionTv);
            pLikesTv=itemView.findViewById(R.id.pLikesTv);
            likeBtn=itemView.findViewById(R.id.likeBtn);
            commentBtn=itemView.findViewById(R.id.commentBtn);
        }
    }
}

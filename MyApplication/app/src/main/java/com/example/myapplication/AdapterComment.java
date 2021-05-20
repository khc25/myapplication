package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.MyHolder>{
        Context context;
        List<ModelComment> commentList;

    public AdapterComment(Context context,List<ModelComment> commentList){
        this.context=context;
        this.commentList=commentList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_users,viewGroup);

        return new AdapterComment.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String userId=commentList.get(position).getUid();
        String userImage=commentList.get(position).getUimg();
        String userName=commentList.get(position).getuName();
        String userComment=commentList.get(position).getUcomment();
        String timestamp=commentList.get(position).getTimestamp();
        holder.mCommentTv.setText(userComment);
        holder.mTimeTv.setText(timestamp);
        holder.mNameTv.setText(userName);

        try{
            Picasso.get().load(userImage)
                    .placeholder(R.drawable.ic_default_img)
                    .into(holder.mAvatarIv);
        }catch (Exception e){
            Toast.makeText(context, "載入失敗: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+userId+"("+timestamp+")", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mAvatarIv;
        TextView mNameTv,mCommentTv,mTimeTv;

        public MyHolder(@NonNull View itemView){
            super(itemView);
            mAvatarIv=itemView.findViewById(R.id.avataeIv);
            mNameTv=itemView.findViewById(R.id.nameTv);
            mCommentTv=itemView.findViewById(R.id.commentTv);
            mTimeTv=itemView.findViewById(R.id.timeTv);
        }
    }
}

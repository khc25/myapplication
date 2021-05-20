package com.example.myapplication;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {
    Context context;
    List<ModelUsers> userList;
    String contactname;

    public AdapterUsers(Context context,List<ModelUsers> userList,String contactname){
        this.context=context;
        this.userList=userList;
        this.contactname=contactname;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_users,viewGroup);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String userImage=userList.get(position).getImage();
        String userName=userList.get(position).getName();
        String userEmail=userList.get(position).getEmail();
        String contact=contactname;

        holder.mNameTv.setText(userName);
        holder.mEmailTv.setText(userEmail);
        if(""==contactname) {
            holder.mContactNameTv.setText(contactname);
        }else{
            holder.mContactNameTv.setText("("+contactname+")");
        }
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
                Toast.makeText(context, ""+userEmail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mAvatarIv;
        TextView mNameTv,mEmailTv,mContactNameTv;

        public MyHolder(@NonNull View itemView){
            super(itemView);
            mAvatarIv=itemView.findViewById(R.id.avataeIv);
            mNameTv=itemView.findViewById(R.id.nameTv);
            mContactNameTv=itemView.findViewById(R.id.contactnameTv);
            mEmailTv=itemView.findViewById(R.id.emailTv);
        }
    }
}

package com.example.RG.ChatroomAdapterData;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.RG.R;

import java.util.ArrayList;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.ImageViewHolder>{

    public ChatroomAdapter(Context context, ArrayList<ChatroomDATA> chatroomlist) {
        this.context = context;
        this.chatroomlist = chatroomlist;
    }

    private Context context;
    private ArrayList<ChatroomDATA> chatroomlist;


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_chatroom,parent,false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        // Glide 사용해서 이미지 출력 (load: 이미지 경로, override: 이미지 가로,세로 크기 조정, into: 이미지를 출력할 ImageView 객체)
//         Glide.with(getApplicationContext()).load().override().into();
//        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
        Glide.with(context).load(chatroomlist.get(position).getChroom_img()).into(holder.chatroomimg);
        holder.groupidx.setText(chatroomlist.get(position).getChroom_idx());
        holder.chatroomname.setText(chatroomlist.get(position).getChroom_name());
        holder.crlastwriter.setText(chatroomlist.get(position).getChroom_lastwriter());
        holder.crlasttext.setText(chatroomlist.get(position).getChroom_lasttext());
        holder.crlasttime.setText(chatroomlist.get(position).getChroom_lasttime());


        String a = chatroomlist.get(position).getMsgcount();
        int intValue1 = Integer.parseInt(a);



        if (0<intValue1&&intValue1<=100 ) {
            holder.Msgcount.setText(chatroomlist.get(position).getMsgcount());
            holder.Msgcount.setVisibility(View.VISIBLE);
        }else if(intValue1>100){
            holder.Msgcount.setText("+100");
            holder.Msgcount.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return chatroomlist.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder
    {

        TextView groupidx, chatroomname,crlastwriter,crlasttext,crlasttime,Msgcount;
        ImageView chatroomimg;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            chatroomimg = itemView.findViewById(R.id.chatroomimg);
            groupidx = itemView.findViewById(R.id.chatroomidx);
            chatroomname = itemView.findViewById(R.id.chatroomname);
            crlastwriter = itemView.findViewById(R.id.crlastwriter);
            crlasttext  = itemView.findViewById(R.id.crlasttext);
            crlasttime  = itemView.findViewById(R.id.crlasttime);
            Msgcount  = itemView.findViewById(R.id.Msgcount);



        }
    }
}


package com.example.RG.LiveAdapter;


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

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ImageViewHolder>{

    public LiveAdapter(Context context, ArrayList<LiveData> livelist) {
        this.context = context;
        this.livelist = livelist;
    }

    private Context context;
    private ArrayList<LiveData> livelist;


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_liveitem,parent,false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        // Glide 사용해서 이미지 출력 (load: 이미지 경로, override: 이미지 가로,세로 크기 조정, into: 이미지를 출력할 ImageView 객체)
//         Glide.with(getApplicationContext()).load().override().into();
//        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);

        holder.live_idx.setText(livelist.get(position).getLive_idx());
        holder.live_title.setText(livelist.get(position).getLive_title());

        holder.live_token.setText(livelist.get(position).getLive_token());

        holder.live_agorachaname.setText(livelist.get(position).getLive_agorachaname());

        holder.live_gridx.setText(livelist.get(position).getLive_gridx());

        holder.live_writer.setText(livelist.get(position).getLive_writer());

        Glide.with(context).load(livelist.get(position).getLive_image()).into(holder.live_image);

        holder.live_opendate.setText(livelist.get(position).getLive_opendate());

        holder.live_onoff.setText(livelist.get(position).getLive_onoff());
        holder.live_pwd.setText(livelist.get(position).getLive_pwd());
        holder.live_gname.setText(livelist.get(position).getLive_gname());




    }

    @Override
    public int getItemCount() {
        return livelist.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder
    {

        TextView live_idx,live_title,live_agorachaname,live_writer,live_opendate,live_gridx,live_token,live_onoff,live_pwd,live_gname;
        ImageView live_image;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            live_idx = itemView.findViewById(R.id.live_idx);
            live_gridx = itemView.findViewById(R.id.live_gridx);
            live_title = itemView.findViewById(R.id.live_title);
            live_agorachaname = itemView.findViewById(R.id.live_agorachaname);
            live_token = itemView.findViewById(R.id.live_token);
            live_writer = itemView.findViewById(R.id.live_writer);
            live_opendate = itemView.findViewById(R.id.live_opendate);
            live_image = itemView.findViewById(R.id.live_image);
            live_onoff = itemView.findViewById(R.id.live_onoff);
            live_pwd = itemView.findViewById(R.id.live_pwd);
            live_gname = itemView.findViewById(R.id.live_gname);

        }
    }
}


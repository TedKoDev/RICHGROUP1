package com.example.RG.post;

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

public class GrPostAdapter extends RecyclerView.Adapter<GrPostAdapter.ImageViewHolder>{ //VolleyMultipartRequest

    public GrPostAdapter(Context context, ArrayList<GrPostData> pimagelist) {
        this.context = context;
        this.pimagelist = pimagelist;
    }

    private Context context;
    private ArrayList<GrPostData> pimagelist;


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_postitem,parent,false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        // Glide 사용해서 이미지 출력 (load: 이미지 경로, override: 이미지 가로,세로 크기 조정, into: 이미지를 출력할 ImageView 객체)
//         Glide.with(getApplicationContext()).load().override().into();
//        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);



        Glide.with(context).load(pimagelist.get(position).getGrpimage()).into(holder.postimage);

        holder.postwriter.setText(pimagelist.get(position).getGrpwriter());
        holder.postgrpidx.setText(pimagelist.get(position).getGrpidx());
        holder.postdisc.setText(pimagelist.get(position).getGrpdisc());
        holder.postdate.setText(pimagelist.get(position).getGrpcreated_at());
        holder.grvideourl.setText(pimagelist.get(position).getGrvideourl());




//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), GroupActivity.class);
//
//
//
//
//               v.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {




        return pimagelist.size();
    }


    class ImageViewHolder extends RecyclerView.ViewHolder
    {

        TextView  postgrpidx, postwriter,postdate,postdisc,grvideourl;
        ImageView postimage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            postgrpidx = itemView.findViewById(R.id.postgrpidx);
            postimage = itemView.findViewById(R.id.postimage);
            postwriter = itemView.findViewById(R.id.postwriter);
            postdate = itemView.findViewById(R.id.postdate);
            postdisc = itemView.findViewById(R.id.postdisc);
            grvideourl = itemView.findViewById(R.id.grvideourl);

//




        }
    }
}
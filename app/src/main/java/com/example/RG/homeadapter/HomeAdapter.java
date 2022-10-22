package com.example.RG.homeadapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.RG.Home_group.GroupActivity;
import com.example.RG.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ImageViewHolder>{

    public HomeAdapter(Context context, ArrayList<HomeData> imagelist) {
        this.context = context;
        this.imagelist = imagelist;
    }

    private Context context;
    private ArrayList<HomeData> imagelist;


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_homeitem,parent,false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        // Glide 사용해서 이미지 출력 (load: 이미지 경로, override: 이미지 가로,세로 크기 조정, into: 이미지를 출력할 ImageView 객체)
//         Glide.with(getApplicationContext()).load().override().into();
//        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
        Glide.with(context).load(imagelist.get(position).getGrimage()).into(holder.imageView);
        holder.nametvimg.setText(imagelist.get(position).getGrname());
        holder.grpwd.setText(imagelist.get(position).getGrpwd());
//        holder.infotvimg.setText(imagelist.get(position).getGrinfo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GroupActivity.class);
               v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder
    {

        TextView nametvimg, grpwd;
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_retrieve);
            nametvimg = itemView.findViewById(R.id.nametvimg);
            grpwd = itemView.findViewById(R.id.grpwd);
//            infotvimg = itemView.findViewById(R.id.infotvimg);



//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    int cupos = getAdapterPosition();
//                    HomeData model = imagelist.get(cupos);
//                    Toast.makeText(context, model.getGrname()+"/n"+ model.getGrid()  , Toast.LENGTH_SHORT).show();
//
//
//                }
//            });

        }
    }
}


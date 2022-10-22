package com.example.RG.SearchAdapter;



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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ImageViewHolder>{

    public SearchAdapter(Context context, ArrayList<SearchData> imagelist) {
        this.context = context;
        this.imagelist = imagelist;
    }

    private Context context;
    private ArrayList<SearchData> imagelist;


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_searchitem,parent,false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        // Glide 사용해서 이미지 출력 (load: 이미지 경로, override: 이미지 가로,세로 크기 조정, into: 이미지를 출력할 ImageView 객체)
//         Glide.with(getApplicationContext()).load().override().into();
//        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
        Glide.with(context).load(imagelist.get(position).getGrimage()).into(holder.searchgrimage);
        holder.searchgrname.setText(imagelist.get(position).getGrname());
        holder.searchgrmaker.setText(imagelist.get(position).getGruserName());
        holder.searchopendata.setText(imagelist.get(position).getCreated_at());
        holder.searchgrinfo.setText(imagelist.get(position).getGrdisc());
        holder.searchgrid.setText(imagelist.get(position).getGrid());
        holder.onoff.setText(imagelist.get(position).getOnoff());
        holder.groupwd.setText(imagelist.get(position).getGroupwd());




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
        return imagelist.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder
    {

        TextView searchgrname, searchgrmaker,searchopendata,searchgrinfo,searchgrid,onoff,groupwd;
        ImageView searchgrimage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            searchgrimage = itemView.findViewById(R.id.searchgrimage);
            searchgrname = itemView.findViewById(R.id.searchgrname);
            searchgrmaker = itemView.findViewById(R.id.searchgrmaker);
            searchopendata = itemView.findViewById(R.id.searchopendata);
            searchgrinfo = itemView.findViewById(R.id.searchgrinfo);
            searchgrid = itemView.findViewById(R.id.searchgrid);
            onoff = itemView.findViewById(R.id.onoff);
            groupwd = itemView.findViewById(R.id.groupwd);
//            infotvimg = itemView.findViewById(R.id.infotvimg);



//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(view.getContext(), MainActivity3.class);
//
//
//
//
//
//                    view.getContext().startActivity(intent);
//
//
//                }
//            });

        }
    }
}


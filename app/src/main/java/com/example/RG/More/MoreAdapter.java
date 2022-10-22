package com.example.RG.More;

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

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ImageViewHolder>{

    public MoreAdapter(Context context, ArrayList<MoreData> morelist) {
        this.context = context;
        this.morelist = morelist;
    }

    private Context context;
    private ArrayList<MoreData> morelist;


    @NonNull
    @Override
    public MoreAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_moreitem,parent,false);

        return new MoreAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreAdapter.ImageViewHolder holder, int position) {

        // Glide 사용해서 이미지 출력 (load: 이미지 경로, override: 이미지 가로,세로 크기 조정, into: 이미지를 출력할 ImageView 객체)
//         Glide.with(getApplicationContext()).load().override().into();
//        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
        Glide.with(context).load(morelist.get(position).getGrimage()).into(holder.moregrimage);
        holder.morename.setText(morelist.get(position).getGrname());
        holder.moregrmaker.setText(morelist.get(position).getGruserName());
        holder.moreopendata.setText(morelist.get(position).getCreated_at());
        holder.moregrinfo.setText(morelist.get(position).getGrdisc());
        holder.moregrid.setText(morelist.get(position).getGrid());


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
        return morelist.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder
    {

        TextView morename, moregrmaker,moreopendata,moregrinfo,moregrid;
        ImageView moregrimage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            moregrimage = itemView.findViewById(R.id.moregrimage);
            morename = itemView.findViewById(R.id.moregrname);
            moregrmaker = itemView.findViewById(R.id.moregrmaker);
            moreopendata = itemView.findViewById(R.id.moreopendata);
            moregrinfo = itemView.findViewById(R.id.moregrinfo);
            moregrid = itemView.findViewById(R.id.moregrid);


        }
    }
}


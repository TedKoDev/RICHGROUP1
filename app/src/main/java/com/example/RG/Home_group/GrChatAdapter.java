package com.example.RG.Home_group;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RG.Chatting.ViewTypeNUM;
import com.example.RG.R;

import java.util.ArrayList;

public class GrChatAdapter extends RecyclerView.Adapter<GrChatAdapter.ViewHolder> {
    private String TAG = "Adapter";
    private Context mContext;
    private ArrayList<GrChatData> mArrayList; //데이터를 담을 어레이리스트

    public GrChatAdapter(Context context, ArrayList<GrChatData> arrayList) {
        this.mArrayList = arrayList;
        this.mContext = context;
    }

    //리스트의 각 항목을 이루는 디자인(xml)을 적용.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == ViewTypeNUM.CENTER_JOIN) {
            view = inflater.inflate(R.layout.center_join, parent, false);
            return new CenterViewHolder(view);
        } else if (viewType == ViewTypeNUM.LEFT_CHAT) {
            view = inflater.inflate(R.layout.left_chat, parent, false);
            return new LeftViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.right_chat, parent, false);
            return new RightViewHolder(view);
        }

    }

    //리스트의 각 항목에 들어갈 데이터를 지정.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (holder instanceof CenterViewHolder) {

            ((CenterViewHolder) holder).name.setText(mArrayList.get(position).getNick());
        } else if (holder instanceof LeftViewHolder) {
            ((LeftViewHolder) holder).name.setText(mArrayList.get(position).getNick());
            ((LeftViewHolder) holder).content.setText(mArrayList.get(position).getComments());
            ((LeftViewHolder) holder).cmtime.setText(mArrayList.get(position).getTime());
        } else {

            ((RightViewHolder) holder).content.setText(mArrayList.get(position).getComments());
            ((RightViewHolder) holder).cmtime.setText(mArrayList.get(position).getTime());
        }

    }

    //화면에 보여줄 데이터의 갯수를 반환.
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + mArrayList.size());
        return mArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mArrayList.get(position).getViewType();
    }

    //뷰홀더 객체에 저장되어 화면에 표시되고, 필요에 따라 생성 또는 재활용 된다.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        //        TextView tv_roomnumber;
        TextView tv_contents;
        TextView cmtime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_name = itemView.findViewById(R.id.tv_name);
//            this.tv_roomnumber = itemView.findViewById (R.id.tv_roomnumber);
            this.tv_contents = itemView.findViewById(R.id.tv_contents);
            this.cmtime = itemView.findViewById(R.id.cmtime);


        }
    }

    private class CenterViewHolder extends GrChatAdapter.ViewHolder {

        TextView name;

        public CenterViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.centername);


        }
    }

    private class LeftViewHolder extends GrChatAdapter.ViewHolder {
        TextView name;
        TextView content;
        TextView cmtime;

        public LeftViewHolder(View view) {
            super(view);
            content = view.findViewById(R.id.content);
            name = view.findViewById(R.id.name);
            cmtime = view.findViewById(R.id.cmtime);

        }
    }

    private class RightViewHolder extends GrChatAdapter.ViewHolder {
        TextView content;
        TextView cmtime;

        public RightViewHolder(View view) {
            super(view);
            content = view.findViewById(R.id.content);
            cmtime = view.findViewById(R.id.cmtime);


        }
    }
}
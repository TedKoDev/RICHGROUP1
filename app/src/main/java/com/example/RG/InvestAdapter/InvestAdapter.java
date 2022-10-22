package com.example.RG.InvestAdapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InvestAdapter extends RecyclerView.Adapter<InvestAdapter.ImageViewHolder> {


    String urlinvestdelete = "http://3.38.117.213/investdelete.php"; //

    private Context context;
    private ArrayList<InvestData> investlist;

    public InvestAdapter(Context context, ArrayList<InvestData> investlist) {
        this.context = context;
        this.investlist = investlist;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {


        holder.invidx.setText(investlist.get(position).getInvidx());
        holder.invkind.setText(investlist.get(position).getInvkind());
        holder.invname.setText(investlist.get(position).getInvname());
        holder.invavg.setText(investlist.get(position).getInvavg());
        holder.invgetper.setText(investlist.get(position).getInvgetper());
        holder.invcount.setText(investlist.get(position).getInvcount());
        holder.invbuyavg.setText(investlist.get(position).getInvbuyavg());
        holder.invnowprice.setText(investlist.get(position).getInvnowprice());
        holder.invbuyprice.setText(investlist.get(position).getInvbuyprice());


    }

    @Override
    public int getItemCount() {
        return investlist.size();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_investitem, parent, false);


        return new ImageViewHolder(view);
    }

    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        TextView invkind, invname, invidx;
        String sinvidx;

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            MenuItem Edit = contextMenu.add(Menu.NONE, 1001, 1, "수정");
//            MenuItem Delete = contextMenu.add(Menu.NONE, 1002, 2, "삭제");
//            Edit.setOnMenuItemClickListener(onEditMenu);
//            Delete.setOnMenuItemClickListener(onEditMenu);

        }
        // 4. 컨텍스트 메뉴에서 항목 클릭시 동작을 설정합니다.
//        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
//
//
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//
//                switch (item.getItemId()) {
//                    case 1001:  // 5. 편집 항목을 선택시
//
//                        // 값입력_액티비티에서 사용
//                        Intent intent = new Intent(context, Investreplace.class); //액티비티 전환
//                        // 전달할 값 ( 첫번째 인자 : key, 두번째 인자 : 실제 전달할 값 )
//                        intent.putExtra("텍스트", "텍스트의 값.");
//                        context.startActivity(intent);
//
//                        break;
//
//                    case 1002:
//                        commentdelete();
//
//                        Intent intent1 = ((Activity)context).getIntent();
//                        ((Activity)context).finish(); //현재 액티비티 종료 실시
//                        ((Activity)context).overridePendingTransition(0, 0); //효과 없애기
//                        ((Activity)context).startActivity(intent1); //현재 액티비티 재실행 실시
//                        ((Activity)context).overridePendingTransition(0, 0); //효과 없애기
//
//                        investlist.remove(getLayoutPosition());
//
//                        notifyItemRemoved(getLayoutPosition());
//                        notifyItemRangeChanged(getLayoutPosition(), investlist.size());
//
//
//                        break;
//
//                }
//                return true;
//            }
//        };
        TextView invavg, invgetper, invcount, invbuyavg, invnowprice, invbuyprice;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            invidx = itemView.findViewById(R.id.invidx);


            invkind = itemView.findViewById(R.id.invkind);
            invname = itemView.findViewById(R.id.invname);
            invavg = itemView.findViewById(R.id.invavg);
            invgetper = itemView.findViewById(R.id.invgetper);
            invcount = itemView.findViewById(R.id.invcount);
            invbuyavg = itemView.findViewById(R.id.invbuyavg);
            invnowprice = itemView.findViewById(R.id.invnowprice);
            invbuyprice = itemView.findViewById(R.id.invbuyprice);

            itemView.setOnCreateContextMenuListener(this); //2. OnCreateContextMenuListener 리스너를 현재 클래스에서 구현한다고 설정해둡니다.
        }



        private void commentdelete() {
            sinvidx = invidx.getText().toString();
            Log.i("삭제 ", "작동 ");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlinvestdelete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Toast.makeText(context.getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    Log.i("삭제invidx", "3");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.i("삭제invidx", "2");
//                    Toast.makeText(context.getApplicationContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("에러", error.toString());

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    Log.i("삭제invidx", "1");
                    Log.i("삭제invidx", sinvidx);
                    params.put("invidx", sinvidx);
                    Log.e("invidx", sinvidx);


                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            requestQueue.add(stringRequest);

        }
    }


}


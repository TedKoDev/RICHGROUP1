package com.example.RG.CommentAdapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ImageViewHolder>{


    String urlcommentdelete = "http://3.38.117.213/commentdelete.php"; //
    String urlcommentupdate = "http://3.38.117.213/commentupdate.php"; //

    String coidx,content,write_at;

    public CommentAdapter(Context context, ArrayList<CommentData> Commentlist) {
        this.context = context;
        this.commentlist = Commentlist;

    }

    private Context context;
    private ArrayList<CommentData> commentlist;


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_commentitem,parent,false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {



        holder.userName.setText(commentlist.get(position).getWriter_idx());
        holder.text.setText(commentlist.get(position).getContent());
        holder.comentidx.setText(commentlist.get(position).getCommentidx());
        holder.write_at.setText(commentlist.get(position).getWrite_at());




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




        return commentlist.size();
    }


    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView  userName, text, comentidx, write_at;

        String position;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.commentwriter);
            text = itemView.findViewById(R.id.commenttext);
            comentidx = itemView.findViewById(R.id.comidx);
            write_at = itemView.findViewById(R.id.write_at);




            itemView.setOnCreateContextMenuListener(this); //2. OnCreateContextMenuListener 리스너를 현재 클래스에서 구현한다고 설정해둡니다.
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            MenuItem Edit = contextMenu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = contextMenu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }
        // 4. 컨텍스트 메뉴에서 항목 클릭시 동작을 설정합니다.
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {



            @Override
            public boolean onMenuItemClick(MenuItem item) {


                switch (item.getItemId()) {
                    case 1001:  // 5. 편집 항목을 선택시


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        // 다이얼로그를 보여주기 위해 edit_box.xml 파일을 사용합니다.

                        View view = LayoutInflater.from(context)
                                .inflate(R.layout.commentedit_box, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.btn_commentchange);
                        final EditText editTextID = (EditText) view.findViewById(R.id.commentchange);
                        final TextView userName = (TextView) view.findViewById(R.id.commentwriteuser);
                        final TextView comidx = (TextView) view.findViewById(R.id.comidx);
                        final TextView write_time = (TextView) view.findViewById(R.id.write_at);




                        // 6. 해당 줄에 입력되어 있던 데이터를 불러와서 다이얼로그에 보여줍니다.
                        editTextID.setText(commentlist.get(getAdapterPosition()).getContent());
                        userName.setText(commentlist.get(getAdapterPosition()).getWriter_idx());
                        comidx.setText(commentlist.get(getAdapterPosition()).getCommentidx());
                        write_at.setText(commentlist.get(getAdapterPosition()).getWrite_at());




                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {


                            // 7. 수정 버튼을 클릭하면 현재 UI에 입력되어 있는 내용으로

                            public void onClick(View v) {

                             content = editTextID.getText().toString();

                                String writer = userName.getText().toString();
                                 coidx = comentidx.getText().toString();
                                String write_time = write_at.getText().toString();






                                CommentData commentData = new CommentData(content,writer,coidx, write_time);


                                // 8. ListArray에 있는 데이터를 변경하고
                                commentlist.set(getAdapterPosition(), commentData);


                                // 9. 어댑터에서 RecyclerView에 반영하도록 합니다.

                                notifyItemChanged(getAdapterPosition());
                                commentupdate();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                        break;

                    case 1002:
                        commentdelete();
                        commentlist.remove(getAdapterPosition());

                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), commentlist.size());


                        break;

                }
                return true;
            }
        };

        private void commentupdate() {




                StringRequest request = new StringRequest(Request.Method.POST, urlcommentupdate, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(context.getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.e(  "ggggg", response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {

                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {


                        Map<String, String> param = new HashMap<String, String>();
                        //맵핑을 통해서 이미지, 이름, 내용, 작성자(shared로 받은값)을 보내줌


                        param.put("commentidx", coidx);  //encodeImagae는  String 변수로 지정해둠  아래에서 값을 넣음
                        param.put("content", content);  //encodeImagae는  String 변수로 지정해둠  아래에서 값을 넣음


                        return param;
                    }
                };
                // requestQueue를 통해서 전달 하면  php문을 통해 DB table에 저장된다.
                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                requestQueue.add(request);





        }

        private void commentdelete() {


            coidx = comentidx.getText().toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlcommentdelete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Toast.makeText(context.getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


//                    Toast.makeText(context.getApplicationContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e(  "에러", error.toString());

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("commentidx",coidx);
                    Log.e(  "댓글번호",coidx);



                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            requestQueue.add(stringRequest);

        }
    }



}
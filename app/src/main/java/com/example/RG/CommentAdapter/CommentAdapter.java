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




            itemView.setOnCreateContextMenuListener(this); //2. OnCreateContextMenuListener ???????????? ?????? ??????????????? ??????????????? ??????????????????.
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            MenuItem Edit = contextMenu.add(Menu.NONE, 1001, 1, "??????");
            MenuItem Delete = contextMenu.add(Menu.NONE, 1002, 2, "??????");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }
        // 4. ???????????? ???????????? ?????? ????????? ????????? ???????????????.
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {



            @Override
            public boolean onMenuItemClick(MenuItem item) {


                switch (item.getItemId()) {
                    case 1001:  // 5. ?????? ????????? ?????????


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        // ?????????????????? ???????????? ?????? edit_box.xml ????????? ???????????????.

                        View view = LayoutInflater.from(context)
                                .inflate(R.layout.commentedit_box, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.btn_commentchange);
                        final EditText editTextID = (EditText) view.findViewById(R.id.commentchange);
                        final TextView userName = (TextView) view.findViewById(R.id.commentwriteuser);
                        final TextView comidx = (TextView) view.findViewById(R.id.comidx);
                        final TextView write_time = (TextView) view.findViewById(R.id.write_at);




                        // 6. ?????? ?????? ???????????? ?????? ???????????? ???????????? ?????????????????? ???????????????.
                        editTextID.setText(commentlist.get(getAdapterPosition()).getContent());
                        userName.setText(commentlist.get(getAdapterPosition()).getWriter_idx());
                        comidx.setText(commentlist.get(getAdapterPosition()).getCommentidx());
                        write_at.setText(commentlist.get(getAdapterPosition()).getWrite_at());




                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {


                            // 7. ?????? ????????? ???????????? ?????? UI??? ???????????? ?????? ????????????

                            public void onClick(View v) {

                             content = editTextID.getText().toString();

                                String writer = userName.getText().toString();
                                 coidx = comentidx.getText().toString();
                                String write_time = write_at.getText().toString();






                                CommentData commentData = new CommentData(content,writer,coidx, write_time);


                                // 8. ListArray??? ?????? ???????????? ????????????
                                commentlist.set(getAdapterPosition(), commentData);


                                // 9. ??????????????? RecyclerView??? ??????????????? ?????????.

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
                        //????????? ????????? ?????????, ??????, ??????, ?????????(shared??? ?????????)??? ?????????


                        param.put("commentidx", coidx);  //encodeImagae???  String ????????? ????????????  ???????????? ?????? ??????
                        param.put("content", content);  //encodeImagae???  String ????????? ????????????  ???????????? ?????? ??????


                        return param;
                    }
                };
                // requestQueue??? ????????? ?????? ??????  php?????? ?????? DB table??? ????????????.
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


//                    Toast.makeText(context.getApplicationContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e(  "??????", error.toString());

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("commentidx",coidx);
                    Log.e(  "????????????",coidx);



                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            requestQueue.add(stringRequest);

        }
    }



}
package com.example.RG.post;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.RG.CommentAdapter.CommentAdapter;
import com.example.RG.CommentAdapter.CommentData;
import com.example.RG.Home_group.GroupActivity;
import com.example.RG.LiveStreaming.VideoPlayerActivity;
import com.example.RG.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class PostdetailActivity extends AppCompatActivity {

    String loginuser, gridx, postnumber, postwriter, postwritedate, posturl;

    String postimg, pdisc;

    TextView postlogname, postgrnum, postnum;
    TextView writer, writetime, postdisc, videoornot;
    ImageView postiv, ivback_post, playbtn;
    RecyclerView postrv;
    EditText commentEtv;
    ImageView commentsave;
    String url = "http://3.38.117.213/postget.php";

    String urlpostdelete = "http://3.38.117.213/postdelete.php";
    String urlcomment = "http://3.38.117.213/commentget.php";
    String urlcommentinsert = "http://3.38.117.213/commentinsert.php";
    String urlcommentupdate = "http://3.38.117.213/gr_idgetinfo.php";
    String urlcommentdelete = "http://3.38.117.213/gr_idgetinfo.php";

    Button postupdate;
    Button postdelete;

    SharedPreferences sp;


    RecyclerView recyclerView;
    CommentData commentData;
    LinearLayoutManager layoutManager;
    NestedScrollView scrollView;
    int frompage = 0;
    int topage = 12;
    private ArrayList<CommentData> commentlist = new ArrayList<>();
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);
        getSupportActionBar().hide();


        ivback_post = findViewById(R.id.ivback_post);//뒤로 이동
        postupdate = findViewById(R.id.postupdate);//뒤로 이동
        postdelete = findViewById(R.id.postdelete);//뒤로 이동

        postlogname = findViewById(R.id.postlogname); //로그인유저
        postgrnum = findViewById(R.id.postgrnum);     //그룹넘버
        postnum = findViewById(R.id.postnum);    //게시물넘버

        writer = findViewById(R.id.writer); //게시글작성자
        writetime = findViewById(R.id.writetime);//게시글 작성일
        postiv = findViewById(R.id.postiv); //게시글 이미지
        postdisc = findViewById(R.id.postdisc); // 게시글내용
        playbtn = findViewById(R.id.playbtn);
        videoornot = findViewById(R.id.videoornot);
        playbtn.bringToFront();


        scrollView = findViewById(R.id.postdetailscroll_view);

        postrv = findViewById(R.id.postrv); // 댓글리사이클러뷰
        commentEtv = findViewById(R.id.commentEtv);//댓글작성란
        commentsave = findViewById(R.id.commentsave); // 댓글 업뎃 이미지뷰

        // 1.상부로그인유저, 2.상부그룹번호, 3.상부포스트번호,
        // usernick =================================== 로그인 유저 닉네임 값
        sp = getSharedPreferences("userName", MODE_PRIVATE);
        loginuser = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        postlogname.setText(loginuser); // name 란엔 xml파일에서 만들어둔 유저이름id값
        //=====================================================
        // =================================== GroupActivity 에서 Bottomsheet Fragment 건너뛰기 위해 사용함
        sp = getSharedPreferences("gridset", MODE_PRIVATE);
        gridx = sp.getString("그룹번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        postgrnum.setText(gridx); // name 란엔 xml파일에서 만들어둔 유저이름id값
        System.out.println(gridx);
        //======================================================


        // =================================== 게시물 번호
        sp = getSharedPreferences("게시물정보", MODE_PRIVATE);
        postnumber = sp.getString("포스트번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        postwriter = sp.getString("작성자이름", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        postwritedate = sp.getString("작성시간", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        posturl = sp.getString("url", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환

        Log.i("posturl", posturl);
        postnum.setText(postnumber); // name 란엔 xml파일에서 만들어둔 유저이름id값
        writer.setText(postwriter); //
        writetime.setText(postwritedate); //

        //======================================================

        if (!Objects.equals(posturl, "NotVideo")) {

            playbtn.setVisibility(View.VISIBLE);
            videoornot.setVisibility(View.VISIBLE);

            playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Log.i("어디>", "비디오뷰로");
                    Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);

                    intent.putExtra("url", posturl);


                    startActivity(intent);


                }
            });
        }

        loaddata(); // 1.포스트작성자, 2.포스트 작성일자, 3.포스트이미지, 4.포스트텍스트


        initrv();

        commentListData();


        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    topage = topage + 10;
                    commentListData();


                }

            }
        });


        commentsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentinsert();

            }
        });


        ivback_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);

            }
        });

        if (postwriter.equals(loginuser)) {
            postdelete.setVisibility(View.VISIBLE);
            postupdate.setVisibility(View.VISIBLE);

        } else {
            postdelete.setVisibility(View.INVISIBLE);
            postupdate.setVisibility(View.INVISIBLE);
        }


        postdelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(v.getContext());
                mbuilder.setTitle("삭제여부");
                mbuilder.setMessage("정말 게시글을 삭제하시겠습니까?");
                mbuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Your Button Click Action Code

                        postdeletevolley();
                        // 값입력_액티비티에서 사용
                        Intent intent = new Intent(getApplicationContext(), GroupActivity.class); //액티비티 전환
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                        startActivity(intent);
                    }
                });
                mbuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Your Button Click Action Code
                        dialog.dismiss();

                    }
                });
                mbuilder.show();
            }
        });

        postupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                BitmapDrawable drawable1 = (BitmapDrawable) postiv.getDrawable(); // imageview를 Bitmap으로 bitmap을 String으로
                Bitmap bitmap1 = drawable1.getBitmap();
                postiv.setImageBitmap(bitmap1);

                //Bitmap을 String형으로 변환
                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos1);
                byte[] bytes1 = baos1.toByteArray();
                postimg = Base64.encodeToString(bytes1, Base64.DEFAULT);


                // 값입력_액티비티에서 사용
                Intent intent = new Intent(getApplicationContext(), PostUpdateActivity.class); //액티비티 전환
                // 전달할 값 ( 첫번째 인자 : key, 두번째 인자 : 실제 전달할 값 )
                intent.putExtra("이미지", postimg);
                intent.putExtra("텍스트", pdisc);
                startActivity(intent);


            }
        });


    }

    private void commentinsert() {

        String comment = commentEtv.getText().toString();    // xml edittext 에서 입력 받은 이름


        StringRequest request = new StringRequest(Request.Method.POST, urlcommentinsert, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                commentEtv.setText(""); // 처리후 값을 비워줌


                commentListData();


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
                param.put("writer_idx", loginuser);  //encodeImagae는  String 변수로 지정해둠  아래에서 값을 넣음
                param.put("content", comment);    // 카테고리
                param.put("grpost_idx", postnumber);  //공개 비공개


                return param;
            }
        };
        // requestQueue를 통해서 전달 하면  php문을 통해 DB table에 저장된다.
        RequestQueue requestQueue = Volley.newRequestQueue(PostdetailActivity.this);
        requestQueue.add(request);


    }

    private void initrv() {

        recyclerView = findViewById(R.id.postrv);

        layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

    }

    private void commentListData() {


        StringRequest request = new StringRequest(Request.Method.POST, urlcomment, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                Log.e("respone", "5555");


                commentlist.clear();
                Log.e("시작", "1");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("시작", "212e12e");
                    String success = jsonObject.getString("success");
                    Log.e("시작", "31e12e");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("시작", "412e12e");
//                    Log.e(  "total", response);

                    if (success.equals("1")) {
                        Log.e("시작", "5");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.e("시작", "6");
                            JSONObject object = jsonArray.getJSONObject(i);
                            Log.e("시작", "7");

                            String comidx = object.getString("commentidx");
                            Log.e("변수명", "1");
                            String content = object.getString("content");
                            Log.e("변수명", "2");
                            String write_at = object.getString("write_at");
                            Log.e("변수명", "3");


                            // String 의 Date화
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// 필수 UTC의 값을 받아왔지만 다시한번 UTC타임존으로 포멧을 해줘야하는 아이러니함
                            Date datedtime = StringtoDateFormat.parse(write_at);

                            //UTC로 저장을 했지만 UTC로 다시 TIMEZONE을 성정 해주어야 정상적으로 출력된다.
                            // 이유를 모르겠다.

                            Log.i("987 소캣메세Date형태 utc재적용", String.valueOf(datedtime));


                            //또한  아래에서 SimpleDateFormat dateFormatLocalization 에서 DATE를 한번더 포맷 고정을 해주어야 변화가 안생긴다.
                            // 이유는 모르겠다
                            // 아마도 Mysql이 KST 기준이라 그런건가 싶기도하고 잘 모르곘다
                            // 여튼 2일동안 난리친걸 해결했으니 일단 넘어간다.
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatLocalization = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//***/*
                            Date utcDate = StringtoDateFormat.parse(dateFormatLocalization.format(datedtime));


                            //타임존을 지정한 Date를 String 화 시킴
//                            String ltime = StringtoDateFormat.format(datedtime);//이렇게 하면안되더라
                            String ltime = dateFormatLocalization.format(datedtime); // 이렇게 타임존 설정이 안되어있는 포맷화를 한번더  되더라 그래서 위에 ***이를 살려둬야한다.
                            System.out.println("987 지정 후 현지시간 적용후 스트링화 : " + ltime);
                            Log.i("987 datedtime", String.valueOf(datedtime));
                            Log.i("987 utcDate", String.valueOf(utcDate));

                            Log.i("987 time", String.valueOf(write_at));
                            Log.i("987 ltime", String.valueOf(ltime));


                            String writer = object.getString("writer_idx");
                            Log.e("변수명", "4");
                            String grp_idx = object.getString("grpost_idx");
                            Log.e("변수명", "4");


                            commentData = new CommentData(content, writer, comidx, ltime); //Data에 6개의 항목을 만들었기때문에 4개의 값을 가져와야함.
                            Log.e("변수명", "6");
                            commentlist.add(commentData);
                            Log.e("변수명", "7");


                            Log.e("변수명", response);


                        }
                        commentAdapter = new CommentAdapter(getApplicationContext(), commentlist); // 특이 context 가 아니라 getactivity로 받음

                        recyclerView.setAdapter(commentAdapter);

                        commentAdapter.notifyDataSetChanged();
                        Log.e("변수명", "8");

                        Log.e("error변수명", "145");

                    }
                } catch (Exception e) {
                }

//                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("grpost_idx", postnumber);
                String Sfrompage = String.valueOf(frompage);
                String Stopage = String.valueOf(topage);

                params.put("from", Sfrompage);
                params.put("to", Stopage);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }


    private void postdeletevolley() {


        postnumber = sp.getString("포스트번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlpostdelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getApplicationContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("에러", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//
                params.put("grpidx", postnumber);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    private void loaddata() {

        postnumber = sp.getString("포스트번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success"); //불러온 값의 참 거짓을 확인하는 분기점
                    if (success) {

                        String url3 = jsonObject.getString("grpimage");
                        pdisc = jsonObject.getString("grpdisc");
                        String grpidx = jsonObject.getString("grpidx");
                        String gr_idx = jsonObject.getString("gr_idx");
                        String grpwriter = jsonObject.getString("grpwriter");
                        String grpcreated_at = jsonObject.getString("grpcreated_at");

                        Glide.with(getApplicationContext()).load("http://3.38.117.213/image/" + url3).into(postiv);
                        postdisc.setText(pdisc);

//                        writetime.setText(pdate);
//                         writer.setText(pwriter);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();


//                    Toast.makeText(getApplicationContext(), "에러 : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getApplicationContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grpidx", postnumber);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}
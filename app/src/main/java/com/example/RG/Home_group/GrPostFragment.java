package com.example.RG.Home_group;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
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
import com.example.RG.ADD.AddGrpostActivity;
import com.example.RG.LiveStreaming.LiveActivity1;
import com.example.RG.MainActivity;
import com.example.RG.OnBackPressedListener;
import com.example.RG.R;
import com.example.RG.post.GrPostAdapter;
import com.example.RG.post.GrPostData;
import com.example.RG.post.PostdetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class GrPostFragment extends Fragment implements OnBackPressedListener {

    public static Context context;
    TextView loginemail, grname, grmaker, grdisc, gropendate, grnum, onoroff, pwd;
    ImageView grpback, grpimg;
    //    String url = "http://3.38.117.213/RG3postmain.php";
    String url = "http://3.38.117.213/gr_idgetinfo.php";
    String urlgrjoin = "http://3.38.117.213/grjoin.php";
    String urlgroupout = "http://3.38.117.213/groupout.php"; // StringRequest를 통해서 데이터값을 보낼 url
    String urlinoutcheck = "http://3.38.117.213/grinoutcheck.php";
    String urlchatroom = "http://3.38.117.213/chatroominsert.php"; // Chatroom DB 값 insert
    String num, grsetnum;
    String loginname;  //로그인유저 닉네임
    String guname;  //로그인유저 닉네임
    String gname; // 그룹이름
    String url3; // 그룹이미지
    String inout;
    String onoff;
    String 사용자;  //로그인유저 닉네임
    String 개설자; //그룹개설자 닉네임
    String Spwd;
    String recyclerurl = "http://3.38.117.213/RG3postget.php";
    Button grjoinbtn, grpostaddbtn, grchatbtn;
    RecyclerView recyclerView;
    GrPostData grPostData;
    NestedScrollView scrollView;
    int frompage = 0;
    int topage = 12;
    private ArrayList<GrPostData> postlist = new ArrayList<>();
    private GrPostAdapter grPostAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("프레그GPF oncreate", "1");

//        프래그먼트를 생성할 때 호출합니다. 프래그먼트가 일시정지 혹은 중단 후 재개되었을 때 유지하고 있어야 하는 것을 여기서 초기화 해야합니다.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup scontainer,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gr_post, scontainer, false);
        Log.i("프레그GPF onCreateView", "1");


//            imageview
        grpback = view.findViewById(R.id.grpback);
        grpimg = view.findViewById(R.id.grpimg);
//              Textview

        loginemail = view.findViewById(R.id.loginemail);  // shared로 로그인email 받기
        grname = view.findViewById(R.id.grname);
        grmaker = view.findViewById(R.id.grmaker);
        grdisc = view.findViewById(R.id.grdisc);
        gropendate = view.findViewById(R.id.gropendate);
        onoroff = view.findViewById(R.id.onoroff);
        pwd = view.findViewById(R.id.pwd);

        scrollView = view.findViewById(R.id.grpostscroll_view);


        grpostaddbtn = view.findViewById(R.id.grpaddbtn);

        grjoinbtn = view.findViewById(R.id.grjoinbtn);
        grnum = view.findViewById(R.id.grnum);
//        grchatbtn =view.findViewById(R.id.grchatbtn);//안씀


        SharedPreferences sp = this.getActivity().getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        loginemail.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값


// =================================== GroupActivity 에서 Bottomsheet Fragment 건너뛰기 위해 사용함  그룹번호 onpause 시 삭제 됨
        sp = this.getActivity().getSharedPreferences("gridset", MODE_PRIVATE);
        grsetnum = sp.getString("그룹번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        grnum.setText(grsetnum); // name 란엔 xml파일에서 만들어둔 유저이름id값
        //======================================================
        buildListData();
        initRecyclerView(view);


        Button 방송시작버튼 = view.findViewById(R.id.btnstartlive);

        방송시작버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Objects.equals(onoff, "공개"))  { Spwd = "Public";

                Intent intent = new Intent(getContext(), LiveActivity1.class);
                intent.putExtra("방번", grsetnum); // 보낼 값 입력
                intent.putExtra("그룹명", gname); // 그룹이름
                intent.putExtra("사용자", loginname); // 보낼 값 입력
                intent.putExtra("onoff", onoff); // 보낼 값 입력
                intent.putExtra("pwd", Spwd); // 보낼 값 입력
                Log.i("Spwd1", Spwd);
                startActivity(intent);

                }else{

                Intent intent = new Intent(getContext(), LiveActivity1.class);
                intent.putExtra("방번", grsetnum); // 보낼 값 입력
                intent.putExtra("그룹명", gname); // 그룹이름
                intent.putExtra("사용자", loginname); // 보낼 값 입력
                intent.putExtra("onoff", onoff); // 보낼 값 입력
                intent.putExtra("pwd", Spwd); // 보낼 값 입력
                Log.i("Spwd2", Spwd);
                startActivity(intent);

                }
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    topage = topage + 5;
                    buildListData();


                }

            }
        });


        상부그룹정보();
        참가여부확인();


        Log.i("프레그GPF onViewCreated", "2");
        grpback.setOnClickListener(new View.OnClickListener() { // 뒤로가기
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);


            }
        });


        grpostaddbtn.setOnClickListener(new View.OnClickListener() { // 글쓰기
            @Override
            public void onClick(View view) {
                inout = grjoinbtn.getText().toString();
                if (inout.equals("탈퇴")) {
                    //fragment에서 엑티비티로
                    Intent intent = new Intent(getActivity(), AddGrpostActivity.class);


                    intent.putExtra("grid", grnum.getText().toString());


                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "모임참여가 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        Log.i("프레그GPF onViewCreated", "3");

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                inout = grjoinbtn.getText().toString();
                grPostData = postlist.get(position);
                if (inout.equals("탈퇴")) {



                        Intent intent = new Intent(getContext(), PostdetailActivity.class);
                        Log.i("어디>", "게시글로");
                        ////================ 클릭하는순간 '그룹번호' 라는 이름으로 그룹번호의 정보가 저장된다.
                        SharedPreferences sp = getContext().getSharedPreferences("게시물정보", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("포스트번호", grPostData.getGrpidx());
                        editor.putString("작성자이름", grPostData.getGrpwriter());
                        editor.putString("작성시간", grPostData.getGrpcreated_at());
                        editor.putString("url", grPostData.getGrvideourl());

                        editor.apply();
////=============================================================================================================

                        startActivity(intent);

                } else {
                    Toast.makeText(getContext(), "모임참여가 필요합니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        Log.i("프레그GPF onViewCreated", "4");

        Log.i("프레그GPF onViewCreated", "5");


        return view;


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Log.i("프레그GPF ", "onViewStateRestored 1");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("프레그GPF onStart ", "1");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("프레그GPF onResume ", "1");
//        buildListData();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//
//        recyclerView.setLayoutManager(layoutManager);
//

    }

    @Override
    public void onPause() {
        super.onPause();
//        사용자가 프래그먼트를 떠나면 첫번 째로 이 메서드를 호출합니다. 사용자가 돌아오지 않을 수도 있으므로 여기에 현재 사용자 세션을 넘어 지속되어야 하는 변경사항을 저장합니다.

        Log.i("프레그GPF onPause ", "1");


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("프레그GPF onStop ", "1");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("프레그GPF onDestroyView ", "1");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("프레그GPF onDestroy ", "1");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("프레그GPF onDetach ", "1");
    }


    private void 참가여부확인() {
        String id = grnum.getText().toString();
//        Log.e("모임idx222", num);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlinoutcheck, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    Log.e("참가여부1", response);


                    if (response.equals("success")) {

                        grjoinbtn.setText("탈퇴");

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    grjoinbtn.setText("모임참여");
//                    Toast.makeText(getContext(), "에러 : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grid", id);
                params.put("loginname", loginname);
                Log.e("참가여부id", id);
                Log.e("참가여부logname", loginname);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    private void 상부그룹정보() {
        String id = grnum.getText().toString();
//        Log.e("모임idx222", num);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success"); //불러온 값의 참 거짓을 확인하는 분기점
                    if (success) {

                        String gid = jsonObject.getString("grid");
                        gname = jsonObject.getString("grname");
                        url3 = jsonObject.getString("grimage");
                        String gdisc = jsonObject.getString("grdisc");
                        String guname = jsonObject.getString("gruserName");
                        onoff = jsonObject.getString("onoff");
                        Spwd = jsonObject.getString("pwd");
                        String creat = jsonObject.getString("created_at");
                        // String 의 Date화
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// 필수 UTC의 값을 받아왔지만 다시한번 UTC타임존으로 포멧을 해줘야하는 아이러니함
                        Date datedtime = StringtoDateFormat.parse(creat);

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

                        Log.i("987 time", String.valueOf(creat));
                        Log.i("987 ltime", String.valueOf(ltime));
//                        String urlimage = "http://3.38.117.213/image/"+url2; Log.e(  "변수명", "5");


                        Glide.with(requireContext()).load("http://3.38.117.213/image/" + url3).into(grpimg);
                        grnum.setText(gid);
                        grname.setText(gname);
                        grdisc.setText(gdisc);
                        onoroff.setText(onoff);
//                        pwd.setText(pwdd);
                        grmaker.setText(guname);
                        Log.e("guname변수명1", guname);
                        gropendate.setText(ltime);


                        ////================ 클릭하는순간 '그룹번호' 라는 이름으로 그룹번호의 정보가 저장된다.
                        SharedPreferences sp = getContext().getSharedPreferences("개설자", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("개설자", guname);
                        editor.apply();
////=============================================================================================================

                        개설자 = grmaker.getText().toString();
                        사용자 = loginemail.getText().toString();

//                        grchatbtn.setOnClickListener(new View.OnClickListener() { // 채팅참여
//                            @Override
//                            public void onClick(View view) {
//
//                                Toast.makeText(getContext(),"Service 시작",Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getActivity(), ChatService.class);
//                                startService(intent);
//
//
//                                chatroomjoin();
//
//                            }
//                        });

                        if (개설자.equals(사용자)) {
                            grjoinbtn.setVisibility(View.INVISIBLE);
                        } else {
                            grjoinbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    // 모임참여 부분
                                    inout = grjoinbtn.getText().toString();
                                    Log.e("inout", inout);
                                    if (inout.equals("모임참여")) {
                                        //채팅방 참여 부분

                                        join();
                                        chatroomjoin();
                                    } else if (inout.equals("탈퇴")) {
                                        탈퇴();
                                    }


                                }
                            });
                        }

                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();


//                    Toast.makeText(getContext(), "에러 : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grid", id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void chatroomjoin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlchatroom, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cr_joiner", loginname);
                params.put("gr_img", url3);
                params.put("gr_idx", grsetnum);
                params.put("cr_name", gname);
                params.put("cr_boss", 개설자);

                Log.e("cr_joiner", loginname);
                Log.e("gr_idx", grsetnum);
                Log.e("cr_name", gname);
                Log.e("cr_boss", 개설자);
                Log.e("url3", url3);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    private void 탈퇴() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
        mbuilder.setTitle("탈퇴여부");
        mbuilder.setMessage("정말 그룹을 탈퇴하시겠습니까? \n 작성하신 게시글 및 채팅 목록도 삭제됩니다.");
        mbuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Your Button Click Action Code

                groupout();
                dialog.dismiss();

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

    private void groupout() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgroupout, new Response.Listener<String>() {
            //그룹에서 탈퇴함과 동시에 게시글과 채팅참여 목록도 전부 삭제됩니다. 채팅내용은 남아있습니다.
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                grjoinbtn.setText("모임참여");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("에러", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//
                params.put("like_user", loginname);
                params.put("like_gridx", grsetnum);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    private void join() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgrjoin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();


                grjoinbtn.setText("탈퇴");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("like_user", loginname);
                params.put("like_gridx", grsetnum);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }


    private void initRecyclerView(View view) {


        recyclerView = view.findViewById(R.id.postgrecyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

    }

    private void buildListData() {

        StringRequest request = new StringRequest(Request.Method.POST, recyclerurl, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                Log.e("respone", "5555");


                postlist.clear();
                Log.e("시작", "1");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    Log.e("total", response);

                    if (success.equals("1")) {
                        Log.e("시작", "5");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.e("시작", "6");
                            JSONObject object = jsonArray.getJSONObject(i);
                            Log.e("시작", "7");

                            String id = object.getString("grpidx");

                            String pid = object.getString("gr_idx");

                            String url2 = object.getString("grpimage");

                            String disc = object.getString("grpdisc");

                            String maker = object.getString("grpwriter");

                            String opendate = object.getString("grpcreated_at");


                            // String 의 Date화
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// 필수 UTC의 값을 받아왔지만 다시한번 UTC타임존으로 포멧을 해줘야하는 아이러니함
                            Date datedtime = StringtoDateFormat.parse(opendate);

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

                            Log.i("987 time", String.valueOf(opendate));
                            Log.i("987 ltime", String.valueOf(ltime));


                            Log.i("opendate", opendate);

                            String videostarttime = object.getString("grpstarttime");


                            String grvideourl = object.getString("grvideourl");

                            String livedisc;

                            if (!grvideourl.equals("NotVideo")) {//비디오인 경우에는 시작시간을 넣어줌
                                livedisc = "방송 녹화물: " + disc;
                                Log.i("livedisc2", livedisc);

                            } else {
                                livedisc = disc;
                                Log.i("livedisc3", livedisc);//비디오가 아닌경우에는  작성시간을 넣어줌
                            }

//                            String posttime;
//
//                            if (!videostarttime.equals("NotVideo")) {//비디오인 경우에는 시작시간을 넣어줌
//                                posttime = videostarttime;
//                                Log.i("opendate2", posttime);
//
//                            } else  {
//                                posttime = opendate;
//                                Log.i("opendate3", posttime);//비디오가 아닌경우에는  작성시간을 넣어줌
//                            }


                            String urlimage = "http://3.38.117.213/image/" + url2;
                            Log.e("변수명", "5");

                            grPostData = new GrPostData(id, pid, urlimage, livedisc, maker, ltime, grvideourl); //Data에 6개의 항목을 만들었기때문에 4개의 값을 가져와야함.
                            Log.e("변수명", "6");
                            postlist.add(grPostData);
                            Log.e("변수명", "7");


                            Log.e("변수명", response);


                        }
                        grPostAdapter = new GrPostAdapter(getContext(), postlist); // 특이 context 가 아니라 getactivity로 받음

                        recyclerView.setAdapter(grPostAdapter);

                        grPostAdapter.notifyDataSetChanged();
                        Log.e("변수명", "8");

                        Log.e("error변수명", "145");

                    }
                } catch (Exception e) {
                }

//                    Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


//                // 액티비티에서 보내준값 프레그먼트에서 값받기   GroupID값 받음
//                String num = "";
//
//                Bundle bundle1 = getArguments();
//                if (bundle1 != null) {
//                    num = bundle1.getString("grid");
//                }
//                grnum.setText(num);
//
//                String id = grnum.getText().toString();
//                Log.e("모임idx", num);

                Map<String, String> params = new HashMap<>();
                params.put("gr_idx", grsetnum);
                Log.i("갑자기", grsetnum);
                String Sfrompage = String.valueOf(frompage);
                String Stopage = String.valueOf(topage);

                params.put("from", Sfrompage);
                params.put("to", Stopage);
                Log.i("갑자기", Sfrompage);
                Log.i("갑자기", Stopage);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }

    @Override
    public void onBackPressed() {


    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GrPostFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GrPostFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
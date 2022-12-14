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
    String urlgroupout = "http://3.38.117.213/groupout.php"; // StringRequest??? ????????? ??????????????? ?????? url
    String urlinoutcheck = "http://3.38.117.213/grinoutcheck.php";
    String urlchatroom = "http://3.38.117.213/chatroominsert.php"; // Chatroom DB ??? insert
    String num, grsetnum;
    String loginname;  //??????????????? ?????????
    String guname;  //??????????????? ?????????
    String gname; // ????????????
    String url3; // ???????????????
    String inout;
    String onoff;
    String ?????????;  //??????????????? ?????????
    String ?????????; //??????????????? ?????????
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

        Log.i("?????????GPF oncreate", "1");

//        ?????????????????? ????????? ??? ???????????????. ?????????????????? ???????????? ?????? ?????? ??? ??????????????? ??? ???????????? ????????? ?????? ?????? ????????? ????????? ???????????????.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup scontainer,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gr_post, scontainer, false);
        Log.i("?????????GPF onCreateView", "1");


//            imageview
        grpback = view.findViewById(R.id.grpback);
        grpimg = view.findViewById(R.id.grpimg);
//              Textview

        loginemail = view.findViewById(R.id.loginemail);  // shared??? ?????????email ??????
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
//        grchatbtn =view.findViewById(R.id.grchatbtn);//??????


        SharedPreferences sp = this.getActivity().getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key?????? ????????? ?????? ???????????????, ???????????????""(??????)??? ??????
        loginemail.setText(loginname); // name ?????? xml???????????? ???????????? ????????????id???


// =================================== GroupActivity ?????? Bottomsheet Fragment ???????????? ?????? ?????????  ???????????? onpause ??? ?????? ???
        sp = this.getActivity().getSharedPreferences("gridset", MODE_PRIVATE);
        grsetnum = sp.getString("????????????", " "); //key?????? ????????? ?????? ???????????????, ???????????????""(??????)??? ??????
        grnum.setText(grsetnum); // name ?????? xml???????????? ???????????? ????????????id???
        //======================================================
        buildListData();
        initRecyclerView(view);


        Button ?????????????????? = view.findViewById(R.id.btnstartlive);

        ??????????????????.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Objects.equals(onoff, "??????"))  { Spwd = "Public";

                Intent intent = new Intent(getContext(), LiveActivity1.class);
                intent.putExtra("??????", grsetnum); // ?????? ??? ??????
                intent.putExtra("?????????", gname); // ????????????
                intent.putExtra("?????????", loginname); // ?????? ??? ??????
                intent.putExtra("onoff", onoff); // ?????? ??? ??????
                intent.putExtra("pwd", Spwd); // ?????? ??? ??????
                Log.i("Spwd1", Spwd);
                startActivity(intent);

                }else{

                Intent intent = new Intent(getContext(), LiveActivity1.class);
                intent.putExtra("??????", grsetnum); // ?????? ??? ??????
                intent.putExtra("?????????", gname); // ????????????
                intent.putExtra("?????????", loginname); // ?????? ??? ??????
                intent.putExtra("onoff", onoff); // ?????? ??? ??????
                intent.putExtra("pwd", Spwd); // ?????? ??? ??????
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


        ??????????????????();
        ??????????????????();


        Log.i("?????????GPF onViewCreated", "2");
        grpback.setOnClickListener(new View.OnClickListener() { // ????????????
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//???????????? ????????????
                startActivity(intent);


            }
        });


        grpostaddbtn.setOnClickListener(new View.OnClickListener() { // ?????????
            @Override
            public void onClick(View view) {
                inout = grjoinbtn.getText().toString();
                if (inout.equals("??????")) {
                    //fragment?????? ???????????????
                    Intent intent = new Intent(getActivity(), AddGrpostActivity.class);


                    intent.putExtra("grid", grnum.getText().toString());


                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "??????????????? ???????????????.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        Log.i("?????????GPF onViewCreated", "3");

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                inout = grjoinbtn.getText().toString();
                grPostData = postlist.get(position);
                if (inout.equals("??????")) {



                        Intent intent = new Intent(getContext(), PostdetailActivity.class);
                        Log.i("??????>", "????????????");
                        ////================ ?????????????????? '????????????' ?????? ???????????? ??????????????? ????????? ????????????.
                        SharedPreferences sp = getContext().getSharedPreferences("???????????????", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("???????????????", grPostData.getGrpidx());
                        editor.putString("???????????????", grPostData.getGrpwriter());
                        editor.putString("????????????", grPostData.getGrpcreated_at());
                        editor.putString("url", grPostData.getGrvideourl());

                        editor.apply();
////=============================================================================================================

                        startActivity(intent);

                } else {
                    Toast.makeText(getContext(), "??????????????? ???????????????.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        Log.i("?????????GPF onViewCreated", "4");

        Log.i("?????????GPF onViewCreated", "5");


        return view;


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Log.i("?????????GPF ", "onViewStateRestored 1");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("?????????GPF onStart ", "1");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("?????????GPF onResume ", "1");
//        buildListData();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//
//        recyclerView.setLayoutManager(layoutManager);
//

    }

    @Override
    public void onPause() {
        super.onPause();
//        ???????????? ?????????????????? ????????? ?????? ?????? ??? ???????????? ???????????????. ???????????? ???????????? ?????? ?????? ???????????? ????????? ?????? ????????? ????????? ?????? ??????????????? ?????? ??????????????? ???????????????.

        Log.i("?????????GPF onPause ", "1");


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("?????????GPF onStop ", "1");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("?????????GPF onDestroyView ", "1");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("?????????GPF onDestroy ", "1");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("?????????GPF onDetach ", "1");
    }


    private void ??????????????????() {
        String id = grnum.getText().toString();
//        Log.e("??????idx222", num);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlinoutcheck, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    Log.e("????????????1", response);


                    if (response.equals("success")) {

                        grjoinbtn.setText("??????");

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    grjoinbtn.setText("????????????");
//                    Toast.makeText(getContext(), "?????? : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grid", id);
                params.put("loginname", loginname);
                Log.e("????????????id", id);
                Log.e("????????????logname", loginname);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    private void ??????????????????() {
        String id = grnum.getText().toString();
//        Log.e("??????idx222", num);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success"); //????????? ?????? ??? ????????? ???????????? ?????????
                    if (success) {

                        String gid = jsonObject.getString("grid");
                        gname = jsonObject.getString("grname");
                        url3 = jsonObject.getString("grimage");
                        String gdisc = jsonObject.getString("grdisc");
                        String guname = jsonObject.getString("gruserName");
                        onoff = jsonObject.getString("onoff");
                        Spwd = jsonObject.getString("pwd");
                        String creat = jsonObject.getString("created_at");
                        // String ??? Date???
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// ?????? UTC??? ?????? ??????????????? ???????????? UTC??????????????? ????????? ??????????????? ???????????????
                        Date datedtime = StringtoDateFormat.parse(creat);

                        //UTC??? ????????? ????????? UTC??? ?????? TIMEZONE??? ?????? ???????????? ??????????????? ????????????.
                        // ????????? ????????????.

                        Log.i("987 ????????????Date?????? utc?????????", String.valueOf(datedtime));


                        //??????  ???????????? SimpleDateFormat dateFormatLocalization ?????? DATE??? ????????? ?????? ????????? ???????????? ????????? ????????????.
                        // ????????? ????????????
                        // ????????? Mysql??? KST ???????????? ???????????? ??????????????? ??? ????????????
                        // ?????? 2????????? ???????????? ??????????????? ?????? ????????????.
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatLocalization = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//***/*
                        Date utcDate = StringtoDateFormat.parse(dateFormatLocalization.format(datedtime));


                        //???????????? ????????? Date??? String ??? ??????
//                            String ltime = StringtoDateFormat.format(datedtime);//????????? ??????????????????
                        String ltime = dateFormatLocalization.format(datedtime); // ????????? ????????? ????????? ??????????????? ???????????? ?????????  ????????? ????????? ?????? ***?????? ??????????????????.
                        System.out.println("987 ?????? ??? ???????????? ????????? ???????????? : " + ltime);
                        Log.i("987 datedtime", String.valueOf(datedtime));
                        Log.i("987 utcDate", String.valueOf(utcDate));

                        Log.i("987 time", String.valueOf(creat));
                        Log.i("987 ltime", String.valueOf(ltime));
//                        String urlimage = "http://3.38.117.213/image/"+url2; Log.e(  "?????????", "5");


                        Glide.with(requireContext()).load("http://3.38.117.213/image/" + url3).into(grpimg);
                        grnum.setText(gid);
                        grname.setText(gname);
                        grdisc.setText(gdisc);
                        onoroff.setText(onoff);
//                        pwd.setText(pwdd);
                        grmaker.setText(guname);
                        Log.e("guname?????????1", guname);
                        gropendate.setText(ltime);


                        ////================ ?????????????????? '????????????' ?????? ???????????? ??????????????? ????????? ????????????.
                        SharedPreferences sp = getContext().getSharedPreferences("?????????", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("?????????", guname);
                        editor.apply();
////=============================================================================================================

                        ????????? = grmaker.getText().toString();
                        ????????? = loginemail.getText().toString();

//                        grchatbtn.setOnClickListener(new View.OnClickListener() { // ????????????
//                            @Override
//                            public void onClick(View view) {
//
//                                Toast.makeText(getContext(),"Service ??????",Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getActivity(), ChatService.class);
//                                startService(intent);
//
//
//                                chatroomjoin();
//
//                            }
//                        });

                        if (?????????.equals(?????????)) {
                            grjoinbtn.setVisibility(View.INVISIBLE);
                        } else {
                            grjoinbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    // ???????????? ??????
                                    inout = grjoinbtn.getText().toString();
                                    Log.e("inout", inout);
                                    if (inout.equals("????????????")) {
                                        //????????? ?????? ??????

                                        join();
                                        chatroomjoin();
                                    } else if (inout.equals("??????")) {
                                        ??????();
                                    }


                                }
                            });
                        }

                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();


//                    Toast.makeText(getContext(), "?????? : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
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


//                Toast.makeText(getContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cr_joiner", loginname);
                params.put("gr_img", url3);
                params.put("gr_idx", grsetnum);
                params.put("cr_name", gname);
                params.put("cr_boss", ?????????);

                Log.e("cr_joiner", loginname);
                Log.e("gr_idx", grsetnum);
                Log.e("cr_name", gname);
                Log.e("cr_boss", ?????????);
                Log.e("url3", url3);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    private void ??????() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
        mbuilder.setTitle("????????????");
        mbuilder.setMessage("?????? ????????? ????????????????????????? \n ???????????? ????????? ??? ?????? ????????? ???????????????.");
        mbuilder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Your Button Click Action Code

                groupout();
                dialog.dismiss();

            }
        });
        mbuilder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
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
            //???????????? ???????????? ????????? ???????????? ???????????? ????????? ?????? ???????????????. ??????????????? ??????????????????.
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                grjoinbtn.setText("????????????");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("??????", error.toString());

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


                grjoinbtn.setText("??????");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
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
                Log.e("??????", "1");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    Log.e("total", response);

                    if (success.equals("1")) {
                        Log.e("??????", "5");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.e("??????", "6");
                            JSONObject object = jsonArray.getJSONObject(i);
                            Log.e("??????", "7");

                            String id = object.getString("grpidx");

                            String pid = object.getString("gr_idx");

                            String url2 = object.getString("grpimage");

                            String disc = object.getString("grpdisc");

                            String maker = object.getString("grpwriter");

                            String opendate = object.getString("grpcreated_at");


                            // String ??? Date???
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// ?????? UTC??? ?????? ??????????????? ???????????? UTC??????????????? ????????? ??????????????? ???????????????
                            Date datedtime = StringtoDateFormat.parse(opendate);

                            //UTC??? ????????? ????????? UTC??? ?????? TIMEZONE??? ?????? ???????????? ??????????????? ????????????.
                            // ????????? ????????????.

                            Log.i("987 ????????????Date?????? utc?????????", String.valueOf(datedtime));


                            //??????  ???????????? SimpleDateFormat dateFormatLocalization ?????? DATE??? ????????? ?????? ????????? ???????????? ????????? ????????????.
                            // ????????? ????????????
                            // ????????? Mysql??? KST ???????????? ???????????? ??????????????? ??? ????????????
                            // ?????? 2????????? ???????????? ??????????????? ?????? ????????????.
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatLocalization = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//***/*
                            Date utcDate = StringtoDateFormat.parse(dateFormatLocalization.format(datedtime));


                            //???????????? ????????? Date??? String ??? ??????
                            //                            String ltime = StringtoDateFormat.format(datedtime);//????????? ??????????????????
                            String ltime = dateFormatLocalization.format(datedtime); // ????????? ????????? ????????? ??????????????? ???????????? ?????????  ????????? ????????? ?????? ***?????? ??????????????????.
                            System.out.println("987 ?????? ??? ???????????? ????????? ???????????? : " + ltime);
                            Log.i("987 datedtime", String.valueOf(datedtime));
                            Log.i("987 utcDate", String.valueOf(utcDate));

                            Log.i("987 time", String.valueOf(opendate));
                            Log.i("987 ltime", String.valueOf(ltime));


                            Log.i("opendate", opendate);

                            String videostarttime = object.getString("grpstarttime");


                            String grvideourl = object.getString("grvideourl");

                            String livedisc;

                            if (!grvideourl.equals("NotVideo")) {//???????????? ???????????? ??????????????? ?????????
                                livedisc = "?????? ?????????: " + disc;
                                Log.i("livedisc2", livedisc);

                            } else {
                                livedisc = disc;
                                Log.i("livedisc3", livedisc);//???????????? ??????????????????  ??????????????? ?????????
                            }

//                            String posttime;
//
//                            if (!videostarttime.equals("NotVideo")) {//???????????? ???????????? ??????????????? ?????????
//                                posttime = videostarttime;
//                                Log.i("opendate2", posttime);
//
//                            } else  {
//                                posttime = opendate;
//                                Log.i("opendate3", posttime);//???????????? ??????????????????  ??????????????? ?????????
//                            }


                            String urlimage = "http://3.38.117.213/image/" + url2;
                            Log.e("?????????", "5");

                            grPostData = new GrPostData(id, pid, urlimage, livedisc, maker, ltime, grvideourl); //Data??? 6?????? ????????? ????????????????????? 4?????? ?????? ???????????????.
                            Log.e("?????????", "6");
                            postlist.add(grPostData);
                            Log.e("?????????", "7");


                            Log.e("?????????", response);


                        }
                        grPostAdapter = new GrPostAdapter(getContext(), postlist); // ?????? context ??? ????????? getactivity??? ??????

                        recyclerView.setAdapter(grPostAdapter);

                        grPostAdapter.notifyDataSetChanged();
                        Log.e("?????????", "8");

                        Log.e("error?????????", "145");

                    }
                } catch (Exception e) {
                }

//                    Toast.makeText(getActivity(), "??????", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


//                // ?????????????????? ???????????? ????????????????????? ?????????   GroupID??? ??????
//                String num = "";
//
//                Bundle bundle1 = getArguments();
//                if (bundle1 != null) {
//                    num = bundle1.getString("grid");
//                }
//                grnum.setText(num);
//
//                String id = grnum.getText().toString();
//                Log.e("??????idx", num);

                Map<String, String> params = new HashMap<>();
                params.put("gr_idx", grsetnum);
                Log.i("?????????", grsetnum);
                String Sfrompage = String.valueOf(frompage);
                String Stopage = String.valueOf(topage);

                params.put("from", Sfrompage);
                params.put("to", Stopage);
                Log.i("?????????", Sfrompage);
                Log.i("?????????", Stopage);
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
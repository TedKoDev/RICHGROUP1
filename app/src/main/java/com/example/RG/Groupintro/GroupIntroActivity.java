package com.example.RG.Groupintro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.RG.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class GroupIntroActivity extends AppCompatActivity {
    SharedPreferences sp;
    String gridx, loginname, grsetnum;
    TextView logname, grnum; // 유저닉, 그룹넘버

    TextView introname, introdisc, introonoff, introopendate, intropeople, intromaker, introcate;

    String url = "http://3.38.117.213/gr_idgetinfo.php";
    String urll = "http://3.38.117.213/gr_getcount.php";

    ImageView ivback, introimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_intro);
        getSupportActionBar().hide();
        logname = findViewById(R.id.logname);
        grnum = findViewById(R.id.grnum);
        ivback = findViewById(R.id.ivback);

        introimg = findViewById(R.id.introimg);

        introname = findViewById(R.id.introname);
        intromaker = findViewById(R.id.intromaker);
        introdisc = findViewById(R.id.introdisc);
        introonoff = findViewById(R.id.introonoff);
        introcate = findViewById(R.id.introcate);
        introopendate = findViewById(R.id.introopendate);
        intropeople = findViewById(R.id.intropeople);


        // usernick =================================== 로그인 유저 닉네임 값
        sp = getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값
        //=====================================================


        // =================================== GroupActivity 에서 Bottomsheet Fragment 건너뛰기 위해 사용함  그룹번호 onpause 시 삭제 됨
        sp = getSharedPreferences("gridset", MODE_PRIVATE);
        grsetnum = sp.getString("그룹번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        grnum.setText(grsetnum); // name 란엔 xml파일에서 만들어둔 유저이름id값
        System.out.println(grsetnum);
        //======================================================
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 프레그먼트로 이동하는법 알아야함
                Intent intent = new Intent(getApplicationContext(), GroupSettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);

            }
        });

        getinfo_exceptcount();
        getinfo_count();


    }


    private void getinfo_exceptcount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success"); //불러온 값의 참 거짓을 확인하는 분기점
                    if (success) {

                        String gid = jsonObject.getString("grid");
                        String gname = jsonObject.getString("grname");
                        String url3 = jsonObject.getString("grimage");
                        String gdisc = jsonObject.getString("grdisc");
                        String guname = jsonObject.getString("gruserName");
                        String cate = jsonObject.getString("category");
                        String onoff = jsonObject.getString("onoff");
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


                        Glide.with(getApplicationContext()).load("http://3.38.117.213/image/" + url3).into(introimg);
                        grnum.setText(gid);
                        introname.setText(gname);
                        introdisc.setText(gdisc);
                        introonoff.setText(onoff);
                        introcate.setText(cate);
                        intromaker.setText(guname);
                        introopendate.setText(ltime);

                    }
                } catch (JSONException | ParseException e) {
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
                params.put("grid", grsetnum);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getinfo_count() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                Log.e("수량확인", response);


                String count = (response);
                Log.e("수량확인", count);


                intropeople.setText(count);

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
                params.put("grid", grsetnum);
                Log.e("chlwhd수명", grsetnum);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


}
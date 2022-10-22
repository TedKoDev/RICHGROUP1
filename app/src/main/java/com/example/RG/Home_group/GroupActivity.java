package com.example.RG.Home_group;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.ExampleBottomSheetDialog;
import com.example.RG.MainActivity;
import com.example.RG.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GroupActivity extends AppCompatActivity implements ExampleBottomSheetDialog.BottomSheetListener {

    public static int noreadchatcount;
    public static Handler GrAcHandler;

    static BottomNavigationView bottomNavigationView;
    static String grsetnum;
    public static String GRCF용룸;
    GrPostFragment grPostFragment = new GrPostFragment();
    GrChatFragment grChatFragment = new GrChatFragment();
    GrAboutFragment grAboutFragment = new GrAboutFragment();
    String str1;
    String 방이름;
    String 사용자이름;
    TextView GRATV1, GRATV2, GRATV3, GRATV4, GRATV5, GRATV6, grnum, chatroomname;
    SharedPreferences sp;
    String GrFragChatreadcount = "http://3.38.117.213/GrFragChatreadcount.php";
    String urldeletechatread = "http://3.38.117.213/deletechatread.php";
    String noreadcount;
    Handler handler = new Handler();
    boolean b = false;

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
        startActivity(intent);

        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        getSupportActionBar().hide();
        GrAcHandler = new Handler();



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Log.i("프레그GRAc onCreate", "1");
        GRATV1 = findViewById(R.id.GRATV1);
        GRATV2 = findViewById(R.id.GRATV2);
        GRATV3 = findViewById(R.id.GRATV3);
        GRATV4 = findViewById(R.id.GRATV4);
        GRATV5 = findViewById(R.id.GRATV5);

        GRATV6 = findViewById(R.id.GRATV6);
        grnum = findViewById(R.id.grnum);
        chatroomname = findViewById(R.id.chatroomname);
        Log.i("프레그GRAc onCreate", "2");
        // ===================================
        sp = getSharedPreferences("gridset", MODE_PRIVATE);
        grsetnum = sp.getString("그룹번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        방이름 = sp.getString("그룹이름", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        grnum.setText(grsetnum); // name 란엔 xml파일에서 만들어둔 유저이름id값
        chatroomname.setText(방이름);
        //======================================================


        sp = getSharedPreferences("userName", Context.MODE_PRIVATE);
        사용자이름 = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
//        //======================================================



        SharedPreferences sp1 = getSharedPreferences("GRCF용룸", MODE_PRIVATE);

        GRCF용룸  = sp1.getString("room1"," "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환





        Log.i("프레그GRAc onCreate", "3");
        bottomNavigationView = findViewById(R.id.homebottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.grmenu_frame_layout, grPostFragment).commit();


        buildListData();


        Log.i("프레그GRAc onResume", "5");
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i("프레그GRAc onResume", "6");

                switch (item.getItemId()) {
                    case R.id.grdashboard:
                        GRCF용룸 = "NO";
                        if (grPostFragment == null) {
                            Log.i("프레그GRAc onResume", "7");
                            getSupportFragmentManager().beginTransaction().add(R.id.grmenu_frame_layout, grPostFragment).commit();
                            Log.i("프레그GRAc onResume", "7 out");


                        } else if (grPostFragment != null) {
                            Log.i("프레그GRAc onResume", "8");


                            getSupportFragmentManager().beginTransaction().replace(R.id.grmenu_frame_layout, grPostFragment).commit();
//                            getSupportFragmentManager().popBackStack();
                            Log.i("프레그GRAc onResume", "8 out");
                        }
                        return true;
                    case R.id.grchat:

//                        SharedPreferences sp1 = getSharedPreferences("GRCF용룸", MODE_PRIVATE);
//
//                        GRCF용룸  = sp1.getString("room1"," "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
//
//
//
//                        b = true;
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                while (b) {
//
//                                    //원하는 타이머를 설정합니다. 현재 10초.
//                                    handler.post(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                            BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.grchat); // 네이게이션에 숫자 나타내게 해주는것
//
//                                            badgeDrawable.setNumber(0);
//                                            noreadchatcount = 0;
////                                            DeletechatreadData();// 최초 진입시 채팅 목록 불러오기 HTTP 통신
//                                            b = false;
//                                        }
//                                    });
//
//
//                                }
//
//                            }
//                        }).start();


//
                        Log.i("프레그GRAc onResume", "9");
                        getSupportFragmentManager().beginTransaction().replace(R.id.grmenu_frame_layout, grChatFragment).commit();
                        Log.i("프레그GRAc onResume", "9 out ");
                        return true;
                    case R.id.grabout:
                        GRCF용룸 = "NO";
                        Log.i("프레그GRAc onResume", "10");
                        ExampleBottomSheetDialog bottomSheetDialog = new ExampleBottomSheetDialog();
                        bottomSheetDialog.show(getSupportFragmentManager(), "exampleBottomSheet");


//                        getSupportFragmentManager().beginTransaction().replace(R.id.grmenu_frame_layout, grAboutFragment).commit();
                        return true;

                }
                return false;
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    private void DeletechatreadData() {
        StringRequest request = new StringRequest(Request.Method.POST, urldeletechatread, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                Log.e("채팅방목록 메세지숫자 사라짐 ", response);


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
                params.put("roomnumber", grsetnum);
                params.put("username", 사용자이름);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }

    private void buildListData() {

        StringRequest request = new StringRequest(Request.Method.POST, GrFragChatreadcount, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {


                Log.i("프레그GRAc", "탈출111");

                try {
                    Log.i("프레그GRAc", "탈출111222");
//                    noreadcount = response;
////                    JSONObject jsonObject = new JSONObject(response);
//                    Log.i("프레그GRAc333", "탈출11333331" + response);
////                    String success = jsonObject.getString("success");
//                    Log.i("11111111noread", response);
//
//
//                    noreadchatcount = Integer.parseInt(noreadcount);
//
//                    Log.i("프레그GRAc onCreate", "4");
//                    BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.grchat); // 네이게이션에 숫자 나타내게 해주는것
//
//
//                    badgeDrawable.setNumber(noreadchatcount);
                    Log.i("프레그GRAc onCreate", "5");

                    Log.i("프레그GRAc", "탈출1144444333331");
                } catch (Exception e) {
                    Log.i("프레그GRAc", "탈출1155555333556655331");
                }


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
                params.put("roomnumber", grsetnum);
                params.put("cr_joiner", 사용자이름);

                Log.i("프레그GRAc", "탈출1144441");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }

    @Override
    public void onButtonClicked(String text) {
        Log.i("프레그GRAc onButtonClicked", "1");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("프레그GRAc onStart", "1");
    }

    @Override
    public void onPause() {
        super.onPause();
        //        사용자가 프래그먼트를 떠나면 첫번 째로 이 메서드를 호출합니다. 사용자가 돌아오지 않을 수도 있으므로 여기에 현재 사용자 세션을 넘어 지속되어야 하는 변경사항을 저장합니다.
        Log.i("프레그GRAc onPause", "1");



    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("프레그GRAc onStop", "1");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("프레그GRAc onDestroy", "1");
    }

//    public static class Noread implements Runnable {
//        private final String room;
//
//        public Noread(String room) {
//            this.room = room;
//        }
//
//        @Override
//        public void run() {
//            String Msg = room;
//
//            Log.i("메세지들어옴여", Msg);
//            Log.i("메세지들어옴여  GRCF용룸", GRCF용룸);
//            Log.i("메세지들어옴여   room", room);
//
//
//            if (!Objects.equals(GRCF용룸, room)) {
//
//
//                noreadchatcount++;
//                BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.grchat); // 네이게이션에 숫자 나타내게 해주는것
//
//                badgeDrawable.setNumber(noreadchatcount);
//                Log.i("메세지들어옴여", String.valueOf(noreadchatcount));
//            }else {
//                Log.i("메세지들어옴여", "메세지못들옴");
//            }
//
//        }


//    }

}

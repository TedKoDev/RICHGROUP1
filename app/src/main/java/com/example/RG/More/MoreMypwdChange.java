package com.example.RG.More;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.R;
import com.example.RG.register.LoginActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoreMypwdChange extends AppCompatActivity {

    String pwdchangeURL = "http://3.38.117.213/pwdupdate.php"; //
    String url비번확인 = "http://3.38.117.213/pwdcheck.php"; //
    TextView logname;


    LinearLayout layoutnowpwd;
    LinearLayout layoutkalogpwdchag;

    TextView tv_checkpw, tv_checkpwre, nowid;
    EditText nowpass;
    Button btn_nowpass;
    String S비밀번호확인;


    ImageView setback;

    String loginname, oldpasswd, useremail;
    TextView oldpwd, newpwdcheck;
    String newpwdd;
    EditText newpwd1, newpwd2;
    Button btnpwdchagne, btncannotchange;
    LinearLayout pwdchangelayout;
    private boolean pasw = false; //패스워드 충족값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypwd_change);
        getSupportActionBar().hide();

        logname = findViewById(R.id.logname);
        layoutnowpwd = findViewById(R.id.layoutnowpwd);
        tv_checkpw = findViewById(R.id.tv_checkpw);
        tv_checkpwre = findViewById(R.id.tv_checkpwre);
        nowid = findViewById(R.id.nowid);
        nowpass = findViewById(R.id.nowpass);
        btn_nowpass = findViewById(R.id.btn_nowpass);
        setback = findViewById(R.id.setback);


        newpwdcheck = findViewById(R.id.newpwdcheck);
        newpwd1 = findViewById(R.id.newpwd1);
        newpwd2 = findViewById(R.id.newpwd2);
        btnpwdchagne = findViewById(R.id.btnpwdchagne);
        btncannotchange = findViewById(R.id.btncannotchange);
        pwdchangelayout = findViewById(R.id.pwdchangelayout);
        layoutkalogpwdchag = findViewById(R.id.layoutkalogpwdchag);


        //유저네임을 사용하는 쉐어드 =====================================================
        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        useremail = sp.getString("user_email", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        oldpasswd = sp.getString("user_pw", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값


        //============================================================================




        if (Objects.equals(oldpasswd, "카카오 로그인입니다.")) {
            layoutnowpwd.setVisibility(View.GONE);
            pwdchangelayout.setVisibility(View.GONE);
            btnpwdchagne.setVisibility(View.GONE);
            layoutkalogpwdchag.setVisibility(View.VISIBLE);
        }

        logname = findViewById(R.id.logname);
        layoutnowpwd = findViewById(R.id.layoutnowpwd);
        tv_checkpw = findViewById(R.id.tv_checkpw);

        nowid = findViewById(R.id.nowid);
        nowpass = findViewById(R.id.nowpass);
        btn_nowpass = findViewById(R.id.btn_nowpass);

        nowid.setText(useremail);





        setback          .setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
            onBackPressed();
             }
         });




        btn_nowpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S비밀번호확인 = nowpass.getText().toString();

                비밀번호검증();
            }
        });


        btncannotchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 대소문자 구분 숫자 특수문자  조합 9 ~ 12 자리

        newpwd2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("REGI-비번정규식", "1");
            }

            // text_input_layout 써보기 !!!!!
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("REGI-비번정규식", "2");
                String password = newpwd1.getText().toString();
                Log.e("REGI-비번정규식", "3");
                String confirm = newpwd2.getText().toString();
                Log.e("REGI-비번정규식", "4");

                Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$");
                Matcher matcher = pattern.matcher(password);
                Log.e("REGI-비번정규식", "5");

//                System.out.println(password);
//                System.out.println(confirm);
//                System.out.println(password.equals(confirm));
//                System.out.println(matcher.find());


                if (password.equals(confirm) && matcher.find()) {
                    newpwd1.setTextColor(Color.BLUE);
                    Log.e("REGI-비번정규식", "6");
                    newpwd2.setTextColor(Color.BLUE);
                    Log.e("REGI-비번정규식", "7");

                    newpwdcheck.setText("비밀번호가 확인이 되었습니다.");
                    Log.e("REGI-비번정규식", "8");
                    newpwdcheck.setTextColor(Color.BLUE);
                    Log.e("REGI-비번정규식", "8");

                    pasw = true;
                    Log.e("REGI-비번정규식", "9");
                    return;
                } else {
//                    Toast.makeText(getApplicationContext(), "비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
                    newpwd1.setTextColor(Color.RED);
                    Log.e("REGI-비번정규식no", "10");
                    newpwd2.setTextColor(Color.RED);
                    Log.e("REGI-비번정규식no", "11");
                    newpwdcheck.setText("비밀번호의 형태를 확인하세요.");
                    Log.e("REGI-비번정규식no", "12");
                    newpwdcheck.setTextColor(Color.RED);
                    Log.e("REGI-비번정규식no", "13");


                    pasw = false;
                    Log.e("REGI-비번정규식no", "14");
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("REGI-비번정규식no", "15");
            }
        });

        btnpwdchagne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pasw == true) {

                    AlertDialog.Builder mbuilder = new AlertDialog.Builder(v.getContext());
                    mbuilder.setTitle("비밀번호 변경");
                    mbuilder.setMessage("정말 비밀번호를 변경하시겠습니까?  ");
                    mbuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Your Button Click Action Code


                            newpwdd = newpwd1.getText().toString();
                            pwdupdate();
//                            SharedPreferences sp2 = getSharedPreferences("autologin", MODE_PRIVATE);
//                            SharedPreferences.Editor editor2 = sp2.edit();
//                            editor2.putString("autolog", "0"); //값을저장시
//                            editor2.apply();
//
//                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
//                            startActivity(intent);

                            onBackPressed();

                            SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("user_pw", newpwdd); //값을저장시

                            editor.apply();



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


                } else if (pasw == false) {
                    Toast.makeText(getApplicationContext(), "비밀번호 양식을 확인하세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void 비밀번호검증() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url비번확인, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                String 답 = response;

                if (Objects.equals(답, "success")) {

//                    Toast.makeText(getApplicationContext(), "성공!!", Toast.LENGTH_SHORT).show();

                    layoutnowpwd.setVisibility(View.GONE);
                    pwdchangelayout.setVisibility(View.VISIBLE);
                    tv_checkpwre.setVisibility(View.VISIBLE);
                    tv_checkpw.setVisibility(View.GONE);



                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();

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
                params.put("email", useremail);
                params.put("password", S비밀번호확인);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void pwdupdate() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, pwdchangeURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("loginname", loginname);
                params.put("newpwd", newpwdd);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}

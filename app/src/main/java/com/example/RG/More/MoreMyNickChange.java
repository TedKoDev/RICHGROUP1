package com.example.RG.More;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.Home_group.GroupActivity;
import com.example.RG.MainActivity;
import com.example.RG.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MoreMyNickChange extends AppCompatActivity {


    String 중복체크URL = "http://3.38.117.213/newnickvalidate.php"; //
    String nickupdateurl = "http://3.38.117.213/nickupdate.php"; //


    TextView logname, loginnick;
    TextView newnick;
    String newnickname;
    String loginname;

    ImageView Yes, No, setback;

    Button btnnickcheck, btnnickchagne;

    String 사용가능;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_my_nick_change);
        getSupportActionBar().hide();

        logname = findViewById(R.id.logname);//상단 닉
        loginnick = findViewById(R.id.loginnick);//기존닉
        newnick = findViewById(R.id.newnick);//변경닉
        btnnickcheck = findViewById(R.id.btnnickcheck); // 중복확인
        btnnickchagne = findViewById(R.id.btnnickchagne);// 변경하기
        Yes = findViewById(R.id.YES);// 가능
        No = findViewById(R.id.NO);// 불가능
        setback = findViewById(R.id.setback);// 뒤로가기


        //유저네임을 사용하는 쉐어드 =====================================================
        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값
        loginnick.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값

        //============================================================================


        btnnickcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newnickname = newnick.getText().toString();

                중복체크();


            }
        });

        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });
        btnnickchagne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (사용가능 == "OK") {
                    AlertDialog.Builder mbuilder = new AlertDialog.Builder(v.getContext());
                    mbuilder.setTitle("닉네임 변경");
                    mbuilder.setMessage("정말 닉네임을 변경하시겠습니까?");
                    mbuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            Toast.makeText(getApplicationContext(), "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("userName", newnickname); //값을저장시
                            editor.apply();

                            nickupdate();


                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                } else {
                    Toast.makeText(getApplicationContext(), "닉네임 중복확인을 해주십시요.", Toast.LENGTH_SHORT).show();
                    newnick.setBackgroundColor(getResources().getColor(R.color.piered));
                }

            }
        });


    }




    private void nickupdate() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, nickupdateurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {




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
                params.put("oldnick", loginname);
                params.put("newnick", newnickname);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }



    private void 중복체크() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, 중복체크URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");


                    if (success) {

                        사용가능 = "OK";
                        Log.e("response", "사용가능 ");
                        Log.e("response", response);
                        Toast.makeText(getApplicationContext(), "사용가능한 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                        newnick.setEnabled(false);
                        Yes.setVisibility(View.VISIBLE);
                        No.setVisibility(View.GONE);
                        newnick.setBackgroundColor(getResources().getColor(R.color.pieblue));

                    } else {
                        사용가능 = "No";
                        Toast.makeText(getApplicationContext(), "닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show();
                        Log.e("response", response);
                        Log.e("response", "사용불가");
                        No.setVisibility(View.VISIBLE);
                        Yes.setVisibility(View.GONE);
                        newnick.setBackgroundColor(getResources().getColor(R.color.piered));
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("newnickname", newnickname);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}

package com.example.RG.Groupintro;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class GroupSettingActivity extends AppCompatActivity {

    SharedPreferences sp;
    String loginname, grsetnum, grmaker;
    TextView logname, grnum, grsetintro, grsetnameimg, grsetdelete;
    ImageView ivback;

    String urldelete = "http://3.38.117.213/RG3groupdelete.php"; // StringRequest를 통해서 데이터값을 보낼 url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_setting);
        getSupportActionBar().hide();


        logname = findViewById(R.id.logname);
        grnum = findViewById(R.id.grnum);
        ivback = findViewById(R.id.ivback);
        grsetintro = findViewById(R.id.grsetintro);
        grsetnameimg = findViewById(R.id.grsetnameimg);
        grsetdelete = findViewById(R.id.grsetdelete);
        // usernick =================================== 로그인 유저 닉네임 값
        sp = getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값
        //=====================================================
        // =================================== GroupActivity 에서 Bottomsheet Fragment 건너뛰기 위해 사용함  그룹번호 onpause 시 삭제 됨
        sp = getSharedPreferences("gridset", MODE_PRIVATE);
        grsetnum = sp.getString("그룹번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        grnum.setText(grsetnum); // name 란엔 xml파일에서 만들어둔 유저이름id값
        //======================================================
        sp = getSharedPreferences("개설자", MODE_PRIVATE);
        grmaker = sp.getString("개설자", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환

        //======================================================
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 프레그먼트로 이동하는법 알아야함
                Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);

            }
        });


        grsetintro.setOnClickListener(new View.OnClickListener() {  //모임소개 액티비티로 이동
            @Override
            public void onClick(View view) {


                // 값입력_액티비티에서 사용
                Intent intent = new Intent(getApplicationContext(), GroupIntroActivity.class); //액티비티 전환
                // 전달할 값 ( 첫번째 인자 : key, 두번째 인자 : 실제 전달할 값 )

                startActivity(intent);


            }
        });
        grsetnameimg.setOnClickListener(new View.OnClickListener() { //모임이름 및 커버설정 액티비티로 이동
            @Override
            public void onClick(View view) {
                if (loginname.equals(grmaker)) {


                    // 값입력_액티비티에서 사용
                    Intent intent = new Intent(getApplicationContext(), GroupNameImgSetActivity.class); //액티비티 전환
                    // 전달할 값 ( 첫번째 인자 : key, 두번째 인자 : 실제 전달할 값 )

                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        grsetdelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (loginname.equals(grmaker)) {
                    AlertDialog.Builder mbuilder = new AlertDialog.Builder(v.getContext());
                    mbuilder.setTitle("삭제여부");
                    mbuilder.setMessage("정말 그룹을 삭제하시겠습니까?");
                    mbuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Your Button Click Action Code

                            grdelete();
                            // 값입력_액티비티에서 사용
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class); //액티비티 전환
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
                    Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void grdelete() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urldelete, new Response.Listener<String>() {
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
                params.put("gridx", grsetnum);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
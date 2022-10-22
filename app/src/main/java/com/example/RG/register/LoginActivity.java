package com.example.RG.register;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.RG.ChatService;
import com.example.RG.Home_group.GroupActivity;
import com.example.RG.MainActivity;
import com.example.RG.R;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.auth.model.Prompt;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "kakologin";
    CheckBox autologin;
    String alogin;
    private EditText et_id, et_pass;
    private Button kakaologin, btn_login;
    private TextView tvlink_reg, tvlink_findpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        Log.i("생명주기", "onCreate");

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        tvlink_reg = findViewById(R.id.tvlink_register);
        kakaologin = findViewById(R.id.btn_register2);
        tvlink_findpass = findViewById(R.id.tvlink_findpass);

        autologin = findViewById(R.id.autologin);


        tvlink_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvlink_findpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 누르면 수행할 코드 작성

                Intent intent = new Intent(LoginActivity.this, FindPassActivity.class);
                startActivity(intent);
            }
        });

        // 카카오톡 로그인
        kakaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("카로그인", "1");
                if (!autologin.isChecked()) {
                    alogin = "0";


                } else if (autologin.isChecked()) {
                    alogin = "1";
                }


                Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
                    @Override
                    public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                        if (oAuthToken != null) {
                            Log.i("카로그인", "1");
                            Log.i("카카오로그인1", String.valueOf(oAuthToken));

                        }
                        if (throwable != null) {
                            Log.i("카카오로그인2", "4");
                            Log.i("카카오로그인2", String.valueOf(throwable));


                        }
                        updateKakaoLoginUI();
                        return null;
                    }
                };

                List<Prompt> prompts = Arrays.asList(Prompt.LOGIN);


//                Log.i(TAG, "클릭됨");
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    //LoginClient에서 UserApiClient로 변경됨됨
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);

                    Log.i(TAG, "카카오있음");
                    Log.i("카카오톡으로 로그인", "6");

                } else { // 카카오가 설치되어 있지 않을때
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, prompts, callback);
                    Log.i(TAG, "카카오없음");

                    Log.i("카카오로그인 웹 화면 ", "7");
                }


                SharedPreferences sp2 = getSharedPreferences("autologin", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sp2.edit();
                editor2.putString("autolog", alogin); //값을저장시
                editor2.apply();

//                Intent intent = new Intent(LoginActivity.this, kakaologin.class);
//                startActivity(intent);

            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() { // 로그인을 눌렀을 경우
            @Override
            public void onClick(View view) {

                if (!autologin.isChecked()) {
                    alogin = "0";


                } else if (autologin.isChecked()) {
                    alogin = "1";
                }


                // EditText에 현재 입력되어있는 값을 get(가져온다) 해온다.
                String user_email = et_id.getText().toString();
                String user_pw = et_pass.getText().toString();
//                String userName = et_pass.getText().toString();

                SharedPreferences sp2 = getSharedPreferences("autologin", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sp2.edit();
                editor2.putString("autolog", alogin); //값을저장시
                editor2.apply();

                Log.i("autolog?", alogin);

                Response.Listener<String> responseListener = new Response.Listener<String>() {  //JSON 오브젝트를 통해서 요청을 보냄  (운반책 같은 느낌) 포장해서 보내고 받은걸 풀어주고
                    //JSON OBJECT란 json형태의 데이터를 관리해 주는 메소드.
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.e("login 수령값 ", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success"); //불러온 값의 참 거짓을 확인하는 분기점
                            if (success) {
                                String user_email = jsonObject.getString("user_email");
                                String user_pw = jsonObject.getString("user_pw");
                                String userName = jsonObject.getString("userName");

                                Toast.makeText(getApplicationContext(), "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                sessionManager.createSession(user_email,userName);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("userName", userName); //값을저장시
                                editor.putString("user_pw", user_pw); //값을저장시
                                editor.putString("user_email", user_email); //값을저장시
                                editor.apply();


                                // 서비스 시작
                                Intent intent1 = new Intent(getApplicationContext(), ChatService.class);
                                getApplicationContext().startService(intent1);

                                intent.putExtra("user_email", user_email);
                                intent.putExtra("user_pw", user_pw);
                                intent.putExtra("userName", userName);
                                startActivity(intent);

                            } else { //로그인에 실패한경우
                                Toast.makeText(getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                };


                //LoginReqeust 라는 클래스를 만들어두었음  클래스를 불러옴

                // 서버로 volley를 이용해서 요청을 한다.
                LoginRequest loginRequest = new LoginRequest(user_email, user_pw, responseListener); //값들을 담아서 loginrequest라는 클래스를 통해  서버로 보내줌
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });


    }

    private void updateKakaoLoginUI() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {

                if (user != null) {
                    Log.i("1카카오1", "2");
                    Log.i(TAG, "invoke: id=" + user.getId());
                    Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
                    Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());




                    String 카카오닉네임;
                    String 카카오id;
                    String 카카오Email;


                    //닉네임 가져오기
                    카카오닉네임 = (user.getKakaoAccount().getProfile().getNickname());
                    //카카오ID 가져오기
                    카카오id = (String.valueOf(user.getId()));
                    //이메일 가져오기
                    카카오Email = (user.getKakaoAccount().getEmail());

                    // 값입력_액티비티에서 사용


                    Intent intent = new Intent(getApplicationContext(), kakaologin.class);
                    intent.putExtra("카카오닉네임", 카카오닉네임); // 보낼 값 입력
                    intent.putExtra("카카오id", 카카오id); // 보낼 값 입력
                    intent.putExtra("카카오Email", 카카오Email); // 보낼 값 입력
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                    startActivity(intent);


                } else {

//                nickName.setText(null);

                    Log.i("카카오취소", "취소됨 ");

//                Intent intent = new Intent(kakaologin.this, LoginActivity.class);
//                startActivity(intent);


                }
                return null;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("생명주기", "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("생명주기", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i("생명주기", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("생명주기", "onDestroy");
    }
}
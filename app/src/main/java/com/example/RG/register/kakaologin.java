package com.example.RG.register;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.RG.ChatService;
import com.example.RG.MainActivity;
import com.example.RG.R;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.auth.model.Prompt;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class kakaologin extends AppCompatActivity {

    private static final String TAG = "kakologin";
    String 중복체크URL = "http://3.38.117.213/newnickvalidate.php"; //
    String 카카오회원등록 = "http://3.38.117.213/kalogregiseter.php"; //
    String 카카오가입패스 = "http://3.38.117.213/kalogpass2.php"; //
    String 카카오가입여부확인 = "http://3.38.117.213/kalogpass.php"; //
    TextView userkaemail;
    EditText setkanick; // 닉작성칸
    Button appstart;
    String choosnick;
    Button btnkanickcheck;
    ImageView Yes, No, kalogback;
    String 사용가능;
    String SKakaonickName, SKakaoId, SKakaoEmail;
    TextView nickName, kaoid, kaoemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakaologin);
        getSupportActionBar().hide();


        Log.i("카카오로그인", "1");


        nickName = findViewById(R.id.nickname); // 상단 닉 TV
        kaoid = findViewById(R.id.kakaoid);// 상단 KAID TV
        kaoemail = findViewById(R.id.kaoemail);// 상단 KAEMAIL TV

        userkaemail = findViewById(R.id.userkaemail); // 이메일 보여주기 tv
        setkanick = findViewById(R.id.setkanick); // 닉 작성칸 et

        btnkanickcheck = findViewById(R.id.btnkanickcheck); // 중복확인
        appstart = findViewById(R.id.appstart); // 앱 시작 (http 가동 회원정도 등록
        kalogback = findViewById(R.id.kalogback); // 뒤로 버튼 (카로그아웃 )

        Yes = findViewById(R.id.YES);
        No = findViewById(R.id.NO);


// 값받는_액티비티에서 사용
Intent intent = getIntent(); // 이전 액티비티에서 보낸 intent 받기
String 카카오닉네임 = intent.getStringExtra("카카오닉네임");
String 카카오id = intent.getStringExtra("카카오id");
String 카카오Email = intent.getStringExtra("카카오Email");





        //닉네임 가져오기
        nickName.setText(카카오닉네임);
        //카카오ID 가져오기
        kaoid.setText(카카오id);
        //이메일 가져오기
        kaoemail.setText(카카오Email);
        userkaemail.setText(카카오Email);
        setkanick.setText(카카오닉네임);
        SKakaonickName = 카카오닉네임;
        SKakaoId= 카카오id;
        SKakaoEmail = 카카오Email;

        카카오가입여부확인();





        kalogback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) { //invoke가 뭔가
                        Log.i("카카오로그인", "8");
                        updateKakaoLoginUI();

                        return null;
                    }
                });
            }
        });


        btnkanickcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choosnick = setkanick.getText().toString();

                중복체크();


            }
        });


        appstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Objects.equals(사용가능, "OK")) {

                    SKakaonickName = setkanick.getText().toString();
                    SKakaoEmail = userkaemail.getText().toString();
                    Log.i("애1SKakaonickName", SKakaonickName);
                    Log.i("애1SKakaoEmail", SKakaoEmail);


                    카카오인원회원가입();

                    SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("userName", SKakaonickName); //값을저장시
                    editor.putString("user_pw", "카카오 로그인입니다."); //값을저장시
                    editor.putString("user_email", SKakaoEmail); //값을저장시
                    editor.apply();
                    Log.i("카서비스", " 최초 로그인이라 앱 시작 버튼후  ");
                    // 서비스 시작
                    Intent intent1 = new Intent(getApplicationContext(), ChatService.class);
                    getApplicationContext().startService(intent1);




                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                    startActivity(intent);


                } else {
                    Toast.makeText(getApplicationContext(), "닉네임 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }


    private void updateKakaoLoginUI() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) {
                    Log.i("1카카오1", "2");

//                    Log.i(TAG, "invoke: id=" + user.getId());
//                    Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
//                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
//                    Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
//                    Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());
//
//
//                    //닉네임 가져오기
//                    nickName.setText(user.getKakaoAccount().getProfile().getNickname());
//                    //카카오ID 가져오기
//                    kaoid.setText(String.valueOf(user.getId()));
//                    //이메일 가져오기
//                    kaoemail.setText(user.getKakaoAccount().getEmail());
//                    userkaemail.setText(user.getKakaoAccount().getEmail());
//                    setkanick.setText(user.getKakaoAccount().getProfile().getNickname());
//
//
//                    SKakaonickName = user.getKakaoAccount().getProfile().getNickname();
//                    SKakaoId= kaoid.getText().toString();
//                    SKakaoEmail = user.getKakaoAccount().getEmail();

                    Log.i("1카카오1", "3");


                    Log.i("1카카오1", "4");

                } else {

                    nickName.setText(null);



                    Intent intent = new Intent(kakaologin.this, LoginActivity.class);
                    startActivity(intent);


                }
                return null;
            }
        });
    }
    private void 카카오가입여부확인() {
        SKakaoId= kaoid.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, 카카오가입여부확인, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");


                    if (success) {


                        Log.i("성공1가입패스SKakaoId ", SKakaoId);
                        Log.i("성공or실패 ", "처음접속인 카카오계정");
                        Log.i("성공or실패 ", response);
                        Toast.makeText(getApplicationContext(), "처음접속인 카카오계정", Toast.LENGTH_SHORT).show();
                        Log.i("1카카오1가입여부첫 접속", "6");
                        // 서비스 시작
                        Intent intent1 = new Intent(getApplicationContext(), ChatService.class);
                        getApplicationContext().startService(intent1);


                    } else {

                        Log.i("성공2가입패스SKakaoId ", SKakaoId);
                        Log.i("성공or실패 ", "이미 접속한 카카오 계정");
                        Log.i("성공or실패 ", response);
                        Toast.makeText(getApplicationContext(), "이미 접속한 카카오 계정", Toast.LENGTH_SHORT).show();
                        Log.i("1카카오1가입여부 이미있음 ", "7");
                        카카오가입패스();
                        // 서비스 시작
                        Intent intent1 = new Intent(getApplicationContext(), ChatService.class);
                        getApplicationContext().startService(intent1);


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

                params.put("SKakaoId", SKakaoId);
                Log.i("애3SKakaoId", SKakaoId);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void 카카오가입패스() {
        SKakaoId= kaoid.getText().toString();
        Log.i("datatSKakaoId", SKakaoId);
        Log.i("1카카오1가입패스", "8");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, 카카오가입패스, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.i("datat", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.i("datat", "");

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);


                            String user_email = object.getString("user_email");
                            String user_pw = object.getString("user_pw");
                            String userName = object.getString("userName");
                            String created_at = object.getString("created_at");
                            String kakaoid = object.getString("kakaoid");

                            Log.i("카로그값가져오기", user_email);
                            Log.i("카로그값가져오기", user_pw);
                            Log.i("카로그값가져오기", userName);
                            Log.i("카로그값가져오기", created_at);
                            Log.i("카로그값가져오기", kakaoid);
                            Log.i("1카카오1가입패스", "9 값받아옴 ");

                            SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("userName", userName); //값을저장시
                            editor.putString("user_pw", "카카오 로그인입니다."); //값을저장시
                            editor.putString("user_email", user_email); //값을저장시
                            editor.apply();

Log.i("카서비스", "이미 가입되어 있어서 가입패스후 값불러온뒤 ");
                            // 서비스 시작
                            Intent intent1 = new Intent(getApplicationContext(), ChatService.class);
                            getApplicationContext().startService(intent1);









                        }
                        Log.i("1카카오1가입패스", "10 ㅇ메인으로이동 ");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                        startActivity(intent);

                    }
                } catch (Exception e) {
                }

//                    Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();


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


                params.put("SKakaoId", SKakaoId);
                Log.i("애3SKakaoId", SKakaoId);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }



    private void 카카오인원회원가입() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, 카카오회원등록, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Log.i("성공or실패 ", response);

                try {

//                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");


                    Log.i("카서비스", "회원가입후 ");
                    // 서비스 시작
                    Intent intent1 = new Intent(getApplicationContext(), ChatService.class);
                    getApplicationContext().startService(intent1);




                    if (success) {
                        Log.i("성공1 ", response);

                    } else {

                    }

                } catch (Exception e) {
                    Log.e("REGISTER-mail-에러1", "23");
                    e.printStackTrace();
                    Log.e("REGISTER-mail-에러2", "24");
                }


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

                params.put("SKakaonickName", SKakaonickName);
                Log.i("애3SKakaonickName", SKakaonickName);

                params.put("SKakaoId", SKakaoId);
                Log.i("애3SKakaoId", SKakaoId);

                params.put("SKakaoEmail", SKakaoEmail);
                Log.i("애3SKakaoEmail", SKakaoEmail);
                params.put("SKakaoPW", SKakaoId);
                Log.i("SKakaoPW", "NO");

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
                        setkanick.setEnabled(false);
                        Yes.setVisibility(View.VISIBLE);
                        No.setVisibility(View.GONE);
                        setkanick.setBackgroundColor(getResources().getColor(R.color.pieblue));

                    } else {
                        사용가능 = "No";
                        Toast.makeText(getApplicationContext(), "닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show();
                        Log.e("response", response);
                        Log.e("response", "사용불가");
                        No.setVisibility(View.VISIBLE);
                        Yes.setVisibility(View.GONE);
                        setkanick.setBackgroundColor(getResources().getColor(R.color.piered));
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
                params.put("newnickname", choosnick);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}

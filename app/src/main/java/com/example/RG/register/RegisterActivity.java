package com.example.RG.register;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.RG.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;


public class RegisterActivity extends AppCompatActivity {


    static int value;
    //인증코드
    String GmailCode;
    int mailSend = 0;
    MainHandler mainHandler;
    private EditText et_pass1, et_pass2, et_nick;
    private TextView tv_pcheck1, tv_pcheck2;
    private Button btn_Echeck, btn_nick, btn_register, btn_mailvali;
    private boolean emailcheck = false; //이메일인증
    private boolean validate = false; //닉네임 중복값
    private boolean pasw = false; //패스워드 충족값
    private boolean ischceked = false; //체크박스 충족값
    //이메일 입력하는곳
    private EditText et_email;
    //인증번호 입력하는곳
    private TextView tv_echeck;
    //인증번호 확인 버튼
    private Button emailCodeButton;
    //텍스트뷰 (인증값 받는곳 )
    private TextView textView;


    // 이용약관 관련 추가 해야함 .

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        Log.e("REGISTER", "1");


        //이메일 입력하는곳
        et_email = findViewById(R.id.et_email);
        //인증번호 입력하는곳
        tv_echeck = findViewById(R.id.tv_echeck);

        //인증번호 받는 부분은 GONE으로 안보이게 숨긴다
        tv_echeck = findViewById(R.id.tv_echeck);
        tv_echeck.setVisibility(View.GONE);
        //인증번호 입력된값
        textView = findViewById(R.id.textView6);

        emailCodeButton = findViewById(R.id.emailCodeButton);
        emailCodeButton.setVisibility(View.GONE);
        btn_Echeck = findViewById(R.id.btn_Echeck);
        btn_Echeck.setVisibility(View.GONE);
        btn_register = findViewById(R.id.btn_register);

        btn_mailvali = findViewById(R.id.btn_mailvali);


        tv_pcheck1 = findViewById(R.id.tv_pcheck1);


        //Email 중복 체크
        btn_mailvali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Register-mail-중복확인", "1");
                String user_email = et_email.getText().toString();
                Log.e("Register-mail-중복확인", "2");

                if (validate) {
                    Log.e("Register-mail-중복확인", "3");
                    return; //검증 완료
                }
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
                Matcher matcher = pattern.matcher(user_email);

                Log.e("Register-mail-중복확인", "4(mailValidateRequest로 전환 -> mailvali 1 ");

                if (!matcher.find()) { //작동함 0223
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    Log.e("REGISTER-mail-중복확인", "5");
                    builder.setMessage("정확한 이메일을 입력하세요.").setPositiveButton("확인", null).create();
                    Log.e("REGISTER-mail-중복확인", "6");
                    builder.show();
                    Log.e("REGISTER-mail-중복", "7");


                    return;

                }


                //검증시작
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    //------- mailvalidaterequest Class가 작동함  mailvali 1~ 3
                    // and "REGISTER-mail"25~28" + mailvali 4 (getparams - 데이터 받아오는 역할)   작동후


                    //                 아래 시작    Log.e(  "REGISTER-mail-중복확인", "8");
//                    받아온 response값 = (rgmailvalidate.php 파일의 $response의 값 )
                    @Override
                    public void onResponse(String response) {
                        Log.e("서버에서받은 response값", response);
                        Log.e("REGISTER-mail-중복확인", "8");
                        Log.e("REGISTER-mail-중복확인", "9");

                        try {
                            Log.e("REGISTER-mail-중복확인", "10");

//                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            Log.e("변수명", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.e("REGISTER-mail-중복확인", "11");
                            boolean success = jsonResponse.getBoolean("success");
                            Log.e("REGISTER-mail-중복확인", "12");


                            if (success) {
                                Log.e("REGISTER-mail-중복확인", "13");
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                Log.e("REGISTER-mail-중복확인", "14");
                                builder.setMessage("사용할 수 있는 Email 입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                Log.e("REGISTER-mail-중복확인", "15");
                                builder.show();
                                Log.e("REGISTER-mail-중복확인", "16");
                                et_email.setEnabled(false);
                                Log.e("REGISTER-mail-중복확인", "17");
//                                validate = true;
//                                btn_nick.setText("확인완료");
//                                btn_nick.setBackgroundColor(getResources().getColor(R.color.gray));
//                                et_nick.setBackgroundColor(getResources().getColor(R.color.gray));

                                btn_Echeck.setVisibility(View.VISIBLE);
                                Log.e("REGISTER-mail-중복확인", "18");
                                btn_mailvali.setVisibility(View.INVISIBLE);
                                Log.e("REGISTER-mail-중복확인", "19");
                            } else {
                                Log.e("REGISTER-mail-중복됨1", "20");
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("사용중인 Email입니다..")
                                        .setNegativeButton("확인", null)
                                        .create();
                                Log.e("REGISTER-mail-중복됨2", "21");
                                builder.show();
                                Log.e("REGISTER-mail-중복됨3", "22");
                            }

                        } catch (Exception e) {
                            Log.e("REGISTER-mail-에러1", "23");
                            e.printStackTrace();
                            Log.e("REGISTER-mail-에러2", "24");
                        }
                    }
                };//Response.Listener 완료

//------------------------------------------------------------------------------------------------
                //  mailvalidaterequest 객체를 만든뒤에 RequestQueue queue를 통해 서버로 보냄
                mailValidateRequest mailValidateRequest = new mailValidateRequest(user_email, responseListener);
                Log.e("REGISTER-mail", "25 from mailvali 3");
                Log.e("REGISTER-mail", "26");
                Log.e("REGISTER-mail", user_email);

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                Log.e("REGISTER-mail", "27");
                queue.add(mailValidateRequest);
                Log.e("REGISTER-mail", "28 서버로 보냄  ");

            }
        });


//---------------------------------------------------------------------------------------------------------------
        //이메일 인증 하는 부분
        //인증코드 시간초가 흐르는데 이때 인증을 마치지 못하면 인증 코드를 지우게 만든다.
        btn_Echeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REGISTER-메일인증", "1");
                //이메일 인증부분을 보여준다.

                //메일을 보내주는 쓰레드
                MailTread mailTread = new MailTread();
                Log.e("REGISTER-메일인증", "2");
                mailTread.start();
                Log.e("REGISTER-메일인증", "3");

                if (mailSend == 0) {
                    Log.e("REGISTER-메일인증", "4");
                    value = 180;
                    Log.e("REGISTER-메일인증", "5");
                    //쓰레드 객체 생성
                    BackgrounThread backgroundThread = new BackgrounThread();
                    Log.e("REGISTER-메일인증", "6");
                    //쓰레드 스타트
                    backgroundThread.start();
                    Log.e("REGISTER-메일인증", "7");
                    mailSend += 1;
                    Log.e("REGISTER-메일인증", "8");
                } else {
                    value = 180;
                    Log.e("REGISTER-메일인증", "9");
                }


                //이메일이 보내지면 이 부분을 실행시킨다.
                tv_echeck.setVisibility(View.VISIBLE);
                Log.e("REGISTER-메일인증", "10");
                emailCodeButton.setVisibility(View.VISIBLE);
                Log.e("REGISTER-메일인증", "11");

//핸들러 객체 생성
                mainHandler = new MainHandler();
                Log.e("REGISTER-메일인증", "12");

            }
        });

        //인증하는 버튼이다
        //혹시 이거랑 같으면 인증을 성공시켜라라
        emailCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REGISTER-메일인증확인", "1");
                textView.setVisibility(View.INVISIBLE);


                //이메일로 전송한 인증코드와 내가 입력한 인증코드가 같을 때
                if (tv_echeck.getText().toString().equals(GmailCode)) {
                    Log.e("REGISTER-메일인증확인yes", "2");
                    Toast.makeText(getApplicationContext(), "인증번호가 정확합니다.", Toast.LENGTH_SHORT).show();
                    Log.e("REGISTER-메일인증확인yes", "3");
                    tv_echeck.setEnabled(false);
                    Log.e("REGISTER-메일인증확인yes", "4");
                    btn_Echeck.setEnabled(false);
                    Log.e("REGISTER-메일인증확인yes", "5");


                    //emailCodeButton.setVisibility(View.INVISIBLE);
                    tv_echeck.setVisibility(View.INVISIBLE);
                    Log.e("REGISTER-메일인증확인yes", "6");
                    emailCodeButton.setVisibility(View.INVISIBLE);
                    Log.e("REGISTER-메일인증확인yes", "7");
                    et_email.setEnabled(false);
                    Log.e("REGISTER-메일인증확인yes", "8");
                    btn_Echeck.setText("확인완료");
                    Log.e("REGISTER-메일인증확인yes", "9");
                    btn_Echeck.setBackgroundColor(getResources().getColor(R.color.gray));
                    Log.e("REGISTER-메일인증확인yes", "10");

                    //emailCodeButton.setBackgroundColor(getResources().getColor(R.color.gray));
                    emailcheck = true;
                    Log.e("REGISTER-메일인증확인yes", "11");
                    return;
                } else {
                    Log.e("REGISTER-메일인증확인no", "1");
                    Toast.makeText(getApplicationContext(), "인증번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.e("REGISTER", "2");

//        if ( et_email.getText().toString().length() == 0 ) {
//            Toast.makeText(RegisterActivity.this, "Email 입력하세요", Toast.LENGTH_SHORT).show();
//            et_email.requestFocus();
//            return;
//        }
//        if ( et_pass1.getText().toString().length() == 0 ) {
//            Toast.makeText(RegisterActivity.this, "Password를 입력하세요", Toast.LENGTH_SHORT).show();
//            et_pass1.requestFocus();
//            return;
//        }
//
//        if ( et_pass2.getText().toString().length() == 0 ) {
//            Toast.makeText(RegisterActivity.this, "Password Confirm 입력하세요", Toast.LENGTH_SHORT).show();
//            et_pass2.requestFocus();
//            return;
//        }
//        if ( et_nick.getText().toString().length() == 0 ) {
//            Toast.makeText(RegisterActivity.this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
//            et_nick.requestFocus();
//            return;
//        }


        //비밀번호 정규식 적용
        et_pass1 = findViewById(R.id.et_pass1);
        et_pass2 = findViewById(R.id.et_pass2);

        // 대소문자 구분 숫자 특수문자  조합 9 ~ 12 자리

        et_pass2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("REGI-비번정규식", "1");
            }

            // text_input_layout 써보기 !!!!!
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("REGI-비번정규식", "2");
                String password = et_pass1.getText().toString();
                Log.e("REGI-비번정규식", "3");
                String confirm = et_pass2.getText().toString();
                Log.e("REGI-비번정규식", "4");

                Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$");
                Matcher matcher = pattern.matcher(password);
                Log.e("REGI-비번정규식", "5");

//                System.out.println(password);
//                System.out.println(confirm);
//                System.out.println(password.equals(confirm));
//                System.out.println(matcher.find());


                if (password.equals(confirm) && matcher.find()) {
                    et_pass1.setTextColor(Color.BLUE);
                    Log.e("REGI-비번정규식", "6");
                    et_pass2.setTextColor(Color.BLUE);
                    Log.e("REGI-비번정규식", "7");

                    tv_pcheck1.setText("비밀번호가 확인이 되었습니다.");
                    Log.e("REGI-비번정규식", "8");
                    tv_pcheck1.setTextColor(Color.BLUE);
                    Log.e("REGI-비번정규식", "8");

                    pasw = true;
                    Log.e("REGI-비번정규식", "9");
                    return;
                } else {
                    Toast.makeText(RegisterActivity.this, "비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
                    et_pass1.setTextColor(Color.RED);
                    Log.e("REGI-비번정규식no", "10");
                    et_pass2.setTextColor(Color.RED);
                    Log.e("REGI-비번정규식no", "11");
                    tv_pcheck1.setText("비밀번호의 형태를 확인하세요.");
                    Log.e("REGI-비번정규식no", "12");
                    tv_pcheck1.setTextColor(Color.RED);
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


        et_nick = findViewById(R.id.et_nick);


        btn_register = findViewById(R.id.btn_register);


        //닉네임 중복 체크
        btn_nick = findViewById(R.id.btn_nick);
        btn_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("REGI-닉중복", "1");
                String userName = et_nick.getText().toString();

                if (validate) {
                    Log.e("REGI-닉중복", "2");
                    return; //검증 완료
                }

                if (userName.equals("")) { //작동함 0223Log.e(  "REGI-닉중복", "1");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    builder.show();
                    Log.e("REGI-닉중복", "3");
                    return;
                }
                Log.e("rg", "닉네임 입력0 ");


                //검증시작
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("REGI-닉중복", "4");

//                           Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            Log.e("변수명", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.e("REGI-닉중복", "5");
                            boolean success = jsonResponse.getBoolean("success");
                            Log.e("REGI-닉중복", "6");
                            if (success) {
                                Log.e("REGI-닉중복", "7");
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                Log.e("REGI-닉중복", "8");
                                builder.setMessage("사용할 수 있는 닉네임 입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                Log.e("REGI-닉중복", "9");
                                builder.show();
                                Log.e("REGI-닉중복", "10");
                                et_nick.setEnabled(false);
                                Log.e("REGI-닉중복", "11");
                                validate = true;
                                Log.e("REGI-닉중복", "12");
                                btn_nick.setText("확인완료");
                                Log.e("REGI-닉중복", "13");
                                btn_nick.setBackgroundColor(getResources().getColor(R.color.gray));
                                Log.e("REGI-닉중복", "14");
                                et_nick.setBackgroundColor(getResources().getColor(R.color.gray));
                                Log.e("REGI-닉중복", "15");
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                Log.e("REGI-닉중복no", "1");
                                builder.setMessage("사용할 수 없는 닉네임입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                Log.e("REGI-닉중복no", "2");
                                builder.show();
                                Log.e("REGI-닉중복no", "3");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("REGI-닉중복 error", "1");
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                NickValidateRequest nickValidateRequest = new NickValidateRequest(userName, responseListener);
                Log.e("REGI-닉중복확인요청", "1");
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                Log.e("REGI-닉중복확인요청", "2");
                queue.add(nickValidateRequest);
                Log.e("REGI-닉중복확인요청", "3");

            }
        });

        //약관 동의
        //전체동의
        CheckBox checkBox = findViewById(R.id.checkbox);
//필수 서비스이용약관
        CheckBox checkBox2 = findViewById(R.id.checkbox2);
//필수 개인정보
        CheckBox checkBox3 = findViewById(R.id.checkbox3);

        //전체동의 클릭시
        //전체 true / 전체 false 로 변경
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REGI-체크박스", "1");
                if (checkBox.isChecked()) {
                    checkBox2.setChecked(true);
                    Log.e("REGI-체크박스", "2");
                    checkBox3.setChecked(true);
                    Log.e("REGI-체크박스", "3");

                } else {
                    checkBox2.setChecked(false);
                    Log.e("REGI-체크박스", "4");
                    checkBox3.setChecked(false);
                    Log.e("REGI-체크박스", "5");

                }
                Log.e("REGI-체크박스", "6");
            }
        });

        //2 클릭시

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REGI-체크박스", "7");
                //만약 전체 클릭이 true 라면 false로 변경
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    //각 체크박스 체크 여부 확인해서  전체동의 체크박스 변경
                } else if (checkBox2.isChecked() && checkBox3.isChecked()) {
                    checkBox.setChecked(true);
                }
            }
        });
        //3 클릭시

        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REGI-체크박스", "8");
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else if (checkBox2.isChecked() && checkBox3.isChecked()) {
                    checkBox.setChecked(true);
                }
            }
        });

        //이용약관 버튼 - 서비스
        Button btn_agr = findViewById(R.id.btn_agr);
        btn_agr.setText(R.string.underlined_text);
        btn_agr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REGI-체크박스", "9");


                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                //다이얼로그 창의 제목 입력
                builder.setTitle("서비스 이용약관 ");
                //다이얼로그 창의 내용 입력
                builder.setMessage(R.string.app_arp); //이용약관 내용 추가  ,예시는 res-values-string 에 추가해서 사용
                //다이얼로그창에 취소 버튼 추가
                builder.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println(TAG + "이용약관 닫기");
                            }
                        });
                //다이얼로그 보여주기
                builder.show();
            }
        });

//이용약관 버튼2 - 위치정보
        Button btn_agr2 = findViewById(R.id.btn_agr2);
        btn_agr2.setText(R.string.underlined_text);
        btn_agr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REGI-체크박스", "10");
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                //다이얼로그 창의 제목 입력
                builder.setTitle("개인정보 취급방침");
                //다이얼로그 창의 내용 입력
                builder.setMessage(R.string.app_arp2); //이용약관 내용 추가 , 예시는 res-values-string 에 추가해서 사용
                //다이얼로그창에 취소 버튼 추가
                builder.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println(TAG + "이용약관 닫기");
                            }
                        });
                //다이얼로그 보여주기
                builder.show();
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Log.e("REGISTER", "3");
                // EditText에 현재 입력되어있는 값을 get(가져온다) 해온다.
                String user_email = et_email.getText().toString();
                Log.e("REGISTER", "4");
                String user_pw = et_pass1.getText().toString();
                Log.e("REGISTER", "5");
                String userName = et_nick.getText().toString();
                Log.e("REGISTER", "6");
//                int userAge = Integer.parseInt(et_age.getText().toString()); //Integer.parseInt 문자열을 숫자로 변환하기
                //              userAge에 넣어준다. <--             Integer.parseInt를 이용해서 문자열을 숫자로 변환한 뒤 <--et_age에 . 입력된 텍스트를 가져와서 .문자열로 변환한뒤

                if (emailcheck == false) {
                    Toast.makeText(getApplicationContext(), "이메일 인증을 해주세요.", Toast.LENGTH_SHORT).show();

                } else if (pasw == false) {
                    Toast.makeText(getApplicationContext(), "패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();

                } else if (validate == false) {
                    Toast.makeText(getApplicationContext(), "닉네임 중복확인을 확인해주세요.", Toast.LENGTH_SHORT).show();

                } else if (!ischecked(checkBox, checkBox2, checkBox3)) {
                    Toast.makeText(getApplicationContext(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();

                } else {


                    //volley 구문
                    Response.Listener<String> responseListener = new Response.Listener<String>() {  //com.android.volley Interface Response.Listener<T>
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                boolean success = jsonObject.getBoolean("success");
                                Log.e("rg", "회원가입 입력 ");
                                Log.e("REGISTER", "11");

                                if (success) {
                                    Log.e("REGISTER", "12");
                                    Toast.makeText(getApplicationContext(), "회원 등록에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                    Log.e("rg1", "회원가입 돌아가나?  ");

                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);


                                } else { //회원등록에 실패한경우
                                    Toast.makeText(getApplicationContext(), "회원 등록에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                                    Log.e("REGISTER1", response);
                                    Log.e("REGISTER", "12");
                                    Log.e("rg2", "안돌아감 ");
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("REGISTER-ERROR", "1");
                            }

                        }
                    };


                    //  REGISTERREQUEST라는 객체를 만든뒤에 RequestQueue queue를 통해 서버로 보냄
                    RegisterRequest registerRequest = new RegisterRequest(user_email, user_pw, userName, responseListener);
                    Log.e("REGISTER-R.request ", "1");
                    Log.e("REGISTER-R.request ", user_email);
                    Log.e("REGISTER-R.request", userName);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    Log.e("REGISTER-R.request ", "2");
                    queue.add(registerRequest);
                    Log.e("REGISTER-R.request ", "3");


                }
            }
        });

    }

    //이용약관
    private boolean ischecked(CheckBox checkBox, CheckBox checkBox2, CheckBox checkBox3) {

        if (checkBox.isChecked() || checkBox2.isChecked() && checkBox3.isChecked()) {
            return true;
        }
        return false;
    }

    //메일 보내는 쓰레드
    class MailTread extends Thread {

        public void run() {
            GMailSender gMailSender = new GMailSender("apswtrare@gmail.com", "wnfk nkxf vvun zyrj");
            //GMailSender.sendMail(제목, 본문내용, 받는사람);


            //인증코드
            GmailCode = gMailSender.getEmailCode();
            try {
                gMailSender.sendMail("RichGroup 회원가입 이메일 인증", GmailCode, et_email.getText().toString());
            } catch (SendFailedException e) {

            } catch (MessagingException e) {
                System.out.println("인터넷 문제" + e);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //시간초가 카운트 되는 쓰레드
    class BackgrounThread extends Thread {
        //180초는 3분
        //메인 쓰레드에 value를 전달하여 시간초가 카운트다운 되게 한다.

        public void run() {
            //180초 보다 밸류값이 작거나 같으면 계속 실행시켜라
            while (true) {
                value -= 1;
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }

                Message message = mainHandler.obtainMessage();
                //메세지는 번들의 객체 담아서 메인 핸들러에 전달한다.
                Bundle bundle = new Bundle();
                bundle.putInt("value", value);
                message.setData(bundle);

                //핸들러에 메세지 객체 보내기기

                mainHandler.sendMessage(message);

                if (value <= 0) {
                    GmailCode = "";
                    break;
                }
            }


        }
    }

    //쓰레드로부터 메시지를 받아 처리하는 핸들러
    //메인에서 생성된 핸들러만이 Ui를 컨트롤 할 수 있다.
    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int min, sec;

            Bundle bundle = message.getData();
            int value = bundle.getInt("value");

            min = value / 60;
            sec = value % 60;
            //초가 10보다 작으면 앞에 0이 더 붙어서 나오도록한다.
            if (sec < 10) {
                //텍스트뷰에 시간초가 카운팅
                tv_echeck.setHint("0" + min + " : 0" + sec);
            } else {
                tv_echeck.setHint("0" + min + " : " + sec);
            }
        }
    }

//회원가입시 체크박스 확인할때 사용하세요.
//                    //체크박스 확인
//                    else if(!ischecked(checkBox,checkBox2,checkBox3)){
//                        Toast.makeText(getApplicationContext(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
//                    }

}
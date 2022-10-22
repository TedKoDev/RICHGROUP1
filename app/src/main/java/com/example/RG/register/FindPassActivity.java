package com.example.RG.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.RG.R;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class FindPassActivity extends AppCompatActivity {


    String temppwdurl = "http://3.38.117.213/temppwdupdate.php"; //

    GMailSender gMailSender;


    String GmailCode;
    String text;

    EditText findpwdemail;
    Button btngetrepairpwd;
    ImageView fidnpwdback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        getSupportActionBar().hide();


        findpwdemail = findViewById(R.id.findpwdemail);
        fidnpwdback = findViewById(R.id.fidnpwdback);
        btngetrepairpwd = findViewById(R.id.btngetrepairpwd);


        //이메일 인증 하는 부분
        //인증코드 시간초가 흐르는데 이때 인증을 마치지 못하면 인증 코드를 지우게 만든다.
        btngetrepairpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                text ="해당 번호는 임시 비밀번호이오니 로그인후 즉시 비밀번호를 변경하기 바랍니다."+"\n"
//                        +"["+findpwdemail.getText().toString()+"]";
                Log.e("REGISTER-메일인증", "1");
                //이메일 인증부분을 보여준다.


                gMailSender = new GMailSender("apswtrare@gmail.com", "wnfk nkxf vvun zyrj");
                //GMailSender.sendMail(제목, 본문내용, 받는사람);

                //인증코드
                GmailCode = gMailSender.getEmailCode();

                //메일을 보내주는 쓰레드
                MailTread mailTread = new MailTread();
                Log.e("REGISTER-메일인증", "2");
                mailTread.start();
                Log.e("REGISTER-메일인증", "3");

                tempPWDupload();

                AlertDialog.Builder mbuilder = new AlertDialog.Builder(v.getContext());
                mbuilder.setTitle("임시 비밀번호가 발급되었습니다.");
                mbuilder.setMessage("이메일을 확인해 주시기바랍니다.\n로그인후 비밀번호를 변경해 주시기 바랍니다.");
                mbuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Your Button Click Action Code


                        // 값입력_액티비티에서 사용
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class); //액티비티 전환
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                        startActivity(intent);
                    }
                });
                mbuilder.setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Your Button Click Action Code
                        dialog.dismiss();

                    }
                });
                mbuilder.show();


            }
        });


    }

    private void tempPWDupload() {

        String Email = findpwdemail.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, temppwdurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();


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
                params.put("Email", Email);
                params.put("temppwd", GmailCode);
                Log.i("tttEmail", Email);
                Log.i("ttttemppwd", GmailCode);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void dialog(View v) {

    }

    //메일 보내는 쓰레드
    class MailTread extends Thread {

        public void run() {

            text = "해당 번호는 임시 비밀번호이오니 로그인후 즉시 비밀번호를 변경하기 바랍니다." + "\n"
                    + "[ " + GmailCode + " ]";

            try {
//                gMailSender.sendMail("RichGroup 임시 비밀번호 발행", GmailCode, findpwdemail.getText().toString());
                gMailSender.sendMail("RichGroup 임시 비밀번호 발행", text, findpwdemail.getText().toString());
            } catch (SendFailedException e) {

            } catch (MessagingException e) {
                System.out.println("인터넷 문제" + e);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
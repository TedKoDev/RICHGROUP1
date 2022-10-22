package com.example.RG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {


TextView name, email;
Button btn_logout;

SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//
//        sessionManager =new SessionManager(this);
//        sessionManager.checkLogin();


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        btn_logout = findViewById(R.id.btn_logout);



        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);

        String loginname = sp.getString("userName"," "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        name.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값


//        HashMap<String,String>  user = sessionManager.getUserDetail();
//        String mName = user.get(sessionManager.USERNAME);
//        String mEmail = user.get(sessionManager.EMAIL);



//        email.setText(mEmail);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });


    }
}
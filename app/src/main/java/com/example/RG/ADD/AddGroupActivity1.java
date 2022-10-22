package com.example.RG.ADD;
import android.widget.Toast;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.RG.MainActivity;
import com.example.RG.R;

public class AddGroupActivity1 extends AppCompatActivity {


    Button addkeywordcoin, addkeywordland, addkeywordstock, addkeywordmoney, addkeywordpaper,
            addkeywordtech, addkeywordpstudy, addkeywordwhat,add1next;
    ImageView ivback1;


    String loginname,whatET;
    EditText addkeywordwhatET;
    TextView logname, grnum, grtypetv, grtypeimgbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group1);
        getSupportActionBar().hide();


//       버튼 설정   ==========================================================

        addkeywordcoin = findViewById(R.id.addkeywordcoin);
        addkeywordland = findViewById(R.id.addkeywordland);
        addkeywordstock = findViewById(R.id.addkeywordstock);
        addkeywordmoney = findViewById(R.id.addkeywordmoney);
        addkeywordpaper = findViewById(R.id.addkeywordpaper);
        addkeywordtech = findViewById(R.id.addkeywordtech);
        addkeywordpstudy = findViewById(R.id.addkeywordpstudy);
        addkeywordwhat = findViewById(R.id.addkeywordwhat);
        ivback1 = findViewById(R.id.ivback1);
        add1next =findViewById(R.id.add1next);
//        ==========================================================

//        모임 공개타입 설정
        grtypetv = findViewById(R.id.grtypetv); //타입 설정완료시 공개 또는 비공개로 변화함
        grtypeimgbtn = findViewById(R.id.grtypeimgbtn); //클릭리스너 추가로 bottomsheet를 이용해서 공개 비공개 결정할것
//        ======================


        addkeywordwhatET = findViewById(R.id.addkeywordwhatET); // 직접입력하는 EditText
        logname = findViewById(R.id.logname);
        grnum = findViewById(R.id.grnum);

        // =================================== 닉네임 값
        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값
//=====================


        ivback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 프레그먼트로 이동하는법 알아야함
                Intent intent = new Intent(AddGroupActivity1.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);

            }
        });
        whatET = addkeywordwhatET.getText().toString();    // xml edittext 에서 입력 받은 이름

        addkeywordcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addkeywordwhatET.setVisibility(View.VISIBLE);
                int myColor = ContextCompat.getColor(getApplicationContext(),R.color.black);
                addkeywordwhatET.setTextColor(myColor);
                addkeywordwhatET.setText("코인");
            }
        });

        addkeywordland.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addkeywordwhatET.setVisibility(View.VISIBLE);
                int myColor = ContextCompat.getColor(getApplicationContext(),R.color.black);
                addkeywordwhatET.setTextColor(myColor);
                addkeywordwhatET.setText("부동산");

            }
        });

        addkeywordstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addkeywordwhatET.setVisibility(View.VISIBLE);
                int myColor = ContextCompat.getColor(getApplicationContext(),R.color.black);
                addkeywordwhatET.setTextColor(myColor);
                addkeywordwhatET.setText("주식");

            }
        });
        addkeywordmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addkeywordwhatET.setVisibility(View.VISIBLE);
                addkeywordwhatET.setText("외화");
            }
        });
        addkeywordpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addkeywordwhatET.setVisibility(View.VISIBLE);
                int myColor = ContextCompat.getColor(getApplicationContext(),R.color.black);
                addkeywordwhatET.setTextColor(myColor);
                addkeywordwhatET.setText("채권");
            }
        });
        addkeywordtech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addkeywordwhatET.setVisibility(View.VISIBLE);
                int myColor = ContextCompat.getColor(getApplicationContext(),R.color.black);
                addkeywordwhatET.setTextColor(myColor);
                addkeywordwhatET.setText("재테크");
            }
        });
        addkeywordpstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addkeywordwhatET.setVisibility(View.VISIBLE);
                int myColor = ContextCompat.getColor(getApplicationContext(),R.color.black);
                addkeywordwhatET.setTextColor(myColor);
                addkeywordwhatET.setText("스터디");

            }
        });




        addkeywordwhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addkeywordwhatET.setVisibility(View.VISIBLE);
                addkeywordwhatET.setText("직접 입력해 주세요.");
             int myColor = ContextCompat.getColor(getApplicationContext(),R.color.gray);
                addkeywordwhatET.setTextColor(myColor);


            }
        });







        add1next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(  "as변수명", whatET);
                whatET = addkeywordwhatET.getText().toString();    // xml edittext 에서 입력 받은 이름
                // 값입력_액티비티에서 사용
                Intent intent = new Intent(getApplicationContext(), AddGroupActivity2.class); //액티비티 전환
                // 전달할 값 ( 첫번째 인자 : key, 두번째 인자 : 실제 전달할 값 )
                intent.putExtra("카테고리", whatET);
                Log.e(  "asfd변수명", whatET);

//                Toast.makeText(getApplicationContext(), whatET, Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });






    }

}
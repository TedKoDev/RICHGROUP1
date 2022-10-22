package com.example.RG.mainfragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.R;

import java.util.HashMap;
import java.util.Map;

public class InvestAdd extends AppCompatActivity {

    String url = "http://3.38.117.213/investaddd.php"; // StringRequest를 통해서 데이터값을 보낼 url
    TextView investkind, logname;
    ImageView invaddback;
    Spinner investspinner;
    EditText ivetname, invetcount, invetbuy, invetnow;
    Button invaddbtn;
    String loginname;

    LinearLayout noshowlist;
    TextView buycost, avgcost, plmi, per;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_add);
        getSupportActionBar().hide();


        logname = findViewById(R.id.inaddlogname); // 사용자이름
        invaddback = findViewById(R.id.invaddback); // 뒤로가기 버튼


        //유저네임을 사용하는 쉐어드 =====================================================
        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값
        //============================================================================

        investkind = findViewById(R.id.investkind); // 분류


        investspinner = (Spinner) findViewById(R.id.investspinner); // 스피너


        ivetname = findViewById(R.id.ivetname);//품명
        invetcount = findViewById(R.id.invetcount);//수량


        invetbuy = findViewById(R.id.invetbuy);//매수가

        invetnow = findViewById(R.id.invetnow);//현재가

        invaddbtn = findViewById(R.id.invaddbtn);//버튼

//        /============안보여도 되는 레이아웃
        noshowlist = findViewById(R.id.noshowlist); //안봐도되는 리스트 모음 레이아웃


        spinner(); //스피너

        invaddback          .setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
            onBackPressed();
             }
         });



        invaddbtn.setOnClickListener(new View.OnClickListener() { // 이미지 + 텍스트 업로드  버튼
            @Override
            public void onClick(View view) {

                String sinvestkind = investkind.getText().toString();
                Log.i("투자sinvestkind", sinvestkind);
                String sivetname = ivetname.getText().toString();
                Log.i("투자sivetname", sivetname);

                String sinvetcount = invetcount.getText().toString();
                Log.i("투자sinvetcount", sinvetcount);
                String sinvetbuy = invetbuy.getText().toString();
                Log.i("투자sinvetbuy", sinvetbuy);
                String sinvetnow = invetnow.getText().toString();
                Log.i("투자sinvetnow", sinvetnow);

                int 수량 = Integer.parseInt(sinvetcount);
                Log.i("투자추가", String.valueOf(수량));
                int 매수가 = Integer.parseInt(sinvetbuy);
                Log.i("투자매수가", String.valueOf(매수가));
                int 현재가 = Integer.parseInt(sinvetnow);
                Log.i("투자현재가", String.valueOf(현재가));
                int 평가금액 = 현재가 * 수량;
                Log.i("투자평가금액", String.valueOf(평가금액));
                int 매수금액 = 매수가 * 수량;
                Log.i("투자매수금액", String.valueOf(매수금액));
                double 수익률 = (double) (현재가-매수가)/매수가 * 100;


                String 수익률C = String.format("%.2f", 수익률);
                Log.i("투자수익률", String.valueOf(수익률C));
                int 평가손익 = 평가금액 - 매수금액;
                Log.i("투자평가손익", String.valueOf(평가손익));


                String 분류S = sinvestkind;
                String 품명S = sivetname;
                String 수량S = String.valueOf(수량);
                String 매수가S = String.valueOf(매수가);
                String 현재가S = String.valueOf(현재가);
                String 평가금액S = String.valueOf(평가금액);
                String 수익률S = String.valueOf(수익률C);
                String 매수금액S = String.valueOf(매수금액);
                String 평가손익S = String.valueOf(평가손익);

//
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        onBackPressed();


//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {


                        Map<String, String> param = new HashMap<String, String>();
                        //맵핑을 통해서 이미지, 이름, 내용, 작성자(shared로 받은값)을 보내줌
                        param.put("invuser", loginname);  //encodeImagae는  String 변수로 지정해둠  아래에서 값을 넣음
                        param.put("invkind", 분류S);  //encodeImagae는  String 변수로 지정해둠  아래에서 값을 넣음
                        param.put("invname", 품명S);          //gr이름
                        param.put("invavg", 평가손익S);           // 그룹 설명
                        param.put("invgetper", 수익률S); // 작성자 이름
                        param.put("invcount", 수량S);    // 카테고리
                        param.put("invbuyavg", 매수가S);  //공개 비공개
                        param.put("invnowprice", 평가금액S);  //공개 비공개
                        param.put("invbuyprice", 매수금액S);  //공개 비공개


                        return param;
                    }
                };
                // requestQueue를 통해서 전달 하면  php문을 통해 DB table에 저장된다.
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(request);


            }


        });


    }


    private void spinner() {
        final String[] investarray = getResources().getStringArray(R.array.my_array);

        ArrayAdapter<CharSequence> menuAdapter = ArrayAdapter.createFromResource(this, R.array.my_array, android.R.layout.simple_spinner_item);

        // Spinner 클릭시 DropDown 모양을 설정
        menuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 스피너에 어댑터를 연결
        investspinner.setAdapter(menuAdapter);


        investspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                investkind.setText(investarray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

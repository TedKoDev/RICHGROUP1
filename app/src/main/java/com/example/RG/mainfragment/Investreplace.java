package com.example.RG.mainfragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Investreplace extends AppCompatActivity {


    String urlinvestdelete = "http://3.38.117.213/investdelete.php"; //
    String url = "http://3.38.117.213/invcall.php";
    String urlupdate = "http://3.38.117.213/investupdate.php";
    TextView logname, investkindr;
    String loginname;
    ImageView invaddback;
    Spinner investspinner;
    EditText ivetname, invetcount, invetbuy, invetnow;
    Button invreplacebtn, invremover;
    String sinvidx;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investreplace);
        getSupportActionBar().hide();

        logname = findViewById(R.id.inaddlognamer); // 사용자이름
        invaddback = findViewById(R.id.invaddbackr); // 뒤로가기 버튼
        loginname = logname.getText().toString();
        investkindr = findViewById(R.id.investkindr);

        investspinner = findViewById(R.id.investspinnerr); // 스피너
        ivetname = findViewById(R.id.ivetnamer);//품명
        invetcount = findViewById(R.id.invetcountr);//수량
        invetbuy = findViewById(R.id.invetbuyrr);//매수가
        invetnow = findViewById(R.id.invetnowr);//현재가
        invreplacebtn = findViewById(R.id.invaddbtnrr);//수정완료버튼
        invremover = findViewById(R.id.invremover);//삭제버튼


        // 값받는_액티비티에서 사용
        Intent intent = getIntent(); // 이전 액티비티에서 보낸 intent 받기
        sinvidx = intent.getStringExtra("invidx");

        Log.i("inreplace", sinvidx);

        spinner(); //스피너


        invaddback.setOnClickListener(new View.OnClickListener() { //뒤로가기
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });

        invremover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mbuilder = new AlertDialog.Builder(v.getContext());
                mbuilder.setTitle("삭제여부");
                mbuilder.setMessage("정말 투자내역을 삭제하시겠습니까?");
                mbuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Your Button Click Action Code

                        investdelete();
                        onBackPressed();

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


            }
        });

        invreplacebtn.setOnClickListener(new View.OnClickListener() { // 이미지 + 텍스트 업로드  버튼
            @Override
            public void onClick(View view) {

                String sinvestkind = investkindr.getText().toString();
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
                StringRequest request = new StringRequest(Request.Method.POST, urlupdate, new Response.Listener<String>() {
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
                        param.put("invuser", loginname);
                        param.put("invidx", sinvidx);
                        Log.i("rr1",sinvidx );
                        param.put("invkind", 분류S);  //
                        Log.i("rr2",분류S );
                        param.put("invname", 품명S);
                        Log.i("rr3",품명S );
                        param.put("invavg", 평가손익S);
                        Log.i("rr4",평가손익S );
                        param.put("invgetper", 수익률S);
                        Log.i("rr5",수익률S );
                        param.put("invcount", 수량S);
                        Log.i("rr6",수량S );
                        param.put("invbuyavg", 매수가S);
                        Log.i("rr7",매수가S );
                        param.put("invnowprice", 평가금액S);
                        Log.i("rr8",평가금액S );
                        param.put("invbuyprice", 매수금액S);
                        Log.i("rr9",매수금액S );


                        return param;
                    }
                };
                // requestQueue를 통해서 전달 하면  php문을 통해 DB table에 저장된다.
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(request);


            }


        });


        loadData();

//

    }


    private void loadData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success"); //불러온 값의 참 거짓을 확인하는 분기점
                    if (success) {

                        String sinvestkindr = jsonObject.getString("investkindr");
                        String sivetnamer = jsonObject.getString("ivetnamer");
                        String sinvetcountr = jsonObject.getString("invetcountr");
                        int iiinvetcountr = Integer.parseInt(sinvetcountr);
                        String sinvetbuyrr = jsonObject.getString("invetbuyrr");
                        String ssinvetnowr = jsonObject.getString("invetnowr");
                        int iinvetnowr = Integer.parseInt(ssinvetnowr);
                        int iinhalf = iinvetnowr / iiinvetcountr;
                        String sinvetnowr = String.valueOf(iinhalf);

                        investkindr.setText(sinvestkindr);
                        ivetname.setText(sivetnamer);

                        invetcount.setText(sinvetcountr);
                        invetbuy.setText(sinvetbuyrr);
                        invetnow.setText(sinvetnowr);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();


//                    Toast.makeText(getApplicationContext(), "에러 : " + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("sinvidx", sinvidx);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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
                investkindr.setText(investarray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updatelist() {


        StringRequest request = new StringRequest(Request.Method.POST, urlupdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), "수정완료", Toast.LENGTH_SHORT).show();

                onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String 분류 = investkindr.getText().toString(); // invkind
                String 품명 = ivetname.getText().toString(); // invname
                String 수량 = invetcount.getText().toString();//
                String 매수가 = invetbuy.getText().toString();//
                String 현재가 = invetnow.getText().toString();//

                Map<String, String> param = new HashMap<String, String>();
                //맵핑을 통해서 이미지, 이름, 내용, 작성자(shared로 받은값)을 보내줌
                param.put("invidx", sinvidx);
                param.put("investkindr", 분류);  //encodeImagae는  String 변수로 지정해둠  아래에서 값을 넣음
                param.put("ivetname", 품명);
                param.put("invetcount", 수량);
                param.put("invetbuy", 매수가);
                param.put("invetnow", 현재가);


                return param;
            }
        };
        // requestQueue를 통해서 전달 하면  php문을 통해 DB table에 저장된다.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }


    private void investdelete() {

        Log.i("삭제 ", "작동 ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlinvestdelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Log.i("삭제invidx", "3");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("삭제invidx", "2");
//                Toast.makeText(getApplicationContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("에러", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.i("삭제invidx", "1");
                Log.i("삭제invidx", sinvidx);
                params.put("invidx", sinvidx);
                Log.e("invidx", sinvidx);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}

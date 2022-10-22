package com.example.RG.LiveStreaming;
import android.content.SharedPreferences;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LiveActivity1 extends AppCompatActivity {

    EditText et_livename;    // 방송제목
    ImageView iv_thumnail;   // 방송 썸네일
    Button btn_startlive;        // 방송 시작 버튼
    String channelNameString;

    String roomname;
    String token;

    int channelProfile;

    String Roomnumber;
    String Host;
    String onoff;
    String pwd;
    Uri selecteduri;
    Bitmap bitmap;
    String imname;
    String gname;
    String encodeImagae;// encode 이미지  encode가 뭔지 공부해보기

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live1);
        getSupportActionBar().hide();
        // 값받는_액티비티에서 사용
        Intent intent = getIntent(); // 이전 액티비티에서 보낸 intent 받기
        Roomnumber = intent.getStringExtra("방번");
        Host = intent.getStringExtra("사용자");
        gname = intent.getStringExtra("그룹명");
        onoff = intent.getStringExtra("onoff");
        pwd = intent.getStringExtra("pwd");

        Log.i("라이브ff", Roomnumber);
        Log.i("라이브fff", Host);
        Log.i("라이브ffff", onoff);
        Log.i("라이브fffff", pwd);
        Log.i("라이브ffffff", gname);

        et_livename = findViewById(R.id.et_livename);// 방송제목
        iv_thumnail = findViewById(R.id.iv_thumnail);// 방송 썸네일
        btn_startlive = findViewById(R.id.startlive);// 방송 시작 버튼


        iv_thumnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 0);
            }
        });

        btn_startlive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int randomNum = (int) (Math.random() * 100000000);

                imname = randomNum + "_" + System.currentTimeMillis() + ".jpeg";
                roomname = et_livename.getText().toString();


                SharedPreferences sp = getSharedPreferences("live", MODE_PRIVATE);
                 SharedPreferences.Editor  editor = sp.edit();  // 해당 정보는 UploadActivity에서 사용된다.

                editor.putString("그룹번호",Roomnumber); //그룹번호
                editor.putString("썸네일이미지",imname); //썸네일이미지
                editor.putString("방제목",roomname); //방제목
                editor.putString("그룹명",gname); //방제목
                editor.putString("작성자",Host); //작성자
                editor.putString("onoff",onoff); //공개비공개
                editor.putString("pwd",pwd); //비번

                 editor.apply();



                getdata();


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 1 || requestCode == RESULT_OK || data.getData() != null) {


                selecteduri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selecteduri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    iv_thumnail.setImageBitmap(bitmap);
                    imageStore(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imagebyte = stream.toByteArray();

        encodeImagae = android.util.Base64.encodeToString(imagebyte, Base64.DEFAULT);


    }

    public void getdata() {

        String url = "http://3.38.117.213/Generater.php"; //
        Log.i("HTTP채널명", "1");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("HTTP채널명", "3");
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Log.i("HTTPresponse", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    channelNameString = jsonObject.getString("channelNameString");
                    token = jsonObject.getString("token");

                    Log.i("http채널명", channelNameString);
                    Log.i("http토큰", token);

                    inserdb();


//
                } catch (JSONException e) {
                    e.printStackTrace();


//                    Toast.makeText(getApplicationContext(), "에러 : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("HTTP채널명", "2");
//                Toast.makeText(getApplicationContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void inserdb() {
        String inserurl = "http://3.38.117.213/tokeninsert2.php"; //
        StringRequest stringRequest = new StringRequest(Request.Method.POST, inserurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LiveActivity2.class);

                intent.putExtra("채널명", channelNameString);
                intent.putExtra("토큰", token);
                intent.putExtra("방장or청자", 1);

                startActivity(intent); //VideoActivity로 채널명과  방장or청자여부를 전달한다.
                Log.i("HTTP토큰", token);
                Log.i("HTTP채널명", channelNameString);
                Log.i("HTTP방장or청자", String.valueOf(channelProfile));
                Log.i("AGㅎMsubmit", "onSubmit");


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


                params.put("ch", roomname);
                params.put("chn", channelNameString);
                params.put("to", token);
                params.put("rn", Roomnumber);
                params.put("us", Host);
                params.put("im", imname);
                params.put("th", encodeImagae);
                params.put("onoff", onoff);
                params.put("pwd", pwd);
                params.put("gname", gname);


                Log.i("라이브방제", roomname);
                Log.i("라이브아고라용방제", channelNameString);
                Log.i("라이이미지", encodeImagae);
                Log.i("라이브방번", Roomnumber);
                Log.i("라이브사용자", Host);
                Log.i("라이브토큰", token);
                Log.i("라이브이미지이름", imname);
                Log.i("라이브onoff", onoff);
                Log.i("라이브pwd", pwd);
                Log.i("라이브gname", gname);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
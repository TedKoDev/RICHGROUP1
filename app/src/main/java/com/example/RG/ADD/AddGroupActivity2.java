package com.example.RG.ADD;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.RG.CustomDialog;
import com.example.RG.MainActivity;
import com.example.RG.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddGroupActivity2 extends AppCompatActivity implements View.OnClickListener {


    Uri selecteduri;
    Button buttonpick, buttonupload, addkeywordcoin, addkeywordland, addkeywordstock, addkeywordmoney, addkeywordpaper, addkeywordwhat;
    ImageView imageView, ivback;
    ProgressBar progressBar;
    Bitmap bitmap;
    String url = "http://3.38.117.213/RG3groupimage.php"; // StringRequest를 통해서 데이터값을 보낼 url
    String urlgetidx = "http://3.38.117.213/RG3groupimage2.php"; // StringRequest를 통해서 데이터값을 보낼 url
    String urlchatroom2 = "http://3.38.117.213/chatroominsert2.php"; // 개설직후 Chatroom DB 값 insert
    String urlgrjoin = "http://3.38.117.213/grjoin.php";

    String encodeImagae;// encode 이미지  encode가 뭔지 공부해보기

    String gridx, loginname, userName, onoff, catrgory;
    EditText nameet, infoet;
    TextView logname, grnum, grtypetv, grtypeimgbtn, cate, onofftv;
    LinearLayout linearLayouton, linearLayoutoff, linearLayoutonoff;
    SharedPreferences sp;


    LinearLayout grouppwd;
    EditText grouppwd2;
    String 그룹비번;
    String A;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group2);
        getSupportActionBar().hide();

        grouppwd = findViewById(R.id.grouppwd);
        grouppwd2 = findViewById(R.id.grouppwd2);

//       버튼 설정   ==========================================================
        buttonupload = findViewById(R.id.uploadimg);
        ivback = findViewById(R.id.ivback);
//        ==========================================================

//        모임 공개타입 설정
        grtypetv = findViewById(R.id.grtypetv); //타입 설정완료시 공개 또는 비공개로 변화함
        grtypeimgbtn = findViewById(R.id.grtypeimgbtn); //클릭리스너 추가로 bottomsheet를 이용해서 공개 비공개 결정할것
//        ======================


        progressBar = findViewById(R.id.pb_img);
        imageView = findViewById(R.id.iv);
        nameet = findViewById(R.id.imgname);
        infoet = findViewById(R.id.imginfo);

        logname = findViewById(R.id.logname);
        grnum = findViewById(R.id.grnum);
        cate = findViewById(R.id.cate);
        onofftv = findViewById(R.id.grtypetv);


        // usernick =================================== 닉네임 값
        sp = getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값
//=====================


        grtypeimgbtn.setOnClickListener(this); // 공개 비공개 customdialog 클릭 이벤트
//        onoff= grtypetv.getText().toString();
//     ===============================================
        // ADD1에서 카테고리 값을 받아옴 값받는_액티비티에서 사용
        Intent intent = getIntent(); // 이전 액티비티에서 보낸 intent 받기
        String getString = intent.getStringExtra("카테고리");
        cate.setText(getString);
        catrgory = cate.getText().toString();
//         ========================


        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 프레그먼트로 이동하는법 알아야함
                Intent intent = new Intent(AddGroupActivity2.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                askpermission();  //사진을 갤러리에서 가져오기 버튼을 클릭시 작동 (아래 class 참조)

            }
        });


        buttonupload.setOnClickListener(new View.OnClickListener() { // 이미지 + 텍스트 업로드  버튼
            @Override
            public void onClick(View view) {
               String 그룹비번1 = grouppwd2.getText().toString();
                Log.i("그룹비번1", "비공개1223" + 그룹비번1);
                String nameimg = nameet.getText().toString();    // xml edittext 에서 입력 받은 이름
                String infoimg = infoet.getText().toString();       // xml edittext 에서 입력 받은 내용
//                userName = logname.getText().toString();  // shared로 저장해서 뿌려준 받아온 닉네임

                if (onoff == "공개") {

                    그룹비번 = "Public";
                    Log.i("그룹", "공개" + 그룹비번);
                } else if (onoff == "비공개") {


                    그룹비번 = 그룹비번1;

                    Log.i("그룹", "비공개123" + 그룹비번);
                }


                if (!nameimg.equals("") && !infoimg.equals("") && !Objects.equals(그룹비번, "") &&!Objects.equals(onoff,"")) {
                    Log.i("그룹", "통과");
                    progressBar.setVisibility(View.VISIBLE);

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            nameet.setText(""); // 처리후 값을 비워줌
                            Log.e("넘기기 ", "3");
                            infoet.setText(""); // 처리후 값을 비워줌
                            Log.e("넘기기 ", "4");
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.e("넘기기886 ", "4");

                            putDBlikelist(); // GR3LIKE table에 입력
                            Log.e("넘기기889ㅓ ", "4"); // 내가생성한 모임이 DB like table 로 들어갈수있게 하는 클래스

                            chatroomjoin();  // 채팅룸 개설


                            Intent intent = new Intent(AddGroupActivity2.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                            startActivity(intent);
                            finish();


//                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }) {

                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {


                            Map<String, String> param = new HashMap<String, String>();
                            //맵핑을 통해서 이미지, 이름, 내용, 작성자(shared로 받은값)을 보내줌
                            param.put("grimage", encodeImagae);  //encodeImagae는  String 변수로 지정해둠  아래에서 값을 넣음
                            param.put("grname", nameimg);          //gr이름
                            param.put("grdisc", infoimg);           // 그룹 설명
                            param.put("gruserName", loginname); // 작성자 이름
                            param.put("category", catrgory);    // 카테고리
                            param.put("onoff", onoff);  //공개 비공개
                            param.put("groupwd", 그룹비번);  //그룹비번

                            Log.i("라이테스트브방제", encodeImagae);
                            Log.i("라이테스트브방제", nameimg);
                            Log.i("라이테스트브방제", infoimg);
                            Log.i("라이테스트브방제", loginname);
                            Log.i("라이테스트브방제", catrgory);
                            Log.i("라이테스트브방제", onoff);
                            Log.i("라이테스트브방제222", 그룹비번);


                            return param;
                        }
                    };
                    // requestQueue를 통해서 전달 하면  php문을 통해 DB table에 저장된다.
                    RequestQueue requestQueue = Volley.newRequestQueue(AddGroupActivity2.this);
                    requestQueue.add(request);


                } else {
                    Toast.makeText(getApplicationContext(), "항목을 확인하세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void askpermission() { // 1  권한을 먼저 물어본다.
        // 여기서도 권한을 물어보지만 manifest 에서 저장장치 사용 permission을 미리 넣어두지 않으면 작동안함


        //Dexter라는 라이브러리를 사용하였음
        Dexter.withContext(AddGroupActivity2.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE) // 저장장치 사용허가
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) { // 권한을 허락하는 경우
                        chooseImage(); // 권한을 허락한경우 작동하는 이미지 불러오기 class (아래참조)

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) { // 권한을 거절하는경우
                        Toast.makeText(getApplicationContext(), "permission required", Toast.LENGTH_SHORT).show();
                    }

                    @Override // 이전에 거절했던적이있는경우 (거의 안씀)
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();


    }


    private void chooseImage() { // 갤러리에서 이미지를 가져오는 클래스

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

//
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        startActivityForResult(intent, 1);

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
                    imageView.setImageBitmap(bitmap);
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

    @Override
    protected void onPause() {
        super.onPause();


    }

    private void putDBlikelist() {
        Log.e("넘기기887 ", "4");
        StringRequest request = new StringRequest(Request.Method.POST, urlgetidx, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("넘기기888 ", "4");
                progressBar.setVisibility(View.INVISIBLE);


//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> param = new HashMap<String, String>();
                //맵핑을 통해서 이미지, 이름, 내용, 작성자(shared로 받은값)을 보내줌

                param.put("gruserName", loginname); // 유저닉네임 을 보낸다.  PHP문을 INSERT INTO SELECT 문을 이용해서  작동함 아래
                // 예시 "INSERT INTO GR3LIKE (like_user, like_gridx) SELECT gruserName,grid From grimage order by grid DESC limit 1";
                //DESC 오름차순 limit 1 1개 만
                //RG3groupimage2.php 문서 확인할것
                Log.e("sop", "99449");

                System.out.println(loginname);


                return param;
            }
        };
        // requestQueue를 통해서 전달 하면  php문을 통해 DB table에 저장된다.
        RequestQueue requestQueue = Volley.newRequestQueue(AddGroupActivity2.this);
        requestQueue.add(request);


    }

    //
//    private void grjoin() {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgrjoin, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success"); //불러온 값의 참 거짓을 확인하는 분기점
//                    if (success) {
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//
//
//                    Toast.makeText(getApplicationContext(), "에러 : " + e.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//
//                Toast.makeText(getApplicationContext(), "에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("like_user", loginname);
//                params.put("like_gridx", gridx);
//
//
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.add(stringRequest);
//
//    }
    private void chatroomjoin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlchatroom2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                Log.e("넘기기888 ", "4");
                progressBar.setVisibility(View.INVISIBLE);


//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> param = new HashMap<String, String>();
                //맵핑을 통해서 이미지, 이름, 내용, 작성자(shared로 받은값)을 보내줌

                param.put("gruserName", loginname); // 유저닉네임 을 보낸다.  PHP문을 INSERT INTO SELECT 문을 이용해서  작동함 아래
                // 예시 "INSERT INTO GR3LIKE (like_user, like_gridx) SELECT gruserName,grid From grimage order by grid DESC limit 1";
                //DESC 오름차순 limit 1 1개 만
                //RG3groupimage2.php 문서 확인할것
                Log.e("sop", "9987989");

                System.out.println(loginname);


                return param;
            }
        };
        // requestQueue를 통해서 전달 하면  php문을 통해 DB table에 저장된다.
        RequestQueue requestQueue = Volley.newRequestQueue(AddGroupActivity2.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.grtypeimgbtn:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setDialogListener(new CustomDialog.CustomDialogListener() {
                    @Override
                    public void onPositiveClicked(String name) {
                        grtypetv.setText("공개");
                        onoff = grtypetv.getText().toString();// 공개 or 비공개 텍스트를 String onoff에 저장
//                        onoroff.setText("공개");
                        grouppwd.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNegativeClicked() {
                        grtypetv.setText("비공개");
                        onoff = grtypetv.getText().toString();// 공개 or 비공개 텍스트를 String onoff에 저장
//                        onoroff.setText("비공개");

                        grouppwd.setVisibility(View.VISIBLE);

                    }
                });
                dialog.show();
                break;
        }
    }
}
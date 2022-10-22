package com.example.RG.Groupintro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.RG.CustomDialog;
import com.example.RG.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GroupNameImgSetActivity extends AppCompatActivity implements View.OnClickListener {

    TextView logname, grname,  grdisc, gropendate,grnum,onoroff,cattt;
    ImageView grpback, grpimg;
    Uri selecteduri;
    String imgString;
    Bitmap bitmap;
    String grsetnum,encodeImagae,grouponoff,groupname,groupdisc,groupdate,groucate;
    String loginname,category2,grou1;
    String urlload = "http://3.38.117.213/gr_updetgetinfo.php";
    String urlupdate = "http://3.38.117.213/RG3groupupdate.php";
    String url3;
    EditText category;
    Button grpohoto, grpostaddbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name_img_set);
        getSupportActionBar().hide();


        grpback = findViewById(R.id.ivback);
        grpohoto = findViewById(R.id.introupdateimgadd);
        grpimg = findViewById(R.id.introupdateimg);

//              Textview


        grname = findViewById(R.id.introupdatename);
        groupname = grname.getText().toString();

        grdisc = findViewById(R.id.introupdatedisc);
        groupdisc = grdisc.getText().toString();

        gropendate = findViewById(R.id.introupdateopendate);

        onoroff = findViewById(R.id.introupdateonoff);
        grouponoff = onoroff.getText().toString();


        category = findViewById(R.id.introcate);
        cattt = findViewById(R.id.cattt);
        groucate = category.getText().toString();

        grpostaddbtn = findViewById(R.id.introupdatesave);


        grnum = findViewById(R.id.grnum);
        logname = findViewById(R.id.logname);

        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
         loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값


// =================================== GroupActivity 에서 Bottomsheet Fragment 건너뛰기 위해 사용함  그룹번호 onpause 시 삭제 됨
        sp = getSharedPreferences("gridset", MODE_PRIVATE);
        grsetnum = sp.getString("그룹번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        grnum.setText(grsetnum); // name 란엔 xml파일에서 만들어둔 유저이름id값
        //======================================================



        loaddata();

        onoroff.setOnClickListener(this); // 공개 비공개 customdialog 클릭 이벤트


        spinner();


        grpohoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickimgpermission();
            }
        });

        grpback.setOnClickListener(new View.OnClickListener() { // 뒤로가기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupSettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);


            }
        });


        grpostaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                update();
                Intent intent = new Intent(getApplicationContext(), GroupSettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);


            }
        });






    }

    private void update() {



        groupname = grname.getText().toString();

        Log.e(  "iwjfoiejf", groupname);


        groupname = grname.getText().toString();

        groupdisc = grdisc.getText().toString();

        grouponoff = onoroff.getText().toString();

        groucate = category.getText().toString();

        BitmapDrawable drawable1 = (BitmapDrawable) grpimg.getDrawable(); // imageview를 Bitmap으로 bitmap을 String으로
        Bitmap bitmap1 = drawable1.getBitmap();
        grpimg.setImageBitmap(bitmap1);

        //Bitmap을 String형으로 변환
        ByteArrayOutputStream baos1= new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 70, baos1);
        byte[] bytes1 = baos1.toByteArray();
      encodeImagae = Base64.encodeToString(bytes1, Base64.DEFAULT);



        StringRequest request = new StringRequest(Request.Method.POST, urlupdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
Log.e(  "ggggg", response);

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


                param.put("grimage", encodeImagae);  //encodeImagae는  String 변수로 지정해둠  아래에서 값을 넣음
                param.put("grid", grsetnum);  //encodeImagae는  String 변수로 지정해둠  아래에서 값을 넣음
                param.put("grname", groupname);          //gr이름
                param.put("grdisc", groupdisc);           // 그룹 설명
                param.put("gruserName", loginname); // 작성자 이름
                param.put("category", groucate);    // 카테고리
                param.put("onoff", grouponoff);  //공개 비공개


                return param;
            }
        };
        // requestQueue를 통해서 전달 하면  php문을 통해 DB table에 저장된다.
        RequestQueue requestQueue = Volley.newRequestQueue(GroupNameImgSetActivity.this);
        requestQueue.add(request);

    }

    private void spinner() {
        Spinner spinnerMenu = (Spinner)findViewById(R.id.spinner);

        final String[] 카테고리 = getResources().getStringArray(R.array.my_array);




        ArrayAdapter menuAdapter = ArrayAdapter.createFromResource(this, R.array.my_array, android.R.layout.simple_spinner_item);

        // Spinner 클릭시 DropDown 모양을 설정
        menuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 스피너에 어댑터를 연결
        spinnerMenu.setAdapter(menuAdapter);


        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category.setText(카테고리[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void pickimgpermission() { // 1  권한을 먼저 물어본다.
        // 여기서도 권한을 물어보지만 manifest 에서 저장장치 사용 permission을 미리 넣어두지 않으면 작동안함


        //Dexter라는 라이브러리를 사용하였음
        Dexter.withContext(GroupNameImgSetActivity.this)
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
                    grpimg.setImageBitmap(bitmap);
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


    private void loaddata() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success"); //불러온 값의 참 거짓을 확인하는 분기점
                    if (success) {

                        String gid = jsonObject.getString("grid");
                        String gname = jsonObject.getString("grname");
                         url3 = jsonObject.getString("grimage");
                        String gdisc = jsonObject.getString("grdisc");
                        String guname = jsonObject.getString("gruserName");
                        String onoff = jsonObject.getString("onoff");
                        String cate = jsonObject.getString("category");
                        String creat = jsonObject.getString("created_at");

//                        String urlimage = "http://3.38.117.213/image/"+url2; Log.e(  "변수명", "5");

                        Glide.with(getApplicationContext()).load("http://3.38.117.213/image/" + url3).into(grpimg);
                        grnum.setText(gid);
                        grname.setText(gname);
                        grdisc.setText(gdisc);
                        onoroff.setText(onoff);

                        category.setText(cate);
                        gropendate.setText(creat);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();


//                    Toast.makeText(getApplicationContext(), "에러 : " + e, Toast.LENGTH_SHORT).show();
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
                params.put("grid", grsetnum);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.introupdateonoff:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setDialogListener(new CustomDialog.CustomDialogListener() {
                    @Override
                    public void onPositiveClicked(String name) {
                        onoroff.setText("공개");
                        grouponoff= onoroff.getText().toString();// 공개 or 비공개 텍스트를 String onoff에 저장
//                        onoroff.setText("공개");

                    }

                    @Override
                    public void onNegativeClicked() {
                        onoroff.setText("비공개");
                        grouponoff= onoroff.getText().toString();// 공개 or 비공개 텍스트를 String onoff에 저장
//                        onoroff.setText("비공개");
                    }
                });
                dialog.show();
                break;
        }
    }


}
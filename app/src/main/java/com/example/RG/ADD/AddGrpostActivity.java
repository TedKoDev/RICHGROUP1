package com.example.RG.ADD;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.RG.Home_group.GrPostFragment;
import com.example.RG.Home_group.GroupActivity;
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

public class AddGrpostActivity extends AppCompatActivity {

    Uri selecteduri;
    Bitmap bitmap;
    String encodeImagae;// encode 이미지  encode가 뭔지 공부해보기

    ImageView grpivback, grpiv;
    Button grpgetimage, grpupload;

    String url = "http://3.38.117.213/RG3grpost.php";

    TextView logname, gr_id;
    EditText grpdisc;

    GrPostFragment grPostFragment = new GrPostFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grpost);
        getSupportActionBar().hide();

        logname = findViewById(R.id.logname);// login한 유저 네임 쉐어드로 넣을것
        gr_id = findViewById(R.id.gr_id);// gr번호
        grpivback = findViewById(R.id.grpivback); // 뒤로버튼
        grpiv = findViewById(R.id.grpiv); //post이미지
        grpdisc = findViewById(R.id.grpdisc);//post 게시글
        grpgetimage = findViewById(R.id.grpgetimage); // 사진가져오기버튼
        grpupload = findViewById(R.id.grpupload); // 게시물 업로드 버튼



        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        String loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값


//grPostFragment로 부터 받은 gr_id 번호
        Intent intent = getIntent();
        String s = intent.getStringExtra("grid");
        gr_id.setText(s);
//-------------------------------------------------------


        grpivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddGrpostActivity.this, GroupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);

            }
        });


        grpgetimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                grpaskpermission();

            }
        });


        grpupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                grpuserName = findViewById(R.id.grpuserName);// login한 유저 네임
//                grpivback = findViewById(R.id.grpivback); // 뒤로버튼
//                grpiv = findViewById(R.id.grpiv); //post이미지
//                grpdisc = findViewById(R.id.grpdisc);//post 게시글
//                grpgetimage = findViewById(R.id.grpgetimage); // 사진가져오기버튼
//                grpupload = findViewById(R.id.grpupload); // 게시물 업로드 버튼

                String userName = logname.getText().toString();
                String grpDisc = grpdisc.getText().toString();
                String gr_ID = gr_id.getText().toString();


                // 글쓰기
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        grpdisc.setText("");


//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
//
                        Intent intent = new Intent(AddGrpostActivity.this, GroupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {

                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> param = new HashMap<String, String>();

                        param.put("gr_idx", gr_ID);
                        param.put("grpimage", encodeImagae);
                        param.put("grpdisc", grpDisc);
                        param.put("grpwriter", userName);
                        param.put("grpstarttime", "NotVideo");


                        return param;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(AddGrpostActivity.this);
                requestQueue.add(request);


            }


        });




    }


    private void grpaskpermission() {

        Dexter.withContext(AddGrpostActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        grpchooseImage();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getApplicationContext(), "permission required", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();


    }

    private void grpchooseImage() {

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
                    grpiv.setImageBitmap(bitmap);
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

}
package com.example.RG.post;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class PostUpdateActivity extends AppCompatActivity {
    Uri selecteduri;
    Bitmap bitmap;
    String encodeImagae;// encode 이미지  encode가 뭔지 공부해보기


    String loginuser, gridx,postnumber, postwriter, postwritedate;

    String postimg,postdiscupdate;
            String gpdisc;

    ImageView grpivback,grpiv;
    Button grpgetimage,grpupload,grpliveshow;

    String url = "http://3.38.117.213/postupdate.php";

    TextView logname;
    EditText grpdisc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_update);
        getSupportActionBar().hide();




        logname = findViewById(R.id.logname);// login한 유저 네임 쉐어드로 넣을것

        grpivback = findViewById(R.id.grpivback); // 뒤로버튼
        grpiv = findViewById(R.id.grpiv); //post이미지
        grpdisc = findViewById(R.id.grpdisc);//post 게시글
        grpgetimage = findViewById(R.id.grpgetimage); // 사진가져오기버튼
        grpupload = findViewById(R.id.grpupload); // 게시물 업로드 버튼


        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        String loginname = sp.getString("userName"," "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값


//PostdetailActivity로 부터 받은 이미지,텍스트 값
        Intent intent = getIntent();

      postimg = intent.getStringExtra("이미지");
        gpdisc = intent.getStringExtra("텍스트");

        byte[] encodeByte = Base64.decode(postimg, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        grpiv.setImageBitmap(bitmap);


        grpdisc.setText(gpdisc);




//-------------------------------------------------------


        // =================================== 게시물 번호
        sp = getSharedPreferences("게시물정보", MODE_PRIVATE);
        postnumber = sp.getString("포스트번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환

//        postnum.setText(postnumber); // name 란엔 xml파일에서 만들어둔 유저이름id값

        //======================================================


        grpivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostUpdateActivity.this, PostdetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
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



                postdiscupdate = grpdisc.getText().toString();


                BitmapDrawable drawable1 = (BitmapDrawable) grpiv.getDrawable(); // imageview를 Bitmap으로 bitmap을 String으로
                Bitmap bitmap1 = drawable1.getBitmap();
                grpiv.setImageBitmap(bitmap1);

                //Bitmap을 String형으로 변환
                ByteArrayOutputStream baos1= new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 70, baos1);
                byte[] bytes1 = baos1.toByteArray();
                encodeImagae = Base64.encodeToString(bytes1, Base64.DEFAULT);



                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
//
                        Intent intent = new Intent(PostUpdateActivity.this, PostdetailActivity.class);
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

                        param.put("grpidx", postnumber);
                        param.put("grpimage", encodeImagae);
                        param.put("grpdisc", postdiscupdate);





                        return param;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(PostUpdateActivity.this);
                requestQueue.add(request);


            }


        });





    }



    private void grpaskpermission() {

        Dexter.withContext(PostUpdateActivity.this)
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

        if ( data != null ) {
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
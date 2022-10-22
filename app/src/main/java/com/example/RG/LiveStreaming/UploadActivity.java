package com.example.RG.LiveStreaming;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.MainActivity;
import com.example.RG.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {


    Uri uri;
    String 그룹번호;
    String 썸네일이미지;
    String 방제목;
    String 작성자;
    String onoff;
    String pwd;
    private String upload_URL = "http://3.38.117.213/volleyvideo2.php";
    //    private String upload_URL = "http://3.38.117.213/volleyvideo.php";
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getSupportActionBar().hide();

        SharedPreferences sp = getSharedPreferences("live", MODE_PRIVATE);

        그룹번호 = sp.getString("그룹번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        썸네일이미지 = sp.getString("썸네일이미지", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        방제목 = sp.getString("방제목", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        작성자 = sp.getString("작성자", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        onoff = sp.getString("onoff", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        pwd = sp.getString("pwd", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환


//        // create a handler to post messages to the main thread
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "게시하시겠습니까", Toast.LENGTH_SHORT).show();
            }
        });

        // 값받는_액티비티에서 사용
        Intent intent = getIntent(); // 이전 액티비티에서 보낸 intent 받기
        String getString = intent.getStringExtra("filename");

        TextView tvfilename = findViewById(R.id.tvfilename);

        tvfilename.setText(getString);

        Button 전송하기 = findViewById(R.id.sending);

        전송하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uri = getUriFromPath(getString);

                uploadPDF("20", getString, uri);
//            File file = new File(getString);
//            boolean deleted = file.delete();
//            Log.i("filePathdelete", String.valueOf(deleted));

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);
                tokendelete();


            }
        });
        Button 취소하기 = findViewById(R.id.cancle);

        취소하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);

                tokendelete();

            }
        });


    }



    private void uploadPDF(final String roomnumber, String filename, Uri uri) {
        Log.i("uri2", String.valueOf(uri));
        Log.i("uri2filename", filename);

        InputStream iStream = null;

        try {

            iStream = getContentResolver().openInputStream(uri);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.i("ㅍvideo", "1");
                            Log.i("ㅍvideo", String.valueOf(response));
                            Log.d("ㅍressssssoo", new String(response.data));
//                            rQueue.getCache().clear();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "너너너"+error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("ㅍvideo에러 ", error.getMessage());
                            Log.i("ㅍvideo", "10");
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("grnumber", 그룹번호);
                    params.put("thumimname", 썸네일이미지);
                    params.put("livename", 방제목);
                    params.put("host", 작성자);
                    return params;
                }

                //                그룹번호
//                        썸네일이미지
//                방제목
//                        작성자
                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    Log.i("ㅍvideo", "11 파일올림 ");
                    params.put("filename", new DataPart(filename, inputData));


                    Log.i("ㅍvideo파일이름매개변수", filename);
                    Log.i("ㅍvideo데이터매개변수", String.valueOf(inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(UploadActivity.this);
            rQueue.add(volleyMultipartRequest);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public Uri getUriFromPath(String path) {
//        String fileName = "file:///storage/emulated/0/DCIM/1654928075163.mp4";
        String fileName = "file:///storage/emulated/0/DCIM/1654929902950.mp4";
//
        Log.i("filePath3", path);

        String A = path;
        Log.i("filePath4", A);
        Uri fileUri = Uri.parse(A);
        Log.i("filePath5", String.valueOf(fileUri));
        String filePa = fileUri.getPath();

        Log.i("filePath6", filePa);
        Cursor c = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePa + "'", null, null);
        c.moveToNext();
        @SuppressLint("Range") int id = c.getInt(c.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
        return uri;
    }

    private void tokendelete() {


        String url = "http://3.38.117.213/tokendelete.php"; //


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                                Toast.makeText(getApplicationContext(), "너너너2토큰제거"+ response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getApplicationContext(), "너너너에러 : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("thumimage", 썸네일이미지);
                params.put("user", 작성자);
                params.put("grnum", 그룹번호);

                Log.i("토큰삭제썸", 썸네일이미지);
                Log.i("토큰삭제유저", 작성자);
                Log.i("토큰삭제그룹번", 그룹번호);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}


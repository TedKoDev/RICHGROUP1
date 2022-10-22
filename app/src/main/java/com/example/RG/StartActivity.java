package com.example.RG;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.RG.register.LoginActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거

            startActivity(intent); // 다음화면으로 넘어가기
            finish(); // Activity 화면 제거


        }
    };
    Runnable r1 = new Runnable() {
        @Override
        public void run() {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거

            startActivity(intent); // 다음화면으로 넘어가기
            finish(); // Activity 화면 제거


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().hide();


        permission();


    }

    public void permission() {

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE

                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        SharedPreferences sp = getSharedPreferences("autologin", MODE_PRIVATE);

                        String alog = sp.getString("autolog", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
                        Log.i("autolog???", alog);


                        if (Objects.equals(alog, "1")) {
                            Log.i("autolog1", alog); // 자동로그인상황( 빠르게 메인으로 넘어감
                            handler.postDelayed(r1, 700); // 1.3초 뒤에 Runnable 객체 수행


                        } else {
                            Log.i("autolog0", alog);
                            handler.postDelayed(r, 1300); // 1.3초 뒤에 Runnable 객체 수행
                        }



                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) { token.continuePermissionRequest();}
                }).check();

    }


    @Override
    protected void onPause() {
        super.onPause();
// 화면을 벗어나면, handler 에 예약해놓은 작업을 취소하자
        handler.removeCallbacks(r); // 예약 취소
    }


}
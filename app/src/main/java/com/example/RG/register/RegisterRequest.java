package com.example.RG.register;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {


    //URL 설정 (PHP 파일 연동)
    final static private String URL ="http://3.38.117.213/rgregister.php";
    private Map<String, String> map;

    public RegisterRequest(String user_email, String user_pw, String userName, Response.Listener<String> listener){
                                                                               //응답을 받을수있게 Response.Listener<String> listener (응답받는용도)추가해줌
        super (Method.POST, URL, listener, null);
        Log.e("REGISTER.Request ", "1");
        Log.e("REGISTER.Request ", user_email);
        Log.e("REGISTER.Request ", user_pw);
        Log.e("REGISTER.Request ", userName);

        map = new HashMap<>();  //intent putextra 랑 비슷함
        map.put("user_email",user_email);   Log.e("REGISTER.Request ", "2");
        map.put("user_pw",user_pw);   Log.e("REGISTER.Request ", "3");
        map.put("userName",userName);   Log.e("REGISTER.Request ", "4");
        map.put("kakaoid","NO");   Log.e("REGISTER.Request ", "4");
        Log.e("REGISTER.Request.map ", user_email);
        Log.e("REGISTER.Request.map ", user_pw);
        Log.e("REGISTER.Request.map ", userName);
    }

    // 컨드롤 + o getParams
    @Override
    protected Map<String, String> getParams() throws AuthFailureError { ////서버에 String에 들어간 값을 넘겨주는 역할을 함 (전송)   없으면 null값이 전달됨
        Log.e("REGISTER.Request ", "5");

        return map;
    }
}

package com.example.RG.register;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest  extends StringRequest {


    //URL 설정 (PHP 파일 연동)
    final static private String URL ="http://3.38.117.213/rglogin.php";
    private Map<String, String> map;

    public LoginRequest(String user_email, String user_pw, Response.Listener<String> listener){
        super (Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("user_email",user_email);
        map.put("user_pw",user_pw);
  }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
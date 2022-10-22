package com.example.RG.register;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class mailValidateRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static private String URL ="http://3.38.117.213/newnickvalidate.php";
    private Map<String, String> parameters;

    public mailValidateRequest(String user_email, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함

        Log.e(  "mailvali", "from Register-mail-중복확인 4");
        Log.e(  "mailvali", user_email);
        Log.e(  "mailvali", "1");

        parameters = new HashMap<>();Log.e(  "mailvali", "2");
        parameters.put("user_email", user_email);
        Log.e(  "mailvali-user_email값", user_email);


        Log.e(  "mailvali", "3-> Register-mail 25");

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {  //서버에 String에 들어간 값을 넘겨주는 역할을 함 (전송)   없으면 null값이 전달됨


        Log.e(  "mailvali", "4");
        Log.e(  "mailvali", "서버로 전송함 ");
        return parameters;
    }

}
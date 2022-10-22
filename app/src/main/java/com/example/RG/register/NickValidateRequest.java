package com.example.RG.register;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NickValidateRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static private String URL ="http://3.38.117.213/rgvalidate.php";
    private Map<String, String> parameters;

    public NickValidateRequest(String userName, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("userName", userName);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
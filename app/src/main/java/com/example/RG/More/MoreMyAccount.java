package com.example.RG.More;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.R;
import com.example.RG.register.LoginActivity;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MoreMyAccount extends AppCompatActivity {


    String investdeleteall = "http://3.38.117.213/investdeleteall.php"; //


    TextView logname, setnickchange, setinvestreset, setlogout, setpwdchange, mcoldpwd;
    ImageView setback;

    String loginname,oldpasswd;

    String url = "https://kauth.kakao.com/oauth/logout?client_id=43b53bf12629f43b79a681e6d3fd0419&logout_redirect_uri=http://3.38.117.213/logoutdone.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_my_account);
        getSupportActionBar().hide();


        logname = findViewById(R.id.logname);
        setnickchange = findViewById(R.id.setnickchange);
        setpwdchange = findViewById(R.id.setpwdchange);
        setinvestreset = findViewById(R.id.setinvestreset);
        setlogout = findViewById(R.id.setlogout);
//        setlogoutka = findViewById(R.id.setlogoutka);
        setback = findViewById(R.id.setback);
        mcoldpwd = findViewById(R.id.mcoldpwd);




        //??????????????? ???????????? ????????? =====================================================
        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key?????? ????????? ?????? ???????????????, ???????????????""(??????)??? ??????
        oldpasswd = sp.getString("user_pw", " "); //key?????? ????????? ?????? ???????????????, ???????????????""(??????)??? ??????

        mcoldpwd.setText(oldpasswd); // name ?????? xml???????????? ???????????? ????????????id???
        logname.setText(loginname); // name ?????? xml???????????? ???????????? ????????????id???

        //============================================================================


//        if (Objects.equals(oldpasswd, "????????? ??????????????????.")){
//
////            pwdchangelayout.setVisibility(View.GONE);
//            setlogout.setVisibility(View.GONE);
//            setlogoutka.setVisibility(View.VISIBLE);
//        }





        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        setnickchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MoreMyNickChange.class);

                startActivity(intent);

            }
        });

        setpwdchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MoreMypwdChange.class);

                startActivity(intent);

            }
        });


        setinvestreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(v.getContext());
                mbuilder.setTitle("???????????? ?????????");
                mbuilder.setMessage("?????? ??????????????? ????????????????????????????");
                mbuilder.setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Your Button Click Action Code

                        investdelete();


                        SharedPreferences sp1 = getSharedPreferences("????????????", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp1.edit();
                        editor.remove("?????????");
                        editor.remove("?????????");
                        editor.remove("?????????");
                        editor.remove("????????????");

                        editor.apply();


                    }
                });
                mbuilder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Your Button Click Action Code
                        dialog.dismiss();

                    }
                });
                mbuilder.show();
            }
        });


//        setlogoutka.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
//                    @SuppressLint("SetJavaScriptEnabled")
//                    @Override
//                    public Unit invoke(Throwable throwable) { //invoke??? ??????
//                        updateKakaoLoginUI();
//
//
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        SharedPreferences sp2 = getSharedPreferences("autologin", MODE_PRIVATE);
//                        SharedPreferences.Editor editor2 = sp2.edit();
//                        editor2.putString("autolog", "0"); //???????????????
//                        editor2.apply();
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//???????????? ????????????
//
//                        startActivity(intent);
//
////                        ?????????????????????();
//                        return null;
//                    }
//                });
//
//
//            }
//        });


        setlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mbuilder = new AlertDialog.Builder(v.getContext());
                mbuilder.setTitle("????????????");
                mbuilder.setMessage("?????? ???????????? ???????????????????");
                mbuilder.setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Your Button Click Action Code

                        SharedPreferences sp2 = getSharedPreferences("autologin", MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sp2.edit();
                        editor2.putString("autolog", "0"); //???????????????
                        editor2.apply();

                        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) { //invoke??? ??????
                                Log.i("??????????????????", "8");
                                updateKakaoLoginUI();

                                return null;
                            }
                        });


                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//???????????? ????????????
                        startActivity(intent);


                    }
                });
                mbuilder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Your Button Click Action Code
                        dialog.dismiss();

                    }
                });
                mbuilder.show();
            }
        });


    }

    private void updateKakaoLoginUI() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) {
                    Log.i("1?????????1", "2");


                } else {

                    Log.i("1?????????1", "????????????");

//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//???????????? ????????????
//                    startActivity(intent);


                }
                return null;
            }
        });
    }

    private void investdelete() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, investdeleteall, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "??????????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getApplicationContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("??????", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("loginname", loginname);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
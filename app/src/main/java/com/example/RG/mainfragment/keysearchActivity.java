package com.example.RG.mainfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.Home_group.GroupActivity;
import com.example.RG.MainActivity;
import com.example.RG.R;
import com.example.RG.RecyclerViewEmptySupport;
import com.example.RG.SearchAdapter.SearchAdapter;
import com.example.RG.SearchAdapter.SearchData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class keysearchActivity extends AppCompatActivity {


    String url = "http://3.38.117.213/RG3searchgroupget.php";
    String urlsearch = "http://3.38.117.213/searchget.php";


    RecyclerViewEmptySupport recyclerView;
    SearchData searchData;
    EditText sET;
    Button serchbtn;
    TextView list_empty;
    String key, Setkeyword;
    ImageView ivback;
    private ArrayList<SearchData> searchlist = new ArrayList<>();
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keysearch);
        getSupportActionBar().hide();


        sET = findViewById(R.id.sET);
        serchbtn = findViewById(R.id.serchbtn);
        ivback = findViewById(R.id.ivback);


        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // ?????????????????? ??????????????? ????????????
                Intent intent = new Intent(keysearchActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//???????????? ????????????
                startActivity(intent);

            }
        });

        serchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sET.getText().toString().equals("") || sET.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                } else {


                    key = sET.getText().toString();
                    Log.e("key??? ???????", key);


                    StringRequest request = new StringRequest(Request.Method.POST, urlsearch, new Response.Listener<String>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(String response) {


                            searchlist.clear();
                            Log.e("??????", "1");
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.e("??????", "2");
                                String success = jsonObject.getString("success");
                                Log.e("??????", "3");

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if ("data".equals("[]") || "data" == null) {
                                    Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();
                                } else {

                                    Log.e("??????", "4");
                                    Log.e("total", response);

                                    if (success.equals("1")) {
                                        Log.e("??????", "5");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            Log.e("??????", "6");
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            Log.e("??????", "7");

                                            String id = object.getString("grid");
                                            Log.e("?????????", "1");
                                            String url2 = object.getString("grimage");
                                            Log.e("?????????", "2");
                                            String name = object.getString("grname");
                                            Log.e("?????????", "3");
                                            String disc = object.getString("grdisc");
                                            Log.e("?????????", "4");
                                            String maker = object.getString("gruserName");
                                            Log.e("?????????", "4");

                                            String onoff = object.getString("onoff");
                                            Log.e("?????????", "4");

                                            String groupwd = object.getString("groupwd");
                                            Log.e("?????????", "4");

                                            String opendate = object.getString("created_at");
                                            Log.e("?????????", "4");

                                                                        // String ??? Date???
                                                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                        StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// ?????? UTC??? ?????? ??????????????? ???????????? UTC??????????????? ????????? ??????????????? ???????????????
                                                                        Date datedtime = StringtoDateFormat.parse(opendate);

                                                                        //UTC??? ????????? ????????? UTC??? ?????? TIMEZONE??? ?????? ???????????? ??????????????? ????????????.
                                                                        // ????????? ????????????.

                                                                        Log.i("987 ????????????Date?????? utc?????????", String.valueOf(datedtime));


                                                                        //??????  ???????????? SimpleDateFormat dateFormatLocalization ?????? DATE??? ????????? ?????? ????????? ???????????? ????????? ????????????.
                                                                        // ????????? ????????????
                                                                        // ????????? Mysql??? KST ???????????? ???????????? ??????????????? ??? ????????????
                                                                        // ?????? 2????????? ???????????? ??????????????? ?????? ????????????.
                                                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatLocalization = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//***/*
                                                                        Date utcDate = StringtoDateFormat.parse(dateFormatLocalization.format(datedtime));


                                                                        //???????????? ????????? Date??? String ??? ??????
                                            //                            String ltime = StringtoDateFormat.format(datedtime);//????????? ??????????????????
                                                                        String ltime = dateFormatLocalization.format(datedtime); // ????????? ????????? ????????? ??????????????? ???????????? ?????????  ????????? ????????? ?????? ***?????? ??????????????????.
                                                                        System.out.println("987 ?????? ??? ???????????? ????????? ???????????? : " + ltime);
                                                                        Log.i("987 datedtime", String.valueOf(datedtime));
                                                                        Log.i("987 utcDate", String.valueOf(utcDate));

                                                                        Log.i("987 time", String.valueOf(opendate));
                                                                        Log.i("987 ltime", String.valueOf(ltime));





                                            String urlimage = "http://3.38.117.213/image/" + url2;

                                            Log.e("?????????", "5");

                                            searchData = new SearchData(id, urlimage, name, disc, maker, onoff, groupwd, ltime); //Data??? 6?????? ????????? ????????????????????? 4?????? ?????? ???????????????.
                                            Log.e("?????????", "6");
                                            searchlist.add(searchData);
                                            Log.e("?????????", "7");


                                            System.out.println(id);
                                            System.out.println(url2);
                                            System.out.println(name);
                                            Log.e("?????????", response);
                                        }

                                    }
                                    searchAdapter = new SearchAdapter(getApplicationContext(), searchlist); // ?????? context ??? ????????? getactivity??? ??????
                                    Log.e("error?????????", "148");
                                    recyclerView.setAdapter(searchAdapter);
                                    Log.e("error?????????", "150");
                                    searchAdapter.notifyDataSetChanged();
                                    Log.e("?????????", "8");

                                    Log.e("error?????????", "145");

                                }

                            } catch (Exception ignored) {
                                Toast.makeText(getApplicationContext(), "??????????????? ????????????.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }) {

                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {


                            Map<String, String> param = new HashMap<String, String>();
                            //????????? ????????? ?????????, ??????, ??????, ?????????(shared??? ?????????)??? ?????????

                            param.put("key", key);  //key???
                            Log.e("?????????", key);


                            return param;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(keysearchActivity.this);
                    requestQueue.add(request);

                }
            }

        });


        //        searching();
//        buildListData();
        initRecyclerView();

        recy();

        list_empty = findViewById(R.id.list_empty);
        recyclerView.setEmptyView(list_empty);


    }

    private void recy() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                searchData = searchlist.get(position);


                Intent intent = new Intent(getApplicationContext(), GroupActivity.class);

                ////================ ?????????????????? '????????????' ?????? ???????????? ??????????????? ????????? ????????????.
                SharedPreferences sp = getApplicationContext().getSharedPreferences("gridset", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("????????????", searchData.getGrid());
                editor.apply();
////=============================================================================================================


                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }


    private void initRecyclerView() {
        recyclerView = findViewById(R.id.srv);
//        gridLayoutManager
//                = new GridLayoutManager(getContext(), 3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        gridLayoutManager.setOrientation(gridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);


    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}
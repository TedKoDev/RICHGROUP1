package com.example.RG;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.LiveAdapter.LiveAdapter;
import com.example.RG.LiveAdapter.LiveData;
import com.example.RG.LiveStreaming.LiveActivity2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class AllLiveListActivity extends AppCompatActivity {


    String loginname;


    String url = "http://3.38.117.213/tokengetall.php";


    RecyclerViewEmptySupport recyclerView;
    TextView list_empty;

    ArrayList<LiveData> livelist = new ArrayList<>();
    LiveData liveData;
    LiveAdapter liveAdapter;

    ProgressBar progress_circular;

    NestedScrollView scrollView;
    ImageView ivback;

    Button btn_alllivelist;

    private int frompage = 0;
    private int topage = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_live_list);
        getSupportActionBar().hide();


        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key?????? ????????? ?????? ???????????????, ???????????????""(??????)??? ??????
        ivback = findViewById(R.id.ivback);

        recyclerView = findViewById(R.id.live_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        buildListData();


        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        list_empty = findViewById(R.id.list_empty);
        recyclerView.setEmptyView(list_empty);

// recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                liveData = livelist.get(position);


                checkinornot();//?????? ????????? ???????????? ????????? ??????


            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


        scrollView = findViewById(R.id.scroll_view);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    topage = topage + 5;
                    buildListData();


                }

            }
        });


    }

    private void checkinornot() {

        String url = "http://3.38.117.213/joinornot.php"; //


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String ??? = response;

                if (Objects.equals(???, "success")) {

                    Log.i("INORNO", "???????????? ");
                    Intent intent = new Intent(getApplicationContext(), LiveActivity2.class);

                    intent.putExtra("??????", liveData.getLive_token()); // ?????? ??? ??????
                    intent.putExtra("?????????", liveData.getLive_agorachaname()); // ?????? ??? ??????
                    intent.putExtra("??????or??????", 2); // ?????? ??? ??????
////=============================================================================================================


                    startActivity(intent);


                } else {
                    Log.i("INORNO", "?????????no?????? ");
                    showLoginDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getApplicationContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grid", liveData.getLive_gridx());
                params.put("user", loginname);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    private void showLoginDialog() {


        Intent intent = new Intent(getApplicationContext(), INorNOTActivity.class);
        intent.putExtra("?????????", liveData.getLive_gridx()); // ?????? ??? ??????
        startActivity(intent);

        Toast.makeText(getApplicationContext(), "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onResume() {
        super.onResume();
        buildListData();


    }


    private void buildListData() {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {


                livelist.clear();
                Log.e("??????", "1");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("??????", "2");
                    String success = jsonObject.getString("success");
                    Log.e("??????", "3");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("??????", "4");
                    Log.e("total", response);

                    if (success.equals("1")) {
                        Log.e("??????", "5");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.e("??????", "6");
                            JSONObject object = jsonArray.getJSONObject(i);
                            Log.e("??????", "7");

                            String idx = object.getString("idx");
                            Log.e("?????????", "1");

                            String gridx = object.getString("roomnum");
                            Log.e("?????????", "3");

                            String chaname = object.getString("chaname");
                            Log.e("?????????", "3");

                            String token = object.getString("token");
                            Log.e("?????????", "4");
                            String title = object.getString("title");
                            Log.e("?????????", "4");
                            String Sonoff = object.getString("onoff");
                            Log.e("?????????", "4");
                            String Spwd = object.getString("pwd");
                            Log.e("?????????", "4");


                            String writer = object.getString("user");
                            Log.e("?????????", "4");

                            String url2 = object.getString("thumimage");
                            Log.e("?????????url2", url2);

                            String opendate = object.getString("opendate");
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


                            String live_gname = object.getString("live_gname");
                            Log.e("?????????", "4");


                            String urlimage = "http://3.38.117.213/image/" + url2;

                            Log.e("?????????", "5");

                            liveData = new LiveData(idx, gridx, chaname, token, title, writer, ltime, urlimage, Sonoff, Spwd, live_gname); //Data??? 6?????? ????????? ????????????????????? 4?????? ?????? ???????????????.
                            Log.e("?????????", "6");
                            livelist.add(liveData);
                            Log.e("?????????", "7");


                        }
                        liveAdapter = new LiveAdapter(getApplicationContext(), livelist); // ?????? context ??? ????????? getactivity??? ??????
                        Log.e("error?????????", "148");
                        recyclerView.setAdapter(liveAdapter);
                        Log.e("error?????????", "150");
                        liveAdapter.notifyDataSetChanged();
                        Log.e("?????????", "8");

                        Log.e("error?????????", "145");

                    }
                } catch (Exception e) {
                }
//                    Toast.makeText(getActivity(), "??????", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                String Sfrompage = String.valueOf(frompage);
                String Stopage = String.valueOf(topage);

                params.put("from", Sfrompage);
                params.put("to", Stopage);

                Log.i("??????Sfrompage", Sfrompage);
                Log.i("??????Stopage", Stopage);
                Log.i("??????Stopage", Stopage);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private AllLiveListActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context applicationContext, RecyclerViewEmptySupport recyclerView, ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
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
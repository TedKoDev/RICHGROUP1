package com.example.RG.mainfragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.AllLiveListActivity;
import com.example.RG.LiveAdapter.LiveAdapter;
import com.example.RG.LiveAdapter.LiveData;
import com.example.RG.LiveStreaming.LiveActivity2;
import com.example.RG.OnBackPressedListener;
import com.example.RG.R;
import com.example.RG.RecyclerViewEmptySupport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;


public class LiveFragment extends Fragment implements OnBackPressedListener {

    String url = "http://3.38.117.213/tokenget.php";
    private int overallYScroll = 0;

    boolean position_flag = true;


    RecyclerViewEmptySupport recyclerView;
    TextView list_empty;

    ArrayList<LiveData> livelist = new ArrayList<>();
    LiveData liveData;
    LiveAdapter liveAdapter;

    NestedScrollView scrollView;

    String loginname;


    Button btn_alllivelist;

    private int frompage = 0;
    private int topage = 5;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildListData();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_live, container, false);


        SharedPreferences sp = this.getActivity().getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key?????? ????????? ?????? ???????????????, ???????????????""(??????)??? ??????
        Log.i("user0", loginname);

        recyclerView = view.findViewById(R.id.live_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        buildListData();

        list_empty = view.findViewById(R.id.list_empty);
        recyclerView.setEmptyView(list_empty);

        btn_alllivelist = view.findViewById(R.id.btn_alllivelist);
        btn_alllivelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(view.getContext(), AllLiveListActivity.class);


                startActivity(intent);

            }
        });


//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy == recyclerView.getChildAt(0).getMeasuredHeight() - recyclerView.getMeasuredHeight()) {
//                    topage = topage + 5;
//                    buildListData();
//
//
//
//                }
//
//
//
//
//            }
//        });


        scrollView = view.findViewById(R.id.scroll_view);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    topage = topage + 5;
                    buildListData();



                }

            }
        });




        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                liveData = livelist.get(position);


//                Log.i("??????", liveData.getLive_onoff());
//                if (!Objects.equals(liveData.getLive_onoff(), "??????")) {
//                    checkinornot();//?????? ????????? ???????????? ????????? ??????
//
//
//                } else {

                    Intent intent = new Intent(getContext(), LiveActivity2.class);
                    intent.putExtra("??????", liveData.getLive_token()); // ?????? ??? ??????
                    intent.putExtra("?????????", liveData.getLive_agorachaname()); // ?????? ??? ??????
                    intent.putExtra("??????or??????", 2); // ?????? ??? ??????


                    startActivity(intent);
//                }


            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        return view;
    }

//    private void checkinornot() {
//
//        String url = "http://3.38.117.213/joinornot.php"; //
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                String ??? = response;
//
//                if (Objects.equals(???, "success")) {
//
//                    Intent intent = new Intent(getContext(), LiveActivity2.class);
//
//                    intent.putExtra("??????", liveData.getLive_token()); // ?????? ??? ??????
//                    intent.putExtra("?????????", liveData.getLive_agorachaname()); // ?????? ??? ??????
//                    intent.putExtra("??????or??????", 2); // ?????? ??? ??????
//////=============================================================================================================
//
//
//                    startActivity(intent);
//
//
//                } else {
//
//                    showLoginDialog();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//
//                Toast.makeText(getContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("grid", liveData.getLive_gridx());
//                params.put("user", loginname);
//                Log.i("user1", loginname);
//
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//
//
//    }

//    private void showLoginDialog() {
//        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout loginLayout = (LinearLayout) vi.inflate(R.layout.pwddialog, null);
//
//
//        final EditText pw = (EditText) loginLayout.findViewById(R.id.pw);
//
//        new AlertDialog.Builder(getContext()).setTitle("???????????? ??????").setView(loginLayout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                String pwdd = pw.getText().toString();
//                Log.i("pwdd1", pwdd);
//                Log.i("pwdd2", liveData.getLive_pwd());
//                if (Objects.equals(liveData.getLive_pwd(), pwdd)) {
//
//                    Intent intent = new Intent(getContext(), LiveActivity2.class);
//
//                    intent.putExtra("??????", liveData.getLive_token()); // ?????? ??? ??????
//                    intent.putExtra("?????????", liveData.getLive_agorachaname()); // ?????? ??? ??????
//                    intent.putExtra("??????or??????", 2); // ?????? ??? ??????
//                    startActivity(intent);
//
//
//                    Toast.makeText(getContext(), "@nPW : " + pw.getText().toString(), Toast.LENGTH_SHORT).show();
//
//                } else if (!Objects.equals(liveData.getLive_pwd(), pwdd)) {
//
//                    Toast.makeText(getContext(), "??????????????? ?????????????????????. ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).show();
//    }

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
                            Log.e("?????????idx", "1");
                            Log.e("?????????idx", idx);

                            String title = object.getString("title");
                            Log.e("?????????title", "2");
                            Log.e("?????????title", title);


                            String token = object.getString("token");
                            Log.e("?????????token", "3");
                            Log.e("?????????token", token);


                            String gridx = object.getString("roomnum");
                            Log.e("?????????gridx", "4??????");
                            Log.e("?????????gridx", gridx);


                            String writer = object.getString("user");
                            Log.e("?????????writer", "5");
                            Log.e("?????????writeruser", writer);
                            Log.e("user3", writer);

                            String url2 = object.getString("thumimage");
                            Log.e("?????????url2", "6");
                            Log.e("?????????url2", url2);

                            String chaname = object.getString("chaname");
                            Log.e("?????????chaname", "7");
                            Log.e("?????????chaname", chaname);


                            String Sonoff = object.getString("onoff");
                            Log.e("?????????", "8");
                            String Spwd = object.getString("pwd");
                            Log.e("?????????", "9");


                            String opendate = object.getString("opendate");
                            Log.e("?????????", "10");

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

                            liveData = new LiveData(idx, gridx, chaname, token, title, writer, opendate, urlimage, Sonoff, Spwd, live_gname);  //Data??? 6?????? ????????? ????????????????????? 4?????? ?????? ???????????????.
                            Log.e("?????????", "6");
                            livelist.add(liveData);
                            Log.e("?????????", "7");


                        }
                        liveAdapter = new LiveAdapter(getContext(), livelist); // ?????? context ??? ????????? getactivity??? ??????
                        Log.e("error?????????", "148");
                        recyclerView.setAdapter(liveAdapter);
                        Log.e("error?????????", "150");
                        liveAdapter.notifyDataSetChanged();
                        Log.e("?????????", "8");

                        Log.e("error?????????", "145");

                    }
                } catch (Exception e) {
                }
//                    Toast.makeText(getActivity(), "??????"+response, Toast.LENGTH_SHORT).show();
                Log.i("??????1", "??????"+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getContext(), error.getMessage()+"1", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"??????", Toast.LENGTH_SHORT).show();
                Log.i("??????2", "??????"+error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                String Sfrompage = String.valueOf(frompage);
                String Stopage = String.valueOf(topage);

                params.put("from", Sfrompage);
                params.put("to", Stopage);
                params.put("user", loginname);
                Log.i("user2", loginname);
                Log.i("??????Sfrompage", Sfrompage);
                Log.i("??????Stopage", Stopage);
                Log.i("??????Stopage", Stopage);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }

    @Override
    public void onBackPressed() {

    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private LiveFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final LiveFragment.ClickListener clickListener) {
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
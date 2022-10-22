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
            public void onClick(View view) { // 프레그먼트로 이동하는법 알아야함
                Intent intent = new Intent(keysearchActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                startActivity(intent);

            }
        });

        serchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sET.getText().toString().equals("") || sET.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {


                    key = sET.getText().toString();
                    Log.e("key값 들감?", key);


                    StringRequest request = new StringRequest(Request.Method.POST, urlsearch, new Response.Listener<String>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(String response) {


                            searchlist.clear();
                            Log.e("시작", "1");
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.e("시작", "2");
                                String success = jsonObject.getString("success");
                                Log.e("시작", "3");

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if ("data".equals("[]") || "data" == null) {
                                    Toast.makeText(getApplicationContext(), "결과가없습니다.", Toast.LENGTH_SHORT).show();
                                } else {

                                    Log.e("시작", "4");
                                    Log.e("total", response);

                                    if (success.equals("1")) {
                                        Log.e("시작", "5");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            Log.e("시작", "6");
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            Log.e("시작", "7");

                                            String id = object.getString("grid");
                                            Log.e("변수명", "1");
                                            String url2 = object.getString("grimage");
                                            Log.e("변수명", "2");
                                            String name = object.getString("grname");
                                            Log.e("변수명", "3");
                                            String disc = object.getString("grdisc");
                                            Log.e("변수명", "4");
                                            String maker = object.getString("gruserName");
                                            Log.e("변수명", "4");

                                            String onoff = object.getString("onoff");
                                            Log.e("변수명", "4");

                                            String groupwd = object.getString("groupwd");
                                            Log.e("변수명", "4");

                                            String opendate = object.getString("created_at");
                                            Log.e("변수명", "4");

                                                                        // String 의 Date화
                                                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                        StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// 필수 UTC의 값을 받아왔지만 다시한번 UTC타임존으로 포멧을 해줘야하는 아이러니함
                                                                        Date datedtime = StringtoDateFormat.parse(opendate);

                                                                        //UTC로 저장을 했지만 UTC로 다시 TIMEZONE을 성정 해주어야 정상적으로 출력된다.
                                                                        // 이유를 모르겠다.

                                                                        Log.i("987 소캣메세Date형태 utc재적용", String.valueOf(datedtime));


                                                                        //또한  아래에서 SimpleDateFormat dateFormatLocalization 에서 DATE를 한번더 포맷 고정을 해주어야 변화가 안생긴다.
                                                                        // 이유는 모르겠다
                                                                        // 아마도 Mysql이 KST 기준이라 그런건가 싶기도하고 잘 모르곘다
                                                                        // 여튼 2일동안 난리친걸 해결했으니 일단 넘어간다.
                                                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatLocalization = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//***/*
                                                                        Date utcDate = StringtoDateFormat.parse(dateFormatLocalization.format(datedtime));


                                                                        //타임존을 지정한 Date를 String 화 시킴
                                            //                            String ltime = StringtoDateFormat.format(datedtime);//이렇게 하면안되더라
                                                                        String ltime = dateFormatLocalization.format(datedtime); // 이렇게 타임존 설정이 안되어있는 포맷화를 한번더  되더라 그래서 위에 ***이를 살려둬야한다.
                                                                        System.out.println("987 지정 후 현지시간 적용후 스트링화 : " + ltime);
                                                                        Log.i("987 datedtime", String.valueOf(datedtime));
                                                                        Log.i("987 utcDate", String.valueOf(utcDate));

                                                                        Log.i("987 time", String.valueOf(opendate));
                                                                        Log.i("987 ltime", String.valueOf(ltime));





                                            String urlimage = "http://3.38.117.213/image/" + url2;

                                            Log.e("변수명", "5");

                                            searchData = new SearchData(id, urlimage, name, disc, maker, onoff, groupwd, ltime); //Data에 6개의 항목을 만들었기때문에 4개의 값을 가져와야함.
                                            Log.e("변수명", "6");
                                            searchlist.add(searchData);
                                            Log.e("변수명", "7");


                                            System.out.println(id);
                                            System.out.println(url2);
                                            System.out.println(name);
                                            Log.e("변수명", response);
                                        }

                                    }
                                    searchAdapter = new SearchAdapter(getApplicationContext(), searchlist); // 특이 context 가 아니라 getactivity로 받음
                                    Log.e("error변수명", "148");
                                    recyclerView.setAdapter(searchAdapter);
                                    Log.e("error변수명", "150");
                                    searchAdapter.notifyDataSetChanged();
                                    Log.e("변수명", "8");

                                    Log.e("error변수명", "145");

                                }

                            } catch (Exception ignored) {
                                Toast.makeText(getApplicationContext(), "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
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
                            //맵핑을 통해서 이미지, 이름, 내용, 작성자(shared로 받은값)을 보내줌

                            param.put("key", key);  //key값
                            Log.e("변수명", key);


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

                ////================ 클릭하는순간 '그룹번호' 라는 이름으로 그룹번호의 정보가 저장된다.
                SharedPreferences sp = getApplicationContext().getSharedPreferences("gridset", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("그룹번호", searchData.getGrid());
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
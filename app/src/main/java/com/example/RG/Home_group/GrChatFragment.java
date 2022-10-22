package com.example.RG.Home_group;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.example.RG.ChatService;
import com.example.RG.MainActivity;
import com.example.RG.OnBackPressedListener;
import com.example.RG.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class GrChatFragment extends Fragment implements OnBackPressedListener {
    private int overallYScroll = 0;
    public static Handler grChatHandler;
    public static RecyclerView grChatRecyclerView;
    public static ArrayList<GrChatData> grChatArrayList;
    public static GrChatAdapter grChatAdapter;
    static String 방번호;
    public SharedPreferences RoomNumfornoti;
    String 방이름;
    String 사용자이름;
    int viewType_num;
    GrChatData grChatData;
    String urlchat_message2 = "http://3.38.117.213/chat_messageget2.php";
    String urlchat_message3 = "http://3.38.117.213/chat_messageget3.php";
    String urldeletechatread = "http://3.38.117.213/deletechatread.php";
    Context mContext;
    EditText message;
    Button SendButton;
    SharedPreferences sp;

    boolean position_flag = true;


    int frompage = 0;
    int topage = 50;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //        프래그먼트를 생성할 때 호출합니다. 프래그먼트가 일시정지 혹은 중단 후 재개되었을 때 유지하고 있어야 하는 것을 여기서 초기화 해야합니다.
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gr_chat, container, false);

        grChatHandler = new Handler();
        sp = this.getActivity().getSharedPreferences("gridset", MODE_PRIVATE);
        방번호 = sp.getString("그룹번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        방이름 = sp.getString("그룹이름", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환


        sp = this.getActivity().getSharedPreferences("userName", Context.MODE_PRIVATE);
        사용자이름 = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환


        RoomNumfornoti = getContext().getSharedPreferences("RoomNumfornoti", MODE_PRIVATE);
        SharedPreferences.Editor editor = RoomNumfornoti.edit();
        editor.putString("room", 방번호); //값을저장시
        editor.apply();


        SharedPreferences GRCF용룸 = getContext().getSharedPreferences("GRCF용룸", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = GRCF용룸.edit();

        editor1.putString("room1", 방번호); //값을저장시

        editor1.apply();


        Log.i("그룹챗", 방번호);
        Log.i("그룹챗", 방이름);
        Log.i("그룹챗", 사용자이름);


        grChatRecyclerView = view.findViewById(R.id.grchat_rv);
        message = view.findViewById(R.id.grchat_rveditText);
        SendButton = view.findViewById(R.id.grchat_rvbutton);

        grChatArrayList = new ArrayList<>();
        grChatAdapter = new GrChatAdapter(mContext, grChatArrayList);
        grChatRecyclerView.setAdapter(grChatAdapter);


        // 리사이클러뷰 및 레이아웃  선언
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        grChatRecyclerView.setLayoutManager(layoutManager);

        DeletechatreadData();// 최초 진입시 채팅 목록 불러오기 HTTP 통신F

        buildListData();// 최초 진입시 채팅 목록 불러오기 HTTP 통신


        grChatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                overallYScroll = overallYScroll + dy;
                Log.i("check", "overall X  = " + overallYScroll);

                if(position_flag) {
                    if (overallYScroll <= -7000 ){
                        overallYScroll = 0;
                        Log.d("최상단",String.valueOf(position_flag));
                        Log.d("팅김","최상단");
                        Log.i("팅김 ", "3");

                        frompage= frompage+50;
                        buildListData2();
                        Log.i("팅김 ", "4");

                    }
                    position_flag = false;

                }
                else position_flag = true;
            }
        });
//                grChatRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (position_flag) {
//                    if ((!v.canScrollVertically(1))) {
//                        Log.d("최하단", String.valueOf(position_flag));
////                        Toast.makeText(getApplicationContext(), "스크롤의 최하단입니다.", Toast.LENGTH_SHORT).show();
//
//                    } else if ((!v.canScrollVertically(-1))) {
//                        Log.d("최상단", String.valueOf(position_flag));
//                        Log.d("팅김", "최상단");
//                        Log.i("팅김 ", "3");
//                        frompage = frompage + 50;
//                        buildListData2();
//                        Log.i("팅김 ", "4");
//                        Toast.makeText(getContext(), "스크롤의 최상단입니다.", Toast.LENGTH_SHORT).show();
//                    }
//
//                    position_flag = false;
//
//                } else position_flag = true;
//
//
//            }
//        });


        SendButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                Date nowDatenow = new Date();
                System.out.println("321메세지보냄포맷 지정 전 : " + nowDatenow);

                SimpleDateFormat simpleDateFormatnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                //원하는 데이터 포맷 지정
                String strNowDatenow = simpleDateFormatnow.format(nowDatenow);
                //채팅액티비티에서 바로  리사이클러뷰로 ADD 시킴
//                LocalTime localTime = LocalTime.now();
//                Log.i("tag 채팅 액티비티 시간", String.valueOf(localTime));
//                String time = localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // 현지시간을 "("HH:mm")) 형태로 변환시킴
                String contents = message.getText().toString();
                viewType_num = 2;
                GrChatData data = new GrChatData(사용자이름, 방번호, contents, viewType_num, strNowDatenow);
                grChatArrayList.add(data);
//-----------------------------------------------------------------------------------------------------


                //DB
                Date nowDate = new Date();
                System.out.println("321메세지보냄포맷 지정 전 : " + nowDate);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                //원하는 데이터 포맷 지정
                String strNowDate = simpleDateFormat.format(nowDate);
                //지정한 포맷으로 변환
                System.out.println("321포맷 지정 후 스트링화 : " + strNowDate);


                SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat utcDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                utcDateFormat2.setTimeZone(TimeZone.getDefault());


                Date localDate = null;
                try {
                    localDate = localDateFormat.parse(strNowDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date utcDate = null;
                try {
                    utcDate = localDateFormat.parse(utcDateFormat.format(localDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date utcDate2 = null;
                try {
                    utcDate2 = localDateFormat.parse(utcDateFormat2.format(localDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String finaltime = simpleDateFormat.format(utcDate);
                String qwe2 = simpleDateFormat.format(utcDate2);
                System.out.println("321포맷 지정 후 UTC 적용후 스트링화 : " + finaltime);
                System.out.println("321포맷 지정 후 UTC 적용후 스트링화 : " + qwe2);

                System.out.println(localDate);
                System.out.println(utcDate);
                System.out.println("1  " + finaltime);
                System.out.println("2  " + qwe2);

//----------------------------------------------------------------------------------------------
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Log.i("꺼졌을때 작동하냐???1 ", "1");
                            ChatService.jsonObject.put("message", contents);
                            Log.i("꺼졌을때 작동하냐???1 ", "2");
                            ChatService.jsonObject.put("nickName", 사용자이름);
                            Log.i("꺼졌을때 작동하냐???1 ", "3");
                            ChatService.jsonObject.put("roomnumber", 방번호);
                            ChatService.jsonObject.put("roomname", 방이름);
                            ChatService.jsonObject.put("intime", finaltime);
                            Log.i("꺼졌을때 작동하냐???1 ", "4");
                            ChatService.sendWriter.println(ChatService.jsonObject);
                            ChatService.sendWriter.flush();
                            message.setText("");

                            Log.i("꺼졌을때 작동하냐???1 ", "1");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                Log.i("tag 채팅 액티비티 메세지보내기", "9");

//                mAdapter.notifyItemInserted(mArrayList.size() - 1);
                grChatRecyclerView.scrollToPosition(grChatArrayList.size() - 1);

            }

        });


        return view;

    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
        startActivity(intent);


    }

    public void buildListData() {

        StringRequest request = new StringRequest(Request.Method.POST, urlchat_message2, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {


                grChatArrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("팅1", response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");


                    if (success.equals("1")) {

                        Log.i("팅1", response);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            String nickname = object.getString("nickname");
                            String contents = object.getString("contents");
                            String time = object.getString("cmtime");


                            // String 의 Date화
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// 필수 UTC의 값을 받아왔지만 다시한번 UTC타임존으로 포멧을 해줘야하는 아이러니함
                            Date datedtime = StringtoDateFormat.parse(time);

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

                            Log.i("987 time", String.valueOf(time));
                            Log.i("987 ltime", String.valueOf(ltime));


//                            String printtime = ltime.substring(11, 16); // 시간 줄이는것


                            if (nickname.equals(사용자이름)) {
                                viewType_num = 2;
                            } else {
                                viewType_num = 1;
                            }


                            grChatData = new GrChatData(nickname, 방번호, contents, viewType_num, ltime); //Data에 4개의 항목을 만들었기때문에 개의 값을 가져와야함.

                            grChatArrayList.add(0, grChatData);


                        }
                        grChatAdapter = new GrChatAdapter(getContext(), grChatArrayList); // 특이 context 가 아니라 getactivity로 받음

                        grChatRecyclerView.setAdapter(grChatAdapter);

//                        mAdapter.notifyDataSetChanged();
                        grChatRecyclerView.scrollToPosition(grChatArrayList.size() - 1); // // 새로운 메세지가 올경우 가장 스크롤을 제일 하단으로 내려줌


                    }
                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("roomnumber", 방번호);
                params.put("username", 사용자이름);
                String Sfrompage = String.valueOf(frompage);
                String Stopage = String.valueOf(topage);
                Log.i("팅1tStopagee", Sfrompage);
                Log.i("팅1topage", Stopage);
                params.put("from", Sfrompage);
                params.put("to", Stopage);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }

    public void buildListData2() {

        StringRequest request = new StringRequest(Request.Method.POST, urlchat_message3, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {


//                mArrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");


                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            String nickname = object.getString("nickname");
                            String contents = object.getString("contents");
                            String time = object.getString("cmtime");


                            // String 의 Date화
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// 필수 UTC의 값을 받아왔지만 다시한번 UTC타임존으로 포멧을 해줘야하는 아이러니함
                            Date datedtime = StringtoDateFormat.parse(time);

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

                            Log.i("987 time", String.valueOf(time));
                            Log.i("987 ltime", String.valueOf(ltime));


                            String printtime = ltime.substring(11, 16); // 시간 줄이는것


                            if (nickname.equals(사용자이름)) {
                                viewType_num = 2;
                            } else {
                                viewType_num = 1;
                            }


                            grChatData = new GrChatData(nickname, 방번호, contents, viewType_num, ltime); //Data에 4개의 항목을 만들었기때문에 개의 값을 가져와야함.

                            grChatArrayList.add(0, grChatData);


                        }
                        grChatAdapter = new GrChatAdapter(getContext(), grChatArrayList); // 특이 context 가 아니라 getactivity로 받음

                        grChatRecyclerView.setAdapter(grChatAdapter);

//                        mAdapter.notifyDataSetChanged();
                        grChatRecyclerView.scrollToPosition(jsonArray.length()); // // 새로운 메세지가 올경우 가장 스크롤을 제일 하단으로 내려줌


                    }
                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("roomnumber", 방번호);
                params.put("username", 사용자이름);
                String Sfrompage = String.valueOf(frompage);
                String Stopage = String.valueOf(topage);

                Log.i("팅tStopagee", Sfrompage);
                Log.i("팅topage", Stopage);
                params.put("from", Sfrompage);
                params.put("to", Stopage);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }


    @Override
    public void onPause() {
        super.onPause();
        DeletechatreadData();// 최초 진입시 채팅 목록 불러오기 HTTP 통신


        SharedPreferences.Editor editor = RoomNumfornoti.edit();
        Log.i("GrChat", "OnPause");
        editor.remove("room");//값을삭제시
        editor.apply();


    }

    @Override
    public void onStop() {
        super.onStop();
        DeletechatreadData();// 최초 진입시 채팅 목록 불러오기 HTTP 통신

        SharedPreferences.Editor editor = RoomNumfornoti.edit();
        Log.i("GrChat", "onStop");
        editor.remove("room");//값을삭제시
        editor.apply();

    }

    private void DeletechatreadData() {
        StringRequest request = new StringRequest(Request.Method.POST, urldeletechatread, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                Log.e("채팅방목록 메세지숫자 사라짐 ", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("roomnumber", 방번호);
                params.put("username", 사용자이름);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static class GetGrChatMSG implements Runnable {

        private final String nick;
        private final String room;
        private final String text;
        private final int viewType_num;
        private final String time;


        public GetGrChatMSG(String nick, String room, String text, int viewType_num, String time) {
            this.nick = nick;
            this.room = room;
            this.text = text;
            this.viewType_num = viewType_num;
            this.time = time;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {


            GrChatData data = new GrChatData(nick, room, text, viewType_num, time);
            Log.i("tag 채팅 nick: ", nick);
            Log.i("tag 채팅 room: ", room);
            Log.i("tag 채팅 text: ", text);
            Log.i("tag 채팅 viewtype: ", String.valueOf(viewType_num));
            Log.i("tag 채팅 time: ", time);


            if (room.equals(방번호)) {
                grChatArrayList.add(data);
                Log.i("tag 채팅 액티비티 메세지 수령", "동일한 방이니 리사이클러뷰추갛ㅂ니다. ");
                grChatRecyclerView.scrollToPosition(grChatArrayList.size() - 1); // 새로운 메세지가 올경우 가장 스크롤을 제일 하단으로 내려줌

            } else {
                Log.i("tag 채팅 액티비티 메세지 수령", "방이다르면 안보낸다~ ");
            }


        }


    }


}
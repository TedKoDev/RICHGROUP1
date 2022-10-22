package com.example.RG.mainfragment;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.RG.ChatroomAdapterData.ChatroomAdapter;
import com.example.RG.ChatroomAdapterData.ChatroomDATA;
import com.example.RG.Chatting.ChatActivity;
import com.example.RG.Chatting.Data;
import com.example.RG.Home_group.GrPostFragment;
import com.example.RG.MainActivity;
import com.example.RG.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ChatFragment extends Fragment {

    public static Handler chatFragHandler;

    public  Context context;

    public static ChatroomAdapter chatroomAdapter;

    TextView loginemail, grname;

    public static TextView textView;
    ImageView grpback;
    //    String urlchatroom = "http://3.38.117.213/chatroomget.php";
    static String urlchatroom = "http://3.38.117.213/chatroomget2.php";
    String grsetnum; // 상단 그룹넘버
    static String loginname;  //상단 로그인유저 닉네임
    public static RecyclerView chatrecyclerView;
    static ChatroomDATA chatroomDATA;
    private static ArrayList<ChatroomDATA> Chatroomlist = new ArrayList<>();

    NestedScrollView scrollView;

   int frompage2 = 0;
int topage2 = 10;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);


        //            imageview
        grpback = view.findViewById(R.id.grpback);
        chatFragHandler = new Handler();

        textView = view.findViewById(R.id.ttt);

        textView.setText("12312312313");

        loginemail = view.findViewById(R.id.loginemail);  // shared로 로그인email 받기
        grname = view.findViewById(R.id.grnum);

        SharedPreferences sp = this.getActivity().getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        loginemail.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값
        chatrecyclerView = view.findViewById(R.id.chatrecy);
        scrollView = view.findViewById(R.id.chatscroll_view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        chatrecyclerView.setLayoutManager(layoutManager);



        buildListData();

        Log.i("페이징 되냐", "1");
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    topage2 = topage2 + 10;
                    buildListData();




                }

            }
        });
//        initRecyclerView(view);
        chatrecyclerView.addOnItemTouchListener(new GrPostFragment.RecyclerTouchListener(getActivity(), chatrecyclerView, new GrPostFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                chatroomDATA = Chatroomlist.get(position);

                Intent intent = new Intent(getContext(), ChatActivity.class);
////================채팅방을 클릭하는순간  '채팅방번호','채팅방명' 라는 이름으로 그룹번호의 정보가 저장된다.
                SharedPreferences sp = getContext().getSharedPreferences("chatroomidx", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("채팅방번호", chatroomDATA.getChroom_idx());
                editor.putString("채팅방명", chatroomDATA.getChroom_name());
                editor.apply();
////===================================================



                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


        return view;


    }

    private void buildListData() {

        StringRequest request = new StringRequest(Request.Method.POST, urlchatroom, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                Log.e("respone", "5555");


                Chatroomlist.clear();
                Log.e("시작", "1");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("시작", "412e12e");
                    Log.e("total", response);

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);


                            String groupidx = object.getString("gridx");
                            String url2 = object.getString("grimg");
                            String chatroomname = object.getString("crname");
                            String nick = object.getString("nickName");
                            String contents = object.getString("contents");
                            String cmtime = object.getString("cmtime");
                            String msgcount = object.getString("msgcount");





                            String chatroomimg = "http://3.38.117.213/image/" + url2;
                            Log.e("변수명", "5");


                            // String 의 Date화
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// 필수 UTC의 값을 받아왔지만 다시한번 UTC타임존으로 포멧을 해줘야하는 아이러니함
                            Date datedtime = StringtoDateFormat.parse(cmtime);

                            //UTC로 저장을 했지만 UTC로 다시 TIMEZONE을 성정 해주어야 정상적으로 출력된다.
                            // 이유를 모르겠다.

                            Log.i(  "987 소캣메세Date형태 utc재적용", String.valueOf(datedtime));


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
                            Log.i(  "987 datedtime", String.valueOf(datedtime));
                            Log.i(  "987 utcDate", String.valueOf(utcDate));

                            Log.i(  "987 time", String.valueOf(cmtime));
                            Log.i(  "987 ltime", String.valueOf(ltime));






                            chatroomDATA = new ChatroomDATA(groupidx, chatroomname, chatroomimg, nick, contents, ltime, msgcount); //Data에 3개의 항목을 만들었기때문에 개의 값을 가져와야함.
                            Log.e("변수명", "6");
                            Chatroomlist.add(chatroomDATA);
                            Log.e("변수명", "7");


                            Log.e("변수명", response);


                        }
                        chatroomAdapter = new ChatroomAdapter(getContext(), Chatroomlist); // 특이 context 가 아니라 getactivity로 받음

                        chatrecyclerView.setAdapter(chatroomAdapter);

                        chatroomAdapter.notifyDataSetChanged();
                        Log.e("변수명", "8");

                        Log.e("error변수명", "145");

                    }
                } catch (Exception e) {
                }

//                    Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

      //      Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("cr_joiner", loginname);

                String Sfrompage = String.valueOf(frompage2);
                String Stopage = String.valueOf(topage2);

                params.put("from", Sfrompage);
                params.put("to", Stopage);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }



    public static class rvupdate implements Runnable {

        private final String nick;
        private final String room;
        private final String text;
        private final int viewType_num;
        private final String time;

        int i;
        public rvupdate(String nick, String room, String text, int viewType_num, String time) {
            this.nick = nick;
            this.room = room;
            this.text = text;
            this.viewType_num = viewType_num;
            this.time = time;

        }

        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
        @Override
        public final void run() {


            Log.i("tag 채팅방 목록2", "들어옴22 ");



                textView.setText("핸들러가 작동해서 넘어옴"+ text);

            Log.i("tag 채팅방 목록3", "들어옴33 "+i);
            StringRequest request = new StringRequest(Request.Method.POST, urlchatroom, new Response.Listener<String>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(String response) {

                    Log.e("respone", "5555");


                    Chatroomlist.clear();
                    Log.e("시작", "1");
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String success = jsonObject.getString("success");

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Log.e("시작", "412e12e");
                        Log.e("total", response);

                        if (success.equals("1")) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);


                                String groupidx = object.getString("gridx");
                                String url2 = object.getString("grimg");
                                String chatroomname = object.getString("crname");
                                String nick = object.getString("nickName");
                                String contents = object.getString("contents");
                                String cmtime = object.getString("cmtime");
                                String msgcount = object.getString("msgcount");





                                String chatroomimg = "http://3.38.117.213/image/" + url2;
                                Log.e("변수명", "5");


                                // String 의 Date화
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// 필수 UTC의 값을 받아왔지만 다시한번 UTC타임존으로 포멧을 해줘야하는 아이러니함
                                Date datedtime = StringtoDateFormat.parse(cmtime);

                                //UTC로 저장을 했지만 UTC로 다시 TIMEZONE을 성정 해주어야 정상적으로 출력된다.
                                // 이유를 모르겠다.

                                Log.i(  "987 소캣메세Date형태 utc재적용", String.valueOf(datedtime));


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
                                Log.i(  "987 datedtime", String.valueOf(datedtime));
                                Log.i(  "987 utcDate", String.valueOf(utcDate));

                                Log.i(  "987 time", String.valueOf(cmtime));
                                Log.i(  "987 ltime", String.valueOf(ltime));






                                chatroomDATA = new ChatroomDATA(groupidx, chatroomname, chatroomimg, nick, contents, ltime, msgcount); //Data에 3개의 항목을 만들었기때문에 개의 값을 가져와야함.
                                Log.e("변수명", "6");
                                Chatroomlist.add(chatroomDATA);
                                Log.e("변수명", "7");


                                Log.e("변수명", response);


                            }
                            chatroomAdapter = new ChatroomAdapter(chatrecyclerView.getContext(), Chatroomlist); // 특이 context 가 아니라 getactivity로 받음

                            chatrecyclerView.setAdapter(chatroomAdapter);

                            chatroomAdapter.notifyDataSetChanged();
                            Log.e("변수명", "8");

                            Log.e("error변수명", "145");

                        }
                    } catch (Exception e) {
                    }



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {


                    Map<String, String> params = new HashMap<>();
                    params.put("cr_joiner", loginname);



                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(chatrecyclerView.getContext());
            requestQueue.add(request);

        }
    }

//    public static class updaterv implements Runnable {
//
//
//        public updaterv() {
//            Log.i("tag 채팅방 목록+1", "들어옴 ");
//        }
//
//        @SuppressLint("NotifyDataSetChanged")
//        @Override
//        public void run() {
//            Log.i("tag 채팅방 목록2", "들어옴22 ");
//            chatrecyclerView.setAdapter(chatroomAdapter);
//            Log.i("tag 채팅방 목록3", "들어옴33 ");
//            chatroomAdapter.notifyDataSetChanged();
////            .scrollToPosition(mArrayList.size() - 1);
//
////                mArrayList.add(data);
//
////
////                mRecyclerView.scrollToPosition(mArrayList.size() - 1); // 새로운 메세지가 올경우 가장 스크롤을 제일 하단으로 내려줌
////
//
//        }
//    }

@Override
public void onDestroyView() {
    super.onDestroyView();


}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buildListData();

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        buildListData();

    }

    @Override
    public void onStart() {
        super.onStart();
        buildListData();
    }

    @Override
    public void onStop() {
        super.onStop();
        buildListData();
    }

    @Override
    public void onPause() {
        super.onPause();
        buildListData();
    }

    @Override
    public void onResume() {
        super.onResume();

        buildListData();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        chatrecyclerView.setLayoutManager(layoutManager);
    }
}
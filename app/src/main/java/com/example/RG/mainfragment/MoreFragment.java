package com.example.RG.mainfragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.RG.Home_group.GroupActivity;
import com.example.RG.More.MoreAdapter;
import com.example.RG.More.MoreData;
import com.example.RG.More.MoreMyAccount;
import com.example.RG.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MoreFragment extends Fragment {


    TextView inlogname;
    String loginname, loginEmail;

    TextView MoreNick, MoreEmail, Moretotalvalue, Moretotaladdpercent, Moretotalinvest, Moretotalprofit;
    RecyclerView MoreRV;
    ImageView MoreSet;

    SharedPreferences sp;


    String url = "http://3.38.117.213/RG3moregroupget.php";
    MoreData moreData;
    private ArrayList<MoreData> morelist = new ArrayList<>();
    private MoreAdapter moreAdapter;


    NestedScrollView scrollView;

    private int frompage = 0;
    private int topage = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);


        inlogname = view.findViewById(R.id.inlogname); //로그인 상태의 닉네임


        MoreNick = view.findViewById(R.id.MoreNick); //닉네임
        MoreEmail = view.findViewById(R.id.MoreEmail);// 이메일
        Moretotalvalue = view.findViewById(R.id.Moretotalvalue); // 총보유자산
        Moretotaladdpercent = view.findViewById(R.id.Moretotaladdpercent); // 수익률
        Moretotalinvest = view.findViewById(R.id.Moretotalinvest);// 투자금
        Moretotalprofit = view.findViewById(R.id.Moretotalprofit);// 평가손익
        MoreRV = view.findViewById(R.id.MoreRV);// 리사이클러뷰
        MoreSet = view.findViewById(R.id.MoreSet);// 이미지버튼 세팅버튼


        sp = this.getActivity().getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        loginEmail = sp.getString("user_email", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        Log.i("loginEmail", loginEmail);
        inlogname.setText(loginname); // 로그인시에 저장한 id값
        MoreNick.setText(loginname); // 로그인시에 저장한 id값
        MoreEmail.setText(loginEmail); // 유저 Email

        scrollView = view.findViewById(R.id.morescroll_view);
        MoreRV = view.findViewById(R.id.MoreRV);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        MoreRV.setLayoutManager(layoutManager);



        //투자내역=================================================================
        sp = getContext().getSharedPreferences("투자내역", MODE_PRIVATE);
        String 총보유 = sp.getString("총보유", "0"); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        String 수익률 = sp.getString("수익률", "0"); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        String 투자금 = sp.getString("투자금", "0"); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        String 평가손익 = sp.getString("평가손익", "0"); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환

        Log.e(  "투자12총보유", 총보유);
        Log.e(  "투자12수익률", 수익률);
        Log.e(  "투자12투자금", 투자금);
        Log.e(  "투자12평가손익", 평가손익);



        Moretotalvalue.setText(총보유);
        Moretotaladdpercent.setText(수익률 + "%");

        Moretotalinvest.setText(투자금);
        Moretotalprofit.setText(평가손익);
        //투자내역=================================================================끝

        //참여중 모임 리스트 ======
        calllist();
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    topage = topage + 10;
                    calllist();




                }

            }
        });

        MoreRV.addOnItemTouchListener(new MoreFragment.RecyclerTouchListener(getContext(), MoreRV, new MoreFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                moreData = morelist.get(position);


                Intent intent = new Intent(getContext(), GroupActivity.class);

                ////================ 클릭하는순간 '그룹번호' 라는 이름으로 그룹번호의 정보가 저장된다.
                SharedPreferences sp = getContext().getSharedPreferences("gridset", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("그룹번호", moreData.getGrid());
                editor.apply();
////=============================================================================================================

                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));




        MoreSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), MoreMyAccount.class);

                startActivity(intent);
            }
        });




        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //투자내역=================================================================
        sp = getContext().getSharedPreferences("투자내역", MODE_PRIVATE);
        String 총보유 = sp.getString("총보유", "0"); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        String 수익률 = sp.getString("수익률", "0"); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        String 투자금 = sp.getString("투자금", "0"); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        String 평가손익 = sp.getString("평가손익", "0"); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환

        Log.e(  "투자12총보유", 총보유);
        Log.e(  "투자12수익률", 수익률);
        Log.e(  "투자12투자금", 투자금);
        Log.e(  "투자12평가손익", 평가손익);
        Moretotalvalue.setText(총보유);
        Moretotaladdpercent.setText(수익률 + "%");

        Moretotalinvest.setText(투자금);
        Moretotalprofit.setText(평가손익);
        calllist();
    }

    private void calllist() {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {


                morelist.clear();
                Log.e("시작", "1");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("시작", "2");
                    String success = jsonObject.getString("success");
                    Log.e("시작", "3");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
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
                            String opendate = object.getString("created_at");
                            Log.e("변수명", "4");


                            String urlimage = "http://3.38.117.213/image/" + url2;
                            Log.e("변수명", "5");

                            moreData = new MoreData(id, urlimage, name, disc, maker, opendate); //Data에 6개의 항목을 만들었기때문에 4개의 값을 가져와야함.
                            Log.e("변수명", "6");
                            morelist.add(moreData);
                            Log.e("변수명", "7");


                            System.out.println(id);
                            System.out.println(url2);
                            System.out.println(name);
                            Log.e("변수명", response);


                        }
                        moreAdapter = new MoreAdapter(getContext(), morelist); // 특이 context 가 아니라 getactivity로 받음
                        Log.e("error변수명", "148");
                        MoreRV.setAdapter(moreAdapter);
                        Log.e("error변수명", "150");
                        moreAdapter.notifyDataSetChanged();
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

//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> param = new HashMap<String, String>();
                //맵핑을 통해서 이미지, 이름, 내용, 작성자(shared로 받은값)을 보내줌

                param.put("gruserName", loginname);
                Log.e("home mapp", "1");

                String Sfrompage = String.valueOf(frompage);
                String Stopage = String.valueOf(topage);

                param.put("from", Sfrompage);
                param.put("to", Stopage);


                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MoreFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MoreFragment.ClickListener clickListener) {
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
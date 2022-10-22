package com.example.RG.mainfragment;


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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.RG.ADD.AddGroupActivity1;
import com.example.RG.Home_group.GroupActivity;
import com.example.RG.MainActivity;
import com.example.RG.R;
import com.example.RG.homeadapter.HomeAdapter;
import com.example.RG.homeadapter.HomeData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {


    String url = "http://3.38.117.213/RG3groupgetimage.php";
    String urll = "http://3.38.117.213/RG3groupgetimage2.php";
    SharedPreferences sp;
    String loginname;
    TextView logname;


    RecyclerView homerecyclerView;
    HomeData homeData;
    GridLayoutManager gridLayoutManager;
    private ArrayList<HomeData> imagelist = new ArrayList<>();
    private HomeAdapter homeAdapter;


    NestedScrollView scrollView;
    ProgressBar progress_circular;

    int frompage = 0;
    int topage = 18;
    private int overallYScroll = 0;

    boolean position_flag = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        프래그먼트를 생성할 때 호출합니다. 프래그먼트가 일시정지 혹은 중단 후 재개되었을 때 유지하고 있어야 하는 것을 여기서 초기화 해야합니다.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // Inflate the layout for this fragment
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        프래그먼트가 자신의 인터페이스를 처음 그리기 위해 호출합니다. View를 반환해야 합니다. 이 메서드는 프래그먼트의 레이아웃 루트이기 때문에 UI를 제공하지 않는 경우에는 null을 반환하면 됩니다.
//        onCreagteView()를 통해 반환된 View 객체는 onViewCreated()의 파라미터로 전달 된다.
//        이 때 Lifecycle이 INITIALIZED 상태로 업데이트가 됨
//        때문에 View의 초기값 설정, LiveData 옵저빙, RecyclerView, ViewPager2에 사용될 Adapter 세팅은 이 메소드에서 해주는 것이 적절함

        Button homeaddbtn1 = view.findViewById(R.id.homeaddbtn1);

        homeaddbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddGroupActivity1.class);

                startActivity(intent);
            }
        });
        logname = view.findViewById(R.id.logname);
        sp = this.getActivity().getSharedPreferences("userName", Context.MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값

        scrollView = view.findViewById(R.id.homescroll_view);
        homerecyclerView = view.findViewById(R.id.homgrecyclerview);




        buildListData();




//        homerecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                overallYScroll = overallYScroll + dy;
//                Log.i("check", "overall X  = " + overallYScroll);
//
//                if(position_flag) {
//                if (overallYScroll >= 1350 ){
//                overallYScroll = 0;
//                Log.d("최상단",String.valueOf(position_flag));
//                Log.d("팅김","최상단");
//                Log.i("팅김 ", "3");
//
//                    topage = topage + 12;
//                    buildListData();
//
//                    Log.i("팅김 ", "4");
//
//                }
//                position_flag = false;
//
//                }
//                else position_flag = true;
//                }
//                });
////
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    topage = topage + 12;
                    buildListData();


                }

            }
        });










        homerecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), homerecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                homeData = imagelist.get(position);

//                if ( homeData.getGrpwd()!= null ){
////
////                    showLoginDialog();
//
//                }else {

                    Intent intent = new Intent(getContext(), GroupActivity.class);

//                intent.putExtra("grid", homeData.getGrid());

////================bottomnavigation 3곳중 어느곳이든 클릭하는순간 '그룹번호' 라는 이름으로 그룹번호의 정보가 저장된다.
                    SharedPreferences sp = getContext().getSharedPreferences("gridset", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("그룹번호", homeData.getGrid());
                    editor.putString("그룹이름", homeData.getGrname());
                    editor.apply();
////=============================================================================================================


                    startActivity(intent);
                }

//            }

            @Override
            public void onLongClick(View view, int position) {



            }
        }));

    }

//    private void showLoginDialog() {
//        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout loginLayout = (LinearLayout) vi.inflate(R.layout.pwddialog, null);
//
//
//        final EditText pw = (EditText)loginLayout.findViewById(R.id.pw);
//
//        new AlertDialog.Builder(getContext()).setTitle("비밀번호 확인").setView(loginLayout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                String pwdd = pw.getText().toString();
//
//                if(homeData.getGrpwd() == pwdd) {
//
//                    Intent intent = new Intent(getContext(), GroupActivity.class);
//
////                intent.putExtra("grid", homeData.getGrid());
//
//////================bottomnavigation 3곳중 어느곳이든 클릭하는순간 '그룹번호' 라는 이름으로 그룹번호의 정보가 저장된다.
//                    SharedPreferences sp = getContext().getSharedPreferences("gridset", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("그룹번호", homeData.getGrid());
//                    editor.putString("그룹이름", homeData.getGrname());
//                    editor.apply();
//////=============================================================================================================
//
//
//                    startActivity(intent);
//
//
//                    Toast.makeText(getContext(), "@nPW : " + pw.getText().toString(), Toast.LENGTH_SHORT).show();
//
//                }else{
//
//                    Toast.makeText(getContext(), "비밀번호가 잘못되었습니다. " , Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).show();
//    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
//        액티비티가 시작됨 상태에 들어가면 이 메서드를 호출합니다. 사용자에게 프래그먼트가 보이게 되고,
//        이 메서드에서 UI를 관리하는 코드를 초기화 합니다. 이 메서드는 매우 빠르게 완료되고, 완료되면 Resumed(재개)상태로 들어가 onResume() 메서드를 호출합니다.
    }

    @Override
    public void onResume() {
        super.onResume();

        buildListData();
        gridLayoutManager
                = new GridLayoutManager(getContext(), 3);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        gridLayoutManager.setOrientation(gridLayoutManager.HORIZONTAL);
        homerecyclerView.setLayoutManager(gridLayoutManager);
    }


    @Override
    public void onPause() {
        super.onPause();
//        사용자가 프래그먼트를 떠나면 첫번 째로 이 메서드를 호출합니다. 사용자가 돌아오지 않을 수도 있으므로 여기에 현재 사용자 세션을 넘어 지속되어야 하는 변경사항을 저장합니다.
    }

    @Override
    public void onStop() {
        super.onStop();
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

    private void initRecyclerView(View view) {
        homerecyclerView = view.findViewById(R.id.homgrecyclerview);
        gridLayoutManager
                = new GridLayoutManager(getContext(), 3);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        gridLayoutManager.setOrientation(gridLayoutManager.HORIZONTAL);
        homerecyclerView.setLayoutManager(gridLayoutManager);

    }

    private void buildListData() {

        StringRequest request = new StringRequest(Request.Method.POST, urll, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {


                imagelist.clear();
                Log.e("시작", "1");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("home", "2");
                    String success = jsonObject.getString("success");
                    Log.e("home", "3");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("home", "4");

                    if (success.equals("1")) {
                        Log.e("home", "5");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.e("home", "6");
                            JSONObject object = jsonArray.getJSONObject(i);
                            Log.e("home", "7");

                            String id = object.getString("grid");
                            Log.e("home", "1");
                            String url2 = object.getString("grimage");
                            Log.e("home", "2");
                            String name = object.getString("grname");
                            Log.e("home", "3");
//                            String info = object.getString("grdisc"); Log.e(  "변수명", "4");

                            String pwd = object.getString("grpwd");
                            Log.e("home", "3");

                            String urlimage = "http://3.38.117.213/image/" + url2;
                            Log.e("변수명", "5");

                            homeData = new HomeData(id, urlimage, name,pwd); //Data에 4개의 항목을 만들었기때문에 4개의 값을 가져와야함.
                            Log.e("변수명", "6");
                            imagelist.add(homeData);
                            Log.e("변수명", "7");


                            System.out.println(id);
                            System.out.println(url2);
                            System.out.println(name);
                            Log.e("변수명", response);


                        }
                        homeAdapter = new HomeAdapter(getContext(), imagelist); // 특이 context 가 아니라 getactivity로 받음
                        Log.e("home", "148");
                        homerecyclerView.setAdapter(homeAdapter);
                        Log.e("home", "150");
                        homeAdapter.notifyDataSetChanged();
                        Log.e("home", "8");

                        Log.e("home", "145");

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
        private HomeFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeFragment.ClickListener clickListener) {
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
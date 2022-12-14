package com.example.RG.mainfragment;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import com.example.RG.OnBackPressedListener;
import com.example.RG.R;
import com.example.RG.SearchAdapter.SearchAdapter;
import com.example.RG.SearchAdapter.SearchData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;


public class SearchFragment extends Fragment implements OnBackPressedListener {


    String url = "http://3.38.117.213/RG3searchgroupget.php";
    String urlsearch = "http://3.38.117.213/searchget.php";


    RecyclerView recyclerView;
    SearchData searchData;
    EditText serchET;
    Button serchbtn;
    Button keyword1, keyword2, keyword3, keyword4, keyword5, keyword6;
    String key, Setkeyword;
    ProgressBar progress_circular;
    NestedScrollView scrollView;
    private ArrayList<SearchData> searchlist = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private int frompage = 0;
    private int topage = 10;
    String loginname;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        SharedPreferences sp = this.getActivity().getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key?????? ????????? ?????? ???????????????, ???????????????""(??????)??? ??????



        serchET = view.findViewById(R.id.serchET);
        progress_circular = view.findViewById(R.id.progress_circular);

//        Setkeyword = serchET.getText().toString();

        serchET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ?????????_?????????????????? ??????
                Intent intent = new Intent(getContext(), keysearchActivity.class); //???????????? ??????

                startActivity(intent);
            }
        });


//

//
//

        scrollView = view.findViewById(R.id.scroll_view);
        recyclerView = view.findViewById(R.id.serchrecyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        buildListData();

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    topage = topage + 10;
                    buildListData();
                    progress_circular.setVisibility(View.VISIBLE);


                }
                progress_circular.setVisibility(View.GONE);
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                searchData = searchlist.get(position);




                Log.i("??????", searchData.getOnoff());
                if (!Objects.equals(searchData.getOnoff(), "??????")) {
                    checkinornot();//?????? ????????? ???????????? ????????? ??????


                } else {

                    Intent intent = new Intent(getContext(), GroupActivity.class);

                    ////================ ?????????????????? '????????????' ?????? ???????????? ??????????????? ????????? ????????????.
                    SharedPreferences sp = getContext().getSharedPreferences("gridset", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("????????????", searchData.getGrid());
                    editor.apply();
////=============================================================================================================


                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        return view;
    }

    private void checkinornot() {

        String url = "http://3.38.117.213/joinornot.php"; //


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String ??? = response;

                if (Objects.equals(???, "success")) {

                    Intent intent = new Intent(getContext(), GroupActivity.class);

                    ////================ ?????????????????? '????????????' ?????? ???????????? ??????????????? ????????? ????????????.
                    SharedPreferences sp = getContext().getSharedPreferences("gridset", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("????????????", searchData.getGrid());
                    editor.apply();
////=============================================================================================================


                    startActivity(intent);



                } else {

                    showLoginDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grid", searchData.getGrid());
                params.put("user",loginname );

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    private void showLoginDialog() {
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout loginLayout = (LinearLayout) vi.inflate(R.layout.pwddialog, null);


        final EditText pw = (EditText) loginLayout.findViewById(R.id.pw);

        new AlertDialog.Builder(getContext()).setTitle("???????????? ??????").setView(loginLayout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String pwdd = pw.getText().toString();
                Log.i("pwdd1", pwdd);
                Log.i("pwdd2", searchData.getGroupwd());
                if (Objects.equals(searchData.getGroupwd(), pwdd)) {

                    Intent intent = new Intent(getContext(), GroupActivity.class);

//                intent.putExtra("grid", homeData.getGrid());

////================bottomnavigation 3?????? ??????????????? ?????????????????? '????????????' ?????? ???????????? ??????????????? ????????? ????????????.
                    SharedPreferences sp = getContext().getSharedPreferences("gridset", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("????????????", searchData.getGrid());
                    editor.putString("????????????", searchData.getGrname());
                    editor.apply();
////=============================================================================================================


                    startActivity(intent);


//                    Toast.makeText(getContext(), "@nPW : " + pw.getText().toString(), Toast.LENGTH_SHORT).show();

                } else if (!Objects.equals(searchData.getGroupwd(), pwdd)) {

                    Toast.makeText(getContext(), "??????????????? ?????????????????????. ", Toast.LENGTH_SHORT).show();
                }
            }
        }).show();
    }


//    private void searching() {
//        StringRequest request = new StringRequest(Request.Method.POST, urlsearch, new Response.Listener<String>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(String response) {
//
//
//                searchlist.clear();
//                Log.e("??????", "1");
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Log.e("??????", "2");
//                    String success = jsonObject.getString("success");
//                    Log.e("??????", "3");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//                    Log.e("??????", "4");
//                    Log.e("total", response);
//
//                    if (success.equals("1")) {
//                        Log.e("??????", "5");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            Log.e("??????", "6");
//                            JSONObject object = jsonArray.getJSONObject(i);
//                            Log.e("??????", "7");
//
//                            String id = object.getString("grid");
//                            Log.e("?????????", "1");
//                            String url2 = object.getString("grimage");
//                            Log.e("?????????", "2");
//                            String name = object.getString("grname");
//                            Log.e("?????????", "3");
//                            String disc = object.getString("grdisc");
//                            Log.e("?????????", "4");
//                            String maker = object.getString("gruserName");
//                            Log.e("?????????", "4");
//                            String opendate = object.getString("created_at");
//                            Log.e("?????????", "4");
//
//
//                            String urlimage = "http://3.38.117.213/image/" + url2;
//                            Log.e("?????????", "5");
//
//                            searchData = new SearchData(id, urlimage, name, disc, maker, opendate); //Data??? 6?????? ????????? ????????????????????? 4?????? ?????? ???????????????.
//                            Log.e("?????????", "6");
//                            searchlist.add(searchData);
//                            Log.e("?????????", "7");
//
//
//                            System.out.println(id);
//                            System.out.println(url2);
//                            System.out.println(name);
//                            Log.e("?????????", response);
//
//
//                        }
//                        searchAdapter = new SearchAdapter(getContext(), searchlist); // ?????? context ??? ????????? getactivity??? ??????
//                        Log.e("error?????????", "148");
//                        recyclerView.setAdapter(searchAdapter);
//                        Log.e("error?????????", "150");
//                        searchAdapter.notifyDataSetChanged();
//                        Log.e("?????????", "8");
//
//                        Log.e("error?????????", "145");
//
//                    }
//                } catch (Exception e) {
//                }
//
////                    Toast.makeText(getActivity(), "??????", Toast.LENGTH_SHORT).show();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(request);
//
//
//    }


    @Override
    public void onResume() {
        super.onResume();
        buildListData();


    }

    private void initRecyclerView(View view) {


    }

    private void buildListData() {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                            Log.e("?????????url2", url2);
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
                        searchAdapter = new SearchAdapter(getContext(), searchlist); // ?????? context ??? ????????? getactivity??? ??????
                        Log.e("error?????????", "148");
                        recyclerView.setAdapter(searchAdapter);
                        Log.e("error?????????", "150");
                        searchAdapter.notifyDataSetChanged();
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

//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

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
        private SearchFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SearchFragment.ClickListener clickListener) {
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
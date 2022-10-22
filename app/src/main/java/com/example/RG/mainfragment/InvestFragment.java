package com.example.RG.mainfragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.RG.InvestAdapter.InvestAdapter;
import com.example.RG.InvestAdapter.InvestData;
import com.example.RG.More.MyValueFormatter;
import com.example.RG.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InvestFragment extends Fragment {


    String url = "http://3.38.117.213/investcall.php";
    String urlpage = "http://3.38.117.213/investcallpage.php";


    TextView totalvalue, totaladdpercent, totalinvest, totalprofit;
    PieChart pieChart;
    TextView stockpercent, coinpercent, landpercent, paperpercent, moneypercent, otherpercent;
    Button addbtn;
    RecyclerView investrecyclerview;
    LinearLayout portfolio;
    ImageView portshowornot1, portshowornot2;
    TextView logname;
    String loginname;
    InvestData investData;
    String Scoinpercent, Slandpercent, Smoneypercent, Spaperpercent, Sotherpercent;
    int 총투자금;
    int 총투자금1;
    private ArrayList<InvestData> investlist = new ArrayList<>();
    private ArrayList<InvestData> investlist2 = new ArrayList<>();
    private InvestAdapter investAdapter;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invest, container, false);


        //총수익률=========================================================
        totalvalue = view.findViewById(R.id.totalvalue);//총 보유자산
        totaladdpercent = view.findViewById(R.id.totaladdpercent); // 수익률
        totalinvest = view.findViewById(R.id.totalinvest);//투자금
        totalprofit = view.findViewById(R.id.totalprofit);//평가손익
        //==============================================================


        //보유자산포트폴리오=====================================================
        portfolio = view.findViewById(R.id.portfolio);//보유자산 포트폴리오 레이아웃
        portshowornot1 = view.findViewById(R.id.portshowornot1);//보유자산 보임숨김이미지(클릭지정시킬것)
        portshowornot2 = view.findViewById(R.id.portshowornot2);//보유자산 보임숨김이미지(클릭지정시킬것)


        pieChart = view.findViewById(R.id.piechart);//원형차트

        stockpercent = view.findViewById(R.id.stockpercent);//주식수익률
        coinpercent = view.findViewById(R.id.coinpercent);//코인수익률
        landpercent = view.findViewById(R.id.landpercent);//부동산 수익률
        moneypercent = view.findViewById(R.id.moneypercent);//외환 수익률
        paperpercent = view.findViewById(R.id.paperpercent);//채권 수익률
        otherpercent = view.findViewById(R.id.otherpercent);//기타 수익률
        //=====================================================================

        //투자 항목 리사이클러뷰 구간 ====================================================
        addbtn = view.findViewById(R.id.addbtn);// 항목추가버튼
        investrecyclerview = view.findViewById(R.id.investRV);//항목리사이클러뷰
        scrollView = view.findViewById(R.id.investscroll_view);
        //============================================================================

        logname = view.findViewById(R.id.inlogname); // 사용자이름


        //유저네임을 사용하는 쉐어드 =====================================================
        SharedPreferences sp = this.getActivity().getSharedPreferences("userName", MODE_PRIVATE);
        loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값
        //============================================================================

        //보유자산 포트폴리오  SHowing 여부 ===========================================================
        portshowornot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portfolio.setVisibility(View.GONE);
                portshowornot1.setVisibility(View.GONE);
                portshowornot2.setVisibility(View.VISIBLE);
            }
        });
        portshowornot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portfolio.setVisibility(View.VISIBLE);
                portshowornot2.setVisibility(View.GONE);
                portshowornot1.setVisibility(View.VISIBLE);
            }
        });
        //===========================================================================

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 값입력_액티비티에서 사용
                Intent intent = new Intent(getContext(), InvestAdd.class); //액티비티 전환
                startActivity(intent);


            }
        });


        initRecyclerView(view);
        buildListData();
        buildListData2();

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    topage = topage + 10;
                    buildListData2();




                }

            }
        });




        investrecyclerview.addOnItemTouchListener(new HomeFragment.RecyclerTouchListener(getContext(), investrecyclerview, new HomeFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                investData = investlist2.get(position);
                Intent intent = new Intent(getActivity(), Investreplace.class);
                intent.putExtra("invidx", investData.getInvidx());
                startActivity(intent);

            }
        }));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        buildListData();
        buildListData2();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        investrecyclerview.setLayoutManager(layoutManager);
    }

    private void initRecyclerView(View view) {


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        investrecyclerview.setLayoutManager(layoutManager);

    }

    private void buildListData() {

        SharedPreferences sp1 = getContext().getSharedPreferences("투자내역", MODE_PRIVATE);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(String response) {
                investlist.clear();
                String pattern3 = "###.##";
                DecimalFormat decimalFormat3 = new DecimalFormat(pattern3);

                HashMap<String, String[]> hashMap = new HashMap<>();
                ArrayList<Integer> 주식 = new ArrayList<Integer>();
                ArrayList<Integer> 코인 = new ArrayList<Integer>();
                ArrayList<Integer> 부동산 = new ArrayList<Integer>();
                ArrayList<Integer> 채권 = new ArrayList<Integer>();
                ArrayList<Integer> 외환 = new ArrayList<Integer>();
                ArrayList<Integer> 기타 = new ArrayList<Integer>();
                ArrayList<Integer> 총투자금 = new ArrayList<Integer>();


                Log.e("시작", "1");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    Log.e("total주식", response);

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject object = jsonArray.getJSONObject(i);
                            Log.e("변수명주식1", "0");
                            String invidx = object.getString("invidx"); // 분류
                            Log.e("변수명주식1", "0");
                            String invkind = object.getString("invkind"); // 분류
                            Log.e("변수명주식1", "1");
                            String invname = object.getString("invname"); // 품명
                            Log.e("변수명주식1", "2");
                            String invavg = object.getString("invavg"); // 평가손익
                            Log.e("변수명주식1", "3");
                            String invgetper = object.getString("invgetper"); // 수익률




                            String invgetpers = invgetper + "%";


                            Log.e("변수명주식1", "4");
                            String invcount = object.getString("invcount"); // 수량
                            Log.e("변수명주식1", "5");
                            String invbuyavg = object.getString("invbuyavg"); // 매수가
                            Log.e("변수명주식1", "6");
                            String invnowprice = object.getString("invnowprice"); //현재평가금액
                            int iinvonowprice = Integer.parseInt(invnowprice);
                            Log.e("변수명주식1", "7");
                            String invbuyprice = object.getString("invbuyprice"); // 총매수금액
                            Log.e("변수명주식1", "8");

                            int iinvbuyprice = Integer.parseInt(invbuyprice);
                            총투자금.add(iinvbuyprice);


                            if (invkind.equals("주식")) {
                                주식.add(iinvonowprice);
                            } else if (invkind.equals("코인")) {
                                코인.add(iinvonowprice);
                            } else if (invkind.equals("부동산")) {
                                부동산.add(iinvonowprice);
                            } else if (invkind.equals("채권")) {
                                채권.add(iinvonowprice);
                            } else if (invkind.equals("외화")) {
                                외환.add(iinvonowprice);
                            } else if (invkind.equals("기타")) {
                                기타.add(iinvonowprice);
                            }


//                            Log.e("변수명주식1", "9");
//                            investData = new InvestData(invidx, invkind, invname, invavg, invgetpers, invcount, invbuyavg, invnowprice, invbuyprice); //Data에 6개의 항목을 만들었기때문에 4개의 값을 가져와야함.
//                            Log.e("변수명주식1", "10");
//                            investlist.add(investData);
//                            Log.e("변수명주식1", "11");

                        }
//                        investAdapter = new InvestAdapter(getContext(), investlist); // 특이 context 가 아니라 getactivity로 받음
//
//                        investrecyclerview.setAdapter(investAdapter);
//
//                        investAdapter.notifyDataSetChanged();
                        Log.e("변수명주식1", "12");

                        int 총투자금합 = 0;
                        for (Integer i : 총투자금) {
                            총투자금합 += i;
                        }
                        Log.e("변수명주식총투자금합1", String.valueOf(총투자금합));

                        Log.e("변수명주식주식1", String.valueOf(주식));
                        int 주식합 = 0;
                        for (Integer i : 주식) {
                            주식합 += i;
                        }
                        Log.e("변수명주식주식합", String.valueOf(주식합));
                        Log.e("변수명주식코인1", String.valueOf(코인));
                        int 코인합 = 0;
                        for (Integer i : 코인) {
                            코인합 += i;
                        }
                        Log.e("변수명주식코인합", String.valueOf(코인합));

                        Log.e("변수명주식부동산1", String.valueOf(부동산));
                        int 부동산합 = 0;
                        for (Integer i : 부동산) {
                            부동산합 += i;
                        }
                        Log.e("변수명주식부동산합", String.valueOf(부동산합));

                        Log.e("변수명주식채권1", String.valueOf(채권));
                        int 채권합 = 0;
                        for (Integer i : 채권) {
                            채권합 += i;
                        }
                        Log.e("변수명주식채권합", String.valueOf(채권합));


                        Log.e("변수명주식외환1", String.valueOf(외환));
                        int 외환합 = 0;
                        for (Integer i : 외환) {
                            외환합 += i;
                        }
                        Log.e("변수명주식외환합", String.valueOf(외환합));
                        Log.e("변수명주식기타1", String.valueOf(기타));
                        int 기타합 = 0;
                        for (Integer i : 기타) {
                            기타합 += i;
                        }
                        Log.e("변수명주식기타합", String.valueOf(기타합));

                        pieChart.setUsePercentValues(true);
                        pieChart.getDescription().setEnabled(false);
                        pieChart.setExtraOffsets(5, 5, 5, 10);

                        pieChart.setDragDecelerationFrictionCoef(0.95f);

                        pieChart.setDrawHoleEnabled(true);
                        pieChart.setHoleColor(Color.WHITE);

                        String A = "Rich \nGroup";
                        pieChart.setCenterText(A);
                        pieChart.setCenterTextSize(30f);

                        pieChart.setTransparentCircleRadius(65f); //가운데 다음 투명 두번째원


                        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();



                        yValues.add(new PieEntry(주식합, "주식"));
                        yValues.add(new PieEntry(코인합, "코인"));
                        yValues.add(new PieEntry(부동산합, "부동산"));
                        yValues.add(new PieEntry(채권합, "채권"));
                        yValues.add(new PieEntry(외환합, "외환"));
                        yValues.add(new PieEntry(기타합, "기타"));


                        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션
                        pieChart.setEntryLabelColor(Color.BLACK);//차트내 항목명 색깔
//                        pieChart.setEntryLabelTextSize(10); // 차트내 항목명 사이즈


                        pieChart.setEntryLabelTextSize(0); // 항목명 사이즈 0으로


                        PieDataSet dataSet = new PieDataSet(yValues, "투자현황");
                        dataSet.setSliceSpace(3f);
                        dataSet.setSelectionShift(6f);
                        dataSet.setColors(new int[]{
                                R.color.piepuple, R.color.piered, R.color.piebrown, R.color.pieblue, R.color.piegreen, R.color.pieyellow}, getActivity());
                        dataSet.setValueTextColor(Color.RED);
                        dataSet.setValueFormatter(new MyValueFormatter());


                        dataSet.setValueTextColor(Color.rgb(251, 99, 118));

                        PieData data = new PieData((dataSet));

                        data.setValueTextSize(15f);
                        data.setValueFormatter(new MyValueFormatter());
                        data.setValueTextColor(Color.BLACK);


                        //색 항목별이름나열 표식 안보이게
                        Legend l = pieChart.getLegend();
                        l.setEnabled(false); // x-Values List false 안보이게 / true 보이게

//        pieChart.invalidate(); // 회전 및 터치 효과 사라짐
                        pieChart.setTouchEnabled(false);
                        pieChart.setData(data);


                        int 전체합 = 주식합 + 코인합 + 부동산합 + 채권합 + 외환합 + 기타합;
                        Log.i("합1", String.valueOf(전체합));
                        Log.i("합11", String.valueOf(주식합));
                        Log.i("합11", String.valueOf(코인합));
                        Log.i("합11", String.valueOf(부동산합));
                        Log.i("합11", String.valueOf(채권합));
                        Log.i("합11", String.valueOf(외환합));
                        Log.i("합11", String.valueOf(기타합));


                        String pattern = "###.##";
                        DecimalFormat decimalFormat = new DecimalFormat(pattern);

                        String pattern1 = "###,###,###";
                        DecimalFormat decimalFormat1 = new DecimalFormat(pattern1);

                        String S전체합 = decimalFormat1.format(전체합);
                        totalvalue.setText(S전체합);
                        String S총투자금 = decimalFormat1.format(총투자금합);
                        totalinvest.setText(S총투자금);
                        double 총투 = 총투자금합;

                        double 총수익률 = ((double) ((전체합 - 총투) / 총투 * 100));

                        String S총수익률c = decimalFormat.format(총수익률);
                        totaladdpercent.setText(S총수익률c+"%");
                        Log.i("S총수익률c", S총수익률c);

                        double 평가손익 = 전체합 - 총투;
                        String S평가손익 = decimalFormat1.format(평가손익);
                        totalprofit.setText(S평가손익);


                        if (총투 != 0) {




                            double 주식퍼센트 = ((double) 주식합 / 전체합) * 100;

                            Log.i("합2주식퍼센트", String.valueOf(주식퍼센트));
                            String S주 = String.valueOf(주식퍼센트);
                            String S주c = decimalFormat.format(주식퍼센트);
                            stockpercent.setText(S주c + "%");
                            Log.i("합2", S주c);

                            double 코인퍼센트 = ((double) 코인합 / 전체합) * 100;

                            String S코 = String.valueOf(코인퍼센트);

                            String S코c = decimalFormat.format(코인퍼센트);
                            coinpercent.setText(S코c + "%");
                            Log.i("합3", S코c);

                            double 부동산퍼센트 = ((double) 부동산합 / 전체합) * 100;
                            String S부 = String.valueOf(부동산퍼센트);

                            String S부c = decimalFormat.format(부동산퍼센트);
                            landpercent.setText(S부c + "%");
                            Log.i("합4", S부c);

                            double 채권센트 = ((double) 채권합 / 전체합) * 100;
                            String S채 = String.valueOf(채권센트);

                            String S채c = decimalFormat.format(채권센트);
                            paperpercent.setText(S채c + "%");
                            Log.i("합5", S채c);

                            double 외환퍼센트 = ((double) 외환합 / 전체합) * 100;
                            String S외 = String.valueOf(외환퍼센트);

                            String S외c = decimalFormat.format(외환퍼센트);
                            moneypercent.setText(S외c + "%");
                            Log.i("합5", S외c);

                            double 기타퍼센트 = ((double) 기타합 / 전체합) * 100;
                            String S기 = String.valueOf(기타퍼센트);


                            String S기c = decimalFormat.format(기타퍼센트);
                            otherpercent.setText(S기c + "%");
                            Log.i("합6", S기c);

                            SharedPreferences.Editor  editor = sp1.edit();
                            editor.putString("총보유",S전체합); //값을저장시
                            editor.putString("수익률",S총수익률c); //값을저장시
                            editor.putString("투자금",S총투자금); //값을저장시
                            editor.putString("평가손익",S평가손익); //값을저장시

                            editor.apply();
//
                            Log.e(  "투자123S전체합", S전체합);
                            Log.e(  "투자123S총수익률c", S총수익률c);
                            Log.e(  "투자123S총투자금", S총투자금);
                            Log.e(  "투자123S평가손익", S평가손익);


                        } else if (총투 == 0) {
                            totaladdpercent.setText("0" + "%");
                            stockpercent.setText("0" + "%");
                            coinpercent.setText("0" + "%");
                            landpercent.setText("0" + "%");
                            paperpercent.setText("0" + "%");
                            moneypercent.setText("0" + "%");
                            otherpercent.setText("0" + "%");

                            SharedPreferences.Editor  editor = sp1.edit();
                            editor.putString("총보유",S전체합); //값을저장시
                            editor.putString("수익률","0"); //값을저장시
                            editor.putString("투자금",S총투자금); //값을저장시
                            editor.putString("평가손익",S평가손익); //값을저장시
                            editor.apply();


                        }


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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("username", loginname);
                String Sfrompage = String.valueOf(frompage);
                String Stopage = String.valueOf(topage);

                params.put("from", Sfrompage);
                params.put("to", Stopage);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);



    }

    private void buildListData2() {
        SharedPreferences sp1 = getContext().getSharedPreferences("투자내역", MODE_PRIVATE);
        StringRequest request = new StringRequest(Request.Method.POST, urlpage, new Response.Listener<String>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(String response) {
                String pattern3 = "###.##";
                DecimalFormat decimalFormat3 = new DecimalFormat(pattern3);

                HashMap<String, String[]> hashMap = new HashMap<>();
                ArrayList<Integer> 주식1 = new ArrayList<Integer>();
                ArrayList<Integer> 코인1 = new ArrayList<Integer>();
                ArrayList<Integer> 부동산1 = new ArrayList<Integer>();
                ArrayList<Integer> 채권1 = new ArrayList<Integer>();
                ArrayList<Integer> 외환1 = new ArrayList<Integer>();
                ArrayList<Integer> 기타1 = new ArrayList<Integer>();
                ArrayList<Integer> 총투자금1 = new ArrayList<Integer>();

                investlist2.clear();
                Log.e("시작", "1");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    Log.e("total주식", response);

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject object = jsonArray.getJSONObject(i);
                            Log.e("변수명주식1", "0");
                            String invidx = object.getString("invidx"); // 분류
                            Log.e("변수명주식1", "0");
                            String invkind = object.getString("invkind"); // 분류
                            Log.e("변수명주식1", "1");
                            String invname = object.getString("invname"); // 품명
                            Log.e("변수명주식1", "2");
                            String invavg = object.getString("invavg"); // 평가손익
                            Log.e("변수명주식1", "3");
                            String invgetper = object.getString("invgetper"); // 수익률




                            String invgetpers = invgetper + "%";


                            Log.e("변수명주식1", "4");
                            String invcount = object.getString("invcount"); // 수량
                            Log.e("변수명주식1", "5");
                            String invbuyavg = object.getString("invbuyavg"); // 매수가
                            Log.e("변수명주식1", "6");
                            String invnowprice = object.getString("invnowprice"); //현재평가금액
                            int iinvonowprice = Integer.parseInt(invnowprice);
                            Log.e("변수명주식1", "7");
                            String invbuyprice = object.getString("invbuyprice"); // 총매수금액
                            Log.e("변수명주식1", "8");

                            int iinvbuyprice = Integer.parseInt(invbuyprice);
                            총투자금1.add(iinvbuyprice);


                            if (invkind.equals("주식")) {
                                주식1.add(iinvonowprice);
                            } else if (invkind.equals("코인")) {
                                코인1.add(iinvonowprice);
                            } else if (invkind.equals("부동산")) {
                                부동산1.add(iinvonowprice);
                            } else if (invkind.equals("채권")) {
                                채권1.add(iinvonowprice);
                            } else if (invkind.equals("외화")) {
                                외환1.add(iinvonowprice);
                            } else if (invkind.equals("기타")) {
                                기타1.add(iinvonowprice);
                            }


                            Log.e("변수명주식1", "9");
                            investData = new InvestData(invidx, invkind, invname, invavg, invgetpers, invcount, invbuyavg, invnowprice, invbuyprice); //Data에 6개의 항목을 만들었기때문에 4개의 값을 가져와야함.
                            Log.e("변수명주식1", "10");
                            investlist2.add(investData);
                            Log.e("변수명주식1", "11");

                        }
                        investAdapter = new InvestAdapter(getContext(), investlist2); // 특이 context 가 아니라 getactivity로 받음

                        investrecyclerview.setAdapter(investAdapter);

                        investAdapter.notifyDataSetChanged();



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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("username", loginname);
                String Sfrompage = String.valueOf(frompage);
                String Stopage = String.valueOf(topage);

                params.put("from", Sfrompage);
                params.put("to", Stopage);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);



    }
    private void piechart() {

    }


}
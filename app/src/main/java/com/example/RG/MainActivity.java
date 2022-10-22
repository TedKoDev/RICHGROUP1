package com.example.RG;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.RG.mainfragment.MainViewpagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    static MainViewpagerAdapter mainViewPagerAdapter;
    TabLayout tabLayout;
    ViewPager tabViewPager;
    TextView logname;
    int getString;
    private long backpressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


//        ---------------------------------

        logname = findViewById(R.id.logname);
        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        String loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        String user_email = sp.getString("user_email", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        String user_pw = sp.getString("user_pw", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(loginname); // name 란엔 xml파일에서 만들어둔 유저이름id값

//        ------------------------
        SharedPreferences sp1 = getSharedPreferences("autologin", MODE_PRIVATE);
        String alog = sp1.getString("autolog", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        Log.i("autolog44???111", alog);

        if (Objects.equals(alog, "1")) {
            Log.i("autolog44111", alog); // 자동로그인상황( 빠르게 메인으로 넘어감

            // 서비스 시작
            Intent intent1 = new Intent(getApplicationContext(), ChatService.class);
            getApplicationContext().startService(intent1);

        }










        Log.i("TLP", "1");


        tabLayout = findViewById(R.id.main_tab_layout);
        tabViewPager = findViewById(R.id.view_pager_content);
        Log.i("TLP", "2");
        //텝레이아웃에 메뉴를 추가한다.
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("홈", R.drawable.tab_select_home)));
        Log.i("TLP", "3");
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("찾기", R.drawable.tab_select_serch)));
        Log.i("TLP", "4");
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("채팅", R.drawable.tab_select_chat)));
        Log.i("TLP", "5");
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("투자", R.drawable.tab_select_invest)));
        Log.i("TLP", "6");

        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("LIVE", R.drawable.tab_select_live)));
        Log.i("TLP", "7");
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("MY", R.drawable.tab_select_mypage)));
        Log.i("TLP", "8");

        //뷰페이저어뎁터 선언 및 텝레이아웃과 연결 (stickode TabViewPagerAdapter참고)
        mainViewPagerAdapter = new MainViewpagerAdapter(
                getSupportFragmentManager(), tabLayout.getTabCount(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        Log.i("TLP", "8");
        //뷰페이저에 어댑터 세팅
        tabViewPager.setAdapter(mainViewPagerAdapter);
        Log.i("TLP", "9");
        // 값받는_액티비티에서 사용
        Intent intent = getIntent(); // 이전 액티비티에서 보낸 intent 받기
        Log.i("TLP", "10");
        getString = intent.getIntExtra("Fnum", 5);
        Log.i("TLP", "11");
        Log.e("TLP Fnum", String.valueOf(getString));
        ;


        //리스너 선언
        tabViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        Log.i("TLP", "12");

//        TabLayout.Tab tab =tabLayout.newTab().setCustomView(createTabView("채팅", R.drawable.tab_select_chat)) ;
//        tabLayout.selectTab(tab);
//        tabLayout.getTabAt(2);
//        tabLayout.
//
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            //탭이 선택되었을때의 이벤트 동작
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("TLP", "13");

                tabViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("TLP", "19");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i("TLP", "20");

            }
        });


    }



    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }

    }

    //커스텀한 탭 뷰(R.layout.tab_custom)를 가져온 후  지정한 이름과 이미지로 세팅하여 리턴해준다.
    private View createTabView(String name, int image) {
        Log.i("TLP", "21");
        View tabView = LayoutInflater.from(MainActivity.this).inflate(R.layout.maintab_custom, null);
        Log.i("TLP", "22");
        TextView tabName = tabView.findViewById(R.id.tab_name);
        Log.i("TLP", "23");
        ImageView tabImage = tabView.findViewById(R.id.tab_image);
        Log.i("TLP", "24");
        tabName.setText(name);
        Log.i("TLP", "25");
        tabImage.setImageResource(image);
        Log.i("TLP", "26");
        return tabView;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
//
//    public static void  refresh(){
//
//        mainViewPagerAdapter.notifyDataSetChanged();
//    }
}

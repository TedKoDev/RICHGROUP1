package com.example.RG.Home_group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.RG.MainActivity;
import com.example.RG.OnBackPressedListener;
import com.example.RG.R;

public class GrAboutFragment extends Fragment implements OnBackPressedListener {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gr_about, container, false);





        return view;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
        startActivity(intent);

    }
}
package com.example.RG;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.RG.Groupintro.GroupSettingActivity;
import com.example.RG.More.MoreMyAccount;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;



public class ExampleBottomSheetDialog extends BottomSheetDialogFragment {

    // 초기변수 설정
    private View view;
    // 인터페이스 변수
    private BottomSheetListener mListener;
    // 바텀시트 숨기기 버튼
    private Button button1,button2;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        mListener = (BottomSheetListener) getContext();

        button1 = view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // 값입력_액티비티에서 사용
              Intent intent = new Intent(getContext(), GroupSettingActivity.class); //액티비티 전환
              // 전달할 값 ( 첫번째 인자 : key, 두번째 인자 : 실제 전달할 값 )

              startActivity(intent);
                dismiss();
            }
        });

        button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 값입력_액티비티에서 사용
                Intent intent = new Intent(getContext(), MoreMyAccount.class); //액티비티 전환
                // 전달할 값 ( 첫번째 인자 : key, 두번째 인자 : 실제 전달할 값 )

                startActivity(intent);
                dismiss();
            }
        });
        return view;
    }

    // 부모 액티비티와 연결하기위한 인터페이스
    public interface BottomSheetListener {
        void onButtonClicked(String text);


    }

}
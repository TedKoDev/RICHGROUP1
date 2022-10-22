package com.example.RG;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomDialog extends Dialog implements View.OnClickListener {


    String name;
    private Context context;
    private CustomDialogListener customDialogListener;

    private EditText editName;
    String onoff;
    private TextView ontv;
    private TextView offtv;
    private LinearLayout layouton;
    private LinearLayout layoutoff;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    } //인터페이스 설정

    public interface CustomDialogListener {
        void onPositiveClicked(String name);

        void onNegativeClicked();
    }

    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener) {
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customsheet_layout_cate); //init


        layouton = (LinearLayout) findViewById(R.id.onoff_on);
        layoutoff = (LinearLayout) findViewById(R.id.onoff_off);
        ontv = (TextView) findViewById(R.id.offtvv);
        offtv = (TextView) findViewById(R.id.ontvv);
        editName = (EditText) findViewById(R.id.editName);

        layouton.setOnClickListener(this);
        layoutoff.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onoff_on:
                //확인 버튼을 눌렀을 때 //각각의 변수에 EidtText에서 가져온 값을 저장
                ontv.setText("공개");

                name = ontv.getText().toString();


                //인터페이스의 함수를 호출하여 변수에 저장된 값들을 Activity로 전달
                customDialogListener.onPositiveClicked(name);
                dismiss();
                break;

            case R.id.onoff_off: //비공개 버튼을 눌렀을 때

                offtv.setText("비공개");

                name = offtv.getText().toString();

                //인터페이스의 함수를 호출하여 변수에 저장된 값들을 Activity로 전달
                customDialogListener.onNegativeClicked();
                dismiss();
                break;
        }
    }
}

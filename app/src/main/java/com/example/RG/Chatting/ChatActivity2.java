package com.example.RG.Chatting;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.RG.ChatService;
import com.example.RG.R;

public class ChatActivity2 extends AppCompatActivity {
    public static Handler mHandler;

    public static TextView chatView;
    String 사용자이름;
    String 방번호;
    String UserID;
    Button chatbutton;
    EditText message;
    String sendmsg;


//
//    String urlchat_message = "http://3.38.117.213/chat_messageget.php";
//    String urlchat_message2 = "http://3.38.117.213/chat_messageget2.php";

    TextView logname, grnum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        getSupportActionBar().hide();


        logname = findViewById(R.id.logname);
        grnum = findViewById(R.id.grnum);
        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        사용자이름 = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        logname.setText(사용자이름); // name 란엔 xml파일에서 만들어둔 유저이름id값


        sp = getSharedPreferences("chatroomidx", MODE_PRIVATE);
        방번호 = sp.getString("채팅방번호", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        grnum.setText(방번호); // name 란엔 xml파일에서 만들어둔 유저이름id값


        mHandler = new Handler();

        chatView = (TextView) findViewById(R.id.chatView);
        message = (EditText) findViewById(R.id.message);

        chatbutton = (Button) findViewById(R.id.chatbutton);

//  ------------------------------------------------------------------------------------------------
        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tag 채팅 액티비티 메세지보내기", "1");
                sendmsg = message.getText().toString();
                Log.i("tag 채팅 액티비티 메세지보내기", "2 전송할 메세지 String 객체 저장 sendmsg: " + sendmsg);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Log.i("tag 채팅 액티비티 메세지보내기", "3");
                            ChatService.jsonObject.put("message", sendmsg);
                            ChatService.jsonObject.put("nickName", 사용자이름);
                            ChatService.jsonObject.put("roomnumber", 방번호);


                            Log.i("tag 채팅 액티비티 메세지보내기", "4 + message" + ChatService.jsonObject.getString(
                                    "message"));
                            Log.i("tag 채팅 액티비티 메세지보내기", "4 + roomnumber" + ChatService.jsonObject.getString(
                                    "roomnumber"));

                            ChatService.sendWriter.println(ChatService.jsonObject);
                            Log.i("tag 채팅 액티비티 메세지보내기", "5  닉네임,메세지,방번호 를 jsonObject에 담아 보냄 + " + ChatService.jsonObject);
                            Log.i("tag 채팅 액티비티 메세지보내기", "6 ChatService.sendWriter + "
                                    + ChatService.sendWriter);
                            ChatService.sendWriter.flush();
                            Log.i("tag 채팅 액티비티 메세지보내기", "7 ChatService.sendWriter flush 비워졌나? +  " + ChatService.sendWriter);
                            Log.i("tag 채팅 액티비티 메세지보내기", "8 + " + ChatService.jsonObject.getString(
                                    "message"));
                            message.setText("");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                Log.i("tag 채팅 액티비티 메세지보내기", "9");
            }
        });
    }


    public static class msgUpdate implements Runnable {
        private final String msg;

        public msgUpdate(String str) {
            this.msg = str;
            Log.i("tag 채팅 액티비티 메세지 수령", "1" + "str:" + str + ", msg:" + this.msg);

        }

        @Override
        public void run() {
            Log.i("tag 채팅 액티비티 메세지 수령", "2 chatView: " + chatView);
            chatView.setText(chatView.getText().toString() + msg + "\n");
            Log.i("tag 채팅 액티비티 메세지 수령", "3 chatView: " + chatView);

        }
    }


}

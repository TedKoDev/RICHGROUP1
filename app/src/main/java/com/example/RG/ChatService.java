package com.example.RG;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.RG.Chatting.ChatActivity;
import com.example.RG.Home_group.GrChatFragment;
import com.example.RG.mainfragment.ChatFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class ChatService extends Service {


    // 채널에 대한 ID생성 - 채널을 구분하기 위한 아이디입니다.
    public static final String NOTIFICATION_CHANNEL_ID = "10002";
    private static final String PATTERN_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static JSONObject jsonObject;
    public static PrintWriter sendWriter;
    public static InputStreamReader io;
    public static OutputStream outputStream;
    public static BufferedReader input;
    public static Boolean isrun = false;
    private final IBinder mBinder = new LocalBinder();
    String Noti;
    NTServiceThread thread;
    Socket socket;
    InetAddress serverAddr;
    String ip = "10.0.2.2";
    int port = 8022;
    String read;
    String NotiRoom, NotiRoomname;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("tag 서비스 시작", "로그인후 서비스 시작 ");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 유저 닉네임 수령
        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
        String loginname = sp.getString("userName", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
        Log.i("tag 서비스", "1");


        //

        myServiceHandler handler = new myServiceHandler();


        jsonObject = new JSONObject();
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {
                    Log.i("tag 서비스", "2");
//-----------------------------------------------------------------------------------------------
                    //1111111111111111111111111111111111111111111111111
                    // ip 및 소캣 연동하기
//
                    serverAddr = InetAddress.getByName(ip);
                    Log.i("tag 서비스", "3");

                    socket = new Socket(serverAddr, port);
                    Log.i("tag 서비스", "4");

//------------------------------------------------------------------------------------------------
                    //22222222222222222222222222222222222222222222222222
                    // 2최초의 로그인시에 서버로 닉네임 값을 전달 하는 부분임
                    // 1.메세지 전달 용 sendWriter객체 생성 (위에서 선언해둠)
                    sendWriter = new PrintWriter(socket.getOutputStream(), true);
                    Log.i("tag 서비스", "5");
                    // 2.jsonObject에 값 저장
                    jsonObject.put("nickName", loginname);
                    Log.i("tag 서비스", "6");
                    Log.i("tag 서비스", loginname);
                    // 3. 1.에서 생성한 sendWriter 객체에  2.값 을 담아 서버로 전달함.
                    sendWriter.println(jsonObject);
                    Log.i("tag 서비스", "7");
                    Log.i("tag 서비스", "서버로 닉 전달" + String.valueOf(sendWriter));

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------


//                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Log.i("tag 서비스", "8 + socket.getInputStream의 인자를 가지는 BufferReader input선언 및 생성 ");
                    Log.i("tag 서비스", "8-1 input 값:" + input);


                    if (input != null) {
                        isrun = true;

                        while (isrun) {//33333333333333333333333333333333333333333333333333333333333333333333
                            // 메세지 수령 대기


                            Log.i("tag 서비스", "10 + 최초 로그인후 while문 진입후  여기까지 작동함 \n " +
                                    "메세지 수령 대기중 메세지가 들어오면 Buffereader.readLine 메소드로 캐치 \n " +
                                    "String read 로 선언한 read객체에 메세지 저장 ");

                            //최초 로그인후 여기까지 작동함

                            Log.i("tag 서비스", "8-1 input 값:" + input);
                            JSONObject jsonObject1;
                            // input.readline을  jsonobject 형태로 받을 수있고
                            jsonObject1 = new JSONObject(input.readLine());

                            // input.readline을  String (read) 형태로도 받을 수 있다.
//                                 read = input.readLine();


//                            Log.i("tag 서비스", "11 + read에 메세지 저장 완료 + read:" + read);
                            Log.i("tag 서비스", "11 + read에 메세지 저장 완료 + input:" + input);
//                           Log.i("tag 서비스", "11 + read에 메세지 저장 완료 + read:" + read);
                            Log.i("tag 서비스", "11 + read에 메세지 저장 완료 + jsonObject:" + jsonObject1);


                            //  getString 등을 이용하여, JSONObject로부터 원하는 값을 뽑아낼 수 있다.
                            String nick = "";
                            String text = "";
                            String room = "";
                            String roomname = "";
                            String time = "";


                            nick = jsonObject1.get("nickName").toString();
                            Log.i("tag 서비스", "11-1 +  json 값풀기 nick:" + nick);
                            text = jsonObject1.get("message").toString();
                            Log.i("tag 서비스", "11-2 +  json 값풀기 message(text):" + text);
                            room = jsonObject1.get("roomnumber").toString();
                            Log.i("tag 서비스", "11-2 +  json 값풀기 roomnumber(room):" + room);

                            roomname = jsonObject1.get("roomname").toString();
                            Log.i("tag 서비스", "11-2 +  json 값풀기 roomname(roomname):" + roomname);

                            time = jsonObject1.get("intime").toString();
                            Log.i("32111tag 서비스", "11-2 +  json 값풀기 intime(time):" + time);



                            NotiRoom = room;
                            NotiRoomname = roomname;
                            // String 의 Date화
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat StringtoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            StringtoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));// 필수 UTC의 값을 받아왔지만 다시한번 UTC타임존으로 포멧을 해줘야하는 아이러니함
                            Date datedtime = StringtoDateFormat.parse(time);

                            //UTC로 저장을 했지만 UTC로 다시 TIMEZONE을 성정 해주어야 정상적으로 출력된다.
                            // 이유를 모르겠다.

                            Log.i("32111 소캣메세Date형태 utc재적용", String.valueOf(datedtime));


                            //또한  아래에서 SimpleDateFormat dateFormatLocalization 에서 DATE를 한번더 포맷 고정을 해주어야 변화가 안생긴다.
                            // 이유는 모르겠다
                            // 아마도 Mysql이 KST 기준이라 그런건가 싶기도하고 잘 모르곘다
                            // 여튼 2일동안 난리친걸 해결했으니 일단 넘어간다.
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatLocalization = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//***/*
                            Date utcDate = StringtoDateFormat.parse(dateFormatLocalization.format(datedtime));


                            //타임존을 지정한 Date를 String 화 시킴
//                            String ltime = StringtoDateFormat.format(datedtime);//이렇게 하면안되더라
                            String ltime = dateFormatLocalization.format(datedtime); // 이렇게 타임존 설정이 안되어있는 포맷화를 한번더  되더라 그래서 위에 ***이를 살려둬야한다.
                            System.out.println("32111포맷 지정 후 현지시간 적용후 스트링화 : " + ltime);
                            Log.i("432111 datedtime", String.valueOf(datedtime));
                            Log.i("432111 utcDate", String.valueOf(utcDate));

                            Log.i("432111 time", String.valueOf(time));
                            Log.i("432111 ltime", String.valueOf(ltime));


//                            String printtime = ltime.substring(11, 16); // 시간 줄이는것


//                        if (jsonObject1 != null) {
                            int viewType_num;
                            viewType_num = 1; // 타인으로 부터 들어오는 메세지 임으로

                            Log.i("tag 서비스", "11-3 +  :" + nick + room + text + ltime);

                            Noti = nick + ": " + text + "    " + ltime;


//==============================노티결정부분========================================================================================
                            SharedPreferences RoomNumfornoti = getSharedPreferences("RoomNumfornoti", MODE_PRIVATE);

                            String chatroom = RoomNumfornoti.getString("room", " "); //key값에 저장된 값이 있는지확인, 값이없다면""(공백)을 반환
                            Log.i("chatroom", chatroom);


                            if (!Objects.equals(chatroom, room)) {
                                thread = new NTServiceThread(handler, Noti);
                                thread.start();
                            }
//==============================노티결정부분 end ================================================================================


                            if (ChatActivity.chatAcHandler != null) {
                                //ChatActivity내의 GetMSG Class를 의 객채를 생성하고 매개 변수로 nick,room,text,viewType_num,time 값을 선언해준다.
                                ChatActivity.GetMSG chatactivity = new ChatActivity.GetMSG(nick, room, text, viewType_num, ltime);
                                // Hndler.post 를 이용해서 해당 chatactivity 객체를 작동시킨다. ->  ChatActivity.GetMSG 로 이동
                                Log.i("tag 서비스", "11-4 +  :" + nick + room + roomname + text + ltime);
                                ChatActivity.chatAcHandler.post(chatactivity);
                            } else {
                                Log.i("tag 서비스", "11-4-1 +  : 채팅액티비티 핸들러 못드가");
                            }


                            if (ChatFragment.chatFragHandler != null) {
                                ChatFragment.rvupdate chatactivity1 = new ChatFragment.rvupdate(nick, room, text, viewType_num, ltime);
                                // Hndler.post 를 이용해서 해당 chatactivity 객체를 작동시킨다. ->  ChatActivity.GetMSG 로 이동
                                Log.i("tag 서비스", "11-5 +  :" + nick + room + roomname + text + ltime);
                                ChatFragment.chatFragHandler.post(chatactivity1);
                            } else {
                                Log.i("tag 서비스", "11-5-1 +  : 채팅방목록  핸들러 못드가");
                            }


                            if (GrChatFragment.grChatHandler != null) {
                                GrChatFragment.GetGrChatMSG grChatMSG = new GrChatFragment.GetGrChatMSG(nick, room, text, viewType_num, ltime);
                                // Hndler.post 를 이용해서 해당 chatactivity 객체를 작동시킨다. ->  ChatActivity.GetMSG 로 이동
                                Log.i("tag 서비스", "11-7 +  :" + nick + room + roomname + text + ltime);
                                GrChatFragment.grChatHandler.post(grChatMSG);
                            } else {
                                Log.i("tag 서비스", "11-7-1 +  : 채팅방목록  핸들러 못드가");
                            }

//                            if (GroupActivity.GrAcHandler != null) {
//                                Log.i("tag 서비스", "11-6 텍스트 들어옴   입장");
//                                GroupActivity.Noread noreadacticity = new GroupActivity.Noread(room);
//                                // Hndler.post 를 이용해서 해당 chatactivity 객체를 작동시킨다. ->  ChatActivity.GetMSG 로 이동
//                                Log.i("tag 서비스", "11-6 텍스트 들어옴   :" + nick);
//                                GroupActivity.GrAcHandler.post(noreadacticity);
//                            } else {
//
//                                Log.i("tag 서비스", "11-6 텍스트 들어옴 +  : 채팅방목록  핸들러 못드가");
//                            }

                        }
                    }

                    Log.i("tag 서비스  응 못드감 ", "응 못드감 ");
                } catch (IOException | JSONException | ParseException e) {
                    e.printStackTrace();
                }
                Log.i("tag 서비스  close ", "13 ");
            }

        }).start();


        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {


        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    public class LocalBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }

    @SuppressLint("HandlerLeak")
    public class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {


            // 채널을 생성 및 전달해 줄수 있는 NotificationManager를 생성한다.

            //NOTIFICATION_SERVICE(자바자체클래스) 의 기능을 가져와서 mNotificationManager에 선언해줌
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            // 이동하려는 액티비티를 작성해준다.
            Intent notiIntent = new Intent(ChatService.this, ChatActivity.class);
            // 노티를 눌러서 이동시 전달할 값을 담는다. // 전달할 값을 notificationIntent에 담습니다.
            notiIntent.putExtra("Fnum", NotiRoom);
            notiIntent.putExtra("FRoomname", NotiRoomname);
            notiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거

            Log.e("NotiRoom", NotiRoom);


//            notiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);  // 잘 알고 쓰시오


            // 매개변수 Context 정보,requestCode :PendingIntent 를 가져올 때 구분하기 위한 고유 코드, 실행할 Intent,플래그
            PendingIntent pendingIntent = PendingIntent.getActivity(ChatService.this, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(ChatService.this, NOTIFICATION_CHANNEL_ID)

                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.richgroup)) //BitMap 이미지 요구
                    .setContentTitle("채팅방: " + NotiRoomname)
//                        .setContentText("메세지: " + MSGcount)
                    .setContentText(Noti)


                    .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 우선순위 순서
                    .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                    .setAutoCancel(true); // 눌러야 꺼지는 설정


            //OREO API 26 이상에서는 채널 필요
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)

                builder.setSmallIcon(R.drawable.richgroup); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                CharSequence channelName = "노티페케이션 채널";

                String description = "오레오 이상";

                int importance = NotificationManager.IMPORTANCE_HIGH;// 우선순위 설정

                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);

                channel.setDescription(description);

                // 노티피케이션 채널을 시스템에 등록
                assert notificationManager != null;

                notificationManager.createNotificationChannel(channel);

            } else
                builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

            assert notificationManager != null;

            notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작


        }

    }
}
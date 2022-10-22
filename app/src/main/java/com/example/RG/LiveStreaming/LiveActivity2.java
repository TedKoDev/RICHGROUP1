package com.example.RG.LiveStreaming;

import static io.agora.rtc.AgoraMediaRecorder.CONTAINER_MP4;
import static io.agora.rtc.AgoraMediaRecorder.STREAM_TYPE_BOTH;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.example.RG.Home_group.GroupActivity;
import com.example.RG.MainActivity;
import com.example.RG.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.agora.rtc.AgoraMediaRecorder;
import io.agora.rtc.AgoraMediaRecorder.IMediaRecorderCallback;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;


public class LiveActivity2 extends AppCompatActivity {
    public static final String LOGIN_MESSAGE = "com.example.agoratest.CHANNEL_LOGIN";
    static AgoraMediaRecorder agoraMediaRecorder;
    String filePath100;
    String filePath1;
    String filename;
    Uri uri;
    Context context;
    FrameLayout Remote화면, Local화면;
    IMediaRecorderCallback iMediaRecorderCallback;
    AgoraMediaRecorder.MediaRecorderConfiguration mediaRecorderConfiguration;
    AgoraMediaRecorder.RecorderInfo recorderInfo;
    //    public int startRecording= 0;
//    public int stopRecording;
    ImageView 제어화면막기, 제어음소거, 제어화면전환, 제어종료;
    private RequestQueue rQueue;
    private String upload_URL = "http://3.38.117.213/volleyvideo.php";
    //    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    private String 토큰;
    private RtcEngine mRtcEngine;
    private String channelName;
    private int channelProfile;
     IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {


        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            Log.i("AGㅎV-onFirstRemoteVid", "실행1 ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("AGㅎV-onFirstRemoteVid", "실행2 ");
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            Log.i("AGㅎV-onUserOffline", "실행1 ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("AGㅎV-onUserOffline", "2 ");
                    onRemoteUserLeft();
                }
            });
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) {
            Log.i("AGㅎV-onUserMuteVideo", "실행 1 ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("AGㅎV-onUserMuteVideo", "2 ");
                    onRemoteUserVideoMuted(uid, muted);

                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live2);
        getSupportActionBar().hide();
        Log.i("AGㅎV", "비디오액티비티 진입");

        Remote화면 = (FrameLayout) findViewById(R.id.remote_video_view_container);
        Local화면 = (FrameLayout) findViewById(R.id.local_video_view_container);
        제어화면막기 = findViewById(R.id.screenoffonlyhost);
        제어음소거 = findViewById(R.id.soundoffonlyhost);
        제어화면전환 = findViewById(R.id.camerachangeonlyhost);
        제어종료 = findViewById(R.id.outlinkboth);


        Intent intent = getIntent();
        channelName = intent.getStringExtra("채널명");
        토큰 = intent.getStringExtra("토큰");
        channelProfile = intent.getIntExtra("방장or청자", -1);

        Log.i("청자입장1", channelName);
        Log.i("청자입장2", 토큰);
        Log.i("청자입장3", String.valueOf(channelProfile));
//        Log.i("AGㅎV", "Main으로부터 채널명, 방장or청자 값 받음");
//        Log.i("AGㅎV-channelName", channelName);
//        Log.i("AGㅎV-channelProfile", "방장:1 ,청자:2 " + String.valueOf(channelProfile));
//        Log.i("AGㅎV", "initAgoraEngineAndJoinChannel 실행 ");
        initAgoraEngineAndJoinChannel();


        if (channelProfile == 1) {
            Remote화면.setVisibility(View.GONE);


            filename = "/storage/emulated/0/DCIM/" + Calendar.getInstance().getTimeInMillis() + ".mp4";
            filePath100 = "file://" + filename;

            AgoraMediaRecorder.MediaRecorderConfiguration mediaRecorderConfiguration = new AgoraMediaRecorder.MediaRecorderConfiguration(
                    filename, CONTAINER_MP4,
                    STREAM_TYPE_BOTH, 120000, 0
            );

            int start = AgoraMediaRecorder.getMediaRecorder(mRtcEngine, iMediaRecorderCallback).startRecording(mediaRecorderConfiguration);



            Log.e("video녹화", String.valueOf(start));
        } else if (channelProfile == 2) {

            Log.i("청자입장", "ㄷㄷㄷㄷ");
            Local화면.setVisibility(View.GONE);
            제어화면막기.setVisibility(View.GONE);
            제어음소거.setVisibility(View.GONE);
            제어화면전환.setVisibility(View.GONE);
        }


        if (channelProfile == -1) {
            Log.e("AGㅎTAG: ", "No profile");
        }


    }

    public Uri getUriFromPath(String path) {
//        String fileName = "file:///storage/emulated/0/DCIM/1654928075163.mp4";
        String fileName = "file:///storage/emulated/0/DCIM/1654929902950.mp4";
//
        Log.i("filePath3", path);

        String A = path;
        Log.i("filePath4", A);
        Uri fileUri = Uri.parse(A);
        Log.i("filePath5", String.valueOf(fileUri));
        String filePa = fileUri.getPath();

        Log.i("filePath6", filePa);
        Cursor c = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePa + "'", null, null);
        c.moveToNext();
        @SuppressLint("Range") int id = c.getInt(c.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
        return uri;
    }


    public void onEndCallClicked(View view) {
        Log.i("AGㅎV-onEndCallClicked", "종료 버튼 클릭");
//        getUriFromPath(filePath);

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(view.getContext());
        mbuilder.setTitle("종료확인");
        mbuilder.setMessage("스트리밍을 종료하시겠습니까?");
        mbuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Your Button Click Action Code
                if (channelProfile == 1) {


                    AgoraMediaRecorder.getMediaRecorder(mRtcEngine, iMediaRecorderCallback).stopRecording();
                    AgoraMediaRecorder.getMediaRecorder(mRtcEngine, iMediaRecorderCallback).release();
                    Log.e("video녹화해제", "해재됨");


                    uri = getUriFromPath(filePath100);


                    Intent intent = new Intent(getApplicationContext(), UploadActivity.class);

                    intent.putExtra("filename", filePath100); // 보낼 값 입력


                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                    startActivity(intent);


                } else {




                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                    startActivity(intent);


                }


            }
        });
        mbuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Your Button Click Action Code
                dialog.dismiss();

            }
        });
        mbuilder.show();

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(LiveActivity2.this);
        mbuilder.setTitle("종료확인");
        mbuilder.setMessage("스트리밍을 종료하시겠습니까?");
        mbuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Your Button Click Action Code
                if (channelProfile == 1) {


                    AgoraMediaRecorder.getMediaRecorder(mRtcEngine, iMediaRecorderCallback).stopRecording();
                    AgoraMediaRecorder.getMediaRecorder(mRtcEngine, iMediaRecorderCallback).release();
                    Log.e("video녹화해제", "해재됨");


                    uri = getUriFromPath(filePath100);
                    ;
//            uploadPDF(filename, uri);

                    Intent intent = new Intent(getApplicationContext(), UploadActivity.class);

                    intent.putExtra("filename", filePath100); // 보낼 값 입력


                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거
                    startActivity(intent);


                } else {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//액티비티 스택제거

                    startActivity(intent);


                }


            }
        });
        mbuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Your Button Click Action Code
                dialog.dismiss();

            }
        });
        mbuilder.show();


    }

    private void initAgoraEngineAndJoinChannel() {
        initalizeAgoraEngine();//initAgoraEngineAndJoinChannel 1 mRtcEngine 생성용도임
        Log.i("AGㅎV-initagora1", "mRtcEngine 생성됨 ");
        Log.i("AGㅎV-initagora2", "mRtcEngine에 채널프로필 (용도) 설정 ");
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);

        Log.i("AGㅎV-initagora3", "mRtcEngine에 방장or청자 값 설정");
        mRtcEngine.setClientRole(channelProfile);

        Log.i("AGㅎV-initagora4", "mRtcEngine에 방장or청자 값 설정");
        setupVideoProfile(); //위에 initalizeAgoraEngine에서 생성한 mRtcEngine에서 비디오사용을 가능하게 해주고 비디오 설정을 해줌
        setupLocalVideo(); // 비디오의 틀을 설정하는것 xml 연결을통한 화면구성,레이아웃등
        joinChannel(); // 채널에 조인하기위한 토큰,채널명,유저옵션,옵션유저정보 등 mRtcEngine에 입력함
    }

    private void initalizeAgoraEngine() {  //initAgoraEngineAndJoinChannel 1
        try {
            Log.i("AGㅎV-initalizeAgoraEn", "mRtcEngine 생성");
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.private_app_id), mRtcEventHandler);
            //private_app_id란? res->values->string 에 입력해둔 나의 AppID (아고라 콘솔에서받음)

            Log.i("AGㅎV-initalizeAgoraEn", "mRtcEngine 생성 완료");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        leaveChannel();
//        Log.i("AGㅎV-onDestroy", "leaveChannel ");
//        leaveChannel();
//        Log.i("AGㅎV-onDestroy", " RtcEngine.destroy(); ");
//        RtcEngine.destroy();
//        Log.i("AGㅎV-onDestroy", " mRtcEngine 비우기  ");
//        mRtcEngine = null;
//        Log.i("AGㅎV-onDestroy", " mRtcEngine :" + mRtcEngine);
    }

    public void onLocalVideoMuteClicked(View view) {
        Log.i("AGㅎV-영상차단", "실행 ");
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            Log.i("AGㅎV-영상차단", "영상나옴");
            iv.setSelected(false);
            iv.clearColorFilter();
        } else {
            Log.i("AGㅎV-영상차단", "차단됨");
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }

        mRtcEngine.muteLocalVideoStream(iv.isSelected());


        SurfaceView surfaceView = (SurfaceView) Local화면.getChildAt(0);
        surfaceView.setZOrderMediaOverlay(!iv.isSelected());
        surfaceView.setVisibility(iv.isSelected() ? View.GONE : View.VISIBLE);
    }

    private void setupRemoteVideo(int uid) {


        Log.i("AGㅎV-setupRemoteVideo", "1");

//        if (container.getChildCount() > 1) {
//            return;
//        }
        Local화면.setVisibility(View.GONE);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        Log.i("AGㅎV-setupRemoteVideo", "2");

        Remote화면.addView(surfaceView);
        Log.i("AGㅎV-setupRemoteVideo", "3");

        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
        Log.i("AGㅎV-setupRemoteVideo", "4");

    }

    private void onRemoteUserLeft() {
        Log.i("AGㅎV-onRemoteUserLeft", "1");


        Log.i("AGㅎV-onRemoteUserLeft", "2");
        Remote화면.removeAllViews();
        Log.i("AGㅎV-onRemoteUserLeft", "3");
    }

    private void setupVideoProfile() {//initAgoraEngineAndJoinChannel 2
        Log.i("AGㅎV-setupVideoset", "비디오사용하게함");
        mRtcEngine.enableVideo();// 비디오 가능하게함

        //비디오 인코더 구성해서 설정하기  비디오 사이즈, 프레임레이트, 비트레이트, 방향모드
        Log.i("AGㅎV-setupVideoset", "비디오 사이즈 프레임 속도 방향모드 설정 ");
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration
                (VideoEncoderConfiguration.VD_640x480,  // 비디오사이즈 설정
                        VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,//프레임레이트 설정 프레임 레이트(Frame rate)란 디스플레이 장치가 화면 하나의 데이터를 표시하는 속도를 말하며, 프레임 속도(frame速度) 또는 프레임률(frame率)이라고 옮기기도 한다.
                        VideoEncoderConfiguration.STANDARD_BITRATE, // 비트레이트 설정 전자 통신과 컴퓨팅에서 비트레이트(bitrate)는 특정한 시간 단위(이를테면 초 단위)마다 처리하는 비트의 수이다.
                        VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT)); // 방향설정  API에는 3종류가 있고, 적응형,풍경형, 초상화형 이 있다. 현재는 초상화형이 설정되어있음.
        Log.i("AGㅎV-setupVideoset", "완료 ");
    }

    private void setupLocalVideo() { //initAgoraEngineAndJoinChannel 3
        Log.i("AGㅎV-setupLocalVideo1", "비디오 틀 구성시작");
        Log.i("AGㅎV-setupLocalVideo2", "비디오 FrameLayout을 findviewByid로 xml과 연결");


        Log.i("AGㅎV-setupLocalVideo3", "렌더러뷰가지는 surfaceView를 만든다."); //렌더러란 오브젝트를 그리는 일련의 과정을 클래스화 시킨것
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext()); // 화면속 화면을 말하는것으로 예상됨  영상통화할때 타인+ 나 두개가 보이는것처럼
        // SurfaceView는 자기 영역 부분의 Window를 뚫어서(punch) 자신이 보여지게끔 하고 Window와 View가 블렌딩되어 화면에 보여지게 된다.
        Log.i("AGㅎV-setupLocalVideo4", "surfaceView에 화면 에 화면 깔기 허용.");
        surfaceView.setZOrderMediaOverlay(true); //위에깔기 (허용)
        Log.i("AGㅎV-setupLocalVideo5", "frameLayout에  만든 surfaceview 속성을 넣음 ");
        Local화면.addView(surfaceView); //
        Log.i("AGㅎV-setupLocalVideo6", "VideoCanvas 만듦 (surface, 공개속성, 유저아이디) 값이 들어감");
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0)); // uid = 유저 아이디 .

        Log.i("AGㅎV-setupLocalVideo7", "mRtcEngine에 비디오 틀 구성 설정시킴 완료 ");
        Log.i("AGㅎV-setupLocalVideo7", "완료");
    }

    private void joinChannel() { //initAgoraEngineAndJoinChannel 4
        Log.i("AGㅎV-joinChannel", "mRtcEngine에 채널조인을 위한 값 부여  token,채널명, 옵션정보,옵션유저id ");
        mRtcEngine.joinChannel(토큰, channelName, "Optional Data", 0);
        Log.i("AGㅎV-joinChannel", "완료");


    }

    private void leaveChannel() {
        Log.i("AGㅎV-leaveChannel", "실행 ");
        mRtcEngine.leaveChannel();
        Log.i("AGㅎV-leaveChannel", "완료 ");

    }

    @Override
    protected void onDestroy() {
        Log.i("AGㅎV-onDestroy", "앱종료시작 ");
        super.onDestroy();
        Log.i("AGㅎV-onDestroy", "leaveChannel ");

        Log.i("AGㅎV-onDestroy", " RtcEngine.destroy(); ");
        RtcEngine.destroy();
        Log.i("AGㅎV-onDestroy", " mRtcEngine 비우기  ");
        mRtcEngine = null;

        Log.i("AGㅎV-onDestroy", " mRtcEngine :" + mRtcEngine);
    }

    public void onSwitchCameraClicked(View view) {
        Log.i("AGㅎV-onSwitchCameraC", " 화면전환클릭-1번은 외부카메라(처음이 셀카임) ");
        mRtcEngine.switchCamera();
        Log.i("AGㅎV-onSwitchCameraC", " 화면전환클릭-2번은 셀카카메라로전환");
    }


    public void onLocalAudioMuteClicked(View view) { // 음소거 기능
        Log.i("AGㅎV-음소거버튼", "실행 ");
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            Log.i("AGㅎV-음소거버튼", "한번눌림-음소거됨 ");
            iv.setSelected(false);
            iv.clearColorFilter();
        } else {
            Log.i("AGㅎV-음소거버튼", "두번눌림-음소거해제 ");
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }
        Log.i("AGㅎV-음소거버튼", "mRtcEngine에 눌림or해제값 입력");
        mRtcEngine.muteLocalAudioStream(iv.isSelected());
    }

    private void onRemoteUserVideoMuted(int uid, boolean muted) {//음소거 부분
        Log.i("AGㅎV-원격영상차단", "실행 ");

        Log.i("AGㅎV-원격영상차단", "실행1 ");
        SurfaceView surfaceView = (SurfaceView) Remote화면.getChildAt(0);
        Log.i("AGㅎV-원격영상차단", "실행2 ");
        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            Log.i("AGㅎV-원격영상차단", "실행 3");
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }

}
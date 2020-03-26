package com.example.coupleapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.coupleapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoCanvas;

import io.agora.rtc.video.VideoEncoderConfiguration;

public class VideoChatActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID = 22;

    // Permission WRITE_EXTERNAL_STORAGE is not mandatory
    // for Agora RTC SDK, just in case if you wanna save
    // logs to external sdcard.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private RtcEngine mRtcEngine;
    private boolean mCallEnd;
    private boolean mMuted;

    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private SurfaceView mLocalView;
    private SurfaceView mRemoteView;

    private ImageView mCallBtn;
    private ImageView mMuteBtn;
    private ImageView mSwitchCameraBtn;
    private ImageView btn_endcall_first,btn_call_first,btn_center;

    private ImageView btn_brush;
    private ConstraintLayout layout_brush;
    private boolean brush=false;

    private SharedPreferences sf;
    private SharedPreferences sf_token;
    private SharedPreferences sf_idx;
    private String token;
    private String email;
    private String couple_idx;
    private String start;

    private String message;


    private static String IP_ADDRESS = "13.125.232.78";
    private String mJsonString;


    private SeekBar seekBar_lightening, seekBar_smoothness, seekBar_redness;
    private float lightening=0.7F,smoothness=0.5F,redness=0.1F;



    /**
     * Event handler registered into RTC engine for RTC callbacks.
     * Note that UI operations needs to be in UI thread because RTC
     * engine deals with the events in a separate thread.
     */
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("agora","Join channel success, uid: " + (uid & 0xFFFFFFFFL));

                }
            });
        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("agora","First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("agora","User offline, uid: " + (uid & 0xFFFFFFFFL));
                    onRemoteUserLeft();
                }
            });
        }
    };

    //원격 비디오 설정부분
    private void setupRemoteVideo(int uid) {
        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        int count = mRemoteContainer.getChildCount();
        View view = null;
        for (int i = 0; i < count; i++) {
            View v = mRemoteContainer.getChildAt(i);
            if (v.getTag() instanceof Integer && ((int) v.getTag()) == uid) {
                view = v;
            }
        }

        if (view != null) {
            return;
        }

        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(mRemoteView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        mRemoteView.setTag(uid);
    }

    //원격사용자가 나가면
    private void onRemoteUserLeft() {
        removeRemoteVideo();
        Dialog();
        finish();
    }

    //원격사용자 화면 제거
    private void removeRemoteVideo() {
        if (mRemoteView != null) {
            mRemoteContainer.removeView(mRemoteView);
        }
        mRemoteView = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);
        initUI();

        start = getIntent().getStringExtra("start");
        Log.e("비디오",start);


        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_token = getSharedPreferences("VIDEO",MODE_PRIVATE);
        token = sf_token.getString("video_token" + email,"");
        Log.e("비디오토큰",token);

        //커플 인덱스 번호가져오기
        sf_idx = getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        mRemoteContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brush){
                    layout_brush.setVisibility(View.GONE);
                    brush =false;
                }
            }
        });

        SeekBar();

        btn_endcall_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData task = new GetData();
                task.execute( "http://" + IP_ADDRESS + "/video_reject.php?email="+email+"&couple_idx="+couple_idx, "");
                finish();
            }
        });


        // Ask for permissions at runtime.
        // This is just an example set of permissions. Other permissions
        // may be needed, and please refer to our online documents.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initEngineAndJoinChannel();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 그 다음 AsyncTask 객체를 만들어 execute()한다

        LocalBroadcastManager.getInstance(this).registerReceiver( mMessageReceiver, new IntentFilter("custom-event-name"));

        SharedPreferences appData = getSharedPreferences("VIDEO_ON",MODE_PRIVATE);
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("video_on", true);
        editor.apply();

    }

    @Override
    protected void onPause() {
        super.onPause();


        LocalBroadcastManager.getInstance(this).unregisterReceiver( mMessageReceiver);

        SharedPreferences appData = getSharedPreferences("VIDEO_ON",MODE_PRIVATE);
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("video_on", false);
        editor.apply();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            message = intent.getStringExtra("reject");
            Log.d("receiver", "Got message: " + message);

            String end = intent.getStringExtra("endcall");

            if(message.equals("1")){
                Toast.makeText(VideoChatActivity.this, "상대방이 전화수신을 거부하였습니다.",Toast.LENGTH_SHORT).show();
                leaveChannel();
            }
            if(end.equals("1")){
                Toast.makeText(VideoChatActivity.this, "상대방이 전화를 종료하였습니다.",Toast.LENGTH_SHORT).show();
                leaveChannel();
            }
        }
    };

    private void SeekBar(){
        seekBar_lightening.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int number = seekBar.getProgress();
                lightening = (float)number/100;
                Update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar_smoothness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int number = seekBar.getProgress();
                smoothness = (float)number/100;
                Update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar_redness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int number = seekBar.getProgress();
                redness = (float)number/100;
                Update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void Update(){
        mRtcEngine.setBeautyEffectOptions(true,new BeautyOptions(BeautyOptions.LIGHTENING_CONTRAST_HIGH,lightening,smoothness,redness));
    }


    //기초 뷰 선언들
    private void initUI() {
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);
        layout_brush = findViewById(R.id.layout_brush);

        mCallBtn = findViewById(R.id.btn_call);
        mMuteBtn = findViewById(R.id.btn_mute);
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);

        btn_brush= findViewById(R.id.btn_brush);

        seekBar_lightening = findViewById(R.id.seekBar_lightening);
        seekBar_smoothness =findViewById(R.id.seekBar_smoothness);
        seekBar_redness=findViewById(R.id.seekBar_redness);

        btn_endcall_first = findViewById(R.id.btn_endcall_first);
        btn_call_first = findViewById(R.id.btn_call_first);
        btn_center = findViewById(R.id.btn_center);

    }



    //카메라 오디오 등 퍼미션
    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

    //퍼미션 체크하는부분
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                finish();
                return;
            }

            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.
            initEngineAndJoinChannel();
        }
    }

    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initEngineAndJoinChannel() {
        // This is our usual steps for joining
        // a channel and starting a call.
        initializeEngine();
        setupVideoConfig();

        mCallBtn.setVisibility(View.GONE);
        mMuteBtn.setVisibility(View.GONE);
        mSwitchCameraBtn.setVisibility(View.GONE);
        btn_brush.setVisibility(View.GONE);

        btn_call_first.setVisibility(View.VISIBLE);
        btn_endcall_first.setVisibility(View.VISIBLE);
        btn_center.setVisibility(View.VISIBLE);

        if(start.equals("1")){
            onstart();
        }
    }

    public void onstart(){
        mCallBtn.setVisibility(View.VISIBLE);
        mMuteBtn.setVisibility(View.VISIBLE);
        mSwitchCameraBtn.setVisibility(View.VISIBLE);
        btn_brush.setVisibility(View.VISIBLE);

        btn_call_first.setVisibility(View.GONE);
        btn_endcall_first.setVisibility(View.GONE);
        btn_center.setVisibility(View.GONE);

        joinChannel();
        setupLocalVideo();
    }

    public void onStartCallClicked(View view){
        onstart();
    }

    //시작전에 종료버튼 누르면 해주는처리
    public void onStartEndCallClicked(View view){
        leaveChannel();
    }

    //mrtcengine 초기화및 활성화 부분
    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    //보여주는 비디오 화질이나 프레임 속도 설정하는부분
    private void setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine.enableVideo();

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    //내가 보내는 화면 설정부분
    private void setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        mLocalView = RtcEngine.CreateRendererView(getBaseContext());
        mLocalView.setZOrderMediaOverlay(true);
        mLocalContainer.addView(mLocalView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }

    //채널에 참가하는 부분
    private void joinChannel() {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.
       // String token = getString(R.string.agora_access_token);
        if (TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
            token = null; // default, no token
        }
        mRtcEngine.joinChannel(token, couple_idx, "Extra Optional Data", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCallEnd) {
            leaveChannel();
            Dialog();
            finish();
        }
        RtcEngine.destroy();
    }

    //채널을 떠난다
    private void leaveChannel() {
        mRtcEngine.leaveChannel();
        Dialog();

        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/video_end.php?email="+email+"&couple_idx="+couple_idx, "");

        finish();
    }

    private void Dialog(){
        Toast.makeText(VideoChatActivity.this, "영상통화가 종료되었습니다.",Toast.LENGTH_SHORT).show();
    }

    //사용자가 음소거 버튼을 누르면
    public void onLocalAudioMuteClicked(View view) {
        mMuted = !mMuted;
        mRtcEngine.muteLocalAudioStream(mMuted);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn.setImageResource(res);
    }

    public void onButton(View view){
        brush =true;
        layout_brush.setVisibility(view.VISIBLE);
    }
    //사용자가 스위치 버튼을 누르면
    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }

    //사용자가 전화버튼을 누르게 되면
    public void onCallClicked(View view) {
        if (mCallEnd) {
            startCall();
            mCallEnd = false;
            mCallBtn.setImageResource(R.drawable.btn_endcall);
        } else {
            endCall();
            mCallEnd = true;
            mCallBtn.setVisibility(View.GONE);
        }

        showButtons(!mCallEnd);
    }

    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    private void endCall() {
        removeLocalVideo();
        removeRemoteVideo();
        leaveChannel();
    }

    private void removeLocalVideo() {
        if (mLocalView != null) {
            mLocalContainer.removeView(mLocalView);
        }
        mLocalView = null;
    }

    private void showButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mMuteBtn.setVisibility(visibility);
        mSwitchCameraBtn.setVisibility(visibility);
        btn_brush.setVisibility(visibility);
    }

    //json 데이터 가져오는  부분
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            progressDialog = ProgressDialog.show(Program.this,
                    "Please Wait", null, true, true);*/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null){

            }
            else {

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


            try {

                URL url = new URL(serverURL);
                Log.e("확인",serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }
}

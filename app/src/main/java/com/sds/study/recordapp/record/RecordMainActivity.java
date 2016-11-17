package com.sds.study.recordapp.record;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sds.study.recordapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordMainActivity extends AppCompatActivity{
    String TAG;
    static final int REQUEST_RECORD_PERMISSION=1;
    MediaRecorder recorder;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=this.getClass().getName();
        setContentView(R.layout.record_main);


        //recorder = new MediaRecorder();
    }

    /*저장파일 구하기*/
    public String getSaveFile(){
        File dir = new File(Environment.getExternalStorageDirectory(), "iot_record");
        Date d = new Date();
        String currentTime=new SimpleDateFormat("yyyy-MM-dd HHmmss").format(d);
        Log.d(TAG, "현재 시간은 "+currentTime);
        File saveFile = new File(dir, currentTime+".mp4");
        return saveFile.getAbsolutePath();
    }

    public void startRecord(){
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            recorder.setOutputFile(getSaveFile());
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*유저의 처리결과 받기!!*/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "requestCode:"+requestCode+",grantResults[0]:"+grantResults[0]+",grantResults"+grantResults[1]);
        switch (requestCode){
            case REQUEST_RECORD_PERMISSION:
                if(permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this,"앱사용을 위해서는 미디어 접근 권한을 주셔야 합니다", Toast.LENGTH_SHORT).show();
                }else if(permissions.length>0 && grantResults[1]==PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this,"앱사용을 위해서는 오디오 권한을 주셔야 합니다", Toast.LENGTH_SHORT).show();
                }
        }
    }

    /*각종 권한을 체크하자*/
    public void btnClick(View view){
        int writePermission= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recordPermission=ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);

        if(writePermission== PackageManager.PERMISSION_DENIED || recordPermission== PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this,
                        new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO
                        }, REQUEST_RECORD_PERMISSION);
        }else {
            startRecord();
        }
    }

}




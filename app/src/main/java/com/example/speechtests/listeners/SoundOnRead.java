package com.example.speechtests.listeners;

import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.speechtests.MainActivity;

public class SoundOnRead extends UtteranceProgressListener {
    private final MainActivity target;

    public SoundOnRead(MainActivity _target){
        target = _target;
    }
    @Override
    public void onStart(String utteranceId) {
        target.setFlagSound(1);
        System.out.println("start");
    }

    @Override
    public void onDone(String utteranceId) {
        target.setFlagSound(0);
        System.out.println("done");
    }

    @Override
    public void onError(String utteranceId) {
        target.setFlagSound(0);
    }
}

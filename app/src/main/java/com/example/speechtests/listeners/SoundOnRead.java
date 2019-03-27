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
        Toast.makeText(target, "Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDone(String utteranceId) {
        target.setFlagSound(0);
        Toast.makeText(target, "Stop", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String utteranceId) {
        target.setFlagSound(0);
    }
}

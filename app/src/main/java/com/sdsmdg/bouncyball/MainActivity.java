package com.sdsmdg.bouncyball;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    GameSurfaceView gameSurfaceView;
    String TAG = "harshit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameSurfaceView = new GameSurfaceView(this);
        GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(this, gameSurfaceView);

        setContentView(gameSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurfaceView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: called");
    }
}

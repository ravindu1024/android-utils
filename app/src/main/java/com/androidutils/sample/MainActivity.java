package com.androidutils.sample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.rw.androidutils.Log;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.enable();
        Log.d("test");
    }
}

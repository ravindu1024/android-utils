package com.androidutils.sample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.rw.androidutils.CustomAlertDialog;
import com.rw.androidutils.Log;
import com.rw.androidutils.Utilities;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.enable();
        Log.d("test");

        Utilities.showYesNoDialog(this, "Title", "Android Utils", "YES", () -> {}, "NO", () -> {});
    }
}

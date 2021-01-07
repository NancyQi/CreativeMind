package com.example.androidapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends Activity {
    private Button callbutton;
    private Button recbutton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        callbutton = (Button) findViewById(R.id.callbutton);
        recbutton = (Button) findViewById(R.id.recbutton);

        callbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Calling.class);
                startActivity(intent);

            }
        });

        recbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Recognize.class);
                startActivity(intent);
            }
        });
    }
}
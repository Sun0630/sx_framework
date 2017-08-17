package com.sx.essayjoke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {

    private static final long WAIT_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // wait for a moment start activity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // start activity
                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, WAIT_TIME);

    }
}

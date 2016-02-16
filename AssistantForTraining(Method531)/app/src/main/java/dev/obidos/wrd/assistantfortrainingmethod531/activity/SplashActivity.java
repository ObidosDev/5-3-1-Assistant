package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import dev.obidos.wrd.assistantfortrainingmethod531.R;

/**
 * Created by vobideyko on 8/14/15.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Runnable r = new Runnable(){

            @Override
            public void run() {
                refreshAllDataAboutAllExercisesAndDates();
                //end of computing
                startNextActivity();
            }
        };
        findViewById(R.id.ivImage).postDelayed(r, 1500);
    }

    private void startNextActivity(){
        finish();
        Intent intent = new Intent(SplashActivity.this, ExercisesActivity.class);
        startActivity(intent);
    }
}
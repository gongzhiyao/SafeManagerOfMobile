package com.example.gongzhiyao.safemanagerofmobile;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.gongzhiyao.safemanagerofmobile.Log.L;

public class MainActivity extends AppCompatActivity {

    private L log;
    private TextView tv_banben;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        log=new L();
        tv_banben= (TextView) findViewById(R.id.tv_banben);

        AlphaAnimation animation= new AlphaAnimation(0.1f,1.0f);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        tv_banben.setAnimation(animation);
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    sleep(2000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }
}

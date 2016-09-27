package com.example.nhan.hack2pokemon.activities;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nhan.hack2pokemon.R;
import com.example.nhan.hack2pokemon.constant.Constant;
import com.example.nhan.hack2pokemon.utils.Utils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay;
    private Button btnSetting;
    private TextView tvHomeScore;
    private TextView tvHighScore;
    private int homeScore;
    private int highScore;
    private MediaPlayer mediaPlayer;

    private FrameLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_not_move);
        setContentView(R.layout.activity_home);
        rootLayout = (FrameLayout) findViewById(R.id.root_layout_home);
        initLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = new MediaPlayer();
        Utils.setDataSourceForMediaPlayer(this, mediaPlayer, Constant.MUSIC_HOME);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

    private void initLayout(){
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnSetting = (Button) findViewById(R.id.btn_setting);

        btnPlay.setOnClickListener(this);
        btnSetting.setOnClickListener(this);

        //get high score from preference
        highScore = Utils.getIntFromPreference(this, Constant.HIGH_SCORE_NAME_PREF);

        //set size and position of btn Play
        int size = (int) (Utils.getActivityWidthPixel(this) * 0.4f);

        btnPlay.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
        btnPlay.setX(Utils.getActivityWidthPixel(this) / 2 - size / 2);
        btnPlay.setY(Utils.getActivityHeightPixel(this) * 5.05f / 8.1f - size / 2);

        tvHomeScore = (TextView) findViewById(R.id.tv_home_score);
        tvHighScore = (TextView) findViewById(R.id.tv_high_score);
        tvHomeScore.setTypeface(Utils.loadFontFromAssetFolder(this, Constant.FONT_SCORE));
        tvHighScore.setTypeface(Utils.loadFontFromAssetFolder(this, Constant.FONT_HIGH_SCORE));

        if (highScore == 0){
            tvHighScore.setText(R.string.highscore);
        } else {
            tvHighScore.setText(getString(R.string.highscore) + " " +String.valueOf(highScore));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                Intent intentPlay = new Intent(HomeActivity.this, PlayActivity.class);
                startActivityForResult(intentPlay, RESULT_FIRST_USER);
                break;
            case R.id.btn_setting:
                Intent intentSetting = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intentSetting);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createCircularReveal(float startRadius, float finalRadius){
        int cx = (int) (Utils.getActivityWidthPixel(this) / 2);
        int cy = (int) (Utils.getActivityHeightPixel(this) * 5.05f / 8.1f);
        Animator animator = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, startRadius, finalRadius);
        animator.setDuration(500);
        rootLayout.setVisibility(View.VISIBLE);
        animator.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            homeScore = data.getIntExtra(Constant.SCORE_KEY, 0);
            tvHomeScore.setText(String.valueOf(homeScore));
            if (homeScore > Utils.getIntFromPreference(this, Constant.HIGH_SCORE_NAME_PREF)){
                highScore = homeScore;
                tvHighScore.setText(R.string.new_high_score);
                Utils.saveIntToPreference(this, Constant.HIGH_SCORE_NAME_PREF, highScore);
            }
        } else {
            tvHighScore.setText(getString(R.string.highscore) + ": " +String.valueOf(highScore));
        }
    }


}

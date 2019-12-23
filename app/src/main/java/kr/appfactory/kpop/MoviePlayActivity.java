package kr.appfactory.kpop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MoviePlayActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    YouTubePlayerView youtubeView;
    YouTubePlayer mPlayer;
    Toolbar myToolbar;
    public String videoId;
    public String subject;
    public String videodesc;
    public String publishedAt;
    public String thum_pic;
    public int viewcnt = 0;
    public int loadingresult = 0;

    DBHelper dbHelper;

    YouTubePlayer.OnInitializedListener listener;


    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewcnt = SharedPreference.getIntSharedPreference(getApplicationContext(), "viewcnt");
        viewcnt ++ ;
        SharedPreference.putSharedPreference(getApplicationContext(), "viewcnt", viewcnt);
        setContentView(R.layout.activity_movie_play);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 화면을 landscape(가로) 화면으로 고정하고 싶은 경우

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);

        dbHelper = new DBHelper(getApplicationContext());
        Intent intent = getIntent();
        loadingresult = viewcnt % 5;
        if (loadingresult == 0 ) AdsFull.getInstance(getApplicationContext()).setAdsFull();



        youtubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        youtubeView.initialize(getResources().getString(R.string.mapi_key), this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {


        //mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        // 비디오 아이디
        Intent intent = getIntent();
        String videoId = intent.getStringExtra("videoId");
        enableDisableView(youtubeView, true);
        mPlayer = youTubePlayer;
        mPlayer.loadVideo(""+ videoId);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            mPlayer.release();
        }
    }
}



package j.hig.tictactoe;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public class MainActivity extends Activity {

    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mPlayer = MediaPlayer.create(this, R.raw.a_guy_1_epicbuilduploop);
        mPlayer.setVolume(0.5f, 0.5f);
        mPlayer.setLooping(true);
        mPlayer.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
    }
}

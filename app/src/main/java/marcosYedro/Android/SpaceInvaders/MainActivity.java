package marcosYedro.Android.SpaceInvaders;

import marcosYedro.Android.SpaceInvaders.GameView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity{
	private GameView gameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		gameView = new GameView(this,this);
		setContentView(gameView);
	}

	@Override
    public void onPause(){
		super.onPause();
		gameView.pauseMusicPlayer();
    }
	
	@Override
	public void onResume(){
		super.onResume();
		gameView.startMusicPlayer();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		gameView.stopMusicPlayer();
		gameView.releaseMusicPlayer();
	} 
}
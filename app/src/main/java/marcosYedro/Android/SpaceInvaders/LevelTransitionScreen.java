package marcosYedro.Android.SpaceInvaders;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;

public class LevelTransitionScreen {
	private GameView gameView;
	private Paint nextLevelMessage = new Paint();
	private Typeface typeface = Typeface.create("Helvetica",Typeface.BOLD);
	private int x;
	private int y;
	
	public LevelTransitionScreen(GameView gameView){
		this.gameView = gameView;
		nextLevelMessage.setTypeface(typeface);
		nextLevelMessage.setColor(Color.WHITE);
		nextLevelMessage.setTextSize(60);
		nextLevelMessage.setTextAlign(Align.CENTER);
		x = gameView.getWidth()/2;
		y = gameView.getHeight()/6;
	}

	@SuppressLint("WrongCall")
	public void draw(Canvas canvas, int level) {
		//canvas.drawText("Level ",x,y,nextLevelMessage);
		canvas.drawText("Level "+level,x, gameView.getWidth()/2,nextLevelMessage);
	}
}

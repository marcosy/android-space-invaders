package marcosYedro.Android.SpaceInvaders;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

public class WelcomeScreen {
	private GameView gameView;
	private Paint welcomeMessage = new Paint();
	private Paint detailsMessage = new Paint();
	private Typeface typeface = Typeface.create("Helvetica",Typeface.BOLD);
	private int x;
	private int y;
	private InvaderSpaceShip ship; 
	
	public WelcomeScreen(GameView gameView){
		Bitmap bmpInvader_1 = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.biginvader_1);
		this.gameView = gameView;
		
		this.ship = new InvaderSpaceShip(gameView,
				bmpInvader_1,1,15,
				gameView.getWidth()/2-bmpInvader_1.getWidth()/30,
				gameView.getHeight()/2-bmpInvader_1.getHeight(),
				100);
		
		welcomeMessage.setTypeface(typeface);
		welcomeMessage.setColor(Color.WHITE);
		welcomeMessage.setTextSize(60);
		welcomeMessage.setTextAlign(Align.CENTER);
		x = gameView.getWidth()/2;
		y = gameView.getHeight()/6;
		
		detailsMessage.setColor(Color.LTGRAY);
		detailsMessage.setTextAlign(Align.CENTER);
		detailsMessage.setTextSize(30);
	}

	@SuppressLint("WrongCall")
	public void draw(Canvas canvas) {
		canvas.drawText("Android Space Invader",x,y,welcomeMessage);
		canvas.drawText("Tap to start...",x, (int) (5.5 * y) ,welcomeMessage);
		
		canvas.drawText("Marcos G. Yedro",x,gameView.getHeight()/2 +ship.getHeight()/2+20,detailsMessage);
		canvas.drawText("Proyecto Final - Dispositivos Moviles",x,gameView.getHeight()/2 +ship.getHeight()/2+50,detailsMessage);
		canvas.drawText("FICH-UNL 2014",x,gameView.getHeight()/2 +ship.getHeight()/2+90,detailsMessage);
		
		ship.onDraw(canvas);
	}
}

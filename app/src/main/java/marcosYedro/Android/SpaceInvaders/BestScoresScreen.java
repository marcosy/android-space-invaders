package marcosYedro.Android.SpaceInvaders;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.text.InputType;
import android.widget.EditText;

public class BestScoresScreen {
	private GameView gameView;
	private Context context;
	private boolean bestScore = false;
	private Paint titlePaint = new Paint();
	private Paint descriptionPaint = new Paint();
	private Typeface typeface = Typeface.create("Helvetica",Typeface.BOLD);
	String nameBest1,nameBest2,nameBest3;
	int scoreBest1, scoreBest2,scoreBest3;
	private int x;
	private int y;
	private String playerName;
	AlertDialog.Builder builder;
	private int actualScore;
	private boolean scoreSet = false;
	
	public BestScoresScreen(GameView gameView, Context context){
		this.context = context;
		this.gameView = gameView;
		
		//Get reference to preferences file
		getScoreValues();
		createInput();
		
		titlePaint.setTypeface(typeface);
		titlePaint.setColor(Color.WHITE);
		titlePaint.setTextSize(60);
		titlePaint.setTextAlign(Align.CENTER);
		descriptionPaint.setColor(Color.WHITE);
		descriptionPaint.setTextAlign(Align.CENTER);
		descriptionPaint.setTextSize(35);
		x = gameView.getWidth()/2;
	}

	@SuppressLint("WrongCall")
	public void draw(Canvas canvas) {
		
		if (bestScore) {
			if(!scoreSet && playerName!=null){
				setScoreValues();
				scoreSet = true;
			}
			
			y = gameView.getHeight()/6;
			canvas.drawText("Well Done!",x,y,titlePaint);
			y = gameView.getHeight()/3;
			canvas.drawText(nameBest1 +" | score: [ "+scoreBest1+" ]",x, y,descriptionPaint);
			y = y +gameView.getHeight()/6;
			canvas.drawText(nameBest2 +" | score: [ "+scoreBest2+" ]",x, y,descriptionPaint);
			y = y +gameView.getHeight()/6;
			canvas.drawText(nameBest3 +" | score: [ "+scoreBest3+" ]",x, y,descriptionPaint);
		}
		else {
			y = gameView.getHeight()/2;
			canvas.drawText("Game Over!",x,y,titlePaint);
		}
	}

	public void showInput(int actualScore) {
		this.actualScore = actualScore;
		bestScore = (actualScore > scoreBest3) ? true : false;
		builder.show();
	}
	
	private void createInput() {
		this.builder = new AlertDialog.Builder(context);
		final EditText input = new EditText(context);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		
		// Set up buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        playerName = input.getText().toString();
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});
	}
	
	private void getScoreValues() {
		SharedPreferences bestScores = context.getSharedPreferences("BestScores", Context.MODE_PRIVATE);
		nameBest1 = bestScores.getString("bestPlayer1", "Player");
		nameBest2 = bestScores.getString("bestPlayer2", "Player");
		nameBest3 = bestScores.getString("bestPlayer3", "Player");
		scoreBest1 = bestScores.getInt("bestScore1", 0);
		scoreBest2 = bestScores.getInt("bestScore2", 0);
		scoreBest3 = bestScores.getInt("bestScore3", 0);
	}
	
	public void setScoreValues() {
		bestScore = true;
		SharedPreferences bestScores = context.getSharedPreferences("BestScores", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = bestScores.edit();
		
		if ( actualScore > scoreBest1 ) {
			editor.putInt("bestScore3", scoreBest2);
			editor.putString("bestPlayer3", nameBest2);
			
			editor.putInt("bestScore2", scoreBest1);
			editor.putString("bestPlayer2", nameBest1);
			
			editor.putInt("bestScore1", actualScore);
			editor.putString("bestPlayer1", playerName);
		}
		else {
			if ( actualScore > scoreBest2 ) {
				editor.putInt("bestScore3", scoreBest2);
				editor.putString("bestPlayer3", nameBest2);
				
				editor.putInt("bestScore2", actualScore);
				editor.putString("bestPlayer2", playerName);
			}
			else {
				editor.putInt("bestScore3", actualScore);
				editor.putString("bestPlayer3", playerName);
			}
		}

		// Commit changes
		editor.commit();
		
		//Update changes in class (and screen)
		getScoreValues();
	}
}

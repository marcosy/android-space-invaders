package marcosYedro.Android.SpaceInvaders;

import android.graphics.Bitmap;

public class InvaderSpaceShip extends Sprite{
	protected int points;
	
	public InvaderSpaceShip(GameView gameView, Bitmap bmp, int spriteRows, int spriteColumns, int x, int y, int points) {
		super(gameView, bmp, spriteRows,spriteColumns);
		
		//Depends on each type of sprite
		this.x = x;//(gameView.getWidth() - width)/2;
		this.y = y;//10;//gameView.getHeight() - height;
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.points = points;
	}

	@Override
	protected void update() {
		if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
			//xSpeed = -xSpeed;
		}
		else {
			x = x + xSpeed;
		}
		
		currentFrameColumn = getNextAnimationColumn();
	}
	
	@Override
	protected int getNextAnimationRow() {
		return 0;
	}
	
	@Override
	protected int getNextAnimationColumn() {
		
		return ++currentFrameColumn % bmpColumns;
	}

	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public void moveDown(int stepDown) {
		this.y = y + stepDown;	
	}
}

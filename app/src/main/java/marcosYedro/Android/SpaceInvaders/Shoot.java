package marcosYedro.Android.SpaceInvaders;

import android.graphics.Bitmap;

public class Shoot extends Sprite {

	boolean invaderShoot;
	
	public Shoot(GameView gameView, Bitmap bmp, int bmpRows, int bmpColumns, int x, int y,int xSpeed, boolean invaderShoot,boolean alive) {
		super(gameView, bmp, bmpRows, bmpColumns);
		
		this.x = x;
		this.y = y;
		this.xSpeed = xSpeed;
		this.ySpeed = invaderShoot ? 25 : -35;
		this.alive = alive;
		this.invaderShoot = invaderShoot;
	}

	@Override
	protected void update() {
		if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
			alive = false;
		}
		else {
			x = x + xSpeed;
		}
		
		if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
			alive = false;
		}
		else {
			y = y + ySpeed;
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

	public int getBottom() {
		return y+width;
	}
}

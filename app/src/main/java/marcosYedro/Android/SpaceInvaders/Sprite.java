package marcosYedro.Android.SpaceInvaders;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	
	protected int bmpRows = 2;
	protected int bmpColumns = 12;
	protected Rect src;
	protected Rect dst;
	
	protected GameView gameView;
	protected Bitmap bmp;
	protected int x = 0;
	protected int y = 0;
	protected int xSpeed;
	protected int ySpeed;
	protected int currentFrameColumn = 0;
	protected int currentFrameRow = 0;
	protected int width;
	protected int height;
	protected boolean alive;
	
	public Sprite(GameView gameView, Bitmap bmp, int bmpRows, int bmpColumns) {
		//Depends on each type of sprite
		this.bmpRows = bmpRows;
		this.bmpColumns = bmpColumns;
		
		//The same for all sprites
		this.width = bmp.getWidth() / bmpColumns;
		this.height = bmp.getHeight() / bmpRows;
		this.gameView = gameView;
		this.bmp = bmp;

		// Initialize Rect
		int srcX = currentFrameColumn * width;
		int srcY = currentFrameRow * height;
		src = new Rect(srcX, srcY, srcX + width, srcY + height);
		dst = new Rect(x, y, x + width, y + height);
		
		//Depends on each type of sprite
		//Random rnd = new Random();
		x = 30;//rnd.nextInt(gameView.getWidth() - width);
		y = 30;//rnd.nextInt(gameView.getHeight() - height);
		xSpeed = 0;
		ySpeed = 0;
		alive = true;
	}

	public int getBmpRows() {
		return this.bmpRows;
	}
	public int getBmpColumns() {
		return this.bmpColumns;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height=height;
	}
	
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	protected void update() {
		if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
			xSpeed = -xSpeed;
		}
		x = x + xSpeed;
		if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
			ySpeed = -ySpeed;
		}
		y = y + ySpeed;

		currentFrameColumn = ++currentFrameColumn % bmpColumns;
	}

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrameColumn * width;
		int srcY = currentFrameRow * height;
		//src = new Rect(srcX, srcY, srcX + width, srcY + height);
		//Rect dst = new Rect(x, y, x + width, y + height);
		
		src.left=srcX;
		src.top = srcY;
		src.right = srcX + width;
		src.bottom = srcY + height;
		dst.left=x;
		dst.top=y;
		dst.right=x+width;
		dst.bottom=y+height;
		
		canvas.drawBitmap(bmp, src, dst, null);
	}

	protected int getNextAnimationRow() {
		return 0;
	}

	protected int getNextAnimationColumn() {
		return 0;
	}

	public boolean isCollition(float x2, float y2) {
		return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
	}
	
	public boolean isCollition(Sprite sprite2) {
		// Detects collision between two rectangles (sprites)
		if(
			this.x < sprite2.getX() + sprite2.getWidth() &&
			this.x + this.width > sprite2.getX() &&
			this.y + this.height > sprite2.getY() &&
			this.y < sprite2.getY() + sprite2.getHeight()
		  )
		{
			return true;	
		}
		else {
			return false;
		}
	}
}

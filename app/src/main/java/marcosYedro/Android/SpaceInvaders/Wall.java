package marcosYedro.Android.SpaceInvaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Wall {
	private List<Brick> wall = new ArrayList<Brick>();
	private int xUnits;
	private int yUnits;
	//private int x;
	//private int y;
	//private int spriteColumns;
	private int spriteRows = 1;
	
	public Wall(GameView gameView, Bitmap brickBmp,int spriteColumns,int horizontalNumOfBrick,int verticalNumOfBrick, int x, int y) {
//		this.x = x;
//		this.y = y;
//		this.spriteColumns=spriteColumns;
		this.xUnits=horizontalNumOfBrick;
		this.yUnits=verticalNumOfBrick;
		
		//Creates a Wall
		int x_brick, y_brick;
		
		for (int i = 0; i<xUnits;i++){
			x_brick = i * brickBmp.getWidth()/spriteColumns;
			
			for(int j = 0; j < yUnits; j++){	
				y_brick = j * brickBmp.getHeight()/spriteRows;
				wall.add(new Brick(gameView,brickBmp,spriteRows,spriteColumns,x+x_brick,y+y_brick) );
			}
		}
		
	}
	
	public boolean isCollision(Sprite sprite){
		
		boolean collision = false;
				
		for (Iterator<Brick> iterator = wall.iterator(); iterator.hasNext();) { 
			Brick brick = iterator.next();
		    if(brick.isCollition(sprite)){
		    	brick.degrade();
		    	if(!brick.isAlive()){
			    	// Remove the current element from the iterator and the list.
			        iterator.remove();
		    	}
		    	collision = true;
		    }
		}
		
		return collision;
	}
	

	public int getSize() {
		
		return wall.size();
	}

	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas) {
		for(Brick brick : wall ){
			brick.onDraw(canvas);
		}
		
	}
	
	
}

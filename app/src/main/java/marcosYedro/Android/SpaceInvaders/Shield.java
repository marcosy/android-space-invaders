package marcosYedro.Android.SpaceInvaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Shield {
	List<Wall> shield = new ArrayList<Wall>();
	int y;
	
	public Shield(GameView gameView, Bitmap brickBmp,int spriteColumns,int horizontalNumOfBrick,int verticalNumOfBrick, int x, int y, int numberOfWalls, int separationX) {
		this.y = y;
		int x_shield;
		for (int i=0; i<numberOfWalls; i++) {
			x_shield =i*separationX;
			shield.add(new Wall(gameView,brickBmp,spriteColumns,horizontalNumOfBrick,verticalNumOfBrick,x+x_shield,y));
		}
	}
	
	public boolean isCollision(Sprite sprite){
		for(Iterator<Wall> iterator = shield.iterator(); iterator.hasNext(); ){
			Wall wall = iterator.next();
			if(wall.isCollision(sprite)){
				if(wall.getSize()==0){
					iterator.remove();
				}
				return true;
			}
		}
		return false;
	}
	
	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas){
		for(Wall wall : shield ) {
			wall.onDraw(canvas);
		}
	}
	
	public boolean isAlive(){
		if(shield.size()!=0){
			return true;
		}
		else {
			return false;
		}
	}

	public int getY() {
		return y;
	}

	public boolean isCollision2(InvaderSpaceFleet invaderSpaceFleet) {
		boolean collision = false;
		for (List<InvaderSpaceShip> fleetRow : invaderSpaceFleet.getInvaderSpaceShips()) {
			for (InvaderSpaceShip sprite : fleetRow) {
				collision=this.isCollision(sprite);
			}
		}
		return collision;
	}
}

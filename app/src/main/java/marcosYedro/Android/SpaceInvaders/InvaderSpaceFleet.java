package marcosYedro.Android.SpaceInvaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

@SuppressLint("WrongCall")
public class InvaderSpaceFleet {
	private List< List<InvaderSpaceShip> > invaderSpaceShips;
	private GameView gameView;
	private int spaceBetweenRows=25;
	private int periodMotherShip=600;
	private int timerMotherShip=600;
	private int periodMoveDown = 200;
	private int timerMoveDown =200;
	private int stepDown = 4;
	private List<InvaderSpaceShip> motherShipList;
	private int lastDestroyedShipPoints;
	private int landingHeight;
	private boolean arrive = false;
	Bitmap bmpMotherShip;
	Bitmap bmpInvader_1;
	Bitmap bmpInvader_2;
	private int numberOfShips=0;
	
	public InvaderSpaceFleet(GameView gameView, int landingHeight,int level) {
		timerMoveDown = timerMoveDown/(2*level);
		periodMoveDown = periodMoveDown/(2*level);
		numberOfShips = (level<=5)?(2 + level):7;
		
	  	this.gameView = gameView;
		this.landingHeight = landingHeight;
		this.bmpMotherShip = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.mothership);
		this.bmpInvader_1 = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.invaderspaceship_1);
		this.bmpInvader_2 = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.invaderspaceship_2);
		 
		invaderSpaceShips = new ArrayList < List<InvaderSpaceShip> >();
		invaderSpaceShips.add(createInvaderSpaceFleetRow(0, bmpInvader_2,1,2,numberOfShips,200) );
		invaderSpaceShips.add(createInvaderSpaceFleetRow(1, bmpInvader_1,1,15,numberOfShips-1,100) );
		invaderSpaceShips.add(createInvaderSpaceFleetRow(2, bmpInvader_2,1,2,numberOfShips,50) );
		invaderSpaceShips.add(createInvaderSpaceFleetRow(3, bmpInvader_1,1,15,numberOfShips-1,25) );
	}

	//Getters and setters
    public List<List<InvaderSpaceShip>> getInvaderSpaceShips() {
		return this.invaderSpaceShips;
	}
		
	public void setInvaderSpaceShips(List<List<InvaderSpaceShip>> invaderSpaceShips) {
		this.invaderSpaceShips = invaderSpaceShips;
	}
	
	public void onDraw(Canvas canvas){
		for (List<InvaderSpaceShip> fleetRow : invaderSpaceShips) {
			for (Sprite sprite : fleetRow) {
	            sprite.onDraw(canvas);
			}
		}
		
		//Timers
		timerMotherShip = timerMotherShip - 1;
		timerMoveDown = timerMoveDown - 1;
		if(timerMotherShip==0) {
			if (!invaderSpaceShips.contains(motherShipList)) {
				createMotherShip();
			}
			else {
				timerMotherShip = periodMotherShip;
			}
		}
		
		if (timerMoveDown == 0) {
			moveDown();
			timerMoveDown = periodMoveDown;
		}
	}

	private void moveDown() {
		for (List<InvaderSpaceShip> fleetRow : invaderSpaceShips){
			for (InvaderSpaceShip sprite : fleetRow) {
				sprite.moveDown(stepDown);
				
				if(sprite.getY()>landingHeight){
					arrive = true;
				}
			}
		}
	}

	// Creates an invader space fleet
    private List<InvaderSpaceShip> createInvaderSpaceFleetRow(int fleetRow,Bitmap bmp, int spriteRows, int spriteColumns,int numberOfShips,int points){
    	ArrayList<InvaderSpaceShip> row = new ArrayList<InvaderSpaceShip>();
		int shipWidth = bmp.getWidth()/spriteColumns;
		int dx = (gameView.getWidth() - shipWidth)/(numberOfShips-1); 
	   
		int x = 0;
		int y = spaceBetweenRows;
	   
		for(int i = 0; i<numberOfShips;i++){
			row.add(createInvaderSpaceShip(fleetRow,bmp,spriteRows,spriteColumns,x,y,points));
			x = x + dx;
		}
		spaceBetweenRows = spaceBetweenRows+bmp.getHeight();
		return row;
}
    
    // Creates an invader ship
    private InvaderSpaceShip createInvaderSpaceShip(int fleetRow, Bitmap bmp, int spriteRows, int spriteColumns, int x, int y, int points) {
 	   InvaderSpaceShip invaderSpaceShip = new InvaderSpaceShip(gameView, bmp, spriteRows, spriteColumns, x, y,points);
 	   return invaderSpaceShip;
    }
    
    // Create a motherShip
    public void createMotherShip(){
    	int spriteRows = 1;
    	int spriteColumns =2;
    	int x = 0;
    	int y = 0;
    	int xSpeed =15;
    	int ySpeed = 0;
    	
    	motherShipList = new ArrayList<InvaderSpaceShip>();
    	InvaderSpaceShip motherShip = new MotherShip(gameView, bmpMotherShip, spriteRows, spriteColumns,x,y,xSpeed,ySpeed,300);
    	motherShipList.add(motherShip);
    	invaderSpaceShips.add(motherShipList);
    	
    	//Initiate timer
    	timerMotherShip = periodMotherShip;
    }

	public boolean invaderArrive() {
		return arrive;
	}
	
	public boolean allInvadersDestroyed(){
		boolean allDestroyed = false;
		if(invaderSpaceShips.size() == 0){
			allDestroyed = true;
		}
		return allDestroyed;
	}

	public Sprite getShooter() {
		Random rnd = new Random();
		int shooterRow = 0;//rnd.nextInt(invaderSpaceShips.size());
		int shooterCol = rnd.nextInt(invaderSpaceShips.get(shooterRow).size());
		
		return invaderSpaceShips.get(shooterRow).get(shooterCol);
	}

	public boolean isCollision(Sprite goodSpaceShipShoot) {
	
		for (List<InvaderSpaceShip> fleetRow : invaderSpaceShips){
			for (InvaderSpaceShip sprite : fleetRow) {
	            
				if(sprite.isCollition(goodSpaceShipShoot)){
					// Delete Sprite
					lastDestroyedShipPoints = sprite.getPoints();
					fleetRow.remove(sprite);
					
					// If row is empty, delete row too
					if(fleetRow.size()==0){
						invaderSpaceShips.remove(fleetRow);
					}
					return true;
	            }
			}
		}

		return false;
	}
	
	public int getPoints(){
		return lastDestroyedShipPoints;
	}
	
}

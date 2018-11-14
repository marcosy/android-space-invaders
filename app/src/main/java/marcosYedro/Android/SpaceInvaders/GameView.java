package marcosYedro.Android.SpaceInvaders;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
 
@SuppressLint("WrongCall")
public class GameView extends SurfaceView {
       private MainActivity activity;
	   private Context context;
	   private GameLoopThread gameLoopThread;
	   private InvaderSpaceFleet invaderSpaceFleet;
	   private boolean firstStart = true;
       private Explosion explosion;
       private GoodSpaceShip goodSpaceShip; 
       private Shoot goodSpaceShipShoot;
       private Shoot invaderSpaceShipShoot;
       private Shield shield;
       private long lastClick;
       private Bitmap bmpExplosion;
       private Bitmap bmpGoodShoot;
       private Bitmap bmpInvaderShoot;
       private Bitmap bmpBrick;
       private int level = 1;
       private int scoreValue=0;
       private int scoreBest3;
       Paint scorePaint = new Paint();
       Typeface scoreTypeFace = Typeface.create("Helvetica",Typeface.BOLD);
       private int gameState = 1; // 1 WelcomeScreen; 2 Play; 3 GameOVer; 4 Next Level

       WelcomeScreen welcomeScreen;
       LevelTransitionScreen levelTransitionScreen;
       BestScoresScreen bestScoresScreen;

       //Sound stuff
       private SoundPool soundPool;
       private MediaPlayer mPlayer;
       private int soundId;
       
       public GameView(Context context, MainActivity activity) {
             super(context);
             this.activity = activity;
             this.context = context;
             
             // Config background Music
             if (mPlayer == null){
	             mPlayer = MediaPlayer.create(getContext(), R.raw.music);
	             mPlayer.setLooping(true);                  
             }

             // Load sounds
             soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
             soundId = soundPool.load(getContext(), R.raw.puaj, 1);

             gameLoopThread = new GameLoopThread(this);
             
             getHolder().addCallback(new SurfaceHolder.Callback() {
 
                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {
                           boolean retry = true;
                           gameLoopThread.setRunning(false);
                           while (retry) {
                                  try {
                                        gameLoopThread.join();
                                        retry = false;
                                  } catch (InterruptedException e) {}
                           }
                    }
 
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                    	bmpExplosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
                    	bmpGoodShoot = BitmapFactory.decodeResource(getResources(), R.drawable.goodspaceshipshoot);
                    	bmpInvaderShoot = BitmapFactory.decodeResource(getResources(), R.drawable.invaderspaceshipshoot);
                    	bmpBrick = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
                    	createScreens();
                    	createGoodSpaceShip();
                    	createInvaderSpaceFleet(level);
                    	createGoodSpaceShipShoot(false);
                    	createInvaderSpaceShipShoot(false);
                    	createExplosion(false,false);
                    	createShield();
                    	
                    	gameLoopThread.setRunning(true);
                    	if(firstStart){
                    		gameLoopThread.start();
                    		firstStart=false;
                    	}
                    }
 
                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
             });
       }
       
       public GameLoopThread getGameLoopThread() {
    	   return gameLoopThread;
       }
       
       private void createScreens(){
    	   this.welcomeScreen = new WelcomeScreen(this);
    	   this.levelTransitionScreen = new LevelTransitionScreen(this);
    	   this.bestScoresScreen = new BestScoresScreen(this,context);
    	   SharedPreferences bestScores = context.getSharedPreferences("BestScores",Context.MODE_PRIVATE);
    	   scoreBest3 = bestScores.getInt("bestScore3", 0);
       }

       // Creates a good ship
       private void createGoodSpaceShip(){
    	   Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.goodspaceship);
       	   this.goodSpaceShip = new GoodSpaceShip(this,bmp,1,13,context);   
       }

       // Creates an invader ship
       private void createInvaderSpaceFleet(int level){
    	 int landingHeight = goodSpaceShip.getHeight(); 
    	 this.invaderSpaceFleet = new InvaderSpaceFleet(this,this.getHeight()-landingHeight,level);
       }

       //Creates a good ship shoot  
       private void createGoodSpaceShipShoot(boolean alive) {
    	   int spriteColumns = 2;
    	   int spriteRows = 1;
    	   int x = goodSpaceShip.getX()+goodSpaceShip.getWidth()/2 - (bmpGoodShoot.getWidth()/spriteColumns) / 2;
    	   int y = goodSpaceShip.getY();
    	   int xSpeed = goodSpaceShip.getXSpeed();
    	   this.goodSpaceShipShoot = new Shoot(this,bmpGoodShoot,spriteRows,spriteColumns,x,y,xSpeed,false,alive);
       }
       
       //Creates a invader ship shoot  
       private void createInvaderSpaceShipShoot(boolean alive) {
    	   int spriteColumns = 9;
    	   int spriteRows = 1;
    	   Sprite shooterShip = invaderSpaceFleet.getShooter();
    	   int x = shooterShip.getX()+shooterShip.getWidth()/2 - (bmpInvaderShoot.getWidth()/spriteColumns) / 2;
    	   int y = shooterShip.getY();
    	   this.invaderSpaceShipShoot = new Shoot(this,bmpInvaderShoot,spriteRows,spriteColumns,x,y,0,true,alive);
       }
     
       //Creates a explosion 
       private void createExplosion(boolean alive, boolean invaderShip) {
    	   int spriteColumns = 25;
    	   int spriteRows = 1;
    	   int x,y;
    	   if(invaderShip){
	    	   x = goodSpaceShipShoot.getX()+goodSpaceShipShoot.getWidth()/2 - (bmpExplosion.getWidth()/spriteColumns) / 2;
	    	   y = goodSpaceShipShoot.getY()-goodSpaceShipShoot.getHeight()/2;
    	   }else{
    		   x= goodSpaceShip.getX();
			   y= goodSpaceShip.getY();
    	   }	
    	   explosion = new Explosion(this,bmpExplosion,spriteRows,spriteColumns,x,y,alive);
       }
       
       private void createShield(){
    	   int horizontalNumOfBrick = 8;
    	   int verticalNumOfBrick = 4;
    	   int spriteColumns = 4;
    	   int numberOfWalls = 3;
    	   
    	   int wallWidth = bmpBrick.getWidth()/spriteColumns * horizontalNumOfBrick;
    	   int separation = (this.getWidth() - wallWidth)/(numberOfWalls-1); 
   	   
    	   int x_ini = 0;
    	   int y_ini = goodSpaceShip.getY() - bmpBrick.getWidth() - 10;
   	   
    	   for(int i = 0; i<numberOfWalls;i++){
    		   shield = new Shield(this,bmpBrick,spriteColumns,horizontalNumOfBrick,verticalNumOfBrick,x_ini,y_ini,numberOfWalls,separation);
    	   }
       }
       
       private void drawScore(Canvas canvas){ 	
           scorePaint.setTypeface(scoreTypeFace);
           scorePaint.setColor(Color.WHITE);
           scorePaint.setTextSize(30);
           canvas.drawText("Score: "+scoreValue+"| Level: "+level,0,30,scorePaint);
       }
       
       private void checkCollisions() {
    	   //Collision good ship shoot vs invader fleet
    	   if (goodSpaceShipShoot.isAlive()){

    		   if(invaderSpaceFleet.isCollision(this.goodSpaceShipShoot)){
    			   scoreValue = scoreValue + invaderSpaceFleet.getPoints();
    			   goodSpaceShipShoot.setAlive(false);
    			   createExplosion(true,true);
    		   }
    	   }

    	   //Collision good ship shoot vs invader ship shoot
    	   if(goodSpaceShipShoot.isAlive() && invaderSpaceShipShoot.isAlive()){
    		   if(invaderSpaceShipShoot.isCollition(this.goodSpaceShipShoot)){
    			   this.invaderSpaceShipShoot.setAlive(false);
    			   this.goodSpaceShipShoot.setAlive(false);
    		   }   
    	   }

    	   //Collision good ship       vs invader ship shoot
    	   if(invaderSpaceShipShoot.getBottom()>goodSpaceShip.getY() && invaderSpaceShipShoot.isAlive()){
    		   if(goodSpaceShip.isCollition(this.invaderSpaceShipShoot)){
    			   this.invaderSpaceShipShoot.setAlive(false);
    			   this.goodSpaceShip.setAlive(false);
    			   createExplosion(true,false);
    		   }
    	   }
    	   
    	   //Shield collisions
    	   if(shield.isAlive()){
    		   
    		   //Collision shield       vs invader ship shoot
    		   if(invaderSpaceShipShoot.getBottom()>shield.getY() && invaderSpaceShipShoot.isAlive()){
    			   if(shield.isCollision(invaderSpaceShipShoot)){
    				   this.invaderSpaceShipShoot.setAlive(false);
    			   }
    		   }

    		   //Collision shield       vs good ship shoot
    		   if(goodSpaceShipShoot.isAlive()){
    			   if(shield.isCollision(goodSpaceShipShoot)){
    				   this.goodSpaceShipShoot.setAlive(false);
    			   }
    		   }
    		   
    		 //Collision shield       vs invader fleet
    		   if(shield.isCollision2(invaderSpaceFleet)){
    			   
    		   }
    	   }
       }

       public void drawGameScreen(Canvas canvas){
    	   if(this.goodSpaceShip.isAlive()){  
    		   this.goodSpaceShip.onDraw(canvas);
    	   }

    	   this.invaderSpaceFleet.onDraw(canvas);

    	   if (this.goodSpaceShipShoot.isAlive()){
    		   this.goodSpaceShipShoot.onDraw(canvas);
    	   }

    	   if (this.invaderSpaceShipShoot.isAlive()){
    		   this.invaderSpaceShipShoot.onDraw(canvas);
    	   }else{
    		   if( Math.random()<0.1 ){
    			   createInvaderSpaceShipShoot(true);
        	   }
    	   }

    	   if(shield.isAlive()){
    		   shield.onDraw(canvas);
    	   }

    	   drawScore(canvas);

    	   checkCollisions();

    	   if(explosion.isAlive()){
    		   explosion.onDraw(canvas);
    	   }

    	   //If an invader arrives or if the ship is destroyed... Game Over
    	   if(invaderSpaceFleet.invaderArrive() || !goodSpaceShip.isAlive()){
    		   gameState=3;
    	   }
    	   
    	   //If all invaders destroyed... NextLevel
    	   if(invaderSpaceFleet.allInvadersDestroyed()){
    		   level = level + 1;
    		   gameState=4;
    	   }
       }
       
       public void drawBestScoresScreen(Canvas canvas) {}
       
       @SuppressLint("WrongCall")
       @Override
       protected void onDraw(Canvas canvas) {
    	   canvas.drawColor(Color.BLACK);
    	   
    	   switch (gameState){
    	   case 1: //Welcome Screen
    		   welcomeScreen.draw(canvas);	
    		   break;
    	   case 2: //Play game
    		   drawGameScreen(canvas);
    		   break;
    	   case 3: //Game Over, (if best score -> enter name)   		   
    		   if(scoreValue > scoreBest3){    			   
	    		   activity.runOnUiThread(new Runnable() {
	    			    public void run() {
	    			    	bestScoresScreen.showInput(scoreValue);
	    			    }
	    			});
    		   }
    		   gameState=5;
    		   break;
    	   case 4: //NextLevel
    		   levelTransitionScreen.draw(canvas,level);
    		   break;
    	   case 5://Scores
    		   bestScoresScreen.draw(canvas);
    		   break;
    	   default:
    		   gameState = 1;
    		   break;
    	   }
       }
       
       @Override
       public boolean onTouchEvent(MotionEvent event) {
    	   if ((System.currentTimeMillis() - lastClick > 300) ) {
    		   lastClick = System.currentTimeMillis();
    		   
    		   synchronized (getHolder()) {
    			   
    			   switch(gameState){
    			   
    			   case 1://Welcome Screen
    				   gameState = 2;
    				   break;
    			   case 2:// Play game
    				   if(!goodSpaceShipShoot.isAlive()){
	    				   createGoodSpaceShipShoot(true);
	        			   soundPool.play(soundId, 1, 1, 0, 0, 1);
    				   }
        			   break;
    			   case 3://Enter name
    				   gameState=5;
    				   break;
    			   case 4: //Next Level transition
    				   gameState = 2;
    				   createGoodSpaceShip();
    				   createInvaderSpaceFleet(level);
    				   createShield();
    				   createGoodSpaceShipShoot(false);
    				   createInvaderSpaceShipShoot(false);
    	    		   break;
    			   case 5:// Scores
    				   gameState = 1;
    				   level = 1;
    				   scoreValue = 0;
    				   createScreens();
    				   createGoodSpaceShip();
    				   createInvaderSpaceFleet(level);
    	    		   createShield(); 
    	    		   createGoodSpaceShipShoot(false);
    				   createInvaderSpaceShipShoot(false);
    				   break;
    			   default:
    				   gameState = 1;
    				   break;
    			   }
    		   }
    	   }
    	   return true;
       }

	// Music functions
       public void startMusicPlayer(){
    	   mPlayer.start();
       }
       
       public void pauseMusicPlayer(){
    	   mPlayer.pause();
       }
       
       public void releaseMusicPlayer(){
    	   mPlayer.release();
    	   soundPool.release();
       }
       
       public void stopMusicPlayer(){
    	   mPlayer.stop();
       }
}
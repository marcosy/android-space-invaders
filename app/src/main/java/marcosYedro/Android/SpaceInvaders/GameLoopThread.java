package marcosYedro.Android.SpaceInvaders;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

@SuppressLint("WrongCall")
public class GameLoopThread extends Thread {
       static final long FPS = 25;
       private GameView view;
       private boolean running = false;
             
       public GameLoopThread(GameView view) {
             this.view = view;
       }
       public void setRunning(boolean run) {
             running = run;
       }
       public boolean isRunning(){
    	   return running;
       }
 
       @Override
       public void run() {
             long ticksPS = 1000 / FPS;
             long startTime;
             long sleepTime;
             
             while (running) {
                    Canvas c = null;
                    startTime = System.currentTimeMillis();

                    try {
                           c = view.getHolder().lockCanvas();
                           synchronized (view.getHolder()) {
                                  view.onDraw(c);
                           }
                    }
                    finally {
                           if (c != null) {
                                  view.getHolder().unlockCanvasAndPost(c);
                           }
                    }

                    sleepTime = ticksPS - (System.currentTimeMillis() - startTime);

                    try {
                           if (sleepTime > 0)
                                  sleep(sleepTime);
                           else
                                  sleep(10);
                    }
                    catch (Exception e) {}
             }
       }
}
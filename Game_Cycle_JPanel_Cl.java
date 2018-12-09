// IMPORTANT
//
// * Important that 'playerMe_Ob' has wrap-around at window-boundary or collision-detect may fail when cornering an object to cause many, repeated collisions

// TODO
//
// * Better Sound
// * Missle Velocity if ship is 0


//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.sound.sampled.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game_Cycle_JPanel_Cl extends JPanel implements KeyListener, Runnable
{
	// * 'enum' must be defined outside of any method
	// * 'protected' to be accessible by subordinate members
	protected enum Direction_Enum
	{
		UP, DOWN, LEFT, RIGHT, SIDEWAYS_AND_DOWN;
	}
    // * NonPlayerMe: collisionDisabledCountdownMax: 300 seems barely enough to pass through cleanly, 400 good, 500 seems too long
    // * PlayerMe: collisionDisabledCountdownMax: 50 good, 100 seems safer

	private Sprite_Cl playerMe_Ob = new Sprite_Cl( "/images/CalvinHobbes-Saucer.png", (int)(Game_Main_JFrame_Cl.WIDTH * 0.50), (int)(Game_Main_JFrame_Cl.HEIGHT * 0.70),100,100,0.1,1, 100);

    //y- private Sprite_Cl playerBot_Ob = new Sprite_Cl( "/images/ufo.png", (int)(Game_Main_JFrame_Cl.WIDTH * 0.50), (int)(Game_Main_JFrame_Cl.HEIGHT * 0.30),100,100,(int)((Math.random()*3))-1,(int)((Math.random()*3))-1,true);
    private Sprite_Cl playerBot_Ob = new Sprite_Cl( "/images/ufo.png", (int)(Game_Main_JFrame_Cl.WIDTH * 0.50), (int)(Game_Main_JFrame_Cl.HEIGHT * 0.50),100,100,0.1,0.5, 300);

	private Sprite_Cl playerFood_Ob = new Sprite_Cl( "images/Circle-Green-20x20.png", (int)(Game_Main_JFrame_Cl.WIDTH * 0.50), (int)(Game_Main_JFrame_Cl.HEIGHT * 0.30),100,100,0.1,0.5, 300);

	private ArrayList<Sprite_Cl> missiles_ObsLst = new ArrayList<Sprite_Cl>();

    List<Integer> playerMe_Input_ObsArrLst = new ArrayList<Integer>();

    // * IMPORTANT: 1 sec = 1 x 10^9 nano-sec
    // * IMPORTANT: To avoid 'java: integer number too large' error, require 'l' for 64bit otherwise 32bit default
    // * Projectile at 1/10 sec frequency
	private long gameCycle_Projectile_Prev_NanoSec = 0l;
	private long gameCycle_Prev_NanoSec = 0l;
	private long gameCycle_Curr_NanoSec = 0l;
    private long gameCycle_Fps_NanoSec = 0l;
    private long gameCycle_Projectile_Per_Sec = 5;  // 10 sometimes cause lag
    private double gameCycle_Period_Sec = 0.0;
    private long gameCycle_DelayFactor_MilliSec = 10; // 5msec too fast, 20msec kindof slow, 10msec seems right

	private BufferedImage bufferedImage_ForNextGameCycle;

    public Game_Cycle_JPanel_Cl(JFrame jFrame_In)
	{
        int velocityRandomNew;

        setBackground(Color.black);
		this.addKeyListener(this);
		new Thread(this).start();
		setVisible(true);

        // * Use Do..While for Non-Zero: Random Range [-1, 1]
        do {
            velocityRandomNew = randomRoll_Mth(3, 0, -1);
        } while ( velocityRandomNew == 0 );
        playerBot_Ob.setVelocityX_Mth( velocityRandomNew * playerBot_Ob.getVelocityMax_Mth() );
        playerBot_Ob.setVelocityY_Mth( velocityRandomNew * playerBot_Ob.getVelocityMax_Mth() );

        // * Use Do..While for Non-Zero: Random Range [-1, 1]
        do {
            velocityRandomNew = randomRoll_Mth(3, 0, -1);
           } while ( velocityRandomNew == 0 );
        playerFood_Ob.setVelocityX_Mth( velocityRandomNew * playerFood_Ob.getVelocityMax_Mth() );
        playerFood_Ob.setVelocityY_Mth( velocityRandomNew * playerFood_Ob.getVelocityMax_Mth() );
    }

	// * Will be called from externally on periodic basis
    // * Later calls 'paint()'
    public void update(Graphics window)
   {
	   paint(window);
   }

    // * Will be called from 'update()' internally on periodic basis
    public void paint( Graphics window )
	{
		//
		// * Timer Update
		//

		// * Calculate timer since last GameEngine Cycle
		gameCycle_Curr_NanoSec = System.nanoTime();
		gameCycle_Period_Sec = (gameCycle_Curr_NanoSec - gameCycle_Prev_NanoSec) / 1000000000.0;
		gameCycle_Fps_NanoSec = Math.round(1/ gameCycle_Period_Sec);
		gameCycle_Prev_NanoSec = gameCycle_Curr_NanoSec;
		//y- debug- System.out.println("> "+ gameCycle_Fps_NanoSec.value);
		//y- System.out.println("> FPS: "+ gameCycle_Fps_NanoSec);

		//set up the double buffering to make the game animation nice and smooth
		Graphics2D graphics2D_WindowMain = (Graphics2D)window;

		//take a snap shop of the current screen and same it as an image
		//that is the exact same width and height as the current screen
		if(bufferedImage_ForNextGameCycle == null){
		  Game_Main_JFrame_Cl.HEIGHT = this.getHeight();
	      Game_Main_JFrame_Cl.WIDTH = this.getWidth();
	      //System.out.println(Game_Main_JFrame_Cl.WIDTH);
		   bufferedImage_ForNextGameCycle = (BufferedImage)(createImage(this.getWidth(),this.getHeight()));
	   }

		//create a graphics reference to the bufferedImage_ForNextGameCycle ground image
		//we will draw_Mth all changes on the background image
		Graphics bufferedImage_ForNextGameCycle_Graphics = bufferedImage_ForNextGameCycle.createGraphics();

		bufferedImage_ForNextGameCycle_Graphics.setColor(Color.BLACK);
		bufferedImage_ForNextGameCycle_Graphics.fillRect(0,0,this.getWidth(),this.getHeight());
        bufferedImage_ForNextGameCycle_Graphics.setColor(Color.WHITE);
        bufferedImage_ForNextGameCycle_Graphics.setFont(new Font("Dialog", Font.PLAIN, 48));
        bufferedImage_ForNextGameCycle_Graphics.drawString("Game_Main_JFrame_Cl ", 50, 50 );
        //y- bufferedImage_ForNextGameCycle_Graphics.drawString( "> FPS: " + String.valueOf(gameCycle_Fps_NanoSec), 50, 100 );
        DecimalFormat dfTemp = new DecimalFormat("000");
        bufferedImage_ForNextGameCycle_Graphics.drawString( "> FPS: " + dfTemp.format(gameCycle_Fps_NanoSec), 50, 100 );
        //todo
        bufferedImage_ForNextGameCycle_Graphics.drawString( "> SCORE: " + dfTemp.format(Game_Main_JFrame_Cl.SCORE), 50, 150 );

        // * IMPORTANT: 1 sec = 1 x 10^9 nano-sec
        // * IMPORTANT: To avoid 'java: integer number too large' error, require 'l' for 64bit otherwise 32bit default
        // * Projectile at 1/10 sec frequency (or (1/10 * Math.pow(10,9)) nanosec)
        // ** '1.0' required for decimal division
        if( ( playerMe_Input_ObsArrLst.contains(Integer.valueOf(KeyEvent.VK_SPACE)) ) && ( gameCycle_Curr_NanoSec - gameCycle_Projectile_Prev_NanoSec > (1.0/gameCycle_Projectile_Per_Sec * Math.pow(10,9)) ) )
		{
            Sprite_Cl missileNew = new Sprite_Cl("/images/Circle-Green-20x20.png", 0, 0, 0, 0);
            missileNew.setImageSize_Mth(10,10);
            missileNew.setPos_Mth(playerMe_Ob.getX_Mth()+ (playerMe_Ob.getWidth_Mth()/2)-(missileNew.getWidth_Mth()/2), playerMe_Ob.getY_Mth()+ (playerMe_Ob.getHeight_Mth()/2)-(missileNew.getHeight_Mth()/2));
            missileNew.setVelocityX_Mth(playerMe_Ob.getVelocityX_Mth()*3);  // was 2, 3 since speed max is 1 for playerMe
            missileNew.setVelocityY_Mth(playerMe_Ob.getVelocityY_Mth()*3);  // was 2, 3 since speed max is 1 for playerMe
            missiles_ObsLst.add(missileNew);
            gameCycle_Projectile_Prev_NanoSec = gameCycle_Curr_NanoSec;

			try {
				// Open an audio input stream.
				//o- URL url = this.getClass().getClassLoader().getResource("gameover.wav");
                //n- URL url = this.getClass().getClassLoader().getResource("/images/Sound_Laser_SoundBible_602495617.wav");
                URL url = this.getClass().getClassLoader().getResource("Sound_Laser_SoundBible_602495617.wav");
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
				// Get a sound clip resource.
				Clip clip = AudioSystem.getClip();
				// Open audio clip and load samples from the audio input stream.
				clip.open(audioIn);
				clip.start();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}

        //	(int)((Math.random()*3))-1 >> ranges [-2,+2]
		playerFood_Ob.move_WithWrapAround_Mth();
        playerFood_Ob.draw_Mth(bufferedImage_ForNextGameCycle_Graphics);

// y-        playerBot_Ob.move_WithWrapAround_Mth();
// wraparound seems more safer:       playerBot_Ob.move_WithRebound_Mth();
        playerBot_Ob.move_WithWrapAround_Mth();
		playerBot_Ob.draw_Mth(bufferedImage_ForNextGameCycle_Graphics);

		// * Draw Collision Boxes
        if(playerMe_Ob.getCollisionDisabledCountdown_Mth() <=0) {
            bufferedImage_ForNextGameCycle_Graphics.setColor(Color.WHITE);
        }else{
            bufferedImage_ForNextGameCycle_Graphics.setColor(Color.RED);
        }
        bufferedImage_ForNextGameCycle_Graphics.drawRect( (int)Math.round(playerMe_Ob.getX_Mth()), (int)Math.round(playerMe_Ob.getY_Mth()), playerMe_Ob.getWidth_Mth(), playerMe_Ob.getHeight_Mth() );

        if(playerBot_Ob.getCollisionDisabledCountdown_Mth() <=0) {
            bufferedImage_ForNextGameCycle_Graphics.setColor(Color.WHITE);
        }else{
            bufferedImage_ForNextGameCycle_Graphics.setColor(Color.RED);
        }
        bufferedImage_ForNextGameCycle_Graphics.drawRect( (int)Math.round(playerBot_Ob.getX_Mth()), (int)Math.round(playerBot_Ob.getY_Mth()), playerBot_Ob.getWidth_Mth(), playerBot_Ob.getHeight_Mth() );

        if(playerFood_Ob.getCollisionDisabledCountdown_Mth() <=0) {
            bufferedImage_ForNextGameCycle_Graphics.setColor(Color.WHITE);
        }else{
            bufferedImage_ForNextGameCycle_Graphics.setColor(Color.RED);
        }
        bufferedImage_ForNextGameCycle_Graphics.drawRect( (int)Math.round(playerFood_Ob.getX_Mth()), (int)Math.round(playerFood_Ob.getY_Mth()), playerFood_Ob.getWidth_Mth(), playerFood_Ob.getHeight_Mth() );

        Iterator<Sprite_Cl> missiles_ObsLst_Iterator = missiles_ObsLst.iterator();
        while( missiles_ObsLst_Iterator.hasNext() ){
            Sprite_Cl missile_Ob_Curr = missiles_ObsLst_Iterator.next();
            missile_Ob_Curr.move_WithWrapAround_Mth();
            missile_Ob_Curr.draw_Mth(bufferedImage_ForNextGameCycle_Graphics);

            if(missile_Ob_Curr.colliding_Mth(playerFood_Ob)) {
                missiles_ObsLst_Iterator.remove();
                Game_Main_JFrame_Cl.SCORE--;
            }
            if(missile_Ob_Curr.colliding_Mth(playerBot_Ob)){
                missiles_ObsLst_Iterator.remove();
                Game_Main_JFrame_Cl.SCORE++;
            }
        }

        if( playerMe_Ob.colliding_Mth(playerBot_Ob) && playerBot_Ob.getCollisionDisabledCountdown_Mth() <= 0){

            // * Exchange velocities between objects for realistic rebound effect
            // * Check against divide by zero error
            double velocity_ForNonPlayerMe_X = ( (playerMe_Ob.getVelocityX_Mth()==0.00) ? 0.00 : ( playerMe_Ob.getVelocityX_Mth() / Math.abs(playerMe_Ob.getVelocityX_Mth()) * playerBot_Ob.getVelocityMax_Mth() ) );
            double velocity_ForNonPlayerMe_Y = ( (playerMe_Ob.getVelocityY_Mth()==0.00) ? 0.00 : ( playerMe_Ob.getVelocityY_Mth() / Math.abs(playerMe_Ob.getVelocityY_Mth()) * playerBot_Ob.getVelocityMax_Mth() ) );

            playerMe_Ob.setVelocityX_Mth( playerBot_Ob.getVelocityX_Mth() );
            playerMe_Ob.setVelocityY_Mth( playerBot_Ob.getVelocityY_Mth() );
            playerBot_Ob.setVelocityX_Mth( velocity_ForNonPlayerMe_X );
            playerBot_Ob.setVelocityY_Mth( velocity_ForNonPlayerMe_Y );

            // * Only if moving in same directions (velocities)
            // * Account also for zero velocities during conditional checking
            //
            if( (playerBot_Ob.getVelocityX_Mth() < 0 && playerMe_Ob.getVelocityX_Mth() < 0 ) ||
                (playerBot_Ob.getVelocityX_Mth() > 0 && playerMe_Ob.getVelocityX_Mth() > 0 )
              )
            {
                playerMe_Ob.setVelocityX_Mth( -playerMe_Ob.getVelocityX_Mth() );
            }
            // * Only if moving in same directions (velocities)
            // * Account also for zero velocities during conditional checking
            //
            if( (playerBot_Ob.getVelocityY_Mth() < 0 && playerMe_Ob.getVelocityY_Mth() < 0 ) ||
                (playerBot_Ob.getVelocityY_Mth() > 0 && playerMe_Ob.getVelocityY_Mth() > 0 )
              )
            {
                playerMe_Ob.setVelocityY_Mth( -playerMe_Ob.getVelocityY_Mth() );
            }

            playerMe_Ob.setCollisionDisabledCountdown_Mth(playerMe_Ob.getCollisionDisabledCountdown_Max_Mth()); // was 200, 0 not work, 50 a little short
            playerBot_Ob.setCollisionDisabledCountdown_Mth(playerBot_Ob.getCollisionDisabledCountdown_Max_Mth()); // was 200, 0 not work, 50 a little short
            Game_Main_JFrame_Cl.SCORE--;
        } else {
            if(playerBot_Ob.getCollisionDisabledCountdown_Mth() > 0){
                playerBot_Ob.setCollisionDisabledCountdown_Mth(playerBot_Ob.getCollisionDisabledCountdown_Mth() - 1);
            }
        }

        if( playerMe_Ob.colliding_Mth(playerFood_Ob) && playerFood_Ob.getCollisionDisabledCountdown_Mth() <= 0 ){

            // * Exchange velocities between objects for realistic rebound effect
            //
            // * Check against divide by zero error
            double velocity_ForNonPlayerMe_X = ( (playerMe_Ob.getVelocityX_Mth()==0.00) ? 0.00 : ( playerMe_Ob.getVelocityX_Mth() / Math.abs(playerMe_Ob.getVelocityX_Mth()) * playerFood_Ob.getVelocityMax_Mth() ) );
            double velocity_ForNonPlayerMe_Y = ( (playerMe_Ob.getVelocityY_Mth()==0.00) ? 0.00 : ( playerMe_Ob.getVelocityY_Mth() / Math.abs(playerMe_Ob.getVelocityY_Mth()) * playerFood_Ob.getVelocityMax_Mth() ) );

            playerMe_Ob.setVelocityX_Mth( playerFood_Ob.getVelocityX_Mth() );
            playerMe_Ob.setVelocityY_Mth( playerFood_Ob.getVelocityY_Mth() );
            playerFood_Ob.setVelocityX_Mth( velocity_ForNonPlayerMe_X );
            playerFood_Ob.setVelocityY_Mth( velocity_ForNonPlayerMe_Y );

            // * Only if moving in same directions (velocities)
            // * Account also for zero velocities during conditional checking
            //
            if( (playerFood_Ob.getVelocityX_Mth() < 0 && playerMe_Ob.getVelocityX_Mth() < 0 ) ||
                (playerFood_Ob.getVelocityX_Mth() > 0 && playerMe_Ob.getVelocityX_Mth() > 0 )
              )
            {
//                playerFood_Ob.setVelocityX_Mth( -playerFood_Ob.getVelocityX_Mth() );
                playerMe_Ob.setVelocityX_Mth( -playerMe_Ob.getVelocityX_Mth() );
            }
            // * Only if moving in same directions (velocities)
            // * Account also for zero velocities during conditional checking
            //
            if( (playerFood_Ob.getVelocityY_Mth() < 0 && playerMe_Ob.getVelocityY_Mth() < 0 ) ||
                (playerFood_Ob.getVelocityY_Mth() > 0 && playerMe_Ob.getVelocityY_Mth() > 0 )
              )
            {
//                playerFood_Ob.setVelocityY_Mth( -playerFood_Ob.getVelocityY_Mth() );
                playerMe_Ob.setVelocityY_Mth( -playerMe_Ob.getVelocityY_Mth() );
            }

            playerMe_Ob.setCollisionDisabledCountdown_Mth(playerMe_Ob.getCollisionDisabledCountdown_Max_Mth()); // was 200, 0 not work, 50 a little short
            playerFood_Ob.setCollisionDisabledCountdown_Mth(playerFood_Ob.getCollisionDisabledCountdown_Max_Mth()); // was 200, 0 not work, 50 a little short
            Game_Main_JFrame_Cl.SCORE++;
        } else {
//            playerBot_Ob.collisionCyclePrev_Bool_Fld = false;
            if (playerFood_Ob.getCollisionDisabledCountdown_Mth() > 0) {
                playerFood_Ob.setCollisionDisabledCountdown_Mth(playerFood_Ob.getCollisionDisabledCountdown_Mth() - 1);
            }
        }

        // * Try moving 'playerMe' last since moves 2 pixels/cycle and others only 1 pixel/cycle to minimize collision-snag
        playerMe_Ob.move_WithWrapAround_Mth( playerMe_Input_ObsArrLst );
        playerMe_Ob.draw_Mth(bufferedImage_ForNextGameCycle_Graphics);

        graphics2D_WindowMain.drawImage(bufferedImage_ForNextGameCycle, null, 0, 0);
		bufferedImage_ForNextGameCycle = null;
	}


	public void keyPressed(KeyEvent e)
	{
	    Integer playerInputCode = e.getKeyCode();
		if (playerInputCode == KeyEvent.VK_LEFT)
		{
		    if( !playerMe_Input_ObsArrLst.contains(playerInputCode))
            {
                playerMe_Input_ObsArrLst.add(playerInputCode);
            }

            //            playerMe_Ob.setVelocityX_Mth(-1);
            //            if(Math.abs(playerMe_Ob.getVelocityX_Mth()) > -Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX) {

            //y for gravity-less:              if(playerMe_Ob.getVelocityX_Mth() > -Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX) {
            //                playerMe_Ob.setVelocityX_Mth(playerMe_Ob.getVelocityX_Mth() - 1);
            //            }
//            playerMe_Ob.setVelocityX_Mth(-Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX);
//            playerMe_Ob.setVelocityY_Mth(0);
        }
		if (playerInputCode == KeyEvent.VK_RIGHT)
		{
            if( !playerMe_Input_ObsArrLst.contains(playerInputCode))
            {
                playerMe_Input_ObsArrLst.add(playerInputCode);
            }
            //            playerMe_Ob.setVelocityX_Mth(+1);
            //            if(Math.abs(playerMe_Ob.getVelocityX_Mth()) < Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX) {

            //y for gravity-less:              if(playerMe_Ob.getVelocityX_Mth() < Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX) {
            //                playerMe_Ob.setVelocityX_Mth(playerMe_Ob.getVelocityX_Mth() + 1);
            //            }
//            playerMe_Ob.setVelocityX_Mth(+Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX);
//            playerMe_Ob.setVelocityY_Mth(0);
        }
		if (playerInputCode == KeyEvent.VK_UP)
		{
            if( !playerMe_Input_ObsArrLst.contains(playerInputCode))
            {
                playerMe_Input_ObsArrLst.add(playerInputCode);
            }
            //            playerMe_Ob.setVelocityY_Mth(-1);
            //            if(Math.abs(playerMe_Ob.getVelocityY_Mth()) < Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX) {

            //y for gravity-less:               if(playerMe_Ob.getVelocityY_Mth() > -Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX) {
            //                playerMe_Ob.setVelocityY_Mth(playerMe_Ob.getVelocityY_Mth() - 1);
            //            }
//            playerMe_Ob.setVelocityX_Mth(0);
//            playerMe_Ob.setVelocityY_Mth(-Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX);
        }
		if (playerInputCode == KeyEvent.VK_DOWN)
		{
            if( !playerMe_Input_ObsArrLst.contains(playerInputCode))
            {
                playerMe_Input_ObsArrLst.add(playerInputCode);
            }
            //y for gravity-less:            playerMe_Ob.setVelocityY_Mth(+1);
            //            if(playerMe_Ob.getVelocityY_Mth() < Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX) {
            //                playerMe_Ob.setVelocityY_Mth(playerMe_Ob.getVelocityY_Mth() + 1);
            //            }
//            playerMe_Ob.setVelocityX_Mth(0);
//            playerMe_Ob.setVelocityY_Mth(+Game_Main_JFrame_Cl.SPRITE_VELOCITY_MAX);
        }
		if (playerInputCode == KeyEvent.VK_SPACE)
		{
            if( !playerMe_Input_ObsArrLst.contains(playerInputCode))
            {
                playerMe_Input_ObsArrLst.add(playerInputCode);
            }
		}
		repaint();
	}

	public void keyReleased(KeyEvent e)
	{
        Integer playerInputCode = e.getKeyCode();
        if (playerInputCode == KeyEvent.VK_LEFT)
		{
            playerMe_Input_ObsArrLst.remove(playerInputCode);
            // * Special check to clobber/stop if current direction of motion is expected
//            if(playerMe_Ob.getVelocityX_Mth() == -1){
//                playerMe_Ob.setVelocityX_Mth(0);
//            }
		}
		if (playerInputCode == KeyEvent.VK_RIGHT)
		{
            playerMe_Input_ObsArrLst.remove(playerInputCode);
            // * Special check to clobber/stop if current direction of motion is expected
//            if(playerMe_Ob.getVelocityX_Mth() == +1){
//                playerMe_Ob.setVelocityX_Mth(0);
//            }
        }
		if (playerInputCode == KeyEvent.VK_UP)
		{
            playerMe_Input_ObsArrLst.remove(playerInputCode);
		}
		if (playerInputCode == KeyEvent.VK_DOWN)
		{
            playerMe_Input_ObsArrLst.remove(playerInputCode);
		}
		if (playerInputCode == KeyEvent.VK_SPACE)
		{
            playerMe_Input_ObsArrLst.remove(playerInputCode);
		}
		repaint();
	}

	public void keyTyped(KeyEvent e)
	{

	}

	public int randomRoll_Mth( int dice_NumberOfSides_In, int dice_ValueMin_In, int dice_FinalOffset ){
        int randomRollNew = 0;
        // * Math.random() >> [0.. 0.99]
        randomRollNew = ((int)(Math.random() * dice_NumberOfSides_In) + dice_ValueMin_In) + dice_FinalOffset;
        return randomRollNew;
    }

   public void run()
   {
   	try
   	{
   		while(true)
   		{
			//y- Thread.currentThread().sleep(5);
			//y- Thread.currentThread().sleep(20);
            //y- Thread.currentThread().sleep(10);  // 5msec too fast, 20msec kindof slow, 10msec seems right
            Thread.currentThread().sleep(gameCycle_DelayFactor_MilliSec);  // 5msec too fast, 20msec kindof slow, 10msec seems right
            repaint();
         }
      }catch(Exception e)
      {
      }
  	}
}


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

	private Sprite_Cl playerMe_Ob = new Sprite_Cl( "/images/CalvinHobbes-Saucer.png", (int)(Game_Main_JFrame_Cl.WIDTH * 0.50), (int)(Game_Main_JFrame_Cl.HEIGHT * 0.70),100,100,0.1,1);

    //y- private Sprite_Cl playerBot_Ob = new Sprite_Cl( "/images/ufo.png", (int)(Game_Main_JFrame_Cl.WIDTH * 0.50), (int)(Game_Main_JFrame_Cl.HEIGHT * 0.30),100,100,(int)((Math.random()*3))-1,(int)((Math.random()*3))-1,true);
    private Sprite_Cl playerBot_Ob = new Sprite_Cl( "/images/ufo.png", (int)(Game_Main_JFrame_Cl.WIDTH * 0.50), (int)(Game_Main_JFrame_Cl.HEIGHT * 0.30),100,100,0.5,1);

	private Sprite_Cl playerFood_Ob = new Sprite_Cl( "images/Circle-Green-20x20.png", (int)(Game_Main_JFrame_Cl.WIDTH * 0.50), (int)(Game_Main_JFrame_Cl.HEIGHT * 0.50),100,100,1,1);

	private ArrayList<Sprite_Cl> missiles_ObsLst = new ArrayList<Sprite_Cl>();

    List<Integer> playerMe_Input_ObsArrLst = new ArrayList<Integer>();

    // * IMPORTANT: 1 sec = 1 x 10^9 nano-sec
    // * IMPORTANT: To avoid 'java: integer number too large' error, require 'l' for 64bit otherwise 32bit default
    // * Projectile at 1/10 sec frequency
	private long gameCycle_Projectile_Prev_NanoSec = 0l;
	private long gameCycle_Prev_NanoSec = 0l;
	private long gameCycle_Curr_NanoSec = 0l;
    private long gameCycle_Fps_NanoSec = 0l;
    private long gameCycle_Projectile_Per_Sec = 10;
    private double gameCycle_Period_Sec = 0.0;
    private long gameCycle_DelayFactor_MilliSec = 10; // 5msec too fast, 20msec kindof slow, 10msec seems right

	private BufferedImage back;

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
//        playerBot_Ob.setVelocityX_Mth( velocityRandomNew );
//        playerBot_Ob.setVelocityY_Mth( velocityRandomNew );
        playerBot_Ob.setVelocityX_Mth( .5 );
        playerBot_Ob.setVelocityY_Mth( .5 );
//.5 better        playerBot_Ob.setVelocityX_Mth( 1 );
//        playerBot_Ob.setVelocityY_Mth( 1 );

        // * Use Do..While for Non-Zero: Random Range [-1, 1]
        do {
            velocityRandomNew = randomRoll_Mth(3, 0, -1);
           } while ( velocityRandomNew == 0 );
        playerFood_Ob.setVelocityX_Mth( velocityRandomNew );
        playerFood_Ob.setVelocityY_Mth( velocityRandomNew );
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
		Graphics2D twoDGraph = (Graphics2D)window;

		//take a snap shop of the current screen and same it as an image
		//that is the exact same width and height as the current screen
		if(back==null){
		  Game_Main_JFrame_Cl.HEIGHT = this.getHeight();
	      Game_Main_JFrame_Cl.WIDTH = this.getWidth();
	      //System.out.println(Game_Main_JFrame_Cl.WIDTH);
		   back = (BufferedImage)(createImage(this.getWidth(),this.getHeight()));
	   }

		//create a graphics reference to the back ground image
		//we will draw_Mth all changes on the background image
		Graphics graphToBack = back.createGraphics();

		graphToBack.setColor(Color.BLACK);
		graphToBack.fillRect(0,0,this.getWidth(),this.getHeight());
        graphToBack.setColor(Color.WHITE);
        graphToBack.setFont(new Font("Dialog", Font.PLAIN, 48));
        graphToBack.drawString("Game_Main_JFrame_Cl ", 50, 50 );
        //y- graphToBack.drawString( "> FPS: " + String.valueOf(gameCycle_Fps_NanoSec), 50, 100 );
        DecimalFormat dfTemp = new DecimalFormat("000");
        graphToBack.drawString( "> FPS: " + dfTemp.format(gameCycle_Fps_NanoSec), 50, 100 );
        //todo
        graphToBack.drawString( "> SCORE: " + dfTemp.format(Game_Main_JFrame_Cl.SCORE), 50, 150 );

        playerMe_Ob.move_WithWrapAround_Mth( playerMe_Input_ObsArrLst );

        // * IMPORTANT: 1 sec = 1 x 10^9 nano-sec
        // * IMPORTANT: To avoid 'java: integer number too large' error, require 'l' for 64bit otherwise 32bit default
        // * Projectile at 1/10 sec frequency (or (1/10 * Math.pow(10,9)) nanosec)
        // ** '1.0' required for decimal division
        if( ( playerMe_Input_ObsArrLst.contains(Integer.valueOf(KeyEvent.VK_SPACE)) ) && ( gameCycle_Curr_NanoSec - gameCycle_Projectile_Prev_NanoSec > (1.0/gameCycle_Projectile_Per_Sec * Math.pow(10,9)) ) )
		{
            Sprite_Cl missileNew = new Sprite_Cl("/images/Circle-Green-20x20.png", 0, 0, 0, 0);
            missileNew.setImageSize_Mth(10,10);
            missileNew.setPos_Mth(playerMe_Ob.getX_Mth()+ (playerMe_Ob.getWidth_Mth()/2)-(missileNew.getWidth_Mth()/2), playerMe_Ob.getY_Mth()+ (playerMe_Ob.getHeight_Mth()/2)-(missileNew.getHeight_Mth()/2));
            missileNew.setVelocityX_Mth(playerMe_Ob.getVelocityX_Mth()*2);
            missileNew.setVelocityY_Mth(playerMe_Ob.getVelocityY_Mth()*2);
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
        playerFood_Ob.draw_Mth(graphToBack);

		playerMe_Ob.draw_Mth(graphToBack);

// y-        playerBot_Ob.move_WithWrapAround_Mth();
// wraparound seems more safer:       playerBot_Ob.move_WithRebound_Mth();
        playerBot_Ob.move_WithWrapAround_Mth();
		playerBot_Ob.draw_Mth(graphToBack);


        Iterator<Sprite_Cl> missiles_ObsLst_Iterator = missiles_ObsLst.iterator();
        while( missiles_ObsLst_Iterator.hasNext() ){
            Sprite_Cl missile_Ob_Curr = missiles_ObsLst_Iterator.next();
            missile_Ob_Curr.move_WithWrapAround_Mth();
            missile_Ob_Curr.draw_Mth(graphToBack);

            if(missile_Ob_Curr.colliding_Mth(playerFood_Ob)) {
                missiles_ObsLst_Iterator.remove();
                Game_Main_JFrame_Cl.SCORE--;
            }
            if(missile_Ob_Curr.colliding_Mth(playerBot_Ob)){
                missiles_ObsLst_Iterator.remove();
                Game_Main_JFrame_Cl.SCORE++;
            }

        }

        // * Comment out as too easy for scoring
//        if( playerMe_Ob.colliding_Mth(playerFood_Ob) ){
//            Game_Main_JFrame_Cl.SCORE++;
//        }
        // * Keep to keep things challenging
        if( playerMe_Ob.colliding_Mth(playerBot_Ob) ){
            // * Reverse direction for each object involved for rebound-effect
            playerMe_Ob.setVelocityX_Mth( -playerMe_Ob.getVelocityX_Mth() );
            playerMe_Ob.setVelocityY_Mth( -playerMe_Ob.getVelocityY_Mth() );
            playerBot_Ob.setVelocityX_Mth( -playerBot_Ob.getVelocityX_Mth() );
            playerBot_Ob.setVelocityY_Mth( -playerBot_Ob.getVelocityY_Mth() );

            // * Exchange velocities between objects for realistic rebound effect
            //

// n-           double velocityTmpX = playerMe_Ob.getVelocityX_Mth();
//            if(Math.abs(velocityTmpX) > playerBot_Ob.getVelocityMax_Mth()){
//                velocityTmpX = playerBot_Ob.getVelocityMax_Mth() * (velocityTmpX/Math.abs(velocityTmpX));
//            }
//            double velocityTmpY = playerMe_Ob.getVelocityY_Mth();
//            if(velocityTmpY > playerBot_Ob.getVelocityMax_Mth()){
//                velocityTmpY = playerBot_Ob.getVelocityMax_Mth() * (velocityTmpY/Math.abs(velocityTmpY));
//            }
//            playerMe_Ob.setVelocityX_Mth( -2 * playerBot_Ob.getVelocityX_Mth() );
//            playerMe_Ob.setVelocityY_Mth( -2 * playerBot_Ob.getVelocityY_Mth() );
////            playerMe_Ob.setVelocityX_Mth( playerBot_Ob.getVelocityX_Mth() );
////            playerMe_Ob.setVelocityY_Mth( playerBot_Ob.getVelocityY_Mth() );
//            playerBot_Ob.setVelocityX_Mth( velocityTmpX );
//            playerBot_Ob.setVelocityY_Mth( velocityTmpY );

//y-            playerMe_Ob.disabledCountdown_Fld = 100; // was 200
            playerMe_Ob.disabledCountdown_Fld = 50 ; // was 200, 0 not work

            Game_Main_JFrame_Cl.SCORE--;
        }

        if( playerMe_Ob.colliding_Mth(playerFood_Ob) ){
            // * Exchange velocities between objects for realistic rebound effect
            //
            double velocityTmpX = playerMe_Ob.getVelocityX_Mth();
            double velocityTmpY = playerMe_Ob.getVelocityY_Mth();
            playerMe_Ob.setVelocityX_Mth( playerFood_Ob.getVelocityX_Mth() );
            playerMe_Ob.setVelocityY_Mth( playerFood_Ob.getVelocityY_Mth() );
            playerFood_Ob.setVelocityX_Mth( velocityTmpX );
            playerFood_Ob.setVelocityY_Mth( velocityTmpY );
            Game_Main_JFrame_Cl.SCORE++;
        }


        twoDGraph.drawImage(back, null, 0, 0);
		back = null;
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


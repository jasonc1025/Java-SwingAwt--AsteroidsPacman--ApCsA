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

    //y- too fast and choppy- 		playerMe_Ob = new Sprite_CoreExt_Movable_Collidable_Cl(310,450,5);
    //y- 	private Sprite_CoreExt_Movable_Collidable_Cl playerMe_Ob = new Sprite_CoreExt_Movable_Collidable_Cl(310,450,1);
    // * Speed was '1'
	//y- private Sprite_CoreExt_Movable_Collidable_Cl playerMe_Ob = new Sprite_CoreExt_Movable_Collidable_Cl( "/images/ship.jpg", (int)(Game_Main_JFrame_Cl.WIDTH * 0.50), (int)(Game_Main_JFrame_Cl.HEIGHT * 0.80),100,100,2);
	private Sprite_CoreExt_Movable_Collidable_Cl playerMe_Ob = new Sprite_CoreExt_Movable_Collidable_Cl( "/images/CalvinHobbes-Saucer.png", (int)(Game_Main_JFrame_Cl.WIDTH * 0.50), (int)(Game_Main_JFrame_Cl.HEIGHT * 0.80),100,100,2);

	List<Integer> playerMe_Input_StringOb_ArrLst = new ArrayList<Integer>();

	//o- private PlayerBots_Cl playerBots_ObsLst;
	private Sprites_CoreExtLst_Movable_Collidable_Cl playerBots_ObsLst;
	private Sprites_CoreExtLst_Movable_Collidable_Cl missiles_ObsLst;

    //	private Long cycle_ProjectileLast_NanoTime = new Long( 0 );
    //	private Long cycle_Last_NanoTime = new Long( 0 );
    //	private Long cycle_Current_NanoTime = new Long( 0 );

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


	//y- private boolean[] keys;
	private BufferedImage back;

	public Game_Cycle_JPanel_Cl(JFrame par)
	{
		//y- keys = new boolean[5];

		setBackground(Color.black);

		//o-  playerBots_ObsLst = new PlayerBots_Cl(100);
		playerBots_ObsLst = new Sprites_CoreExtLst_Movable_Collidable_Cl(100);

		missiles_ObsLst = new Sprites_CoreExtLst_Movable_Collidable_Cl();

		this.addKeyListener(this);
		new Thread(this).start();

		setVisible(true);
	}

    public void update(Graphics window)
   {
	   paint(window);
   }

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
		//we will draw all changes on the background image
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


		//y- if(keys[0] == true)
        if( playerMe_Input_StringOb_ArrLst.contains(Integer.valueOf(KeyEvent.VK_LEFT)) )
		{
			playerMe_Ob.move(Direction_Enum.LEFT);
		}
		//y- if(keys[1] == true)
        if( playerMe_Input_StringOb_ArrLst.contains(Integer.valueOf(KeyEvent.VK_RIGHT)) )
        {
			playerMe_Ob.move(Direction_Enum.RIGHT);
		}
		//y- if(keys[2] == true)
        if( playerMe_Input_StringOb_ArrLst.contains(Integer.valueOf(KeyEvent.VK_UP)) )
        {
			playerMe_Ob.move(Direction_Enum.UP);
		}
		//y- if(keys[3] == true)
        if( playerMe_Input_StringOb_ArrLst.contains(Integer.valueOf(KeyEvent.VK_DOWN)) )
        {
			playerMe_Ob.move(Direction_Enum.DOWN);
		}
        // * IMPORTANT: 1 sec = 1 x 10^9 nano-sec
        // * IMPORTANT: To avoid 'java: integer number too large' error, require 'l' for 64bit otherwise 32bit default
        // * Projectile at 1/10 sec frequency (or (1/10 * Math.pow(10,9)) nanosec)
        // ** '1.0' required for decimal division
        //y- if( (keys[4] == true) && ( gameCycle_Curr_NanoSec - gameCycle_Projectile_Prev_NanoSec > (1.0/gameCycle_Projectile_Per_Sec * Math.pow(10,9)) ) )
        if( ( playerMe_Input_StringOb_ArrLst.contains(Integer.valueOf(KeyEvent.VK_SPACE)) ) && ( gameCycle_Curr_NanoSec - gameCycle_Projectile_Prev_NanoSec > (1.0/gameCycle_Projectile_Per_Sec * Math.pow(10,9)) ) )
		{
			//y- missiles_ObsLst.add(new Sprite_CoreExt_Movable_Collidable_Cl(playerMe_Ob.getX()+playerMe_Ob.getWidth()/2-5, playerMe_Ob.getY(), 10, 10, 5));
            Sprite_CoreExt_Movable_Collidable_Cl missileTemp = new Sprite_CoreExt_Movable_Collidable_Cl("/images/Circle-Green-20x20.png");
            //y- missiles_ObsLst.add(new Sprite_CoreExt_Movable_Collidable_Cl( "/images/Circle-Green-20x20.png",playerMe_Ob.getX()+playerMe_Ob.getWidth()/2-10, playerMe_Ob.getY()-10, 5));
            missileTemp.setImageSize(10,10);
            missileTemp.setPos(playerMe_Ob.getX()+ playerMe_Ob.getWidth()/2-(missileTemp.getWidth()/2), playerMe_Ob.getY()-(missileTemp.getHeight()/2));
            missileTemp.setSpeed(5);
            //y missiles_ObsLst.add(new Sprite_CoreExt_Movable_Collidable_Cl( "/images/Circle-Green-20x20.png",playerMe_Ob.getX()+playerMe_Ob.getWidth()/2-10, playerMe_Ob.getY()-10, 5));
            missiles_ObsLst.add(missileTemp);
			//y turn off to allow continuous fire- keys[4] = false;
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

		//y- missiles_ObsLst.move("UP");
		missiles_ObsLst.move(Direction_Enum.UP);
		missiles_ObsLst.draw(graphToBack);

		playerMe_Ob.draw(graphToBack);

		//y- playerBots_ObsLst.move("SIDEWAYS_AND_DOWN");
		playerBots_ObsLst.move(Direction_Enum.SIDEWAYS_AND_DOWN);
		playerBots_ObsLst.draw(graphToBack);

		//collision detection
		missiles_ObsLst.boundaryCheckAndClean();
		playerBots_ObsLst.boundaryCheckAndClean();

		//o- playerBots_ObsLst.removeDeadOnes(playerMe_Ob, missiles_ObsLst.getList());
//		playerBots_ObsLst.collisionCheckAndClean(playerMe_Ob,-1);
//		playerBots_ObsLst.collisionCheckAndClean(missiles_ObsLst.getList(),+1);
		playerBots_ObsLst.collisionCheckAndClean(playerMe_Ob,-1);
		playerBots_ObsLst.collisionCheckAndClean(missiles_ObsLst.getList(),+1);


		twoDGraph.drawImage(back, null, 0, 0);
		back = null;
	}


	public void keyPressed(KeyEvent e)
	{
	    Integer playerInputCode = e.getKeyCode();
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
		    if( !playerMe_Input_StringOb_ArrLst.contains(playerInputCode))
            {
                playerMe_Input_StringOb_ArrLst.add(playerInputCode);
            }
			//y- keys[0] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
            if( !playerMe_Input_StringOb_ArrLst.contains(playerInputCode))
            {
                playerMe_Input_StringOb_ArrLst.add(playerInputCode);
            }
            //y- keys[1] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP)
		{
            if( !playerMe_Input_StringOb_ArrLst.contains(playerInputCode))
            {
                playerMe_Input_StringOb_ArrLst.add(playerInputCode);
            }
			//y- keys[2] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
            if( !playerMe_Input_StringOb_ArrLst.contains(playerInputCode))
            {
                playerMe_Input_StringOb_ArrLst.add(playerInputCode);
            }
			//y- keys[3] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
            if( !playerMe_Input_StringOb_ArrLst.contains(playerInputCode))
            {
                playerMe_Input_StringOb_ArrLst.add(playerInputCode);
            }
			//y- keys[4] = true;
		}
		repaint();
	}

	public void keyReleased(KeyEvent e)
	{
        Integer playerInputCode = e.getKeyCode();
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
            playerMe_Input_StringOb_ArrLst.remove(playerInputCode);
			//y- keys[0] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
            playerMe_Input_StringOb_ArrLst.remove(playerInputCode);
			//y- keys[1] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP)
		{
            playerMe_Input_StringOb_ArrLst.remove(playerInputCode);
			//y- keys[2] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
            playerMe_Input_StringOb_ArrLst.remove(playerInputCode);
			//y- keys[3] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
            playerMe_Input_StringOb_ArrLst.remove(playerInputCode);
			//y- keys[4] = false;
		}
		repaint();
	}

	public void keyTyped(KeyEvent e)
	{

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


// Features over Original StarFighter-Lab25-APlusCS-Armstrong
// using 'BattleCross Technology' from Encourage and Empower
// * Collision-Detection Algorithm simplified by 50%: 8 lines to 4 lines
// * Encapsulation via Sharable Class for all/any Sprites
// * Velocity-Sensitive Motion for PlayerMe_Ob

// TODO
// * boundary check for methods

//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import javax.swing.JFrame;
import java.awt.Component;

public class Game_Main_JFrame_Cl extends JFrame
{
	// * Max size for laptop screen
	//y- public static int WIDTH = 800;
	//y- public static int WIDTH = 2300;
	public static int WIDTH = 1600;
	//y- public static int HEIGHT = 600;
	//y- public static int HEIGHT = 1300;
	public static int HEIGHT = 800;

	public static int BORDER_SAFETY_MARGIN = 50;
	// * 2 seems like a good speed, not too fast nor slow
	public static int SPRITE_VELOCITY_MAX = 2;


	public static int SCORE = 0;

	public Game_Main_JFrame_Cl()
	{
		super("STARFIGHTER");

		setSize(WIDTH, HEIGHT);

		Game_Cycle_JPanel_Cl theGame = new Game_Cycle_JPanel_Cl(this);
		((Component)theGame).setFocusable(true);

      	getContentPane().add( theGame );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main( String args[] )
	{
		Game_Main_JFrame_Cl run = new Game_Main_JFrame_Cl();
	}
}
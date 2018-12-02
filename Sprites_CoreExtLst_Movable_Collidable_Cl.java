//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sprites_CoreExtLst_Movable_Collidable_Cl
{
	//n invalid methods-	privatea List<Sprite_Core_ClAb> sprites_ObsLst_Fl;
	private List<Sprite_CoreExt_Movable_Collidable_Cl> sprites_ObsLst_Fl;

	public Sprites_CoreExtLst_Movable_Collidable_Cl()
	{
		sprites_ObsLst_Fl = new ArrayList<Sprite_CoreExt_Movable_Collidable_Cl>();
	}

	public Sprites_CoreExtLst_Movable_Collidable_Cl(int size)
	{
		sprites_ObsLst_Fl = new ArrayList<Sprite_CoreExt_Movable_Collidable_Cl>();
		// * x & y need to be greater than its own 'width + origin' to not trigger a premature speed reversal
		int x = Game_Main_JFrame_Cl.BORDER_SAFETY_MARGIN;
		int y = Game_Main_JFrame_Cl.BORDER_SAFETY_MARGIN;
		for( int i = 0; i < size; i++)
		{
            //y- 			sprites_ObsLst_Fl.add( new Sprite_CoreExt_Movable_Collidable_Cl("/images/alien.jpg", x, y, 35, 35, 1) );
			//y- sprites_ObsLst_Fl.add( new Sprite_CoreExt_Movable_Collidable_Cl("/images/alien.jpg", x, y, 100, 100, 1) );
			sprites_ObsLst_Fl.add( new Sprite_CoreExt_Movable_Collidable_Cl("/images/ufo.png", x, y, 50, 50, 1) );
			// * BUG FIX: Cannot be '>' or will cause premature boundary-crossover and sprite will inadvertently go off screen
			if( x >= Game_Main_JFrame_Cl.WIDTH - Game_Main_JFrame_Cl.BORDER_SAFETY_MARGIN)
			{
				x = Game_Main_JFrame_Cl.BORDER_SAFETY_MARGIN;
				//y- y += 75;
				y += (sprites_ObsLst_Fl.get(i).getHeight() * 1);
				sprites_ObsLst_Fl.get(i).setPos(x,y);
			}
			//y- x += 75;
			x += (sprites_ObsLst_Fl.get(i).getWidth() * 1);
		}
	}

	public void add(Sprite_CoreExt_Movable_Collidable_Cl sprite_Ob_In)
	{
	   sprites_ObsLst_Fl.add(sprite_Ob_In);
	}

	public void draw( Graphics window )
	{
		//n invalid methods-			for( Sprite_Core_ClAb al : sprites_ObsLst_Fl)
		for( Sprite_CoreExt_Movable_Collidable_Cl sprite_Ob_In : sprites_ObsLst_Fl)
			sprite_Ob_In.draw( window );
	}

	public void move( Game_Cycle_JPanel_Cl.Direction_Enum direction_Enum_In )
	{
		for(Sprite_CoreExt_Movable_Collidable_Cl sprite_Ob_In : sprites_ObsLst_Fl)
		{
			sprite_Ob_In.move( direction_Enum_In );
		}
	}

	public void collisionCheckAndClean(Sprite_CoreExt_Movable_Collidable_Cl playerMe_Ob_In, int scoreAdjust_In)
	{
		Iterator<Sprite_CoreExt_Movable_Collidable_Cl> playerBots_Iterator = sprites_ObsLst_Fl.iterator();
		while( playerBots_Iterator.hasNext() )
		{
			Sprite_CoreExt_Movable_Collidable_Cl playerBot_Ob = playerBots_Iterator.next();

			if( playerBot_Ob.colliding( playerMe_Ob_In ))
			{
				playerBots_Iterator.remove();
				Game_Main_JFrame_Cl.SCORE += scoreAdjust_In;
			}
		}
	}

	public void collisionCheckAndClean(List<Sprite_CoreExt_Movable_Collidable_Cl> missiles_ObsLst_In, int scoreAdjust_In)
	{
		Iterator<Sprite_CoreExt_Movable_Collidable_Cl> playerBots_Iterator = sprites_ObsLst_Fl.iterator();
		while( playerBots_Iterator.hasNext() )
		{
			Sprite_CoreExt_Movable_Collidable_Cl playerBot_Ob = playerBots_Iterator.next();

			Iterator<Sprite_CoreExt_Movable_Collidable_Cl> missile_Iterator = missiles_ObsLst_In.iterator();
			while (missile_Iterator.hasNext()) {
				Sprite_CoreExt_Movable_Collidable_Cl missile_Ob = missile_Iterator.next();

				if (playerBot_Ob.colliding(missile_Ob)) {
					playerBots_Iterator.remove();
					missile_Iterator.remove();
					Game_Main_JFrame_Cl.SCORE += scoreAdjust_In;
					break;
				}
			}
		}
	}

	public void boundaryCheckAndClean()
	{
		for(int i = 0; i< sprites_ObsLst_Fl.size(); i++)
		{
//			if(sprites_ObsLst_Fl.get(i).getX()<-20)
			if( (sprites_ObsLst_Fl.get(i).getY() < (0 + Game_Main_JFrame_Cl.BORDER_SAFETY_MARGIN) ) ||
                (sprites_ObsLst_Fl.get(i).getY() > (Game_Main_JFrame_Cl.HEIGHT - Game_Main_JFrame_Cl.BORDER_SAFETY_MARGIN)) ) {
                sprites_ObsLst_Fl.remove(i);
                //b Seems to cause score start at -4: Game_Main_JFrame_Cl.SCORE--;
                Game_Main_JFrame_Cl.SCORE--;
            }
		}
	}

	public List<Sprite_CoreExt_Movable_Collidable_Cl> getList()
	{
		return sprites_ObsLst_Fl;
	}

	public String toString()
	{
		return "" + sprites_ObsLst_Fl;
	}
}

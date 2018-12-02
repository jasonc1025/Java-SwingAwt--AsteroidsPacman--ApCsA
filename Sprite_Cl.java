//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;

public class Sprite_Cl {
	//y- protected Image image_Fl;
	private Image image_Fl;
	private int positionX_Fl;
	private int positionY_Fl;
	private int imageWidth_Fl;
	private int imageHeight_Fl;
	//y- private int speed_Fl;
	private int speed_Fl;
	private boolean aiMode_Bool_Fl;

	public Sprite_Cl()
	{
		this("/images/Circle-Green-20x20.png",0,0,0, true);
	}

	public Sprite_Cl(String imageFileIn, int x, int y, int s, boolean aiModeBool_In)
	{
		positionX_Fl = x;
		positionY_Fl = y;
		//y- imageWidth_Fl = w;
		//y- imageHeight_Fl = h;

		try
		{
			//y- URL url = getClass().getResource("/images/ship.jpg");
			URL url = getClass().getResource(imageFileIn);
			setImage(ImageIO.read(url));
			this.imageWidth_Fl = image_Fl.getWidth( null );
			this.imageHeight_Fl = image_Fl.getHeight( null );
		}
		catch(Exception e)
		{
			//feel free to do something here
		}
		setSpeed(s);
		setAiMode_Bool(aiModeBool_In);
	}

	public Sprite_Cl(String imageFileIn, int x, int y, int w, int h, int s, boolean aiModeBool_In)
	{
		positionX_Fl = x;
		positionY_Fl = y;

		try
		{
			//y- URL url = getClass().getResource("/images/ship.jpg");
			URL url = getClass().getResource(imageFileIn);
			setImage(ImageIO.read(url));
			this.imageWidth_Fl = w;
			this.imageHeight_Fl = h;
		}
		catch(Exception e)
		{
			//feel free to do something here
		}
		setSpeed(s);
        setAiMode_Bool(aiModeBool_In);
	}

	public void setPos( int x, int y)
	{
		positionX_Fl = x;
		positionY_Fl = y;
	}

	public void setX(int x)
	{
		positionX_Fl =x;
	}

	public void setY(int y)
	{
		positionY_Fl =y;
	}

	public int getX()
	{
		return positionX_Fl;
	}

	public int getY()
	{
		return positionY_Fl;
	}

	public void setWidth(int w)
	{
		imageWidth_Fl = w;
	}

	public void setHeight(int h)
	{
		imageHeight_Fl = h;
	}

	public int getWidth()
	{
		return imageWidth_Fl;
	}

	public int getHeight()
	{
		return imageHeight_Fl;
	}

	//y- public void setSpeed(int s)
	// {
	//    speed_Fl=s;
	// }
	//y- public int getSpeed()
	// {
	//    return speed_Fl;
	// }

	//y- public abstract void move(String direction);
	//o- public void draw(Graphics window);
	public void draw( Graphics window )
	{
		window.drawImage(getImage(),getX(),getY(),getWidth(),getHeight(),null);
	}

	public String toString()
	{
		return getX() + " " + getY() + " " + getWidth() + " " + getHeight();
	}

	public Image getImage() {
		return image_Fl;
	}

	public void setImage(Image imageFlIn) {
		this.image_Fl = imageFlIn;
		this.imageWidth_Fl = imageFlIn.getWidth( null );
		this.imageHeight_Fl = imageFlIn.getHeight( null );
	}
	public void setImageSize(int w, int h) {
		this.imageWidth_Fl = w;
		this.imageHeight_Fl = h;
	}

	public void setSpeed(int s)
	{
		speed_Fl =s;
	}
	public int getSpeed()
	{
		return speed_Fl;
	}

    public void setAiMode_Bool(boolean aiMode_Bool_In)
    {
        aiMode_Bool_Fl = aiMode_Bool_In;
        return;
    }
    public boolean getAiMode_Bool()
    {
        return aiMode_Bool_Fl;
    }


	public void move(Game_Cycle_JPanel_Cl.Direction_Enum direction_Enum_In)
	{
	    int positionX_New = getX();
	    int positionY_New = getY();

		//o- if(direction_Enum_In.equals("LEFT"))
		if(direction_Enum_In == Game_Cycle_JPanel_Cl.Direction_Enum.LEFT)
		{
		    positionX_New = getX() - getSpeed();
        }
        //o- else if(direction_Enum_In.equals("RIGHT"))
		else if(direction_Enum_In == Game_Cycle_JPanel_Cl.Direction_Enum.RIGHT)
		{
            //			setX(getX()+getSpeed());
            positionX_New = getX() + getSpeed();
        }
        //o- else if(direction_Enum_In.equals("UP"))
		else if(direction_Enum_In == Game_Cycle_JPanel_Cl.Direction_Enum.UP)
		{
            //            setY(getY() - getSpeed());
            positionY_New = getY() - getSpeed();
        }
        //o- else if(direction_Enum_In.equals("DOWN"))
        else if(direction_Enum_In == Game_Cycle_JPanel_Cl.Direction_Enum.DOWN)
        {
            //            setY(getY()+getSpeed());
            positionY_New = getY() + getSpeed();
        }

        // * Check Boundaries: X
        //
		if (!boundaryOk_PosX_Mth(positionX_New, this.getWidth()))
        {
            if(aiMode_Bool_Fl){
                // * reverse speed
                setSpeed(-getSpeed());
            }
            // * remain at old position
            positionX_New = getX();
        }
        setX(positionX_New);

		// * Check Boundaries: Y
        //
        if (!boundaryOk_PosY_Mth(positionY_New, this.getHeight()))
        {
            if(aiMode_Bool_Fl){
                // * reverse speed
                setSpeed(-getSpeed());
            }
            // * remain at old position
            positionY_New = getY();
        }
        setY(positionY_New);

	}

    public boolean boundaryOk_PosX_Mth(int positionX_In, int spriteWidth_In)
    {
        boolean boundaryOk_Bool = true;
	    if( (positionX_In < 0) || (positionX_In > Game_Main_JFrame_Cl.WIDTH-spriteWidth_In) )
        {
           boundaryOk_Bool = false;
        }
        return boundaryOk_Bool;
    }

    public boolean boundaryOk_PosY_Mth(int positionY_In, int spriteHeigth_In)
    {
        boolean boundaryOk_Bool = true;
        if( (positionY_In < 0) || (positionY_In > Game_Main_JFrame_Cl.HEIGHT-spriteHeigth_In) )
        {
            boundaryOk_Bool = false;
        }
        return boundaryOk_Bool;
    }

    public boolean colliding( Sprite_Cl spriteOtherIn )
    {
        boolean colliding_Boo = false;  // * default to false

        if (
             ((this.getX() + this.getWidth() >= spriteOtherIn.getX()) && (this.getY() + this.getHeight() >= spriteOtherIn.getY())) &&
             ((this.getX() <= spriteOtherIn.getX() + spriteOtherIn.getWidth()) && (this.getY() <= spriteOtherIn.getY() + spriteOtherIn.getHeight()))
           ){
            colliding_Boo = true;
        }
        return colliding_Boo;
    }

}
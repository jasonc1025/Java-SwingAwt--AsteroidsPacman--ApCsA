//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;

public class Sprite_Cl {
	//y- protected Image image_Fld;
	private Image image_Fld;
//	private int positionX_Fld;
//	private int positionY_Fld;
//	private int velocityX_Fld;
//	private int velocityY_Fld;
	private int positionX_Fld;
	private int positionY_Fld;
	private int velocityX_Fld;
	private int velocityY_Fld;
	private int imageWidth_Fld;
	private int imageHeight_Fld;
	private int speed_Fld;
	private boolean aiMode_Bool_Fld;

	public Sprite_Cl()
	{
		this("/images/Circle-Green-20x20.png",0,0,0, true);
	}

	public Sprite_Cl(String imageFileIn, int x, int y, int s, boolean aiModeBool_In)
	{
		positionX_Fld = x;
		positionY_Fld = y;
		//y- imageWidth_Fld = w;
		//y- imageHeight_Fld = h;

		try
		{
			//y- URL url = getClass().getResource("/images/ship.jpg");
			URL url = getClass().getResource(imageFileIn);
			setImage(ImageIO.read(url));
			this.imageWidth_Fld = image_Fld.getWidth( null );
			this.imageHeight_Fld = image_Fld.getHeight( null );
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
		positionX_Fld = x;
		positionY_Fld = y;

		try
		{
			//y- URL url = getClass().getResource("/images/ship.jpg");
			URL url = getClass().getResource(imageFileIn);
			setImage(ImageIO.read(url));
			this.imageWidth_Fld = w;
			this.imageHeight_Fld = h;
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
		positionX_Fld = x;
		positionY_Fld = y;
	}

	public void setX(int x)
	{
		if(x < 0){ x = 0; }
		if(x > Game_Main_JFrame_Cl.WIDTH){ x = Game_Main_JFrame_Cl.WIDTH; }
		positionX_Fld = x;
	}

	public void setY(int y)
	{
		if(y < 0){ y = 0; }
		if(y > Game_Main_JFrame_Cl.HEIGHT){ y = Game_Main_JFrame_Cl.HEIGHT; }
		positionY_Fld = y;
	}

	public int getX()
	{
		return positionX_Fld;
	}

	public int getY()
	{
		return positionY_Fld;
	}

	public void setWidth(int w)
	{
		imageWidth_Fld = w;
	}

	public void setHeight(int h)
	{
		imageHeight_Fld = h;
	}

	public int getWidth()
	{
		return imageWidth_Fld;
	}

	public int getHeight()
	{
		return imageHeight_Fld;
	}

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
		return image_Fld;
	}

	public void setImage(Image imageFlIn) {
		this.image_Fld = imageFlIn;
		this.imageWidth_Fld = imageFlIn.getWidth( null );
		this.imageHeight_Fld = imageFlIn.getHeight( null );
	}
	public void setImageSize(int w, int h) {
		this.imageWidth_Fld = w;
		this.imageHeight_Fld = h;
	}

	public void setSpeed(int s)
	{
		speed_Fld = s;
	}
	public int getSpeed()
	{
		return speed_Fld;
	}

	public void setVelocityX(int velocityX_In){
		this.velocityX_Fld = velocityX_In;
	}
	public void setVelocityY(int velocityY_In){
		this.velocityY_Fld = velocityY_In;
	}
	public int getVelocityX(){
		return this.velocityX_Fld;
	}
	public int getVelocityY(){
		return this.velocityY_Fld;
	}

	public void setAiMode_Bool(boolean aiMode_Bool_In)
    {
        aiMode_Bool_Fld = aiMode_Bool_In;
        return;
    }
    public boolean getAiMode_Bool()
    {
        return aiMode_Bool_Fld;
    }


	public void move(Game_Cycle_JPanel_Cl.Direction_Enum direction_Enum_In)
	{
	    int positionX_New = getX();
	    int positionY_New = getY();

		//o- 		//o- if(direction_Enum_In.equals("LEFT"))
		//		if(direction_Enum_In == Game_Cycle_JPanel_Cl.Direction_Enum.LEFT)
		//		{
		//		    positionX_New = getX() - getSpeed();
		//        }
		//        //o- else if(direction_Enum_In.equals("RIGHT"))
		//		else if(direction_Enum_In == Game_Cycle_JPanel_Cl.Direction_Enum.RIGHT)
		//		{
		//            //			setX(getX()+getSpeed());
		//            positionX_New = getX() + getSpeed();
		//        }
		//        //o- else if(direction_Enum_In.equals("UP"))
		//		else if(direction_Enum_In == Game_Cycle_JPanel_Cl.Direction_Enum.UP)
		//		{
		//            //            setY(getY() - getSpeed());
		//            positionY_New = getY() - getSpeed();
		//        }
		//        //o- else if(direction_Enum_In.equals("DOWN"))
		//        else if(direction_Enum_In == Game_Cycle_JPanel_Cl.Direction_Enum.DOWN)
		//        {
		//            //            setY(getY()+getSpeed());
		//            positionY_New = getY() + getSpeed();
		//        }

		this.positionX_Fld += this.velocityX_Fld;
		this.positionY_Fld += this.velocityY_Fld;

        // * Check Boundaries: X
        //
		if (!boundaryOk_PosX_Mth(positionX_New, this.getWidth()))
        {
            if(aiMode_Bool_Fld){
                // * reverse speed
				//				setSpeed(-getSpeed());
				setVelocityX(-getVelocityX());
            }
            // * remain at old position
            positionX_New = getX();
        }
        setX(positionX_New);

		// * Check Boundaries: Y
        //
        if (!boundaryOk_PosY_Mth(positionY_New, this.getHeight()))
        {
            if(aiMode_Bool_Fld){
                // * reverse speed
				//                setSpeed(-getSpeed());
				setVelocityY(-getVelocityY());
            }
            // * remain at old position
            positionY_New = getY();
        }
        setY(positionY_New);

	}


	public void move()
	{
		int positionX_New = getX();
		int positionY_New = getY();

		positionX_New += this.velocityX_Fld;
		positionY_New += this.velocityY_Fld;

		// * Check Boundaries: X
		//
		if (!boundaryOk_PosX_Mth(positionX_New, this.getWidth()))
		{
//			if(aiMode_Bool_Fld){
				// * reverse speed
				//				setSpeed(-getSpeed());
				setVelocityX(-getVelocityX());
//			}
			// * remain at old position
			positionX_New = getX();
		}
		setX(positionX_New);

		// * Check Boundaries: Y
		//
		if (!boundaryOk_PosY_Mth(positionY_New, this.getHeight()))
		{
//			if(aiMode_Bool_Fld){
				// * reverse speed
				//                setSpeed(-getSpeed());
				setVelocityY(-getVelocityY());
//			}
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
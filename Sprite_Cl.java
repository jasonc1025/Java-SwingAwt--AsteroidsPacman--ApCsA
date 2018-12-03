//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;

public class Sprite_Cl {
	//y- protected Image image_Fld;
	private Image image_Fld;
	private int imageWidth_Fld;
	private int imageHeight_Fld;

	private int positionX_Fld;
	private int positionY_Fld;

	private int velocityX_Fld;
	private int velocityY_Fld;

	private boolean aiMode_Bool_Fld;

	public Sprite_Cl()
	{
		this("/images/Circle-Green-20x20.png",0,0, 0, 0, true);
	}

	public Sprite_Cl(String imageFile_In, int x_In, int y_In, int velocityX_In, int velocityY_In, boolean aiModeBool_In)
	{
		positionX_Fld = x_In;
		positionY_Fld = y_In;
		//y- imageWidth_Fld = w;
		//y- imageHeight_Fld = h;

		try
		{
			//y- URL url = getClass().getResource("/images/ship.jpg");
			URL url = getClass().getResource(imageFile_In);
			setImage_Mth(ImageIO.read(url));
			this.imageWidth_Fld = image_Fld.getWidth( null );
			this.imageHeight_Fld = image_Fld.getHeight( null );
		}
		catch(Exception e)
		{
			//feel free to do something here
		}
		//		setSpeed(s);
		velocityX_Fld = velocityX_In;
		velocityY_Fld = velocityY_In;

		setAiMode_Bool_Mth(aiModeBool_In);
	}

	public Sprite_Cl(String imageFile_In, int x_In, int y_In, int width_In, int height_In, int velocityX_In, int velocityY_In, boolean aiModeBool_In)
	{
		positionX_Fld = x_In;
		positionY_Fld = y_In;

		try
		{
			//y- URL url = getClass().getResource("/images/ship.jpg");
			URL url = getClass().getResource(imageFile_In);
			setImage_Mth(ImageIO.read(url));
			this.imageWidth_Fld = width_In;
			this.imageHeight_Fld = height_In;
		}
		catch(Exception e)
		{
			//feel free to do something here
		}
		// setSpeed(s);
		velocityX_Fld = velocityX_In;
		velocityY_Fld = velocityY_In;
        setAiMode_Bool_Mth(aiModeBool_In);
	}

	public void setPos_Mth(int x_In, int y_In)
	{
		positionX_Fld = x_In;
		positionY_Fld = y_In;
	}

	public void setX_Mth(int x_In)
	{
		if(x_In < 0){ x_In = 0; }
		if(x_In > Game_Main_JFrame_Cl.WIDTH){ x_In = Game_Main_JFrame_Cl.WIDTH; }
		positionX_Fld = x_In;
	}

	public void setY_Mth(int y_In)
	{
		if(y_In < 0){ y_In = 0; }
		if(y_In > Game_Main_JFrame_Cl.HEIGHT){ y_In = Game_Main_JFrame_Cl.HEIGHT; }
		positionY_Fld = y_In;
	}

	public int getX_Mth()
	{
		return positionX_Fld;
	}

	public int getY_Mth()
	{
		return positionY_Fld;
	}

	public void setWidth_Mth(int width_In)
	{
		imageWidth_Fld = width_In;
	}

	public void setHeight_Mth(int height_In)
	{
		imageHeight_Fld = height_In;
	}

	public int getWidth_Mth()
	{
		return imageWidth_Fld;
	}

	public int getHeight_Mth()
	{
		return imageHeight_Fld;
	}

	//y- public abstract void move(String direction);
	//o- public void draw_Mth(Graphics window);
	public void draw_Mth(Graphics window )
	{
		window.drawImage(getImage_Mth(), getX_Mth(), getY_Mth(), getWidth_Mth(), getHeight_Mth(),null);
	}

	public String toString()
	{
		return getX_Mth() + " " + getY_Mth() + " " + getWidth_Mth() + " " + getHeight_Mth();
	}

	public Image getImage_Mth() {
		return image_Fld;
	}

	public void setImage_Mth(Image image_In) {
		this.image_Fld = image_In;
		this.imageWidth_Fld = image_In.getWidth( null );
		this.imageHeight_Fld = image_In.getHeight( null );
	}
	public void setImageSize_Mth(int width_In, int height_In) {
		this.imageWidth_Fld = width_In;
		this.imageHeight_Fld = height_In;
	}

	public void setVelocityX_Mth(int velocityX_In){
		this.velocityX_Fld = velocityX_In;
	}
	public void setVelocityY_Mth(int velocityY_In){
		this.velocityY_Fld = velocityY_In;
	}
	public int getVelocityX_Mth(){
		return this.velocityX_Fld;
	}
	public int getVelocityY_Mth(){
		return this.velocityY_Fld;
	}

	public void setAiMode_Bool_Mth(boolean aiMode_Bool_In)
    {
        aiMode_Bool_Fld = aiMode_Bool_In;
        return;
    }
    public boolean getAiMode_Bool()
    {
        return aiMode_Bool_Fld;
    }

	public void move_Mth()
	{
		int positionX_New = getX_Mth();
		int positionY_New = getY_Mth();

		positionX_New += this.velocityX_Fld;
		positionY_New += this.velocityY_Fld;

		// * Check Boundaries: X
		//
		if (!boundaryOk_PosX_Mth(positionX_New, this.getWidth_Mth()))
		{
//			if(aiMode_Bool_Fld){
				// * reverse speed
				//				setSpeed(-getSpeed());
				setVelocityX_Mth(-getVelocityX_Mth());
//			}
			// * remain at old position
			positionX_New = getX_Mth();
		}
		setX_Mth(positionX_New);

		// * Check Boundaries: Y
		//
		if (!boundaryOk_PosY_Mth(positionY_New, this.getHeight_Mth()))
		{
//			if(aiMode_Bool_Fld){
				// * reverse speed
				//                setSpeed(-getSpeed());
				setVelocityY_Mth(-getVelocityY_Mth());
//			}
			// * remain at old position
			positionY_New = getY_Mth();
		}
		setY_Mth(positionY_New);

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

    public boolean colliding_Mth(Sprite_Cl spriteOther_In )
    {
        boolean colliding_Boo = false;  // * default to false

        if (
             ((this.getX_Mth() + this.getWidth_Mth() >= spriteOther_In.getX_Mth()) && (this.getY_Mth() + this.getHeight_Mth() >= spriteOther_In.getY_Mth())) &&
             ((this.getX_Mth() <= spriteOther_In.getX_Mth() + spriteOther_In.getWidth_Mth()) && (this.getY_Mth() <= spriteOther_In.getY_Mth() + spriteOther_In.getHeight_Mth()))
           ){
            colliding_Boo = true;
        }
        return colliding_Boo;
    }

}
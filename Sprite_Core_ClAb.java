//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;

public abstract class Sprite_Core_ClAb {
	//y- protected Image image_Fld;
	private Image image_Fld;
	private int positionX_Fld;
	private int positionY_Fld;
	private int imageWidth_Fld;
	private int imageHeight_Fld;
	//y- private int speed_Fld;
	protected int speed_Fld;

	public Sprite_Core_ClAb()
	{
		this("/images/Circle-Green-20x20.png",0,0,0);
	}

	public Sprite_Core_ClAb(String imageFileIn, int x, int y, int s)
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
		//y- setSpeed(s);
	}

	public Sprite_Core_ClAb(String imageFileIn, int x, int y, int w, int h, int s)
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
		//y- setSpeed(s);
	}

	public void setPos( int x, int y)
	{
		positionX_Fld = x;
		positionY_Fld = y;
	}

	public void setX(int x)
	{
		positionX_Fld =x;
	}

	public void setY(int y)
	{
		positionY_Fld =y;
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

	//y- public void setSpeed(int s)
	// {
	//    speed_Fld=s;
	// }
	//y- public int getSpeed()
	// {
	//    return speed_Fld;
	// }

	//y- public abstract void move(String direction);
	public abstract void draw(Graphics window);

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
}
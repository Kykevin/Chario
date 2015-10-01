package main;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Sprite {

	protected int x;
	protected int y;
	protected int camOffset = 0;
	protected int width;
	protected int height;
	protected boolean vis;
	protected Image image;

	public Sprite(int x, int y) {

		this.x = x;
		this.y = y;
		vis = true;
	}
	
	public Sprite(int x, int y, int camOffset) {

		this.x = x;
		this.y = y;
		this.camOffset = camOffset;
		vis = true;
	}

	protected void loadImage(String imageName) {

		ImageIcon ii = new ImageIcon(imageName);
		image = ii.getImage();
	}

	protected void getImageDimensions() {

		width = image.getWidth(null);
		height = image.getHeight(null);
	}    

	public Image getImage() {
		return image;
	}

	public int getX() {
		return x - camOffset;
	}

	public int getY() {
		return y;
	}

	public boolean isVisible() {
		return vis;
	}
	
	public int getCamOffset(){
		return camOffset;
	}

	public void setVisible(boolean visible) {
		vis = visible;
	}
}


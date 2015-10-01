package main;

public class Tile extends Sprite{

	public Tile(int x,int y, int tileType, int camOffset){
		super(x,y,camOffset);

		initTile(tileType);
	}

	private void initTile(int tileType){
		loadImage("images/tile"+tileType+".png");
		getImageDimensions();
	}
	
	public void update(){
		if (camOffset - x > 200){
			vis = false;
		}
		if (x - camOffset > 1000){
			vis = false;
		}
	}
}

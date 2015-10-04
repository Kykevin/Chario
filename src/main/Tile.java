package main;

public class Tile extends Sprite{
	public boolean gas = false;
	public boolean explode = false;
	
	public Tile(int x,int y, char tileType, int camOffset){
		super(x,y,camOffset);

		initTile(tileType);
	}

	private void initTile(char tileType){
		if(tileType == 'E'){
			explode = true;
		}		
		if(tileType == 'G'){
			gas = true;
		}
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

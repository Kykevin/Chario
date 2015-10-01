package main;

public class HitBoxManager {

	public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;

	public HitBoxManager(){

	}
	public static boolean[] checkPosition(int [][] mapTileTypes, 
			int camOffset, Sprite object){
		boolean rts[] = new boolean [4];
		rts[0] = true;
		rts[1] = true;
		rts[2] = true;
		rts[3] = true;
		final int x = object.x,
				y = object.y,
				width = object.width,
				height = object.height;
		final int left = x / 100,
				up = y / 100,
				right = (x + width) / 100 - ((x + width) % 100 == 0 ? 1 : 0),
				down = (y + height) / 100 - ((y + height) % 100 == 0 ? 1 : 0);
		//left
		if (x < 0){
			rts[LEFT] = false;
		}
		if (mapTileTypes[up][left] != 0 || mapTileTypes[down][left] != 0){
			rts[LEFT] = false;
		}

		//right
		if (mapTileTypes[up][right] != 0 || mapTileTypes[down][right] != 0){
			rts[RIGHT] = false;
		}

		//up
		if (y < 0){
			rts[UP] = false;
		}
		if (mapTileTypes[up][left] != 0 || mapTileTypes[up][right] != 0){
			rts[UP] = false;
		}

		//down
		if (mapTileTypes[down][left] != 0 || mapTileTypes[down][right] != 0){
			rts[DOWN] = false;
		}
		return rts;
	}

	public static boolean checkHitBetween(Sprite a, Sprite b){
		if(pointInside(a.x, a.y, b)){
			return true;
		}
		if(pointInside(a.x + a.width, a.y, b)){
			return true;
		}
		if(pointInside(a.x, a.y + a.height, b)){
			return true;
		}
		if(pointInside(a.x + a.width, a.y + a.height, b)){
			return true;
		}
		if(pointInside(b.x, b.y, a)){
			return true;
		}
		if(pointInside(b.x + b.width, b.y, a)){
			return true;
		}
		if(pointInside(b.x, b.y + b.height, a)){
			return true;
		}
		if(pointInside(b.x + b.width, b.y + b.height, a)){
			return true;
		}
		return false;
	}

	private static boolean pointInside(int x, int y, Sprite a){
		final int up = a.y;
		final int down = a.y + a.height;
		final int left = a.x;
		final int right = a.x + a.width;
		if (x >= left && x <= right && y >= up && y <= down){
			return true;
		}
		return false;
	}
}

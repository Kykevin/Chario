package main;

public class HitBoxManager {

	public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;

	public HitBoxManager(){

	}
	public static boolean[] checkPosition(char [][] mapTileTypes, 
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
		final int left = x / 50,
				up = y / 50,
				right = (x + width) / 50 - ((x + width) % 50 == 0 ? 1 : 0),
				down = (y + height) / 50 - ((y + height) % 50 == 0 ? 1 : 0);
		//left
		if (x < 0){
			rts[LEFT] = false;
			rts[UP] = false;
			rts[DOWN] = false;
			if (mapTileTypes[up][right] != ' ' || mapTileTypes[down][right] != ' '){
				rts[RIGHT] = false;
			}
		}
		else if (y < 0){
			rts[UP] = false;
			rts[LEFT] = false;
			rts[RIGHT] = false;
			if (mapTileTypes[down][left] != ' ' || mapTileTypes[down][right] != ' '){
				rts[DOWN] = false;
			}			
		}
		else {
			//left
			if (mapTileTypes[up][left] != ' ' || mapTileTypes[down][left] != ' '){
				rts[LEFT] = false;
			}

			//right
			if (mapTileTypes[up][right] != ' ' || mapTileTypes[down][right] != ' '){
				rts[RIGHT] = false;
			}

			//up
			if (mapTileTypes[up][left] != ' ' || mapTileTypes[up][right] != ' '){
				rts[UP] = false;
			}

			//down
			if (mapTileTypes[down][left] != ' ' || mapTileTypes[down][right] != ' '){
				rts[DOWN] = false;
			}			
		}
		return rts;
	}

	public static boolean checkOnGround(char [][] mapTileTypes, 
			int camOffset, Sprite object){
		final int x = object.x,
				y = object.y+1,
				width = object.width,
				height = object.height;
		final int left = x / 50,
				right = (x + width) / 50 - ((x + width) % 50 == 0 ? 1 : 0),
				down = (y + height) / 50 - ((y + height) % 50 == 0 ? 1 : 0);
		if (mapTileTypes[down][left] != ' ' || mapTileTypes[down][right] != ' '){
			return true;
		}
		return false;
	}

	public static boolean checkHitBetween(Sprite a, Sprite b){
		int centerXa = a.x + a.width/2;
		int centerYa = a.y + a.height/2;
		int centerXb = b.x + b.width/2;
		int centerYb = b.y + b.height/2;
//		System.out.println(Math.abs(centerXa - centerXb));
		if(Math.abs(centerXa - centerXb) < a.width/2 + b.width/2 && Math.abs(centerYa - centerYb) < a.height/2+b.height/2){
			return true;
		}
		else return false;
	}

}

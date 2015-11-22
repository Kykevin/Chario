package main;

import java.util.ArrayList;

public class Monster extends Sprite {

	private int vx = -1;
	private double vy = 0;
	private ArrayList<Missile> missiles;
	private boolean isActive = false;
	private final int SHOOTINGDELAY = 100;
	private final int ACTIVEDELAY = 50;
	public int activeTimer = 0;
	private int shootingTimer = 0;
	private int initCharX = 0;

	public Monster(int x, int y) {
		super(x, y);
		initMonster();
	}

	private void initMonster() {

		missiles = new ArrayList<Missile>();
		loadImage("images/monster.png"); 
		getImageDimensions();
	}

	public void move(char [][] mapTileTypes, int charX) {
		//		System.out.println(activeTimer);
		if(initCharX == 0){
			if(this.getX() < 750)
				initCharX = charX;
		}
		else {
			if (charX - initCharX >= 100 || charX - initCharX <= -100){
				isActive = true;
			}
			if (isActive){
				boolean pos[];
				x += vx;
				pos = HitBoxManager.checkPosition(mapTileTypes, camOffset, this);
				if (pos[HitBoxManager.LEFT] == false || pos[HitBoxManager.RIGHT] == false){
					x -= vx;
				}
				if (shootingTimer % SHOOTINGDELAY == 0){
					fire();
				}
				shootingTimer++;
			}
			else if (this.getX() <= 750) {
				activeTimer++;
				if (activeTimer == ACTIVEDELAY){
					isActive = true;
				}
			}
			boolean pos[];
			//		System.out.println(HitBoxManager.checkOnGround(mapTileTypes, camOffset, this));
			y += vy;
			pos = HitBoxManager.checkPosition(mapTileTypes, camOffset, this);
			if (pos[HitBoxManager.DOWN] == false){
				y -= vy;
			}
			vy += MainScreen.GRAVITY;
			if (HitBoxManager.checkOnGround(mapTileTypes, camOffset, this)){
				vy = 0;
			}
		}
	}

	public ArrayList<Missile> getMissiles() {
		return missiles;
	}


	public void fire() {
		SoundPlayer.getInstance().play("monsShoot");

		missiles.add(new Missile(x - 67, y + height / 2 - 20,1));
	}


}

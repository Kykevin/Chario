package main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Monster extends Sprite {

	private int vx;
	private int vy;
	private ArrayList<Missile> missiles;
	private boolean isActive;
	private final int SHOOTINGDELAY = 100;
	private final int ACTIVEDELAY = 50;
	public int activeTimer = 0;
	private int shootingTimer = 0;

	public Monster(int x, int y) {
		super(x, y);

		initMonster();
	}

	private void initMonster() {

		missiles = new ArrayList<Missile>();
		loadImage("images/monster.png"); 
		getImageDimensions();
	}

	public void move(int [][] mapTileTypes) {
		if (isActive){
			boolean pos[];
			x += vx;
			pos = HitBoxManager.checkPosition(mapTileTypes, camOffset, this);
			if (pos[HitBoxManager.LEFT] == false || pos[HitBoxManager.RIGHT] == false){
				x -= vx;
			}
			y += vy;
			pos = HitBoxManager.checkPosition(mapTileTypes, camOffset, this);
			if (pos[HitBoxManager.UP] == false || pos[HitBoxManager.DOWN] == false){
				y -= vy;
			}
			vy += MainScreen.GRAVITY;
			y += 1;
			pos = HitBoxManager.checkPosition(mapTileTypes, camOffset, this);
			if (pos[HitBoxManager.DOWN] == false){
					vy = 0;
			}
			y -= 1;
			if (shootingTimer % SHOOTINGDELAY == 0){
				fire();
			}
			shootingTimer++;
		}
		else {
			activeTimer++;
			if (activeTimer == ACTIVEDELAY){
				isActive = true;
				vx = -1;
			}
		}

	}

	public ArrayList<Missile> getMissiles() {
		return missiles;
	}


	public void fire() {
		SoundPlayer.getInstance().play("monsShoot");

		missiles.add(new Missile(x - 67, y + height / 2 - 40,1));
	}


}

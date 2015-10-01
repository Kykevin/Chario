package main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;




public class Char extends Sprite {

	private int vx;
	private double vy;
	private ArrayList<Missile> missiles;
	private boolean onGround = true;
	private boolean shooting = false;
	private boolean jumping = false;
	private final int SHOOTINGDELAY = 50;
	private final int ONESEC = 100;
	private int shootingTimer = 0;
	private boolean lefting = false;
	private boolean righting = false;
	public int health = 20;
	private final int HEALTHCAP = 40; 


	public Char(int x, int y) {
		super(x, y);

		initChar();
	}

	private void initChar() {

		missiles = new ArrayList<Missile>();
		loadImage("images/character.png"); 
		getImageDimensions();
	}

	public void move(int [][] mapTileTypes, int camOffset) {
		boolean pos[];
		if(shooting){
			if (shootingTimer % (SHOOTINGDELAY * 10 + ONESEC) % SHOOTINGDELAY
					== 0 &&	shootingTimer % (SHOOTINGDELAY * 10 + ONESEC)
					< SHOOTINGDELAY * 10){
				fire();
			}
			shootingTimer++;
		}
		if (lefting && !righting){
			vx = -4;
		}
		else if (righting && !lefting){
			vx = 4;
		}
		else {
			vx = 0;
		}
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
		vy += 0.13;
		y += 1;
		pos = HitBoxManager.checkPosition(mapTileTypes, camOffset, this);
		if (pos[HitBoxManager.DOWN] == false){
			onGround = true;
			if(jumping){
				vy = -5;
			}
			else{
				vy = 0;
			}
		}
		else {
			onGround = false;
		}
		y -= 1;
		if (x - camOffset < 100 && camOffset + vx >= 0 && lefting){
			this.camOffset += vx;
		}
		if (x - camOffset > 250 && righting){
			this.camOffset += vx;
		}
	}

	public ArrayList<Missile> getMissiles() {
		return missiles;
	}

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == KeyEvent.VK_S) {
			shooting = true;
		}

		if (key == KeyEvent.VK_LEFT) {
			lefting = true;
		}

		if (key == KeyEvent.VK_RIGHT) {
			righting = true;
		}

		if (key == KeyEvent.VK_UP) {
			jumping = true;
		}
	}

	public void fire() {
		missiles.add(new Missile(x + width, y + height / 2 - 20,0));
	}

	public void keyReleased(KeyEvent e) {

		int key = e.getKeyCode();
		if (key == KeyEvent.VK_S) {
			shooting = false;
			shootingTimer = 0;
		}
		if (key == KeyEvent.VK_LEFT) {
			lefting = false;
		}

		if (key == KeyEvent.VK_RIGHT) {
			righting = false;
		}
		if (key == KeyEvent.VK_UP) {
			jumping = false;
		}
	}
}


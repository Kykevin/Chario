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

	public int invulnerableTimer = 0;
	private int gasTimer = 0;

	private boolean lefting = false;
	private boolean righting = false;

	private int motionTimer = 0;
	private int stayTimer = 0;
	public int health = 20;
	private final int HEALTHCAP = 40; 
	public int endOffset;

	public Char(int x, int y) {
		super(x, y);
		initChar();
	}

	private void initChar() {

		missiles = new ArrayList<Missile>();
		loadImage("images/character.png"); 
		getImageDimensions();
	}

	public void move(char [][] mapTileTypes, int camOffset) {
		if (health >= HEALTHCAP){
			health = HEALTHCAP;
		}
//		System.out.println(onGround);
		if (health != 0){

			if(invulnerableTimer > 0){
				invulnerableTimer --;
			}
			if(gasTimer > 0){
				gasTimer --;
			}
			else{
				if(shooting){
					if (shootingTimer % SHOOTINGDELAY == 0)	{
						fire();
						if(shootingTimer == SHOOTINGDELAY * 10){
							gasTimer = 100;
							shootingTimer = 0;
						}
					}
					shootingTimer++;
				}
			}
			boolean pos[];
			if (!lefting && !righting && onGround){
				stayTimer++;
				motionTimer = 0;
			}
			else {
				motionTimer++;
				stayTimer = 0;
			}
			if ((stayTimer+1) % 100 == 0){
				health += 5;
			}
			if ((motionTimer+1) % 20 == 0){
				health += 1;
			}
			if (health >= HEALTHCAP){
				health = HEALTHCAP;
			}

			if (lefting && !righting){
				vx = -3;
			}
			else if (righting && !lefting){
				vx = 3;
			}
			else {
				vx = 0;
			}
			x += vx;
			pos = HitBoxManager.checkPosition(mapTileTypes, camOffset, this);
			if (pos[HitBoxManager.LEFT] == false){
				if (x < 0){
					x = 0;
				}
				else {
					x = (x/50+1)*50;
				}
			}
			if(pos[HitBoxManager.RIGHT] == false){
				x = x/50*50;
			}

			vy += MainScreen.GRAVITY;
			y += vy;
			pos = HitBoxManager.checkPosition(mapTileTypes, camOffset, this);
			if(pos[HitBoxManager.DOWN] == false){
				y = y/50*50;
				if(jumping){
					vy = -6.8;
				}
				else{
					vy = 0;
				}
				final int left = x / 50,
						right = (x + width) / 50 - ((x + width) % 50 == 0 ? 1 : 0),
						down = (y + height) / 50;
				if(mapTileTypes[down][left] == 'G' || mapTileTypes[down][right] == 'G'){
					gasTimer = 100;
				}			
			}
			onGround = HitBoxManager.checkOnGround(mapTileTypes, camOffset, this);
			if(pos[HitBoxManager.UP] == false){
				if (y < 0){
					y = 0;
				}
				else {
					y = (y/50+1)*50;
				}
			}
			if (pos[HitBoxManager.UP] == false && vy <= 0){
				vy = 0;			
			}
			if (x - camOffset < 100 && camOffset + vx >= 0 && lefting){
				this.camOffset += vx;
			}
			if (x - camOffset > 250 && righting){
				this.camOffset += vx;
				if(this.camOffset > this.endOffset){
					this.camOffset = this.endOffset;
				}
			}
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
		SoundPlayer.getInstance().play("charShoot");
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


//y += 1;
//			System.out.println(vy + " " + pos[HitBoxManager.UP]);
//pos = HitBoxManager.checkPosition(mapTileTypes, camOffset, this);
//			if (pos[HitBoxManager.DOWN] == false){
//				onGround = true;
//				if(jumping){
//					vy = -6.8;
//				}
//				else{
//					vy = 0;
//				}
//			}
//			else {
//				onGround = false;
//			}
//y -= 1;

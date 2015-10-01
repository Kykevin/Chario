package main;

public class Missile extends Sprite {

	private final double MISSILE_SPEED[] = {6,-4};
	private int distance = 0;
	private int type;
	
	public Missile(int x, int y, int type) {
		super(x, y);
		this.type = type;
		initMissile(type);
	}

	private void initMissile(int type) {
		loadImage("images/missile"+type+".png");  
		getImageDimensions();
	}


	public void move() {

		x += MISSILE_SPEED[type];
		distance += MISSILE_SPEED[type];
		if (distance > 400 || distance < -400) {
			vis = false;
		}
	}
}

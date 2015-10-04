package main;

public class PowerUp extends Sprite{
	public char powerUpType;
	public PowerUp(int x, int y, int camOffset, char powerUpType){
		super(x, y, camOffset);


		initPowerUp(powerUpType);
	}

	private void initPowerUp(char powerUpType){
		this.powerUpType = powerUpType;
		loadImage("images/powerUp"+powerUpType+".png");
		getImageDimensions();
	}

	public void update(int camOffset){
		this.camOffset = camOffset;
	}

}

package main;

import java.awt.*;
import java.awt.event.*;

import javax.sound.sampled.AudioFormat;
import javax.swing.JFrame;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;



public class MainScreen extends JPanel implements ActionListener {

	private final char 
	NORMALTOP = 'T',
	NORMAL = 'N',
	EXPLODING = 'E',
	EXPLODED = 'D',
	GAS = 'G',
	MONSTER = 'M',
	MUSHROOM = 'R',
	STAR = 'S',
	NONE = ' ';



	public static final double GRAVITY = 0.13;

	private final int INITCHAR_X = 100;
	private final int INITCHAR_Y = 450;
	private final int DELAY = 10;
	private Timer timer;
	private Char character;
	private ArrayList<Monster> monsters;
	private ArrayList<Tile> mapTiles;
	private ArrayList<PowerUp> powerUps; 
	private char mapTileTypes[][] = new char [12][1000];
	private boolean mapTileDrawed[][] = new boolean [12][1000];
	int camOffset = 0;
	boolean died = false;

	private JLabel health = new JLabel();
	private JLabel dieMsg = new JLabel("R.I.P.");

	public MainScreen(){
		initUI();
		//default map
		int column, row;
		for(column = 0;column < 1000;++column){
			for(row = 0; row < 12;++row){
				if (row == 9 && column % 10 == 9){
					mapTileTypes[row][column] = MONSTER;
				}
				else if (row == 11){
					mapTileTypes[row][column] = NORMAL;
				}
				else if (row == 10){
					mapTileTypes[row][column] = NORMALTOP;
				}
				else if (row == 9 && column % 10 == 5){
					mapTileTypes[row][column] = STAR;
				}
				else if (row == 4 && column % 10 == 7){
					mapTileTypes[row][column] = MUSHROOM;
				}
				else if (row == 6 && column % 10 == 5){
					mapTileTypes[row][column] = NORMALTOP;
				}
				else {
					mapTileTypes[row][column] = NONE;
				}
				if (row == 10 && column % 10 == 3){
					mapTileTypes[row][column] = EXPLODING;
				}
				if (row == 10 && column % 10 == 8){
					mapTileTypes[row][column] = GAS;
				}
				
			}
		}
		initTiles();
	}

	public MainScreen(String s[]){
		initUI();
		initTiles();
	}

	private void initUI(){
		JFrame screen = new JFrame("My Game");



		screen.add(this);

		screen.setSize(800, 630);    
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		setLocation((screenWidth-800) / 2, (screenHeight-600) / 2);
		screen.setLocationRelativeTo(null);
		screen.setResizable(false);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setVisible(true);

		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);

		character = new Char(INITCHAR_X, INITCHAR_Y);
		this.setLayout(null);
		timer = new Timer(DELAY, this);
		timer.start();

		health.setBounds(300, 0, 200, 50);
		health.setHorizontalAlignment(JLabel.CENTER);
		health.setFont(new Font("Verdana",1,20));
		health.setForeground(Color.white);
		health.setText("Health: "+character.health);
		this.add(health);
		dieMsg.setBounds(0, 0, 800, 600);
		dieMsg.setHorizontalAlignment(JLabel.CENTER);
		dieMsg.setFont(new Font("Verdana",1,200));
		dieMsg.setForeground(Color.white);
	}

	private void initTiles(){
		mapTiles = new ArrayList<Tile>();
		monsters = new ArrayList<Monster>();
		powerUps = new ArrayList<PowerUp>();
		int row,column;
		for(row = 0;row < 12;++row){
			for(column = 0;column < 20;++column){
				mapTileDrawed[row][column] = true;
				if (mapTileTypes[row][column] == MONSTER) {
					Monster tempMonster = new Monster(column * 50,
							row * 50,character.x);
					mapTileTypes[row][column] = NONE;
					monsters.add(tempMonster);
				}
				else if(mapTileTypes[row][column] == STAR || mapTileTypes[row][column] == MUSHROOM){
					PowerUp tempPU = new PowerUp(column * 50,
							row * 50, character.camOffset, mapTileTypes[row][column]);
					mapTileTypes[row][column] = NONE;
					powerUps.add(tempPU);
				}
				else if (mapTileTypes[row][column] != NONE){
					Tile tempTile = new Tile(column * 50,
							row * 50, mapTileTypes[row][column],camOffset);
					mapTiles.add(tempTile);
				}

			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);

		Toolkit.getDefaultToolkit().sync();
	}

	private void doDrawing(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(character.getImage(), character.getX(),
				character.getY(), this);

		ArrayList<Missile> charMissiles = character.getMissiles();

		for (Missile currMissile : charMissiles) {
			g2d.drawImage(currMissile.getImage(), currMissile.getX(),
					currMissile.getY(), this);
		}

		for (Tile currTile : mapTiles) {
			g2d.drawImage(currTile.getImage(), currTile.getX(),
					currTile.getY(), this);
		}

		for (Monster currMonster : monsters){
			g2d.drawImage(currMonster.getImage(), currMonster.getX(),
					currMonster.getY(), this);
			ArrayList<Missile> monsterMissiles = currMonster.getMissiles();
			for (Missile currMissile : monsterMissiles) {
				g2d.drawImage(currMissile.getImage(), currMissile.getX(),
						currMissile.getY(), this);
			}
		}

		for (PowerUp currPowerUp : powerUps){
			g2d.drawImage(currPowerUp.getImage(), currPowerUp.getX(),
					currPowerUp.getY(), this);
		}

	}

	public void actionPerformed(ActionEvent e) {
		checkHits();

		updateMissiles();
		updateChar();
		updateTiles();
		updateMonsters();
		updatePowerUps();
		repaint();
	}

	private void checkHits(){
		//char shoot monster
		ArrayList<Missile> charMissiles = character.getMissiles();
		for (Missile currMissile : charMissiles) {
			for (int i = 0; i < monsters.size(); i++){
				Monster currMonster = monsters.get(i); 
				if (HitBoxManager.checkHitBetween(currMissile, currMonster)){
					monsters.remove(i);
					currMissile.vis = false;
					//play monster die sound
					character.health += 10;
					SoundPlayer.getInstance().play("monsDie");
				}
			}
		}

		//monster shoot char
		for (Monster currMonster : monsters){
			if (character.invulnerableTimer == 0){
				ArrayList<Missile> monsterMissiles = currMonster.getMissiles();
				for (Missile currMissile : monsterMissiles) {
					if (HitBoxManager.checkHitBetween(currMissile, character)){
						//Char.shotByMissile();
						//play char get shot sound;
						if (died == false){
							SoundPlayer.getInstance().play("charShot");
						}
						currMissile.vis = false;
						character.health -= 5;
						if (character.health <= 0) {
							character.health = 0;
							if (died == false){
								SoundPlayer.getInstance().play("charDie");
								died = true;
								this.add(dieMsg);
							}
						}
					}
				}
			}			
			//char hit monster
			if(HitBoxManager.checkHitBetween(currMonster, character)){
				//Char.die();

				character.health = 0;

				//play char died sound.
				if (died == false){
					SoundPlayer.getInstance().play("charDie");
					died = true;
					this.add(dieMsg);
				}			
			}
		}
		for (int i = 0; i < powerUps.size(); i++){
			PowerUp currPowerUp = powerUps.get(i); 
			if (HitBoxManager.checkHitBetween(currPowerUp, character)){
				currPowerUp.vis = false;
				powerUps.remove(i);
				if(currPowerUp.powerUpType == MUSHROOM){
					character.health +=5;
					SoundPlayer.getInstance().play("mush");
				}
				else if (currPowerUp.powerUpType == STAR){
					character.invulnerableTimer = 100;
					SoundPlayer.getInstance().play("star");
				}

			}
		}
		final int x = character.x,
				y = character.y,
				width = character.width,
				height = character.height;
		final int left = x / 50,
				right = (x + width) / 50 - ((x + width) % 50 == 0 ? 1 : 0),
				down = (y + height) / 50;
		if(mapTileTypes[down][left] == EXPLODING)
		{
			mapTileTypes[down][left] = EXPLODED;
			if (died == false){
				SoundPlayer.getInstance().play("charShot");
			}
			character.health -= 10;
			if (character.health <= 0) {
				character.health = 0;
				if (died == false){
					SoundPlayer.getInstance().play("charDie");
					died = true;
					this.add(dieMsg);
				}
			}
		}
		if(mapTileTypes[down][right] == EXPLODING){
			mapTileTypes[down][right] = EXPLODED;
			if (died == false){
				SoundPlayer.getInstance().play("charShot");
			}
			character.health -= 10;
			if (character.health <= 0) {
				character.health = 0;
				if (died == false){
					SoundPlayer.getInstance().play("charDie");
					died = true;
					this.add(dieMsg);
				}
			}
		}	
	}

	private void updateMissiles() {

		ArrayList<Missile> charMissiles = character.getMissiles();

		for (int i = 0; i < charMissiles.size(); i++) {
			Missile currMissile = charMissiles.get(i);
			boolean pos[] = HitBoxManager.checkPosition(mapTileTypes, camOffset, currMissile);
			if (!pos[HitBoxManager.LEFT] || !pos[HitBoxManager.RIGHT])
				currMissile.vis = false;
			if (currMissile.isVisible()) {
				currMissile.camOffset = camOffset;
				currMissile.move();
			} else {
				charMissiles.remove(i);
			}
		}
	}

	private void updateChar() {
		character.move(mapTileTypes, camOffset);
		camOffset = character.getCamOffset();
		health.setText("Health: "+character.health);
	}

	private void updateTiles() {
		for (int i = 0; i < mapTiles.size(); i++) {
			Tile currTile = mapTiles.get(i);
			currTile.camOffset = camOffset;
			currTile.update();
			if (currTile.isVisible()) {

			} else {
				mapTileDrawed[currTile.y / 50][currTile.x / 50] = false;
				mapTiles.remove(i);
			}
		}


		int row,column;
		for(row = 0;row < 12;++row){
			for(column = camOffset / 50 - 2;column < camOffset / 50 + 20;++column){
				if(column >= 0){
					if (mapTileDrawed[row][column] != true){
						mapTileDrawed[row][column] = true;
						if (mapTileTypes[row][column] == MONSTER) {
							Monster tempMonster = new Monster(column * 50,
									row * 50,character.x);
							mapTileTypes[row][column] = NONE;
							monsters.add(tempMonster);
						}
						else if(mapTileTypes[row][column] == STAR || mapTileTypes[row][column] == MUSHROOM){
							PowerUp tempPU = new PowerUp(column * 50,
									row * 50, character.camOffset, mapTileTypes[row][column]);
							mapTileTypes[row][column] = NONE;
							powerUps.add(tempPU);
						}
						else if (mapTileTypes[row][column] != NONE){
							Tile tempTile = new Tile(column * 50,
									row * 50, mapTileTypes[row][column],camOffset);
							mapTiles.add(tempTile);
						}

					}
				}
			}
		}

	}

	private void updateMonsters() {


		for (Monster currMonster : monsters){
			currMonster.camOffset = camOffset;
			if (currMonster.x - character.x < 450){
				currMonster.move(mapTileTypes,character.x);
			}

			ArrayList<Missile> monsterMissiles = currMonster.getMissiles();
			for (int j = 0; j < monsterMissiles.size(); j++) {
				Missile currMissile = monsterMissiles.get(j);
				boolean pos[] = HitBoxManager.checkPosition(mapTileTypes, camOffset, currMissile);
				if (!pos[HitBoxManager.LEFT] || !pos[HitBoxManager.RIGHT])
					currMissile.vis = false;
				if (currMissile.isVisible()) {
					currMissile.camOffset = camOffset;
					currMissile.move();
				} else {
					monsterMissiles.remove(j);
				}
			}
		}
	}

	private void updatePowerUps(){
		for (PowerUp currPowerUp : powerUps){
			currPowerUp.update(character.camOffset);
		}
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			character.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			character.keyPressed(e);
		}
	}


	public static void main(final String s[]) {
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				MainScreen screen = new MainScreen();
			}
		});
	}
}

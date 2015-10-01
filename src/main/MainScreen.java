package main;

import java.awt.*;
import java.awt.event.*;

import javax.sound.sampled.AudioFormat;
import javax.swing.JFrame;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;



public class MainScreen extends JPanel implements ActionListener {

	private final int 
	NORMALTOP = 1,
	NORMAL = 2,
	EXPLODING = 3,
	GAS = 4,
	MONSTER = 5,
	NONE = 0;



    public static final double GRAVITY = 0.13;
	
	private final int INITCHAR_X = 100;
	private final int INITCHAR_Y = 300;
	private final int DELAY = 10;
	private Timer timer;
	private Char character;
	private ArrayList<Monster> monsters;
	private ArrayList<Tile> mapTiles;
	private int mapTileTypes[][] = new int [6][1000];
	private boolean mapTileDrawed[][] = new boolean [6][1000];
	int camOffset = 0;


	
	public MainScreen(){
		initUI();
		//default map
		int column, row;
		for(column = 0;column < 100;++column){
			for(row = 0; row < 6;++row){
				if (row == 4 && column % 10 == 9){
					mapTileTypes[row][column] = MONSTER;
				}
				else if (row == 5){
					mapTileTypes[row][column] = NORMALTOP;
				}
				else if (row == 4 && column % 10 == 5){
					mapTileTypes[row][column] = NORMALTOP;
				}
				else {
					mapTileTypes[row][column] = NONE;
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
		screen.setLocationRelativeTo(null);
		screen.setResizable(false);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setVisible(true);

		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);

		character = new Char(INITCHAR_X, INITCHAR_Y);

		timer = new Timer(DELAY, this);
		timer.start();
		
	}

	private void initTiles(){
		mapTiles = new ArrayList<Tile>();
		monsters = new ArrayList<Monster>();
		int row,column;
		for(row = 0;row < 6;++row){
			for(column = 0;column < 10;++column){
				mapTileDrawed[row][column] = true;
				if (mapTileTypes[row][column] != MONSTER && mapTileTypes[row][column] != NONE){
					Tile tempTile = new Tile(column * 100,
							row * 100, mapTileTypes[row][column],camOffset);
					mapTiles.add(tempTile);
					mapTileDrawed[row][column] = true;
				}
				else if (mapTileTypes[row][column] == MONSTER) {
					Monster tempMonster = new Monster(column * 100,
							row * 100);
					mapTileTypes[row][column] = NONE;
					mapTileDrawed[row][column] = true;
					monsters.add(tempMonster);
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

	}

	public void actionPerformed(ActionEvent e) {
		checkHits();
		
		updateMissiles();
		updateChar();
		updateTiles();
		updateMonsters();
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
					SoundPlayer.getInstance().play("monsDie");
				}
			}
		}

		//monster shoot char
		for (Monster currMonster : monsters){
			ArrayList<Missile> monsterMissiles = currMonster.getMissiles();
			for (Missile currMissile : monsterMissiles) {
				if (HitBoxManager.checkHitBetween(currMissile, character)){
					//Char.shotByMissile();
					//play char get shot sound;
					SoundPlayer.getInstance().play("charShot");
					currMissile.vis = false;
				}
			}
			//char hit monster
			if(HitBoxManager.checkHitBetween(currMonster, character)){
				//Char.die();
				character.vis = false;
				//play char died sound.
				SoundPlayer.getInstance().play("charDie");
			}
		}
		

	}
	
	private void updateMissiles() {

		ArrayList<Missile> charMissiles = character.getMissiles();

		for (int i = 0; i < charMissiles.size(); i++) {
			Missile currMissile = charMissiles.get(i);
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
	}

	private void updateTiles() {
		for (int i = 0; i < mapTiles.size(); i++) {
			Tile currTile = mapTiles.get(i);
			currTile.camOffset = camOffset;
			currTile.update();
			if (currTile.isVisible()) {
			} else {
				mapTileDrawed[currTile.y / 100][currTile.x / 100] = false;
				mapTiles.remove(i);
			}
		}
		int row,column;
		for(row = 0;row < 6;++row){
			for(column = camOffset % 100 - 2;column < camOffset % 100 + 10;++column){
				if(column >= 0){
					if (mapTileDrawed[row][column] != true){
						if (mapTileTypes[row][column] != MONSTER && mapTileTypes[row][column] != NONE){
							Tile tempTile = new Tile(column * 100,
									row * 100, mapTileTypes[row][column],camOffset);
							mapTiles.add(tempTile);
						}
						else if (mapTileTypes[row][column] == MONSTER) {
							Monster tempMonster = new Monster(column * 100,
									row * 100);
							mapTileTypes[row][column] = NONE;
							monsters.add(tempMonster);
						}
						mapTileDrawed[row][column] = true;
					}
				}
			}
		}

	}

	private void updateMonsters() {
		
		
		for (Monster currMonster : monsters){
			currMonster.camOffset = camOffset;
			if (currMonster.x - character.x < 450 || currMonster.activeTimer != 0){
					currMonster.move(mapTileTypes);
			}

			ArrayList<Missile> monsterMissiles = currMonster.getMissiles();
			for (int j = 0; j < monsterMissiles.size(); j++) {
				Missile currMissile = monsterMissiles.get(j);
				if (currMissile.isVisible()) {
					currMissile.camOffset = camOffset;
					currMissile.move();
				} else {
					monsterMissiles.remove(j);
				}
			}
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

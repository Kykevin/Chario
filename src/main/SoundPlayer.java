package main;

import javax.sound.sampled.AudioFormat;

import javax.sound.sampled.*;
import java.io.*;

public class SoundPlayer {
	private static final AudioFormat PLAYBACK_FORMAT =
			new AudioFormat(44100, 16, 1, true, false);

	private File charShoot = new File("sounds/charShoot.wav");
	private File charDie = new File("sounds/charDie.wav");
	private File charShot = new File("sounds/charShot.wav");
	private File monsShoot = new File("sounds/monsShoot.wav");
	private File monsDie = new File("sounds/monsDie.wav");
	private File mush = new File("sounds/mush.wav");
	private File star = new File("sounds/star.wav");
	
	SoundPlayer(){
		charShoot = new File("sounds/charShoot.wav");
		charDie = new File("sounds/charDie.wav");
		charShot = new File("sounds/charShot.wav");
		monsShoot = new File("sounds/monsShoot.wav");
		monsDie = new File("sounds/monsDie.wav");
		mush = new File("sounds/mush.wav");
		star = new File("sounds/star.wav");
	}
	private static SoundPlayer singleton;

	public static SoundPlayer getInstance(){
		if (singleton == null){
			singleton = new SoundPlayer();
		}
		return singleton;
	}

	public void play(String s){
		if(s == "charShoot")
			playSound(charShoot);
		if(s == "charDie")
			playSound(charDie);
		if(s == "charShot")
			playSound(charShot);
		if(s == "monsShoot")
			playSound(monsShoot);
		if(s == "monsDie")
			playSound(monsDie);
		if(s == "mush")
			playSound(mush);
		if(s == "star")
			playSound(star);
	}
	
	private void playSound(File file) 
	{
		try {
	    
		    AudioInputStream stream;
		    AudioFormat format;
		    DataLine.Info info;
		    Clip clip;

		    stream = AudioSystem.getAudioInputStream(file);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		}
		catch (Exception e) {
		    //whatevers
		}
	}
}

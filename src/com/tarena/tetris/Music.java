package com.tarena.tetris;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;


public class Music {
	AudioClip bj1,bj2,move,over,sorce,land,exit,suspend,start;
	public void init(){//背景音乐播放
		try {
			
			bj1 = Applet.newAudioClip(new File("music/bj.wav").toURI().toURL());
			bj2 = Applet.newAudioClip(new File("music/7895.wav").toURI().toURL());
			move = Applet.newAudioClip(new File("music/move.wav").toURI().toURL());
			over = Applet.newAudioClip(new File("music/sb.wav").toURI().toURL());
			sorce = Applet.newAudioClip(new File("music/df.wav").toURI().toURL());
			land = Applet.newAudioClip(new File("music/land.wav").toURI().toURL());	
			exit = Applet.newAudioClip(new File("music/tc.wav").toURI().toURL());
			suspend = Applet.newAudioClip(new File("music/zt.wav").toURI().toURL());
			start = Applet.newAudioClip(new File("music/start.wav").toURI().toURL());
			System.out.println("BGM正常");
			System.out.println("音效正常");
			
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("图形加载正常");
	}
	
	public void playBj1(){
		bj1.loop();//循环播放
	}
	public void stopBj1(){
		bj1.stop();
	}
	
	public void playBj2(){
		bj2.loop();
	}
	public void stopBj2(){
		bj2.stop();
	}
	
	public void playMove(){
		move.play();
	}
	public void stopMove(){
		move.stop();
	}
	
	public void playOver(){
		over.play();
	}
	public void stopOver(){
		over.stop();
	}
	
	public void playSorce(){
		sorce.play();
	}
	public void stopSorce(){
		sorce.stop();
	}
	
	public void playLand(){
		land.play();
	}
	public void stopLand(){
		land.stop();
	}
	
	public void playExit(){
		exit.play();
	}
	public void stopExit(){
		exit.stop();
	}
	
	public void playSuspend(){
		suspend.play();
	}
	public void stopSuspend(){
		suspend.stop();
	}
	
	public void playStart(){
		start.play();
	}
	public void stopStart(){
		start.stop();
	}
}

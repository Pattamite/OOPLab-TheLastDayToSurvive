package com.thelastdaytosurvive.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thelastdaytosurvive.game.LastDayGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "The Last Day to Suvive";
		config.width = 1600;
		config.height = 900;
		new LwjglApplication(new LastDayGame(), config);
	}
}

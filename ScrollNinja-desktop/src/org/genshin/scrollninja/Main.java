package org.genshin.scrollninja;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Scroll Ninja";
		cfg.useGL20 = false;
		cfg.resizable = false;
		
		if(GlobalParam.INSTANCE.FULLSCREEN)
		{
			final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			cfg.width = d.width;
			cfg.height = d.height;
			cfg.fullscreen = true;
		}
		else
		{
			cfg.width = GlobalParam.INSTANCE.CLIENT_WIDTH;
			cfg.height = GlobalParam.INSTANCE.CLIENT_HEIGHT;
		}

		new LwjglApplication(new ScrollNinja(), cfg);
	}
}
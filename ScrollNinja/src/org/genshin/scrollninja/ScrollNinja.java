package org.genshin.scrollninja;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class ScrollNinja extends Game {
	public static Vector2 window;

	// 定数　画面解像度
	/*
	public static final int XGA = 0;	// 4:3
	public static final int HD = 1;		// 16:9
	public static final int SXGA = 2;	// 5:4
	public static final int WUXGA = 3;	// 16:10
	*/

	public static int aspectRatio;					// アスペクト比
	public static boolean FULL_SCREEN = false;		// フルスクリーンかどうか
	public static float scale;						// カメラ、画像の縮小サイズ

	@Override
	public void create() {
		/**
			事前に初期設定が必要ならここで
			セーブデータとか読み込むのも多分ここ
		*/
		// ウインドウサイズ取得
		getWindowSize();
		// アスペクト比計算
		//calculateAspectRatio();
		// TODO 画面比によってスケーリング変えれる？
		scale = 0.1f;

		// TODO リリース時にはメニュー画面から開始する。
		//setScreen(new MainMenu(this));	// メインメニュー読み込み
		setScreen(new GameMain(this, 0));	// ゲームメイン読み込み
	}

	// ウインドウサイズ取得
	public void getWindowSize() {
		window = new Vector2();

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		// ゲームのサイズ
		window.x = 1280;
		window.y = 720;

		// TODO リリース時にはフルスクリーンか選ばせる。もしくはオプションか何かで設定したモードで起動する。
		int message = JOptionPane.NO_OPTION;
		//JOptionPane.showConfirmDialog(null, "フルスクリーンで起動しますか？", "test", JOptionPane.YES_NO_OPTION);
		if(message == JOptionPane.OK_OPTION) {
			// 自分のPCのウインドウサイズ
			Gdx.graphics.setDisplayMode((int)d.getWidth(), (int)d.getHeight(), true);
		}
	}

	// アスペクト比計算
	/*
	public void calculateAspectRatio() {
		// TODO スケールサイズは後で調整
		if (window.x * 3 == window.y * 4) {
			aspectRatio = XGA;
			scale = 0.1f;
		} else if (window.x * 9 == window.y * 16) {
			aspectRatio = HD;
			scale = 0.1f;
		} else if (window.x * 5 == window.y * 4) {
			aspectRatio = SXGA;
			scale = 0.1f;
		} else {//(window.x * 5 == window.y * 8)
			aspectRatio = WUXGA;
			scale = 0.1f;
		}
	}
	*/

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
	}
}
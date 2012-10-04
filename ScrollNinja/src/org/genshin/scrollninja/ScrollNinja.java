package org.genshin.scrollninja;

import com.badlogic.gdx.Game;

public class ScrollNinja extends Game {
	@Override
	public void create() {
		/**
			事前に初期設定が必要ならここで
		*/
		
		/*
		 * 背景読み込み
		 * */
		
		// メインメニュー読み込み
		setScreen(new MainMenu(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
	}
}
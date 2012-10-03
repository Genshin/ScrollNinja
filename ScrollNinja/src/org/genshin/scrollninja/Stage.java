package org.genshin.scrollninja;

import java.util.ArrayList;
import java.util.Timer;

import com.badlogic.gdx.math.Vector2;

public class Stage {
	private ArrayList<EnemyProto> enemies;
	private ArrayList<ItemProto> items;
	private Timer time;
	private String fastTime;
	private Vector2 size;
	private ArrayList<Background> backgroundLayers;

	// コンストラクタ
	public Stage() {
		this.enemies = new ArrayList<EnemyProto>();
		this.items = new ArrayList<ItemProto>();
		this.time = new Timer();
		this.fastTime = "";
		this.size = new Vector2(0, 0);
		this.backgroundLayers = new ArrayList<Background>();
	}

	// 画面スクロール
	public void moveBackground() {

	}

	// プレイヤー出現
	public void spawnPlayer() {

	}

	// 敵出現
	public void popEnemy() {

	}

	// アイテム出現
	public void popItem() {

	}

	// タイムカウント
	public void startTimer() {

	}
}

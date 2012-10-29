package org.genshin.scrollninja;

import java.util.ArrayList;

import org.genshin.scrollninja.object.Player;


import com.badlogic.gdx.math.Vector2;

// TODO playerListの中で、今操作中のプレイヤーは何番目？
public class PlayerManager {
	private static ArrayList<Player> playerList = new ArrayList<Player>();

	private PlayerManager(){}

	/**
	 * 更新
	 */
	public static void Update() {
		for( int i = 0; i < playerList.size(); i ++) {
			playerList.get(i).Update();
		}
	}

	/**
	 * スプライト描画
	 */
	public static void Draw() {
		for( int i = 0; i < playerList.size(); i ++) {
			playerList.get(i).Draw();
		}
	}

	/**
	 * プレイヤー生成
	 * @param position	初期座標
	 */
	public static void CreatePlayer(Vector2 position) {
		if (playerList == null)
			playerList = new ArrayList<Player>();

		// 生成した順に管理番号付与
		Player pPlayer = new Player(playerList.size() + 1, position);
		playerList.add(pPlayer);						// 追加
	}

	/**
	 * プレイヤー参照
	 * @param i		管理番号
	 * @return		プレイヤー
	 */
	public static Player GetPlayer( int i) {
		return playerList.get(i);
	}

	/**
	 * 解放処理
	 */
	public static void dispose() {
		if (playerList != null) {
			for (int i = 0; i < playerList.size(); i++) {
				playerList.get(i).Release();
			}
		}
		playerList = new ArrayList<Player>();
	}
}
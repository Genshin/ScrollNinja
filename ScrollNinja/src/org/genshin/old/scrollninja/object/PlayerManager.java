package org.genshin.old.scrollninja.object;

import java.util.ArrayList;

import org.genshin.old.scrollninja.GameMain;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.object.gui.Cursor;

import com.badlogic.gdx.math.Vector2;

// TODO playerListの中で、今操作中のプレイヤーは何番目？
public class PlayerManager {
	private static ArrayList<PlayerNinja> playerList = new ArrayList<PlayerNinja>();

	private PlayerManager(){}

	/**
	 * 更新
	 * @param deltaTime TODO
	 */
	public static void Update(float deltaTime) {
		for( int i = 0; i < playerList.size(); i ++) {
			playerList.get(i).update(deltaTime);
		}
	}

	/**
	 * スプライト描画
	 */
	public static void Draw() {
		for( int i = 0; i < playerList.size(); i ++) {
			playerList.get(i).render();
		}
	}

	/**
	 * プレイヤー生成
	 * @param position	初期座標
	 */
	public static void CreatePlayer(Vector2 position, Cursor cursor) {
		if (playerList == null)
			playerList = new ArrayList<PlayerNinja>();

		// 生成した順に管理番号付与
		PlayerNinja pPlayer = new PlayerNinja(GameMain.world, position, cursor);
		playerList.add(pPlayer);						// 追加
	}

	/**
	 * プレイヤー参照
	 * @param i		管理番号
	 * @return		プレイヤー
	 */
	public static PlayerNinja GetPlayer( int i) {
		return playerList.get(i);
	}

	/**
	 * 解放処理
	 */
	public static void dispose() {
		if (playerList != null) {
			for (int i = 0; i < playerList.size(); i++) {
				playerList.get(i).dispose();
			}
		}
		playerList = new ArrayList<PlayerNinja>();
	}
}
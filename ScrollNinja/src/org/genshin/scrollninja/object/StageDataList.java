package org.genshin.scrollninja.object;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

// TODO 別ファイルから読み込めるようにしたい
public class StageDataList {
	public StageDataList() {}

	public static StageData lead(int stageNum) {
		StageData data = new StageDataList().new StageData();

		if (stageNum == 0) {
			// ステージ０設定
			data.playerPosition.set(0, 0);
			data.enemyType.add(0);
			data.enemyNum.add(1);
			data.enemyPosition.add(new Vector2(20, 0));
			data.enemyType.add(1);
			data.enemyNum.add(1);
			data.enemyPosition.add(new Vector2(30, 0));
			data.backgroundFileName.add("data/stage/00_far.png");
			data.backgroundSize.add(new Vector2(1024, 1024));
			data.backgroundFileName.add("data/stage/00_main.png");
			data.backgroundSize.add(new Vector2(2048, 1333));
			data.backgroundFileName.add("data/stage/00_near.png");
			data.backgroundSize.add(new Vector2(2048, 256));
			data.backgroundBodyFileName = "data/stage/00.json";
			data.backgroundBodyName = "bgTest";

			// data.musicFileName.add("data/");
			// data.goalPosition.set(0, 0);
		}

		// ステージ１設定
		// data = new StageDataList().new StageData();

		return data;
	}

	public class StageData {
		public Vector2 playerPosition = new Vector2();							// プレイヤーの出現位置
		public ArrayList<Integer> enemyType = new ArrayList<Integer>();			// 出現する敵の種類
		public ArrayList<Integer> enemyNum = new ArrayList<Integer>();			// 各種敵の数
		public ArrayList<Vector2> enemyPosition = new ArrayList<Vector2>();		// 各種敵の出現位置
		public ArrayList<String> backgroundFileName = new ArrayList<String>();	// 使用する背景のファイル名
		public ArrayList<Vector2> backgroundSize = new ArrayList<Vector2>();	// 使用する背景の実際のサイズ
		public String backgroundBodyFileName = "";								// 背景当たり判定ファイル名
		public String backgroundBodyName = "";									// ファイルの中の使用する名前
		public ArrayList<Integer> objectType = new ArrayList<Integer>();		// 出現するオブジェクトの種類
		public ArrayList<Integer> objectNum = new ArrayList<Integer>();			// 各種オブジェクトの数
		public ArrayList<Vector2> objectPosition = new ArrayList<Vector2>();	// 各種オブジェクトの位置
		public ArrayList<String> musicFileName = new ArrayList<String>();		// 使用するBGMのファイル名
		public Vector2 goalPosition = new Vector2();							// ゴールの位置
		// public final ArrayList<Integer> dropItem = new ArrayList<Integer>();	// 落ちているアイテムの種類
		// public final ArrayList<Vector2> itemPosition = ArrayList<Vector2>();	// アイテムの出現位置
		// public final int eventNo;											// 発生イベントNo
	}
}
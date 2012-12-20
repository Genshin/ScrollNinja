package org.genshin.old.scrollninja.object;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

//========================================
// クラス宣言
//========================================
//***** モノステート *****/
public class StageObjectManager {
	private static ArrayList<Integer>		stageObjectList = new ArrayList<Integer>();	// ステージオブジェクトリスト
	private static ArrayList<StageObject>	rockList		= new ArrayList<StageObject>();

	// コンストラクタ
	private StageObjectManager() {
//		stageObjectList = new ArrayList<StageObject>();
	}

	//************************************************************
	// Draw
	// 描画処理まとめ
	//************************************************************
	public static void Draw() {
		for( int i = 0; i < rockList.size(); i ++) {
			rockList.get(i).render();
		}
	}

	//************************************************************
	// CreateStageObject
	// ステージオブジェクト生成
	// 同じ種類が既にある場合は最後に追加
	// まだその種類がリストにない場合は新規で追加
	//************************************************************
	public static int CreateStageObject(int Type, Vector2 pos) {
		// 同じ種類のアイテムがないか探す
		for(int i = 0; i < stageObjectList.size(); i ++ ) {
			// 同じ種類発見
			if( stageObjectList.get(i).equals(Type) ) {

				switch( Type ) {
				case StageObject.ROCK:
					int j = rockList.size() + 1;
					StageObject pStageObject = new StageObject(Type, j, pos);	// 最後の番号を管理番号に
					rockList.add(pStageObject);									// 追加
				break;

				case StageObject.HOUSE:
					break;
				}

				return 0;
			}
		}
		// なかった
		stageObjectList.add(Type);				// この種類の項目を増やす
		StageObject pStageObject = new StageObject(Type, 1, pos);	// 最初の一つ目なので管理番号は１

		switch( Type ) {
		case StageObject.ROCK:
			rockList.add(pStageObject);			// 追加
			break;

		case StageObject.HOUSE:
			break;
		}

		return 0;
	}
	public static int CreateStageObject(int Type, float x, float y) {
		if (stageObjectList == null)
			stageObjectList = new ArrayList<Integer>();
		if (rockList == null)
			rockList = new ArrayList<StageObject>();

		// 同じ種類のアイテムがないか探す
		for(int i = 0; i < stageObjectList.size(); i ++ ) {
			// 同じ種類発見
			if( stageObjectList.get(i).equals(Type) ) {

				switch( Type ) {
				case StageObject.ROCK:
					int j = rockList.size() + 1;
					StageObject pStageObject = new StageObject(Type, j, x, y);	// 最後の番号を管理番号に
					rockList.add(pStageObject);									// 追加
				break;

				case StageObject.HOUSE:
					break;
				}

				return 0;
			}
		}
		// なかった
		stageObjectList.add(Type);				// この種類の項目を増やす
		StageObject pStageObject = new StageObject(Type, 1, x, y);	// 最初の一つ目なので管理番号は１

		switch( Type ) {
		case StageObject.ROCK:
			rockList.add(pStageObject);			// 追加
			break;

		case StageObject.HOUSE:
			break;
		}

		return 0;
	}

	//************************************************************
	// DeleteStageObject
	// ステージオブジェクト削除と同時にソートして管理番号変更
	//************************************************************
	public static void DeleteStageObject(int Type, int Num) {
		for(int i = 0; i < stageObjectList.size(); i ++ ) {

			// 発見
			if( stageObjectList.get(i).equals(Type) ) {
				for( int j = 0; j < rockList.size(); j ++ ) {
					if( rockList.get(j).GetNum() == Num ) {
						rockList.get(j).dispose();
						rockList.remove(j);					// 削除！
					}
				}

				// 削除に合わせて管理番号変更。とりあえず全部
				for( int j = 0; j < rockList.size(); j ++ ) {
					rockList.get(j).SetNum(j + 1);
				}
			}
		}
	}

	/**
	 * 解放処理
	 */
	public static void dispose() {
		stageObjectList = new ArrayList<Integer>();
		if (rockList != null) {
			for (int i = 0; i < rockList.size(); i++) {
				rockList.get(i).dispose();
			}
		}
		rockList = new ArrayList<StageObject>();
	}
}
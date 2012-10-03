package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

//========================================
// クラス宣言
//========================================
public class CStageObjectManager {
	private ArrayList<CStageObject> m_StageObjectList;			// ステージオブジェクトリスト
	
	// コンストラクタ
	CStageObjectManager() {
		m_StageObjectList = new ArrayList<CStageObject>();
	}
	
	// ステージオブジェクトの生成
	public void CreateStageObject(int i) {
		CStageObject pStageObject = new CStageObject(i);		// オブジェクトを生成（&初期化）して
		m_StageObjectList.add(pStageObject);					// リストに追加
	}
	
	// ステージオブジェクトの削除
	public void DeleteStage(int i) {
		m_StageObjectList.remove(i);							// 引数で渡されたオブジェクトを削除
	}
	
	// 参照
	public CStageObject GetStage(int i) {
		return m_StageObjectList.get(i);						// 引数で渡されたオブジェクトのポインタを返す
	}
}

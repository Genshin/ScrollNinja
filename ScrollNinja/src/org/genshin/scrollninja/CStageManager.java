package org.genshin.scrollninja;

//========================================
//インポート
//========================================
import java.util.ArrayList;

//========================================
// クラス宣言
//========================================
public class CStageManager {
	private ArrayList<CStage> m_StageList;				// ステージリスト
	
	// コンストラクタ
	public CStageManager(){
		m_StageList = new ArrayList<CStage>();
	}
	
	// ステージの生成
	public void CreateStage(int i) {
		CStage pStage = new CStage(i);			// オブジェクトを生成（&初期化）して
		m_StageList.add(pStage);				// リストに追加
	}
	
	// ステージの削除
	public void DeleteStage(int i) {
		m_StageList.remove(i);					// 引数で渡されたオブジェクトを削除
	}
	
	// 参照
	public CStage GetStage(int i) {
		return m_StageList.get(i);				// 引数で渡されたオブジェクトのポインタを返す
	}
}

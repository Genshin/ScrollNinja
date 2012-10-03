package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;
import java.util.Iterator;

public class CEnemyManager {
	// 変数宣言
	private ArrayList<CEnemy> m_EnemyList;		// 敵リスト
	
	// コンストラクタ
	CEnemyManager() {
		m_EnemyList = new ArrayList<CEnemy>();
	}
	
	// 敵の生成
	public int CreateEnemy(String Name) {
		if( m_EnemyList.contains(Name) ) {		// 既にその名前が作られている時
			return -1;		// エラー処理
		}
		
		CEnemy pEnemy = new CEnemy(Name);		// オブジェクトを生成（&初期化）して
		m_EnemyList.add(pEnemy);				// リストに追加
		
		return 1;
	}
	
	// 敵の削除 
	public void DeleteEnemy(String Name) {
		m_EnemyList.remove(m_EnemyList.indexOf(Name));		// 引数で渡されたオブジェクトを削除
	}
	
	// 参照
	public CEnemy GetEnemy(int i) {
		return m_EnemyList.get(i);				// 引数で渡されたオブジェクトのポインタを返す
	}
}

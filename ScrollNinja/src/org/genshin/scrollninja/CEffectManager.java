package org.genshin.scrollninja;

//========================================
//インポート
//========================================
import java.util.ArrayList;

public class CEffectManager {
	// 変数宣言
	private ArrayList<Effect> m_EffectList;
	
	// コンストラクタ
	CEffectManager(){
		m_EffectList = new ArrayList<Effect>();
	}
	
	// 敵の生成 
	public void CreateEnemy() {
		Effect pEffect = new Effect();			// オブジェクトを生成（&初期化）して
		m_EffectList.add(pEffect);					// リストに追加
	}
	
	// 敵の削除
	public void DeleteEnemy(int i) {
		m_EffectList.remove(i);					// 引数で渡されたオブジェクトを削除
	}
	
	// 参照
	public Effect GetEnemy(int i) {
		return m_EffectList.get(i);				// 引数で渡されたオブジェクトのポインタを返す
	}
	
}

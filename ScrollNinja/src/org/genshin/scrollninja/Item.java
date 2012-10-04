
package org.genshin.scrollninja;

public class Item {
	
	public String name;
	
	// コンストラクタ
	public Item() {
		
	}
	
	// アイテム効果
	public void ItemEffect() {
	
		// アイテムをプレイヤーに反映
	}
	// アイテムゲット
	public void GetItem(boolean enemyDead) {
		
		// 敵が死んだらアイテムゲット
		if(enemyDead) {
			
			ItemEffect();		//効果発動
		}
	}
	
}

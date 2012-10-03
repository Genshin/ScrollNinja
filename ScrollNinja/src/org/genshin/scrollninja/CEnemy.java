package org.genshin.scrollninja;

import com.badlogic.gdx.math.Vector2;

public class CEnemy extends CCharacterBase {
	// 変数宣言
	private String name;
	
	// コンストラクタ 
	CEnemy(String Name) {
		hp			= 100;
		attackNum	= 0;
		position	= new Vector2(0,0);
		speed		= 0;
	}
}

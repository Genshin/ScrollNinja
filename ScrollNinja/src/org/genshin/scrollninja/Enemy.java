package org.genshin.scrollninja;

import com.badlogic.gdx.math.Vector2;

public class Enemy extends CharacterBase {
	// 変数宣言
	private String name;
	
	// コンストラクタ 
	Enemy(String Name) {
		hp			= 100;
		attackNum	= 0;
		position	= new Vector2(0,0);
		speed		= 0;
		name		= new String(Name);
	}
	
	// 参照
	public Enemy GetEnemy() {
		return this;
	}
}

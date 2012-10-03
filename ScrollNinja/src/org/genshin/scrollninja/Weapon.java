//******************************
//	Weapon.java
//******************************

package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Weapon {

	private Sprite	sprite;		// スプライト
	private Vector2 	position;		// 武器座標
	private float 	attackNum;		// 武器威力
	private int 		weaponLevel;	// 武器レベル
	private Boolean 	use;			// 使用フラグ
	private Body		body;			// ボディ


	//コンストラクタ
	public Weapon(int i) {
		this.position    = new Vector2(0,0);
		this.attackNum   = 0;
		this.weaponLevel = 0;
		this.use         = true;

	}

	//武器座標ゲット
	public Vector2 GetWeaponPos() {
		return this.position;
	}

	//武器威力ゲット
	public float GetAttackNum() {
		return this.attackNum;
	}


	//武器レベルゲット
	public int GetWeaponLv() {
		return this.GetWeaponLv();
	}


}







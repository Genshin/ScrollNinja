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

	private String	name;		// 名前
	private Sprite	sprite;		// スプライト
	private Vector2 	position;		// 武器座標
	private float 	attackNum;		// 武器威力
	private int 		weaponLevel;	// 武器レベル
	private Boolean 	use;			// 使用フラグ
	private Body		body;			// ボディ
	
	
	//private static final int AADD = 10;

	//コンストラクタ
	public Weapon(String Name) {
		name			 = new String(Name);
		this.position    = new Vector2(0,0);
		this.attackNum   = 0;
		this.weaponLevel = 0;
		this.use         = true;

	}

	//武器座標ゲット
	public Vector2 GetWeaponPos() {
		return position;
	}

	//武器威力ゲット
	public float GetAttackNum() {
		return attackNum;
	}


	//武器レベルゲット
	public int GetWeaponLv() {
		return GetWeaponLv();
	}
	
	// フラグゲット
	public boolean GetUseFlag() {
		return use;
	}
	
	// 武器モーション
	public void WeaponMove() {
		
		//------------------
		// 武器動作
		//------------------
	}
	
	// 武器のレベルアップ(仮)
	public int WeaponLvUp(int chakra) {	
		
		return this.weaponLevel;
	}
	
	// 武器のボックスあたり判定(仮)
	public int HitCheck() {
		return -1;
	}

	

}

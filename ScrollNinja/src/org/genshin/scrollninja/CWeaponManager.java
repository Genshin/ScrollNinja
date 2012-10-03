package org.genshin.scrollninja;

import java.util.ArrayList;

public class CWeaponManager {
	// 変数宣言
	private ArrayList<Weapon> m_WeaponList;

	// コンストラクタ
	CWeaponManager(){
		m_WeaponList = new ArrayList<Weapon>();
	}

	// 武器の生成
	public void CreateWeapon(int i) {
		Weapon pWeapon = new Weapon(i);			// オブジェクトを生成（&初期化）して
		m_WeaponList.add(pWeapon);				// リストに追加
	}

	// 武器の削除
	public void DeleteWeapon(int i) {
		m_WeaponList.remove(i);					// 引数で渡されたオブジェクトを削除
	}

	// 参照
	public Weapon GetWeapon(int i) {
		return m_WeaponList.get(i);				// 引数で渡されたオブジェクトのポインタを返す
	}
}

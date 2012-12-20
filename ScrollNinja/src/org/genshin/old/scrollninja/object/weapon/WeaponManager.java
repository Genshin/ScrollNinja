package org.genshin.old.scrollninja.object.weapon;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import org.genshin.scrollninja.object.character.AbstractCharacter;

//========================================
// クラス宣言
//========================================
//***** モノステート *****/
public class WeaponManager {
	// 定数宣言
	public static final int KATANA		= 0;
	public static final int SHURIKEN	= 1;

	// 変数宣言
	public static ArrayList<Katana> katanaList		= new ArrayList<Katana>();
//	public static ArrayList<>

	/**
	 * コンストラクタ
	 */
	private WeaponManager(){}

	public static void Update(float deltaTime) {
		for( int i = 0; i < katanaList.size(); i ++ ) {
			katanaList.get(i).update(deltaTime);
		}
	}

	/**
	 * 武器生成
	 * @param owner			使用者
	 * @param type			種類
	 * @param lv			武器レベル
	 */
	public static AbstractWeapon CreateWeapon(AbstractCharacter owner, int type, int lv) {
		switch(type) {
		case KATANA:
			Katana pKatana = new Katana(owner, katanaList.size() + 1, lv);
			katanaList.add(pKatana);
			return pKatana;
		case SHURIKEN:
			break;
		}

		return null;
	}

	/**
	 * 解放処理
	 */
	public static void dispose() {
		katanaList = new ArrayList<Katana>();
	}
}
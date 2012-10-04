package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

//========================================
//クラス宣言
//========================================
//***** シングルトン *****/
public class WeaponManager {
	
	private static final WeaponManager Instance = new WeaponManager();			// このクラスの唯一のインスタンスを作ります
	
	// インスタンスを返す
	public static WeaponManager GetInstace() {
		return Instance;
	}
	
	// 変数宣言
	private ArrayList<Weapon> weaponList;

	// コンストラクタ
	private WeaponManager(){
		weaponList = new ArrayList<Weapon>();
	}

	// 武器の生成
	public int CreateWeapon(String Name) {
		if( weaponList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}
		
		Weapon pWeapon = new Weapon(Name);			// オブジェクトを生成（&初期化）して
		weaponList.add(pWeapon);					// リストに追加
		
		return 1;
	}

	// 武器の削除
	public int DeleteWeapon(String Name) {
		if( !weaponList.contains(Name) ) {		// 名前が見つからなかった場合はエラー
			return -1;		// エラー処理
		}
		
		weaponList.remove(weaponList.indexOf(Name));		// 引数で渡されたオブジェクトを削除
		return 1;
	}

	// 参照
	public Weapon GetWeapon(String Name) {
		return weaponList.get(weaponList.indexOf(Name));	// 引数で渡されたオブジェクトのポインタを返す
	}
}

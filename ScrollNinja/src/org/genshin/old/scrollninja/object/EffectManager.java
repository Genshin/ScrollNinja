package org.genshin.old.scrollninja.object;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;


//========================================
// クラス宣言
//========================================
// ***** モノステート *****/
public class EffectManager {
	// 変数宣言
	private static ArrayList<Effect> effectList			= new ArrayList<Effect>();
	// TODO とりあえず。他にいい方法があればそっちで…
	// 今だけパブリック。
	public static ArrayList<Effect> enemyEffectList	= new ArrayList<Effect>();

	// コンストラクタ
	private EffectManager(){}

	/**
	 * 描画
	 */
	public static void Draw() {
		for( int i = 0; i < effectList.size(); i ++ ) {
			effectList.get(i).render();
		}
		for( int i = 0; i < enemyEffectList.size(); i ++ ) {
			enemyEffectList.get(i).render();
		}
	}

	/**
	 * 作成
	 * @param Type	種類
	 */
	public static void CreateEffect(int Type, AbstractCharacter owner) {
 		Effect pEffect = new Effect(Type, owner);	// オブジェクトを生成（&初期化）して
 		if (owner.getClass().equals(PlayerNinja.class))
 			effectList.add(pEffect);				// リストに追加
 		else
 			enemyEffectList.add(pEffect);
	}

	/**
	 * 削除
	 * @param Type	種類
	 */
	public static void DeleteEffect(int Type, AbstractCharacter owner) {
		if (owner.getClass().equals(PlayerNinja.class))
			effectList.remove(effectList.indexOf(Type));		// 引数で渡されたオブジェクトを削除
		else
			enemyEffectList.remove(effectList.indexOf(Type));
	}

	/**
	 * 参照
	 * @param Type		種類
	 * @return エフェクト
	 */
	public static Effect GetEffect(int Type, AbstractCharacter owner) {
		if (owner.getClass().equals(PlayerNinja.class)) {
			for(int i = 0; i < effectList.size(); i ++) {
				if( effectList.get(i).GetType() == Type ) {
					return effectList.get(i);				// 引数で渡されたオブジェクトのポインタを返す
				}
			}
		} else {
			for(int i = 0; i < enemyEffectList.size(); i ++) {
				if( enemyEffectList.get(i).GetType() == Type ) {
					return enemyEffectList.get(i);			// 引数で渡されたオブジェクトのポインタを返す
				}
			}
		}
		return null;
	}

	/**
	 * 参照
	 * @param current	現在のエフェクト
	 * @return エフェクト
	 */
	public static Effect GetEffect(Effect current, AbstractCharacter owner) {
		if (owner.getClass().equals(PlayerNinja.class)) {
			for( int i = 0; i < effectList.size(); i ++ ) {
				if( effectList.get(i).equals(current) )
					return effectList.get(i);
			}
		} else {
			for( int i = 0; i < enemyEffectList.size(); i ++ ) {
				if( enemyEffectList.get(i).equals(current) )
					return enemyEffectList.get(i);
			}
		}
		return null;
	}

	/**
	 * 参照
	 * @param i
	 * @return エフェクト
	 */
	public static Effect GetEffectForLoop(int i, AbstractCharacter owner) {
		if (owner.getClass().equals(PlayerNinja.class))
			return effectList.get(i);
		else
			return enemyEffectList.get(i);
	}

	/**
	 * サイズ参照
	 * @return エフェクトリストサイズ
	 */
	public static int GetListSize(AbstractCharacter owner) {
		if (owner.getClass().equals(PlayerNinja.class))
			return effectList.size();
		else
			return enemyEffectList.size();
	}

	/**
	 * 解放処理
	 */
	public static void dispose() {
		if (effectList != null) {
			for (int i = 0; i < effectList.size(); i++) {
				effectList.get(i).dispose();
			}
		}
		effectList = new ArrayList<Effect>();
		if (enemyEffectList != null) {
			for (int i = 0; i < enemyEffectList.size(); i++) {
				enemyEffectList.get(i).dispose();
			}
		}
		enemyEffectList = new ArrayList<Effect>();
	}
}
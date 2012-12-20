package org.genshin.old.scrollninja.object.weapon;



import org.genshin.old.scrollninja.object.Effect;
import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.character.AbstractCharacter;

import com.badlogic.gdx.math.Vector2;

/**
 * 武器の基本クラス。
 * @author	インターン生
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractWeapon extends AbstractCollisionObject
{
	/**
	 * コンストラクタ
	 */
	public AbstractWeapon()
	{
	}
	
	
	
	
	
	// TODO 以下、そのうち要るもの要らないもの振り分ける。
	// 迷ってるもの
	protected int			level;			// レベル
	protected int 			stateTime;		// 武器とエフェクトは同期する？のでここに

	// おそらく必要なもの
	protected int			attackNum;		// 攻撃力
	protected int			number;			// 管理番号
	protected Vector2		position;		// 座標
	protected AbstractCharacter	owner;			// 使用者
	protected Effect		myEffect;
	protected boolean		use;			// 使用フラグ

	/**
	 * 使用・停止
	 * @param flag		使用フラグ
	 */
	public void SetUseFlag( boolean flag ) { use = flag; }

	/**
	 * とりあえず
	 * @return
	 */
	public boolean GetUseFlag() { return use; }
	public Effect GetMyEffect(){ return myEffect; }
	public int GetStateTime(){ return stateTime; }
	public float GetAttackNum() { return attackNum; }
	public AbstractCharacter GetOwner() { return owner; }
}
package org.genshin.scrollninja.object.character;


import org.genshin.scrollninja.object.AbstractDynamicObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * キャラクタの基本クラス
 * @author インターン生
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractCharacter extends AbstractDynamicObject
{
	/**
	 * 古いソースとの整合性を保つためのコンストラクタ。
	 * FIXME 最終的には消し去る。
	 */
	@Deprecated
	public AbstractCharacter()
	{
		/* 何もしない */
	}
	
	/**
	 * コンストラクタ 
	 * @param world			所属するWorldオブジェクト
	 * @param position		初期座標
	 */
	public AbstractCharacter(World world, Vector2 position)
	{
		super(world);
	}
	
	// FIXME 以下　たぶん　ぜんぶ　いらない。
	// 変数宣言
	// TODO MAXHPは増加するからプレイヤーデータ（レベル）読み込みで変更させないと
	public int	MAX_HP	= 100;
	public int 	hp		= MAX_HP;						// HP

	// いずれ消す変数
	public float	speed = 0;					// 素早さ
	public Vector2 	position = new Vector2();				// 座標
	public int		direction = 1;				// 向き
}
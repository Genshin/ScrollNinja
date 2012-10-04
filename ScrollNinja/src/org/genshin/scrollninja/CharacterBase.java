package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

//========================================
// クラス宣言 
//========================================
public class CharacterBase extends ObJectBase {
	// 定数宣言
	protected final static int		MAX_HP		=		(100);
	
	// 変数宣言
	protected int 		hp;						// HP							
	protected int 		attackNum;				// 攻撃力
	protected int 		speed;					// 素早さ
	protected int 		textureNum;				// テクスチャ番号
	protected Vector2 	position;				// 座標
	
	// コンストラクタ
	public CharacterBase() {
		hp				= 0;
		attackNum		= 0;
		speed			= 0;
		textureNum		= 0;
		position		= new Vector2(0,0);
	}
	
}
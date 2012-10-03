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
public class CStageObject {
	private int			m_StageObjectNum;		// ステージオブジェクト番号
	private Sprite		m_Sprite;				// スプライト
	private Vector2		m_Pos;					// 座標
	private Body		m_Box;					// 当たり判定用BOX
	
	// コンストラクタ
	CStageObject(int i) {
		m_StageObjectNum	= i;
		m_Pos				= new Vector2(0,0);
	}
}

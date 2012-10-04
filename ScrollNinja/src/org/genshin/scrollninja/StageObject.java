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
public class StageObject {
	private String		name;			// 名前
	private Vector2		position;		// 座標
	
	// コンストラクタ
	StageObject(String Name) {
		name		= new String(Name);
		position	= new Vector2(0,0);
	}
	
	// 参照
	public StageObject GetStageObject() {
		return this;
	}
}

package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

//========================================
// クラス宣言
//========================================
public class ObJectBase {
	protected Sprite 	sprite;			// スプライト
	protected Body 		body;			// 当たり判定用BOX
	protected Fixture 	sensor;			// センサー
	
	// コンストラクタ
	ObJectBase(){
		sprite = new Sprite();
	}
}

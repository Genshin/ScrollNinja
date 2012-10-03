package org.genshin.scrollninja;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class CStage {
	private int		m_StageNum;				// ステージ番号
	
	// コンストラクタ
	public CStage(int i){
		m_StageNum = i;						// オブジェクト化と同時にステージ番号の代入
	}
	
	// 参照
	public int GetStageNum() {
		return m_StageNum;
	}
}

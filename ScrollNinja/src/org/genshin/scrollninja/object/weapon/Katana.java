package org.genshin.scrollninja.object.weapon;


import org.genshin.scrollninja.EffectManager;
import org.genshin.scrollninja.object.CharacterBase;
import org.genshin.scrollninja.object.Effect;
import org.genshin.scrollninja.object.WeaponBase;

import com.badlogic.gdx.math.Vector2;

public class Katana extends WeaponBase {

	/**
	 * コンストラクタ
	 * @param i		管理番号
	 */
	public Katana(CharacterBase chara, int i, int lv) {
		owner		= chara;					// 使用者
		number		= i;						// 管理番号
		level		= lv;						// レベル TODO
		attackNum	= (level * 10);				// 攻撃力（てきとー）
		position 	= new Vector2(0.0f, 0.0f);
		use			= false;

		switch (level) {
		case 1:
			EffectManager.CreateEffect(Effect.FIRE_1, owner);
			break;
		case 2:
			EffectManager.CreateEffect(Effect.FIRE_2, owner);
			break;
		case 3:
			EffectManager.CreateEffect(Effect.FIRE_3, owner);
			break;
		}
	}

	/**
	 * 更新
	 */
	public void Update() {
		if( !use ) stateTime = 0; else stateTime ++;
		if( stateTime % 18 == 0 ) use = false;

		switch( level ) {
		case 1:
			EffectManager.GetEffect(Effect.FIRE_1, owner).Update(use);
			break;
		case 2:
			EffectManager.GetEffect(Effect.FIRE_2, owner).Update(use);
			break;
		case 3:
			EffectManager.GetEffect(Effect.FIRE_3, owner).Update(use);
			break;
		}
	}
}
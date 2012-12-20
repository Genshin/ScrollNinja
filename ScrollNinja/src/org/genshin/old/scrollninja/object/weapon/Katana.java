package org.genshin.old.scrollninja.object.weapon;


import org.genshin.old.scrollninja.object.Effect;
import org.genshin.old.scrollninja.object.EffectManager;
import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.character.AbstractCharacter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;

public class Katana extends AbstractWeapon {

	/**
	 * コンストラクタ
	 * @param i		管理番号
	 */
	public Katana(AbstractCharacter chara, int i, int lv) {
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
	public void update(float deltaTime) {
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

	@Override
	public void dispatchCollision(AbstractCollisionObject object, Contact contact)
	{
		object.dispatchCollision(this, contact);
	}
}
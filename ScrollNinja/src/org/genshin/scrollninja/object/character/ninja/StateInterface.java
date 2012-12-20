package org.genshin.scrollninja.object.character.ninja;

import com.badlogic.gdx.physics.box2d.Contact;

interface StateInterface
{
	/**
	 * 状態を更新し、必要であれば次の状態に遷移する。
	 * @param me			自身を示す忍者オブジェクト
	 * @param deltaTime		経過時間（秒）
	 * @return				次の状態。状態を変更しない場合はthisを返す。
	 */
	StateInterface update(PlayerNinja me, float deltaTime);
	
	/**
	 * 地形と衝突した。
	 * @param me		自身を示す忍者オブジェクト
	 * @param contact	衝突情報
	 */
	void collisionTerrain(PlayerNinja me, Contact contact);
}

package org.genshin.old.scrollninja.object;

import java.util.List;

import org.genshin.old.scrollninja.GameMain;
import org.genshin.scrollninja.object.AbstractCollisionObject;

import com.badlogic.gdx.physics.box2d.Contact;

public class CollisionDetector {
	/**
	 * コンストラクタ
	 */
	private CollisionDetector() {}

	/**
	 * 当たり判定検出
	 */
	public static void HitTest(){

		List<Contact> contactList = GameMain.world.getContactList();

		for(int i = 0; i < contactList.size(); i++) {
			Contact contact = contactList.get(i);

			if( contact.isTouching() ) {
				AbstractCollisionObject a = (AbstractCollisionObject)contact.getFixtureA().getBody().getUserData();
				AbstractCollisionObject b = (AbstractCollisionObject)contact.getFixtureB().getBody().getUserData();
				if( a != null && b != null ) {		//TODO バグ起こさない為に一応
					a.dispatchCollision(b, contact);
					b.dispatchCollision(a, contact);
				}
			}
		}
	}
}
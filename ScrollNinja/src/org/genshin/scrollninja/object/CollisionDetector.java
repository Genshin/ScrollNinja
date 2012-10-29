package org.genshin.scrollninja.object;

import java.util.List;

import org.genshin.scrollninja.GameMain;

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
				ObjectBase a = (ObjectBase)contact.getFixtureA().getUserData();
				ObjectBase b = (ObjectBase)contact.getFixtureB().getUserData();
				if( a != null && b != null ) {		//TODO バグ起こさない為に一応
					a.collisionDispatch(b, contact);
					b.collisionDispatch(a, contact);
				}
			}
		}
	}
}
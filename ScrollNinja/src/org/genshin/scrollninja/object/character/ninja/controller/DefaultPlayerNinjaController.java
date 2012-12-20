/**
 * 
 */
package org.genshin.scrollninja.object.character.ninja.controller;

import org.genshin.scrollninja.object.ObjectInterface;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

/**
 * プレイヤーが操作する忍者の操作状態を管理するオブジェクトのデフォルト設定。
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class DefaultPlayerNinjaController extends AbstractPlayerNinjaController
{
	/**
	 * コンストラクタ
	 * @param ninja		忍者オブジェクト（忍者の向き計算用）
	 * @param cursor	マウスカーソルオブジェクト（忍者の向き計算用）
	 */
	public DefaultPlayerNinjaController(ObjectInterface ninja, ObjectInterface cursor)
	{
		this.ninja = ninja;
		this.cursor = cursor;
	}
	
	@Override
	protected final void initialize()
	{
		registKey(InputType.LEFT, Keys.A);
		registKey(InputType.RIGHT, Keys.D);
		registKey(InputType.DASH, Keys.SHIFT_LEFT);
		registKey(InputType.JUMP, Keys.W);
		registMouse(InputType.ATTACK, Buttons.LEFT);
		registMouse(InputType.KAGINAWA, Buttons.RIGHT);
		registKey(InputType.KAGINAWA_RELEASE, Keys.S);
	}
	

	@Override
	protected final void updateDirection()
	{
		setDirection(
			cursor.getPositionX() - ninja.getPositionX(),
			cursor.getPositionY() - ninja.getPositionY()
		);
	}


	/** 忍者オブジェクト（鉤縄の向き計算用） */
	private final ObjectInterface ninja;
	
	/** マウスカーソルオブジェクト（鉤縄の向き計算用） */
	private final ObjectInterface cursor;
}

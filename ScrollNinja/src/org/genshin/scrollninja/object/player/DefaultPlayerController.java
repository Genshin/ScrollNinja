/**
 * 
 */
package org.genshin.scrollninja.object.player;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

/**
 * デフォルト状態に設定されたプレイヤーコントローラー。<br>
 * プレイヤーと入力状態とを仲介する。
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class DefaultPlayerController extends PlayerControllerBase
{
	@Override
	protected void initialize()
	{
		registKey(ACTION.LEFT, Keys.A);
		registKey(ACTION.RIGHT, Keys.D);
		registKey(ACTION.DASH, Keys.SHIFT_LEFT);
		registKey(ACTION.JUMP, Keys.W);
		registMouse(ACTION.ATTACK, Buttons.LEFT);
		registMouse(ACTION.KAGINAWA, Buttons.RIGHT);
	}
}

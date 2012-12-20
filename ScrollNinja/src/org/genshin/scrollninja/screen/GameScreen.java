package org.genshin.scrollninja.screen;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.object.gui.Cursor;

import com.badlogic.gdx.math.Vector2;

/**
 * ゲームスクリーン
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class GameScreen extends AbstractDebugScreen		// FIXME リリース時はAbstractScreenを継承する。
{
	/**
	 * コンストラクタ
	 */
	public GameScreen()
	{
		//---- 忍者を生成する。
		new PlayerNinja(getWorld(), Vector2.Zero, getCursor());
	}

	@Override
	protected Cursor createCursor()
	{
		return new Cursor(getCamera(), 2.0f * GlobalParam.INSTANCE.WORLD_SCALE);
	}
}

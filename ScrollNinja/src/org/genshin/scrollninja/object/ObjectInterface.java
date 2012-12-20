package org.genshin.scrollninja.object;

import org.genshin.engine.system.Renderable;
import org.genshin.engine.system.Updatable;

/**
 * オブジェクトのインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface ObjectInterface extends Updatable, Renderable
{
	/**
	 * 解放すべきものを全て解放する。
	 */
	public abstract void dispose();

	/**
	 * X座標を取得する。
	 * @return		X座標
	 */
	public abstract float getPositionX();

	/**
	 * Y座標を取得する。
	 * @return		Y座標
	 */
	public abstract float getPositionY();
}

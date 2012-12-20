package org.genshin.engine.system;

/**
 * 更新処理を持つインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface Updatable
{
	/**
	 * 更新処理を実行する。
	 * @param deltaTime TODO
	 * @param deltaTime		経過時間
	 */
	public void update(float deltaTime);
}

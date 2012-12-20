package org.genshin.engine.manager;

import org.genshin.engine.system.Updatable;

/**
 * 更新オブジェクトを一括管理する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public final class UpdatableManager extends AbstractProcessManager<Updatable>
{
	/**
	 * 処理優先度順に更新処理を実行する。
	 * @param deltaTime		経過時間
	 */
	public void update(float deltaTime)
	{
		this.deltaTime = deltaTime;
		process();
	}

	@Override
	protected void processOne(Updatable object)
	{
		object.update(deltaTime);
	}
	
	/** 経過時間 */
	float deltaTime;
}

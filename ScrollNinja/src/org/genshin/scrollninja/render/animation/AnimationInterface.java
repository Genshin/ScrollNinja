package org.genshin.scrollninja.render.animation;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * アニメーションを管理するインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface AnimationInterface
{
	/**
	 * アニメーションを適用する。
	 * @param sprite			アニメーションを適用するスプライト
	 * @param animationTime		アニメーションの時間
	 */
	public void apply(Sprite sprite, float animationTime);
	
	/**
	 * アニメーションがループするか調べる。
	 * @return		ループする場合はtrue
	 */
	public boolean isLooping();
	
	/**
	 * アニメーションが終了しているか調べる。
	 * @param animationTime		アニメーションの時間
	 * @return		終了している場合はtrue
	 */
	public boolean isFinished(float animationTime);
}

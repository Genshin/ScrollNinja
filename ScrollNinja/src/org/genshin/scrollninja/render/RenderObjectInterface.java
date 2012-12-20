package org.genshin.scrollninja.render;

import org.genshin.engine.system.Renderable;
import org.genshin.engine.system.Updatable;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * 描画オブジェクトのインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface RenderObjectInterface extends Updatable, Renderable
{
	/**
	 * 描画オブジェクトを移動させる。
	 * @param x		X座標の移動量
	 * @param y		Y座標の移動量
	 */
	public void translate(float x, float y);
	
	/**
	 * 描画オブジェクトを回転させる。
	 * @param degrees		回転量（度）
	 */
	public void rotate(float degrees);
	
	/**
	 * アニメーションを設定する。
	 * @param name		アニメーションの名前
	 */
	public void setAnimation(String name);

	/**
	 * アニメーションの時間を設定する。
	 * @param time		アニメーションの時間（秒）
	 */
	public void setAnimationTime(float time);
	
	/**
	 * 座標を設定する。
	 * @param x		X座標
	 * @param y		Y座標
	 */
	public void setPosition(float x, float y);
	
	/**
	 * 回転を設定する。
	 * @param degrees		角度（単位：度）
	 */
	public void setRotation(float degrees);

	/**
	 * アニメーションを持つか調べる。
	 * @return		アニメーションを持つ場合はtrue
	 */
	public boolean hasAnimation();

	/**
	 * アニメーションがループするか調べる。
	 * @return		ループする場合はtrue
	 */
	public boolean isAnimationLooping();

	/**
	 * アニメーションが終了しているか調べる。
	 * @return		終了している場合はtrue
	 */
	public boolean isAnimationFinished();

	/**
	 * 描画スプライトを取得する。
	 * @return		描画スプライト
	 */
	public abstract Sprite getSprite();
	
	/**
	 * アニメーションの時間を取得する。
	 * @return		アニメーションの時間
	 */
	public float getAnimationTime();
	
	/**
	 * X座標を取得する。
	 * @return		座標
	 */
	public float getPositionX();
	
	/**
	 * Y座標を取得する。
	 * @return		座標
	 */
	public float getPositionY();
	
	/**
	 * 角度を取得する。
	 * @return		角度（単位：度）
	 */
	public float getRotation();
}

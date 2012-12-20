/**
 * 
 */
package org.genshin.scrollninja.object.character.ninja.controller;

import com.badlogic.gdx.math.Vector2;

/**
 * 忍者の操作状態を管理するオブジェクトのインタフェース。
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public interface NinjaControllerInterface
{
	/**
	 * 	忍者の操作状態を更新する。
	 */
	void update();
	
	/**
	 * 移動の操作状態を取得する。
	 * @return		左方向への移動操作がある場合は-1.0f、右方向への移動操作がある場合は1.0f、移動操作がない場合は0.0f
	 */
	float getMovePower();
	
	/**
	 * 忍者の向きを取得する。（スプライトの左右反転の判定、攻撃・鉤縄の射出方向などに使用する）
	 * @return		忍者の向き
	 */
	Vector2 getDirection();
	
	/**
	 * 移動し始める時の操作状態を取得する。
	 * @return		移動し始める時の操作がある場合はtrue
	 */
	boolean isMoveStart();
	
	/**
	 * ダッシュの操作状態を取得する。
	 * @return　ダッシュの操作がある場合はtrue
	 */
	boolean isDash();
	
	/**
	 * ダッシュの開始操作状態を取得する。
	 * @return	ダッシュの開始操作がある場合はtrue
	 */
	boolean isDashStart();
	
	/**
	 * ジャンプの操作状態を取得する。
	 * @return	ジャンプの操作がある場合はtrue
	 */
	boolean isJump();
	
	/**
	 * 空中でのジャンプの操作状態を取得する。
	 * @return	空中でのジャンプの操作がある場合はtrue
	 */
	boolean isAerialJump();
	
	/**
	 * 天井への吸着をやめる操作状態を取得する。
	 * @return		天井への吸着をやめる操作がある場合はtrue
	 */
	boolean isLeaveSnapCeiling();
	
	/**
	 * 攻撃の操作状態を取得する。
	 * @return	攻撃の操作がある場合はtrue
	 */
	boolean isAttack();
	
	/**
	 * 鉤縄を伸ばす操作の状態を取得する。
	 * @return	鉤縄を伸ばす操作がある場合はtrue
	 */
	boolean isKaginawaSlack();
	
	/**
	 * 鉤縄を縮める操作の状態を取得する。
	 * @return	鉤縄を縮める操作がある場合はtrue
	 */
	boolean isKaginawaShrink();
	
	/**
	 * 鉤縄にぶら下がる操作の状態を取得する。
	 * @return	鉤縄にぶら下がる操作がある場合はtrue
	 */
	boolean isKaginawaHang();
	
	/**
	 * 鉤縄を離す操作の状態を取得する。
	 * @return	鉤縄を離す操作がある場合はtrue
	 */
	boolean isKaginawaRelease();	
}

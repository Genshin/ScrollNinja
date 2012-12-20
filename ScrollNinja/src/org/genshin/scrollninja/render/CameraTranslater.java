package org.genshin.scrollninja.render;

import java.util.ArrayList;

import org.genshin.engine.system.Updatable;
import org.genshin.scrollninja.object.ObjectInterface;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;

/**
 * カメラを動かすオブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class CameraTranslater implements Updatable
{
	/**
	 * コンストラクタ
	 * @param camera	動かすカメラオブジェクト
	 */
	public CameraTranslater(Camera camera)
	{
		this.camera = camera;
	}
	
	@Override
	public void update(float deltaTime)
	{
		//---- 目標座標を計算する。
		float targetX = 0.0f;
		float targetY = 0.0f;
		for(ObjectInterface object : targetObjects)
		{
			targetX += object.getPositionX();
			targetY += object.getPositionY();
		}
		final float div = 1.0f/targetObjects.size();
		targetX *= div;
		targetY *= div;
		
		//---- 座標を適用する。
		camera.translate(
			(targetX - camera.position.x) * interporation,
			(targetY - camera.position.y) * interporation,
			0.0f
		);
		
		//---- 移動範囲を制限する。
		final float left		= translateArea.x;
		final float right		= translateArea.x + translateArea.width;
		final float top		= translateArea.y;
		final float bottom		= translateArea.y + translateArea.height;
		// X
		if( camera.position.x > right )
		{
			camera.position.x = right;
		}
		else if( camera.position.x < left )
		{
			camera.position.x = left;
		}
		// Y
		if( camera.position.y > bottom )
		{
			camera.position.y = bottom;
		}
		else if( camera.position.y < top )
		{
			camera.position.y = top;
		}
		
		//---- カメラを更新する。
		camera.update();
	}
	
	/**
	 * 追従するオブジェクトを追加する。
	 * @param object	追従するオブジェクト
	 */
	public void addTargetObject(ObjectInterface object)
	{
		targetObjects.add(object);
	}
	
	/**
	 * 追従するオブジェクトを削除する。
	 * @param object	追従するオブジェクト
	 */
	public void removeTargetObject(ObjectInterface object)
	{
		targetObjects.remove(object);
	}
	
	/**
	 * 移動可能な範囲を設定する。
	 * @param x			X座標
	 * @param y			Y座標
	 * @param width		X方向の大きさ
	 * @param height	Y方向の大きさ
	 */
	public void setTranslateArea(float x, float y, float width, float height)
	{
		translateArea.set(x + camera.viewportWidth * 0.5f, y + camera.viewportHeight * 0.5f, width - camera.viewportWidth, height - camera.viewportHeight);
	}

	
	/** 動かすカメラオブジェクト */
	private final Camera camera;
	
	/** 移動可能な範囲 */
	private final Rectangle translateArea = new Rectangle(0, 0, 0, 0);
	
	/** 追従するオブジェクトの配列 */
	private final ArrayList<ObjectInterface> targetObjects = new ArrayList<ObjectInterface>(1);
	
	/** 補間係数（デフォルト値：0.1） */
	private float interporation = 0.1f;
}

package org.genshin.scrollninja.object.gui;

import org.genshin.scrollninja.object.AbstractObject;
import org.genshin.scrollninja.render.RenderObjectFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

/**
 * マウスカーソル
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class Cursor extends AbstractObject
{
	/**
	 * コンストラクタ
	 * @param camera		カーソルの座標計算に使用するカメラオブジェクト
	 * @param speed			カーソルの移動速度
	 */
	public Cursor(Camera camera, float speed)
	{
		//---- OSのマウスカーソルを非表示
		Gdx.input.setCursorCatched(true);
		
		//---- フィールド初期化
		this.camera = camera;
		this.speed = speed;
	}

	@Override
	public void update(float deltaTime)
	{
		//---- 移動
		position.x += Gdx.input.getDeltaX() * speed;
		position.y -= Gdx.input.getDeltaY() * speed;

		//---- 範囲制限
		final float halfWidth = camera.viewportWidth * 0.5f;
		final float halfHeight = camera.viewportHeight * 0.5f;
		if( Math.abs(position.x) > halfWidth )	position.x = Math.signum(position.x) * halfWidth;
		if( Math.abs(position.y) > halfHeight )	position.y = Math.signum(position.y) * halfHeight;
		
		//---- 適用
		getRenderObject(0).setPosition(camera.position.x + position.x, camera.position.y + position.y);
	}

	@Override
	protected void initializeSprite()
	{
		addRenderObject( RenderObjectFactory.getInstance().get("Cursor") );
	}
	
	
	/** カーソルの座標計算に使用するカメラオブジェクト */
	private final Camera camera;
	
	/** カーソルの移動速度 */
	private final float speed;
	
	/** カーソルのスクリーン座標 */
	private final Vector2 position = new Vector2(Vector2.Zero);
}

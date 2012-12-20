package org.genshin.scrollninja.object.effect;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.render.RenderObjectFactory;
import org.genshin.scrollninja.render.RenderObjectInterface;

import com.badlogic.gdx.math.Vector2;

/**
 * ダッシュ時の煙のエフェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class DashSmokeEffect extends AbstractEffect
{
	/**
	 * コンストラクタ
	 * @param positionX		X座標
	 * @param positionY		Y座標
	 * @param degrees		回転（度）
	 * @param flip			反転フラグ
	 */
	public DashSmokeEffect(float positionX, float positionY, float degrees, boolean flip)
	{
		flip(flip, false);
		
		//---- 座標設定
		for(RenderObjectInterface ro : getRenderObjects())
		{
			ro.setPosition(positionX, positionY);
			ro.setRotation(degrees);
		}
		
		//---- 各種パラメータ設定
		final Vector2 direction = Vector2.tmp.set( (flip?-1.0f:1.0f) * 300.0f, 100.0f );
		direction.mul(GlobalParam.INSTANCE.WORLD_SCALE);
		direction.rotate(degrees);
		setVelocityInterpolation(direction.x, direction.y, 0.0f, 0.0f);
		setColorInterpolation(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f);
	}

	@Override
	protected float getLife()
	{
		return 0.5f;
	}

	@Override
	protected void initializeSprite()
	{
		addRenderObject(RenderObjectFactory.getInstance().get("DashSmoke"));
	}
}

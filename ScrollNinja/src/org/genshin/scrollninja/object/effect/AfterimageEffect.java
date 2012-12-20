package org.genshin.scrollninja.object.effect;

import java.util.ArrayList;

import org.genshin.scrollninja.render.RenderObjectFactory;
import org.genshin.scrollninja.render.RenderObjectInterface;

/**
 * 残像エフェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class AfterimageEffect extends AbstractEffect
{
	/**
	 * コンストラクタ
	 * @param renderObjects		描画オブジェクト（現在は忍者限定）
	 * @param positionX			X座標
	 * @param positionY			Y座標
	 * @param degrees			回転（度）
	 */
	public AfterimageEffect(ArrayList<RenderObjectInterface> renderObjects, float positionX, float positionY, float degrees)
	{
		//---- 描画オブジェクトの状態をコピーする。
		final int count = renderObjects.size();
		for(int i = 0;  i < count;  ++i)
		{
			final RenderObjectInterface src = renderObjects.get(i);
			final RenderObjectInterface dest = getRenderObject(i);
			
			dest.getSprite().set(src.getSprite());
			dest.translate(positionX, positionY);
			dest.rotate(degrees);
		}

		//---- 各種パラメータ設定
		final float r = 0.3f;
		final float g = 0.3f;
		final float b = 0.0f;
		final float startA = 0.7f;
		setColorInterpolation(r, g, b, startA, r, g, b, 0.0f);
	}

	@Override
	protected void initializeSprite()
	{
		addRenderObject( RenderObjectFactory.getInstance().get("NinjaFoot") );
		addRenderObject( RenderObjectFactory.getInstance().get("NinjaBody") );
	}

	@Override
	protected float getLife()
	{
		return 0.15f;
	}
}

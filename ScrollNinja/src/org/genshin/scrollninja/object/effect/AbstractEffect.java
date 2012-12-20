package org.genshin.scrollninja.object.effect;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.AbstractObject;
import org.genshin.scrollninja.render.RenderObjectInterface;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * エフェクトの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractEffect extends AbstractObject
{
	/**
	 * コンストラクタ
	 */
	public AbstractEffect()
	{
		//---- 更新処理を管理するオブジェクトに追加する。
		GlobalParam.INSTANCE.currentUpdatableManager.add(this, getUpdatePriority());
		
		//---- 描画処理を管理するオブジェクトに追加する。
		GlobalParam.INSTANCE.currentRenderableManager.add(this, getRenderPriority());
		
		//---- フィールドを初期化する。
		life = getLife();
	}

	@Override
	public void dispose()
	{
		//---- 更新処理を管理するオブジェクトから削除する。
		GlobalParam.INSTANCE.currentUpdatableManager.remove(this, getUpdatePriority());

		//---- 描画処理を管理するオブジェクトから削除する。
		GlobalParam.INSTANCE.currentRenderableManager.remove(this, getRenderPriority());
		
		//---- 基本クラスの破棄もしておく。
		super.dispose();
	}
	
	@Override
	public void update(float deltaTime)
	{
		//---- 寿命が尽きたら自動的に破棄する。
		if( (life -= deltaTime) < 0.0f )
		{
			dispose();
			return;
		}
		
		//---- 移動の補間
		// len = (v1 + v2) * (t2 - t1) / 2
		//  v2 = v1 + a
		// len = (v1 + v1 + a) * (t2 - t1) / 2
		//     = (v1/2 + v1/2 + a/2) * (t2 - t1)
		//     = (v1 + a/2) * (t2 - t1)
		final float translateX = (velocity.x + accel.x * deltaTime * 0.5f) * deltaTime;
		final float translateY = (velocity.y + accel.y * deltaTime * 0.5f) * deltaTime;
		velocity.add(accel.tmp().mul(deltaTime));
		
		//---- 色の補間
		color.r += colorAccelR * deltaTime;
		color.g += colorAccelG * deltaTime;
		color.b += colorAccelB * deltaTime;
		color.a += colorAccelA * deltaTime;
		
		//---- 変化を適用する。
		for(RenderObjectInterface ro:getRenderObjects())
		{
			final Sprite sprite = ro.getSprite();
			
			sprite.translate(translateX,  translateY);
			sprite.setColor(color);
		}
	}
	
	/**
	 * 速度の補間率を設定する。
	 * @param startX	初期状態でのX軸方向の速度
	 * @param startY	初期状態でのY軸方向の速度
	 * @param endX		寿命が尽きた時点でのX軸方向の速度
	 * @param endY		寿命が尽きた時点でのY軸方向の速度
	 */
	protected final void setVelocityInterpolation(float startX, float startY, float endX, float endY)
	{
		//---- 速度
		velocity.set(startX, startY);
		
		//---- 加速度
		accel.set(endX, endY);
		accel.sub(velocity);
		accel.mul(1.0f/life);
	}
	
	protected final void setColorInterpolation(float startR, float startG, float startB, float startA, float endR, float endG, float endB, float endA)
	{
		//---- 現在の色
		color.set(startR, startG, startB, startA);
		
		//---- 色の変化率
		final float invLife = 1.0f / life;
		colorAccelR = (endR - startR) * invLife;
		colorAccelG = (endG - startG) * invLife;
		colorAccelB = (endB - startB) * invLife;
		colorAccelA = (endA - startA) * invLife;
	}

	/**
	 * エフェクトの寿命を取得する。
	 * @return		寿命（秒）
	 */
	protected float getLife()
	{
		return 1.0f;
	}
	
	/**
	 * 更新処理の処理優先度を取得する。
	 * @return		更新処理の処理優先度
	 */
	protected int getUpdatePriority()
	{
		return 0;
	}
	
	/**
	 * 描画処理の処理優先度を取得する。
	 * @return		描画処理の処理優先度
	 */
	protected int getRenderPriority()
	{
		return 0;
	}
	
	
	/** 寿命（秒） */
	private float life;
	
	/** 速度 */
	private final Vector2 velocity = new Vector2(Vector2.Zero);
	
	/** 加速度 */
	private final Vector2 accel = new Vector2(Vector2.Zero);
	
	/** 現在の色 */
	private final Color color = new Color(Color.WHITE);
	
	/** 色の変化率（赤） */
	private float colorAccelR = 0.0f;

	/** 色の変化率（緑） */
	private float colorAccelG = 0.0f;

	/** 色の変化率（青） */
	private float colorAccelB = 0.0f;

	/** 色の変化率（不透明度） */
	private float colorAccelA = 0.0f;
}

package org.genshin.scrollninja.render;

import java.util.HashMap;
import java.util.Map;

import org.genshin.old.scrollninja.GameMain;
import org.genshin.scrollninja.render.animation.AnimationInterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 描画オブジェクトの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractRenderObject implements RenderObjectInterface
{
	@Override
	public void translate(float x, float y)
	{
		sprite.translate(x, y);
	}

	@Override
	public void rotate(float degrees)
	{
		sprite.rotate(degrees);
	}

	@Override
	public void update(float deltaTime)
	{
		if( currentAnimation != null )
		{
			animationTime += deltaTime;
			currentAnimation.apply(sprite, animationTime);
		}
	}

	@Override
	public void render()
	{
		final SpriteBatch spriteBatch = GameMain.spriteBatch;
		final float originX = sprite.getOriginX();
		final float originY = sprite.getOriginY();
		sprite.translate(-originX, -originY);
		sprite.draw(spriteBatch);
		sprite.translate(originX, originY);
	}
	
	@Override
	public void setAnimation(String name)
	{
		AnimationInterface nextAnimation = animations.get(name);
		if( currentAnimation != nextAnimation )
		{
			currentAnimation = nextAnimation;
			animationTime = 0.0f;
		}
	}
	
	@Override
	public void setAnimationTime(float time)
	{
		animationTime = time;
	}
	
	@Override
	public void setPosition(float x, float y)
	{
		sprite.setPosition(x, y);
	}

	@Override
	public void setRotation(float degrees)
	{
		sprite.setRotation(degrees);
	}

	@Override
	public boolean hasAnimation()
	{
		return animations != null && !animations.isEmpty();
	}

	@Override
	public boolean isAnimationLooping()
	{
		return currentAnimation != null && currentAnimation.isLooping();
	}

	@Override
	public boolean isAnimationFinished()
	{
		return currentAnimation == null || currentAnimation.isFinished(animationTime);
	}
	
	@Override
	public Sprite getSprite()
	{
		return sprite;
	}
	
	@Override
	public float getAnimationTime()
	{
		return animationTime;
	}

	@Override
	public float getPositionX()
	{
		return sprite.getX();
	}

	@Override
	public float getPositionY()
	{
		return sprite.getY();
	}

	@Override
	public float getRotation()
	{
		return sprite.getRotation();
	}
	
	/**
	 * 描画に使用するスプライトを設定する。
	 * @param sprite	スプライトオブジェクト
	 */
	protected void setSprite(Sprite sprite)
	{
		this.sprite.set(sprite);
	}

	/**
	 * アニメーションを追加する。
	 * @param name			アニメーションの名前
	 * @param animation		アニメーションオブジェクト
	 */
	protected void addAnimation(String name, AnimationInterface animation)
	{
		if(animations == null)
		{
			animations = new HashMap<String, AnimationInterface>();
		}
		animations.put(name, animation);
	}

	
	/** 描画スプライト */
	private final Sprite	sprite = new Sprite();
	
	/** アニメーション用のタイマー */
	private float	animationTime;
	
	/** 現在のアニメーション */
	private AnimationInterface	currentAnimation;
	
	/** アニメーションのマップ */
	private Map<String, AnimationInterface>	animations;
}

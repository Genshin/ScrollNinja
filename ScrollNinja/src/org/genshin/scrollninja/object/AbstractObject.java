package org.genshin.scrollninja.object;

import java.util.ArrayList;

import org.genshin.old.scrollninja.GameMain;
import org.genshin.scrollninja.render.RenderObjectInterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * オブジェクトの基本クラス
 * @author インターン生
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractObject implements ObjectInterface
{
	/**
	 * コンストラクタ
	 */
	public AbstractObject()
	{
		initializeSprite();
	}
	
	@Override
	public void dispose()
	{
		sprites.clear();
		renderObjects.clear();
	}

	@Override
	public void update(float deltaTime)
	{
		/* 何もしない */
	}

	@Override
	public void render()
	{
		// 描画処理
		if( !renderObjects.isEmpty() )
		{
			for(RenderObjectInterface ro : renderObjects)
			{
				ro.update(1.0f/60.0f);
				ro.render();
			}
		}
		else
		{
			final SpriteBatch spriteBatch = GameMain.spriteBatch;
			final int count = sprites.size();
			for (int i = 0; i < count; ++i)
			{
				final Sprite current = sprites.get(i);
				
				current.draw(spriteBatch);
			}
		}
	}
	
	@Override
	public float getPositionX()
	{
		if( !renderObjects.isEmpty() )
		{
			return renderObjects.get(0).getPositionX();
		}
		else
		{
			return sprites.get(0).getX();
		}
	}
	
	@Override
	public float getPositionY()
	{
		if( !renderObjects.isEmpty() )
		{
			return renderObjects.get(0).getPositionY();
		}
		else
		{
			return sprites.get(0).getY();
		}
	}
	
	/**
	 * スプライト反転フラグを設定する。
	 * @param x		x方向の反転フラグ
	 * @param y		y方向の反転フラグ
	 */
	protected final void flip(boolean x, boolean y)
	{
		if( !renderObjects.isEmpty() )
		{
			for( RenderObjectInterface ro : renderObjects )
			{
				Sprite sprite = ro.getSprite();
				sprite.setScale(x?-1.0f:1.0f, y?-1.0f:1.0f);
			}
		}
		{
			int count = sprites.size();
			for(int i = 0; i < count;  ++i)
			{
				sprites.get(i).flip(x, y);
			}
		}
	}
	
	/**
	 * ここでSpriteを生成する。<br>
	 * このメソッドは、AbstractObjectのコンストラクタから自動的に呼び出される。
	 */
	protected void initializeSprite()
	{
		// TODO 最終的にはabstract化する。
	}
	
	/**
	 * 描画オブジェクトを追加する。
	 * @param renderObject		描画オブジェクト
	 */
	protected void addRenderObject(RenderObjectInterface renderObject)
	{
		assert renderObject != null;
		renderObjects.add(renderObject);
	}
	
	/**
	 * Spriteオブジェクトの配列を取得する。
	 * @return		Spriteオブジェクトの配列
	 */
	protected ArrayList<Sprite> getSprites()
	{
		return sprites;
	}
	
	/**
	 * Spriteオブジェクトを取得する。
	 * @param		index インデックス番号
	 * @return		Spriteオブジェクト
	 */
	protected Sprite getSprite(int index)
	{
		assert index < sprites.size();
		return sprites.get(index);
	}
	
	/**
	 * 描画オブジェクトの配列を取得する。
	 * @return		描画オブジェクトの配列
	 */
	protected ArrayList<RenderObjectInterface> getRenderObjects()
	{
		return renderObjects;
	}
	
	/**
	 * 描画オブジェクトを取得する。
	 * @param index		インデックス番号
	 * @return		描画オブジェクト
	 */
	protected RenderObjectInterface getRenderObject(int index)
	{
		assert index < renderObjects.size();
		return renderObjects.get(index);
	}
	

	/** 描画オブジェクト配列 */
	protected final ArrayList<RenderObjectInterface> renderObjects = new ArrayList<RenderObjectInterface>(1);
	
	// XXX スプライト配列はいずれ廃止して、描画オブジェクト配列に置き換える。
	/** スプライト配列 */
	protected final ArrayList<Sprite>	sprites = new ArrayList<Sprite>(1);		// TODO いずれprivate化すべし。
}

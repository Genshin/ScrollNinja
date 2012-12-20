package org.genshin.scrollninja.render;

import org.genshin.engine.factory.FactoryInterface;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.render.animation.AnimationInterface;
import org.genshin.scrollninja.render.animation.TextureAnimation;
import org.genshin.scrollninja.render.animation.TextureAnimationDef;
import org.genshin.scrollninja.utils.SpriteLoader;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 描画オブジェクトを生成する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public final class RenderObjectFactory implements FactoryInterface<RenderObjectInterface, String>
{
	/**
	 * コンストラクタ
	 */
	private RenderObjectFactory()
	{
		/* 何もしない */
	}
	
	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	public static final RenderObjectFactory getInstance()
	{
		return instance;
	}
	
	@Override
	public RenderObjectInterface get(String key)
	{
		//---- TODO 仮。いずれもっと効率的な実装にすること。
		Element rootElement = XMLFactory.getInstance().get(GlobalParam.INSTANCE.XML_DIRECTORY_PATH + GlobalParam.INSTANCE.SPRITE_PARAM_XML_FILE_NAME);
		rootElement = rootElement.getChildByName(key);

		// 描画オブジェクト生成
		final RenderObjectForFactory ro = new RenderObjectForFactory();
		
		// スプライト生成
		final SpriteLoader spriteLoader = new SpriteLoader(rootElement);
		final Sprite sprite = spriteLoader.create();
		ro.setSprite(sprite);
		
		// アニメーション生成
		rootElement = rootElement.getChildByName("Sprite");
		final int childCount = rootElement.getChildCount();
		final float invWorldScale = GlobalParam.INSTANCE.INV_WORLD_SCALE;
		final float invFPS = 1.0f / 60.0f;
		for(int i = 0;  i < childCount;  ++i)
		{
			final Element childElement = rootElement.getChild(i);
			
			if( childElement.getName().equals("Animation") )
			{
				String name = childElement.get("Name");

				TextureAnimationDef tad = new TextureAnimationDef();
				tad.texture			= sprite.getTexture();
				tad.size.x			= (int)(sprite.getWidth() * invWorldScale);
				tad.size.y			= (int)(sprite.getHeight() * invWorldScale);
				tad.startIndex.x	= childElement.getInt("StartX", 0);
				tad.startIndex.y	= childElement.getInt("StartY", 0);
				tad.frameCount		= childElement.getInt("FrameCount", 1);
				tad.time			= childElement.getInt("Time", 1) * invFPS;
				tad.looping			= childElement.getBoolean("Looping", true);
				
				AnimationInterface animation = new TextureAnimation(tad);
				
				ro.addAnimation(name, animation);
			}
		}
		
		return ro;
	}
	
	
	/** シングルトンインスタンス */
	static final RenderObjectFactory instance = new RenderObjectFactory();
}

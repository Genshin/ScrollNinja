package org.genshin.scrollninja.object.kaginawa;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.utils.FixtureDefLoader;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 鉤縄関連の定数等を定義する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
enum KaginawaParam
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	private KaginawaParam()
	{
		final float worldScale = GlobalParam.INSTANCE.WORLD_SCALE;
		
		//---- 鉤縄の挙動関連
		{
			Element rootElement = XMLFactory.getInstance().get(GlobalParam.INSTANCE.XML_DIRECTORY_PATH + GlobalParam.INSTANCE.OBJECT_PARAM_XML_FILE_NAME);
			rootElement = rootElement.getChildByName("Kaginawa");
			SLACK_VELOCITY		= rootElement.getFloat("SlackVelocity") * worldScale;
			SHRINK_VELOCITY		= rootElement.getFloat("ShrinkVelocity") * worldScale;
			LENGTH				= rootElement.getFloat("Length") * worldScale;
		}
		
		//---- 衝突関連
		{
			Element rootElement = XMLFactory.getInstance().get(GlobalParam.INSTANCE.XML_DIRECTORY_PATH + GlobalParam.INSTANCE.COLLISION_PARAM_XML_FILE_NAME);
			rootElement = rootElement.getChildByName("Kaginawa");
			FIXTURE_DEF_LOADER = new FixtureDefLoader(rootElement);

			// XXX 仮
			COLLISION_RADIUS	= rootElement.getChildByName("FixtureDef").getChildByName("CircleShape").getFloat("Radius") * worldScale;
		}
	}
	
	
	/** 鉤縄の伸びる速度（毎秒） */
	final float SLACK_VELOCITY;
	
	/** 鉤縄の縮む速度（毎秒） */
	final float SHRINK_VELOCITY;
	
	/** 鉤縄の長さ */
	final float LENGTH;
	
	/** 衝突判定の半径（仮） */
	final float COLLISION_RADIUS;
	
	/** Fixtureの定義情報 */
	final FixtureDefLoader FIXTURE_DEF_LOADER;
}

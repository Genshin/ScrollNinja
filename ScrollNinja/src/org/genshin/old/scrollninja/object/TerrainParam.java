package org.genshin.old.scrollninja.object;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.utils.FixtureDefLoader;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 地形関連の定数等を定義する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
enum TerrainParam
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	private TerrainParam()
	{
		//---- 衝突関連
		{
			Element rootElement = XMLFactory.getInstance().get(GlobalParam.INSTANCE.XML_DIRECTORY_PATH + GlobalParam.INSTANCE.COLLISION_PARAM_XML_FILE_NAME);
			rootElement = rootElement.getChildByName("Terrain");
			
			FIXTURE_DEF_LOADER = new FixtureDefLoader(rootElement);
		}
	}
	
	/** Fixtureの定義情報 */
	final FixtureDefLoader FIXTURE_DEF_LOADER;
}

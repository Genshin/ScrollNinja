package org.genshin.scrollninja.utils;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * Fixtureの定義を読み込み、生成する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class FixtureDefLoader
{
	/**
	 * Fixtureの定義をXMLから読み込むコンストラクタ
	 * @param xmlElement	XMLエレメントオブジェクト
	 */
	public FixtureDefLoader(Element xmlElement)
	{
		assert xmlElement != null;
		
		xmlElement	= xmlElement.getChildByName("FixtureDef");
		density		= xmlElement.getFloat("Density", 0.0f);
		friction	= xmlElement.getFloat("Friction", 0.2f);
		restitution	= xmlElement.getFloat("Restitution", 0.0f);
	}
	
	/**
	 * この定義を元にしたFixtureDefオブジェクトを生成する。
	 * @return		この定義を元にしたFixtureDefオブジェクト
	 */
	public FixtureDef createFixtureDef()
	{
		FixtureDef fd = new FixtureDef();
		
		fd.density		= density;
		fd.friction		= friction;
		fd.restitution	= restitution;
		
		return fd;
	}
	
	/** 密度（kg/m^2） */
	private final float density;
	
	/** 摩擦係数 */
	private final float friction;
	
	/** 反発係数 */
	private final float restitution;
}

package org.genshin.scrollninja.utils;

import java.io.IOException;

import org.genshin.engine.factory.AbstractFlyweightFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public final class XMLFactory extends AbstractFlyweightFactory<Element, String>
{
	/**
	 * コンストラクタ
	 */
	private XMLFactory()
	{
		/* 何もしない */
	}
	
	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	public static XMLFactory getInstance()
	{
		return instance;
	}
	
	@Override
	protected Element create(String key)
	{
		final XmlReader reader = new XmlReader();
		Element element = null;
		
		try
		{
			element = reader.parse( Gdx.files.internal(key) );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return element;
	}

	/** シングルトンインスタンス */
	private static final XMLFactory instance = new XMLFactory(); 
}

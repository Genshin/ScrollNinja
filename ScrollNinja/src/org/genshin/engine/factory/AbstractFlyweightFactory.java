package org.genshin.engine.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * オブジェクトを生成する基本クラス。<br>
 * オブジェクトを取得する際、使い回せるものがあればそれを使い回す。
 * @author kou
 * @since		1.0
 * @version	1.0
 *
 * @param <ReturnType>		生成するオブジェクトの型
 * @param <KeyType>			オブジェクトを判別するための識別子
 */
public abstract class AbstractFlyweightFactory<ReturnType, KeyType> implements FactoryInterface<ReturnType, KeyType>
{
	@Override
	public final ReturnType get(KeyType key)
	{
		ReturnType result = objects.get(key);
		if(result == null)
		{
			result = create(key);
			objects.put(key, result);
		}
		return result;
	}

	/**
	 * 新しいオブジェクトを生成する。
	 * @param key	オブジェクトの識別子
	 * @return		新しいオブジェクト
	 */
	protected abstract ReturnType create(KeyType key);
	
	/** 管理オブジェクトのマップ */
	private final Map<KeyType, ReturnType> objects = new HashMap<KeyType, ReturnType>();
}

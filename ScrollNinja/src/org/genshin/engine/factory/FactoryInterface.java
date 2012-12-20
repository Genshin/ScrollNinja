package org.genshin.engine.factory;

/**
 * オブジェクトを生成するインタフェース。
 * @author kou
 * @since		1.0
 * @version	1.0
 *
 * @param <ReturnType>		生成するオブジェクトの型
 * @param <KeyType>			オブジェクトを判別するための識別子
 */
public interface FactoryInterface<ReturnType, KeyType>
{
	/**
	 * オブジェクトを取得する。
	 * @param key	オブジェクトの識別子
	 * @return		オブジェクト
	 */
	public ReturnType get(KeyType key);
}

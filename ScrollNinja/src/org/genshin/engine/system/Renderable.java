package org.genshin.engine.system;

/**
 * 描画処理を持つインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface Renderable
{
	/**
	 * 描画処理を実行する。
	 * XXX 引数にSpriteBatchとか入れる？　ライブラリ依存度が高くなるのが問題かも知れないけど。
	 * XXX 場合によってはprotected化するのもアリ？
	 */
	public void render();
}

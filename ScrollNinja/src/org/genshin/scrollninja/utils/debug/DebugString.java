package org.genshin.scrollninja.utils.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * デバッグ文字列
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public final class DebugString
{
	/**
	 * 初期化する。
	 * @param enabled	有効フラグ
	 */
	public static final void initialize(boolean enabled)
	{
		if( debugFontImpl == null )
			debugFontImpl = enabled ? new DebugFontImpl() : new NullDebugFontImpl();
	}
	
	/**
	 * デバッグフォントを描画する。
	 */
	public static final void render()
	{
		debugFontImpl.render();
	}
	
	/**
	 * デバッグフォントで描画する情報を追加する。
	 * @param str		描画する情報
	 */
	public static final void add(String str)
	{
		debugFontImpl.add(str);
	}
	
	/** 実装オブジェクト */
	private static DebugFontImplInterface debugFontImpl = null;
}


/**
 * 実装オブジェクトインタフェース
 */
interface DebugFontImplInterface
{
	public void render();
	public void add(String str);
}


/**
 * 空の実装オブジェクト
 */
final class NullDebugFontImpl implements DebugFontImplInterface
{

	@Override
	public final void render()
	{
		/* 何もしない */
	}

	@Override
	public final void add(String str)
	{
		/* 何もしない */
	}
}


/**
 * 実装オブジェクト
 */
final class DebugFontImpl implements DebugFontImplInterface
{
	@Override
	public final void render()
	{
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.begin();
		bitmapFont.drawMultiLine(spriteBatch, buf, 0.0f, Gdx.graphics.getHeight());
		spriteBatch.end();
		
		buf.setLength(0);
	}

	@Override
	public final void add(String str)
	{
		buf.append(str);
		buf.append("\n");
	}
	
	/** スプライトバッチ */
	private final SpriteBatch spriteBatch = new SpriteBatch();
	
	/** フォント */
	private final BitmapFont bitmapFont = new BitmapFont();
	
	/** 文字列バッファ */
	private final StringBuffer buf = new StringBuffer(1000);
}

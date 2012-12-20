package org.genshin.scrollninja.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * デバッグ用のスクリーンの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractDebugScreen extends AbstractScreen
{
	@Override
	public void render(float delta)
	{
		//---- 描画フラグの切り替え
		final boolean nowInput = Gdx.input.isKeyPressed(Keys.NUM_0);
		if( !prevInput && nowInput )
			renderEnabled = !renderEnabled;
		prevInput = nowInput;
		
		//---- メイン描画
		super.render(delta);
		
		//---- デバッグ用描画
		if(renderEnabled)
			box2dDebugRenderer.render(getWorld(), getCamera().combined);
	}
	
	/** 前回の入力情報 */
	private boolean prevInput = false;
	
	/** Box2Dの衝突判定オブジェクトを描画するためのレンダラ  */
	private static final Box2DDebugRenderer box2dDebugRenderer = new Box2DDebugRenderer();
	
	/** 描画フラグ */
	private static boolean renderEnabled = false;
}

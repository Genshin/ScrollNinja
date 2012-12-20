package org.genshin.scrollninja.render.animation;

import java.awt.Point;

import com.badlogic.gdx.graphics.Texture;


public class TextureAnimationDef
{
	/** アニメーションに使用するテクスチャ */
	public Texture texture;
	
	/** 1コマの大きさ */
	public final Point size = new Point();
	
	/** アニメーションの最初のコマの位置（ピクセル数ではなく、0から始まるコマ数で指定する） */
	public final Point startIndex = new Point();
	
	/** アニメーションの枚数 */
	public int frameCount = 1;
	
	/** アニメーションにかかる時間（秒） */
	public float time = 1.0f/60.0f;
	
	/** ループフラグ */
	public boolean looping	 = true;
}

package org.genshin.scrollninja;

import java.awt.Image;

import com.badlogic.gdx.math.Vector2;

//========================================
// 画像クラス
//========================================
public class StructImage {
	
	private Image image;
	private Vector2 position;
	
	/**
	 * コンストラクタ
	 * @param x		座標X
	 * @param y		座標Y
	 */
	StructImage( String filePath ) {
		position = new Vector2( 0.0f, 0.0f);
		
		image = FileOperation.LoadImage(filePath);
	}
	
	/**
	 * 画像を返す
	 * @return
	 */
	public Image GetImage() { return image; }
}

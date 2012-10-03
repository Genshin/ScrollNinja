package org.genshin.scrollninja;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.Sprite;
//
public class Background {
	private float zIndex;
	private Point centerPoint;
	private Sprite graphic;

	// 定数
	private final static float Z_FAR = -0.5f;
	private final static float Z_MAIN = 5.0f;
	private final static float Z_NEAR = 10.0f;

	// コンストラクタ
	public Background() {
		this.zIndex = Z_MAIN;
		this.centerPoint = new Point(0, 0);
		this.graphic = null;
	}

	// コンストラクタ
	public Background(int type, int x, int y, Sprite sprite) {
		// typeで背景(マイナス)、メイン（ゼロ）、近景の判別（プラス）
		if (type < 0)
			this.zIndex = Z_FAR;
		else if (type > 0)
			this.zIndex = Z_NEAR;
		else
			this.zIndex = Z_MAIN;

		this.centerPoint.x = x;
		this.centerPoint.y = y;
		this.graphic = sprite;
	}

	// zIndex（移動速度）をゲット
	public float getZIndex() {
		return this.zIndex;
	}

	// 中央のxy値をゲット
	public Point getCenterPoint() {
		return this.centerPoint;
	}

	// スプライト（背景画像）セット
	public void setSprite(Sprite sprite) {
		this.graphic = sprite;
	}
}

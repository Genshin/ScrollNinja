package org.genshin.scrollninja;

import java.util.ArrayList;

import org.genshin.scrollninja.object.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

// TODO チャクラと武器表示がまだ…
//========================================
//クラス宣言		インターフェース表示を扱うクラス
//========================================
public class Interface {
	private static Sprite scroll;		// HPの巻物本体部分
	private static Sprite scrollRight;	// 巻物の右端
	private static Sprite hp;			// プレイヤーHP
	private static Sprite hyoutan;		// チャクラのひょうたん部分
	private static Sprite chakra;		// プレイヤーチャクラ
	private ArrayList<Sprite> weapon;	// 武器

	private Animation scrollAnimation;	// 巻物のアニメーション
	private TextureRegion nowFrame;		// 巻物の現在のコマ
	private float stateTime;			// アニメーション用

	private Player player;				// プレイヤー情報格納
	private float percentHP;			// 現在のHPの割合　1が最大
	private float countHP;				// 巻物を0.01ずつ現在のHPの割合まで動かすためのカウンタ
	private float percentChakra;		// 現在のチャクラの割合　1が最大
	private float countChakra;			// 巻物を0.01ずつ現在のチャクラの割合まで動かすためのカウンタ
	public static boolean calculateHP;		// HP計算
	public static boolean calculateChakra;	// チャクラ計算
	private static float transrateX;		// X移動量
	private boolean stopHP;
	private boolean stopChakra;

	/**
	 * コンストラクタ
	 */
	public Interface() {
		weapon = new ArrayList<Sprite>();

		// テクスチャ画像読み込み
		Texture texture = new Texture(Gdx.files.internal("data/interface.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// 巻物アニメーション
		TextureRegion[][] tmp = TextureRegion.split(texture, 128, 128);
		TextureRegion[] frame = new TextureRegion[3];
		int index = 0;
		for (int i = 0; i < frame.length; i++)
			frame[index++] = tmp[0][i];
		scrollAnimation = new Animation(5.0f, frame);
		scroll = new Sprite(scrollAnimation.getKeyFrame(0, false));
		scroll.setOrigin(scroll.getWidth() * 0.5f, scroll.getHeight() * 0.5f);
		scroll.setScale(ScrollNinja.scale);

		nowFrame = scrollAnimation.getKeyFrame(0, false);

		// HP部分
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 128, 512, 128);
		hp = new Sprite(tmpRegion);
		hp.setOrigin(hp.getWidth() * 0.5f, hp.getHeight() * 0.5f);
		hp.setScale(ScrollNinja.scale);

		// 巻物の右端部分
		tmpRegion = new TextureRegion(texture, 384, 0, 128, 128);
		scrollRight = new Sprite(tmpRegion);
		scrollRight.setOrigin(scrollRight.getWidth() * 0.5f, scrollRight.getHeight() * 0.5f);
		scrollRight.setScale(ScrollNinja.scale);

		// ひょうたん
		tmpRegion = new TextureRegion(texture, 0, 256, 128, 128);
		hyoutan = new Sprite(tmpRegion);
		hyoutan.setOrigin(hyoutan.getWidth() * 0.5f, hyoutan.getHeight() * 0.5f);
		hyoutan.setScale(ScrollNinja.scale);

		// チャクラ
		tmpRegion = new TextureRegion(texture, 128, 256, 128, 128);
		chakra = new Sprite(tmpRegion);
		chakra.setOrigin(chakra.getWidth() * 0.5f, chakra.getHeight() * 0.5f);
		chakra.setScale(ScrollNinja.scale);

		// 最初の設定；
		percentHP 			= 1;
		countHP 			= percentHP;
		percentChakra 	= 0;
		countChakra 		= percentChakra;
		calculateHP 		= false;
		calculateChakra 	= false;
		stopHP 			= true;
		stopChakra 		= true;
		stateTime 			= 0;
		transrateX 		= 0;
	}

	/**
	 * update
	 * 更新
	 */
	public void update() {
		// 描画位置セット
		Vector2 cameraPosition = new Vector2(GameMain.camera.position.x, GameMain.camera.position.y);
		scroll.setPosition(cameraPosition.x - 64 - (ScrollNinja.window.x * 0.5f * ScrollNinja.scale - 6.4f),
						   cameraPosition.y - 64 + (ScrollNinja.window.y * 0.5f * ScrollNinja.scale -6.4f));
		hp.setPosition
			(cameraPosition.x - 256 - (ScrollNinja.window.x * 0.5f * ScrollNinja.scale - 25.6f) - transrateX,
			 cameraPosition.y - 64 + (ScrollNinja.window.y * 0.5f * ScrollNinja.scale -6.4f));
		
		scrollRight.setPosition(
				cameraPosition.x - 64 -(ScrollNinja.window.x * 0.5f * ScrollNinja.scale - 6.4f) + 44.5f - transrateX,
				 cameraPosition.y - 64 + (ScrollNinja.window.y * 0.5f * ScrollNinja.scale -6.4f));
		/*
		hyoutan.setPosition(scroll.getX() + 51.2f, scroll.getY());
		chakra.setPosition(hyoutan.getX(), hyoutan.getY());
		*/

		// HPに変動があれば計算
		if (calculateHP)
			calculateHP();

		// チャクラに変動があれば計算
		if (calculateChakra)
			calculateChakra();

		// HP回復　1フレームで0.01ずつ増加
		if ( !stopHP && countHP < 0 ) {
			countHP += 0.01f;
			hp.scroll(0.0085f, 0);
			transrateX -= 0.45f;

			stateTime += 1;
			scrollAnimation.setPlayMode(Animation.LOOP_REVERSED);
			nowFrame = scrollAnimation.getKeyFrame(stateTime, true);
			scroll.setRegion(nowFrame);

			if (countHP > 0)
				stopHP = true;
		}
		// HP減る　1フレームで0.01ずつ減少
		if ( !stopHP && countHP > 0 ) {
			countHP -= 0.01f;
			hp.scroll(-0.0085f, 0);
			transrateX += 0.45f;

			stateTime += 1;
			scrollAnimation.setPlayMode(Animation.LOOP);
			nowFrame = scrollAnimation.getKeyFrame(stateTime, true);
			scroll.setRegion(nowFrame);

			if (countHP < 0)
				stopHP = true;
		}

		// チャクラ増える　1フレームで0.01ずつ増加
		if ( !stopChakra && countChakra < 0 ) {
			countChakra += 0.01f;
			chakra.scroll(0, -0.01f);

			if (countChakra > 0)
				stopChakra = true;
		}
		// チャクラ減る　1フレームで0.01ずつ減少
		if ( !stopChakra && countChakra > 0 ) {
			countChakra -= 0.01f;
			chakra.scroll(0, 0.01f);

			if (countChakra < 0)
				stopChakra = true;
		}

		if (stateTime > 60)
			stateTime = 0;
	}

	/**
	 * calculateHP
	 * HP表示用計算
	 */
	public void calculateHP() {
		countHP = percentHP;
		// プレイヤー情報取得
		player = PlayerManager.GetPlayer(0);
		// 現在の割合を取得
		percentHP = (float)player.GetHP() / (float)player.GetMaxHP();
		// いくつ減らすか計算
		countHP -= percentHP;
		calculateHP = false;
		stopHP = false;
	}

	/**
	 * calculateChakra
	 * チャクラ表示用計算
	 */
	public void calculateChakra() {
		countChakra = percentChakra;
		// プレイヤー情報取得
		player = PlayerManager.GetPlayer(0);
		// 現在の割合を取得
		percentChakra = (float)player.GetChakra() / (float)player.GetMaxChakra();
		// いくつ減らすか計算
		countChakra -= percentChakra;
		calculateChakra = false;
		stopChakra = false;
	}

	/**
	 * Draw
	 * 描画
	 */
	public void Draw() {
		hp.draw(GameMain.spriteBatch);
		scrollRight.draw(GameMain.spriteBatch);
		scroll.draw(GameMain.spriteBatch);
		chakra.draw(GameMain.spriteBatch);
		hyoutan.draw(GameMain.spriteBatch);
	}
}
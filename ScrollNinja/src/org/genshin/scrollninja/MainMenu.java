package org.genshin.scrollninja;

import javax.swing.JOptionPane;

import org.genshin.scrollninja.object.Background;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainMenu implements Screen{
	private Game scrollNinja;

	private OrthographicCamera camera;
	public static SpriteBatch spriteBatch;

	private Sprite modeContinue;			// コンティニュー
	private Sprite modeNewGame;				// ニューゲーム
	private Sprite modeLoadGame;			// ロードゲーム
	private Sprite modeNetwork;				// ネットワーク
	private Sprite modeOption;				// オプション
	private Sprite modeExit;				// エグジット

	private int    nextStageNum;			// 次の画面で表示されるステージのナンバー

	// 画像座標
	private int spritePositionX;
	private final float MOVE_SPEED = 0.5f;	// スクロール時の移動速度
	private boolean scrollFlag;				// スクロールフラグ

	private final static int FADE_MENU = -50;		// 画像移動の終わり座標

	// コンストラクタ
	public MainMenu(Game game) {
		this.scrollNinja = game;

		// カメラ作成
		camera = new OrthographicCamera(ScrollNinja.window.x * ScrollNinja.scale,
										ScrollNinja.window.y * ScrollNinja.scale);
		// スプライトバッチ作成
		spriteBatch = new SpriteBatch();

		nextStageNum = 0;
		// TODO 読み込んだセーブファイルからコンティニューする時のステージを設定
		// stage =
		// StageManager.StageTrance(stage);
		// nextStageNum = StageManager.GetNowStage();
		BackgroundManager.CreateBackground(nextStageNum, false);

		// テクスチャ読み込み
		Texture texture = new Texture(Gdx.files.internal("data/menu.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		spritePositionX = -105;
		// TODO 位置は後で要調整
		// 選択肢コンテニュー
		TextureRegion region = new TextureRegion(texture, 0, 0, 256, 35);
		modeContinue = new Sprite(region);
		modeContinue.setPosition(spritePositionX, 0);
		modeContinue.setScale(ScrollNinja.scale);

		// 選択肢ニューゲーム
		region = new TextureRegion(texture, 0, 40, 256, 35);
		modeNewGame = new Sprite(region);
		modeNewGame.setPosition(spritePositionX, -4);
		modeNewGame.setScale(ScrollNinja.scale);

		// 選択肢ロードゲーム
		region = new TextureRegion(texture, 0, 85, 256, 35);
		modeLoadGame = new Sprite(region);
		modeLoadGame.setPosition(spritePositionX, -8);
		modeLoadGame.setScale(ScrollNinja.scale);

		// 選択肢ネットワーク
		region = new TextureRegion(texture, 0, 128, 256, 35);
		modeNetwork = new Sprite(region);
		modeNetwork.setPosition(spritePositionX, -12);
		modeNetwork.setScale(ScrollNinja.scale);

		// 選択肢オプション
		region = new TextureRegion(texture, 0, 176, 256, 35);
		modeOption = new Sprite(region);
		modeOption.setPosition(spritePositionX, -16);
		modeOption.setScale(ScrollNinja.scale);

		// 選択肢終了
		region = new TextureRegion(texture, 0, 218, 256, 35);
		modeExit = new Sprite(region);
		modeExit.setPosition(spritePositionX, -20);
		modeExit.setScale(ScrollNinja.scale);

		// 初期化
		scrollFlag = false;
	}

	// 更新
	public void update(float delta) {

		if (Gdx.input.isKeyPressed(Keys.A)) {
			FileOperation.LoadFile("data/test.txt");
			FileOperation.ExportFile("data/aaa.txt");
		}

		// エンターキーでコンティニュー　仮挿入中
		if (Gdx.input.isKeyPressed(Keys.ENTER))
				scrollFlag = true;		// スプライトを動かすフラグオン

		// クリックされたらゲームステート移行
		if (Gdx.input.isTouched()) {
			// クリック座標によってどにメニューが選択されたか判断
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();

			// コンティニュー
			if (x > 530 && x < 770 && y > 105 && y < 140) {
				scrollFlag = true;
			}
			// ニューゲーム
			if (x > 530 && x < 770 && y > 140 && y < 175) {

			}
			// ロードゲーム
			if (x > 530 && x < 770 && y > 175 && y < 210) {

			}
			// ネットワーク
			if (x > 530 && x < 770 && y > 210 && y < 245) {
			}
			// オプション
			if (x > 530 && x < 770 && y > 245 && y < 280) {

			}
			// 終了
			if ( x > 530 && x < 770 && y > 315 && y < 350 ) {
				int message =
					JOptionPane.showConfirmDialog(null, "終了しますか？", "Exit", JOptionPane.YES_NO_OPTION);
				if(message == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		}

		// 選択肢をクリックしたら画像移動
		moveSprite();
	}

	//---------------------------------------------------
	// 画像移動
	// 画面端にいったらステート移行
	//---------------------------------------------------
	public void moveSprite() {
		// クリックされたらメニューの文字が移動
		if (scrollFlag) {
			// 加速して画面外へ
			spritePositionX += MOVE_SPEED;

			// スプライト移動
			modeContinue.setPosition(spritePositionX, 0);
			modeNewGame.setPosition(spritePositionX, -4);
			modeLoadGame.setPosition(spritePositionX, -8);
			modeNetwork.setPosition(spritePositionX, -12);
			modeOption.setPosition(spritePositionX, -16);
			modeExit.setPosition(spritePositionX, -20);
		}

		// メニューの文字が画面外まで移動したらゲームメイン移行
		if(spritePositionX >= FADE_MENU ) {
			scrollNinja.setScreen(new GameMain(scrollNinja, nextStageNum));
		}
	}

	// 描画関係
	public void draw(float delta) {
		// クリア
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// スプライト描画
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();

		// 背景描画
		BackgroundManager.backgroundList.Draw(Background.FAR, true);
		BackgroundManager.backgroundList.Draw(Background.MAIN, true);

		// メニュー選択肢描画
		modeContinue.draw(spriteBatch);
		modeNewGame.draw(spriteBatch);
		modeLoadGame.draw(spriteBatch);
		modeNetwork.draw(spriteBatch);
		modeOption.draw(spriteBatch);
		modeExit.draw(spriteBatch);

		spriteBatch.end();
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}
}
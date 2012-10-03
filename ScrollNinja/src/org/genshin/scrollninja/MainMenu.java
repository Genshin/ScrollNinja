package org.genshin.scrollninja;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MainMenu implements Screen {
	private Game scrollNinja;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Texture texture;
	private Sprite cursor;
	private Sprite mode_GameRun;
	private Sprite mode_Settings;

	private float rotation;

	// カーソルの位置
	private int position;

	// コンストラクタ
	public MainMenu(Game game) {
		this.scrollNinja = game;

		// カメラ作成
		camera =
			new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// スプライトバッチ作成
		batch = new SpriteBatch();

		// カーソルの初期位置
		// 0:GameRun 1:Settings
		position = 0;

		// テクスチャ読み込み
		try {
			texture = new Texture(Gdx.files.internal("data/top01_test.png"));
		} catch (NullPointerException e) {
			System.out.println("ファイルがありません");
		} catch (GdxRuntimeException e) {
			System.out.println("テクスチャサイズは２の乗数にしてください");
		}
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// カーソルセット
		TextureRegion region = new TextureRegion(texture, 0, 200, 50, 50);
		cursor = new Sprite(region);
		cursor.setOrigin(cursor.getWidth() / 2, cursor.getHeight() / 2);
		cursor.setPosition(-200, 0);

		// 選択肢１セット
		region = new TextureRegion(texture, 0, 0, 256, 100);
		mode_GameRun = new Sprite(region);
		mode_GameRun.setPosition(-128, -25);

		// 選択肢２セット
		region = new TextureRegion(texture, 0, 100, 256, 100);
		mode_Settings = new Sprite(region);
		mode_Settings.setPosition(-128, -150);

		// おまけの回転
		rotation = 0;
	}

	// 更新
	public void update(float delta) {
		// キー入力
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			if (position == 1) {
				cursor.setPosition(-200, 0);
				position = 0;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			if (position == 0) {
				cursor.setPosition(-200, -125);
				position = 1;
			}
		}

		if (Gdx.input.isKeyPressed(Keys.ENTER)) {
			if (position == 0) {
				scrollNinja.setScreen(new GameScreen(scrollNinja));
				return;
			}
			if (position == 1) {
				scrollNinja.setScreen(new SettingsScreen(scrollNinja));
				return;
			}
		}
		/*
		Status.running(true);
		Status.setGameMode(Status.RunModes.MAIN_MENU.ordinal());
		while (Status.running()) {
			int mode = Status.getGameMode();

			if (mode == Status.RunModes.MAIN_MENU.ordinal()) {
				//MainMenu.show();
			} else if (mode == Status.RunModes.SETTINGS.ordinal()) {

			} else if (mode == Status.RunModes.GAME_INIT.ordinal()) {

			} else if (mode == Status.RunModes.GAME_RUN.ordinal()) {

			} else if (mode == Status.RunModes.SHUTDOWN.ordinal()) {

				return;
			}
		}
		*/
	}

	// 描画関係
	public void draw(float delta) {
		// クリア
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// スプライト描画
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		cursor.draw(batch);
		mode_GameRun.draw(batch);
		mode_Settings.draw(batch);
		batch.end();

		// おまけの回転
		rotation += 1;
		cursor.setRotation(rotation);
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
		batch.dispose();
		texture.dispose();
	}
}
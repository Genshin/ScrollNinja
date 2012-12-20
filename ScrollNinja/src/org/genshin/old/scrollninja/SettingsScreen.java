package org.genshin.old.scrollninja;

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
import com.badlogic.gdx.utils.GdxRuntimeException;

public class SettingsScreen implements Screen {
	private Game scrollNinja;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Texture texture;
	private Sprite backGround;

	// コンストラクタ
	public SettingsScreen(Game game) {
		this.scrollNinja = game;
		first();
	}

	// 最初のセッティング
	public void first() {
		// カメラ作成
		camera =
			new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// スプライトバッチ作成
		batch = new SpriteBatch();

		// テクスチャ読み込み
		try {
			texture = new Texture(Gdx.files.internal("data/old/back02_test.png"));
		} catch (NullPointerException e) {
			System.out.println("ファイルがありません");
		} catch (GdxRuntimeException e) {
			System.out.println("テクスチャサイズは２の乗数にしてください");
		}
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// カーソルセット
		TextureRegion region = new TextureRegion(texture, 0, 0, 1024, 1024);
		backGround = new Sprite(region);
		backGround.setPosition(-400, -700);
	}

	// 更新
	public void update(float delta) {
		// キー入力
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
		}
	}

	// 描画関係
	public void draw(float delta) {
		// クリア
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// スプライト描画
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		backGround.draw(batch);
		batch.end();
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
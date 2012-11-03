package org.genshin.scrollninja.object;

import org.genshin.scrollninja.BackgroundManager;
import org.genshin.scrollninja.FileOperation;
import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.Mouse;
import org.genshin.scrollninja.ScrollNinja;
import org.genshin.scrollninja.StructObjectManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class StageEditor implements Screen, InputProcessor {

	// 変数
	private  int 			stageNum;
	private  int			priority;
	private  int			count[] = new int[10];			// トリガー取得の為のカウント用変数
	private  Vector2		position = new Vector2( 0.0f, 0.0f);
	private  Box2DDebugRenderer	renderer = new Box2DDebugRenderer();
//	private  ItemWindow itemWindow = new ItemWindow();
//	private EditTable editTable = new EditTable();
//	private LayerWindow layerWindow = new LayerWindow();

	public StageEditor() {
		Gdx.input.setInputProcessor(this);
	}

	/**
	 * 初期化
	 * とりあえず全部生成
	 */
	public  void Init() {
		priority = 9;

		for(int i = 0; i < 10; i++ ) {
			count[i] = 0;
		}
		

//		itemWindow.Init();
//		editTable.Init();
	//	layerWindow.Init();
	}

	/**
	 * 更新
	 */
	private void Update() {
		Move();
		Mouse.Update();
		Priority();
//		editTable.Update();
//		layerWindow.Update();

		// キャラクター
		StructObjectManager.Update(priority);

		if( count[1] == 1 ) {
			FileOperation.Save();
		}
		if( count[2] == 1 ) {
			FileOperation.Load();
		}
		if( count[3] == 1 ) {
			for( int i = 0; i < StructObjectManager.GetListSize(); i ++ ) {
				if( StructObjectManager.GetStructObject(i).GetHold() ) {
					StructObjectManager.DeleteStructObject(i);
				}
			}
		}

//		System.out.println("マウスX:" + (Mouse.GetPosition().x * 0.1 - 64.0 ));
//		System.out.println("マウスY:" + (Mouse.GetPosition().y * 0.1 - 36.0 ));
//		System.out.println("て　き :" + EnemyManager.enemyList.get(0).GetPosition());
//		System.out.println("カメラX:" + GameMain.camera.position.x);
//		System.out.println("カメラY:" + GameMain.camera.position.y);
	}

	/**
	 * 描画
	 */
	private void Draw() {
		// 全部クリア
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		GameMain.spriteBatch.setProjectionMatrix(GameMain.camera.combined);		// プロジェクション行列のセット
		GameMain.spriteBatch.begin();									// 描画開始
		{
			BackgroundManager.backgroundList.Draw(Background.FAR);
			BackgroundManager.backgroundList.Draw(Background.MAIN);

			for( int j = 0; j <= priority; j ++ ) {
				for( int i = 0; i < StructObjectManager.GetListSize(); i ++ ) {
					if( StructObjectManager.GetStructObject(i).GetPriority() == j ) {
						StructObjectManager.GetStructObject(i).Draw();
					}
				}
			}
		}
		GameMain.spriteBatch.end();										// 描画終了
		renderer.render(GameMain.world, GameMain.camera.combined);
		GameMain.world.step(Gdx.graphics.getDeltaTime(), 20, 20);
	}

	/**
	 * カメラ移動
	 */
	private void Move() {
		GameMain.camera.position.set(position.x, position.y, 0);

		if (GameMain.camera.position.x < -(BackgroundManager.backgroundList.sprite.get(1).getWidth() * 0.5f - ScrollNinja.window.x * 0.5f) * ScrollNinja.scale) {
			GameMain.camera.position.x = -(BackgroundManager.backgroundList.sprite.get(1).getWidth() * 0.5f - ScrollNinja.window.x * 0.5f) * ScrollNinja.scale;
		}
		else if( Gdx.input.isKeyPressed(Keys.A) ) {
			position.x --;
		}
		if (GameMain.camera.position.x > (BackgroundManager.backgroundList.sprite.get(1).getWidth() * 0.5f - ScrollNinja.window.x * 0.5f) * ScrollNinja.scale) {
			GameMain.camera.position.x = (BackgroundManager.backgroundList.sprite.get(1).getWidth() * 0.5f - ScrollNinja.window.x * 0.5f) * ScrollNinja.scale;
		}
		else if( Gdx.input.isKeyPressed(Keys.D) ) {
			position.x ++;
		}

		if (GameMain.camera.position.y < -(1333 - ScrollNinja.window.y) * 0.5f * ScrollNinja.scale) {
			GameMain.camera.position.y = -(1333 - ScrollNinja.window.y) * 0.5f * ScrollNinja.scale;
		}
		else if( Gdx.input.isKeyPressed(Keys.S) ) {
			position.y --;
		}
		if (GameMain.camera.position.y > (1333 - ScrollNinja.window.y) * 0.5f * ScrollNinja.scale) {
			GameMain.camera.position.y = (1333 - ScrollNinja.window.y) * 0.5f * ScrollNinja.scale;
		}
		else if( Gdx.input.isKeyPressed(Keys.W) ) {
			position.y ++;
		}

		GameMain.camera.update();
	}

	/**
	 * ステージ遷移
	 */
	private void ChangeStage() {
		if( Gdx.input.isKeyPressed(Keys.NUM_1)) {
			stageNum = 1;
		}

		if( Gdx.input.isKeyPressed(Keys.NUM_2)) {
			stageNum = 2;
		}

		if( Gdx.input.isKeyPressed(Keys.NUM_3)) {
			stageNum = 3;
		}
	}

	/**
	 * 優先度の変更
	 */
	private void Priority() {
		boolean hold = false;

		for( int i = 0; i < StructObjectManager.GetListSize(); i ++ ) {
			hold = StructObjectManager.GetStructObject(i).GetHold();
			if( hold ) {
				return;
			}
		}

		if( !hold ) {
			if( Gdx.input.isKeyPressed(Keys.NUM_0)) {
				priority = 0;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_1)) {
				priority = 1;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_2)) {
				priority = 2;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_3)) {
				priority = 3;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_4)) {
				priority = 4;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_5)) {
				priority = 5;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_6)) {
				priority = 6;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_7)) {
				priority = 7;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_8)) {
				priority = 8;
			}
			if( Gdx.input.isKeyPressed(Keys.NUM_9)) {
				priority = 9;
			}
		}
	}

	@Override
	public void render(float delta) {
		Update();
		Draw();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

	@Override
	public boolean keyDown(int keycode) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println(character);
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO 自動生成されたメソッド・スタブ
//		System.out.println(position.x);
//		System.out.println(position.y);
		System.out.println("x:" + x);
		System.out.println("y:" + y);
		position.y = y;
		position.x = x;
//		GameMain.camera.update();
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}

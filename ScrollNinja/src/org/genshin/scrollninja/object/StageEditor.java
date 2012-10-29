package org.genshin.scrollninja.object;


import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.KeyListener;

import org.genshin.scrollninja.BackgroundManager;
import org.genshin.scrollninja.EditTable;
import org.genshin.scrollninja.EnemyManager;
import org.genshin.scrollninja.FileOperation;
import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.Keyboard;
import org.genshin.scrollninja.Mouse;
import org.genshin.scrollninja.ScrollNinja;
import org.genshin.scrollninja.StructObject;
import org.genshin.scrollninja.StructObjectManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class StageEditor implements Screen {

	// 変数
	private  int 			stageNum;
	private  int			priority;
	private  int			count[] = new int[10];			// トリガー取得の為のカウント用変数
	private  Vector2		position = new Vector2( 0.0f, 0.0f);
	private  Box2DDebugRenderer	renderer = new Box2DDebugRenderer();
	private  EditTable	editTable = new EditTable();
	private Keyboard keyboard = new Keyboard();
	private Container cont = new Container();
	
	public StageEditor() {
		cont.addKeyListener(keyboard);
		cont.setVisible(true);
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
		
		editTable.Init();
	}

	/**
	 * 更新
	 */
	private void Update() {
		Move();
		Mouse.Update();
		Priority();
		editTable.Update();

		// キャラクター
		StructObjectManager.Update(priority);

		//TODO		トリガー実装はよ
		if( Gdx.input.isKeyPressed(Keys.B)) {
			count[0] ++;
		}
		else {
			count[0] = 0;
		}

		if( Gdx.input.isKeyPressed(Keys.K)) {
			count[1] ++;
		}
		else {
			count[1] = 0;
		}
		
		if( Gdx.input.isKeyPressed(Keys.L)) {
			count[2] ++;
		}
		else {
			count[2] = 0;
		}
		
		if( Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
			count[3] ++;
		}
		else {
			count[3] = 0;
		}
		
		if( count[0] == 1 ) {
		}
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
	 * マウス処理
	 */
/*	private void Mouse() {
		if(Mouse.LeftClick()) {
			for( int i = 0; i < EnemyManager.normalEnemyList.size(); i++ ) {
				if( EnemyManager.enemyList.get(i).GetPosition().x - 1.6 < (Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x) &&
					EnemyManager.enemyList.get(i).GetPosition().x + 1.6 > (Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x) &&
					EnemyManager.enemyList.get(i).GetPosition().y - 2.4 < (GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 ) &&
					EnemyManager.enemyList.get(i).GetPosition().y + 2.4 > (GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 )) {
					EnemyManager.enemyList.get(i).SetPosition((float)((Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x)),(float)((GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 )));
				}
			}
		}
	}*/

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

}

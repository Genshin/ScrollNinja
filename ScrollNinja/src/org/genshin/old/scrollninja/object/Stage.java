

package org.genshin.old.scrollninja.object;

import java.util.ArrayList;

import org.genshin.engine.manager.RenderableManager;
import org.genshin.engine.manager.UpdatableManager;
import org.genshin.old.scrollninja.GameMain;
import org.genshin.old.scrollninja.object.StageDataList.StageData;
import org.genshin.old.scrollninja.object.item.Item;
import org.genshin.old.scrollninja.object.weapon.WeaponManager;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.object.gui.Cursor;
import org.genshin.scrollninja.render.CameraTranslater;
import org.genshin.scrollninja.utils.TextureFactory;
import org.genshin.scrollninja.utils.debug.DebugString;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class Stage implements StageBase {

	private Box2DDebugRenderer		renderer;		//
	private ArrayList<Item>			popItems;		//
	private ArrayList<Enemy>		popEnemys;		//

	private int						stageNum;		// ステージナンバー
	private StageData	 			stageData;		// ステージのデータ
	
	private Cursor cursor;
	private CameraTranslater cameraTranslater;

	private final UpdatableManager updatableManager = new UpdatableManager();
	private final RenderableManager renderableManager = new RenderableManager();
	
	private boolean prevInput;
	private boolean renderDebug = false;
	
	private Sprite screenEdgeSprite = null;
	private final Matrix4 tmpMatrix = new Matrix4(); 

//	private Sprite screenNoiseSprite = null;
	private Sprite logoSprite = null;
	private Sprite miniInputGuideSprite = null;
	private Sprite fullInputGuideSprite = null;
	private final SpriteBatch spriteBatch = new SpriteBatch(50);
	private boolean prevInputF1 = false;
	private boolean renderFullInputGuide = false;

	// コンストラクタ
	public Stage(int num){
		stageNum = num;
		stageData = StageDataList.lead(stageNum);

		renderer = new Box2DDebugRenderer();
		
		cursor = new Cursor(GameMain.camera, 2.0f * GlobalParam.INSTANCE.WORLD_SCALE);
		
		// 超　仮
		{
			final float worldScale = GlobalParam.INSTANCE.WORLD_SCALE;
			screenEdgeSprite = new Sprite( TextureFactory.getInstance().get("data/textures/gui/screen_edge.png") );
			logoSprite = new Sprite( TextureFactory.getInstance().get("data/textures/alpha/logo.png") );
			miniInputGuideSprite = new Sprite( TextureFactory.getInstance().get("data/textures/alpha/input_guide_mini.png") );
			fullInputGuideSprite = new Sprite( TextureFactory.getInstance().get("data/textures/alpha/input_guide_full.png") );
			logoSprite.setPosition(1280.0f - logoSprite.getWidth(), 0.0f);
			spriteBatch.setProjectionMatrix( tmpMatrix.setToOrtho2D(0, 0, 1280, 720) );
		}

		GlobalParam.INSTANCE.currentUpdatableManager = updatableManager;
		GlobalParam.INSTANCE.currentRenderableManager = renderableManager;
	}

	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update(float deltaTime) {
		DebugString.add("Update Count : " + updatableManager.size());
		DebugString.add("Render Count : " + renderableManager.size());
		
		cursor.update(deltaTime);
		CollisionDetector.HitTest();			// これ最初にやってほしいかも？
		EnemyManager.Update(deltaTime);
		WeaponManager.Update(deltaTime);
		PlayerManager.Update(deltaTime);
		ItemManager.Update(deltaTime);
		cameraTranslater.update(deltaTime);
		BackgroundManager.backgroundList.update(deltaTime);
		updatableManager.update(deltaTime);

		// TODO 今だけリポップ
//		if (EnemyManager.enemyList.get(0) == null && EnemyManager.enemyList.get(1) == null) {
//			EffectManager.enemyEffectList = new ArrayList<Effect>();
//			EnemyManager.dispose();
//		}
//
//		// TODO 今だけリポップ
//		if( EnemyManager.enemyList.size() == 0) {
//			for(StageData.EnemyData enemy : stageData.enemyData)
//			{
//				EnemyManager.CreateEnemy(enemy.type, enemy.position);
//			}
//		}

//		GameMain.playerInfo.update();
		
		
		// TODO 最終的には消すハズ
		if( Gdx.input.isKeyPressed(Keys.NUM_0) )
		{
			if(!prevInput)
			{
				renderDebug = !renderDebug;
			}
			prevInput = true;
		}
		else
		{
			prevInput = false;
		}
		
		if( Gdx.input.isKeyPressed(Keys.F1) )
		{
			if(!prevInputF1)
			{
				renderFullInputGuide = !renderFullInputGuide;
			}
			prevInputF1 = true;
		}
		else
		{
			prevInputF1 = false;
		}
	}

	//************************************************************
	// Draw
	// 描画処理まとめ
	//************************************************************
	public void Draw() {
		GameMain.spriteBatch.setProjectionMatrix(GameMain.camera.combined);		// プロジェクション行列のセット
		GameMain.spriteBatch.begin();											// 描画開始
		{
			BackgroundManager.backgroundList.Draw(0);
			BackgroundManager.backgroundList.Draw(1);
			BackgroundManager.backgroundList.Draw(2);
			BackgroundManager.backgroundList.Draw(3);
			BackgroundManager.backgroundList.Draw(4);
			StageObjectManager.Draw();
			renderableManager.render();
			PlayerManager.Draw();
			EnemyManager.Draw();
			EffectManager.Draw();
			ItemManager.Draw();
			BackgroundManager.backgroundList.Draw(5);
			BackgroundManager.backgroundList.Draw(6);
			BackgroundManager.backgroundList.Draw(7);
			BackgroundManager.backgroundList.Draw(8);
//			GameMain.playerInfo.Draw();
			cursor.render();
			
			GameMain.spriteBatch.setProjectionMatrix(tmpMatrix);
			screenEdgeSprite.draw(GameMain.spriteBatch);
		}
		GameMain.spriteBatch.end();										// 描画終了

		spriteBatch.begin();
		logoSprite.draw(spriteBatch);
		miniInputGuideSprite.draw(spriteBatch);
		if(renderFullInputGuide)	fullInputGuideSprite.draw(spriteBatch);
		spriteBatch.end();

		// TODO リリース前にこの処理をクリア直後に持ってくる
		if(renderDebug)	renderer.render(GameMain.world, GameMain.camera.combined);
		GameMain.world.step(Gdx.graphics.getDeltaTime(), 20, 20);

		/**
		 * ↑rendererの処理を前に持っていくと位置がずれることがあるので
		 * 微調整が必要な場合は一度確認した方がよさそうです。
		 */
	}

	//************************************************************
	// PopEnemy
	// 敵の出現タイミングの設定
	//************************************************************
	public void PopEnemy() {
/*		if( PlayerManager.GetPlayer("プレイヤー").GetPosition().x > 200 ) {
			EnemyManager.CreateEnemy(Enemy.NORMAL, 20.0f, 30.0f);

		}*/
	}

	// アイテムポップ
	public void popItem() {

	}

	public void moveBackground() {

	}

	public PlayerNinja spawnPlayer(PlayerNinja player) {
		return player;
	}

	public int timeCount(int nowTime) {
		// 制限時間 or 経過時間加算
		nowTime += 1;

		return nowTime;
	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Stage GetStage() { return this; }
	public int GetStageNum(){ return stageNum; }

	@Override
	public void Init() {
		PlayerManager.CreatePlayer(stageData.startPosition, cursor);
		// TODO 後でステージオブジェクトリスト追加
//		StageObjectManager.CreateStageObject(StageObject.ROCK, 200.0f, 38.0f);
		for(StageData.EnemyData enemy : stageData.enemyData)
		{
			EnemyManager.CreateEnemy(enemy.type, enemy.position);
		}
		
		cameraTranslater = new CameraTranslater(GameMain.camera);
		cameraTranslater.addTargetObject(cursor);
		cameraTranslater.addTargetObject(PlayerManager.GetPlayer(0));

		cameraTranslater.setTranslateArea(0.0f, 0.0f, stageData.size.x, stageData.size.y);
	}

	@Override
	public void Release() {
	}

	/**************************************************
	 * dispose()
	 * 解放処理
	 * 他の画面の移動する場合は行わなければ描画がおかしくなる
	 * TODO 検証不足につき他で不具合が出るかも…
	 **************************************************/
	public void dispose() {
		EffectManager.dispose();
		WeaponManager.dispose();
		PlayerManager.dispose();
		EnemyManager.dispose();
		BackgroundManager.dispose();
		ItemManager.dispose();
		StageObjectManager.dispose();

		renderer.dispose();
	}

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************

}


package org.genshin.scrollninja.object;

import java.util.ArrayList;

import org.genshin.scrollninja.BackgroundManager;
import org.genshin.scrollninja.EffectManager;
import org.genshin.scrollninja.EnemyManager;
import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.ItemManager;
import org.genshin.scrollninja.PlayerManager;
import org.genshin.scrollninja.ScrollNinja;
import org.genshin.scrollninja.StageObjectManager;
import org.genshin.scrollninja.WeaponManager;
import org.genshin.scrollninja.object.StageDataList.StageData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class Stage implements StageBase {

	private Box2DDebugRenderer		renderer;		//
	private ArrayList<Item>			popItems;		//
	private ArrayList<Enemy>		popEnemys;		//
	private ArrayList<Weapon>		popWeapons;

	private int						stageNum;		// ステージナンバー
	private StageData	 			stageData;		// ステージのデータ

	// コンストラクタ
	public Stage(int num){
		stageNum = num;
		stageData = StageDataList.lead(stageNum);

		renderer = new Box2DDebugRenderer();
	}

	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update() {
		CollisionDetector.HitTest();			// これ最初にやってほしいかも？
		EnemyManager.Update();
		BackgroundManager.backgroundList.update();
		WeaponManager.Update();
		PlayerManager.Update();
		ItemManager.Update();

		// TODO 今だけリポップ
		if (EnemyManager.enemyList.get(0) == null && EnemyManager.enemyList.get(1) == null) {
			EffectManager.enemyEffectList = new ArrayList<Effect>();
			EnemyManager.dispose();
		}

		// TODO 今だけリポップ
		if( EnemyManager.enemyList.size() == 0) {
			for (int i = 0; i < stageData.enemyType.size(); i++) {
				for (int j = 0; j < stageData.enemyNum.get(i); j++) {
					EnemyManager.CreateEnemy(stageData.enemyType.get(i),
										 	 stageData.enemyPosition.get((i+1)*(j+1)-1));
				}
			}
		}

		updateCamera();
		GameMain.playerInfo.update();
	}

	//************************************************************
	// Draw
	// 描画処理まとめ
	//************************************************************
	public void Draw() {
		// 全部クリア
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		GameMain.spriteBatch.setProjectionMatrix(GameMain.camera.combined);		// プロジェクション行列のセット
		GameMain.spriteBatch.begin();											// 描画開始
		{
			BackgroundManager.backgroundList.Draw(0);
			BackgroundManager.backgroundList.Draw(1);
			StageObjectManager.Draw();
			PlayerManager.Draw();
			EnemyManager.Draw();
			EffectManager.Draw();
			ItemManager.Draw();
			BackgroundManager.backgroundList.Draw(2);
			GameMain.playerInfo.Draw();
		}
		GameMain.spriteBatch.end();										// 描画終了

		// TODO リリース前にこの処理をクリア直後に持ってくる
		renderer.render(GameMain.world, GameMain.camera.combined);
		GameMain.world.step(Gdx.graphics.getDeltaTime(), 20, 20);

		/**
		 * ↑rendererの処理を前に持っていくと位置がずれることがあるので
		 * 微調整が必要な場合は一度確認した方がよさそうです。
		 */
	}

	//************************************************************
	// updateCamera
	// カメラ情報更新
	//************************************************************
	public void updateCamera() {
		// カメラはプレイヤーに追随
		GameMain.camera.position.set(PlayerManager.GetPlayer(0).body.getPosition().x,
									 PlayerManager.GetPlayer(0).body.getPosition().y, 0);

		// カメラの移動制限
		if (GameMain.camera.position.x <
				-(BackgroundManager.backgroundList.sprite.get(Background.MAIN).getWidth() * 0.5
														- ScrollNinja.window.x * 0.5f) * ScrollNinja.scale)
			GameMain.camera.position.x =
				-(BackgroundManager.backgroundList.sprite.get(Background.MAIN).getWidth() * 0.5f
														- ScrollNinja.window.x * 0.5f) * ScrollNinja.scale;
		if (GameMain.camera.position.x >
				(BackgroundManager.backgroundList.sprite.get(Background.MAIN).getWidth() * 0.5
														- ScrollNinja.window.x * 0.5f) * ScrollNinja.scale)
			GameMain.camera.position.x =
				(BackgroundManager.backgroundList.sprite.get(Background.MAIN).getWidth() * 0.5f
														- ScrollNinja.window.x * 0.5f) * ScrollNinja.scale;

		if (GameMain.camera.position.y < -(stageData.backgroundSize.get(Background.MAIN).y
												- ScrollNinja.window.y) * 0.5f * ScrollNinja.scale)
			GameMain.camera.position.y = -(stageData.backgroundSize.get(Background.MAIN).y
												- ScrollNinja.window.y) * 0.5f * ScrollNinja.scale + 1;
		if (GameMain.camera.position.y > (stageData.backgroundSize.get(Background.MAIN).y
												- ScrollNinja.window.y) * 0.5f * ScrollNinja.scale)
			GameMain.camera.position.y = (stageData.backgroundSize.get(Background.MAIN).y
												- ScrollNinja.window.y) * 0.5f * ScrollNinja.scale - 1;

		GameMain.camera.update();
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

	public Player spawnPlayer(Player player) {
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
		PlayerManager.CreatePlayer(stageData.playerPosition);
		// TODO 後でステージオブジェクトリスト追加
		StageObjectManager.CreateStageObject(StageObject.ROCK, 0.0f, 0.0f);
		for (int i = 0; i < stageData.enemyType.size(); i++) {
			for (int j = 0; j < stageData.enemyNum.get(i); j++) {
				EnemyManager.CreateEnemy(stageData.enemyType.get(i),
										 stageData.enemyPosition.get((i+1)*(j+1)-1));
			}
		}
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
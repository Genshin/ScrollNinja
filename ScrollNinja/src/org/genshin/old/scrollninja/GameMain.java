package org.genshin.old.scrollninja;

import org.genshin.old.scrollninja.object.BackgroundManager;
import org.genshin.old.scrollninja.object.Stage;
import org.genshin.old.scrollninja.object.StageManager;
import org.genshin.scrollninja.GlobalParam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

// 制作メモ
// 10/5 制作開始
// 10/6 とりあえず表示まで。シングルトンを全部モノステートに。
// 10/8 コメント追加
// 10/10 仕様変更

// *メモ*
// stageってのがシーンの代わりです。
// シーン（ステージ）を遷移させたい時は、Stage型の変数を宣言してnew(world)して
// StageManager.StageTrance(変数名)で遷移します。
// 初期化処理は今はコンストラクタでやってますがあとで追加していきます。

public class GameMain implements Screen{
	private Game							scrollNinja;
	private Stage 							stage;			// ステージ
	public static World					world;			// ワールド
	public static OrthographicCamera		camera;			// カメラ
	public static SpriteBatch				spriteBatch;	// スプライトバッチ

	private int							stageNum;		// ステージナンバー
	
	private boolean paused = false;
	
	
	/**
	 * コンストラクタ
	 * @param game	ScrollNinja
	 * @param num	ステージナンバー
	 */
	public GameMain(Game game, int num) {
		scrollNinja		= game;
		// TODO 重力は調整必要あり
		world				= new World(new Vector2(0, GlobalParam.INSTANCE.GRAVITY), true);

		// TODO 画面サイズによって数値を変更
		camera				= new OrthographicCamera(GlobalParam.INSTANCE.CLIENT_WIDTH * GlobalParam.INSTANCE.WORLD_SCALE,
													 GlobalParam.INSTANCE.CLIENT_HEIGHT * GlobalParam.INSTANCE.WORLD_SCALE);
		spriteBatch 		= new SpriteBatch();
		stageNum			= num;
		stage				= new Stage(stageNum);

		BackgroundManager.CreateBackground(stageNum, true);
		StageManager.ChangeStage(stage);
		StageManager.GetNowStage().Init();
	}
	
	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
//	public World GetWorld(){ return world; }
//	public OrthographicCamera GetCamera(){ return camera; }
//	public SpriteBatch GetSpriteBatch(){ return spriteBatch; }

	//************************************************************
	// render
	// メイン処理
	//************************************************************
	@Override
	public void render(float delta) {
		if(!paused)
		{
			StageManager.Update(delta);
		}
		StageManager.Draw();
	}

	@Override
	public void resize(int width, int height)
	{
		//---- アスペクト比を計算する
		final float windowAspectRatio = (float)width / height;
		final float viewportAspectRatio = camera.viewportWidth / camera.viewportHeight;
		
		//---- アスペクト比が等しくない場合、ビューポートを調整する
		if( Math.abs(windowAspectRatio - viewportAspectRatio) > 1.0e-6 )
		{
			int newWidth = width;
			int newHeight = height;
			
			// ウィンドウの横幅が広い
			if(windowAspectRatio > viewportAspectRatio)
			{
				newWidth = (int)(height * viewportAspectRatio);
			}
			// ウィンドウの縦幅が広い
			else
			{
				newHeight = (int)(width / viewportAspectRatio);
			}
			
			// ビューポートを設定する
			Gdx.gl.glViewport((width-newWidth)/2, (height-newHeight)/2, newWidth, newHeight);
		}
	}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause()
	{
		paused = true;
	}

	@Override
	public void resume()
	{
		paused = false;
	}

	@Override
	public void dispose() {
		world.dispose();
		spriteBatch.dispose();
		stage = null;
	}
}
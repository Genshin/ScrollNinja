package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

// メモ
// このクラスは、背景を読み込んで表示するところ。
// ステージの当たり判定もこのクラスが持っています。
// その他の敵の出現位置などの細かな情報はステージクラスが持っています

// 10/9 背景移動追加、カメラ座標のゲッター追加

//========================================
// クラス宣言
//========================================
public class Background extends ObJectBase {
	//========================================
	// 定数宣言
	// spriteの配列
	//========================================
	private final static int	FAR				= 0;
	private final static int	MAIN			= 1;
	private final static int	NEAR			= 2;

	// 変数宣言
	private float				zIndex;								// Zインデックス
	private Vector2				playerPos;
	private int					bgNum;

	/**
	 *  コンストラクタ
	 */
	public Background(){}
	public Background(int num) {
		sprite = new ArrayList<Sprite>();
		sensor = new ArrayList<Fixture>();
		playerPos = new Vector2(0.0f, 0.0f);
		

		bgNum = num;
		switch(num){
		case 0:
			LoadTexture();
			break;
		case 1:
			LoadTexture();
			createBody();
			break;
		}
	}

	/**************************************************
	 * @Override
	 * @param i		スプライト番号
	 * @param flag	とりあえず付けときました＾ｑ＾
	 * 
	 * 描画処理
	 ***************************************************/
	public void Draw(int i, boolean flag) {
		sprite.get(i).draw(MainMenu.spriteBatch);
	}

	/**************************************************
	 * @Override
	 * @param i		スプライト番号
	 * 
	 * 描画処理
	 ***************************************************/
	public void Draw(int i) {
		sprite.get(i).draw(GameMain.spriteBatch);
	}

	/**************************************************
	 * テクスチャ読み込み、スプライトセット
	 ***************************************************/
	public void LoadTexture() {
		// 奥から作成
		// 奥
		Texture texture = new Texture(Gdx.files.internal("data/stage_far.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		sprite.add(new Sprite(tmpRegion));
		sprite.get(FAR).setPosition(-sprite.get(FAR).getWidth() * 0.5f, -sprite.get(FAR).getHeight() * 0.5f);
		if(sprite.get(FAR).getWidth() > ScrollNinja.window.x )
			sprite.get(FAR).setScale(0.11f);
		else
			sprite.get(FAR).setScale(0.1f * (ScrollNinja.window.x / sprite.get(FAR).getWidth()) * 1.05f);

		// メインステージ
		texture = new Texture(Gdx.files.internal("data/stage_main.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmpRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		sprite.add(new Sprite(tmpRegion));
		sprite.get(MAIN).setPosition(-sprite.get(MAIN).getWidth() * 0.5f, -sprite.get(MAIN).getHeight() * 0.5f);
		sprite.get(MAIN).setScale(0.1f);

		// 手前
		texture = new Texture(Gdx.files.internal("data/stage_near.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmpRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		sprite.add(new Sprite(tmpRegion));
		// 41.05は((メインテクスチャ1333)-(手前テクスチャ256*scale2倍) ÷　（空白は上下あるから）2) ?
		sprite.get(NEAR).setPosition(-sprite.get(NEAR).getWidth() * 0.5f,
										-sprite.get(NEAR).getHeight() * 0.5f -41.05f);
		sprite.get(NEAR).setScale(0.25f, 0.2f);
	}

	/**************************************************
	 * 当たり判定作成
	 ***************************************************/
	public void createBody() {
		// 当たり判定作成用ファイル読み込み
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));

		// ボディタイプ設定
		BodyDef bd	= new BodyDef();
		bd.type		= BodyType.StaticBody;		// 動かない物体
		// -357.5は（2048-1333）÷２　（画像サイズ-実際に描かれているサイズ）=空白　空白は上下にあるので÷２
		bd.position.set(-sprite.get(1).getWidth() * 0.5f * 0.1f,
								(-sprite.get(1).getHeight() * 0.5f -357.5f) * 0.1f);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 1000;		// 密度
		fd.friction		= 100;		// 摩擦
		fd.restitution	= 0;		// 反発係数

		// ボディ作成
		body = GameMain.world.createBody(bd);
		loader.attachFixture( body, "bgTest", fd, sprite.get(1).getWidth() * 0.1f);

		for(int i = 0; i < body.getFixtureList().size(); i ++) {
			sensor.add(body.getFixtureList().get(i));
			sensor.get(i).setUserData(this);
		}
	}

	/**************************************************
	 * 更新処理
	 ***************************************************/
	public void update() {
		// プレイヤーの座標を代入
		playerPos = PlayerManager.GetPlayer(0).body.getPosition();

		// 近景
		if (playerPos.x > -(sprite.get(MAIN).getWidth() - ScrollNinja.window.x) * 0.5 * 0.1
							&& playerPos.x < (sprite.get(MAIN).getWidth() - ScrollNinja.window.x) * 0.5 * 0.1)
				sprite.get(NEAR).setPosition
							(-sprite.get(NEAR).getWidth() * 0.5f - playerPos.x * 1.5f, sprite.get(NEAR).getY());
		// 1333は実際の画像のサイズ　20は適当
		// 11.05はLoadTexture時の41.05-画面サイズ600÷2 ?
		if (playerPos.y > -(1333 - ScrollNinja.window.y) * 0.5  * 0.1 && playerPos.y < 20)
			sprite.get(NEAR).setPosition
							(sprite.get(NEAR).getX(), -sprite.get(NEAR).getHeight() * 0.5f -11.05f + playerPos.y);

		// 遠景
		if (playerPos.x > -(sprite.get(MAIN).getWidth() - ScrollNinja.window.x) * 0.5 * 0.1
							&& playerPos.x < (sprite.get(MAIN).getWidth() - ScrollNinja.window.x) * 0.5 * 0.1)
			sprite.get(FAR).setPosition(playerPos.x - (sprite.get(FAR).getWidth() * 0.5f) + (playerPos.x * -0.05f),
									sprite.get(FAR).getY());
		// 1333は実際のサイズ
		if (playerPos.y > -(sprite.get(FAR).getHeight() - ScrollNinja.window.y) * 0.5 * 0.1
													&& playerPos.y < (1333 - ScrollNinja.window.y) * 0.5 * 0.1)
			sprite.get(FAR).setPosition(sprite.get(FAR).getX(),
									playerPos.y - (sprite.get(FAR).getHeight() * 0.5f) + (playerPos.y * -0.15f));
	}
	
	@Override
	public void collisionDispatch(ObJectBase obj, Contact contact) {
		obj.collisionNotify(this, contact);
	}
	
	@Override
	public void collisionNotify(Player obj, Contact contact) {
		
	}
	
	@Override
	public void collisionNotify(Enemy obj, Contact contact){}
	
	@Override
	public void collisionNotify(Effect obj, Contact contact){}
	
	@Override
	public void collisionNotify(Item obj, Contact contact){}
	
	@Override
	public void collisionNotify(StageObject obj, Contact contact){}
	
	@Override
	public void collisionNotify(Weapon obj, Contact contact){}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
/*	public Sprite GetSprite(int i) { return sprite.get(i); }
	public Body GetBody() { return body; }
	public Fixture GetSensor(int i) { return sensor.get(i); }*/
	//public Vector2 GetCamPos() { return cameraPos; }
	public Background GetBackground(){ return this; }
	public int GetBackgroundNum(){ return bgNum; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
/*	public static void SetSprite(Sprite sp) { sprite.add(sp); }
	public static void SetBody(Body bd) { body = bd; }
	public static void SetFixture(Fixture ss){ sensor.add(ss); }
*/
}
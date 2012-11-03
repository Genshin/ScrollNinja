package org.genshin.scrollninja;

import java.util.ArrayList;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class StructObject {

	// プレイヤー
	public static final int			PLAYER			= 0;

	// アイテム
	public static final int			ONIGIRI_ITEM	= 10;
	public static final int			OKANE_ITEM		= 11;

	// 敵
	public static final int			NORMAL_ENEMY	= 100;
	public static final int			RARE_ENEMY		= 101;
	public static final int			AUTO_ENEMY		= 102;

	// ステージオブジェクト
	public static final int			ROCK_OBJECT		= 1;
	public static final int			ROCK2_OBJECT	= 1001;

	/**
	 * テキストファイルに吐き出す情報
	 */
	public int		type;			/* オブジェクトの種類
	 								 * ０				プレイヤー
	 								 * １０〜９９			アイテム
	 								 * １００〜９９９		敵
	 								 * １０００〜			ステージオブジェクト
	 								 */
	public float	positionX;		// 保存用座標Ｘ
	public float	positionY;		// 保存用座標Ｙ
	public Integer	priority;		// 優先度（数値が低い方が奥）

	/**
	 * エディタで使うもの
	 */
	public Vector2	position;		// 表示用座標
	public boolean	hold;
	public Vector2 size;
	public ArrayList<Sprite> sprite;
	public Body body;

	/**
	 * コンストラクタ
	 */
	public StructObject(int Type) {
		sprite = new ArrayList<Sprite>();
		position = new Vector2(0.0f, 0.0f);
		size = new Vector2(0.0f, 0.0f);
		type = 1;		// とりあえず
		positionX = 0.0f;
		positionY = 0.0f;
		priority = 0;
		hold = false;

		// スプライト読み込み
		Create();
	}

	/**
	 * コンストラクタ
	 */
	public StructObject(int Type, float x, float y, int p) {
		sprite = new ArrayList<Sprite>();
		position = new Vector2(0.0f, 0.0f);
		size = new Vector2(0.0f, 0.0f);
		type = Type;
		positionX = x;
		positionY = y;
		priority = p;
		hold = false;

		// スプライト読み込み
		Create();
	}

	/**
	 * ホールド情報取得
	 */
	public boolean GetHold() { return hold; }
	/**
	 * 優先度取得　
	 */
	public Integer GetPriority(){ return priority; }

	/**
	 * 更新
	 */
	public void Update(int p) {
		position = body.getPosition();					// 現在位置の更新

		Hold(p);		// ホールド判定
		Priority();		// 優先度の設定
		Move();			// 動く
	}

	/**
	 * 描画
	 */
	public void Draw() {
		SpriteBatch sb = GameMain.spriteBatch;
		Vector2 pos = body.getPosition();
		float rot = (float) Math.toDegrees(body.getAngle());

		int count = sprite.size();
		for (int i = 0; i < count; ++i)
		{
			Sprite current = sprite.get(i);
			// 座標・回転
			current.setPosition(pos.x - current.getOriginX(), pos.y - current.getOriginY());
			current.setRotation(rot);
			// 描画
			current.draw(sb);
		}
	}

	/**
	 * 解放処理
	 */
	public void Release() {
		GameMain.world.destroyBody(body);
		body = null;
		sprite.clear();
	}

	/**
	 * 動かす
	 */
	public void Move() {
		if( hold ) {
			positionX = (float)( Mouse.GetPosition().x * 0.1 - 64.0 );
			positionY = (float)( Mouse.GetPosition().y * 0.1 - 36.0 );
			position.x = (float)((Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x));
			position.y = (float)((GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 ));
			body.setTransform(position.x,position.y,0);
			for( int i = 0; i < sprite.size(); i ++ ) {
				sprite.get(i).setPosition(position.x, position.y);
			}
		}
	}

	/**
	 * ホールド設定
	 */
	public void Hold(int p) {
		// 左クリックでオブジェクトを掴む
		if( Mouse.LeftClick() ) {
			if( p >= priority ) {
				if( position.x - size.x < (Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x) && position.x + size.x > (Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x) &&
					position.y - size.y < (GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 ) && position.y + size.y > (GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 )) {

					for( int i = 0; i < StructObjectManager.GetListSize(); i ++ ) {
						if( StructObjectManager.GetStructObject(i).GetHold() ) {
							return;
						}
					}
					hold = true;
				}
				else {
					hold = false;
				}
			}
			else {
				hold = false;
			}
		}

		// とりあえず右クリックで解放
		if( Mouse.RightClick() ) {
			hold = false;
		}
	}

	/**
	 * 優先度の設定
	 */
	public void Priority() {
		if( hold ) {
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

	/**
	 * 生成
	 */
	public void Create() {
		switch(type) {
		case NORMAL_ENEMY:
			CreateEnemy();
			break;
		case ROCK_OBJECT:
			CreateStageObject();
			break;
		}
	}

	/**
	 * ステージオブジェクト生成
	 */
	public void CreateStageObject() {
		// Bodyのタイプを設定 Staticは動かない物体
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		body = GameMain.world.createBody(bd);
		body.setTransform(positionX, positionY, 0);

		Texture texture = new Texture(Gdx.files.internal("data/stage_object.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 128, 256, 256);
		sprite.add(new Sprite(tmpRegion));
		sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
		sprite.get(0).setScale(ScrollNinja.scale);

		size.x = 5.0f;
		size.y = 6.0f;
	}

	/**
	 * 敵生成
	 */
	public void CreateEnemy() {
		// Body作成
		BodyDef bd	= new BodyDef();
		bd.type	= BodyType.StaticBody;
		body = GameMain.world.createBody(bd);
		body.setTransform(positionX, positionY, 0);			// 最初の位置

		Texture texture = new Texture(Gdx.files.internal("data/enemy.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);

		// スプライトに反映
		sprite.add(new Sprite(region));
		sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
		sprite.get(0).setScale(ScrollNinja.scale);

		// 当たり判定のサイズ
		size.x = 1.6f;
		size.y = 2.4f;
	}
}

package org.genshin.scrollninja.object;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.ScrollNinja;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

// TODO マップエディターで作成したのち、座標などを読み出せるように
//========================================
// クラス宣言
//========================================
public class StageObject extends ObjectBase {
	public static final int ROCK			= 0;
	public static final int HOUSE			= 1;

	private int			type;			// タイプ
	private int			number;			// 管理番号
	private Vector2		position;		// 座標

	// コンストラクタ
	public StageObject(int Type, int num, Vector2 pos) {
		sprite		= new ArrayList<Sprite>();
		sensor		= new ArrayList<Fixture>();

		number		= num;
		type		= Type;
		position	= new Vector2(pos);

		Create();
	}
	public StageObject(int Type, int num, float x, float y) {
		sprite		= new ArrayList<Sprite>();
		sensor		= new ArrayList<Fixture>();

		number		= num;
		type		= Type;
		position	= new Vector2(x,y);

		Create();
	}

	public void Create() {
		switch(type) {
		case ROCK:
			Texture texture = new Texture(Gdx.files.internal("data/stage_object.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion tmpRegion = new TextureRegion(texture, 0, 128, 256, 256);
			sprite.add(new Sprite(tmpRegion));
			// TODO テクスチャとボディの位置関係がおかしい…
			sprite.get(0).setOrigin(0, 0);
			//sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
			sprite.get(0).setScale(ScrollNinja.scale);

			// 当たり判定読み込み
			BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/stageObject.json"));

			// Bodyのタイプを設定 Staticは動かない物体
			BodyDef bd = new BodyDef();
			bd.type = BodyType.StaticBody;

			// Bodyの設定を設定
			FixtureDef fd	= new FixtureDef();
			fd.density		= 1000;				// 密度
			fd.friction		= 0;				// 摩擦
			fd.restitution	= 0;				// 反発係数

			body = GameMain.world.createBody(bd);
			// body.setTransform(0, 0, 0);

			// 各種設定を適用。引数は　Body、JSON中身のどのデータを使うか、FixtureDef、サイズ
			loader.attachFixture(body, "gravestone", fd, texture.getWidth() * ScrollNinja.scale);

			for(int i = 0; i < body.getFixtureList().size(); i ++) {
				sensor.add(body.getFixtureList().get(i));
				sensor.get(i).setUserData(this);
			}

			// TODO テスト中でいれているだけ
			body.setTransform(0, -58, (float)Math.toRadians(20));

			break;

		case HOUSE:
			break;
		}
	}

	@Override
	public void collisionDispatch(ObjectBase obj, Contact contact) {
		obj.collisionNotify(this, contact);
	}

	@Override
	public void collisionNotify(Background obj, Contact contact){}

	@Override
	public void collisionNotify(Player obj, Contact contact){}

	@Override
	public void collisionNotify(Enemy obj, Contact contact){}

	@Override
	public void collisionNotify(Effect obj, Contact contact){}

	@Override
	public void collisionNotify(Item obj, Contact contact){}

	@Override
	public void collisionNotify(StageObject obj, Contact contact){}

	@Override
	public void collisionNotify(WeaponBase obj, Contact contact){}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public StageObject GetStageObject() { return this; }
	public int GetType(){ return type; }
	public int GetNum(){ return number; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
	public void SetNum(int num){ number = num; }

}

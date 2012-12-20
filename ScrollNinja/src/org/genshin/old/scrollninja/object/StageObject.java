package org.genshin.old.scrollninja.object;

//========================================
// インポート
//========================================
import org.genshin.old.scrollninja.GameMain;
import org.genshin.old.scrollninja.object.item.Item;
import org.genshin.old.scrollninja.object.weapon.AbstractWeapon;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;

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
import com.badlogic.gdx.physics.box2d.FixtureDef;

// TODO マップエディターで作成したのち、座標などを読み出せるように
//========================================
// クラス宣言
//========================================
public class StageObject extends AbstractCollisionObject {
	public static final int ROCK			= 0;
	public static final int HOUSE			= 1;

	private int			type;			// タイプ
	private int			number;			// 管理番号
	private Vector2		position;		// 座標

	// コンストラクタ
	public StageObject(int Type, int num, Vector2 pos) {
		number		= num;
		type		= Type;
		position	= new Vector2(pos);

		Create();
	}
	public StageObject(int Type, int num, float x, float y) {
		number		= num;
		type		= Type;
		position	= new Vector2(x,y);

		Create();
	}

	public void Create() {
		switch(type) {
		case ROCK:
			Texture texture = new Texture(Gdx.files.internal("data/old/stage_object.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion tmpRegion = new TextureRegion(texture, 0, 128, 256, 256);
			sprites.add(new Sprite(tmpRegion));
			// TODO テクスチャとボディの位置関係がおかしい…
			sprites.get(0).setOrigin(0, 0);
			//sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
			sprites.get(0).setScale(GlobalParam.INSTANCE.WORLD_SCALE);

			// 当たり判定読み込み
			BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/old/stageObject.json"));

			// Bodyのタイプを設定 Staticは動かない物体
			BodyDef bd = new BodyDef();
			bd.type = BodyType.StaticBody;

			// TODO テスト中でいれているだけ
			bd.position.set(position);
			bd.angle = (float)Math.toRadians(20);
			
			createBody(GameMain.world, bd);

			// Bodyの設定を設定
			FixtureDef fd	= new FixtureDef();
			fd.density		= 1000;				// 密度
			fd.friction		= 0;				// 摩擦
			fd.restitution	= 0;				// 反発係数
			
			createFixtureFromFile(fd, "data/old/stageObject.json", "gravestone", texture.getWidth() * GlobalParam.INSTANCE.WORLD_SCALE);

			break;

		case HOUSE:
			break;
		}
	}

	@Override
	public void dispatchCollision(AbstractCollisionObject object, Contact contact) {
		object.notifyCollision(this, contact);
	}

	@Override
	public void notifyCollision(Background obj, Contact contact){}

	@Override
	public void notifyCollision(PlayerNinja obj, Contact contact){}

	@Override
	public void notifyCollision(Enemy obj, Contact contact){}

	@Override
	public void notifyCollision(Effect obj, Contact contact){}

	@Override
	public void notifyCollision(Item obj, Contact contact){}

	@Override
	public void notifyCollision(StageObject obj, Contact contact){}

	@Override
	public void notifyCollision(AbstractWeapon obj, Contact contact){}

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

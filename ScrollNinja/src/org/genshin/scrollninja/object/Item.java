
package org.genshin.scrollninja.object;

import java.util.ArrayList;
import java.util.List;

import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.ItemManager;
import org.genshin.scrollninja.ScrollNinja;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

// *メモ*
// アイテム番号はマネージャで自動に割り振る
// 当たり判定が二重になっているとき（地面とプレイヤー同時HITとか）にうまく削除が出来ないので削除フラグ追加

// TODO これも別ファイルでデータリスト作って読み込めるようにするべきか
public class Item extends ObjectBase {

	//========================================
	// 定数宣言
	// アイテムの種類
	//========================================
	public final static int		ONIGIRI		= 0;
	public final static int		OKANE		= 1;

	private final static float JUMP_POWER	=  20.0f;	// ジャンプ加速度

	// 変数宣言
	private int 		number;				// アイテム番号
	private int 		type;				// アイテムの種類
	private int			survivalTime;		// 生存時間
	private boolean		appear;				// 出現フラグ
	private Vector2		position;			// 座標
	private Vector2 	velocity;			// 移動用速度
	private boolean		deleteFlag;			// 削除フラグ
	private boolean		groundJudge;

	// コンストラクタ
	public Item(int Type, int num, float x,  float y) {
		sprite			= new ArrayList<Sprite>();
		sensor			= new ArrayList<Fixture>();
		type			= Type;
		number			= num;
		position		= new Vector2(x,y);
		velocity		= new Vector2(0.0f, 0.0f);
		appear			= true;
		deleteFlag		= false;
		survivalTime	= 600;

		Create();
		sensor.get(0).setUserData(this);
	}

	//************************************************************
	// Create
	// アイテム生成
	//************************************************************
	private void Create() {
		switch(type) {
		case ONIGIRI:
			Texture texture = new Texture(Gdx.files.internal("data/item.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, 32, 32);
			sprite.add(new Sprite(tmpRegion));
			sprite.get(0).setPosition(-sprite.get(0).getWidth() * 0.5f, -sprite.get(0).getHeight() * 0.5f);
			sprite.get(0).setScale(ScrollNinja.scale);

			BodyDef def	= new BodyDef();
			def.type	= BodyType.DynamicBody;		// 動く物体
			body = GameMain.world.createBody(def);

			// 当たり判定の作成
			PolygonShape poly		= new PolygonShape();
			poly.setAsBox(1.0f, 1.0f);

			// ボディ設定
			FixtureDef fd	= new FixtureDef();
			fd.density		= 0;
			fd.friction		= 0;//100.0f;
			fd.restitution	= 0;
			fd.shape		= poly;

			sensor.add(body.createFixture(poly, 0));
			sensor.get(0).setSensor(true);		// 物理シミュレーションの影響を受けない
			poly.dispose();
			body.setGravityScale(0.0f);			// 重力無視
			body.setBullet(true);			// すり抜け防止
			body.setFixedRotation(true);	// シミュレーションでの自動回転をしない
			body.setTransform(position, 0);		// 初期位置
			body.setLinearVelocity(0.0f, 0.0f);
			break;
		}
	}

	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update() {

		System.out.println(survivalTime);

		survivalTime --;			// 生存時間減少

		if( survivalTime <= 0 ) {
			deleteFlag = true;
		}

		if(deleteFlag) {
			ItemManager.DeleteItem(this);
			return;
		}

		position = body.getPosition();
		body.setTransform(position ,0);

		sprite.get(0).setPosition(position.x - 16, position.y - 16);
		sprite.get(0).setRotation((float) (body.getAngle()*180/Math.PI));

		Appear();
		Flashing();

		groundJudge = false;
	}

	/**
	 * アイテム出現時の動き
	 */
	public void Appear() {
		if(appear) {
			velocity.y = JUMP_POWER;
			appear = false;
		}
		else {
			if( !groundJudge ) {
				body.setLinearVelocity(velocity);
				velocity.y -= 0.5f;

				if( velocity.y < -20.0f ) {
					velocity.y = -20.0f;
				}
			}
			else {
				velocity.y = 0.0f;
				body.setLinearVelocity(velocity);
			}
		}
	}

	/**
	 * 点滅処理
	 */
	public void Flashing() {
		// 高速点滅
		if( survivalTime < 60 ) {
			if( survivalTime % 3 > 0 ) {
				sprite.get(0).setColor( 0, 0, 0, 0);
			}
			else {
				sprite.get(0).setColor(1, 1, 1, 1);
			}
		}

		// まぁ早め
		else if( survivalTime < 180 ) {
			if( survivalTime % 30 > 15 ) {
				sprite.get(0).setColor( 0, 0, 0, 0);
			}
			else {
				sprite.get(0).setColor(1, 1, 1, 1);
			}
		}

		// 普通
		else if( survivalTime < 300 ) {
			if( survivalTime % 60 > 30 ) {
				sprite.get(0).setColor(0, 0, 0, 0);
			}
			else {
				sprite.get(0).setColor(1, 1, 1, 1);
			}
		}
	}

	//************************************************************
	// GetEffect
	// プレイヤーがアイテムを取った時のエフェクト処理
	//************************************************************
	public void GetEffect() {
	}

	@Override
	public void collisionDispatch(ObjectBase obj, Contact contact) {
		obj.collisionNotify(this, contact);
	}

	@Override
	public void collisionNotify(Background obj, Contact contact){
		groundJudge = true;
	}

	@Override
	public void collisionNotify(Player obj, Contact contact){
		deleteFlag = true;
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
	public void collisionNotify(WeaponBase obj, Contact contact){}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Item GetItem() { return this; }
	public int GetType(){ return type; }
	public int GetNum(){ return number; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
	public void SetNum(int num){ number = num; }

}


package org.genshin.old.scrollninja.object.item;

import org.genshin.old.scrollninja.GameMain;
import org.genshin.old.scrollninja.object.Background;
import org.genshin.old.scrollninja.object.Effect;
import org.genshin.old.scrollninja.object.Enemy;
import org.genshin.old.scrollninja.object.ItemManager;
import org.genshin.old.scrollninja.object.StageObject;
import org.genshin.old.scrollninja.object.weapon.AbstractWeapon;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

// *メモ*
// アイテム番号はマネージャで自動に割り振る
// 当たり判定が二重になっているとき（地面とプレイヤー同時HITとか）にうまく削除が出来ないので削除フラグ追加

// TODO これも別ファイルでデータリスト作って読み込めるようにするべきか
public class Item extends AbstractCollisionObject {

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
		type			= Type;
		number			= num;
		position		= new Vector2(x,y);
		velocity		= new Vector2(0.0f, 0.0f);
		appear			= true;
		deleteFlag		= false;
		survivalTime	= 600;

		Create();
	}

	//************************************************************
	// Create
	// アイテム生成
	//************************************************************
	private void Create() {
		switch(type) {
		case ONIGIRI:
			Texture texture = new Texture(Gdx.files.internal("data/old/item.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, 32, 32);
			sprites.add(new Sprite(tmpRegion));
			sprites.get(0).setPosition(-sprites.get(0).getWidth() * 0.5f, -sprites.get(0).getHeight() * 0.5f);
			sprites.get(0).setScale(GlobalParam.INSTANCE.WORLD_SCALE);

			BodyDef bd	= new BodyDef();
			bd.type				= BodyType.DynamicBody;		// 動く物体
			bd.bullet			= true;
			bd.fixedRotation	= true;
			bd.gravityScale		= 0.0f;
			bd.position.set(position);
			bd.linearVelocity.set(Vector2.Zero);
			createBody(GameMain.world, bd);

			// 当たり判定の作成
			PolygonShape poly		= new PolygonShape();
			poly.setAsBox(1.0f, 1.0f);

			// ボディ設定
			FixtureDef fd	= new FixtureDef();
			fd.density		= 0;
			fd.friction		= 0;//100.0f;
			fd.restitution	= 0;
			fd.shape		= poly;
			fd.isSensor		= true;

			createFixture(fd);
			poly.dispose();
			break;
		}
	}

	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void update(float deltaTime) {
		Body body = getBody();

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

		sprites.get(0).setPosition(position.x - 16, position.y - 16);
		sprites.get(0).setRotation((float) (body.getAngle()*180/Math.PI));

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
				getBody().setLinearVelocity(velocity);
				velocity.y -= 0.5f;

				if( velocity.y < -20.0f ) {
					velocity.y = -20.0f;
				}
			}
			else {
				velocity.y = 0.0f;
				getBody().setLinearVelocity(velocity);
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
				sprites.get(0).setColor( 0, 0, 0, 0);
			}
			else {
				sprites.get(0).setColor(1, 1, 1, 1);
			}
		}

		// まぁ早め
		else if( survivalTime < 180 ) {
			if( survivalTime % 30 > 15 ) {
				sprites.get(0).setColor( 0, 0, 0, 0);
			}
			else {
				sprites.get(0).setColor(1, 1, 1, 1);
			}
		}

		// 普通
		else if( survivalTime < 300 ) {
			if( survivalTime % 60 > 30 ) {
				sprites.get(0).setColor(0, 0, 0, 0);
			}
			else {
				sprites.get(0).setColor(1, 1, 1, 1);
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
	public void dispatchCollision(AbstractCollisionObject object, Contact contact) {
		object.notifyCollision(this, contact);
	}

	@Override
	public void notifyCollision(Background obj, Contact contact){
		groundJudge = true;
	}

	@Override
	public void notifyCollision(PlayerNinja obj, Contact contact){
		deleteFlag = true;
	}

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
	public Item GetItem() { return this; }
	public int GetType(){ return type; }
	public int GetNum(){ return number; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
	public void SetNum(int num){ number = num; }

}

package org.genshin.old.scrollninja.object;

import org.genshin.old.scrollninja.GameMain;
import org.genshin.old.scrollninja.object.item.Item;
import org.genshin.old.scrollninja.object.weapon.AbstractWeapon;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;

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
import com.badlogic.gdx.physics.box2d.PolygonShape;

// TODO エネミー用しか作っていない
// weaponパッケージに移動しようとしたらプレイヤー情報取得に詰まった…
public class Syuriken extends AbstractWeapon {
	private final int SYURIKEN_SPEED = 30;
	private final int EXIST_TIME = 240;
	private int timeCount;

	private float rotate = 0;

	/**
	 * コンストラクタ
	 * @param owner		使用者
	 * @param i			管理番号
	 */
	public Syuriken(AbstractCharacter owner, int i) {
		this.owner	= owner;					// 使用者
		number		= i;						// 管理番号
		level		= 2;						// レベル		TODO 外からひっぱってこないと
		attackNum	= (level * 5);				// 攻撃力 	TODO （てきとー）
		position 	= new Vector2(0, 0);
		use			= true;

		Create();
	}

	/**************************************************
	* create
	* 武器生成
	**************************************************/
	public void Create() {
		// テクスチャー読み込み
		// TODO 手裏剣テクスチャの位置はとりあえずなので後で要調整
		Texture	texture = new Texture(Gdx.files.internal("data/old/enemy.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 448, 64, 64);

		// スプライト反映
		sprites.add(new Sprite(region));
		sprites.get(0).setOrigin(sprites.get(0).getWidth() * 0.5f, sprites.get(0).getHeight() * 0.5f);
		sprites.get(0).setScale(GlobalParam.INSTANCE.WORLD_SCALE * 0.5f);

		// Body作成
		BodyDef bd	= new BodyDef();
		bd.type				= BodyType.DynamicBody;
		bd.bullet			= true;
		bd.fixedRotation	= true;
		bd.gravityScale		= 0.0f;
		createBody(GameMain.world, bd);

		// 当たり判定の作成
		PolygonShape poly		= new PolygonShape();
		poly.setAsBox(1.0f, 1.0f);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 0;
		fd.friction		= 0;
		fd.restitution	= 0;
		fd.shape		= poly;
		fd.isSensor		= true;

		createFixture(fd);

		// プレイヤーの武器の設定
		// TODO プレイヤーの場合はターゲットはマウスクリック座標？
		if (owner.getClass().equals(PlayerNinja.class)) {

		}

		// エネミーの武器の設定
		if (owner.getClass().equals(Enemy.class)) {
			timeCount = EXIST_TIME;				// 240で消える

			// 出現位置
			position = new Vector2(owner.getBody().getPosition());
			getBody().setTransform(position.x, position.y, 0);
			// 角度を求める
			// TODO 現在操作中のプレイヤー情報を求められるように変更必要あり
			Vector2 terget = new Vector2(PlayerManager.GetPlayer(0).getBody().getPosition());
			float rad = (float) Math.atan2(terget.y - position.y, terget.x - position.x);
			// 移動速度を求める
			Vector2 vel = new Vector2(0, 0);
			vel.x = (float) (Math.cos(rad) * SYURIKEN_SPEED);
			vel.y = (float) (Math.sin(rad) * SYURIKEN_SPEED);

			getBody().setLinearVelocity(vel.x, vel.y);
		}
	}

	/**
	 * 更新
	 */
	public void update(float deltaTime) {
		// 手裏剣表示時間
		timeCount -= 1;
		// 回転
		rotate++;

		position = getBody().getPosition();
		getBody().setTransform(position, rotate);

		// 消滅
		if(timeCount < 0) {
			use = false;
			timeCount = EXIST_TIME;
		}
	}

	/**
	 * 当たり判定
	 */
	@Override
	public void dispatchCollision(AbstractCollisionObject object, Contact contact) {
		object.notifyCollision(this, contact);
	}

	@Override
	public void notifyCollision(Background obj, Contact contact){}

	@Override
	public void notifyCollision(PlayerNinja obj, Contact contact){
		use = false;
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
}
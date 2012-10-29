//******************************
//	Weapon.java
//******************************

package org.genshin.scrollninja.object;

import java.util.ArrayList;

import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.PlayerManager;
import org.genshin.scrollninja.ScrollNinja;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

// TODO WeaponBaseに移行して不要になる？

public class Weapon extends ObjectBase{
	// 定数
	// 武器の種類 TODO 増えるごとに追加する必要が…
	private final int SYURIKEN			= 0;	// 手裏剣
	private final int EXIST_TIME		= 240;	// 手裏剣の存在時間
	private final int SYURIKEN_SPEED	= 30;	// 手裏剣の速さ
	// 武器の攻撃力
	private final int ATTACKNUM_SYURIKEN = 5;

	private int			type;			// 武器のタイプ
	private String		name;			// 名前
	private Vector2 	position;		// 武器座標
	private float 		attackNum;		// 武器威力
	private int 		weaponLevel;	// 武器レベル
	private boolean 	use;			// 使用フラグ
	private int			timeCount;		// 手裏剣の経過時間
	private float		rotate;			// 手裏剣回転要
	private double		random;			// 手裏剣をランダムで複数出す用

	private Player player;
	private Enemy enemy;

	//コンストラクタ　プレイヤーの場合
	public Weapon(String Name, Player player, int type) {
		sprite = new ArrayList<Sprite>();
		sensor = new ArrayList<Fixture>();

		name				= new String(Name);
		this.player			= player;
		this.enemy			= null;
		this.type			= type;

		this.position		= new Vector2(0, 0);
		this.attackNum		= 0;
		this.weaponLevel	= 0;
		this.use			= true;
		rotate = 0;

		Create();
	}

	//コンストラクタ　敵の場合
	public Weapon(String Name, Enemy enemy, int type) {
		sprite = new ArrayList<Sprite>();
		sensor = new ArrayList<Fixture>();

		name		= new String(Name);
		this.player	= null;
		this.enemy	= enemy;
		this.type	= type;

		this.position	= new Vector2(0, 0);
		this.use		= true;
		rotate = 0;

		Create();
	}

	/**************************************************
	* create
	* 武器生成
	**************************************************/
	public void Create() {
		// テクスチャー読み込み
		// TODO 武器によって位置が違ってくるので調整
		// 手裏剣テクスチャの位置はとりあえずなので後で要調整
		Texture texture = null;
		TextureRegion region = null;
		switch (type) {
		case SYURIKEN:
			texture = new Texture(Gdx.files.internal("data/enemy.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			region = new TextureRegion(texture, 0, 448, 64, 64);
			break;
		}

		// スプライト反映
		sprite.add(new Sprite(region));
		sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
		sprite.get(0).setScale(ScrollNinja.scale * 0.5f);

		// Body作成
		BodyDef def	= new BodyDef();
		def.type	= BodyType.DynamicBody;
		body = GameMain.world.createBody(def);

		// 当たり判定の作成
		PolygonShape poly		= new PolygonShape();
		poly.setAsBox(1.0f, 1.0f);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 0;
		fd.friction		= 0.0f;
		fd.restitution	= 0;
		fd.shape		= poly;

		sensor.add(body.createFixture(fd));	// センサーに追加
		sensor.get(0).setUserData(this);	// 当たり判定用にUserDataセット
		sensor.get(0).setSensor(true);		// 他の当たり判定に影響しない
		body.setBullet(true);				// すり抜け防止
		body.setFixedRotation(true);		// シミュレーションでの自動回転をしない
		body.setGravityScale(0);			// 重力の影響を受けない

		// プレイヤーの武器の設定
		if (player != null) {

		}

		// エネミーの武器の設定
		if (enemy != null) {
			switch (type) {
			case SYURIKEN:
				attackNum = ATTACKNUM_SYURIKEN;
				timeCount = EXIST_TIME;				// 240で消える

				// 出現位置
				Vector2 current = new Vector2(enemy.body.getPosition());
				body.setTransform(current.x + 3 * enemy.GetDirection(), current.y, 0);
				// 角度を求める
				// TODO 現在操作中のプレイヤー情報を求められるように変更必要あり
				Vector2 terget = new Vector2(PlayerManager.GetPlayer(0).body.getPosition());
				float rad = (float) Math.atan2(terget.y - current.y, terget.x - current.x);
				// 移動速度を求める
				Vector2 vel = new Vector2(0, 0);
				vel.x = (float) (Math.cos(rad) * SYURIKEN_SPEED);
				vel.y = (float) (Math.sin(rad) * SYURIKEN_SPEED);

				body.setLinearVelocity(vel.x, vel.y);

				player = null;
				break;
			}
		}
	}

	/**************************************************
	* Update
	* 更新まとめ
	**************************************************/
	public void Update() {
		// 現在位置
		position = body.getPosition();

		// 武器の種類によって分岐
		switch (type) {
		case SYURIKEN :
			syuriken();
			break;
		}
	}

	/**************************************************
	* syuriken
	* 手裏剣の動き
	**************************************************/
	public void syuriken() {
		// 手裏剣表示時間
		timeCount -= 1;
		// 回転
		rotate++;

		body.setTransform(position, rotate);

		// 消滅
		if(timeCount < 0) {
			use = false;
			timeCount = EXIST_TIME;
		}
	}

	/**************************************************
	* WeaponMove()
	* 武器モーション
	**************************************************/
	public void WeaponMove() {
		//------------------
		// TODO 武器動作
		//------------------
	}

	/**************************************************
	* WeaponLvUp
	* 武器のレベルアップ(仮)
	**************************************************/
	public int WeaponLvUp(int chakra) {
		return this.weaponLevel;
	}

	/**************************************************
	* collisionDispatch
	* 当たり判定
	**************************************************/
	//@Override
	/*
	public void collisionDispatch(ObJectBase obj, Contact contact) {
		obj.collisionNotify(this, contact);
	}
	*/

	@Override
	public void collisionNotify(Background obj, Contact contact){}

	@Override
	public void collisionNotify(Player obj, Contact contact){
		// 敵の攻撃だった場合
		if (player == null) {

		}
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

	/**************************************************
	* Get
	* ゲッターまとめ
	**************************************************/
	public Weapon GetWeapon() { return this; }
	public String GetName(){ return name; }
	public Vector2 GetWeaponPosition() { return position; } 	//武器座標ゲット
	public float GetAttackNum() { return attackNum; }			//武器威力ゲット
	public int GetWeaponLv() { return GetWeaponLv(); }			//武器レベルゲット
	public boolean GetUseFlag() { return use; }					// フラグゲット

	/**************************************************
	* Set
	* セッターまとめ
	**************************************************/
	public void SetUseFlag(boolean flag) { this.use = flag; };
}
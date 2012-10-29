package org.genshin.scrollninja.object;

import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.Interface;
import org.genshin.scrollninja.ScrollNinja;
import org.genshin.scrollninja.WeaponManager;
import org.genshin.scrollninja.object.player.DefaultPlayerController;
import org.genshin.scrollninja.object.player.IPlayerController;
import org.genshin.scrollninja.object.weapon.Kaginawa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

// 制作メモ
// 10/2 	制作開始
// 10/3 	変数と空の関数を実装
// 			ジャンプと移動だけ先に明日実装！
// 10/4 	ジャンプと移動は実装完了だけど実行してない
//			明日アニメーション関連進行。今週までに表示までいきたい
// 10/8		移動だけ動作確認。段差のところで空中移行になってるのを直そう
//			重力弱いから要調整。ジャンプできてねー＾ｑ＾
// 10/19 	・エフェクトとのアニメーションを一致させました
//			・上半身を下半身の前に描画するようにしました
//			・攻撃後すぐ戻ると不自然だったので上半身がそのまましばらく残るようにしました
//			・落下速度の調整

// *メモ*
// 攻撃はダッシュしながら攻撃可能（足は止まらない）
// 右クリック押しっぱなしで伸び続ける
// 壁とかに付いた後も押しっぱでそっちに移動
// 壁とかに付いた状態で離すとブラーン
// もう一回右クリックで離す

//TODO	currentState = ◯◯のコメントアウト
//		↑の部分は現在立ちアニメーションがないのでコメントアウトしてモーション遷移確認のため歩きで代用しています
// 		コメントアウト部分消すと完成した時どこ直せばいいのか探すのめんどいので消さないようにお願いします

//========================================
// クラス宣言
//========================================
/**
 * プレイヤークラス
 * @author
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class Player extends CharacterBase {
	// 定数宣言
	private static final float RUN_MAX_VEL		= 20.0f;	// 走りの最高速度
	private static final float DASH_MAX_VEL	= 40.0f;	// ダッシュの最高速度
	private static final float RUN_ACCEL		= 5.0f;	// 走りの加速度
	private static final float DASH_ACCEL		= 10.0f;	// ダッシュの加速度
	private static final float JUMP_POWER		= 70.0f;	// ジャンプ加速度
	private static final int JUMP_MAX			= 2;		// 連続でジャンプできる回数

	private static final int FOOT	= 0;
	private static final int BODY	= 1;

	private static final int RIGHT			=  1;
	private static final int LEFT			= -1;
	private static final int STAND			=  0;
	private static final int RUN				=  1;
	private static final int DASH			=  2;
	private static final int JUMP			=  3;
	private static final int ATTACK			=  4;

	// 変数宣言
	private int					number;					// プレイヤー番号
	private int					charge;					// チャージゲージ
	private int					currentState;			// 現在の状態
	private int					maxChakra;				// チャクラ最大値
	private int					chakra;				// チャクラ
	private int					count;					// カウント用変数
	private int					invincibleTime;		// 無敵時間
	private float				stateTime;
	private boolean				jump;					// ジャンプフラグ
	private boolean				groundJudge;			// 地面と当たってますよフラグ
	private WeaponBase			weapon;
	private Kaginawa			kaginawa;		// 鉤縄
	private IPlayerController	controller;		// プレイヤーの操作状態インタフェース

	private Animation		standAnimation;			// 立ちアニメーション
	private Animation		walkAnimation;			// 歩きアニメーション
	private Animation		dashAnimation;			// ダッシュアニメーション
	private Animation		jumpAnimation;			// ジャンプアニメーション
	private Animation		attackAnimation;		// 攻撃アニメーション
	private Animation		footWalkAnimation;		// 下半身・歩きアニメーション
	private TextureRegion[]	frame;					// アニメーションのコマ
	private TextureRegion	nowFrame;				// 現在のコマ
	private TextureRegion	nowFootFrame;			// 下半身用の現在のコマ

	// おそらく別のクラスに吐き出す変数
	private int				money;					// お金

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public int GetDirection(){ return direction; }
	public int GetChakra(){ return chakra; }
	public int GetMaxChakra(){ return maxChakra; }
	public Sprite GetSprite(String type) {
		if (type.equals("BODY"))
			return sprite.get(BODY);
		else
			return sprite.get(FOOT);
	}
	public int GetMaxHP() { return MAX_HP;}
	public float GetHP() { return hp; }
	public WeaponBase GetWeapon(){ return weapon; }

	/**
	 * コンストラクタ
	 * @param Number	管理番号
	 * @param position	初期座標
	 */
	public Player(int Number, Vector2 position) {
		// body生成
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;		// 移動する
		bd.position.set(position);			// 座標初期化
		bd.bullet = true;					// すり抜けない
		bd.fixedRotation = true;			// 回転しない
		body = GameMain.world.createBody(bd);

		// fixture生成
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(1.6f, 1.6f, new Vector2(0.0f, 1.6f), 0.0f);

		FixtureDef fd = new FixtureDef();
		fd.density			= 0.0f;	// 密度
		fd.friction		= 0.0f;	// 摩擦
		fd.restitution	= 0.0f;	// 反発
		fd.shape			= poly;	// 形状

		Fixture bodyFixture = body.createFixture(fd);
		bodyFixture.setUserData(this);
		poly.dispose();

		CircleShape circle = new CircleShape();
		circle.setRadius(1.5f);
		fd.shape = circle;
		Fixture footFixture = body.createFixture(fd);
		footFixture.setUserData(this);
		circle.dispose();

		sensor.add(footFixture);
		sensor.add(bodyFixture);

		// テクスチャの読み込み
		Texture texture = new Texture(Gdx.files.internal("data/player.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// アニメーション
		TextureRegion[][] tmp = TextureRegion.split(texture, 64, 64);

		// 上半身・歩き　２行目６フレーム
		frame = new TextureRegion[6];
		int index = 0;
		for (int i = 0; i < frame.length; i++)
			frame[index++] = tmp[1][i];
		walkAnimation = new Animation(5.0f, frame);

		// 下半身・歩き １行目６フレーム
		frame = new TextureRegion[6];
		index = 0;
		for (int i = 0; i < frame.length; i
				++)
			frame[index++] = tmp[0][i];
		footWalkAnimation = new Animation(5.0f, frame);

		// 上半身・攻撃　３行目５フレーム
		frame = new TextureRegion[5];
		index = 0;
		for (int i = 0; i < frame.length; i++)
			frame[index++] = tmp[2][i];
		attackAnimation = new Animation(3.6f, frame);

		// スプライトに反映 最初は立ちの第１フレーム
		// （※現在は用意されていないので歩きの第１フレームで代用）
		Sprite footSprite = new Sprite(footWalkAnimation.getKeyFrame(0, true));
		footSprite.setOrigin(footSprite.getWidth() * 0.5f, footSprite.getHeight() * 0.5f - 8);
		footSprite.setScale(ScrollNinja.scale);

		Sprite bodySprite = new Sprite(walkAnimation.getKeyFrame(0, true));
		bodySprite.setOrigin(bodySprite.getWidth() * 0.5f, bodySprite.getHeight() * 0.5f - 8);
		bodySprite.setScale(ScrollNinja.scale);

		sprite.add(footSprite);
		sprite.add(bodySprite);

		// 一番最初の表示　現在は歩きで代用
		nowFrame = walkAnimation.getKeyFrame(0, true);
		nowFootFrame = footWalkAnimation.getKeyFrame(0, true);

		hp			 = MAX_HP;
		charge		 = 0;
		money		 = 0;
		direction	 = 1;
		currentState = STAND;
		jump		 = false;
		count		 = 0;
		invincibleTime = 0;
		number = Number;
		weapon = WeaponManager.CreateWeapon(this, WeaponManager.KATANA, 2);	// TODO 今だけレベル２
		kaginawa = new Kaginawa(body);
		controller = new DefaultPlayerController();
	}


	/**
	 * 更新処理
	 */
	public void Update() {
		// 操作状態を更新
		controller.update();

		sprite.get(BODY).setRegion(nowFrame);
		sprite.get(FOOT).setRegion(nowFootFrame);

		int prevState = currentState;
		if( invincibleTime > 0 ) invincibleTime --;		// 無敵時間の減少S
		if( count > 0 ) count --;						// アニメーションカウントの減少

		Flashing();			// 点滅処理
		Move();				// 移動処理
		Stand();				// 立ち処理
		Jump();				// ジャンプ処理
		Attack();				// 攻撃処理
		updateKaginawa();		// 鉤縄処理

		if( prevState != currentState ) {
			stateTime = 0;
		}
		animation();		// アニメーション処理（これ最後で）

		// 画像反転処理
		flip(direction==RIGHT, false);

		position = body.getPosition();

		groundJudge = false;
	}

	/**
	 *
	 */
	private void Stand() {

		Vector2 velocity = body.getLinearVelocity();
		if( velocity.x == 0 ) {
			currentState = STAND;
		}
	}
	/**
	 * ジャンプ処理
	 * Ｗでジャンプ
	 */
	private void Jump() {

		// 地面に接触しているならジャンプ可能
		if( groundJudge ) {
			// 上押したらジャンプ！
			if (controller.jump()) {
				jump = true;
				currentState = JUMP;
				body.setLinearVelocity(body.getLinearVelocity().x, 0.0f);
				body.applyLinearImpulse(0.0f, JUMP_POWER, position.x, position.y);
			}
		}
		else
		{
			nearRotate(0);
		}

		// ジャンプ中の処理
		if( jump ) {
//			body.setLinearVelocity(velocity.x, velocity.y);
//			velocity.y -= 1;
		}
	}

	/**
	 * 移動処理
	 * 左右で移動
	 */
	private void Move()
	{
		if(controller.dash())
			move(DASH_ACCEL, DASH_MAX_VEL, DASH);
		else
			move(RUN_ACCEL, RUN_MAX_VEL, RUN);
	}

	private void move(float accel, float maxVel, int state)
	{
		// TODO 壁走り、天井走りを継続させるため、地面に足が着いている時は地面への吸着力を発生させる？

		// 速度制限
		// FIXME 現状だと水平方向にしか制限が働かないため、なんとかする。
		Vector2 vel = body.getLinearVelocity();
		if( Math.abs(vel.x) > maxVel ) {
			body.setLinearVelocity(Math.signum(vel.x)*maxVel, vel.y);
		}

		// 移動処理
		// FIXME 斜面でずり落ちないようにする。摩擦いじる？
		float moveLevel = controller.moveLevel();

		if(moveLevel == 0.0f)
		{
			if( groundJudge ) {
				Vector2 velocity = body.getLinearVelocity();
				velocity.x *= 0.8;
				if( velocity.x < 0.5 && velocity.x > -0.5 ) {
					velocity.x = 0;
				}
				body.setLinearVelocity(velocity);
			}
		}
		else
		{
			direction = moveLevel>0.0f ? RIGHT : LEFT;
			float sin = (float) Math.sin(body.getAngle());
			float cos = (float) Math.cos(body.getAngle());
			body.applyLinearImpulse(accel*direction*cos, accel*direction*sin, position.x, position.y);

			if( groundJudge ) {
				currentState = state;
			}
		}
	}

	//************************************************************
	// Attack
	// 攻撃処理。左クリックで攻撃
	//************************************************************
	private void Attack() {
		if( weapon.GetUseFlag() ) {
			currentState = ATTACK;
		}
		else if( currentState != RUN/*STAND*/ ){
			currentState = JUMP;
		}

		if(controller.attack() && currentState != ATTACK ) {
			count = 30 + 18;
			currentState = ATTACK;
			weapon.SetUseFlag(true);
		}
	}

	/**
	 * 鉤縄の更新処理を実行する。
	 */
	private void updateKaginawa()
	{
		if( controller.kaginawaThrow() )
		{
			kaginawa.attack();
		}
		kaginawa.Update();
	}

	@Override
	public void Draw()
	{
		kaginawa.Draw();
		super.Draw();
	}

	//************************************************************
	// animation
	// 現在の状態を参照して画像を更新
	//************************************************************
	private void animation() {
		switch(currentState) {
		case STAND:		// 立ち
			break;
		case RUN:		// 歩き
			if( count == 0 ) {
				nowFrame = walkAnimation.getKeyFrame(stateTime, true);
			}
			nowFootFrame = footWalkAnimation.getKeyFrame(stateTime, true);
			stateTime ++;
			break;
		case DASH:		// 走り
			break;
		case JUMP:		// ジャンプ
			break;
		case ATTACK:
			if( count > 30 ) {
				nowFrame = attackAnimation.getKeyFrame(stateTime, true);
			}
			nowFootFrame = footWalkAnimation.getKeyFrame(stateTime, true);
			stateTime ++;
			break;
		}
	}

	// 武器変更
	private void changeWeapon() {
	}

	/**
	 * 点滅処理
	 */
	public void Flashing() {
		// 高速点滅
		if( invincibleTime != 0 ) {
			if( invincibleTime % 10 > 5 ) {
				for(int i = 0; i < sprite.size(); i ++) {
					sprite.get(i).setColor( 0, 0, 0, 0);
				}
			}
			else {
				for(int i = 0; i < sprite.size(); i ++) {
					sprite.get(i).setColor(1, 1, 1, 1);
				}
			}
		}
	}

	@Override
	public void collisionDispatch(ObjectBase obj, Contact contact) {
		obj.collisionNotify(this, contact);
	}

	/**
	 * @Override
	 * 地面との当たり判定
	 */
	public void collisionNotify(Background obj, Contact contact) {
		// 衝突したのが足でなければそのままでOK
		Fixture footFixture = sensor.get(FOOT);
		if(contact.getFixtureA()!=footFixture && contact.getFixtureB()!=footFixture)
			return;

		// 足が地面に着いたらジャンプできるよ！
		jump = false;

		if( currentState != ATTACK ) {
			//currentState = STAND;
			currentState = RUN;
		}

		groundJudge = true;

		// 壁走り
		nearRotate( (float)Math.toRadians(contact.getWorldManifold().getNormal().angle()-90) );
	}

	/**
	 * @Override
	 * アイテムと当たった時
	 */
	public void collisionNotify(Item obj, Contact contact){
		switch(obj.GetType()) {
		case Item.ONIGIRI:
			hp += 50;
			if( hp > MAX_HP ) {
				hp = MAX_HP;
			}
			Interface.calculateHP = true;
			break;
		case Item.OKANE:
			break;
		}
	}

	@Override
	public void collisionNotify(WeaponBase obj, Contact contact){
		if (obj.GetOwner() != this) {
			if( invincibleTime == 0 ) {
				invincibleTime = 120;		// 無敵時間付与
				hp -= obj.GetAttackNum();
				Interface.calculateHP = true;
			}
			if( hp <= 0 ) {
				// TODO ゲームオーバー処理へ
				hp = 100;
			}
		}
	}
	@Override
	public void collisionNotify(Effect obj, Contact contact){
		if (obj.GetOwner() != this) {
			if( invincibleTime == 0 ) {
				invincibleTime = 120;		// 無敵時間付与
				hp -= obj.GetAttackNum();
				Interface.calculateHP = true;
			}
			if( hp <= 0 ) {
				// TODO ゲームオーバー処理へ
				hp = 100;
			}
		}
	}

	/**
	 * 180度以下の範囲で回転する。
	 * @param radian		回転後の角度
	 */
	private void nearRotate(float radian)
	{
		float stateTime = 10;		// 引数から指定する仕様に変更するかも知れない

		radian -= body.getAngle();
		radian = (float)( Math.abs(radian) % (Math.PI*2.0f) * Math.signum(radian) );
		if(Math.abs(radian) > Math.PI)
			radian = (float)( radian - Math.signum(radian) * Math.PI );

		body.setAngularVelocity(radian*stateTime);
	}
}

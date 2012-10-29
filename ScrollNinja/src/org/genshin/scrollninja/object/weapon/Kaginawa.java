/**
 *
 */
package org.genshin.scrollninja.object.weapon;

import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.ScrollNinja;
import org.genshin.scrollninja.object.Background;
import org.genshin.scrollninja.object.ObjectBase;
import org.genshin.scrollninja.object.WeaponBase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

/**
 * 鉤縄クラス
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class Kaginawa extends WeaponBase
{
	/** 鉤縄の状態 */
	private enum STATE
	{
		/** 待機状態 */
		IDLE,
		/** 投げられて飛んでいる状態 */
		THROW,
		/** 縮んでいる状態 */
		SHRINK,
		/** ぶら下がっている状態 */
		HANG,
		/** 離した状態 */
		RELEASE,
	}

	/** 衝突関連の定数 */
	private static final class COLLISION
	{
		/** 衝突オブジェクトの半径({@value}) */
		private static final float RADIUS = 1.0f;
	}

	/** スプライト関連の定数 */
	private static final class SPRITE
	{
		/** テクスチャのパス({@value}) */
		private static final String PATH = "data/shuriken.png";
	}

	/** 鉤縄の飛ぶ速度({@value}) */
	private static final float THROW_VEL = 45.0f;

	/** 鉤縄の縮む速度({@value}) */
	private static final float SHRINK_VEL = THROW_VEL*2.0f;

	/** 鉤縄の長さ({@value}) */
	private static final float LEN_MAX = THROW_VEL*1.0f;

	/** 鉤縄の持ち主 */
	private Body owner;

	/** 鉤縄と持ち主を繋ぐロープ */
	private RopeJoint ropeJoint;

	/** 鉤縄の状態 */
	private STATE state;

	/** 鉤縄の飛ぶ方向 */
	private Vector2 dir;

	/** 鉤縄の長さ */
	private float len;

	/** 更新メソッド */
	private final IUpdateMethod updateMethods[];

	/**
	 * コンストラクタ
	 */
	public Kaginawa(Body owner)
	{
		World world = GameMain.world;

		// body生成
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;		// 移動するオブジェクト
		bd.active = false;					// 最初は活動しない
		bd.bullet = true;					// すり抜けない
		bd.gravityScale = 0.0f;				// 重力の影響を受けない
		body = world.createBody(bd);

		// ロープジョイント生成
		RopeJointDef rjd = new RopeJointDef();
		rjd.bodyA = owner;
		rjd.bodyB = body;
		rjd.maxLength = LEN_MAX;
		Joint joint = world.createJoint(rjd);
		assert joint instanceof RopeJoint : "ジョイントの生成に失敗してるよ。";
		ropeJoint = (RopeJoint)joint;

		// 衝突オブジェクト生成
		CircleShape cs = new CircleShape();
		cs.setRadius(COLLISION.RADIUS);

		FixtureDef fd = new FixtureDef();
		fd.density		= 0.0f;	// 密度
		fd.friction	= 0.0f;	// 摩擦
		fd.isSensor	= true;	// センサーフラグ
		fd.shape		= cs;		// 形状

		Fixture fixture = body.createFixture(fd);
		fixture.setUserData(this);
		sensor.add(fixture);
		cs.dispose();

		// スプライト生成
		Texture texture = new Texture(Gdx.files.internal(SPRITE.PATH));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Sprite sprite = new Sprite(texture);
		sprite.setOrigin(texture.getWidth()*0.5f, texture.getHeight()*0.5f);
		sprite.setScale(ScrollNinja.scale);

		this.sprite.add(sprite);

		// フィールド初期化
		this.owner = owner;
		dir = new Vector2();
		state = STATE.IDLE;

		// 更新メソッド初期化
		updateMethods = new IUpdateMethod[STATE.values().length];
		IUpdateMethod updateEmpty = new UpdateEmpty();
		updateMethods[STATE.IDLE.ordinal()] = updateEmpty;
		updateMethods[STATE.THROW.ordinal()] = new UpdateThrow();
		updateMethods[STATE.SHRINK.ordinal()] = new UpdateShrink();
		updateMethods[STATE.HANG.ordinal()] = new UpdateHang();
		updateMethods[STATE.RELEASE.ordinal()] = new UpdateRelease();
	}

	/**
	 * 鉤縄を投げる。
	 */
	public void attack()
	{
		// 待機状態でなければ無視する
		if(state != STATE.IDLE)
			return;

		// 初期座標、アクティブフラグを設定
		body.setTransform(owner.getPosition(), 0);
		body.setActive(true);

		// FIXME 鉤縄の向き設定（仮）
		Vector2 mousePos = new Vector2(
			Gdx.input.getX() - Gdx.graphics.getWidth()*0.5f,
			Gdx.graphics.getHeight()*0.5f - Gdx.input.getY()
		);
		Vector2 ownerPos = owner.getPosition();

		mousePos.mul(ScrollNinja.scale);
		mousePos.x += GameMain.camera.position.x;
		mousePos.y += GameMain.camera.position.y;

		dir.x = mousePos.x - ownerPos.x;
		dir.y = mousePos.y - ownerPos.y;
		dir.nor();

		// 速度を設定
		body.setLinearVelocity(dir.x*THROW_VEL, dir.y*THROW_VEL);

		// 状態遷移
		state = STATE.THROW;

		// 長さ初期化
		len = 0.0f;
	}

	/**
	 * 鉤縄にぶら下がる。
	 */
	public final void hang()
	{

	}

	/**
	 * 鉤縄を離す。
	 */
	public final void release()
	{
		// TODO 鉤縄を離した時のエフェクト的なものを発生させる。
		// 状態遷移
		state = STATE.RELEASE;
	}

	@Override
	public void Update()
	{
		IUpdateMethod updateMethod = updateMethods[state.ordinal()];

		assert updateMethod!=null : "鉤縄は正しく初期化されていません。(state=" + state.toString() + ")";

		updateMethods[state.ordinal()].invoke(this);
	}

	@Override
	public void Draw()
	{
		if(body.isActive())
			super.Draw();
	}

	@Override
	protected void collisionDispatch(ObjectBase obj, Contact contact)
	{
		// TODO collisionNotifyはpublic化する。（別パッケージなのでprotected呼べない）
		//obj.collisionNotify(this, contact);
	}

	@Override
	protected void collisionNotify(Background obj, Contact contact)
	{
		// TODO 鉤縄の衝突処理とか。
		body.setLinearVelocity(0.0f, 0.0f);
		state = STATE.SHRINK;
	}



	/**
	 * 更新メソッドのインタフェース
	 */
	private interface IUpdateMethod
	{
		/**
		 * 更新メソッドを実行する。
		 * @param kaginawa	更新する鉤縄オブジェクト
		 */
		public void invoke(Kaginawa kaginawa);
	}

	/**
	 * 何もしない更新メソッド
	 */
	private class UpdateEmpty implements IUpdateMethod
	{
		@Override
		public void invoke(Kaginawa kaginawa)
		{
			// 何もしない
		}
	}

	/**
	 * 鉤縄が飛んでいる状態の更新メソッド
	 */
	private class UpdateThrow implements IUpdateMethod
	{
		@Override
		public void invoke(Kaginawa kaginawa)
		{
//			if((kaginawa.len+=THROW_VEL/60) > LEN_MAX)
//			kaginawa.release();
		}
	}

	/**
	 * 鉤縄が縮んでいる状態の更新メソッド
	 */
	private class UpdateShrink implements IUpdateMethod
	{
		@Override
		public void invoke(Kaginawa kaginawa)
		{
			Body owner = kaginawa.owner;
			Vector2 kaginawaPos = kaginawa.body.getPosition();
			Vector2 ownerPos = owner.getPosition();
			Vector2 direction = kaginawaPos.sub(ownerPos);
			float len2 = direction.len2();
			direction.nor().mul(SHRINK_VEL);

			owner.applyLinearImpulse(direction, ownerPos);

			if(len2 < (SHRINK_VEL*SHRINK_VEL)/30/30)
			{
				kaginawa.state = STATE.IDLE;
				kaginawa.body.setActive(false);
			}
		}
	}

	/**
	 * 鉤縄にぶら下がっている状態の更新メソッド
	 */
	private class UpdateHang implements IUpdateMethod
	{
		@Override
		public void invoke(Kaginawa kaginawa)
		{
			kaginawa.state = STATE.IDLE;
		}
	}

	/**
	 * 鉤縄を離した状態の更新メソッド
	 */
	private class UpdateRelease implements IUpdateMethod
	{
		@Override
		public void invoke(Kaginawa kaginawa)
		{
			kaginawa.state = STATE.IDLE;
			kaginawa.body.setActive(false);
		}
	}
}

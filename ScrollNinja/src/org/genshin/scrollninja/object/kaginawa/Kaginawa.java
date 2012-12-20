package org.genshin.scrollninja.object.kaginawa;

import org.genshin.old.scrollninja.object.Background;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.AbstractDynamicObject;
import org.genshin.scrollninja.object.effect.KaginawaReleaseEffect;
import org.genshin.scrollninja.render.RenderObjectFactory;
import org.genshin.scrollninja.render.RenderObjectInterface;

import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

/**
 * 鉤縄クラス
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class Kaginawa extends AbstractDynamicObject
{
	/**
	 * コンストラクタ
	 * @param world		所属先となるWorldオブジェクト
	 * @param owner		持ち主となるBodyオブジェクト
	 */
	public Kaginawa(World world, Body owner)
	{
		super(world);

		this.owner = owner;
		changeState(State.IDLE);
	}

	/**
	 * 鉤縄を伸ばす。
	 * @param direction 伸ばす方向
	 */
	public final void slack(Vector2 direction)
	{
		state.slack(this, direction);
	}
	
	/**
	 * 鉤縄を縮める。
	 */
	public final void shrink()
	{
		state.shrink(this);
	}

	/**
	 * 鉤縄にぶら下がる。
	 */
	public final void hang()
	{
		state.hang(this);
	}

	/**
	 * 鉤縄を離す。
	 */
	public final void release()
	{
		state.release(this);
	}

	@Override
	public void update(float deltaTime)
	{
		state.update(this);
	}

	@Override
	public void render()
	{
		// XXX 縄の描画（仮）
		final Vector2 kaginawaPosition = getBody().getPosition();
		final Vector2 ownerPosition = owner.getPosition();
		final Vector2 direction = ownerPosition.sub(kaginawaPosition);
		final Sprite ropeSprite = getRopeRenderObject().getSprite();
		final float len = direction.len();
		
		ropeSprite.setSize(len, ropeSprite.getHeight());
		ropeSprite.setRotation(direction.angle() - (float)Math.toDegrees(getBody().getAngle()));
		ropeSprite.setRegion(0, 0, (int)(len*GlobalParam.INSTANCE.INV_WORLD_SCALE), ropeSprite.getRegionHeight());
		
		super.render();
	}

	@Override
	public void dispatchCollision(AbstractCollisionObject object, Contact contact)
	{
		object.notifyCollision(this, contact);
	}

	@Override
	public void notifyCollision(Background obj, Contact contact)
	{
		// TODO 鉤縄の衝突処理とか。
		state.collision(this);
	}
	
	/**
	 * ロープジョイントを使うフラグを設定する。
	 * @param useRopeJoint		ロープジョイントを使うフラグ
	 */
	public void setUseRopeJoint(boolean useRopeJoint)
	{
		this.useRopeJoint = useRopeJoint;
	}
	
	/**
	 * 鉤縄が縮んでいる状態か調べる。
	 * @return		鉤縄が縮んでいる状態の場合true
	 */
	public boolean isShrinkState()
	{
		return state == State.SHRINK;
	}
	
	/**
	 * 鉤縄にぶら下がっている状態か調べる。
	 * @return		鉤縄にぶら下がっている状態の場合true
	 */
	public boolean isHangState()
	{
		return state == State.HANG;
	}
	
	/**
	 * 鉤縄を離した状態か調べる。
	 * @return		鉤縄を離した状態の場合true
	 */
	public boolean isReleaseState()
	{
		return state == State.RELEASE;
	}

	@Override
	protected void initializeSprite()
	{
		final RenderObjectInterface ropeRenderObject = RenderObjectFactory.getInstance().get("KaginawaRope");
		ropeRenderObject.getSprite().getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		addRenderObject( ropeRenderObject );
		addRenderObject( RenderObjectFactory.getInstance().get("KaginawaAnchor") );
	}
	
	@Override
	protected BodyDef createBodyDef()
	{
		BodyDef bd = super.createBodyDef();
		bd.gravityScale = 0.0f;
		return bd;
	}

	@Override
	protected FixtureDef createFixtureDef()
	{
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(KaginawaParam.INSTANCE.COLLISION_RADIUS);
		circleShape.setPosition(new Vector2(16.0f * GlobalParam.INSTANCE.WORLD_SCALE, 0.0f));
		
		FixtureDef fd	= super.createFixtureDef();
		fd.isSensor		= true;			// XXX センサーフラグ。いずれはFilterに代わる予定。
		fd.shape		= circleShape;
		return fd;
	}

	/**
	 * 状態を変更する。
	 * @param next	次の状態
	 */
	void changeState(State next)
	{
		assert next!=null;
		
		state = next;
		state.initialize(this);
	}
	
	/**
	 * 縄の描画オブジェクトを取得する。
	 * @return		縄の描画オブジェクト
	 */
	RenderObjectInterface getRopeRenderObject()
	{
		return getRenderObject(0);
	}
	
	/**
	 * 鉤の描画オブジェクトを取得する。
	 * @return		鉤の描画オブジェクト
	 */
	RenderObjectInterface getAnchorRenderObject()
	{
		return getRenderObject(1);
	}

	/** 鉤縄の持ち主 */
	private final Body owner;

	/** 鉤縄の状態 */
	private State state;
	
	/** 縄の長さを制限するためのジョイント */
	private Joint joint;
	
	/** 鉤縄の向き */
	private final Vector2 direction = new Vector2();
	
	/** XXX あやしいフラグ（仮）　外部から指定するのは何ともメンドクサイので何とかならんかね。 */
	boolean useRopeJoint = false;
	
	
	/** 
	 * 鉤縄の状態による振る舞い
	 */
	private enum State
	{
		/** 
		 * 待機状態
		 */
		IDLE
		{
			@Override
			void initialize(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				
				// 鉤縄を初期化
				kaginawa.setActive(false);
				
				// 持ち主を初期化
			}

			@Override
			void slack(Kaginawa me, Vector2 direction)
			{
				me.direction.set(direction.nor());
				me.changeState(SLACK);
			}
		},
		
		/**
		 * 鉤縄を伸ばしている状態
		 */
		SLACK
		{
			@Override
			void initialize(Kaginawa me)
			{
				Body kaginawa		= me.getBody();
				Body owner			= me.owner;
				Vector2 direction	= me.direction;
				
				// 鉤縄を初期化
				kaginawa.setType(BodyType.DynamicBody);
				kaginawa.setLinearVelocity(direction.x*KaginawaParam.INSTANCE.SLACK_VELOCITY, direction.y*KaginawaParam.INSTANCE.SLACK_VELOCITY);
				kaginawa.setTransform(owner.getPosition(), (float)Math.toRadians(direction.angle()));
				kaginawa.setActive(true);
				
				// 持ち主を初期化
			}

			@Override
			void update(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				
				// 縄の長さが限界に達したら、鉤縄を窓から投げ捨てろ！
				if(kaginawa.getPosition().dst2(owner.getPosition()) > KaginawaParam.INSTANCE.LENGTH*KaginawaParam.INSTANCE.LENGTH)
				{
					release(me);
				}
			}

			@Override
			void shrink(Kaginawa me)
			{
				me.changeState(RELEASE);
			}

			@Override
			void release(Kaginawa me)
			{
				me.changeState(RELEASE);
			}

			@Override
			void collision(Kaginawa me)
			{
				me.changeState(HANG);
			}
		},
		
		/**
		 * 鉤縄を縮めている状態
		 */
		SHRINK
		{
			@Override
			void initialize(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				World world		= kaginawa.getWorld();
				
				// 鉤縄を初期化
				kaginawa.setType(BodyType.StaticBody);
				kaginawa.setLinearVelocity(Vector2.Zero);
				
				// 持ち主を初期化
				owner.setLinearVelocity(Vector2.Zero);		// TODO なくす？
				
				// ジョイントがあれば切り離す
				if(me.joint != null)
				{
					world.destroyJoint(me.joint);
					me.joint = null;
				}
			}

			@Override
			void update(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				Vector2 kaginawaPos = kaginawa.getPosition();
				Vector2 ownerPos = owner.getPosition();
				Vector2 direction = new Vector2(kaginawaPos.x-ownerPos.x, kaginawaPos.y-ownerPos.y);
				float len2 = direction.len2();
				direction.nor().mul(KaginawaParam.INSTANCE.SHRINK_VELOCITY);
	
				owner.setLinearVelocity(direction);
			}

			@Override
			void hang(Kaginawa me)
			{
				me.changeState(HANG);
			}

			@Override
			void release(Kaginawa me)
			{
				me.changeState(RELEASE);
			}
		},
		
		/** 
		 * 鉤縄にぶら下がっている状態
		 */
		HANG
		{
			@Override
			void initialize(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				World world		= kaginawa.getWorld();
				
				// 鉤縄にジョイントは存在しないハズ
				assert kaginawa.getJointList().isEmpty();

				// 鉤縄を初期化
				kaginawa.setType(BodyType.StaticBody);
				kaginawa.setLinearVelocity(Vector2.Zero);
				
				// 持ち主を初期化
				owner.setLinearVelocity(Vector2.Zero);
				
				// ジョイントを生成
				if( me.useRopeJoint )
				{
					RopeJointDef jd = new RopeJointDef();
					jd.bodyA = owner;
					jd.bodyB = kaginawa;
					jd.localAnchorA.set(Vector2.Zero);
					jd.localAnchorB.set(Vector2.Zero);
					jd.maxLength = KaginawaParam.INSTANCE.LENGTH;
					me.joint = world.createJoint(jd);
				}
				else
				{
					DistanceJointDef jd = new DistanceJointDef();
					jd.initialize(owner, kaginawa, owner.getPosition(), kaginawa.getPosition());
					me.joint = world.createJoint(jd);
				}
			}

			@Override
			void update(Kaginawa me)
			{
				// TODO Auto-generated method stub
			}

			@Override
			void shrink(Kaginawa me)
			{
				me.changeState(SHRINK);
			}

			@Override
			void release(Kaginawa me)
			{
				me.changeState(RELEASE);
			}
		},
		
		/** 
		 * 鉤縄を離した状態
		 */
		RELEASE
		{
			@Override
			void initialize(Kaginawa me)
			{
				Body kaginawa	= me.getBody();
				Body owner		= me.owner;
				World world		= kaginawa.getWorld();
				
				// 鉤縄を初期化
				kaginawa.setActive(false);
				
				// 持ち主を初期化
				
				// ジョイントがあれば切り離す
				if(me.joint != null)
				{
					world.destroyJoint(me.joint);
					me.joint = null;
				}
				
				//---- エフェクトを発生させる。
				new KaginawaReleaseEffect(me.getRenderObjects(), me.getPositionX(), me.getPositionY(), (float)Math.toDegrees(kaginawa.getAngle()));
			}

			@Override
			void update(Kaginawa me)
			{
				// TODO エフェクトが消えるのを待つ処理とか？
				me.changeState(IDLE);
			}
		},
		;
		
		/**
		 * 状態を初期化する。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		abstract void initialize(Kaginawa me);
		
		/**
		 * 状態を更新する。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void update(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄を伸ばす。
		 * @param me			自身を指す鉤縄オブジェクト
		 * @param direction		伸ばす方向
		 */
		void slack(Kaginawa me, Vector2 direction) { /* 何もしない */ }

		/**
		 * 鉤縄を縮める。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void shrink(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄にぶら下がる。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void hang(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄を離す。
		 * @param me	自身を示す鉤縄オブジェクト
		 */
		void release(Kaginawa me) { /* 何もしない */ }
		
		/**
		 * 鉤縄が何かに衝突した。
		 * @param me	自身を示す鉤縄オブジェクト
		 */
		void collision(Kaginawa me) { /* 何もしない */ }
	}
}

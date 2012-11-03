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

/**
 * 鉤縄クラス
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class Kaginawa extends WeaponBase
{
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

	/** 鉤縄の状態 */
	private State state;
	
	/** 縄の長さを制限するためのジョイント */
	private Joint ropeJoint;
	
	/** 鉤縄の向き */
	private Vector2 dir;

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
		
		changeState(State.IDLE);
	}

	/**
	 * 鉤縄を投げる。
	 */
	public final void doThrow()
	{
		state.doThrow(this);
	}
	
	/**
	 * 鉤縄を縮める。
	 */
	public final void doShrink()
	{
		state.doShrink(this);
	}

	/**
	 * 鉤縄にぶら下がる。
	 */
	public final void doHang()
	{
		state.doHang(this);
	}

	/**
	 * 鉤縄を離す。
	 */
	public final void doRelease()
	{
		state.doRelease(this);
	}

	@Override
	public void Update()
	{
		state.update(this);
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
		doShrink();
	}
	
	/**
	 * 状態を変更する。
	 * @param next	次の状態
	 */
	private void changeState(State next)
	{
		assert next!=null;
		
		state = next;
		state.initialize(this);
	}
	
	
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
				Body kaginawa	= me.body;
				Body owner		= me.owner;
				
				kaginawa.setActive(false);
				owner.setGravityScale(1.0f);
			}

			@Override
			void doThrow(Kaginawa me)
			{
				Body kaginawa	= me.body;
				Body owner		= me.owner;
				Vector2 dir	= me.dir;
				
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
				
				me.changeState(THROW);
			}
		},
		
		/**
		 * 鉤縄を投げている状態
		 */
		THROW
		{
			@Override
			void initialize(Kaginawa me)
			{
				Body kaginawa	= me.body;
				Body owner		= me.owner;
				Vector2 dir	= me.dir;
				
				kaginawa.setTransform(owner.getPosition(), 0);
				kaginawa.setActive(true);
				kaginawa.setLinearVelocity(dir.x*THROW_VEL, dir.y*THROW_VEL);
				owner.setGravityScale(1.0f);
			}

			@Override
			void update(Kaginawa me)
			{
				Body kaginawa	= me.body;
				Body owner		= me.owner;
				
				// 縄の長さが限界に達したらぶら下がりん。
				Vector2 kaginawaPos = kaginawa.getPosition();
				Vector2 ownerPos = owner.getPosition();
				Vector2 direction = new Vector2(kaginawaPos.x-ownerPos.x, kaginawaPos.y-ownerPos.y);
				
				if(direction.len2() > LEN_MAX*LEN_MAX)
				{
					doRelease(me);
				}
			}

			@Override
			void doShrink(Kaginawa me)
			{
				me.changeState(SHRINK);
			}

			@Override
			void doHang(Kaginawa me)
			{
				me.changeState(HANG);
			}

			@Override
			void doRelease(Kaginawa me)
			{
				me.changeState(RELEASE);
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
				Body kaginawa	= me.body;
				Body owner		= me.owner;
				
				kaginawa.setLinearVelocity(Vector2.Zero);
				owner.setGravityScale(0.0f);
				owner.setLinearVelocity(Vector2.Zero);
			}

			@Override
			void update(Kaginawa me)
			{
				Body kaginawa	= me.body;
				Body owner		= me.owner;
				Vector2 kaginawaPos = kaginawa.getPosition();
				Vector2 ownerPos = owner.getPosition();
				Vector2 direction = new Vector2(kaginawaPos.x-ownerPos.x, kaginawaPos.y-ownerPos.y);
				float len2 = direction.len2();
				direction.nor().mul(SHRINK_VEL);
	
				owner.applyLinearImpulse(direction, ownerPos);
	
				if(len2 < (SHRINK_VEL*SHRINK_VEL)/30/30)
				{
					doRelease(me);
				}
			}

			@Override
			void doHang(Kaginawa me)
			{
				me.changeState(HANG);
			}

			@Override
			void doRelease(Kaginawa me)
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
				Body kaginawa	= me.body;
				Body owner		= me.owner;

				owner.setGravityScale(1.0f);
			}

			@Override
			void update(Kaginawa me)
			{
				// TODO Auto-generated method stub
			}

			@Override
			void doShrink(Kaginawa me)
			{
				me.changeState(SHRINK);
			}

			@Override
			void doRelease(Kaginawa me)
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
				Body kaginawa	= me.body;
				Body owner		= me.owner;
				
				// TODO 鉤縄を離した時のエフェクト的なものを発生させる。
				kaginawa.setActive(false);
				owner.setGravityScale(1.0f);
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
		 * 鉤縄を投げる。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void doThrow(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄を縮める。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void doShrink(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄にぶら下がる。
		 * @param me	自身を指す鉤縄オブジェクト
		 */
		void doHang(Kaginawa me) { /* 何もしない */ }

		/**
		 * 鉤縄を離す。
		 * @param me	自身を示す鉤縄オブジェクト
		 */
		void doRelease(Kaginawa me) { /* 何もしない */ }
	}
}




///** 更新メソッド */
//private enum UpdateMethod
//{
//	/** 何もしない更新メソッド */
//	EMPTY
//	{
//		@Override
//		void invoke(Kaginawa kaginawa)
//		{
//			/* 何もしない */
//		}
//	},
//	/** 鉤縄が飛んでいる状態の更新メソッド */
//	THROW
//	{
//		@Override
//		void invoke(Kaginawa kaginawa)
//		{
//			// 縄の長さが限界に達したらぶら下がりん。
//			Vector2 kaginawaPos = kaginawa.body.getPosition();
//			Vector2 ownerPos = kaginawa.owner.getPosition();
//			Vector2 direction = new Vector2(kaginawaPos.x-ownerPos.x, kaginawaPos.y-ownerPos.y);
//			
//			if(direction.len2() > LEN_MAX*LEN_MAX)
//			{
//				kaginawa.hang();
//			}
//		}
//	},
//	/** 鉤縄が縮んでいる状態の更新メソッド */
//	SHRINK
//	{
//		@Override
//		void invoke(Kaginawa kaginawa)
//		{
//			Vector2 kaginawaPos = kaginawa.body.getPosition();
//			Vector2 ownerPos = kaginawa.owner.getPosition();
//			Vector2 direction = new Vector2(kaginawaPos.x-ownerPos.x, kaginawaPos.y-ownerPos.y);
//			float len2 = direction.len2();
//			direction.nor().mul(SHRINK_VEL);
//
//			kaginawa.owner.applyLinearImpulse(direction, ownerPos);
//
//			if(len2 < (SHRINK_VEL*SHRINK_VEL)/30/30)
//			{
//				kaginawa.release();
//			}
//		}
//	},
//	/** 鉤縄にぶら下がっている状態の更新メソッド */
//	HANG
//	{
//		@Override
//		void invoke(Kaginawa kaginawa)
//		{
////			kaginawa.release();
//		}
//	},
//	/** 鉤縄を離した状態の更新メソッド */
//	RELEASE
//	{
//		@Override
//		void invoke(Kaginawa kaginawa)
//		{
//			kaginawa.idle();
//		}
//	},
//	;
//	/**
//	 * 更新メソッドを実行する。
//	 * @param kaginawa	更新する鉤縄オブジェクト
//	 */
//	abstract void invoke(Kaginawa kaginawa);
//}

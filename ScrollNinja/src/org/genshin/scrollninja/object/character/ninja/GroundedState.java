package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.object.effect.DashSmokeEffect;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;


/**
 * 地面と接触している時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class GroundedState extends AbstractNormalState
{
	/**
	 * コンストラクタ
	 * @param me		自身を示す忍者オブジェクト
	 */
	GroundedState(PlayerNinja me)
	{
		//---- 鉤縄のロープジョイントフラグを立てておく
		me.kaginawa.setUseRopeJoint(true);
		
		//---- 摩擦をデフォルト値に設定する。
		me.getFootFixture().setFriction(me.defaultFriction);
	}
	
	@Override
	public StateInterface update(PlayerNinja me, float deltaTime)
	{
		//---- 地面との衝突判定用タイマーを更新する。
		--me.groundedTimer;
		
		//---- あとは基本クラスに任せる。
		return super.update(me, deltaTime);
	}

	@Override
	public void collisionTerrain(PlayerNinja me, Contact contact)
	{
		//---- 衝突したのが下半身でなければ何もしない。
		if( !checkContactIsFoot(me, contact) )
			return;
		
		//---- 衝突したのが下半身なら地面に立っている時の処理を加える。
		// 空中ジャンプのカウント初期化
		me.restAerialJumpCount = NinjaParam.INSTANCE.AERIAL_JUMP_COUNT;
		
		// 地面との衝突判定用タイマー初期化
		me.groundedTimer = NinjaParam.INSTANCE.GROUNDED_JUDGE_TIME;
		
		// 前方ベクトルを設定
		final Vector2 normal = contact.getWorldManifold().getNormal();
		me.frontDirection.set(normal.y, -normal.x);
		
		// ジャンプ方向ベクトルを設定
		if( Math.abs(me.frontDirection.x) > Math.abs(me.frontDirection.y) )
		{
			me.jumpDirection.set(0.0f, me.frontDirection.x<0.0f?-1.0f:1.0f);
		}
		else
		{
			me.jumpDirection.set(me.frontDirection.y<0.0f?1.0f:-1.0f, me.frontDirection.x<0.0f?-1.0f:1.0f);
			me.jumpDirection.nor();
		}
	
		// キャラの角度を補正
		// TODO なだらかな坂に真っ直ぐ立つ処理（仮）　現在は発動しないようにしてある。
		final float normalAngle = normal.angle();
		final float verticalAngleZone = 0.0f;
		final float rotateTime = 0.1f;
		if( Math.abs(normalAngle % 180.0f - 90.0f) < verticalAngleZone )
		{
			nearRotate( me, (float)Math.toRadians(me.jumpDirection.angle()-90.0f), rotateTime );
		}
		else
		{
			nearRotate( me, (float)Math.toRadians(normalAngle-90.0f), rotateTime );
		}
	}

	@Override
	protected void updateMove(PlayerNinja me, float deltaTime)
	{
		//---- 基本クラスの処理を実行する。
		super.updateMove(me, deltaTime);
		
		//---- エフェクトを生成する。
		final float movePower = me.controller.getMovePower() * me.moveDirection;
		if(		me.controller.isDashStart() && movePower != 0.0f
			||	me.controller.isMoveStart() && me.controller.isDash()	)
		{
			if(movePower != 0.0f)
				new DashSmokeEffect(me.getPositionX(), me.getPositionY(), me.frontDirection.angle(), movePower > 0.0f);
		}
	}

	@Override
	protected void updateJump(PlayerNinja me)
	{
		if( isSnapCeiling(me) )
		{
			if( me.controller.isLeaveSnapCeiling() )
			{
				// 天井に吸着していた場合は、進行方向を逆転させる
				me.moveDirection = -me.moveDirection;
				leaveSnapCeiling(me);
			}
		}
		else if( me.controller.isJump() )
		{
			jump(me);
		}
	}

	@Override
	protected StateInterface getNextState(PlayerNinja me)
	{
		//---- 足が地面から離れていれば空中状態へ
		if( !me.isGrounded() )
		{
			return new AerialState(me);
		}
		
		//---- 地上で鉤縄にぶら下がっている状態へ
		if( me.kaginawa.isHangState() )
		{
			return new GroundedToKaginawaState(me);
		}
		
		//---- あとは基本クラスに任せる
		return super.getNextState(me);
	}
	
	/**
	 * 天井に吸着しているか調べる。
	 * @param me		自身を示す忍者オブジェクト
	 * @return			天井に吸着している場合はtrue
	 */
	private boolean isSnapCeiling(PlayerNinja me)
	{
		return me.jumpDirection.y < 0.0f;
	}
}

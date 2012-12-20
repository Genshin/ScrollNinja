package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.object.effect.JumpSmokeEffect;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;


/**
 * 空中にいる（地面と接触していない）時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class AerialState extends AbstractNormalState
{
	/**
	 * コンストラクタ
	 * @param me		自身を示す忍者オブジェクト
	 */
	AerialState(PlayerNinja me)
	{
		//---- 鉤縄のロープジョイントフラグを叩き折っておく
		me.kaginawa.setUseRopeJoint(false);
		
		//---- 地上フラグをへし折る
		me.groundedTimer = 0;
		
		//---- 前方ベクトルを強制的にX軸にする。
		me.frontDirection.set(Vector2.X);
		
		//---- ジャンプ方向ベクトルを強制的にY軸にする。
		me.jumpDirection.set(Vector2.Y);
	}
	
	@Override
	public StateInterface update(PlayerNinja me, float deltaTime)
	{
		//---- 初回フレーム判定用
		if(firstFrame)
		{
			firstFrame = false;
		}
		
		//---- 姿勢を起こす
		nearRotate(me, 0.0f, 0.1f);
		
		//---- 連続ジャンプ時は摩擦を無視する
		me.getFootFixture().setFriction(me.controller.isJump() ? 0.0f : me.defaultFriction);
		
		//---- あとは基本クラスに任せる。
		return super.update(me, deltaTime);
	}
	
	@Override
	public void collisionTerrain(PlayerNinja me, Contact contact)
	{
		//---- 衝突したのが足でなければ何もしない？
		if( !checkContactIsFoot(me, contact) )
			return;
		
		//---- 初回フレームなら何もしない
		if( firstFrame )
			return;
		
		//---- 地面と接触した時の処理を加える。
		// 地面との衝突判定用タイマー初期化
		me.groundedTimer = NinjaParam.INSTANCE.GROUNDED_JUDGE_TIME;
	}
	
	@Override
	protected void updateJump(PlayerNinja me)
	{
		if( me.controller.isAerialJump() && me.restAerialJumpCount > 0 )
		{
			jump(me);
			me.restAerialJumpCount--;
		}
	}

	@Override
	protected StateInterface getNextState(PlayerNinja me)
	{
		//---- 地面と接触したら地上状態へ
		if( me.isGrounded() )
		{
			new JumpSmokeEffect(me.getPositionX(), me.getPositionY(), 0.0f);
			return new GroundedState(me);
		}
		
		//---- 鉤縄にぶら下がっている状態へ
		if( me.kaginawa.isHangState() )
		{
			return new KaginawaHangState();
		}
		
		//---- あとは基本クラスに任せる
		return super.getNextState(me);
	}

	@Override
	protected String getStayAnimationName()
	{
		return "Jump";
	}

	/** 初回フレームフラグ */
	boolean firstFrame = true;
}

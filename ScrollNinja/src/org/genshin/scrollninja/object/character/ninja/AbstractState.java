package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.object.effect.JumpSmokeEffect;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * 忍者の状態の基本クラス。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
abstract class AbstractState implements StateInterface
{
// XXX お手軽状態遷移出力（よく使うので残しておく。最終的には消す）
//	static String oldState;
//	AbstractState()
//	{
//		String newState = getClass().getSimpleName();
//		Logger.global.info(GlobalParam.INSTANCE.frameCount + ": " + oldState + " -> " + newState);
//		oldState = newState;
//	}
	
	@Override
	public StateInterface update(PlayerNinja me, float deltaTime)
	{
		//---- 各種処理を実行する。
		updateMove(me, deltaTime);		// 移動
		updateJump(me);					// ジャンプ
		updateKaginawa(me);				// 鉤縄
		updateGravity(me, deltaTime);	// 重力
		
		//---- 次の状態を返す。
		return getNextState(me);
	}

	/**
	 * 180度以下の範囲で回転する。
	 * @param me		自身を示す忍者オブジェクト
	 * @param radian	回転後の角度（ラジアン）
	 * @param time		回転するのにかかる時間（秒）
	 */
	protected final void nearRotate(PlayerNinja me, float radian, float time)
	{
		final Body body = me.getBody();

		//---- 角度計算
		float angularVelocity = radian - body.getAngle();

		// -360～360度の範囲に丸める
		angularVelocity = (float)( Math.abs(angularVelocity) % (Math.PI*2.0f) * Math.signum(angularVelocity) );
		
		// -180～180度の範囲に丸める
		if(Math.abs(angularVelocity) > Math.PI)
			angularVelocity -= (float)( Math.signum(angularVelocity) * Math.PI * 2.0f );
		
		// 回転するのにかかる時間を適用する
		angularVelocity /= time;

		body.setAngularVelocity(angularVelocity);
	}
	
	/**
	 * 移動処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 * @param movePower		移動入力の強さ
	 * @param accel			加速度
	 * @param maxVelocity	制限速度
	 */
	protected final void move(PlayerNinja me, float movePower, float accel, float maxVelocity)
	{
		final Body body = me.getBody();
		final Vector2 oldVelocity = body.getLinearVelocity().tmp();
		final float oldVelocityLenAbs = Math.abs( me.frontDirection.dot(oldVelocity) );
		
		//---- 加速度を加える
		body.applyLinearImpulse(accel*movePower*me.frontDirection.x, accel*movePower*me.frontDirection.y, me.getPositionX(), me.getPositionY());
		
		//---- 速度に制限をかける
		final Vector2 newVelocity = body.getLinearVelocity();
		final float newVelocityLen = me.frontDirection.dot(newVelocity);
		final float newVelocityLenAbs = Math.abs(newVelocityLen);
		
		// 制限速度を越えた場合
		if( newVelocityLenAbs > maxVelocity )
		{
			// 最初から制限速度を越えていた場合
			if( oldVelocityLenAbs > maxVelocity )
			{
				// 加速度を加えた結果さらに加速していた場合、この加速はなかったことにする
				if( newVelocityLenAbs > oldVelocityLenAbs )
				{
					body.setLinearVelocity(oldVelocity);
				}
			}
			// 加速度を加えた結果制限速度を越えてしまった場合、前方に働く速度が制限速度内になるように丸める
			else
			{
				body.setLinearVelocity( newVelocity.sub(me.frontDirection.tmp().mul(maxVelocity/newVelocityLen)) );
			}
		}
	}

	/**
	 * ジャンプ処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 */
	protected final void jump(PlayerNinja me)
	{
		setAxisImpulse(me, me.jumpDirection, NinjaParam.INSTANCE.JUMP_POWER);
		
		//---- エフェクトを生成する。
		final Body body = me.getBody();
		final Vector2 position = body.getPosition();
		final float rotation = (float)Math.toDegrees(body.getAngle());
		new JumpSmokeEffect(position.x, position.y, rotation);
	}
	
	/**
	 * 天井への吸着を解除する処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 */
	protected final void leaveSnapCeiling(PlayerNinja me)
	{
		setAxisImpulse(me, me.jumpDirection, NinjaParam.INSTANCE.LEAVE_SNAP_CEILING_POWER);
		
		//---- エフェクトを生成する。
		final Body body = me.getBody();
		final Vector2 position = body.getPosition();
		final float rotation = (float)Math.toDegrees(body.getAngle());
		new JumpSmokeEffect(position.x, position.y, rotation);
	}
	
	/**
	 * 任意軸方向の速度を殺してから衝撃を与える。
	 * @param me			自身を示す忍者オブジェクト
	 * @param axis			軸（正規化済）
	 * @param impulse		衝撃
	 */
	private final void setAxisImpulse(PlayerNinja me, Vector2 axis, float impulse)
	{
		final Body body = me.getBody();
		
		// 一度ジャンプ方向の力を殺す
		final Vector2 velocity = body.getLinearVelocity();
		final float power = axis.dot(velocity);
		body.setLinearVelocity(velocity.sub(axis.tmp().mul(power)));
		
		// ジャンプ力を与える
		body.applyLinearImpulse(axis.x * impulse, axis.y * impulse, me.getPositionX(), me.getPositionY());
		
		// 地面との接触判定を消し飛ばす
		me.groundedTimer = 0;
	}
	
	/**
	 * 移動の更新処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 * @param deltaTime		経過時間（秒）
	 */
	protected abstract void updateMove(PlayerNinja me, float deltaTime);
	
	/**
	 * ジャンプの更新処理を実行する。
	 * @param me		自身を示す忍者オブジェクト
	 */
	protected abstract void updateJump(PlayerNinja me);
	
	/**
	 * 鉤縄の更新処理を実行する。
	 * @param me		自身を示す忍者オブジェクト
	 */
	protected abstract void updateKaginawa(PlayerNinja me);
	
	/**
	 * 重力の更新処理を実行する。
	 * @param me			自身を示す忍者オブジェクト
	 * @param deltaTime		経過時間（秒）
	 */
	protected void updateGravity(PlayerNinja me, float deltaTime)
	{
		me.getBody().applyLinearImpulse(me.frontDirection.y * me.worldGravity * deltaTime, -me.frontDirection.x * me.worldGravity * deltaTime, me.getPositionX(), me.getPositionY());
	}
	
	/**
	 * 衝突したのが下半身か調べる。
	 * @param me		自身を示す忍者オブジェクト
	 * @param contact	衝突情報
	 * @return
	 */
	protected boolean checkContactIsFoot(PlayerNinja me, Contact contact)
	{
		final Fixture footFixture = me.getFootFixture();
		return contact.getFixtureA() == footFixture || contact.getFixtureB() == footFixture;
	}
	
	/**
	 * 現在の状況に合わせた次の状態を取得する。
	 * @param me		自身を示す忍者オブジェクト
	 * @return			次の状態
	 */
	protected abstract StateInterface getNextState(PlayerNinja me);
}

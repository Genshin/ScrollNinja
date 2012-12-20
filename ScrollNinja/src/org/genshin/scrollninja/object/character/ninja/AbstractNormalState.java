package org.genshin.scrollninja.object.character.ninja;


/**
 * 通常状態の忍者処理の基本クラス。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
abstract class AbstractNormalState extends AbstractState
{
	@Override
	protected void updateMove(PlayerNinja me, float deltaTime)
	{
		if(me.controller.isMoveStart())
		{
			me.updateMoveDirection();
		}
		
		//---- 移動入力があれば移動する。
		final float movePower = me.controller.getMovePower() * me.moveDirection * deltaTime;
		if(movePower != 0.0f)
		{
			// ダッシュ
			if( me.controller.isDash() )
			{
				move(me, movePower, NinjaParam.INSTANCE.DASH_ACCEL, NinjaParam.INSTANCE.DASH_MAX_VELOCITY);
				me.setAnimation("Dash");
			}
			// 走り
			else
			{
				move(me, movePower, NinjaParam.INSTANCE.RUN_ACCEL, NinjaParam.INSTANCE.RUN_MAX_VELOCITY);
				me.setAnimation("Run");
			}
		}
		//---- 移動入力がなければアニメーションを待機状態に変更する。
		else
		{
			me.setAnimation(getStayAnimationName());
		}
	}
	
	@Override
	protected void updateKaginawa(PlayerNinja me)
	{
		if( me.controller.isKaginawaSlack() )
		{
			me.kaginawa.slack(me.controller.getDirection());
		}
		else if( me.controller.isKaginawaShrink() )
		{
			me.kaginawa.shrink();
		}
	}

	@Override
	protected StateInterface getNextState(PlayerNinja me)
	{
		//---- 鉤縄が縮み始めたら、鉤縄が縮んでいる時の状態へ
		if( me.kaginawa.isShrinkState() )
		{
			return new KaginawaShrinkState(me);
		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
	
	/**
	 * 待機状態のアニメーション名を取得する。
	 * @return		待機状態のアニメーション名
	 */
	protected String getStayAnimationName()
	{
		return "Stay";
	}
}

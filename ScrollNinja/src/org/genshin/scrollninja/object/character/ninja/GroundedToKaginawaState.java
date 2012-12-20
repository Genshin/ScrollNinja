package org.genshin.scrollninja.object.character.ninja;

/**
 * 地上から鉤縄状態に移行する時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
class GroundedToKaginawaState extends GroundedState
{
	/**
	 * コンストラクタ
	 * @param me		自身を示す忍者オブジェクト
	 */
	GroundedToKaginawaState(PlayerNinja me)
	{
		super(me);
	}

	@Override
	protected StateInterface getNextState(PlayerNinja me)
	{
		//---- 足が地面から離れていればジャンプ状態へ
		if( !me.isGrounded() )
		{
			return new AerialToKaginawaState(me);
		}

		//---- 鉤縄が縮み始めたら、鉤縄が縮んでいる時の状態へ
		if( me.kaginawa.isShrinkState() )
		{
			return new KaginawaShrinkState(me);
		}
		
		//---- どれにも当てはまらなければ現状維持
		return this;
	}
}

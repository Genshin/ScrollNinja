package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.utils.FixtureDefLoader;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 忍者関連の定数等を定義する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
enum NinjaParam
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	private NinjaParam()
	{
		final float worldScale = GlobalParam.INSTANCE.WORLD_SCALE;
		
		//---- 忍者の挙動関連
		{
			Element rootElement = XMLFactory.getInstance().get(GlobalParam.INSTANCE.XML_DIRECTORY_PATH + GlobalParam.INSTANCE.OBJECT_PARAM_XML_FILE_NAME);
			rootElement = rootElement.getChildByName("Ninja");
			
			// 走り
			RUN_ACCEL			= rootElement.getFloat("RunAccel") * worldScale;
			RUN_MAX_VELOCITY	= rootElement.getFloat("RunMaxVelocity") * worldScale;
			
			// ダッシュ
			DASH_ACCEL			= rootElement.getFloat("DashAccel") * worldScale;
			DASH_MAX_VELOCITY	= rootElement.getFloat("DashMaxVelocity") * worldScale;
			
			// ジャンプ
			JUMP_POWER			= rootElement.getFloat("JumpPower") * worldScale;
			AERIAL_JUMP_COUNT	= rootElement.getInt("AerialJumpCount");
			GROUNDED_JUDGE_TIME	= rootElement.getInt("GroundedJudgeTime");

			// 天井への吸着を解除する時の力
			LEAVE_SNAP_CEILING_POWER	= rootElement.getFloat("LeaveSnapCeilingPower") * worldScale;
		}
		
		//---- 衝突関連
		{
			Element rootElement = XMLFactory.getInstance().get(GlobalParam.INSTANCE.XML_DIRECTORY_PATH + GlobalParam.INSTANCE.COLLISION_PARAM_XML_FILE_NAME);
			rootElement = rootElement.getChildByName("Ninja");
			
			// 上半身
			BODY_FIXTURE_DEF_LOADER = new FixtureDefLoader(rootElement.getChildByName("Body"));
			
			// 下半身
			FOOT_FIXTURE_DEF_LOADER = new FixtureDefLoader(rootElement.getChildByName("Foot"));
		}
	}
	
	/** 走りの加速度 */
	final float RUN_ACCEL;
	
	/** 走りの最高速度 */
	final float RUN_MAX_VELOCITY;

	/** ダッシュの加速度 */
	final float DASH_ACCEL;
	
	/** ダッシュの最高速度 */
	final float DASH_MAX_VELOCITY;
	
	/** 天井への吸着をやめる時に発生する力 */
	final float LEAVE_SNAP_CEILING_POWER;
	
	/** ジャンプ力 */
	final float JUMP_POWER;
	
	/** 空中でジャンプできる回数 */
	final int AERIAL_JUMP_COUNT;

	/** 地面との衝突判定を出し続ける時間 */
	final int GROUNDED_JUDGE_TIME;
	
	/** 上半身Fixtureの定義情報 */
	final FixtureDefLoader BODY_FIXTURE_DEF_LOADER;
	
	/** 下半身Fixtureの定義情報 */
	final FixtureDefLoader FOOT_FIXTURE_DEF_LOADER;
}

/**
 * 
 */
package org.genshin.scrollninja.object.player;

import com.badlogic.gdx.Gdx;

/**
 * プレイヤーコントローラーの基本クラス。<br>
 * プレイヤーと入力状態とを仲介する。
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public abstract class PlayerControllerBase implements IPlayerController
{
	protected enum ACTION
	{
		LEFT,
		RIGHT,
		DASH,
		JUMP,
		ATTACK,
		KAGINAWA,
	}
	
	private IInputHelper inputHelpers[];
	
	/**
	 * コンストラクタ
	 */
	public PlayerControllerBase()
	{
		inputHelpers = new IInputHelper[ACTION.values().length];
		initialize();
	}

	@Override
	public final void update()
	{
		for(int i = 0;  i < inputHelpers.length;  ++i)
		{
			assert inputHelpers[i]!=null : "プレイヤーコントローラは正しく初期化されていません。(ACTION=" + ACTION.values()[i].toString() + ")";
			inputHelpers[i].update();
		}
	}

	@Override
	public final float moveLevel()
	{
		float result = 0.0f;
		if(inputHelpers[ACTION.LEFT.ordinal()].prs())		result -= 1.0f;
		if(inputHelpers[ACTION.RIGHT.ordinal()].prs())	result += 1.0f;
		return result;
	}

	@Override
	public final boolean dash()
	{
		return inputHelpers[ACTION.DASH.ordinal()].prs();
	}

	@Override
	public final boolean jump()
	{
		return inputHelpers[ACTION.JUMP.ordinal()].prs();
	}

	@Override
	public final boolean aerialJump()
	{
		return inputHelpers[ACTION.JUMP.ordinal()].trg();
	}

	@Override
	public final boolean attack()
	{
		return inputHelpers[ACTION.ATTACK.ordinal()].prs();
	}

	@Override
	public final boolean kaginawaThrow()
	{
		return inputHelpers[ACTION.KAGINAWA.ordinal()].trg();
	}
	
	@Override
	public final boolean kaginawaShrink()
	{
		return !inputHelpers[ACTION.KAGINAWA.ordinal()].prs();
	}

	@Override
	public final boolean kaginawaHang()
	{
		return inputHelpers[ACTION.KAGINAWA.ordinal()].prs();
	}

	@Override
	public final boolean kaginawaRelease()
	{
		// TODO 鉤縄を離す時の入力処理
		return false;
	}

	/**
	 * インスタンスを初期化する。
	 */
	protected abstract void initialize();
	
	/**
	 * キーボード入力を登録する。
	 * @param action		行動の種類
	 * @param key			キーコード(com.badlogic.gdx.Input.Keys)
	 * @see com.badlogic.gdx.Input.Keys
	 */
	protected final void registKey(ACTION action, int key)
	{
		inputHelpers[action.ordinal()] = new KeyInputHelper(key);
	}
	
	/**
	 * マウス入力を登録する。
	 * @param action		行動の種類
	 * @param mouse		マウスコード(com.badlogic.gdx.Input.Buttons)
	 * @see com.badlogic.gdx.Input.Buttons
	 */
	protected final void registMouse(ACTION action, int mouse)
	{
		inputHelpers[action.ordinal()] = new MouseInputHelper(mouse);
	}
	
	
	
	/** 
	 * 入力補助インタフェース
	 */
	private interface IInputHelper
	{
		/**
		 * 入力状態を更新する。
		 */
		void update();
		
		/**
		 * プレス入力を取得する。
		 * @return	プレス入力
		 */
		boolean prs();

		/**
		 * トリガ入力を取得する。
		 * @return	トリガ入力
		 */
		boolean trg();

		/**
		 * リリース入力を取得する。
		 * @return	リリース入力
		 */
		boolean rls();
	}
	
	/** 
	 * 入力補助基本クラス
	 */
	private abstract class InputHelperBase implements IInputHelper
	{
		/** 入力状態 */
		protected boolean prev, now;
		
		/**
		 * 入力状態を更新する。
		 * @param newInput		新しい入力状態
		 */
		protected final void update(boolean newInput)
		{
			prev = now;
			now = newInput;
		}

		@Override
		public final boolean prs()
		{
			return now;
		}

		@Override
		public final boolean trg()
		{
			return !prev && now;
		}

		@Override
		public final boolean rls()
		{
			return prev && !now;
		}
	}
	
	/** 
	 * キーボード入力補助クラス
	 */
	private final class KeyInputHelper extends InputHelperBase
	{
		/** キーコード */
		private final int key;
		
		/**
		 * コンストラクタ
		 * @param key		キーコード
		 */
		public KeyInputHelper(int key)
		{
			this.key = key;
		}

		@Override
		public final void update()
		{
			update(Gdx.input.isKeyPressed(key));
		}
	}
	
	/** 
	 * マウス入力補助クラス
	 */
	private final class MouseInputHelper extends InputHelperBase
	{
		/** ボタンコード */
		private final int button;
		
		/**
		 * コンストラクタ
		 * @param button		ボタンコード
		 */
		public MouseInputHelper(int button)
		{
			this.button = button;
		}

		@Override
		public final void update()
		{
			update(Gdx.input.isButtonPressed(button));
		}
	}
}

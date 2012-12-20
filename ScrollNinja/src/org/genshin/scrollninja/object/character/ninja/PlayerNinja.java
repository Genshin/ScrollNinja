package org.genshin.scrollninja.object.character.ninja;

import org.genshin.old.scrollninja.object.Background;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.character.ninja.controller.DefaultPlayerNinjaController;
import org.genshin.scrollninja.object.character.ninja.controller.NinjaControllerInterface;
import org.genshin.scrollninja.object.effect.AfterimageEffect;
import org.genshin.scrollninja.object.gui.Cursor;
import org.genshin.scrollninja.object.kaginawa.Kaginawa;
import org.genshin.scrollninja.render.RenderObjectFactory;
import org.genshin.scrollninja.render.RenderObjectInterface;
import org.genshin.scrollninja.utils.debug.DebugString;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


/**
 * プレイヤーが操作する忍者
 * @author	インターン生
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class PlayerNinja extends AbstractCharacter {
	/**
	 * コンストラクタ
	 * @param world		所属するWorldオブジェクト
	 * @param position	初期座標
	 * @param cursor	カーソルオブジェクト（鉤縄の向き計算用）
	 */
	public PlayerNinja(World world, Vector2 position, Cursor cursor) {
		super(world, position);
		
		// 初期座標設定
		getBody().setTransform(position, 0.0f);
		
		// フィールドの初期化
		controller = new DefaultPlayerNinjaController(this, cursor);
		kaginawa = new Kaginawa(world, getBody());
		restAerialJumpCount = NinjaParam.INSTANCE.AERIAL_JUMP_COUNT;
		groundedTimer = 0;
		worldGravity = world.getGravity().len();
		defaultFriction = getFootFixture().getFriction();

		state = new AerialState(this);
	}

	/**
	 * 更新処理
	 */
	public void update(float deltaTime)
	{
		// 操作状態を更新
		controller.update();

		// 状態に合わせた更新処理
		state = state.update(this, deltaTime);
		
		// 鉤縄を更新
		kaginawa.update(deltaTime);
		
		//---- 残像を付けてみる。
		new AfterimageEffect(getRenderObjects(), getPositionX(), getPositionY(), (float)Math.toDegrees(getBody().getAngle()));
		
		//---- デバッグ文字列
		DebugString.add("");
		DebugString.add("Ninja State : " + state.getClass().getSimpleName());
		DebugString.add("Ninja Position : " + getPositionX() + ", " + getPositionY());
		DebugString.add("Ninja Velocity : " + getBody().getLinearVelocity().x + ", " + getBody().getLinearVelocity().y + " (" + getBody().getLinearVelocity().len() + ")");
	}
	
	@Override
	public void render()
	{
		//---- 描画時の左右反転フラグを設定する。
		final Vector2 direction = controller.getDirection();
		final float angle = getBody().getAngle();
		final Vector2 upDirection = new Vector2(-(float)Math.sin(angle), (float)Math.cos(angle));
		flip(upDirection.crs(direction) < 0.0f, false);
		
		//---- 鉤縄を描画する。
		kaginawa.render();

		//---- 自身を描画する。
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
		state.collisionTerrain(this, contact);
	}
	
	@Override
	protected void initializeSprite()
	{
		addRenderObject(RenderObjectFactory.getInstance().get("NinjaFoot"));
		addRenderObject(RenderObjectFactory.getInstance().get("NinjaBody"));
		setAnimation("Stay");
	}

	@Override
	protected BodyDef createBodyDef()
	{
		BodyDef bd = super.createBodyDef();
		bd.fixedRotation = true;		// 回転しない
		bd.gravityScale = 0.0f;			// 重力は独自に与える
		return bd;
	}

	@Override
	protected void initializeFixture()
	{
		final float worldScale = GlobalParam.INSTANCE.WORLD_SCALE;
		
		// 下半身
		FixtureDef ffd = NinjaParam.INSTANCE.FOOT_FIXTURE_DEF_LOADER.createFixtureDef();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(15.0f * worldScale);
		ffd.shape = circleShape;
		
		createFixture(ffd);
		circleShape.dispose();
		
		// 上半身
		FixtureDef bfd = NinjaParam.INSTANCE.BODY_FIXTURE_DEF_LOADER.createFixtureDef();
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(16.0f * worldScale, 16.0f * worldScale, new Vector2(0.0f * worldScale, 16.0f * worldScale), 0.0f);
		bfd.shape = polygonShape;

		createFixture(bfd);
		polygonShape.dispose();
	}
	
	/**
	 * 移動する方向を更新する。
	 */
	void updateMoveDirection()
	{
		if(groundedTimer < NinjaParam.INSTANCE.GROUNDED_JUDGE_TIME-1 || frontDirection.x >= 0.0f)
			moveDirection = 1.0f;
		else
			moveDirection = -1.0f;
	}

	/**
	 * 上半身のアニメーションを設定する。
	 * @param name		アニメーションの名前
	 */
	void setBodyAnimation(String name)
	{
		getBodyRenderObject().setAnimation(name);
	}

	/**
	 * 下半身のアニメーションを設定する。
	 * @param name		アニメーションの名前
	 */
	void setFootAnimation(String name)
	{
		getFootRenderObject().setAnimation(name);
	}
	
	/**
	 * アニメーションを設定する。
	 * @param name		アニメーションの名前
	 */
	void setAnimation(String name)
	{
		setBodyAnimation(name);
		setFootAnimation(name);
	}
	
	/**
	 * 上半身の描画オブジェクトを取得する。
	 * @return		上半身の描画オブジェクト
	 */
	RenderObjectInterface getBodyRenderObject()
	{
		return getRenderObject(1);
	}
	
	/**
	 * 下半身の描画オブジェクトを取得する。
	 * @return		下半身の描画オブジェクト
	 */
	RenderObjectInterface getFootRenderObject()
	{
		return getRenderObject(0);
	}
	
	/**
	 * 上半身の衝突オブジェクトを取得する。
	 * @return		上半身の衝突オブジェクト
	 */
	Fixture getBodyFixture()
	{
		return getFixture(1);
	}
	
	/**
	 * 下半身の衝突オブジェクトを取得する。
	 * @return		下半身の衝突オブジェクト
	 */
	Fixture getFootFixture()
	{
		return getFixture(0);
	}
	
	/**
	 * 地面に立っているか調べる。
	 * @return		地面に立っている場合はtrue
	 */
	boolean isGrounded()
	{
		return groundedTimer > 0;
	}
	
	/** 忍者の操作を管理するオブジェクト */
	NinjaControllerInterface	controller;
	
	/** 鉤縄オブジェクト */
	Kaginawa	kaginawa;
	
	/** 空中でジャンプできる残り回数 */
	int	restAerialJumpCount;
	
	/** 正面方向を表すベクトル */
	final Vector2 frontDirection = new Vector2();
	
	/** ジャンプ方向を表すベクトル */
	final Vector2 jumpDirection = new Vector2(Vector2.Y);
	
	/** 世界の重力の強さ */
	final float worldGravity;
	
	/** 地面との接触フラグ */
	int groundedTimer;
	
	/** 移動する方向（天井に張り付いた状態から自然に移動する用） */
	float moveDirection = 0.0f;
	
	/** 摩擦のデフォルト値 */
	final float defaultFriction;
	
	/** 忍者の状態を管理するオブジェクト */
	private StateInterface		state;
	
	
	// TODO まだ触ってないソースとの互換性を保つためだけの実装。いずれ消し去る。
	public int GetChakra(){ return 1; }
	public int GetMaxChakra(){ return 1; }
	public int GetMaxHP() { return 1;}
	public float GetHP() { return 1; }
}

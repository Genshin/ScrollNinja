//******************************
//	Effect.java
//******************************

package org.genshin.old.scrollninja.object;

//========================================
// インポート
//========================================
import org.genshin.old.scrollninja.GameMain;
import org.genshin.old.scrollninja.object.item.Item;
import org.genshin.old.scrollninja.object.weapon.AbstractWeapon;
import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

//========================================
// クラス宣言
//========================================
public class Effect extends AbstractCollisionObject {

	//========================================
	// 定数宣言
	// エフェクトの種類
	//========================================
	public final static int FIRE_1		= 0;
	public final static int FIRE_2		= 1;
	public final static int FIRE_3		= 2;
	public final static int WATER_1		= 3;
	public final static int WATER_2		= 4;
	public final static int WATER_3		= 5;
	public final static int WIND_1		= 6;
	public final static int WIND_2		= 7;
	public final static int WIND_3		= 8;

	//変数宣言
	private AbstractCharacter	owner;			// 使用者
	private int				effectType;		// エフェクトの種類
	private int 			effectTime;		// 効果時間
	private float 			attackNum;		// 攻撃力
	private Vector2			position;		// 座標
	private float			stateTime;		//
	private boolean			useFlag;		// 使用フラグ
	private TextureRegion[]	frame;			// アニメーションのコマ
	private TextureRegion	nowFrame;		// 現在のコマ
	private Animation		animation;		// アニメーション

	/**************************************************
	 * @param type		エフェクトの種類
	 *
	 * コンストラクタ
	 ***************************************************/
	public Effect(int type, AbstractCharacter owner) {
		this.owner	= owner;
		effectType	= type;
		effectTime	= 0;
		stateTime	= 0;
		attackNum	= 0;
		position	= new Vector2(-100.0f, -100.0f);
		useFlag		= false;

		Create();
	}

	/**************************************************
	 * @return
	 *
	 * ゲッターまとめ
	 ***************************************************/
	public int GetType() { return effectType; }
	public int GetEffectTime() { return effectTime; }
	public float GetAttackNum(){ return attackNum; }
	public boolean GetUseFlag(){ return useFlag; }
	public AbstractCharacter GetOwner() { return owner; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
	public void SetUseFlag(boolean use) { useFlag = use; }
	public void SetAttackNum(int num){ attackNum = num; }
//	public void SetOwner(CharacterBase owner) { myOwner = owner; }

	/**************************************************
	 * エフェクト生成
	 ***************************************************/
	public void Create() {
		BodyDef bd = new BodyDef();
		PolygonShape poly = new PolygonShape();
		FixtureDef fd = new FixtureDef();;
		Texture texture;
		TextureRegion region;
		TextureRegion[][] tmp;
		int index;

		// TODO ここスッキリさせたい。武器エフェクト少ないからこのままでもよいのかな
		switch(effectType) {
		case FIRE_1:
			bd.type		= BodyType.DynamicBody;		// 動く物体
			bd.bullet	= true;

			// 当たり判定の作成
			poly.setAsBox(1.7f, 2.2f);

			// ボディ設定
			fd.density		= 0;
			fd.friction		= 0;
			fd.restitution	= 0;
			fd.shape		= poly;
			fd.isSensor		= true;

			// テクスチャの読み込み
			texture = new Texture(Gdx.files.internal("data/old/effect_fire.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			region = new TextureRegion(texture, 0, 0, 128, 128);

			// スプライトに反映
			sprites.add(new Sprite(region));
			sprites.get(0).setOrigin(sprites.get(0).getWidth() * 0.5f, sprites.get(0).getHeight() * 0.5f);
			sprites.get(0).setScale(GlobalParam.INSTANCE.WORLD_SCALE);

			// アニメーション
			tmp = TextureRegion.split(texture, 128, 128);
			frame = new TextureRegion[5];
			index = 0;
			for (int i = 0; i < 5; i++) {
				frame[index++] = tmp[0][i];
			}

			animation = new Animation(3.000f, frame);
			attackNum = 20.0f;		// TODO 武器の攻撃力にしないと…
			break;
		case FIRE_2:
			bd.type		= BodyType.DynamicBody;		// 動く物体
			bd.bullet	= true;

			// 当たり判定の作成
			poly.setAsBox(2.2f, 3f);

			// ボディ設定
			fd.density		= 0;
			fd.friction		= 0;
			fd.restitution	= 0;
			fd.shape		= poly;
			fd.isSensor		= true;

			// テクスチャの読み込み
			texture = new Texture(Gdx.files.internal("data/old/effect_fire.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			region = new TextureRegion(texture, 0, 0, 128, 128);

			// スプライトに反映
			sprites.add(new Sprite(region));
			sprites.get(0).setOrigin(sprites.get(0).getWidth() * 0.5f, sprites.get(0).getHeight() * 0.5f);
			sprites.get(0).setScale(GlobalParam.INSTANCE.WORLD_SCALE);

			// アニメーション
			tmp = TextureRegion.split(texture, 128, 128);
			frame = new TextureRegion[6];
			index = 0;
			for (int i = 1; i < 2; i++) {
				for (int j = 0; j < 6; j++) {
					if(index < 6)
						frame[index++] = tmp[i][j];
				}
			}

			animation = new Animation(3.000f, frame);
			attackNum = 50.0f;		// TODO 武器の攻撃力にしないと…
			break;
		case FIRE_3:
			break;
		case WATER_1:
			break;
		case WATER_2:
			break;
		case WATER_3:
			break;
		}
		
		createBody(GameMain.world, bd);
		createFixture(fd);
	}

	/**************************************************
	 * 更新処理
	 ***************************************************/
	public void Update(boolean use) {
		Body body = getBody();
		
		useFlag = use;
		if( useFlag ) {
			nowFrame = animation.getKeyFrame(stateTime, true);
			stateTime++;

			body.setTransform(owner.position.x +
								owner.direction * 3f,
									owner.position.y + 4, 0);
			position = body.getPosition();
			// 64はTextureRegionの幅÷２。後は微調整
			sprites.get(0).setPosition(position.x - 64 - (1 * owner.direction), position.y - 64 + 1);
			sprites.get(0).setScale(-owner.direction * GlobalParam.INSTANCE.WORLD_SCALE, GlobalParam.INSTANCE.WORLD_SCALE);
			sprites.get(0).setRegion(nowFrame);

			animation();
		}
		// 画面外へ
		else {
			stateTime = 0;
			body.setTransform( -100.0f, -100.0f, 0.0f);
			position = body.getPosition();
			sprites.get(0).setPosition(position.x - 100, position.y);
		}
	}

	/**
	 * スプライトを描画する。
	 */
	public void render()
	{
		if( !useFlag )
			return;
		
		super.render();
	}

	/**************************************************
	 * アニメーション処理
	 ***************************************************/
	private void animation() {
		switch(effectType) {
		case FIRE_1:
			break;
		case FIRE_2:
			break;
		case FIRE_3:
			break;
		case WATER_1:
			break;
		case WATER_2:
			break;
		case WATER_3:
			break;
		}
	}

	@Override
	public void dispatchCollision(AbstractCollisionObject object, Contact contact) {
		object.notifyCollision(this, contact);
	}

	@Override
	public void notifyCollision(Background obj, Contact contact){}

	@Override
	public void notifyCollision(PlayerNinja obj, Contact contact){}

	@Override
	public void notifyCollision(Enemy obj, Contact contact){}

	@Override
	public void notifyCollision(Effect obj, Contact contact){}

	@Override
	public void notifyCollision(Item obj, Contact contact){}

	@Override
	public void notifyCollision(StageObject obj, Contact contact){}

	@Override
	public void notifyCollision(AbstractWeapon obj, Contact contact){}
}
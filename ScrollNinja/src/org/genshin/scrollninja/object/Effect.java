//******************************
//	Effect.java
//******************************

package org.genshin.scrollninja.object;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.ScrollNinja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

//========================================
// クラス宣言
//========================================
public class Effect extends ObjectBase {

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
	private CharacterBase	owner;			// 使用者
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
	public Effect(int type, CharacterBase owner) {
		this.owner	= owner;
		sprite		= new ArrayList<Sprite>();
		sensor		= new ArrayList<Fixture>();
		effectType	= type;
		effectTime	= 0;
		stateTime	= 0;
		attackNum	= 0;
		position	= new Vector2(-100.0f, -100.0f);
		useFlag		= false;

		Create();
		sensor.get(0).setUserData(this);
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
	public CharacterBase GetOwner() { return owner; }

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
		BodyDef def;
		PolygonShape poly;
		FixtureDef fd;
		Texture texture;
		TextureRegion region;
		TextureRegion[][] tmp;
		int index;

		// TODO ここスッキリさせたい。武器エフェクト少ないからこのままでもよいのかな
		switch(effectType) {
		case FIRE_1:
			def	= new BodyDef();
			def.type	= BodyType.DynamicBody;		// 動く物体
			body		= GameMain.world.createBody(def);

			// 当たり判定の作成
			poly		= new PolygonShape();
			poly.setAsBox(1.7f, 2.2f);

			// ボディ設定
			fd	= new FixtureDef();
			fd.density		= 50;
			fd.friction		= 0;
			fd.restitution	= 0;
			fd.shape		= poly;

			sensor.add(body.createFixture(poly, 0));
			sensor.get(0).setSensor(true);
			body.setBullet(true);			// すり抜け防止

			// テクスチャの読み込み
			texture = new Texture(Gdx.files.internal("data/effect_fire.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			region = new TextureRegion(texture, 0, 0, 128, 128);

			// スプライトに反映
			sprite.add(new Sprite(region));
			sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
			sprite.get(0).setScale(ScrollNinja.scale);

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
			def	= new BodyDef();
			def.type	= BodyType.DynamicBody;		// 動く物体
			body		= GameMain.world.createBody(def);

			// 当たり判定の作成
			poly		= new PolygonShape();
			poly.setAsBox(2.2f, 3f);

			// ボディ設定
			fd	= new FixtureDef();
			fd.density		= 50;
			fd.friction		= 0;
			fd.restitution	= 0;
			fd.shape		= poly;

			sensor.add(body.createFixture(poly, 0));
			sensor.get(0).setSensor(true);
			body.setBullet(true);			// すり抜け防止

			// テクスチャの読み込み
			texture = new Texture(Gdx.files.internal("data/effect_fire.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			region = new TextureRegion(texture, 0, 0, 128, 128);

			// スプライトに反映
			sprite.add(new Sprite(region));
			sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
			sprite.get(0).setScale(ScrollNinja.scale);

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
	}

	/**************************************************
	 * 更新処理
	 ***************************************************/
	public void Update(boolean use) {
		useFlag = use;
		if( useFlag ) {
			nowFrame = animation.getKeyFrame(stateTime, true);
			stateTime++;

			body.setTransform(owner.position.x +
								owner.direction * 3f,
									owner.position.y + 4, 0);
			position = body.getPosition();
			// 64はTextureRegionの幅÷２。後は微調整
			sprite.get(0).setPosition(position.x - 64 - (1 * owner.direction), position.y - 64 + 1);
			sprite.get(0).setScale(-owner.direction * ScrollNinja.scale, ScrollNinja.scale);
			sprite.get(0).setRegion(nowFrame);

			animation();
		}
		// 画面外へ
		else {
			stateTime = 0;
			body.setTransform( -100.0f, -100.0f, 0.0f);
			position = body.getPosition();
			sprite.get(0).setPosition(position.x - 100, position.y);
		}
	}

	/**
	 * スプライトを描画する。
	 */
	public void Draw()
	{
		if( useFlag ) {
			Vector2 pos = body.getPosition();
			float rot = (float) Math.toDegrees(body.getAngle());

			int count = sprite.size();
			for (int i = 0; i < count; ++i)
			{
				Sprite current = sprite.get(i);
				// 座標・回転
				current.setPosition(pos.x - current.getOriginX(), pos.y - current.getOriginY());
				current.setRotation(rot);
				// 描画
				current.draw(GameMain.spriteBatch);
			}
		}
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
	public void collisionDispatch(ObjectBase obj, Contact contact) {
		obj.collisionNotify(this, contact);
	}

	@Override
	public void collisionNotify(Background obj, Contact contact){}

	@Override
	public void collisionNotify(Player obj, Contact contact){}

	@Override
	public void collisionNotify(Enemy obj, Contact contact){}

	@Override
	public void collisionNotify(Effect obj, Contact contact){}

	@Override
	public void collisionNotify(Item obj, Contact contact){}

	@Override
	public void collisionNotify(StageObject obj, Contact contact){}

	@Override
	public void collisionNotify(WeaponBase obj, Contact contact){}
}
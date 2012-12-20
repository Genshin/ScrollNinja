package org.genshin.old.scrollninja.object;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import org.genshin.old.scrollninja.GameMain;
import org.genshin.old.scrollninja.MainMenu;
import org.genshin.old.scrollninja.object.StageDataList.StageData;
import org.genshin.old.scrollninja.object.item.Item;
import org.genshin.old.scrollninja.object.weapon.AbstractWeapon;
import org.genshin.scrollninja.object.AbstractCollisionObject;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.utils.TextureFactory;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;

// メモ
// このクラスは、背景を読み込んで表示するところ。
// ステージの当たり判定もこのクラスが持っています。
// その他の敵の出現位置などの細かな情報はステージクラスが持っています
// ↑StageDataListにまとめました

// 10/9 背景移動追加、カメラ座標のゲッター追加

//========================================
// クラス宣言
//========================================
public class Background extends AbstractCollisionObject {
	//========================================
	// 定数宣言
	// spriteの配列
	//========================================
	public final static int	FAR				= 0;
	public final static int	MAIN			= 1;
	public final static int	NEAR			= 2;

	// 変数宣言
	private int				stageNum;		// ステージ番号
	private StageData 		stageData;		// ステージのデータ
	
	private final Vector2 lightAnimVelocity = new Vector2(-0.0005f, -0.0005f);

	/**
	 *  コンストラクタ
	 *  @param num			ステージ番号
	 *  @param createFlag	bodyを作成するかどうか
	 */
	public Background(int num, boolean createFlag) {
		stageNum = num;
		stageData = StageDataList.lead(stageNum);

		LoadTexture();
		// MainMenuではcreateしない
		if (createFlag)
			createBody();
	}

	/**************************************************
	 * @Override
	 * @param i		スプライト番号
	 * @param flag	MainMenuで描画するにはこれが必要
	 *
	 * 描画処理
	 ***************************************************/
	public void Draw(int i, boolean flag) {
		sprites.get(i).draw(MainMenu.spriteBatch);
	}

	/**************************************************
	 * @Override
	 * @param i		スプライト番号
	 *
	 * 描画処理
	 ***************************************************/
	public void Draw(int i) {
		sprites.get(i).draw(GameMain.spriteBatch);
	}

	/**************************************************
	 * テクスチャ読み込み、スプライトセット
	 ***************************************************/
	public void LoadTexture() {
		for(StageData.LayerData layer : stageData.layerData)
		{
			for(StageData.LayerData.BGData bg : layer.bgData)
			{
				Texture texture = TextureFactory.getInstance().get( bg.textureFileName );
				Sprite sprite = new Sprite(texture);
				sprite.setBounds(bg.position.x * layer.scale, bg.position.y * layer.scale, bg.size.x * layer.scale, bg.size.y * layer.scale);
				sprites.add(sprite);
			}
		}
		
		// 透明度（仮）
		Sprite sprite = sprites.get(6);
		sprite.setColor(1.0f, 1.0f, 1.0f, 0.25f);
		sprite.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		sprite.setRegion(0.0f, 0.0f, 8.0f, 8.0f);
//		sprites.get(8).setColor(1.0f, 1.0f, 1.0f, 0.5f);
	}

	/**************************************************
	 * 当たり判定作成
	 ***************************************************/
	public void createBody() {
		// ボディタイプ設定
		BodyDef bd	= new BodyDef();
		bd.type		= BodyType.StaticBody;		// 動かない物体

		// ボディ設定
		FixtureDef fd	= TerrainParam.INSTANCE.FIXTURE_DEF_LOADER.createFixtureDef();

		// ボディ作成
		createBody(GameMain.world, bd);
		createFixtureFromFile(fd, stageData.collisionData.fileName, stageData.collisionData.bodyName, stageData.size.x);
	}

	/**************************************************
	 * 更新処理
	 ***************************************************/
	public void update(float deltaTime) {
		final Camera camera = GameMain.camera;
		final Vector2 stageSize = stageData.size;
		final int layerCount = stageData.layerData.size();

		final float cameraXRatio = (camera.position.x - camera.viewportWidth * 0.5f) / (stageSize.x - camera.viewportWidth);
		final float cameraYRatio = (camera.position.y - camera.viewportHeight * 0.5f) / (stageSize.y - camera.viewportHeight);
		
		for(int i = 0;  i < layerCount;  ++i)
		{
			final float layerScale = stageData.layerData.get(i).scale;
			final Vector2 bgPosition = stageData.layerData.get(i).bgData.get(0).position;
			final Sprite sprite = sprites.get(i);

			sprite.setPosition(
				cameraXRatio * stageSize.x * (1.0f - layerScale) + bgPosition.x * layerScale,
				cameraYRatio * stageSize.y * (1.0f - layerScale) + bgPosition.y * layerScale
			);
		}
		
		final Sprite sprite = sprites.get(6);
		sprite.setU(sprite.getU() + lightAnimVelocity.x);
		sprite.setU2(sprite.getU2() + lightAnimVelocity.x);
		sprite.setV(sprite.getV() + lightAnimVelocity.y);
		sprite.setV2(sprite.getV2() + lightAnimVelocity.y);
	}

	@Override
	public void dispatchCollision(AbstractCollisionObject object, Contact contact) {
		object.notifyCollision(this, contact);
	}

	@Override
	public void notifyCollision(PlayerNinja obj, Contact contact) {

	}

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

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
/*	public Sprite GetSprite(int i) { return sprite.get(i); }
	public Body GetBody() { return body; }
	public Fixture GetSensor(int i) { return sensor.get(i); }*/
	//public Vector2 GetCamPos() { return cameraPos; }
	public Background GetBackground(){ return this; }
	public int GetBackgroundNum(){ return stageNum; }
	public ArrayList<Sprite> getSprites(){ return sprites; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
/*	public static void SetSprite(Sprite sp) { sprite.add(sp); }
	public static void SetBody(Body bd) { body = bd; }
	public static void SetFixture(Fixture ss){ sensor.add(ss); }
*/
}
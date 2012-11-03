package org.genshin.scrollninja.object;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import org.genshin.scrollninja.GameMain;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

//========================================
// クラス宣言
//========================================
public abstract class ObjectBase {
	protected Body 					body;			// 当たり判定用BOX
	protected ArrayList<Sprite> 	sprite;		// スプライト			TODO 変数名変更する。
	protected ArrayList<Fixture> 	sensor;		// センサー				TODO 変数名変更する。

	/**
	 * コンストラクタ
	 * TODO 将来的には引数にWorldを投げて初期化させる？
	 */
	ObjectBase()
	{
		// フィールドの初期化
		body = null;
		sprite = new ArrayList<Sprite>();
		sensor = new ArrayList<Fixture>();
	}

	//************************************************************
	// Release
	// 開放処理まとめ
	//************************************************************
	public void Release(){
		GameMain.world.destroyBody(body);
		body = null;
		sprite.clear();
		sensor.clear();
	}

	/**
	 * オブジェクトを更新する。
	 */
	public void Update() {}

	/**
	 * スプライトを描画する。
	 */
	public void Draw()
	{
		// アクティブでなければ描画しない
		if(!body.isActive())
			return;
		
		// 描画処理
		SpriteBatch sb = GameMain.spriteBatch;
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
			current.draw(sb);
		}
	}

	/**
	 * 衝突を振り分ける。
	 * @param obj			衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	protected void collisionDispatch(ObjectBase obj, Contact contact)
	{
		// FIXME いずれ抽象メソッド化する。
	}

	/**
	 * 背景との衝突を通知する。
	 * @param obj			衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	protected void collisionNotify(Background obj, Contact contact){}

	/**
	 * プレイヤーとの衝突を通知する。
	 * @param obj		衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	protected void collisionNotify(Player obj, Contact contact){}

	/**
	 * 敵との衝突を通知する。
	 * @param obj			衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	protected void collisionNotify(Enemy obj, Contact contact){}

	/**
	 * エフェクトとの衝突を通知する。
	 * TODO 「エフェクトと衝突する」って捉え方はなんかおかしい気がする
	 * @param obj			衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	protected void collisionNotify(Effect obj, Contact contact){}

	/**
	 * アイテムとの衝突を通知する。
	 * @param obj		衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	protected void collisionNotify(Item obj, Contact contact){}

	/**
	 * ステージオブジェクトとの衝突を通知する。
	 * @param obj			衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	protected void collisionNotify(StageObject obj, Contact contact){}

	/**
	 * 武器との衝突を通知する。
	 * @param obj			衝突したオブジェクト
	 * @param contact	衝突情報
	 */
	protected void collisionNotify(WeaponBase obj, Contact contact){}

	/**
	 * スプライト反転フラグを設定する。
	 * @param x		x方向の反転フラグ
	 * @param y		y方向の反転フラグ
	 */
	protected final void flip(boolean x, boolean y)
	{
		int count = sprite.size();
		for(int i = 0; i < count;  ++i)
		{
			sprite.get(i).flip(x, y);
		}
	}

	/**
	 * 座標を取得する。
	 * @return	座標
	 */
	public final Vector2 getPosition()
	{
		return body.getPosition();
	}
}

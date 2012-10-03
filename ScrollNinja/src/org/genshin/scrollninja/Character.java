package org.genshin.scrollninja;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Character {
	private String name;
	private int hp;
	private int mp;
	private int lv;
	private int exp;
	private ArtsFormProto form;
	private WeaponProto primaryWeapon;
	private WeaponProto secondaryWeapon;
	private ArrayList<WeaponProto> weapons;
	private ArrayList<ItemProto> items;
	private ArrayList<ArtsFormProto> knownForms;
	private Vector2 position;
	//private Boolean jump;

	private Sprite sprite;
	private Texture texture;
	private Body body;
	private Fixture sensor;

	private Vector2 tmpPosition;
	// ジャンプ処理用
	private Boolean isGround = false;
	private int jump = 2;  // 0:地面　1:ジャンプ中　2:落下中
	private int jumpHeight = 0;

	// 定数
	private final static int DEFAULT_HP = 100;
	private final static int DEFAULT_MP = 10;

	// コンストラクタ
	public Character() {
		this.name = "";
		this.hp = DEFAULT_HP;
		this.mp = DEFAULT_MP;
		this.lv = 1;
		this.exp = 0;
		this.form = new ArtsFormProto();
		this.primaryWeapon = new WeaponProto();
		this.secondaryWeapon = this.primaryWeapon;
		this.weapons = new ArrayList<WeaponProto>();
		this.items = new ArrayList<ItemProto>();
		this.knownForms = new ArrayList<ArtsFormProto>();
		this.position = new Vector2(0, 0);
		//this.jump = false;
	}

	// テクスチャ貼り付け
	public void createSprite() {
		// テクスチャ読み込み
		texture = new Texture(Gdx.files.internal("data/chara.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);
		// キャラクタースプライトにセット
		sprite = new Sprite(region);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}

	// 当たり判定作成
	public void createBox(World world) {
		// Bodyタイプ
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		body = world.createBody(def);

		// 物体の形
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(16, 24);
		// Bodyの設定
		FixtureDef fd = new FixtureDef();
		fd.density = 50;				// 密度
		fd.friction = 100f;				// 摩擦
		fd.restitution = 0;				// 反発係数
		fd.shape = poly;

		// 設定を入れる
		body.createFixture(fd);
		// 当たり判定用
		sensor = body.createFixture(poly, 0);
		poly.dispose();

		body.setBullet(true);

		// 初期位置
		body.setTransform(0, 300, 0);
	}

	// キャラクター移動
	public void move(World world) {
		// キー入力
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			position = body.getPosition();
			position.x -= 5;
			body.setTransform(position, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			position = body.getPosition();
			position.x += 5;
			body.setTransform(position, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			if (jump == 0) {
				jumpHeight = 0;
				jump = 1;
				//sound.play(1.0f);
			}
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
		}

		// 現在位置再習得
		position = body.getPosition();

		// ジャンプ中
		if (jump == 1) {
			// 上昇中
			position = body.getPosition();
			position.y += 15;
			body.setTransform(position, 0);
			jumpHeight += 15;
			// 落下
			if (jumpHeight > 300)
				jump = 2;
			// 画面外に行かないよう制限
			position = body.getPosition();
			if (position.y > 1024) {
				position.x = body.getPosition().x;
				position.y = 1024;
				body.setTransform(position, 0);
			}
		}

		// 重力が思ったようにきかないのでとりあえず…
		isGround = isPlayerGrounded(world);
		if (!isGround && jump == 2) {
			position = body.getPosition();
			position.y -= 5;
			body.setTransform(position, 0);
		}
	}

	// 攻撃
	public void attack() {
	}

	// 武器変更
	public void changeWeapon() {
	}

	// アイテム使用
	public void useItem() {
	}

	// アニメーション
	public void animation() {
	}

	// プレイヤーが地面にいるかいないか
	private boolean isPlayerGrounded(World world) {
		// リスト取得
		List<Contact> contactList = world.getContactList();
		for(int i = 0; i < contactList.size(); i++) {
			Contact contact = contactList.get(i);
			// プレイヤーと接触しているかいないか
			if(contact.isTouching() && (contact.getFixtureA() == sensor ||
				contact.getFixtureB() == sensor)) {
				// 地面に接触したらジャンプはリセット
				if (jump != 0)
					jump = 0;
				return true;
			}
		}
		// ジャンプ（上昇）中でない、地面に接触していないのなら落下中
		if (jump != 1)
			jump = 2;
		return false;
	}

	// 位置ゲット
	public Vector2 getPosition() {
		return this.position;
	}

	// Spriteゲット
	public Sprite getSprite() {
		return this.sprite;
	}

	// Bodyゲット
	public Body getBody() {
		return this.body;
	}
}

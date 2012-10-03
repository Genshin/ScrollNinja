package org.genshin.scrollninja;

import com.badlogic.gdx.math.Vector2;

public class EnemyProto {
	private int hp;
	private float speed;
	private int power;
	private int haveExp;
	private Vector2 position;

	// 定数
	private final static int DEFAULT_HP = 10;
	private final static float DEFAULT_SPEED = 0.5f;
	private final static int DEFAULT_POWER = 1;
	private final static int DEFAULT_HAVE_EXP = 1;

	// コンストラクタ
	public EnemyProto() {
		this.hp = DEFAULT_HP;
		this.speed = DEFAULT_SPEED;
		this.power = DEFAULT_POWER;
		this.haveExp = DEFAULT_HAVE_EXP;
		this.position = new Vector2(0, 0);
	}

	// 移動
	public void move() {

	}

	// 攻撃
	public void attack() {

	}

	// アニメーション
	public void animation() {

	}
}

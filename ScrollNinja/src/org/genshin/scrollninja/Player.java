package org.genshin.scrollninja;

import java.util.ArrayList;

public class Player {
	private String userName;
	private Character curCharacter;
	private ArrayList<Character> characters;

	// コンストラクタ
	public Player() {
		this.userName = "";
		this.curCharacter = new Character();
		this.characters = new ArrayList<Character>();
	}

	// キャラクター変更
	public void changeCharacter() {
	}

	// ユーザー名セット
	public void setUserName(String name) {
		this.userName = name;
	}

	// ユーザー名ゲット
	public String getUserName() {
		return this.userName;
	}
}
package org.genshin.scrollninja;

import org.genshin.scrollninja.object.Background;


// TODO ArrayListとして保持しておく必要はない？　というか無駄になるからステージ毎にCreateして１つのみにした方がよさげ？
// 一度書き換えてみる。古いデータは保存中
// マネージャクラスも不要になるかも
public class BackgroundManager {
	public static Background backgroundList = null;

	// コンストラクタ
	private BackgroundManager(){}

	/**************************************************
	 * CreateBackground
	 * @param num		ステージ番号
	 * @param flag		bodyを作成するかどうか
	 * 作成
	 ***************************************************/
	public static void CreateBackground(int num, boolean createFlag) {
		backgroundList = new Background(num, createFlag);
	}

	/**************************************************
	 * GetBackground
	 * @param num		ステージ番号
	 * 参照
	 ***************************************************/
	public static Background GetBackground(){
		return backgroundList;
	}

	/**************************************************
	 * dispose
	 * 解放処理
	 ***************************************************/
	public static void dispose() {
		if (backgroundList != null)
			backgroundList.Release();
		backgroundList = null;
	}
}
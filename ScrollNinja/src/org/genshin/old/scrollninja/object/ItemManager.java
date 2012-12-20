package org.genshin.old.scrollninja.object;

import java.util.ArrayList;

import org.genshin.old.scrollninja.object.item.Item;

// TODO おにぎり以外のアイテムはどうしようか
// これもエネミーと同じように一つのリストにまとめてもよいかもしれない
public class ItemManager {

	private static ArrayList<Integer>	itemList		= new ArrayList<Integer>();	// アイテムリスト
	private static ArrayList<Item>		onigiriList		= new ArrayList<Item>();	// 管理番号
//	private static ArrayList<Item>

	/**
	 * コンストラクタ
	 */
	private ItemManager(){}

	/**
	 * 更新
	 * @param deltaTime TODO
	 */
	public static void Update(float deltaTime) {
		if (onigiriList != null) {
			for( int i = 0; i < onigiriList.size(); i ++) {
				onigiriList.get(i).update(deltaTime);
			}
		}
	}

	/**
	 * スプライト描画
	 */
	public static void Draw() {
		if (onigiriList != null) {
			for( int i = 0; i < onigiriList.size(); i ++) {
				onigiriList.get(i).render();
			}
		}
	}

	/**
	 * アイテム生成
	 * 同じ種類が既にある場合は最後に追加
	 * まだその種類がリストにない場合は新規で追加
	 */
	public static void CreateItem(int Type, float x, float y) {
		// 同じ種類のアイテムがないか探す
		for(int i = 0; i < itemList.size(); i ++ ) {
			// 同じ種類発見
			if( itemList.get(i).equals(Type) ) {

				switch( Type ) {
				case Item.ONIGIRI:
					int j = onigiriList.size() + 1;
					Item pItem = new Item(Type, j, x, y);		// 最後の番号を管理番号に
					onigiriList.add(pItem);					// 追加
				break;
				}

				return;
			}
		}
		// なかった
		itemList.add(Type);						// この種類の項目を増やす
		Item pItem = new Item(Type, 1, x, y);	// 最初の一つ目なので管理番号は１

		switch( Type ) {
		case Item.ONIGIRI:
			onigiriList.add(pItem);					// 追加
			break;
		}

		return;
	}

	/**
	 * 削除とソート
	 * @param Type		アイテムの種類
	 * @param Num		管理番号
	 */
	public static void DeleteItem(int Type, int Num) {
		for(int i = 0; i < itemList.size(); i ++ ) {

			// 発見
			if( itemList.get(i).equals(Type) ) {
				for( int j = 0; j < onigiriList.size(); j ++ ) {
					if( onigiriList.get(j).GetNum() == Num ) {
						onigiriList.get(j).dispose();
						onigiriList.remove(j);					// 削除！
					}
				}

				// 削除に合わせて管理番号変更。とりあえず全部
				for( int j = 0; j < onigiriList.size(); j ++ ) {
					onigiriList.get(j).SetNum(j + 1);
				}
			}
		}
	}

	/**
	 * 削除とソート
	 * @param item		削除するアイテムのポインタ
	 */
	public static void DeleteItem(Item item) {
		for(int i = 0; i < itemList.size(); i ++ ) {

			// 発見
			if( itemList.get(i).equals(item.GetType()) ) {
				for( int j = 0; j < onigiriList.size(); j ++ ) {
					if( onigiriList.get(j).GetNum() == item.GetNum() ) {
						onigiriList.get(j).dispose();
						onigiriList.remove(j);					// 削除！
					}
				}

				// 削除に合わせて管理番号変更。とりあえず全部
				for( int j = 0; j < onigiriList.size(); j ++ ) {
					onigiriList.get(j).SetNum(j + 1);
				}
			}
		}
	}

	/**
	 * 解放処理
	 */
	public static void dispose() {
		itemList = new ArrayList<Integer>();;
		if (onigiriList != null) {
			for (int i = 0; i < onigiriList.size(); i++) {
				onigiriList.get(i).dispose();
			}
		}
		onigiriList = new ArrayList<Item>();
	}
}
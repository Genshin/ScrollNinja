package org.genshin.old.scrollninja;

import java.util.ArrayList;

public class StructObjectManager {
	public static ArrayList<StructObject> objectList		= new ArrayList<StructObject>();
	
	/**
	 * コンストラクタ
	 */
	StructObjectManager(){}
	
	/**
	 * 更新
	 */
	public static void Update( int priority) {
		for( int i = 0; i < objectList.size(); i ++ ) {
			objectList.get(i).Update(priority);
		}
	}
	
	/**
	 * 描画
	 */
	public static void Draw() {
		for( int i = 0; i < objectList.size(); i ++ ) {
			objectList.get(i).Draw();
		}
	}
	
	/**
	 * 描画
	 */
	public static void Draw(int i) {
		objectList.get(i).Draw();
	}
	
	/**
	 * 生成
	 * @param type			種類
	 */
	public static void CreateStructObject( int type ) {
		StructObject pStructObject = new StructObject(type);
		objectList.add(pStructObject);
	}
	
	/**
	 * 生成
	 * @param type			種類
	 * @param x				X座標
	 * @param y				Y座標
	 * @param priority		優先度
	 */
	public static void CreateStructObject( int type, float x, float y, int priority ) {
		StructObject pStructObject = new StructObject(type, x, y, priority);
		objectList.add(pStructObject);
	}
	
	/**
	 * 削除
	 */
	public static void DeleteStructObject() {
		for( int i = objectList.size(); i > 0; i -- ) {
			objectList.get(i - 1).Release();
			objectList.remove(i - 1);
		}
	}
	
	/**
	 * 削除
	 */
	public static void DeleteStructObject( int i ) {
		objectList.get(i).Release();
		objectList.remove(i);
	}
	
	/**
	 * リストサイズを返す
	 * @return
	 */
	public static int GetListSize() { return objectList.size(); }
	
	/**
	 * 指定された番号のオブジェクトを返す
	 * @param i		番号
	 * @return
	 */
	public static StructObject GetStructObject(int i) { return objectList.get(i); }
}

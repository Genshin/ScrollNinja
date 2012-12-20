package org.genshin.old.scrollninja;

import java.awt.Image;
import java.util.ArrayList;

//========================================
// 画像管理クラス
//========================================
public class StructImageManager {
	private static ArrayList<StructImage> imageList = new ArrayList<StructImage>();
	
	/**
	 * コンストラクタ
	 */
	private StructImageManager() {}
	
	/**
	 * 画像生成
	 */
	public static void CreateStructImage( String filePath ) {
		StructImage pStructImage = new StructImage( filePath );
		imageList.add(pStructImage);
;		}
	
	/**
	 * 配列の大きさを返す　
	 */
	public static int GetListSize() {
		return imageList.size();
	}
	
	/**
	 * 画像を返す
	 */
	public static Image GetImage( int i ) {
		return imageList.get(i).GetImage();
	}
	
}

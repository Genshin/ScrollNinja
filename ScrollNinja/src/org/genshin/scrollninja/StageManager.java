package org.genshin.scrollninja;

//========================================
//インポート
//========================================
import java.util.ArrayList;

//========================================
// クラス宣言
//========================================
//***** シングルトン *****/
public class StageManager {
	
	private static final StageManager Instance = new StageManager();			// このクラスの唯一のインスタンスを作ります
	
	// インスタンスを返す
	public static StageManager GetInstace() {
		return Instance;
	}
	
	private ArrayList<Stage> stageList;				// ステージリスト
	
	// コンストラクタ
	private StageManager(){
		stageList = new ArrayList<Stage>();
	}
	
	// ステージの生成
	public int CreateStage(String Name) {
		if( stageList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}
		
		Stage pStage = new Stage(Name);			// オブジェクトを生成（&初期化）して
		stageList.add(pStage);					// リストに追加
		
		return 1;
	}
	
	// ステージの削除
	public int DeleteStage(String Name) {
		if( !stageList.contains(Name) ) {		// 名前が見つからなかった場合はエラー
			return -1;		// エラー処理
		}
		
		stageList.remove(stageList.indexOf(Name));		// 引数で渡されたオブジェクトを削除
		return 1;
	}
	
	// 参照
	public Stage GetStage(String Name) {
		return stageList.get(stageList.indexOf(Name));				// 引数で渡されたオブジェクトのポインタを返す
	}
}

package org.genshin.scrollninja;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class FileOperation {
	public static ArrayList<StructObject> objectList		= new ArrayList<StructObject>();
	
	/**
	 * コンストラクタ
	 */
	private FileOperation(){}
	
	/**
	 * ファイル読み込み
	 * @param filePath		読み込むデータのファイルパス
	 */
	public static void LoadFile(String filePath) {
        int i = 1;
        int type = 0, priority = 0;
        float positionX = 0,positionY = 0;
		
        try {
        	FileReader fr		= new FileReader(filePath);		// FileReaderオブジェクトの作成
        	BufferedReader br	= new BufferedReader(fr);		// バッファ
        	StreamTokenizer st	= new StreamTokenizer(br);     	// StreamTokenizerオブジェクトの作成
        	st.wordChars('/', '/');				// 文字認識記号の設定
        	st.whitespaceChars('/', '/');		// 区切り文字の設定
        	st.eolIsSignificant(true);			// 改行の検知

        	// ファイルの終わりに達するとTT_EOFが返されるので、そこでループ終了
        	while( st.nextToken() != StreamTokenizer.TT_EOF) {
        		switch(st.ttype) {
        		// 改行
        		case StreamTokenizer.TT_EOL:
        			i = 1;
        			break;
        		// スラッシュ（文字）検知
/*        		case StreamTokenizer.TT_WORD:
        			i ++;
        			break;*/
        		case StreamTokenizer.TT_NUMBER:
					switch(i) {
        				case 1:
        					type = (int)st.nval;
        					i ++;
        					break;
        				case 2:
        					positionX = (float)st.nval;
        					i ++;
        					break;
        				case 3:
        					positionY = (float)st.nval;
        					i ++;
        					break;
        				case 4:
        					priority = (int)st.nval;
                    		StructObject pStructObject = new StructObject();
                    		pStructObject.type = type;
                    		pStructObject.positionX = positionX;
                    		pStructObject.positionY = positionY;
                    		pStructObject.priority = priority;
                    		
                    		objectList.add(pStructObject);
        					break;
            			default:
            				break;
        			}
        			break;
        		}
        	}
        	fr.close();
        }
        catch(Exception e) {
        	System.out.println(e);  //エラーが起きたらエラー内容を表示
        }
	}

	/**
	 * ファイル書き出し
	 * @param fileName		書き出すファイル名
	 */
	public static void ExportFile(String fileName) {
		File file = new File(fileName);
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			
			for( int i = 0; i < objectList.size(); i ++ ) {
				pw.print(objectList.get(i).type);
				pw.print('/');
				pw.print((int)objectList.get(i).positionX);
				pw.print('/');
				pw.print((int)objectList.get(i).positionY);
				pw.print('/');
				pw.println(objectList.get(i).priority);
			}
			pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
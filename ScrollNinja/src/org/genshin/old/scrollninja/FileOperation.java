package org.genshin.old.scrollninja;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

import javax.imageio.ImageIO;


public class FileOperation {
		
	/**
	 * コンストラクタ
	 */
	private FileOperation(){}
	
	/**
	 * 保存
	 */
	public static void Save() {
		FileDialog f_dialog = new FileDialog(new Frame() , "FileDialog" ,FileDialog.SAVE);
		f_dialog.setVisible(true);
		
//		if( f_dialog.SAVE) {
			String path = new String();
			path = f_dialog.getDirectory() + f_dialog.getFile();
			System.out.println(path);
			ExportFile(path);
//		}
//		f_dialog.dispose();
	}
	
	/**
	 * ロード
	 */
	public static void Load() {
		FileDialog f_dialog = new FileDialog(new Frame() , "FileDialog" ,FileDialog.LOAD);
		f_dialog.setVisible(true);
		
		String path = new String();
		path = f_dialog.getDirectory() + f_dialog.getFile();
		LoadFile(path);
	}
	
	/**
	 * 画像読み込み
	 */
	public static BufferedImage LoadImage(String filePath) {
		try {
			FileInputStream in = new FileInputStream(filePath);
			BufferedImage 	rv = ImageIO.read(in);
			in.close();
			return rv;
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ファイル読み込み
	 * @param filePath		読み込むデータのファイルパス
	 */
	public static void LoadFile(String filePath) {
        int i = 1;
        int type = 0, priority = 0;
        float positionX = 0,positionY = 0;
        
        StructObjectManager.DeleteStructObject();
		
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
                    		StructObjectManager.CreateStructObject(type, positionX, positionY, priority);
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
			
			for( int i = 0; i < StructObjectManager.GetListSize(); i ++ ) {
				pw.print(StructObjectManager.GetStructObject(i).type);
				pw.print('/');
				pw.print((int)StructObjectManager.GetStructObject(i).position.x);
				pw.print('/');
				pw.print((int)StructObjectManager.GetStructObject(i).position.y);
				pw.print('/');
				pw.println(StructObjectManager.GetStructObject(i).priority);
			}
			pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}

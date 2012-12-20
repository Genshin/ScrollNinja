package org.genshin.old.scrollninja;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.genshin.scrollninja.GlobalParam;

public class ItemWindow {
	
	private final int			WIDTH	= 360;
	private final int			HEIGHT	= 720;
	private Frame  frm = new Frame();
	private JPanel panel = new JPanel();				// スクロールバー用	
	private JScrollPane scrollPane;						// スクロールバー
	private boolean frontFlag;							// 表示位置フラグ
	
	/**
	 * コンストラクタ
	 */
	public ItemWindow(){
		frontFlag = true;
	}
	
	/**
	 * 初期化
	 */
	public void Init(){
		 /* フレームを作成します。*/
	     frm.setSize(new Dimension(WIDTH,HEIGHT));
	     
		frm.toFront();
		frm.setTitle("アイテムウィンドウ");
		frm.setSize(new Dimension(WIDTH,HEIGHT));
		frm.setBounds((int)GlobalParam.INSTANCE.CLIENT_WIDTH - WIDTH,0/*(int)GlobalParam.INSTANCE.CLIENT_HEIGHT - a.top*/, WIDTH, HEIGHT);
		panel.setLayout( new GridLayout(0,3));
		
		panel.add( new ButtonForEdit("data/old/shuriken.png", 1));
		
    	scrollPane = new JScrollPane(panel);	// なんかコンストラクタで指定しないとうまく動かないわ
    	frm.add(scrollPane);
    	
    	frm.setVisible(true);
	}
}

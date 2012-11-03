package org.genshin.scrollninja;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonForEdit extends JButton implements ActionListener {

	public static final int ROCK		= 0;

	private int type;

	public ButtonForEdit(String filePath, int Type) {
		// TODO 自動生成されたコンストラクター・スタブ
//		setText(name);
		addActionListener(this);
		type = Type;

		ImageIcon icon = new ImageIcon(filePath);
		this.setIcon(icon);
	}

	/**
	 * @OVERRIDE
	 * ボタンが押されたら呼ばれる
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
//		switch( type ) {
//		case ROCK:
			StructObjectManager.CreateStructObject(1);
//		}
	}
}

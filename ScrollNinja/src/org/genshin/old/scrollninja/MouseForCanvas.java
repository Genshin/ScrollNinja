package org.genshin.old.scrollninja;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseForCanvas implements MouseListener, MouseMotionListener {
	
	private Point position;
	private boolean press;
	private boolean click;
	private boolean drag;
	private boolean exit;
	
	/**
	 * コンストラクタ
	 */
	public MouseForCanvas() {
		position = new Point();
		press = false;
		click = false;
		drag = false;
		exit = false;
	}
	
	/**
	 * マウス座標を返す
	 */
	public Point GetPosition() {
		return position;
	}
	
	/**
	 * 押した判定を返す
	 */
	public boolean GetPress() {
		return press;
	}
	
	/**
	 * ドラッグ判定を返す
	 */
	public boolean GetDrag() {
		return drag;
	}
	
	/**
	 * 外に出た判定を返す
	 */
	public boolean GetExit() {
		return exit;
	}
	
	/**
	 * クリック判定を返す
	 */
	public boolean GetClick() {
		return click;
	}
	
	/**
	 * クリック判定をリセット
	 */
	public void ResetClick() {
		click = false;
	}
	

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		drag = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		position = e.getPoint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		click = true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		exit = false;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		exit = true;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		press = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		press = false;
		drag = false;
	}

}

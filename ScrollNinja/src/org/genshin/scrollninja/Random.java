package org.genshin.scrollninja;


public class Random {
	
	public static void Rnd() {
		int r;
		
		// 0 ～ 99 までの乱数生成
		r = (int)(Math.random() * 100);
		System.out.print(r + "");
		
		System.out.println();
		
	}

}

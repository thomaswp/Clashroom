package com.clashroom.shared;

import java.util.Random;

public class RandomTest {
	public static void random() {
		Random rand = new Random(100);
		for (int i = 0; i < 5; i++) {
			Debug.write(rand.nextInt());
		}
	}
}

package nassaty.com.cornerstone.utils;

import android.content.Context;

public class Calculator {

	public static int calculateShares(int amount, int total){
		int percentage;

		if (total > 0 && amount > 0){
			percentage = amount * 100 / total;
		}else {
			return 0;
		}

		return percentage;
	}
}

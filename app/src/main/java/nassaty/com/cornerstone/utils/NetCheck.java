package nassaty.com.cornerstone.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetCheck {

	public static boolean checkNetwork(Context context){
		final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if (info != null){
			if (info.getType() == ConnectivityManager.TYPE_WIFI)return true;
			else if (info.getType() == ConnectivityManager.TYPE_MOBILE)return true;
		}
		return false;
	}

}

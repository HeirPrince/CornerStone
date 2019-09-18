package nassaty.com.cornerstone.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import nassaty.com.cornerstone.Database.CornerDB;
import nassaty.com.cornerstone.models.History;

public class LogVModel extends AndroidViewModel {

	CornerDB cornerDB;

	public LogVModel(@NonNull Application application) {
		super(application);
		cornerDB = CornerDB.getInstance(application);
	}

	public void log(History history){
		new insertLog(cornerDB).execute(history);
	}

	public void deleteLog(History history){
		new deleteAsync(cornerDB).execute(history);
	}


	private class insertLog extends AsyncTask<History, Void, Void> {

		CornerDB db;

		public insertLog(CornerDB cornerDB) {
			db = cornerDB;
		}

		@Override
		protected Void doInBackground(History... histories) {
			db.logEntity().log(histories[0]);
			return null;
		}
	}

	private class deleteAsync extends AsyncTask<History, Void, Void> {

		CornerDB db;

		public deleteAsync(CornerDB cornerDB) {
			db = cornerDB;
		}

		@Override
		protected Void doInBackground(History... histories) {
			db.logEntity().deleteLog(histories[0]);
			return null;
		}
	}
}

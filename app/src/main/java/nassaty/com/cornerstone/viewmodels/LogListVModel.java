package nassaty.com.cornerstone.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import nassaty.com.cornerstone.Database.CornerDB;
import nassaty.com.cornerstone.models.History;

public class LogListVModel extends AndroidViewModel {

	CornerDB db;

	public LogListVModel(@NonNull Application application) {
		super(application);
		db = CornerDB.getInstance(application);
	}

	public LiveData<List<History>> getLogs(String uid) {
		return db.logEntity().getLog(uid);
	}
}

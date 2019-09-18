package nassaty.com.cornerstone.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;

import nassaty.com.cornerstone.Database.CornerDB;

public class UserVModel extends ViewModel {
	CornerDB db;

	public UserVModel(Application application) {
		this.db = CornerDB.getInstance(application);
	}
}

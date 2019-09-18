package nassaty.com.cornerstone;

import android.app.Application;

import com.facebook.stetho.Stetho;

import nassaty.com.cornerstone.Database.CornerDB;
import nassaty.com.cornerstone.Database_repository.DatabaseRepository;

public class CornerStone extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);
	}

	public CornerDB getDatabase() {
		return CornerDB.getInstance(this);
	}

	public DatabaseRepository getRepository() {
		return DatabaseRepository.getInstance(getDatabase());
	}
}

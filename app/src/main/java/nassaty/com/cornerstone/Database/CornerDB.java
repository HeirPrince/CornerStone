package nassaty.com.cornerstone.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import nassaty.com.cornerstone.models.Account;
import nassaty.com.cornerstone.models.History;
import nassaty.com.cornerstone.models.Member_Offline;
import nassaty.com.cornerstone.models.User;
import nassaty.com.cornerstone.models.Vault;

@Database(entities = {User.class, Member_Offline.class, Vault.class, History.class, Account.class}, version = 10)
public abstract class CornerDB extends RoomDatabase {

	private static CornerDB instance;
	private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

	public static CornerDB getInstance(Context context) {
		if (instance == null){
			instance = Room.databaseBuilder(context, CornerDB.class,"corner_dbase").fallbackToDestructiveMigration().build();
		}
		return instance;
	}

	private void updateDatabaseCreated(final Context context) {
		if (context.getDatabasePath("corner_dbase").exists()) {
			setDatabaseCreated();
		}
	}

	private void setDatabaseCreated(){
		mIsDatabaseCreated.postValue(true);
	}

	public LiveData<Boolean> getDatabaseCreated() {
		return mIsDatabaseCreated;
	}

	public abstract UserDao userEntity();
	public abstract MemberDao memberEntity();
	public abstract VaultDao masterTable();
	public abstract HistoryDao logEntity();
	public abstract AccountDao accountEntity();
}

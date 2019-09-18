package nassaty.com.cornerstone.Database_repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

import nassaty.com.cornerstone.Database.CornerDB;
import nassaty.com.cornerstone.models.History;
import nassaty.com.cornerstone.models.Member_Offline;

public class DatabaseRepository {

	private static DatabaseRepository instance;

	private final CornerDB mDatabse;
	private MediatorLiveData<List<Member_Offline>> mObservableMembers;
	private MediatorLiveData<List<History>> mObservableLogs;

	public DatabaseRepository(CornerDB mDatabse) {
		this.mDatabse = mDatabse;
		mObservableMembers = new MediatorLiveData<>();
		mObservableLogs = new MediatorLiveData<>();

		mObservableMembers.addSource(mDatabse.memberEntity().getAllMembers(),
				productEntities -> {
					if (mDatabse.getDatabaseCreated().getValue() != null) {
						mObservableMembers.postValue(productEntities);
					}
				});

	}

	public static DatabaseRepository getInstance(final CornerDB cornerDB) {
		if (instance == null){
			synchronized (DatabaseRepository.class){
				if (instance == null){
					instance = new DatabaseRepository(cornerDB);
				}
			}
		}
		return instance;
	}

	public LiveData<List<Member_Offline>> getMembers(){
		return mDatabse.memberEntity().getAllMembers();
	}

	public LiveData<List<History>> getLog(String uid){return mDatabse.logEntity().getLog(uid);}

}

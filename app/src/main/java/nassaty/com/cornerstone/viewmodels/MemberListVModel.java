package nassaty.com.cornerstone.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import nassaty.com.cornerstone.CornerStone;
import nassaty.com.cornerstone.Database_repository.DatabaseRepository;
import nassaty.com.cornerstone.models.Member_Offline;

public class MemberListVModel extends AndroidViewModel {

	private final DatabaseRepository repository;

	private final MediatorLiveData<List<Member_Offline>> mObservableMembers;

	public MemberListVModel(@NonNull Application application) {
		super(application);

		mObservableMembers = new MediatorLiveData<>();
		mObservableMembers.setValue(null);

		repository = ((CornerStone)application).getRepository();
		LiveData<List<Member_Offline>> members = repository.getMembers();

		mObservableMembers.addSource(members, mObservableMembers::setValue);
	}

	public LiveData<List<Member_Offline>> getMembers() {
		return repository.getMembers();
	}
}

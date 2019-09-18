package nassaty.com.cornerstone.callbacks;

import nassaty.com.cornerstone.models.Member_Offline;

public interface onMemberSavedListener {

	void onSaved(Member_Offline memberOffline);
	void onError(String e);
}

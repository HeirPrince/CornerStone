package nassaty.com.cornerstone.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nonnull;

import nassaty.com.cornerstone.Database.CornerDB;
import nassaty.com.cornerstone.models.Member_Offline;

public class MemberVModel extends AndroidViewModel{

	private CornerDB database;
	private FirebaseFirestore firebaseFirestore;

	public MemberVModel(@Nonnull Application application) {
		super(application);
		database = CornerDB.getInstance(application);
		firebaseFirestore = FirebaseFirestore.getInstance();
	}

	public void setMember(Member_Offline memberOffline){
		new insertMemberTask(database).execute(memberOffline);
	}

	public LiveData<Member_Offline> getMemberbyUID(String uid) {
		return database.memberEntity().getMemberByUID(uid);
	}

	public void updateBalance(String uid,int amt){
		new updateBalAsync(database, uid).execute(amt);
	}

	public void deleteMember(Member_Offline memberOffline){
		new deleteAsync(database).execute(memberOffline);
	}

	public void updateMember(Member_Offline memberOffline) {
		new updateMemberAsync(database, memberOffline.getUid()).execute(memberOffline);
	}

	static class insertMemberTask extends AsyncTask<Member_Offline, Void, Void> {

		CornerDB db;

		public insertMemberTask(CornerDB database) {
			db = database;
		}

		@Override
		protected Void doInBackground(Member_Offline... memberOfflines) {
			db.memberEntity().addMember(memberOfflines[0]);
			return null;
		}
	}

	private class deleteAsync extends AsyncTask<Member_Offline, Void, Void> {

		CornerDB db;

		public deleteAsync(CornerDB database) {
			db = database;
		}

		@Override
		protected Void doInBackground(Member_Offline... memberOfflines) {
			db.memberEntity().deleteMember(memberOfflines[0]);
			return null;
		}
	}

	private class updateBalAsync extends AsyncTask<Integer, Void, Void>{

		CornerDB db;
		String uid;

		public updateBalAsync(CornerDB database, String id) {
			db = database;
			uid = id;
		}

		@Override
		protected Void doInBackground(Integer... integers) {
			db.memberEntity().updateAmount(uid, integers[0]);
			return null;
		}
	}

	private class updateMemberAsync extends AsyncTask<Member_Offline, Void, Void>{

		CornerDB db;
		String uid;

		updateMemberAsync(CornerDB database, String id) {
			db = database;
			uid= id;
		}

		@Override
		protected Void doInBackground(Member_Offline... memberOfflines) {
			db.memberEntity().updateAmount(uid, memberOfflines[0].getAmount());
			return null;
		}
	}
}

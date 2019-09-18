package nassaty.com.cornerstone.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import nassaty.com.cornerstone.Database.CornerDB;
import nassaty.com.cornerstone.models.Account;
import nassaty.com.cornerstone.utils.NetCheck;

public class AccountVModel extends AndroidViewModel {

	private CornerDB db;
	private FirebaseFirestore firebaseFirestore;

	public AccountVModel(@NonNull Application application) {
		super(application);
		db = CornerDB.getInstance(application);
		firebaseFirestore = FirebaseFirestore.getInstance();
	}

	public void createAccount(Account account){
		new insertAccountAsync(db).execute(account);
	}

	public void debit(String uid, int amt){
		new debitAccountAsync(db, uid).execute(amt);
	}

	private class insertAccountAsync extends AsyncTask<Account, Void, Void> {

		CornerDB cornerDB;
		insertAccountAsync(CornerDB db) {
			cornerDB = db;
		}


		@Override
		protected Void doInBackground(Account... accounts) {
			cornerDB.accountEntity().insertNew(accounts[0]);

			//check network
			if (NetCheck.checkNetwork(getApplication())){
				firebaseFirestore.collection("bank").document(accounts[0].getUid()).set(accounts[0]);
			}else {
				Toast.makeText(getApplication(), "To be saved later when network returns", Toast.LENGTH_SHORT).show();
			}
			return null;
		}
	}

	private class debitAccountAsync extends AsyncTask<Integer, String, Void> {

		CornerDB cornerDB;
		String uid;

		debitAccountAsync(CornerDB db, String id) {
			cornerDB = db;
			uid = id;
		}

		@Override
		protected Void doInBackground(Integer... integers) {
			cornerDB.accountEntity().debit(uid, integers[0]);
			firebaseFirestore.collection("bank").document(uid).update("balance", integers[0]);
			return null;
		}
	}
}

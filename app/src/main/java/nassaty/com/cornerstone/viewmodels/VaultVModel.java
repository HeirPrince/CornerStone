package nassaty.com.cornerstone.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import nassaty.com.cornerstone.Database.CornerDB;
import nassaty.com.cornerstone.models.Vault;

public class VaultVModel extends AndroidViewModel {

	CornerDB db;
	LiveData<Vault> vaultLiveData;

	public VaultVModel(@NonNull Application application) {
		super(application);
		this.db = CornerDB.getInstance(application);
		this.vaultLiveData = db.masterTable().getVault();
	}

	public void setVault(Vault vault){
		new initVault(db).execute(vault);
	}

	public void setInitialAmount(int amt){
		new setInitial(db).execute(amt);
	}

	public void setMemberCount(int nm){
		new setCount(db).execute(nm);
	}

	public void setTotalAmount(int nm){new setTotal(db).execute(nm);}

	private static class initVault extends AsyncTask<Vault, Void, Void>{

		CornerDB database;

		initVault(CornerDB db) {
			database = db;
		}

		@Override
		protected Void doInBackground(Vault... vaults) {
			database.masterTable().createVault(vaults[0]);
			return null;
		}
	}

	private static class setInitial extends AsyncTask<Integer, Void, Void> {

		CornerDB database;

		setInitial(CornerDB db) {
			database = db;
		}

		@Override
		protected Void doInBackground(Integer... integers) {
			if (integers[0] > 0)
				database.masterTable().setInitialAmount(integers[0]);
			else
				database.masterTable().setInitialAmount(0);

			return null;
		}
	}

	private static class setCount extends AsyncTask<Integer, Void, Void> {

		CornerDB database;

		setCount(CornerDB db) {
			database = db;
		}

		@Override
		protected Void doInBackground(Integer... integers) {
			if (integers[0] > 0)
				database.masterTable().setNmMembers(integers[0]);
			else
				database.masterTable().setNmMembers(0);
			return null;
		}
	}

	private static class setTotal extends AsyncTask<Integer, Void, Void> {

		CornerDB database;

		setTotal(CornerDB db) {
			database = db;
		}

		@Override
		protected Void doInBackground(Integer... integers) {
			if (integers[0] > 0)
				database.masterTable().setTotalCash(integers[0]);
			else
				database.masterTable().setTotalCash(0);
			return null;
		}
	}

	//get initAmt
	public LiveData<Vault> getVaultState(){
		return vaultLiveData;
	}
}

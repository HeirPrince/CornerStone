package nassaty.com.cornerstone.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import nassaty.com.cornerstone.models.Account;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface AccountDao {

	@Insert(onConflict = REPLACE)
	void insertNew(Account account);

	@Delete
	void deleteAccount(Account account);

	@Query("UPDATE Account SET uid= :uid, balance= :bal")
	void updateAccount(String uid, int bal);

	@Query("UPDATE Account SET balance= :bal WHERE uid= :uid")
	void debit(String uid, int bal);

	@Query("UPDATE Account SET balance= :bal WHERE uid= :uid")
	void withdraw(String uid, int bal);
}

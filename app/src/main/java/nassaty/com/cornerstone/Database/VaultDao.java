package nassaty.com.cornerstone.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import nassaty.com.cornerstone.models.Vault;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface VaultDao {

	@Insert(onConflict = REPLACE)
	void createVault(Vault vault);

	@Delete
	void deleteVault(Vault vault);

	@Query("UPDATE Vault SET initAmt= :amt WHERE id= 1")
	public void setInitialAmount(int amt);

	@Query("UPDATE Vault SET nmMembers= :nm WHERE id= 1")
	public void setNmMembers(int nm);

	@Query("SELECT * FROM Vault WHERE id= 1")
	LiveData<Vault> getVault();

	@Query("UPDATE Vault SET total= :integer WHERE id= 1")
	void setTotalCash(Integer integer);
}

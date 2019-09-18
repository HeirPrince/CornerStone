package nassaty.com.cornerstone.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import nassaty.com.cornerstone.models.History;

@Dao
public interface HistoryDao {

	@Query("SELECT * FROM History WHERE uid= :uid")
	LiveData<List<History>> getLog(String uid);

	@Insert
	void log(History history);

	@Delete
	void deleteLog(History history);

}

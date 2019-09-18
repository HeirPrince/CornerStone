package nassaty.com.cornerstone.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import nassaty.com.cornerstone.models.User;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

	@Query("SELECT * FROM User")
	LiveData<List<User>> getAllUsers();

	@Insert(onConflict = REPLACE)
	void addUser(User user);

	@Query("UPDATE User SET phone = :phoneNum WHERE uid = :uid")
	void updatePhone(String uid, String phoneNum);

	@Delete
	void deleteUser(User user);


}

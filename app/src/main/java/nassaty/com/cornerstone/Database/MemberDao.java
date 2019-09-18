package nassaty.com.cornerstone.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import nassaty.com.cornerstone.models.Member_Offline;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MemberDao {

	@Insert(onConflict = REPLACE)
	void addMember(Member_Offline member);

	@Delete
	void deleteMember(Member_Offline member);

	@Query("SELECT * FROM Member_Offline WHERE uid= :uid")
	LiveData<Member_Offline> getMemberByUID(String uid);

	@Query("UPDATE Member_Offline SET uid= :uid, names= :names, imageRef= :ref, amount= :amt WHERE uid= :uid")
	void updateMember(String uid, String names, String ref, int amt);

	@Query("UPDATE Member_Offline SET imageRef= :ref WHERE uid= :uid")
	void updateImage(String uid, String ref);

	@Query("UPDATE Member_Offline SET names= :names WHERE uid= :uid")
	void updateNames(String uid, String names);

	@Query("UPDATE Member_Offline SET amount= :amt WHERE uid= :uid")
	void updateAmount(String uid, int amt);

	@Query("SELECT * FROM Member_Offline ORDER BY names ASC")
	LiveData<List<Member_Offline>> getAllMembers();

}

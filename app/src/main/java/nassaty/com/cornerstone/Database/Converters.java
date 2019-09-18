package nassaty.com.cornerstone.Database;

import android.arch.persistence.room.TypeConverter;

import nassaty.com.cornerstone.models.Member_Offline;

public class Converters {

	@TypeConverter
	public static Member_Offline fromMember(Member_Offline memberOffline){
		return new Member_Offline();
	}

	

}

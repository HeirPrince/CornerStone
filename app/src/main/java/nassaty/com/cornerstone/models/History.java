package nassaty.com.cornerstone.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class History {

	/*
	* History - thoughts
	* ------------------
	*
	* 1. History has a message, time, type
	* 2. Every Member_Offline has his/her own story
	* 3. History will be deleted locally then saved online*/

	@PrimaryKey(autoGenerate = true)
	private int id;
	private String uid;
	private String text;

	public History() {
	}

	public History(int id, String uid, String text) {
		this.id = id;
		this.uid = uid;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

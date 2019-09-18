package nassaty.com.cornerstone.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Member_Offline {

	@PrimaryKey(autoGenerate = true)
	private int id;

	private String uid;
	private String names;
	private String imageRef;
	private int amount;

	public Member_Offline() {
	}

	public Member_Offline(String uid, String names, String imageRef, int amount) {
		this.uid = uid;
		this.names = names;
		this.imageRef = imageRef;
		this.amount = amount;
	}

	public Member_Offline(String uid, String names, int amount) {
		this.uid = uid;
		this.names = names;
		this.amount = amount;
	}

	public Member_Online setOnline(Member_Offline offline){
		Member_Online online = new Member_Online();
		online.setPhotoUrl(offline.getImageRef());
		online.setUid(offline.getUid());

		return online;
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

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getImageRef() {
		return imageRef;
	}

	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}

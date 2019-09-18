package nassaty.com.cornerstone.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Account {
	@PrimaryKey(autoGenerate = true)
	private int id;

	private String uid;;
	private int balance;

	public Account() {
	}

	public Account(String uid, int balance) {
		this.uid = uid;
		this.balance = balance;
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

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
}

package nassaty.com.cornerstone.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Vault {

	@PrimaryKey(autoGenerate = true)
	private int id;

	private int total;
	private int initAmt;
	private int nmMembers;

	public Vault() {
	}

	public Vault(int id, int total, int initAmt, int nmMembers) {
		this.id = id;
		this.total = total;
		this.initAmt = initAmt;
		this.nmMembers = nmMembers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getInitAmt() {
		return initAmt;
	}

	public void setInitAmt(int initAmt) {
		this.initAmt = initAmt;
	}

	public int getNmMembers() {
		return nmMembers;
	}

	public void setNmMembers(int nmMembers) {
		this.nmMembers = nmMembers;
	}
}

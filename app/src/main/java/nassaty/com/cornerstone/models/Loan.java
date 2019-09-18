package nassaty.com.cornerstone.models;

public class Loan {

	private String uid;
	private int bal;
	private Boolean isPayed;

	public Loan() {
	}

	public Loan(String uid, int bal, Boolean isPayed) {
		this.uid = uid;
		this.bal = bal;
		this.isPayed = isPayed;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getBal() {
		return bal;
	}

	public void setBal(int bal) {
		this.bal = bal;
	}

	public Boolean getPayed() {
		return isPayed;
	}

	public void setPayed(Boolean payed) {
		isPayed = payed;
	}
}

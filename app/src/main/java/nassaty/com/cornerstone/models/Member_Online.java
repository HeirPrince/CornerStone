package nassaty.com.cornerstone.models;

public class Member_Online {

	private String uid;
	private String photoUrl;
	private int acnum;
	private String names;

	public Member_Online() {
	}

	public Member_Online(String uid, String photoUrl, int acnum, String names) {
		this.uid = uid;
		this.photoUrl = photoUrl;
		this.acnum = acnum;
		this.names = names;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public int getAcnum() {
		return acnum;
	}

	public void setAcnum(int acnum) {
		this.acnum = acnum;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}
}

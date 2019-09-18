package nassaty.com.cornerstone.models;

public class Historic {

	public enum HistoricType{
		LOAN, DEBT, WITHDRAW, ACCOUNT, DELETE;
	}

	private String code;
	private String uid;
	private Long timeStamp;
	private HistoricType type;

	public Historic() {
	}

	public Historic(String code, String uid, Long timeStamp, HistoricType type) {
		this.code = code;
		this.uid = uid;
		this.timeStamp = timeStamp;
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public HistoricType getType() {
		return type;
	}

	public void setType(HistoricType type) {
		this.type = type;
	}
}


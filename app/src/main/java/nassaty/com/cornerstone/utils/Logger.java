package nassaty.com.cornerstone.utils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import nassaty.com.cornerstone.models.Historic;

public class Logger {

	CollectionReference logRef = FirebaseFirestore.getInstance().collection("History");

	//types
	final int TYPE_LOAN = 1;
	final int TYPE_DEBT = 2;
	final int TYPE_WITHDRAW = 3;
	final int TYPE_CREATE = 4;
	final int TYPE_DELETE = 5;

	public Logger() {

	}

	private long getTimestamp(){
		Long tsLong = System.currentTimeMillis()/1000;
		return tsLong;
	}

	public void logCreateAccount(String uid){
		Historic historic = new Historic();
		historic.setCode("create");
		historic.setUid(uid);
		historic.setTimeStamp(getTimestamp());
		historic.setType(Historic.HistoricType.ACCOUNT);
		logRef.document(uid).collection("log").add(historic);
	}

	public void logProfileupate(String uid){
		Historic historic = new Historic();
		historic.setCode("profile");
		historic.setUid(uid);
		historic.setTimeStamp(getTimestamp());
		historic.setType(Historic.HistoricType.ACCOUNT);
		logRef.document(uid).collection("log").add(historic);
	}

	public void logAccountDeletion(String uid){
		Historic historic = new Historic();
		historic.setCode("delete");
		historic.setUid(uid);
		historic.setTimeStamp(getTimestamp());
		historic.setType(Historic.HistoricType.DELETE);
		logRef.document(uid).collection("log").add(historic);
	}

	public void logDebit(String uid){
		Historic historic = new Historic();
		historic.setCode("debit");
		historic.setUid(uid);
		historic.setTimeStamp(getTimestamp());
		historic.setType(Historic.HistoricType.DEBT);
		logRef.document(uid).collection("log").add(historic);
	}

	public void logLoan(String uid){
		Historic historic = new Historic();
		historic.setCode("loan");
		historic.setUid(uid);
		historic.setTimeStamp(getTimestamp());
		historic.setType(Historic.HistoricType.LOAN);
		logRef.document(uid).collection("log").add(historic);
	}
}

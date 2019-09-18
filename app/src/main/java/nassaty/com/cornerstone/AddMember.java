package nassaty.com.cornerstone;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nassaty.com.cornerstone.callbacks.onMemberSavedListener;
import nassaty.com.cornerstone.models.Account;
import nassaty.com.cornerstone.models.History;
import nassaty.com.cornerstone.models.Member_Offline;
import nassaty.com.cornerstone.viewmodels.AccountVModel;
import nassaty.com.cornerstone.viewmodels.LogVModel;
import nassaty.com.cornerstone.viewmodels.MemberVModel;

public class AddMember extends AppCompatActivity implements onMemberSavedListener {

	EditText uname;
	TextView acStatus;
	MemberVModel vModel;
	LogVModel logVModel;
	AccountVModel accountVModel;
	FirebaseAuth auth;
	FirebaseUser firebaseUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_member);
		auth = FirebaseAuth.getInstance();
		firebaseUser = auth.getCurrentUser();
		vModel = ViewModelProviders.of(this).get(MemberVModel.class);
		logVModel = ViewModelProviders.of(this).get(LogVModel.class);
		accountVModel = ViewModelProviders.of(this).get(AccountVModel.class);
		bindViews();
	}

	private void bindViews() {
		uname = findViewById(R.id.bal);
		acStatus = findViewById(R.id.acstatus);
	}

	public void register(View view){
		String username = uname.getText().toString();

		if (!TextUtils.isEmpty(username)){
			Member_Offline memberOffline = new Member_Offline(firebaseUser.getUid(), username, 0);
			vModel.setMember(memberOffline);
			History history = new History();
			history.setText(username+" registered to Corner Stone");
			history.setUid(firebaseUser.getUid());
			logVModel.log(history);
			Account account = new Account();
			account.setBalance(0);
			account.setUid(memberOffline.getUid());
			accountVModel.createAccount(account);
			finish();
		}else {
			CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
					.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
					.setAutoDismissAfter(1000)
					.setCancelable(true)
					.setTitle("Add new Member_Offline")
					.setMessage("Please Fill out all fields");

			// Show the alert
			builder.show();
		}
	}

	public String generateAc(){
		return "NASSATY#1";
	}

	@Override
	public void onSaved(Member_Offline memberOffline) {
		Toast.makeText(this, "Member_Offline Saved Successfully", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onError(String e) {
		Toast.makeText(this, "Member_Offline not Saved", Toast.LENGTH_SHORT).show();
	}
}

package nassaty.com.cornerstone;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;

import nassaty.com.cornerstone.models.History;
import nassaty.com.cornerstone.models.Member_Offline;
import nassaty.com.cornerstone.models.Vault;
import nassaty.com.cornerstone.utils.Calculator;
import nassaty.com.cornerstone.viewmodels.AccountVModel;
import nassaty.com.cornerstone.viewmodels.LogVModel;
import nassaty.com.cornerstone.viewmodels.MemberVModel;
import nassaty.com.cornerstone.viewmodels.VaultVModel;

public class Transaction extends AppCompatActivity {

	TextView username, ac, amt, payAmt;
	EditText uid;
	VaultVModel vaultVModel;
	MemberVModel memberVModel;
	LogVModel logVModel;
	Member_Offline selectedMemberOffline;
	Vault vaultState;
	int intAmt = 0;
	AccountVModel accountVModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);

		vaultVModel = ViewModelProviders.of(this).get(VaultVModel.class);
		memberVModel = ViewModelProviders.of(this).get(MemberVModel.class);
		logVModel = ViewModelProviders.of(this).get(LogVModel.class);
		accountVModel = ViewModelProviders.of(this).get(AccountVModel.class);

		username = findViewById(R.id.bal);
		ac = findViewById(R.id.balance);
		amt = findViewById(R.id.shares);
		payAmt = findViewById(R.id.pay_amt);
		uid = findViewById(R.id.uid);

		selectedMemberOffline = new Member_Offline();
		vaultState = new Vault();

		feed();
		
	}

	private void feed() {
		vaultVModel.getVaultState().observe(this, new Observer<Vault>() {
			@Override
			public void onChanged(@Nullable Vault vault) {
				if (vault != null){
					vaultState = vault;
					payAmt.setText(String.format("%s RF", String.valueOf(vaultState.getInitAmt())));
					intAmt = vaultState.getInitAmt();
				}else {
					payAmt.setText(String.format("%s RF", String.valueOf(0)));
				}
			}
		});
	}

	public void addShare(View view){
		memberVModel.getMemberbyUID(selectedMemberOffline.getUid()).observe(this, member -> {
			if (member != null){
				//update amount
				member.setAmount(member.getAmount() + vaultState.getInitAmt());
				memberVModel.updateMember(member);      //TODO update vault's total amount(vault stats from the main menu should be retrieved not generated from the loop;

				setLog(member);
				accountVModel.debit(member.getUid(), member.getAmount());

				if (vaultState != null) {//TODO default amount is 100 RF. The minimum amount will be retrieved from user settings

					
				}else {
					CFAlertDialog.Builder dialog = new CFAlertDialog.Builder(this)
							.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
							.setAutoDismissAfter(1000)
							.setCancelable(true)
							.setCornerRadius(10)
							.setMessage(R.string.member_not_found)
							.onDismissListener(new DialogInterface.OnDismissListener() {
								@Override
								public void onDismiss(DialogInterface dialogInterface) {
									updateInitAmt(view);
								}
							})
							.setTitle("Corner Stone");
					dialog.show();
					Toast.makeText(this, "Member_Offline not found", Toast.LENGTH_SHORT).show();
				}

			}else {
				Toast.makeText(Transaction.this, "memberOffline not found", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setLog(Member_Offline memberOffline) {
		History history = new History();
		history.setText(memberOffline.getUid()+" Saved "+vaultState.getInitAmt());//TODO what is the memberOffline adds an amount different from the init shares mhh?
		history.setUid(memberOffline.getUid());
		logVModel.log(history);
	}

	public void updateInitAmt(View view){
		LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
		View mView = layoutInflaterAndroid.inflate(R.layout.text_input_layout, null);
		AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
		alertDialogBuilderUserInput.setView(mView);

		final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);
		alertDialogBuilderUserInput
				.setCancelable(false)
				.setPositiveButton("Set", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogBox, int id) {
						int amt = Integer.parseInt(userInputDialogEditText.getText().toString());
						changeInitAmt(amt);
					}
				})

				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogBox, int id) {
								dialogBox.cancel();
							}
						});

		AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
		alertDialogAndroid.show();
	}

	public void changeInitAmt(int amt){
		vaultVModel.setInitialAmount(amt);
	}

	public void lookup(View view) {
		String accountNumber = uid.getText().toString();
		if (vaultState != null){
			memberVModel.getMemberbyUID(accountNumber).observe(Transaction.this, new Observer<Member_Offline>() {
				@Override
				public void onChanged(@Nullable Member_Offline memberOffline) {
					if (memberOffline != null){
						username.setText(memberOffline.getNames());
						ac.setText(String.format("%s RF", String.valueOf(memberOffline.getAmount())));
						amt.setText(String.format("%s%s", String.valueOf(Calculator.calculateShares(memberOffline.getAmount(), vaultState.getTotal())), getApplication().getString(R.string.percentage)));

						selectedMemberOffline = memberOffline;
					}else {
						Toast.makeText(Transaction.this, "memberOffline not found", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}else {
			Toast.makeText(Transaction.this, "Vault state can't be completed", Toast.LENGTH_SHORT).show();
		}
	}
}

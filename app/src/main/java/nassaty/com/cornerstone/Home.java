package nassaty.com.cornerstone;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nassaty.com.cornerstone.models.Member_Offline;
import nassaty.com.cornerstone.models.Member_Online;
import nassaty.com.cornerstone.models.Vault;
import nassaty.com.cornerstone.utils.Calculator;
import nassaty.com.cornerstone.utils.Constants;
import nassaty.com.cornerstone.viewmodels.MemberListVModel;
import nassaty.com.cornerstone.viewmodels.MemberVModel;
import nassaty.com.cornerstone.viewmodels.VaultVModel;

public class Home extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

	private static final int RC_SIGN_IN = 007;
	RecyclerView member_list;
	MemberVModel memberVModel;
	MemberListVModel memberListVModel;
	List<Member_Offline> memberOfflineList;
	TextView totAc, totMbr, totDebts;
	VaultVModel vaultVModel;
	Vault vault;
	FirebaseUser user;
	FirebaseFirestore firestore;
	private FirestoreRecyclerAdapter<Member_Online, MemberVHolder> adapter;

	static {
		FirebaseFirestore.setLoggingEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		member_list = findViewById(R.id.member_list);
		memberOfflineList = new ArrayList<>();
		firestore = FirebaseFirestore.getInstance();

		memberVModel = ViewModelProviders.of(this).get(MemberVModel.class);
		memberListVModel = ViewModelProviders.of(this).get(MemberListVModel.class);
		vaultVModel = ViewModelProviders.of(this).get(VaultVModel.class);
		vault = new Vault();

		totAc = findViewById(R.id.totAc);
		totMbr = findViewById(R.id.totMbr);
		totDebts = findViewById(R.id.totDebt);
		displayMembers();
	}

	private void initVault() {
		vaultVModel.getVaultState().observe(this, new Observer<Vault>() {
			@Override
			public void onChanged(@Nullable Vault thisVault) {
				if (thisVault != null) {
					vault = thisVault;
				} else {
					vault.setTotal(0);
					vault.setNmMembers(0);
					vault.setInitAmt(Constants.DEF_INIT_AMT);
				}
			}
		});
	}

	public void makeTransaction(View view) {
		startActivity(new Intent(Home.this, Transaction.class));
	}

	private void displayMembers() {
		member_list.setLayoutManager(new LinearLayoutManager(this));
		member_list.setHasFixedSize(true);
		DefaultItemAnimator animator = new DefaultItemAnimator();
		member_list.setItemAnimator(animator);
		DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
		member_list.addItemDecoration(itemDecoration);

		Query query = firestore.collection("members");

		FirestoreRecyclerOptions<Member_Online> options = new FirestoreRecyclerOptions.Builder<Member_Online>()
				.setQuery(query, Member_Online.class)
				.build();

		adapter = new FirestoreRecyclerAdapter<Member_Online, MemberVHolder>(options) {
			@NonNull
			@Override
			public MemberVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_member_item, parent, false);
				return new MemberVHolder(view);
			}

			@Override
			protected void onBindViewHolder(@NonNull MemberVHolder holder, int position, @NonNull Member_Online model) {
				if (model != null){
					Toast.makeText(Home.this, model.getUid(), Toast.LENGTH_SHORT).show();
					holder.bind(model, vault);

					holder.itemView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent intent = new Intent(Home.this, MemberDetails.class);
							intent.putExtra("uid", model.getUid());
							startActivity(intent);
						}
					});

				}else {
					Toast.makeText(Home.this, "null", Toast.LENGTH_SHORT).show();
				}
			}
		};
		adapter.notifyDataSetChanged();
		member_list.setAdapter(adapter);
	}

	private void updateVault(Vault vault) {
		vaultVModel.setVault(vault);
		
		totMbr.setText(String.valueOf(vault.getNmMembers()));
		totAc.setText(String.valueOf(vault.getTotal()));
		Toast.makeText(Home.this, "vault initialized" + String.valueOf(vault.getTotal()), Toast.LENGTH_SHORT).show();
	}

	public void signIn(){
		// Choose authentication providers
		List<AuthUI.IdpConfig> providers = Arrays.asList(
				new AuthUI.IdpConfig.EmailBuilder().build(),
				new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
		startActivityForResult(
				AuthUI.getInstance()
						.createSignInIntentBuilder()
						.setAvailableProviders(providers)
						.setIsSmartLockEnabled(false)
						.build(),
				RC_SIGN_IN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.addM:
				startActivity(new Intent(this, AddMember.class));
				break;

			case R.id.reqLoan:
				startActivity(new Intent(this, LoanActivity.class));
				break;

			case R.id.settings:
				startActivity(new Intent(this, Settings.class));
				break;

			case R.id.out:
				AuthUI.getInstance().signOut(this);
				signIn();
				break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			IdpResponse response = IdpResponse.fromResultIntent(data);

			if (resultCode == RESULT_OK) {
				// Successfully signed in
				user = FirebaseAuth.getInstance().getCurrentUser();
				Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
				displayMembers();
				// ...
			} else {
				// Sign in failed. If response is null the user canceled the
				// sign-in flow using the back button. Otherwise check
				// response.getError().getErrorCode() and handle the error.
				// ...
				signIn();
			}
		}
	}

	@Override
	public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
		user = firebaseAuth.getCurrentUser();
		if (user == null)
			signIn();
		else
			displayMembers();
	}

	class MemberVHolder extends RecyclerView.ViewHolder{

		TextView username, balance, shares, account;
		ImageView avatar;

		MemberVHolder(View itemView) {
			super(itemView);
			username = itemView.findViewById(R.id.username);
			balance = itemView.findViewById(R.id.balance);
			shares = itemView.findViewById(R.id.shares);
			account = itemView.findViewById(R.id.account);
			avatar = itemView.findViewById(R.id.avatar);
		}

		void bind(Member_Online memberOnline, Vault vault){
			Picasso.get().load(memberOnline.getPhotoUrl()).into(avatar);
			username.setText(memberOnline.getNames());
			balance.setText(String.format("%s RF", 0));
			account.setText(String.valueOf(memberOnline.getAcnum()));
			if (vault == null)
				shares.setText("0%");
			else {
				shares.setText(String.format("%s%s", String.valueOf(Calculator.calculateShares(0, vault.getTotal())), getString(R.string.percentage)));
			}
		}
	}

	private void attachListeners(){
		adapter.startListening();
	}

	private void detachListeners(){
		adapter.stopListening();
	}

	@Override
	protected void onStart() {
		super.onStart();
		attachListeners();
	}

	@Override
	protected void onStop() {
		super.onStop();
		detachListeners();
	}
}

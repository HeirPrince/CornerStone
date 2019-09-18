package nassaty.com.cornerstone;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import nassaty.com.cornerstone.models.Loan;
import nassaty.com.cornerstone.models.Member_Online;
import nassaty.com.cornerstone.models.Vault;
import nassaty.com.cornerstone.viewmodels.MemberVModel;
import nassaty.com.cornerstone.viewmodels.VaultVModel;

public class LoanActivity extends AppCompatActivity {

	RecyclerView loan_list;
	Context context;
	MemberVModel memberVModel;
	VaultVModel vaultVModel;
	FirebaseFirestore firestore;
	FirebaseAuth auth;
	FirestoreRecyclerAdapter<Loan, LoanVHolder> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan);
		Objects.requireNonNull(getSupportActionBar()).setTitle("Loans");
		context = LoanActivity.this;
		firestore = FirebaseFirestore.getInstance();
		auth = FirebaseAuth.getInstance();
		loan_list = findViewById(R.id.loanList);

		memberVModel = ViewModelProviders.of(this).get(MemberVModel.class);
		vaultVModel = ViewModelProviders.of(this).get(VaultVModel.class);
		initVault();
		getLoans();
	}

	private void initVault() {
		vaultVModel.getVaultState().observe(this, new Observer<Vault>() {
			@Override
			public void onChanged(@Nullable Vault thisVault) {
				if (thisVault == null)
					Toast.makeText(context, "init failure", Toast.LENGTH_SHORT).show();
				else {
					getLoans();
				}
			}
		});
	}

	public void getLoans(){
		loan_list.setLayoutManager(new LinearLayoutManager(this));
		loan_list.setItemAnimator(new DefaultItemAnimator());
		DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
		loan_list.addItemDecoration(itemDecoration);
		Query query = firestore.collection("bank").document("loans").collection("pending");

		FirestoreRecyclerOptions<Loan> options = new FirestoreRecyclerOptions.Builder<Loan>()
				.setQuery(query, Loan.class)
				.build();

		adapter = new FirestoreRecyclerAdapter<Loan, LoanVHolder>(options) {
			@NonNull
			@Override
			public LoanVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loan_item, parent, false);

				return new LoanVHolder(view);
			}

			@Override
			protected void onBindViewHolder(@NonNull LoanVHolder holder, int position, @NonNull Loan model) {
				holder.bind(model);
			}
		};

		adapter.notifyDataSetChanged();
		loan_list.setAdapter(adapter);
	}

	class LoanVHolder extends RecyclerView.ViewHolder{

		TextView username, status, loanAmt, shares;
		ImageView avatar;
		Button view;

		LoanVHolder(View itemView) {
			super(itemView);
			avatar = itemView.findViewById(R.id.avatar);
			username = itemView.findViewById(R.id.username);
			status = itemView.findViewById(R.id.status);
			loanAmt = itemView.findViewById(R.id.loanAmt);
			shares = itemView.findViewById(R.id.shares);
			view = itemView.findViewById(R.id.view);
		}

		void bind(Loan loan){
			Query imageRef = firestore.collection("members").whereEqualTo("uid", loan.getUid());
			imageRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
				@Override
				public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
					if (e != null)
						Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

					for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
						if (snapshot.exists()){
							Member_Online member_online = snapshot.toObject(Member_Online.class);
							if (TextUtils.isEmpty(member_online.getPhotoUrl())){
								Picasso.get().load(member_online.getPhotoUrl()).placeholder(getDrawable(R.drawable.ic_launcher_background)).into(avatar);
							}else {
								Picasso.get().load(member_online.getPhotoUrl()).placeholder(getDrawable(R.drawable.ic_launcher_background)).into(avatar);
							}
						}
					}
				}
			});

			//status
//			if(loan.getPayed() == true) {
//				status.setText("Payed");
//				status.setTextColor(getResources().getColor(R.color.colorAccent));
//			}else {
//				status.setText("Not yet Payed");
//			}

			username.setText(auth.getCurrentUser().getUid());
			loanAmt.setText(String.format("%sRF", String.valueOf(loan.getBal())));

			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(LoanActivity.this, LoanDetails.class);
					intent.putExtra("uid", loan.getUid());
					startActivity(intent);
				}
			});
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

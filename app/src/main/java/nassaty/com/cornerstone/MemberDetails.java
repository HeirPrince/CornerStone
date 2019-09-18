package nassaty.com.cornerstone;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.com.cornerstone.models.Account;
import nassaty.com.cornerstone.models.Historic;
import nassaty.com.cornerstone.models.Member_Online;

public class MemberDetails extends AppCompatActivity {

	TextView balance, username;
	CircleImageView profile;
	RecyclerView list;
	FirebaseFirestore firebaseFirestore;
	FirestoreRecyclerAdapter<Historic, HistoricVHolder> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_details);

		balance = findViewById(R.id.bal);
		username = findViewById(R.id.username);
		profile = findViewById(R.id.profile);
		list = findViewById(R.id.list);
		feedData(getIntent().getStringExtra("uid"));
		getHistory(getIntent().getStringExtra("uid"));
	}

	private void feedData(String uid) {
		firebaseFirestore = FirebaseFirestore.getInstance();
		Query getMember = firebaseFirestore.collection("members").whereEqualTo("uid", uid);

		getMember.addSnapshotListener(new EventListener<QuerySnapshot>() {
			@Override
			public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
				if (null != e)
					Toast.makeText(MemberDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();

				for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
					if (snapshot.exists()){

						Member_Online member = snapshot.toObject(Member_Online.class);
						username.setText(member.getNames());
						firebaseFirestore.collection("bank").document(uid)
								.get()
								.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
									@Override
									public void onComplete(@NonNull Task<DocumentSnapshot> task) {
										DocumentSnapshot account = task.getResult();
										Account bal = account.toObject(Account.class);
										balance.setText(String.format("%s RF", String.valueOf(bal.getBalance())));
									}
								})
								.addOnFailureListener(new OnFailureListener() {
									@Override
									public void onFailure(@NonNull Exception e) {
										Toast.makeText(MemberDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
									}
								});

						Picasso.get().load(member.getPhotoUrl()).into(profile);
					}else {
						Toast.makeText(MemberDetails.this, "member not found", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private void getHistory(String uid) {

		list.setLayoutManager(new LinearLayoutManager(this));
		list.setHasFixedSize(true);

		Query query = FirebaseFirestore.getInstance().collection("History").document(getIntent().getStringExtra("uid")).collection("log").whereEqualTo("uid", uid);

		FirestoreRecyclerOptions<Historic> options = new FirestoreRecyclerOptions.Builder<Historic>()
				.setQuery(query, Historic.class)
				.build();

		List<Historic> histories = new ArrayList<>();

		adapter = new FirestoreRecyclerAdapter<Historic, HistoricVHolder>(options) {
			@Override
			protected void onBindViewHolder(@NonNull HistoricVHolder holder, int position, @NonNull Historic model) {
				if (model == null) {
					Toast.makeText(MemberDetails.this, "null", Toast.LENGTH_SHORT).show();
				} else {
					histories.add(model);
					Toast.makeText(MemberDetails.this, model.getCode(), Toast.LENGTH_SHORT).show();
					holder.bind(model);
					
				}
			}

			@NonNull
			@Override
			public HistoricVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_historic_item, parent, false);
				return new HistoricVHolder(view, viewType);
			}

			@Override
			public int getItemViewType(int position) {
				return TimelineView.getTimeLineViewType(position, getItemCount());
			}
		};
		
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);


	}


	@Override
	protected void onStart() {
		super.onStart();
		adapter.startListening();
	}

	@Override
	protected void onStop() {
		super.onStop();
		adapter.stopListening();
	}

	private class HistoricVHolder extends RecyclerView.ViewHolder {

		TextView timestamp, text;
		CircleImageView icon;
		TimelineView timelineView;

		public HistoricVHolder(View itemView, int viewType) {
			super(itemView);
			timestamp = itemView.findViewById(R.id.timestamp);
			text = itemView.findViewById(R.id.text);
			icon = itemView.findViewById(R.id.icon);
			timelineView = itemView.findViewById(R.id.timeline);

			timelineView.initLine(viewType);
		}

		void bind(Historic historic){
			timestamp.setText(String.valueOf(historic.getTimeStamp()));
			
			if (historic.getType() == Historic.HistoricType.LOAN){

			}

			switch (historic.getType()){
				case LOAN:
					timelineView.setMarker(getDrawable(R.drawable.loan));
					break;

				case DEBT:
					timelineView.setMarker(getDrawable(R.drawable.debt));
					break;

				case ACCOUNT:
					timelineView.setMarker(getDrawable(R.drawable.account));
					break;

				case DELETE:
					timelineView.setMarker(getDrawable(R.drawable.delete));
					break;

				case WITHDRAW:
					timelineView.setMarker(getDrawable(R.drawable.withdraw));
					break;

			}
		}
	}
}

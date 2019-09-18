package nassaty.com.cornerstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.com.cornerstone.models.Member_Online;

public class LoanDetails extends AppCompatActivity {

	CircleImageView avatar;
	TextView username, email, perc;

	FirebaseFirestore firestore;
	FirebaseAuth auth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan_details);

		avatar = findViewById(R.id.image);
		username = findViewById(R.id.username);
		email = findViewById(R.id.email);
		perc = findViewById(R.id.perc);

		firestore = FirebaseFirestore.getInstance();
		auth = FirebaseAuth.getInstance();

		show(getIntent().getStringExtra("uid"));
	}

	private void show(String uid) {
		firestore.collection("members").whereEqualTo("uid", uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
			@Override
			public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
				if (e != null)
					Toast.makeText(LoanDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();

				for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
					if (snapshot != null){
						Member_Online online = snapshot.toObject(Member_Online.class);
						username.setText(online.getNames());
						email.setText(auth.getCurrentUser().getEmail());
						Picasso.get().load(online.getPhotoUrl()).into(avatar);

					}
				}

			}
		});
	}
}

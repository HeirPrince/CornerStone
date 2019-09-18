package nassaty.com.cornerstone.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import nassaty.com.cornerstone.R;
import nassaty.com.cornerstone.callbacks.onMemberClicked;
import nassaty.com.cornerstone.models.Member_Offline;
import nassaty.com.cornerstone.models.Vault;
import nassaty.com.cornerstone.utils.Calculator;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberVHolder>{

	Context context;
	List<Member_Offline> memberOfflines;
	onMemberClicked callback;
	Vault vault;

	public MemberAdapter(Context context, List<Member_Offline> memberOfflines, Vault vault, onMemberClicked clicked) {
		this.context = context;
		this.memberOfflines = memberOfflines;
		this.callback = clicked;
		this.vault = vault;
	}

	@NonNull
	@Override
	public MemberVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_member_item, parent, false);
		return new MemberVHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MemberVHolder holder, int position) {
		Member_Offline memberOffline = memberOfflines.get(position);
		
		holder.bind(memberOffline, vault);
	}

	@Override
	public int getItemCount() {
		return memberOfflines.size();
	}

	public void setMemberOfflines(List<Member_Offline> memberOfflineList) {
		if (memberOfflines == null){
			memberOfflines = memberOfflineList;
			notifyItemRangeInserted(0, memberOfflines.size());
		}else {
			DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
				@Override
				public int getOldListSize() {
					return memberOfflines.size();
				}

				@Override
				public int getNewListSize() {
					return memberOfflines.size();
				}

				@Override
				public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
					return memberOfflines.get(oldItemPosition).getId() ==
							memberOfflines.get(newItemPosition).getId();
				}

				@Override
				public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
					Member_Offline newMemberOffline = memberOfflines.get(newItemPosition);
					Member_Offline oldMemberOffline = memberOfflines.get(oldItemPosition);
					return newMemberOffline.getId() == oldMemberOffline.getId()
							&& Objects.equals(newMemberOffline.getNames(), oldMemberOffline.getNames())
							&& Objects.equals(newMemberOffline.getAmount(), oldMemberOffline.getAmount())
							&& newMemberOffline.getUid() == oldMemberOffline.getUid();
				}
			});
			memberOfflines = memberOfflineList;
			result.dispatchUpdatesTo(this);
		}
	}

	class MemberVHolder extends RecyclerView.ViewHolder{

		TextView username, account, amt;

		MemberVHolder(View itemView) {
			super(itemView);
			username = itemView.findViewById(R.id.bal);
			account = itemView.findViewById(R.id.balance);
			amt = itemView.findViewById(R.id.shares);

			itemView.setOnClickListener(view -> callback.onClicked(memberOfflines.get(getAdapterPosition())));
		}

		void bind(Member_Offline memberOffline, Vault vault){
			username.setText(memberOffline.getNames());
			account.setText(String.format("%s RF", String.valueOf(memberOffline.getAmount())));
			if (vault == null)
				amt.setText("0%");
			else {
				amt.setText(String.format("%s%s", String.valueOf(Calculator.calculateShares(memberOffline.getAmount(), vault.getTotal())), context.getString(R.string.percentage)));
			}
		}
	}
}

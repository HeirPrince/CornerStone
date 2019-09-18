package nassaty.com.cornerstone.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nassaty.com.cornerstone.R;
import nassaty.com.cornerstone.models.History;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogVHolder> {

	private Context context;
	private List<History> logs;

	public LogAdapter(Context context, List<History> logs) {
		this.context = context;
		this.logs = logs;
	}

	@NonNull
	@Override
	public LogVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_log_item, parent, false);

		return new LogVHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull LogVHolder holder, int position) {
		holder.bind(logs.get(position));
	}

	@Override
	public int getItemCount() {
		return logs.size();
	}

	class LogVHolder extends RecyclerView.ViewHolder{

		TextView msg;

		LogVHolder(View itemView) {
			super(itemView);
			msg = itemView.findViewById(R.id.msg);
		}

		void bind(History log){
			msg.setText(log.getText());
		}
	}
	
}

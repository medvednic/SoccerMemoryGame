package itworks.eddy.soccermemorygame;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import itworks.eddy.soccermemorygame.Models.User;

/**
 * Created by medve on 16/05/2016.
 */
public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {
    private List<User> scores;

    public ScoresAdapter(List<User> scores) {
        this.scores = scores;
    }

    @Override
    public ScoresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ScoresAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoresAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(scores.get(position).getUsername());
        holder.tvScore.setText(scores.get(position).getLvl1().toString());
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvScore;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.tvScore = (TextView) itemView.findViewById(R.id.tvScore);
        }
    }
}



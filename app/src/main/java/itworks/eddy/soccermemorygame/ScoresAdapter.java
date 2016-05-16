package itworks.eddy.soccermemorygame;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import itworks.eddy.soccermemorygame.Models.User;

/** ScoresAdapter - Data adapter for RecyclerView functionality, also contains ViewHolder class.
 *
 */
public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {
    private List<User> scores;
    private int level;

    public ScoresAdapter(List<User> scores, int level) {
        this.scores = scores;
        this.level = level;
    }

    @Override
    public ScoresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ScoresAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoresAdapter.ViewHolder holder, int position) {
        //bind score data to view according to selected difficulty level
        String playerName = scores.get(position).getUsername();
        String playerScore = String.valueOf(scores.get(position).getLevelScore(level));
        holder.tvName.setText(playerName);
        holder.tvScore.setText(playerScore);
        //highlight the score of the current player
        if (playerName.equals(Session.currentUser.getUsername())){
            holder.tvName.setTextColor(Color.YELLOW);
            holder.tvScore.setTextColor(Color.YELLOW);
        }
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



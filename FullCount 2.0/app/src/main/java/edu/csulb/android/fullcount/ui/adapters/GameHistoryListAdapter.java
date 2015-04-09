package edu.csulb.android.fullcount.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Game;
import edu.csulb.android.fullcount.io.models.Team;

public class GameHistoryListAdapter extends ArrayAdapter<Game> {

    private Context mContext;
	private LayoutInflater mInflater;

	private Team mTeam;
    private ArrayList<Game> mGamesList;

    public GameHistoryListAdapter(Context context, Team team, ArrayList<Game> gameList) {
        super(context, R.layout.item_score_history, gameList);

        mContext = context;
	    mInflater = LayoutInflater.from(context);
	    mTeam = team;
	    mGamesList = gameList != null ? gameList : new ArrayList<Game>();
    }

	public class ViewHolder {
		TextView mTeamName;
		TextView mTeamScore;
		TextView mOpponentScore;
		TextView mOpponentName;
	}

    public View getView(int position, View convertView, ViewGroup parent) {

	    ViewHolder viewHolder;

	    if (convertView == null) {

		    convertView = mInflater.inflate(R.layout.item_score_history, parent, false);

		    viewHolder = new ViewHolder();
		    viewHolder.mTeamName = (TextView) convertView.findViewById(R.id.score_history_team);
		    viewHolder.mTeamScore = (TextView) convertView.findViewById(R.id.score_history_team_score);
		    viewHolder.mOpponentScore = (TextView) convertView.findViewById(R.id.score_history_opponent_score);
		    viewHolder.mOpponentName = (TextView) convertView.findViewById(R.id.score_history_opponent);

		    convertView.setTag(viewHolder);

	    } else {
		    viewHolder = (ViewHolder) convertView.getTag();
	    }

	    final Game game = mGamesList.get(position);

	    if (game != null) {
		    viewHolder.mTeamName.setText(mTeam.getName());
		    viewHolder.mTeamScore.setText(String.valueOf(game.getTeamScore()));
		    viewHolder.mOpponentScore.setText(String.valueOf(game.getOpponentScore()));
		    viewHolder.mOpponentName.setText(game.getOpponentName());
	    }

	    return convertView;
    }

    @Override
    public long getItemId(int position) {
	    return 0;
    }

}

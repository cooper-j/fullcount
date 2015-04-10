package edu.csulb.android.fullcount.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;
import edu.csulb.android.fullcount.io.models.RosterMember;

/**
 * Created by james_000 on 3/11/2015.
 */
public class TeamRosterListAdapter extends ArrayAdapter<RosterMember> {

    final int INVALID_ID = -1;
    private final Activity context;
    private ArrayList<RosterMember> mRosterMembers;
    HashMap<RosterMember, Integer> mIdMap = new HashMap<RosterMember, Integer>();

    public TeamRosterListAdapter(Activity context, ArrayList<RosterMember> rosterMembers) {
        super(context, R.layout.team_roster_list_item, rosterMembers);
        for (int i = 0; i < rosterMembers.size(); ++i) {
            mIdMap.put(rosterMembers.get(i), i);
        }
        mRosterMembers = rosterMembers;
        this.context = context;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.team_roster_list_item, null,true);

        ViewHolder holder = new ViewHolder();
        holder.removePlayerButton = (Button)rowView.findViewById(R.id.team_roster_list_delete);
        holder.removePlayerButton.setTag(mRosterMembers.get(position));

        holder.name = (TextView)rowView.findViewById(R.id.player_name);
        holder.icon = (ImageView)rowView.findViewById(R.id.player_icon);

        Button deleteButton = (Button) rowView.findViewById(R.id.team_roster_list_delete);
        deleteButton.setTag(position);

        deleteButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer index = (Integer)v.getTag();
                        mRosterMembers.remove(index.intValue());
                        notifyDataSetChanged();
                    }
                }
        );

        rowView.setTag(holder);

        setupItem(holder, position);
        return rowView;
    }

    private void setupItem(ViewHolder holder, int position) {
        holder.name.setText(mRosterMembers.get(position).getName());
        /* TODO add picture*/
        holder.icon.setImageResource(R.drawable.ic_launcher);
    }

    static class ViewHolder {
        TextView name;
        Button removePlayerButton;
        ImageView icon;
    }


    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        RosterMember item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

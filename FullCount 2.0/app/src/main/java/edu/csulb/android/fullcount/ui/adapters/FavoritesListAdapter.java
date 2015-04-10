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

import edu.csulb.android.fullcount.R;
import edu.csulb.android.fullcount.io.models.Player;

/**
 * Created by james_000 on 3/11/2015.
 */
public class FavoritesListAdapter extends ArrayAdapter<Player> {

    private final Activity context;
    private ArrayList<Player> mFavorites;

    public FavoritesListAdapter(Activity context, ArrayList<Player> favorites) {
        super(context, R.layout.player_list_item, favorites);

        mFavorites = favorites != null ? favorites : new ArrayList<Player>();
        this.context = context;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.player_list_item, null,true);

        ViewHolder holder = new ViewHolder();
        //holder.removePlayerButton = (Button)rowView.findViewById(R.id.team_roster_list_delete);
        //holder.removePlayerButton.setTag(mFavorites.get(position));

        holder.name = (TextView)rowView.findViewById(R.id.player_name);
        holder.icon = (ImageView)rowView.findViewById(R.id.player_icon);



        /*Button deleteButton = (Button) rowView.findViewById(R.id.team_roster_list_delete);
        deleteButton.setTag(position);

        deleteButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer index = (Integer)v.getTag();
                        mFavorites.remove(index.intValue());
                        notifyDataSetChanged();
                    }
                }
        );*/

        rowView.setTag(holder);

        setupItem(holder, position);
        return rowView;
    }

    private void setupItem(ViewHolder holder, int position) {
        holder.name.setText(mFavorites.get(position).getUsername());
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
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

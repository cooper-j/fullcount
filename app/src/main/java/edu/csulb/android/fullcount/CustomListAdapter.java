package edu.csulb.android.fullcount;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by james_000 on 3/11/2015.
 */
public class CustomListAdapter extends ArrayAdapter<Player> {

    private final Activity context;
    private List<Player> _players;

    public CustomListAdapter(Activity context, List<Player> players) {
        super(context, R.layout.batting_roster_list, players);

        this.context=context;
        _players = players;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.batting_roster_list, null,true);

        /*TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(_players.get(position).getName());
        imageView.setImageResource(_players.get(position).getImageId());

        Button deleteImageView = (Button)  rowView.findViewById(R.id.delete);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show();
            }
        });*/

        ViewHolder holder = new ViewHolder();
        holder.player = _players.get(position);
        holder.removePlayerButton = (Button)rowView.findViewById(R.id.delete);
        holder.removePlayerButton.setTag(holder.player);

        holder.name = (TextView)rowView.findViewById(R.id.Itemname);
        holder.icon = (ImageView)rowView.findViewById(R.id.icon);

        rowView.setTag(holder);

        setupItem(holder);

        return rowView;
    }

    private void setupItem(ViewHolder holder) {
        holder.name.setText(holder.player.getName());
        holder.icon.setImageResource(holder.player.getImageId());
    }

    static class ViewHolder {
        TextView name;
        Button removePlayerButton;
        ImageView icon;
        Player player;
    }
}

package edu.csulb.android.fullcount;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by james_000 on 3/11/2015.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> itemname;
    private final List<Integer> imgid;

    public CustomListAdapter(Activity context, List<String> itemname, List<Integer> imgid) {
        super(context, R.layout.batting_roster_list, itemname);

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.batting_roster_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(itemname.get(position));
        imageView.setImageResource(imgid.get(position));

        return rowView;
    }
}

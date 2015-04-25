package edu.csulb.android.fullcount.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import edu.csulb.android.fullcount.R;

/**
 * Created by james_000 on 4/18/2015.
 */

public class PlayerSearchAdapter extends SimpleCursorAdapter {

    private Context context=null;
    public PlayerSearchAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context=context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView=(ImageView)view.findViewById(R.id.player_icon);
        TextView textView=(TextView)view.findViewById(R.id.player_name);

        imageView.setImageResource(R.drawable.ic_launcher);
        textView.setText(cursor.getString(1));
    }
}

/*public class PlayerSearchAdapter extends CursorAdapter {

    private List<Player> items;

    private ImageView mIcon;
    private TextView mText;

    public PlayerSearchAdapter(Context context, Cursor cursor, List<Player> items) {

        super(context, cursor, false);

        this.items = items;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mIcon = (ImageView) view.findViewById(R.id.player_icon);
        mText = (TextView) view.findViewById(R.id.player_name);

        mIcon.setImageResource(R.drawable.ic_launcher);
        mText.setText(items.get(cursor.getPosition()).getUsername());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.player_list_item, parent, false);


        return view;

    }*/

    /*public override void BindView(View view, Context context, ICursor cursor)
    {
        var textView = view.FindViewById<TextView>(Android.Resource.Id.Text1);
        textView.Text = cursor.GetString(1); // 'name' is column 1 in the cursor query
    }
    public override View NewView(Context context, ICursor cursor, ViewGroup parent)
    {
        return this.context.LayoutInflater.Inflate(Android.Resource.Layout.SimpleListItem1, parent, false);
    }*/

//}

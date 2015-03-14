package edu.csulb.android.fullcount;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;

public class BattingRoster extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batting_roster);

        //final DynamicListView dynLv = (DynamicListView) findViewById(R.id.);

        //String[] players = {"Test1", "Test2", "Test3", "Test4", "Test5", "Test6", "Test7", "Test8", "Test9"};

        ArrayList<String> players = new ArrayList<>();
        players.add("Test1");
        players.add("Test2");
        players.add("Test3");
        players.add("Test4");
        players.add("Test5");
        players.add("Test6");
        players.add("Test7");
        players.add("Test8");
        players.add("Test9");

        Integer[] imgid = new Integer[players.size()];
        for (int i = 0; i < players.size(); i++) {
            imgid[i] = R.drawable.avatar_icon;
        }

        StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.batting_roster_list, players, imgid);
        DynamicListView listView = (DynamicListView) findViewById(R.id.listview);

        listView.setCheeseList(players);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }
}

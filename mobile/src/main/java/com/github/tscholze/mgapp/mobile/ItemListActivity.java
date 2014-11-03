package com.github.tscholze.mgapp.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all information and methods to provide a item list
 * activity of fetched rss items.
 */
public class ItemListActivity
        extends Activity
{

    private final static String FEED_URL = "http://mobilegeeks.de/feed";
    public final static String EXTRA_SELECTED_ITEM_CONTENT = "com.github.tscholze.mgapp.mobile.ItemListActivity.selectedItemContent";
    public final static String EXTRA_SELECTED_ITEM_TITLE = "com.github.tscholze.mgapp.mobile.ItemListActivity.selectedItemTitle";

    private SimpleRss2ParserCallback onParsedCallback;
    private ItemListActivity currentActivity;
    private ListView feedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        currentActivity = this;
        feedItems = (ListView)findViewById(R.id.feedItems);
        feedItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                RSSItem selectedItem = (RSSItem)feedItems.getItemAtPosition(position);
                handlePostClick(selectedItem);
            }
        });

        new SimpleRss2Parser(FEED_URL, getCallback()).parseAsync();
    }

    private void handlePostClick(RSSItem selectedItem)
    {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(EXTRA_SELECTED_ITEM_CONTENT, selectedItem.getContent());
        intent.putExtra(EXTRA_SELECTED_ITEM_TITLE, selectedItem.getTitle());
        startActivity(intent);
    }

    /**
     * Describes what happens after the parser finished.
     * On success set adapter from the item list to the view.
     * On error make a toast to inform the user.
     *
     * @return Callback method.
     */
    private SimpleRss2ParserCallback getCallback()
    {
        if (onParsedCallback == null)
        {
            onParsedCallback = new SimpleRss2ParserCallback()
            {

                @Override
                public void onFeedParsed(List<RSSItem> items)
                {
                    feedItems.setAdapter(new RSSItemListAdapter(currentActivity, R.layout.list_item,
                            (ArrayList<RSSItem>)items));
                }

                @Override
                public void onError(Exception ex)
                {
                    Toast.makeText(currentActivity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };
        }

        return onParsedCallback;
    }

    /**
     * List adapter to create rss item list views.
     */
    private class RSSItemListAdapter
            extends ArrayAdapter<RSSItem>
    {
        private ArrayList<RSSItem> items;
        private Context context;
        private int layout;

        public RSSItemListAdapter(Context context, int layout, ArrayList<RSSItem> items)
        {
            super(context, layout, items);
            this.items = items;
            this.context = context;
            this.layout = layout;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layout, null);
            }

            RSSItem item = items.get(position);
            if (item != null)
            {
                TextView feedPubDate = ((TextView)convertView.findViewById(R.id.feedPubDate));
                TextView feedTitle = ((TextView)convertView.findViewById(R.id.feedTitle));
                feedPubDate.setText(item.getDate());
                feedTitle.setText(item.getTitle());
            }

            return convertView;
        }
    }
}

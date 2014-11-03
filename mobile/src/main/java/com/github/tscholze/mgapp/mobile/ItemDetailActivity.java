package com.github.tscholze.mgapp.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebView;
import android.widget.TextView;


public class ItemDetailActivity
        extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent = getIntent();

        if (getActionBar() != null)
        {
            getActionBar().setTitle(intent.getStringExtra(ItemListActivity.EXTRA_SELECTED_ITEM_TITLE));
        }

        String itemContent = intent.getStringExtra(ItemListActivity.EXTRA_SELECTED_ITEM_CONTENT);
        TextView content = ((TextView)(findViewById(R.id.detail_content_web_view)));
        content.setText(Html.fromHtml(itemContent));
        content.setMovementMethod(new ScrollingMovementMethod());
    }
}

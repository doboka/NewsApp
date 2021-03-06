package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StoryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Story>> {

    private static final String LOG_TAG = StoryActivity.class.getName();

    /**
     * URL for story news data from the guardianapi dataset
     */
    private static final String GUARDIANAPI_REQUEST_URL =
            "https://content.guardianapis.com/search?";

    /**
     * Constant value for the story loader ID.
     */
    private static final int STORY_LOADER_ID = 1;

    /**
     * Adapter for the list of stories
     */
    private StoryAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView storyListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        storyListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of stories as input
        mAdapter = new StoryAdapter(this, new ArrayList<Story>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        storyListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected story.
        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current story that was clicked on
                Story currentStory = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri storyUri = Uri.parse(currentStory.getUrl());

                // Create a new intent to view the story URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, storyUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(STORY_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Story>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(GUARDIANAPI_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("section", "culture|politics");
        uriBuilder.appendQueryParameter("order-by", "newest");

        return new StoryLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous story news data
        mAdapter.clear();

        // If there is a valid list of {@link Story}s, then add them to the adapter's
        // data set.
        if (stories != null && !stories.isEmpty()) {
            mAdapter.addAll(stories);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            refreshListView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshListView() {
        LoaderManager loaderManager = getLoaderManager();
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
        if (loaderManager != null) {
            loaderManager.restartLoader(STORY_LOADER_ID, null, this);
        } else {
            loaderManager.initLoader(STORY_LOADER_ID, null, this);
        }
    }

}

package com.example.android.newsapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * An {@link StoryAdapter} knows how to create a list item layout for each story
 * in the data source (a list of {@link Story} objects).
 */
public class StoryAdapter extends ArrayAdapter<Story> {

    /**
     * Constructs a new {@link StoryAdapter}.
     *
     * @param context of the app
     * @param stories is the list of stories, which is the data source of the adapter
     */
    public StoryAdapter(Context context, List<Story> stories) {
        super(context, 0, stories);
    }

    /**
     * Returns a list item view that displays information about the story at the given position
     * in the list of stories.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Find the story at the given position in the list of stories
        Story currentStory = getItem(position);

        // Find the TextView with view ID article_title
        TextView articleTitleView = (TextView) listItemView.findViewById(R.id.article_title);
        // Display the title of the current story in that TextView
        articleTitleView.setText(currentStory.getTitle());

        // Find the TextView with view ID section_name
        TextView authorNameView = (TextView) listItemView.findViewById(R.id.section_name);
        // Display the title of the current story in that TextView
        authorNameView.setText(currentStory.getSection());

        // Create a new object for the publication date of the story
        String date = currentStory.getPublicationDate().replace("T", "\n").replace("Z", "");
        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Display the publication date of the current story in that TextView
        dateView.setText(date);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}

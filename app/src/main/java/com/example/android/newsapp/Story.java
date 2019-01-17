package com.example.android.newsapp;

/**
 * An {@link Story} object contains information related to a single story.
 */
public class Story {

    /**
     * Title of the story
     */
    private String mTitle;

    /**
     * Section to the story belongs
     */
    private String mSection;

    /**
     * Date published
     */
    private String mPublicationDate;

    /**
     * Website URL of the story
     */
    private String mUrl;

    /**
     * Constructs a new {@link Story} object.
     *
     * @param title           is the title of the story
     * @param section         is the section to the story belongs
     * @param publicationDate is the time when the story is published
     * @param url             is the website URL to find more details about the story
     */
    public Story(String title, String section, String publicationDate, String url) {
        mTitle = title;
        mSection = section;
        mPublicationDate = publicationDate;
        mUrl = url;
    }

    /**
     * Returns the title of the story.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the section of the story.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Returns the time of the publishing.
     */
    public String getPublicationDate() {
        return mPublicationDate;
    }

    /**
     * Returns the website URL to find more information about the story.
     */
    public String getUrl() {
        return mUrl;
    }
}


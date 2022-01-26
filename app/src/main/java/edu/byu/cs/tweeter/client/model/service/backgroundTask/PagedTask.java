package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask<T> extends AuthorizedTask {

    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";
    /**
     * Maximum number of followed users to return (i.e., page size).
     */
    protected final int limit;

    /**
     * The last in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected final T lastItem;
    private List<T> items;
    boolean hasMorePages;

    public PagedTask(Handler messageHandler, AuthToken authToken, int limit, T lastItem) {
        super(messageHandler, authToken);
        this.limit = limit;
        this.lastItem = lastItem;
    }

    @Override
    protected void runTask() {
        Pair<List<T>, Boolean> pageOfItems = getItems();

        items = pageOfItems.getFirst();
        hasMorePages = pageOfItems.getSecond();
        // TODO milestone 3
    }

    protected abstract Pair<List<T>, Boolean> getItems();

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }
}

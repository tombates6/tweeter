package edu.byu.cs.tweeter.client.presenter;
import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.client.presenter.view.PageView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter<PageView<T>> {
    protected static final int PAGE_SIZE = 10;
    protected T lastItem;
    private boolean hasMorePages;
    private boolean isLoading = false;
    
    public PagedPresenter(PageView<T> view, String logTag, String subject) {
        super(view, logTag, subject);
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadMoreItems(User user) {
        if (!isLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            getItemsFromService(user);
        }
    }
    
    public abstract void getItemsFromService(User user);

    public class GetItemsObserver implements PagedTaskObserver<T> {

        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            Log.e(LOG_TAG, message);
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage("Failed to get " + subject + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage());
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage("Failed to get " + subject + " because of exception: " + exception.getMessage());
        }
    }
}

package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.PageView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {
    private final StatusService feedService;

    public FeedPresenter(PageView<Status> view) {
        super(view, "FeedPresenter", "feed");
        this.feedService = new StatusService();
    }

    @Override
    public void getItemsFromService(User user) {
        feedService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new PagedTaskObserver());
    }
}

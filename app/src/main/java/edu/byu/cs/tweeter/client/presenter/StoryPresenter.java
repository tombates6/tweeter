package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.PageView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status>{
    private final StatusService storyService;

    public StoryPresenter(PageView<Status> view) {
        super(view, "StoryPresenter", "story");
        this.storyService = new StatusService();
    }

    @Override
    public void getItemsFromService(User user) {
        storyService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new PagedTaskObserver());
    }
}

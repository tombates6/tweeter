package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.view.PageView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User>{
    private final FollowService followingService;

    public FollowingPresenter(PageView<User> view) {
        super(view, "FollowingPresenter", "following");
        this.followingService = new FollowService();
    }

    @Override
    public void getItemsFromService(User user) {
        followingService.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new PagedTaskObserver());
    }
}

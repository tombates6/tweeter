package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.view.PageView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User>{
    private final FollowService followersService;

    public FollowersPresenter(PageView<User> view) {
        super(view, "FollowersPresenter", "followers");
        this.followersService = new FollowService();
    }

    @Override
    public void getItemsFromService(User user) {
        followersService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new PagedTaskObserver());
    }
}

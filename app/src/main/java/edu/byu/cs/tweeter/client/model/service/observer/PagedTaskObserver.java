package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

public interface PagedTaskObserver<T> extends BaseObserver {
    void handleSuccess(List<T> items, boolean hasMorePages);
}

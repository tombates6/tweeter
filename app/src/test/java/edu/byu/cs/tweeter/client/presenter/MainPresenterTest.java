package edu.byu.cs.tweeter.client.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.IEmptySuccessObserver;
import edu.byu.cs.tweeter.client.presenter.view.MainView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.util.Collections;

public class MainPresenterTest {
    private static MainPresenter presenter;
    private static StatusService statusService;
    private static MainView view;
    private static String post = "test";
    private static String failMsg = "fail";
    private static String exMsg = "exception";
    private static User user = new User("firstName", "lastName", "imageUrl");
    private static Status status = new Status("post", user, "dateTime", Collections.emptyList(), Collections.emptyList());

    public static class PostStatusTests {
        private static String postAction = "post status";
        @Before
        public void setup() throws ParseException {
            view = mock(MainView.class);
            presenter = spy(new MainPresenter(view));
            statusService = mock(StatusService.class);
            doNothing().when(presenter).showError(any(), any());
            doNothing().when(presenter).showFailure(any(), any());
            when(presenter.createStatus(post)).thenReturn(status);
            when(presenter.getStatusService()).thenReturn(statusService);
        }
        @Test
        public void success() throws ParseException {
            Answer<Void> answer = invocationOnMock -> {
                Object[] args = invocationOnMock.getArguments();
                MainPresenter.PostStatusObserver observer = (MainPresenter.PostStatusObserver) args[2];
                assertNotNull(observer);
                observer.handleSuccess();
                return null;
            };
            doAnswer(answer).when(statusService).postStatus(any(), any(), any());
            presenter.postStatus(post);
            verify(statusService).postStatus(eq(null), eq(status), any(IEmptySuccessObserver.class));
            verify(view).post();
        }

        @Test
        public void failure() throws ParseException {
            Answer<Void> answer = invocationOnMock -> {
                Object[] args = invocationOnMock.getArguments();
                MainPresenter.PostStatusObserver observer = (MainPresenter.PostStatusObserver) args[2];
                assertNotNull(observer);
                observer.handleFailure(failMsg);
                return null;
            };
            doAnswer(answer).when(statusService).postStatus(any(), any(), any());
            presenter.postStatus(post);
            verify(statusService).postStatus(eq(null), eq(status), any(IEmptySuccessObserver.class));
            verify(presenter).showFailure(postAction, failMsg);
        }

        @Test
        public void error() throws ParseException {
            Answer<Void> answer = invocationOnMock -> {
                Object[] args = invocationOnMock.getArguments();
                MainPresenter.PostStatusObserver observer = (MainPresenter.PostStatusObserver) args[2];
                assertNotNull(observer);
                observer.handleException(new Exception(exMsg));
                return null;
            };
            doAnswer(answer).when(statusService).postStatus(any(), any(), any());
            presenter.postStatus(post);
            verify(statusService).postStatus(eq(null), eq(status), any(IEmptySuccessObserver.class));
            verify(presenter).showError(postAction, exMsg);
        }
    }
}

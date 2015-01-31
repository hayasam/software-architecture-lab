package org.sm.lab.mybooks.client.ui.desktop;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.secure.CookieSecureSessionAccessor;
import net.customware.gwt.dispatch.client.secure.SecureDispatchAsync;

import org.sm.lab.mybooks.client.AppActivityMapper;
import org.sm.lab.mybooks.client.AppPlaceHistoryMapper;
import org.sm.lab.mybooks.client.ClientFactory;
import org.sm.lab.mybooks.client.activity.BookFormActivity;
import org.sm.lab.mybooks.client.activity.BookListActivity;
import org.sm.lab.mybooks.client.activity.LoginActivity;
import org.sm.lab.mybooks.client.activity.NoteFormActivity;
import org.sm.lab.mybooks.client.activity.ProfileFormActivity;
import org.sm.lab.mybooks.client.place.LoginPlace;
import org.sm.lab.mybooks.client.ui.desktop.view.BookFormViewImpl;
import org.sm.lab.mybooks.client.ui.desktop.view.BookListViewImpl;
import org.sm.lab.mybooks.client.ui.desktop.view.DesktopMainViewImpl;
import org.sm.lab.mybooks.client.ui.desktop.view.LoginViewImpl;
import org.sm.lab.mybooks.client.ui.desktop.view.NoteFormViewImpl;
import org.sm.lab.mybooks.client.ui.desktop.view.ProfileFormViewImpl;
import org.sm.lab.mybooks.client.util.IAppDialogBox;
import org.sm.lab.mybooks.client.view.BookFormView;
import org.sm.lab.mybooks.client.view.BookListView;
import org.sm.lab.mybooks.client.view.LoginView;
import org.sm.lab.mybooks.client.view.MainView;
import org.sm.lab.mybooks.client.view.NoteFormView;
import org.sm.lab.mybooks.client.view.ProfileFormView;
import org.sm.lab.mybooks.shared.AppConsts;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class ClientFactoryDesktopImpl implements ClientFactory {

    private DispatchAsync dispatchAsync;
	private EventBus eventBus;
	private PlaceController placeController;
	
	private MainView mainView;
	private IAppDialogBox appDialogBox;
	
	private LoginView loginView;
	private BookListView bookListView;
	private BookFormView bookFormView;
	private NoteFormView noteFormView;
	private ProfileFormView profileFormView;
	
	private LoginActivity loginActivity;
	private BookListActivity bookListActivity;
	private BookFormActivity bookFormActivity;
	private NoteFormActivity noteFormActivity;
	private ProfileFormActivity profileFormActivity;


	public ClientFactoryDesktopImpl() {
		dispatchAsync = new SecureDispatchAsync(new DefaultExceptionHandler(), new CookieSecureSessionAccessor(AppConsts.COOKIE_NAME));
		eventBus = new SimpleEventBus();
		placeController = new PlaceController(eventBus);
		mainView = new DesktopMainViewImpl(eventBus);
		
		appDialogBox = new AppDialogBox();
		
		Place defaultPlace = new LoginPlace();
		
        // Start ActivityManager for the main widget with our ActivityMapper
        ActivityMapper activityMapper = new AppActivityMapper(this);
        ActivityManager activityManager = new ActivityManager(activityMapper, this.getEventBus());
        activityManager.setDisplay(mainView.getContentPanel());
	
        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        AppPlaceHistoryMapper historyMapper= GWT.create(AppPlaceHistoryMapper.class);
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(this.getPlaceController(), this.getEventBus(), defaultPlace);
		
		RootLayoutPanel.get().add(mainView);
        historyHandler.handleCurrentHistory();
		
	}
	
	@Override
    public DispatchAsync getDispatchAsync() {
        return dispatchAsync;
    }
	
	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public MainView getMainView() {
		return mainView;
	}
	
	@Override
	public IAppDialogBox getAppDialogBox() {
		return appDialogBox;
	}
	
	@Override
	public LoginView getLoginView() {
		if (loginView == null) {
			loginView = new LoginViewImpl();
		}
		return loginView;
	}
	
	@Override
	public BookListView getBookListView() {
		if (bookListView == null) {
			bookListView = new BookListViewImpl();
		}
		return bookListView;
	}
	
	@Override
	public BookFormView getBookFormView() {
		if (bookFormView == null) {
			bookFormView = new BookFormViewImpl();
		}
		return bookFormView;
	}

	@Override
	public NoteFormView getNoteFormView() {
		if (noteFormView == null) {
			noteFormView = new NoteFormViewImpl();
		}
		return noteFormView;
	}	
	
	@Override
	public ProfileFormView getProfileFormView() {
		if (profileFormView == null) {
			profileFormView = new ProfileFormViewImpl();
		}
		return profileFormView;
	}
	
	@Override
	public LoginActivity getLoginActivity() {
		if (loginActivity == null) {
			loginActivity = new LoginActivity(this);
		}
		return loginActivity;
	}



	@Override
	public BookListActivity getBookListActivity() {
		if (bookListActivity == null) {
			bookListActivity = new BookListActivity(this);
		}
		return bookListActivity;
	}

	@Override
	public BookFormActivity getBookFormActivity() {
		if (bookFormActivity == null) {
			bookFormActivity = new BookFormActivity(this);
		}
		return bookFormActivity;	
	}

	@Override
	public NoteFormActivity getNoteFormActivity() {
		if (noteFormActivity == null) {
			noteFormActivity = new NoteFormActivity(this);
		}
		return noteFormActivity;	
	}

	@Override
	public ProfileFormActivity getProfileFormActivity() {
		if (profileFormActivity == null) {
			profileFormActivity = new ProfileFormActivity(this);
		}
		return profileFormActivity;
	}


}

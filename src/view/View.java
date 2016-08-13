package view;

import java.util.ArrayList;
import java.util.List;

import view.ViewImpl.UserInfo;
import controller.Controller;

/**
 *
 * Interface for the view. It defines methods which can be used by the
 * controller.
 *
 * @author Luca Giorgetti
 */
public interface View {

	/**
	 *
	 * method to invoke at the start of the program. It sets the main frame and
	 * the starting screen
	 */
	void startView();

	/**
	 *
	 * method which @return the typed Username in login.
	 */
	String getUsername();

	/**
	 *
	 * method which @return the typed Password in login.
	 */
	String getPassword();

	/**
	 *
	 * method which @return the string user wants to search.
	 */
	String getSearchText();

	/**
	 *
	 * method which @return the filter in which the user wants to search.
	 */
	String getSearchFilter();

	/**
	 *
	 * method which @return the type (Book or Movie) the user wants o search in.
	 */
	String getItemFilter();

	/**
	 * method which returns the score typed by user.
	 *
	 * @return
	 */
	int getScore();

	/**
	 *
	 * method which @return the review typed by the user.
	 */
	String getReview();

	/**
	 *
	 * method which sets the list of items filtered by the word typed .
	 */
	void setFilteredList();

	/**
	 *
	 * method which returns informations from user registration.
	 */
	void getUserRegistration();

	/**
	 *
	 * method which @return the password typed by manager.
	 */
	String getMenagerPassword();

	/**
	 *
	 * method which returns information from item creation.
	 */
	void getAddedItem();

	/**
	 *
	 * method which returns information from item removal.
	 */
	void getRemovedItem();

	/**
	 *
	 * method which @return the numeber of taken sit.
	 */
	int getStudyRoomSitsJustTaken();

	/**
	 *
	 * method which sets the status of study room.
	 */
	void setStudyRoomStatus();

	/**
	 * method which borrow item.
	 *
	 * @param selectedItem
	 */
	void borrowItem(String selectedItem);

	/**
	 * method which give back item.
	 *
	 * @param selectedItem
	 */
	void giveBackItem(String selectedItem);

	/**
	 * method which review item.
	 *
	 * @param selectedItem
	 */
	void reviewItem(String selecetedItem);

	/**
	 * method which puts user's like to item.
	 *
	 * @param selectedItem
	 */
	void likeItem(String selecetedItem);

	/**
	 * method which sets the program controller.
	 *
	 * @param c
	 */
	void setController(Controller c);

	List<String> setLikeList();

	List setBorrowedItemList();

	void swapView(String panelName);

	void sendLogin();

	void sendUserModify();

	void giveMeUserInfo();

	void setUserModifyField(String name, String surname, String username,
			String password, String birthDate, String email, String telephone);

	String getModifiedInfo(UserInfo info);

	void setStudyRoomStatus(ArrayList studyRoomStatus);
}

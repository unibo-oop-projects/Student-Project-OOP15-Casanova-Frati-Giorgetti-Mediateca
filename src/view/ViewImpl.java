package view;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import utils.UserInfo;
import view.ItemScreenImpl.ItemScreenType;
import view.ListScreenImpl.ListScreenType;
import view.UserLoginImpl.LoginType;
import view.UserScreenImpl.UserScreenType;
import controller.Controller;

/**
 * Main class of the view. Contains the implementation of methods which can be
 * used by controller.
 *
 * @author Luca
 *
 */

public class ViewImpl implements View {

	private static final JPanel container = new JPanel();
	private static final CardLayout cl = new CardLayout();
	final int SCREEN_LENGHT = 1280;
	final int SCREEN_WIDTH = 920;
	final int STUDY_ROOM_SITS = 100;

	private JPanel card1;
	private JPanel card2;
	private JPanel card3;
	private JPanel card4;
	private JPanel card5;
	private JPanel card6;
	private JPanel card7;
	private JPanel card8;
	private JPanel card9;
	private JPanel card10;
	private JPanel card11;

	private JFrame mainFrame = new JFrame();

	/**
	 * enum for List screen type
	 *
	 * @author Luca Giorgetti
	 *
	 */
	private Controller c;

	@Override
	public void setController(final Controller c) {
		this.c = c;
	}

	/**
	 * enum for card name.
	 *
	 * @author Luca Giorgetti
	 *
	 */
	public enum CardName {
		MAIN("Main Card"), LOGIN("Login Card"), MENU("Menu Card"), ITEM(
				"Item Card"), USER_MODIFY("User Modify Card"), LIKE_LIST(
				"LikeList Screen Card"), BORROWED_LIST(
				"BorrowedList Screen Card"), REVIEW("Review Card"), USER_CREATE(
				"User Create Card"), MANAGER_LOGIN("Manager Login"), ITEM_CREATE(
				"Item Create Card"), MANAGER_MENU("Manager Menu Card"), STUDY_ROOM(
				"Study Room Card");

		private final String name;

		/**
		 * @param text
		 */
		CardName(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void startView() {
		this.card1 = new UserLoginImpl(this, LoginType.USER,
				this.SCREEN_LENGHT, this.SCREEN_WIDTH);
		this.card2 = new UserMenuImpl(this, this.SCREEN_LENGHT,
				this.SCREEN_WIDTH);
		this.card3 = new MediatecaScreenImpl(this, this.SCREEN_LENGHT,
				this.SCREEN_WIDTH);
		this.card4 = new UserScreenImpl(this, UserScreenType.MODIFY,
				this.SCREEN_LENGHT, this.SCREEN_LENGHT);
		this.card5 = new ListScreenImpl(this, this.SCREEN_LENGHT,
				this.SCREEN_WIDTH, ListScreenType.LIKE);
		this.card6 = new ListScreenImpl(this, this.SCREEN_LENGHT,
				this.SCREEN_WIDTH, ListScreenType.BORROWED);
		this.card7 = new ReviewScreenImpl(this, this.SCREEN_LENGHT,
				this.SCREEN_WIDTH);
		this.card8 = new UserScreenImpl(this, UserScreenType.CREATE,
				this.SCREEN_LENGHT, this.SCREEN_LENGHT);
		this.card9 = new UserLoginImpl(this, LoginType.MANAGER,
				this.SCREEN_LENGHT, this.SCREEN_WIDTH);
		this.card10 = new ItemScreenImpl(this, ItemScreenType.CREATE,
				this.SCREEN_LENGHT, this.SCREEN_LENGHT);
		this.card11 = new StudyRoomImpl(this, this.STUDY_ROOM_SITS);
		this.mainFrame.setTitle("Mediateca");
		this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.mainFrame.setSize(this.SCREEN_LENGHT, this.SCREEN_WIDTH);
		this.mainFrame.setResizable(false);
		ViewImpl.container.setLayout(ViewImpl.cl);
		final JPanel card0 = new JPanel();
		final JLabel welcome = new JLabel("Benvenuto in Mediateca!");
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setFont(new Font("Tahoma", Font.BOLD, 40));
		welcome.setBounds(325, 63, 615, 82);
		final JButton login = new JButton("Login");
		login.setFont(new Font("Tahoma", Font.BOLD, 30));
		final JButton userCreate = new JButton("Registrati");
		userCreate.setFont(new Font("Tahoma", Font.BOLD, 30));
		userCreate.setBounds(670, 173, 281, 179);
		login.setBounds(299, 173, 281, 179);

		card0.setLayout(null);
		card0.setSize(this.SCREEN_LENGHT, this.SCREEN_WIDTH);
		card0.add(welcome);
		card0.add(login);
		card0.add(userCreate);
		this.card1.setSize(this.SCREEN_LENGHT, this.SCREEN_WIDTH);

		ViewImpl.container.add(card0, CardName.MAIN.toString());

		JLabel ManagerLogin = new JLabel("Accedi come Gestore");
		ManagerLogin.setHorizontalAlignment(SwingConstants.CENTER);
		ManagerLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				ViewImpl.this.swapView(CardName.MANAGER_LOGIN);
			}
		});
		ManagerLogin.setFont(new Font("Tahoma", Font.BOLD, 30));
		ManagerLogin.setBounds(40, 801, 334, 64);
		card0.add(ManagerLogin);
		ViewImpl.container.add(this.card1, CardName.LOGIN.toString());
		ViewImpl.container.add(this.card2, CardName.MENU.toString());
		ViewImpl.container.add(this.card3, CardName.ITEM.toString());
		ViewImpl.container.add(this.card4, CardName.USER_MODIFY.toString());
		ViewImpl.container.add(this.card5, CardName.LIKE_LIST.toString());
		ViewImpl.container.add(this.card6, CardName.BORROWED_LIST.toString());
		ViewImpl.container.add(this.card7, CardName.REVIEW.toString());
		ViewImpl.container.add(this.card8, CardName.USER_CREATE.toString());
		ViewImpl.container.add(this.card9, CardName.MANAGER_LOGIN.toString());
		ViewImpl.container.add(this.card10, CardName.ITEM_CREATE.toString());
		ViewImpl.container.add(this.card11, CardName.STUDY_ROOM.toString());

		this.swapView(CardName.MAIN);
		login.addActionListener(e -> this.swapView(CardName.LOGIN));
		userCreate.addActionListener(e -> this.swapView(CardName.USER_CREATE));
		this.mainFrame.getContentPane().add(ViewImpl.container);
		this.mainFrame.setVisible(true);
	}

	// //OK

	@Override
	public void swapView(final CardName name) {
		ViewImpl.cl.show(ViewImpl.container, name.toString());
		return;
	}

	// //OK
	@Override
	public String getUsername() {
		return ((UserLogin) this.card1).getUserUsername();
	}

	// //OK
	@Override
	public String getPassword() {
		return ((UserLogin) this.card1).getUserPassword();

	}

	// //OK
	@Override
	public String getSearchFilter() {
		return ((MediatecaScreen) this.card3).getSearchFilter();

	}

	// //OK
	@Override
	public String getItemFilter() {
		return ((MediatecaScreen) this.card3).getItemType();

	}

	// //OK
	@Override
	public String getSearchText() {
		return ((MediatecaScreen) this.card3).getSearchText();

	}

	// //OK
	@Override
	public int getScore() {
		return ((ReviewScreen) this.card7).getSelectedScore();
	}

	// //OK
	@Override
	public String getReview() {
		return ((ReviewScreen) this.card7).getReview();
	}

	// //OK
	@Override
	public void setFilteredList(final String[] filteredList) {
		((MediatecaScreenImpl) this.card3).setFilteredList(filteredList);
	}

	// //OK
	@Override
	public void setBorrowedItemList(final String[] borrowedItemsList) {
		((ListScreenImpl) this.card6).setBorrowedList(borrowedItemsList);
	}

	@Override
	public void borrowItem() {
	}

	@Override
	public void giveBackItem() {
	}

	@Override
	public void reviewItem() {
	}

	@Override
	public void likeItem() {
	}

	@Override
	public void getUserRegistration(final utils.UserInfo info) {
		((UserScreenImpl) this.card8).getInfo(info);

	}

	@Override
	public void getAddedItem() {

	}

	@Override
	public void getRemovedItem() {

	}

	// //OK
	@Override
	public void sendLogin() {
		this.c.login();
	}

	@Override
	public String getMenagerPassword() {
		return ((UserLoginImpl) this.card9).getManagerPassword();
	}

	// //waiting CONTROLLER Function Name
	@Override
	public void sendUserModify() {
		// ViewImpl.c.userModify();
	}

	// //OK
	@Override
	public void setUserModifyField(final String name, final String surname,
			final String username, final String password,
			final String birthDate, final String email, final String telephone) {
		((UserScreenImpl) this.card4).setField(name, surname, username,
				password, birthDate, email, telephone);
	}

	// //OK
	@Override
	public void giveMeUserInfo() {

	}

	// //OK
	@Override
	public String getModifiedInfo(final UserInfo info) {
		return ((UserScreenImpl) this.card4).getInfo(info);
	}

	// //OK
	@Override
	public void setLikeItemList(final String[] likeList) {
		((ListScreenImpl) this.card5).setLikeList(likeList);
	}

	// //waiting CONTROLLER function name
	@Override
	public void giveMeBorrowList() {
		// ViewImpl.c.borrowList();
	}

	// //waiting CONTROLLER function name
	@Override
	public void giveMeLikeList() {
		// ViewImpl.c.likeList();
	}

	// //waiting CONTROLLER function name
	@Override
	public void controllerGetReview() {
		// ViewImpl.c.review();

	}

	// //waiting CONTROLLER function name
	@Override
	public void sendUserCreate() {
		// this.c.userCreate();

	}

	// //waiting CONTROLLER function name
	@Override
	public void sendManagerLogin() {
		// this.c.managerLogin();

	}

	// //waiting CONTROLLER function name
	@Override
	public void giveMeFilteredList() {
		// this.c.filterList();

	}

	// //waiting CONTROLLER function name
	@Override
	public void sendItemCreate() {
		// this.c.itemCreate();

	}

	// //OK
	@Override
	public Object getItemInfo(final utils.ItemInfo info) {
		return ((ItemScreenImpl) this.card10).getItemInfo(info);
	}

	// //OK
	@Override
	public void setBookModifyField(final String title, final String author,
			final String manifacturer, final String year,
			final utils.ItemGenre genre, final String imagePath,
			final String isbn, final utils.Language language) {
		((ItemScreenImpl) this.card10).setBookField(title, author,
				manifacturer, year, genre, imagePath, isbn, language);
	}

	// //OK
	@Override
	public void setFilmModifyField(final String title, final String author,
			final String manifacturer, final String year,
			final utils.ItemGenre genre, final String imagePath,
			final String duration, final utils.TypeColor color,
			final utils.Language language) {
		((ItemScreenImpl) this.card10)
		.setFilmField(title, author, manifacturer, year, genre,
				imagePath, duration, color, language);
	}

	// //waiting CONTROLLER function name
	@Override
	public void giveMeItemInfo() {
		// this.c.setItemInfo();

	}

	@Override
	public void sendItemModify() {
		// this.c.itemModify();
	}

	@Override
	public void showItemInfo(final utils.TypeItem type) {
		ItemShow itemScreen = new ItemShowImpl();
		itemScreen.startItemShow(this, type);
	}

	@Override
	public void goodLogin() {
		this.swapView(CardName.MENU);
	}

	@Override
	public void showError(final String errorMessage) {
		JOptionPane.showMessageDialog(this.mainFrame, errorMessage);
	}

	/*
	 * @Override public void showMessage(final String message) {
	 * JOptionPane.showMessageDialog(this.mainFrame,message); }
	 */
	// //waiting CONTROLLER function name
	@Override
	public void takeSit() {
		// c.takeSit();
	}

	// //waiting CONTROLLER function name
	@Override
	public void giveMeStudyRoomStatus() {
		// c.studyRoomStatus
	}

	@Override
	public void setStudyRoomStatus(final int[] status) {
		((StudyRoomImpl) this.card11).setStudyRoomStatus(status);

	}

}

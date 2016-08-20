package controller;

import java.awt.print.Book;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import model.Model;
import model.ModelImpl;
import model.Pair;
import model.item.ItemImpl;
import model.item.ItemInfo;
import model.item.ReviewImpl;
import model.user.User;
import model.user.UserImpl;
import utils.ItemGenre;
import utils.Language;
import utils.TypeItem;
import utils.TypeItemInfo;
import utils.UserInfo;
import view.View;
import view.ViewImpl;

/**
 * Class which implements the controller interface.
 *
 * @author
 *
 */

public class ControllerImpl implements Controller {
	private View v;
	private Model m;
	// after the login, the corrispondent user will be saved here...
	private UserImpl actualUser;
	// ...and its LoanArchive will be saved here
	private Map<Integer, Pair<Boolean, Optional<Integer>>> actualLoanArchive;

	// constants for I/O
	private static final String FILENAMEUSER = "archivio.utenti";
	private static final String FILENAMEITEM = "archivio.oggetti";
	private static final String FILENAMESTUDYROOM = "archivio.aulastudio";

	private FileManager fm = new FileManager();

	/**
	 * Constructor for ControllerImpl.
	 *
	 * @throws Exception
	 *             in the case which singleton already exist.
	 */
	public ControllerImpl() throws Exception {
		File fileItem = new File(this.fm.getPath() + ControllerImpl.FILENAMEITEM);
		File fileUser = new File(this.fm.getPath() + ControllerImpl.FILENAMEUSER);
		File fileStudyRoom = new File(this.fm.getPath() + ControllerImpl.FILENAMESTUDYROOM);

		if ((fileItem.exists() && !fileItem.isDirectory()) && (fileUser.exists() && !fileUser.isDirectory())
				&& (fileStudyRoom.exists() && !fileStudyRoom.isDirectory())) {
			Map<Integer, UserImpl> userArchive = this.fm.readArchiveUserFromFile(ControllerImpl.FILENAMEUSER);
			Map<Integer, Pair<ItemImpl, ItemInfo>> itemArchive = this.fm
					.readArchiveItemFromFile(ControllerImpl.FILENAMEITEM);
			Map<GregorianCalendar, ArrayList<Integer>> studyRoomArchive = this.fm
					.readStudyRoomFromFile(ControllerImpl.FILENAMESTUDYROOM);
			this.m = new ModelImpl(itemArchive, userArchive, studyRoomArchive);
		} else {
			this.m = new ModelImpl();
			this.writeOnFile();
		}
	}

	@Override
	public void writeOnFile() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 1994);
		calendar.set(Calendar.MONTH, 3);
		calendar.set(Calendar.DAY_OF_MONTH, 6);

		try {
			this.m.registerUser("Enrico", "Casanova", calendar, "asd", "asd", "enrico.casanova@dadas.it",
					"334534534534", new ArrayList<ItemGenre>(), new ArrayList<ItemGenre>());
			this.m.registerBook("Il signore degli anelli", 1945, "J.R.R. Tolkien", Language.ENGLISH, "23123121",
					ItemGenre.ADVENTURE_HISTORY, "Ges�", 0011, 100000);
			this.m.registerMovie("Star Trek", 2009, "Bad Robot", "J.J. Abrams", Language.ENGLISH, ItemGenre.FANTASY,
					120, true, 1000000);
			User u = new UserImpl("Enrico", "Casanova", calendar, "asd", "asd", "enrico.casanova@dadas.it",
					"334534534534", new ArrayList<ItemGenre>(), new ArrayList<ItemGenre>());

			this.m.bookSit(calendar, 1, ((UserImpl) u).getIdUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.fm.writeObjectIntoFile("archivio.utenti", this.m);
		this.fm.writeObjectIntoFile("archivio.oggetti", this.m);
		this.fm.writeObjectIntoFile("archivio.aulastudio", this.m);
	}

	@Override
	public void userLogin() {
		final String username = this.v.getUsername();
		final String password = this.v.getPassword();
		Map<Integer, UserImpl> map = this.m.getUserArchive();
		java.util.Optional<UserImpl> user = map.entrySet().stream()
				.filter(e -> e.getValue().getUsername().equals(username))
				.filter(e -> e.getValue().getPassword().equals(password)).map(e -> e.getValue()).findFirst();
		if (user.isPresent()) {
			this.actualUser = user.get();
			this.v.goodLogin();
			// mettere metodo

			try {
				this.m.setReccomandedList(this.actualUser.getIdUser());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			this.v.showError("Utente non trovato");
		}

		/*
		 * for (Entry<Integer, UserImpl> entry : map.entrySet()) { if
		 * ((entry.getValue().getUsername().equals(username)) &&
		 * (entry.getValue().getPassword().equals(password))) { this.actualUser
		 * = entry.getValue(); this.v.goodLogin(); // inserire
		 * showGiveBackOption check = true; try {
		 * this.m.setReccomandedList(this.actualUser.getIdUser()); } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } break; } } if (!check) { this.v.showError(
		 * "Utente non trovato"); }
		 */
	}

	@Override
	public void managerLogin() {
		if (this.m.getSystemPassword().equals(this.v.getMenagerPassword())) {
			// esegui login manager
			this.v.showMessage("Login effettuato");
			// inserire metodo
		} else {
			this.v.showMessage("Password errata");
		}
	}

	/**
	 *
	 * Method who elaborates inputs from the user and set the list with items
	 * filtered.
	 *
	 * @throws Exception
	 */
	@Override
	public void itemElaboration() {
		int index = 0;
		TypeItem ty = null;
		for (TypeItem y : TypeItem.values()) {
			if (y.equals(this.v.getItemFilter())) {
				ty = y;
			}
		}
		TypeItemInfo ts = null;
		for (TypeItemInfo s : TypeItemInfo.values()) {
			if (s.equals(this.v.getSearchFilter())) {
				ts = s;
			}
		}
		Object searchText = this.v.getSearchText();
		String[] array = new String[this.m.getAllItemId(ty).size()];
		try {
			for (Integer i : this.m.filtersItem(this.m.getAllItemId(ty), ts, searchText)) {
				array[index] = this.m.getItemArchive().get(i).toString();
				index++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.v.showError("Filtraggio oggetti fallito");
		}
		this.v.setFilteredList(array);
	}

	// first draft
	@Override
	public void addLike() {
		// DA CONTROLLARE
		for (Integer i : this.m.getItemArchive().keySet()) {
			if (this.m.getItemArchive().get(i).toString().equals(this.v.getItemSelectedByUser())) {
				try {
					this.m.addLike(i, this.actualUser.getIdUser());
					this.v.showMessage("Oggetto " + this.m.getItemArchive().get(i) + " messo in wishlist");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					this.v.showError("Errore inserimento oggetto in wishlist");
				}
			}
		}
	}

	@Override
	public void addReview() {
		// DA CONTROLLARE
		for (Integer i : this.m.getItemArchive().keySet()) {
			if (this.m.getItemArchive().get(i).toString().equals(this.v.getItemSelectedByUser())) {
				try {
					this.m.addReview(i, this.actualUser.getIdUser(), this.v.getScore(), this.v.getReview());
					this.v.showMessage("Recensione per l'oggetto " + this.m.getItemArchive().get(i) + "inserita");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					this.v.showError("Errore inserimento recensione oggetto");
				}
			}
		}
	}

	@Override
	public void borrowList() {
		try {
			this.actualLoanArchive = this.actualUser.getLoanArchive();
			String[] array = new String[this.actualLoanArchive.size()];
			int index = 0;

			for (Integer i : this.m.getItemBorrowed(this.actualUser.getIdUser())) {
				array[index] = this.m.getItemArchive().get(i).getFirst().toString();
				index++;
			}
			this.v.setBorrowedItemList(array);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.v.showError("Errore ritrovamento oggetto prestato");
		}
	}

	public void borrowItem() {
		// DA FARE
		// oggetto da prenotare
		item id = this.v.getItemSelectedByUser(); // viene da lista oggetti
													// filtrati
		this.m.borrowItem(itemId, this.actualUser.getIdUser());
	}

	@Override
	public void userModify() {
		// for per ogni tipo di userinfo e fare changeUser totale
		for (UserInfo ui : UserInfo.values()) {
			try {
				this.m.changeUser(ui, this.actualUser.getIdUser(), this.v.getModifiedInfo(ui));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				this.v.showError("Errore nell'UserInfo");
			}
		}
	}

	@Override
	public void itemModify() {
		// getItemToRemoveModify ottiene la stringa da setAllItemList!!!
		Integer itemId = null;
		for (Integer i : this.m.getItemArchive().keySet()) {
			if (this.m.getItemArchive().get(i).toString().equals(this.v.getItemToRemoveModify())) {
				itemId = i;
			}
		}
		for (TypeItemInfo ti : TypeItemInfo.values()) {
			try {
				switch (ti) {
				case TITLE:
					this.m.changeItem(ti, itemId, this.v.getItemInfo(ti));
					break;
				case RELEASE_YEAR:
					this.m.changeItem(ti, itemId, (int) this.v.getItemInfo(ti));
					break;
				case AUTHOR:
					this.m.changeItem(ti, itemId, this.v.getItemInfo(ti));
					break;
				case LANGUAGE:
					this.m.changeItem(ti, itemId, this.v.getItemInfo(ti));
					break;
				case GENRE:
					this.m.changeItem(ti, itemId, this.v.getItemInfo(ti));
					break;
				case PRODUCER:
					this.m.changeItem(ti, itemId, this.v.getItemInfo(ti));
					break;
				default:
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				this.v.showError("Errore nell'TypeItemInfo o itemId");
			}
		}
	}

	public void setItemInfo() {
		// getItemSelectedByuSer lo prendo da setFilteredList
		Integer itemId;
		for (Integer i : this.m.getItemArchive().keySet()) {
			if (this.m.getItemArchive().get(i).toString().equals(this.v.getItemSelectedByUser())) {
				itemId = i;
			}
		}
		String title = this.m.getItemArchive().get(itemId).getFirst().getTitle();
		int releaseYear = this.m.getItemArchive().get(itemId).getFirst().getReleaseYear();
		String author = this.m.getItemArchive().get(itemId).getFirst().getAuthor();
		Language language = this.m.getItemArchive().get(itemId).getFirst().getCurrentLanguage();
		String isbn = (String) this.v.getItemInfo(TypeItemInfo.ISBN);// mancante
		ItemGenre genre = this.m.getItemArchive().get(itemId).getFirst().getGenre();
		String publisher = this.m.getItemArchive().get(itemId).getFirst().getPublisher();
		Integer numRelease = (Integer) this.v.getOtherItemInfo(ViewImpl.OtherItemFilter.RELEASE_NUMBER);
		Integer numCopy = (Integer) this.v.getOtherItemInfo(ViewImpl.OtherItemFilter.COPIES_NUMBER);
		String duration = (String) this.v.getItemInfo(TypeItemInfo.DURATION);
		String color = (String) this.v.getItemInfo(TypeItemInfo.COLOR);

		// determinare il tipo book o movie in base all'id
		if (TypeItem.BOOK.getClass().isInstance(this.m.getItemArchive().get(itemId).getFirst()){
		this.v.setBookField(title, author, publisher, releaseYear, genre, isbn, language, numCopy, numRelease);
		} else {
			this.v.setFilmField(title, author, publisher, releaseYear, genre, duration, color, language, numCopy,
				numRelease);
		}
	}

	public void suggestedBook(){
		// stampa 3 libri di 3 preferenze
		int index = 0;
		float big = 0;
		String[] array = new String[3];
		for (ItemGenre ig : this.m.getUserArchive().get(this.actualUser.getIdUser()).getBookPreferences()){
		for (Integer i : this.m.getItemArchive().keySet()){
			this.m.getItemArchive().get(i).getFirst().setAverageVote();
			if (this.m.getItemArchive().get(i).getFirst().getGenre() == ig){
				if (this.m.getItemArchive().get(i).getFirst().getAverageVote() > big){
					big = this.m.getItemArchive().get(i).getFirst().getAverageVote();
				}

			}
		}
		}

			array[index] = this.m.get


		this.v.setSuggestedBooks(array);
	}

	public void suggestedfilm() {
		this.m.getUserArchive().get(this.actualUser.getIdUser()).getMoviePreferences();
		// controllare ogni oggetto di quel genere e fare la media recensioni,
		// questo per i 3 generi preferiti dall'utente
		this.v.setSuggestedFilms(bList);
	}

	@Override
	public void registerNewUser() {
		String name = (String) this.v.getUserRegistration(UserInfo.NAME);
		String surname = (String) this.v.getUserRegistration(UserInfo.SURNAME);
		GregorianCalendar day = new GregorianCalendar();
		day.set(this.v.getStudyRoomSelectedYear(), this.v.getStudyRoomSelectedMonth(),
				this.v.getStudyRoomSelectedDay());
		String username = (String) this.v.getUserRegistration(UserInfo.USERNAME);
		String password = (String) this.v.getUserRegistration(UserInfo.PASSWORD);
		String email = (String) this.v.getUserRegistration(UserInfo.EMAIL);
		String telephoneNumber = (String) this.v.getUserRegistration(UserInfo.TELEPHONE_NUMBER);
		ItemGenre bookPref1 = (ItemGenre) this.v.getUserRegistration(UserInfo.BOOK_PREF1);
		ItemGenre bookPref2 = (ItemGenre) this.v.getUserRegistration(UserInfo.BOOK_PREF2);
		ItemGenre bookPref3 = (ItemGenre) this.v.getUserRegistration(UserInfo.BOOK_PREF3);
		List<ItemGenre> bookList = new ArrayList<>(Arrays.asList(bookPref1, bookPref2, bookPref3));
		ItemGenre moviePref1 = (ItemGenre) this.v.getUserRegistration(UserInfo.FILM_PREF1);
		ItemGenre moviePref2 = (ItemGenre) this.v.getUserRegistration(UserInfo.FILM_PREF2);
		ItemGenre moviePref3 = (ItemGenre) this.v.getUserRegistration(UserInfo.FILM_PREF3);
		List<ItemGenre> movieList = new ArrayList<>(Arrays.asList(moviePref1, moviePref2, moviePref3));
		try {
			this.m.registerUser(name, surname, day, username, password, email, telephoneNumber, bookList, movieList);
			this.v.showMessage("Utente " + username + " registrato con successo!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.v.showError("Errore! Utente gi� presente nell'archivio");
		}
	}

	/**
	 * Method which adds a new Book to the archive.
	 */
	public void registerNewBook() {
		String title = (String) this.v.getItemInfo(TypeItemInfo.TITLE);
		int releaseYear = (int) this.v.getItemInfo(TypeItemInfo.RELEASE_YEAR);
		String author = (String) this.v.getItemInfo(TypeItemInfo.AUTHOR);
		Language language = (Language) this.v.getItemInfo(TypeItemInfo.LANGUAGE);
		String isbn = (String) this.v.getItemInfo(TypeItemInfo.ISBN);
		ItemGenre genre = (ItemGenre) this.v.getItemInfo(TypeItemInfo.GENRE);
		String publisher = (String) this.v.getItemInfo(TypeItemInfo.PRODUCER);
		Integer numRelease = (Integer) this.v.getOtherItemInfo(ViewImpl.OtherItemFilter.RELEASE_NUMBER);
		Integer numCopy = (Integer) this.v.getOtherItemInfo(ViewImpl.OtherItemFilter.COPIES_NUMBER);
		try {
			this.m.registerBook(title, releaseYear, author, language, isbn, genre, publisher, numRelease, numCopy);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.v.showError("Errore! Numero di copie inferiori a 0");
		}
	}

	/**
	 * Method which adds a new Movie to the archive.
	 */
	public void registerNewMovie() {
		String title = (String) this.v.getItemInfo(TypeItemInfo.TITLE);
		int releaseYear = (int) this.v.getItemInfo(TypeItemInfo.RELEASE_YEAR);
		String publisher = (String) this.v.getItemInfo(TypeItemInfo.PRODUCER);
		String author = (String) this.v.getItemInfo(TypeItemInfo.AUTHOR);
		Language language = (Language) this.v.getItemInfo(TypeItemInfo.LANGUAGE);
		ItemGenre genre = (ItemGenre) this.v.getItemInfo(TypeItemInfo.GENRE);
		Integer duration = (Integer) this.v.getItemInfo(TypeItemInfo.DURATION);
		Boolean color = (Boolean) this.v.getItemInfo(TypeItemInfo.COLOR);
		Integer numCopy = (Integer) this.v.getOtherItemInfo(ViewImpl.OtherItemFilter.COPIES_NUMBER);
		try {
			this.m.registerMovie(title, releaseYear, publisher, author, language, genre, duration, color, numCopy);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.v.showError("Errore! Numero di copie inferiori a 0");
		}
	}

	@Override
	public void itemCreate() {
		if (this.v.getItemInfo(TypeItemInfo.TYPE) == TypeItem.BOOK) {
			this.registerNewBook();
		} else if (this.v.getItemInfo(TypeItemInfo.TYPE) == TypeItem.MOVIE) {
			this.registerNewMovie();
		}
	}

	@Override
	public void elaborateLoans() {
		Map<Integer, Double> map;
		try {
			map = this.m.checkDeadlineas(this.actualUser.getIdUser());

			map.keySet().stream().filter(i -> map.get(i) > 30).forEach(
					i -> this.v.showGiveBackOptionMessage(this.m.getItemArchive().get(i).getFirst().getTitle()));
			/*
			 * for (Integer i : map.keySet()) { if (map.get(i) > 30) {
			 * this.v.showGiveBackOptionMessage(i.toString()); } }
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.v.showError("Errore! Utente non presente nell'archivio");
		}
	}

	@Override
	public void giveBackItem(final String item) {
		// DA RIVEDERE
		for (Integer i : this.actualUser.getLoanArchive().keySet()) {
			if (this.m.getItemArchive().get(i).getFirst().getTitle().equals(item)) {
				try {
					this.m.returnItem(i, this.actualUser.getIdUser());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					this.v.showError("Errore! Id utente e oggetto non associati");
				}
			} else {
				this.v.showMessage("Oggetto da restituire non trovato!");
			}
		}
	}

	public void giveBackItemSelectedByUser() {
		this.giveBackItem(this.v.getItemSelectedByUser());
	}

	public void settakensitslist() {
		for (GregorianCalendar gc : this.m.getStudyRoom().keySet()) {

		}
		// ritorna array di stringhe che mostra data e posto occupati
	}

	@Override
	public void takeSit() {
		GregorianCalendar day = new GregorianCalendar();
		day.set(this.v.getStudyRoomSelectedYear(), this.v.getStudyRoomSelectedMonth(),
				this.v.getStudyRoomSelectedDay());
		try {
			this.m.bookSit(day, this.v.getTakenSits(), this.actualUser.getIdUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.v.showError("Errore! Posto o Id utente non validi");
		}
	}

	@Override
	public void cancelSit() {

		this.v.getSelectedSit(); // convertire per ottenere nel posto da
									// cancellare e il giorno
		// mi ritorna un pair con posto, giorno e devo fare il cast inverso

		try {
			this.m.cancelSit(day, this.v.getTakenSits(), this.actualUser.getIdUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.v.showError("Errore! Posto selezionato non valido per la cancellazione");
		}
	}

	@Override
	public void studyRoomStatus() {
		GregorianCalendar day = new GregorianCalendar();
		day.set(this.v.getStudyRoomSelectedYear(), this.v.getStudyRoomSelectedMonth(),
				this.v.getStudyRoomSelectedDay());
		int[] arrayint = new int[this.m.getAllUserSit(day).size()];
		int index = 0;
		for (Integer i : this.m.getAllUserSit(day)) {
			arrayint[index] = i;
		}
		this.v.setStudyRoomStatus(arrayint);
	}

	@Override
	public void setWishlist() {
		String[] array = new String[this.actualUser.getWishlist().size()];
		int index = 0;
		for (Integer i : this.actualUser.getWishlist()) {
			array[index] = this.m.getItemArchive().get(i).toString();
			index++;
		}
		this.v.setWishlist(array);
	}

	@Override
	public void removeFromWishList() {
		for (Integer i : this.m.getItemArchive().keySet()) {
			if (this.m.getItemArchive().get(i).toString().equals(this.v.getItemToRemoveFromLikeBorrowWish())) {
				try {
					this.m.removeLike(i, this.actualUser.getIdUser());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					this.v.showError("Errore! ID utente o oggetto non valido");
				}
			}
		}
	}

	@Override
	public void setAllUserList() {
		// forse sbagliato
		// manca v.setAllUserList
		int index = 0;
		String[] array = new String[this.m.getUserArchive().size()];
		for (Integer i : this.m.getUserArchive().keySet()) {
			array[index] = this.m.getUserArchive().get(i).toString();
			index++;
		}
		this.v.setUserList(array);
	}

	@Override
	public void setAllItemList() {
		// forse sbagliato
		// manca v.setAllItemList
		// manca v.getItemType
		int index = 0;
		String[] array = new String[this.m.getItemArchive().size()];
		for (Integer i : this.m.getItemArchive().keySet()) {
			array[index] = this.m.getItemArchive().get(i).toString();
			index++;
		}
		this.v.setItemList(array);
	}

	@Override
	public void deleteItem() {
		int itemIdReceived = 0;
		for (Integer i : this.m.getItemArchive().keySet()) {
			if (this.m.getItemArchive().get(i).toString().equals(this.v.getItemToRemoveModify())) {
				itemIdReceived = i;
			}
		}
		try {
			this.m.deleteItem(itemIdReceived);
			this.v.showMessage("Oggetto " + itemIdReceived + " cancellato");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.v.showError("Errore! Oggetto non presente nell'archivio");
		}
	}

	@Override
	public void deleteUser() {
		try {
			this.m.deleteUser(this.actualUser.getIdUser());
			this.v.showMessage("Utente " + this.actualUser.getIdUser() + " cancellato");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.v.showError("Errore! Utente non presente nell'archivio");
		}
	}

	@Override
	public void checkDeadlines() {
		try {
			Map<Integer, Double> map = this.m.checkDeadlineas(this.actualUser.getIdUser());
			for (Integer i : this.actualUser.getLoanArchive().keySet()) {
				// DA COMPLETARE
				if (map.get(i) > 30) {

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void extendBorrow() {
		// fai comparire solo uan finestra che dice che � stato esteso

		// se block user � true, manda la schermata solo per restituire
		this.v.showGiveBackMessage(book);
	}

	public void allItemReviews() {
		this.v.getItemSelectedByUser(); // lista di oggetti filtrati
		String[] array = new String[this.m.getAllItemReview(null).size()];
		int index = 0;
		for (ReviewImpl r : this.m.getAllItemReview(itemId)) {
			array[index] = r.toString();
			// aggiungere anche get vote?
			index++;

		}
		this.v.setItemReviewsList(array);
	}

	@Override
	public void setView(final view.View v) {
		this.v = v;
	}
}

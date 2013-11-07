/**
 * 
 */
package library;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author David Matuszek
 */
public class LibraryTest {
	private Book contact;
	private Book contact2;
	private Book equalRites;
	private Book sisters;
	private Book witches;
	private Book nightly;
	private Book time;
	private Book rings;
	private ArrayList<Book> books;
	private Library library;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		contact = new Book("Contact", "Carl Sagan");
		equalRites = new Book("Equal Rites", "Terry Pratchett");
		sisters = new Book("Weird Sisters", "Terry Pratchett");
		witches = new Book("Witches Abroad", "Terry Pratchett");
		nightly = new Book("Disappearing Nightly", "Laura Resnick");
		contact2 = new Book("Contact", "Carl Sagan");
		time = new Book("Nick of Time", "Ted Bell");
		rings = new Book("Lord of the rings", "J. R. R. Tolkien");
		books = new ArrayList<Book>();
		books.add(contact);
		books.add(witches);
		books.add(sisters);
		books.add(equalRites);
		books.add(nightly);
		books.add(contact2);
		books.add(time);
		books.add(rings);
		library = new Library(books);
	}

	private Patron openAndServeDave() {
		library.open();
		Patron dave = library.issueCard("Dave");
		library.serve("Dave");
		return dave;
	}
	
	/**
	 * Test method for {@link library.Library#open()}.
	 */
	@Test
	public void testOpen() {
		// The open() method doesn't make any change that we can
		// readily test, but we can at least make sure it returns
		// an ArrayList<Book>.
		assertEquals(new ArrayList<OverdueNotice>(), library.open());
	}
	
	/**
	 * Test method to ensure that calendar changes date 
	 */
	@Test
	public void testOpenAdvanceDay() {
		int today = library.getCalendar().getDate();
		library.open();
		assertEquals(today + 1, library.getCalendar().getDate());
	}
	

	/**
	 * Test method for {@link library.Library#createOverdueNotices()}.
	 * Also tests isOverdue
	 */
	@Test
	public void testCreateOverdueNotices() {
		ArrayList<OverdueNotice> notices;

		openAndServeDave();
		ArrayList<Book> foundBooks = library.search("Equal Rites");
		assertEquals(equalRites, foundBooks.get(0));
		Book book = foundBooks.get(0);
		library.checkOut(1);
		int dueDate = 7;
		// Don't send an overdue notice during the next seven days
		for (int i = 0; i < dueDate; i++) {
			library.close();
			notices = library.open();
			assertTrue(notices.isEmpty());
		}
		library.close();
		// Send a notice on the 8th day
		notices = library.open();
		int yesterday = library.getCalendar().getDate() - 1;
		assertTrue(library.isOverdue(book, yesterday)); // Book is overdue yesterday
		assertFalse(notices.isEmpty());
		library.close();
		// Don't send another notice after that
		notices = library.open();
		assertTrue(notices.isEmpty());
		yesterday = library.getCalendar().getDate() - 1;
		assertFalse(library.isOverdue(book, yesterday)); // Book is no longer considered 'overdue'

	}

	/**
	 * Test method for {@link library.Library#issueCard(java.lang.String)}.
	 */
	@Test
	public void testIssueCard() {
		library.open();
		Patron dave = library.issueCard("Dave");
		assertTrue(dave instanceof Patron);
	}

	/**
	 * Test method for {@link library.Library#issueCard(java.lang.String)}.
	 */
	@Test(expected=RuntimeException.class)
	public void testIssueCardToNobody() {
		library.open();
		Patron dave = library.issueCard("");
	}

	/**
	 * Test method for {@link library.Library#issueCard(java.lang.String)}.
	 */
	@Test(expected=RuntimeException.class)
	public void testIssueCardWhenLibraryIsNotOpen() {
		Patron dave = library.issueCard("Dave");
	}

	/**
	 * Test method for {@link library.Library#issueCard(java.lang.String)}.
	 */
	@Test(expected=RuntimeException.class)
	public void testIssueCardWhenPatronAlreadyHasOne() {
		library.issueCard("Dave");
		library.issueCard("Dave");
	}

	/**
	 * Test method for {@link library.Library#serve(java.lang.String)}.
	 */
	@Test
	public void testServe() {
		// We can test if the correct Patron is returned, but not if
		// it's being saved. The tests for checkIn and checkOut can
		// determine this.
		library.open();
		Patron dave = library.issueCard("Dave");
		Patron paula = library.issueCard("Paula");
		assertEquals(dave, library.serve("Dave"));
		assertEquals(dave, library.getCurrentPatron()); 
	}
	
	@Test
	public void testServeCorrectPatron() {
		// Tests to ensure that currentPatron is set to the most recent patron being served
		library.open();
		Patron dave = library.issueCard("Dave");
		Patron paula = library.issueCard("Paula");
		assertTrue(library.getCurrentPatron() == null); // no-one served yet 
		assertEquals(dave, library.serve("Dave"));
		assertEquals(dave, library.getCurrentPatron()); // dave is being served
		assertEquals(paula, library.serve("Paula"));
		assertEquals(paula, library.getCurrentPatron()); // paula is being served
		library.close();
		assertTrue(library.getCurrentPatron() == null); // no-one served 
	}

	/**
	 * Test method for {@link library.Library#serve(java.lang.String)}.
	 */
	@Test(expected=RuntimeException.class)
	public void testServeWhenLibraryIsNotOpen() {
		library.open();
		Patron dave = library.issueCard("Dave");
		library.close();
		library.serve("Dave");
	}

	/**
	 * Test method for {@link library.Library#serve(java.lang.String)}.
	 */
	@Test(expected=RuntimeException.class)
	public void testServeWhenPatronHasNoLibraryCard() {
		library.open();
		Patron dave = library.issueCard("Dave");
		library.serve("Paula");
	}

	/**
	 * Test method for {@link library.Library#serve(java.lang.String)}.
	 */
	@Test(expected=RuntimeException.class)
	public void testServePatronFromYesterday() {
		Patron dave = openAndServeDave();
		library.close();
		library.open();
		// Dave shouldn't still be here!
		ArrayList<Book> foundBooks = library.search("Disappearing Nightly");
		library.checkOut(1);
	}

	/**
	 * Test method for {@link library.Library#checkOut(int[])}.
	 */
	@Test
	public void testCheckOutOneBook() {
		Patron dave = openAndServeDave();
		library.search("Time");
		ArrayList<Book> booksCheckedOut = library.checkOut(1);
		assertTrue(dave.getBooks().contains(time));
		assertEquals(dave.getBooks(), booksCheckedOut);
		// Book shouldn't still be in library
		ArrayList<Book> booksFound = library.search("Time");
		assertTrue(booksFound.isEmpty());
	}

	/**
	 * Test method for {@link library.Library#checkOut(int[])}.
	 */
	@Test
	public void testCheckOutOneOfMultipleCopies() {
		Patron dave = openAndServeDave();
		ArrayList<Book> booksFound = library.search("Carl Sagan");
		library.checkOut(1);
		// There should still be another copy in the library
		booksFound = library.search("Carl Sagan");
		assertEquals(1, booksFound.size());
	}

	/**
	 * Test method for {@link library.Library#checkOut(int[])}.
	 */
	@Test(expected=RuntimeException.class)
	public void testCheckOutWhenNotServingAnyone() {
		library.open();
		library.search("Time");
		library.checkOut(1);
	}

	/**
	 * Test method for {@link library.Library#checkOut(int[])}.
	 */
	@Test(expected=RuntimeException.class)
	public void testCheckOutBookFromYesterdaysSearch() {
		Patron dave = openAndServeDave();
		library.open();
		library.search("Time");
		library.close();
		openAndServeDave();
		library.checkOut(1);
	}

	/**
	 * Test method for {@link library.Library#checkOut(int[])}.
	 */
	@Test(expected=RuntimeException.class)
	public void testCheckOutWhenLibraryIsNotOpen() {
		library.checkOut();
	}

	/**
	 * Test method for {@link library.Library#checkOut(int[])}.
	 */
	@Test(expected=RuntimeException.class)
	public void testCheckOutWhenNoSearchHasBeenDone() {
		Patron dave = openAndServeDave();
		library.checkOut(1);
	}

	/**
	 * Test method for {@link library.Library#checkOut(int[])}.
	 */
	@Test(expected=RuntimeException.class)
	public void testCheckOutSameBookTwice() {
		Patron dave = openAndServeDave();
		library.search("Time");
		library.checkOut(1);
		library.checkOut(1);
	}

	/**
	 * Test method for {@link library.Library#checkOut(int[])}.
	 */
	@Test(expected=RuntimeException.class)
	public void testCheckOutTooManyBooks() {
		Patron dave = openAndServeDave();
		openAndGiveBooksToDave();
		library.search("Time");
		library.checkOut(1);

	}

	/**
	 * Test method for {@link library.Library#checkOut(int[])}.
	 */
	@Test
	public void testCheckOutBooksInRandomOrder() {
		Patron dave = openAndServeDave();
		library.search("Terry Pratchett");
		library.checkOut(1);
		library.checkOut(3);
		library.checkOut(2);
		ArrayList<Book> davesBooks = dave.getBooks();
		assertTrue(davesBooks.contains(witches));
		assertTrue(davesBooks.contains(sisters));
		assertTrue(davesBooks.contains(equalRites));
	}

	/**
	 * Test method for {@link library.Library#checkIn(int[])}.
	 */
	@Test
	public void testCheckInOneBook() {
		Patron dave = openAndServeDave();
		ArrayList<Book> foundBooks = library.search("Disappearing Nightly");
		// Checking out a book moves it from the library to the patron
		library.checkOut(1);
		assertTrue(dave.getBooks().contains(nightly));
		assertTrue(library.search("Disappearing Nightly").isEmpty());
		// Checking in a book moves it back from the patron to the library
		library.serve("Dave");
		library.checkIn(1);
		assertFalse(library.search("Disappearing Nightly").isEmpty());
	}

	/**
	 * Test method for {@link library.Library#checkIn(int[])}.
	 */
	@Test(expected=RuntimeException.class)
	public void testCheckInWhenLibraryIsNotOpen() {
		library.checkIn();
	}

	/**
	 * Test method for {@link library.Library#checkIn(int[])}.
	 */
	@Test(expected=RuntimeException.class)
	public void testCheckInWhenNotServingAnyone() {
		library.open();
		library.checkIn();
	}

	/**
	 * Test method for {@link library.Library#checkIn(int[])}.
	 */
	@Test(expected=RuntimeException.class)
	public void testCheckInSameBookTwice() {
		Patron dave = openAndServeDave();
		library.search("Terry");
		library.checkOut(1);
		library.serve("Dave");
		library.checkIn(1);
		library.checkIn(1);
	}
	

	/**
	 * Test method for {@link library.Library#checkIn(int[])}.
	 */
	@Test
	public void testCheckInManyBooks() {
		openAndGiveBooksToDave();
		Patron dave = library.serve("Dave");
		ArrayList<Book> davesBooks = dave.getBooks();
		assertEquals(3, davesBooks.size());
		Book someBook = davesBooks.get(1); // Can't be sure which book this is
		library.search(someBook.getTitle()).isEmpty();
	}

	private void openAndGiveBooksToDave() {
		Patron dave = openAndServeDave();
		library.search("Terry");
		library.checkOut(1);
		library.checkOut(2);
		library.checkOut(3);
	}

	/**
	 * Test method for {@link library.Library#search(java.lang.String)}.
	 */
	@Test
	public void testSearchTitle() {
		library.open();
		ArrayList<Book> foundBooks = library.search("appear");
		assertEquals(1, foundBooks.size());
		assertEquals("Disappearing Nightly", foundBooks.get(0).getTitle());
		foundBooks = library.search("xyzzy");
		assertEquals(0, foundBooks.size());
	}

	/**
	 * Test method for {@link library.Library#search(java.lang.String)}.
	 */
	@Test
	public void testSearchAuthor() {
		library.open();
		ArrayList<Book> foundBooks = library.search("Resnick");
		assertEquals(1, foundBooks.size());
		assertEquals("Laura Resnick", foundBooks.get(0).getAuthor());
	}

	/**
	 * Test method for {@link library.Library#search(java.lang.String)}.
	 */
	@Test
	public void testSearchWithMixedCase() {
		library.open();
		ArrayList<Book> foundBooks = library.search("laura");
		assertEquals(1, foundBooks.size());
		foundBooks = library.search("NIGHTLY");
		assertEquals(1, foundBooks.size());
		foundBooks = library.search("Nick");
		assertEquals(2, foundBooks.size());
	}

	/**
	 * Test method for {@link library.Library#search(java.lang.String)}.
	 */
	@Test
	public void testSearchAndIgnoreDuplicates() {
		library.open();
		ArrayList<Book> foundBooks = library.search("Contact");
		assertEquals(1, foundBooks.size());
	}

	/**
	 * Test method for {@link library.Library#search(java.lang.String)}.
	 */
	@Test(expected=RuntimeException.class)
	public void testSearchWhenLibraryIsNotOpen() {
		library.search("tact");
	}

	/**
	 * Test method for {@link library.Library#search(java.lang.String)}.
	 */
	@Test(expected=RuntimeException.class)
	public void testSearchWhenStringIsTooShort() {
		library.open();
		library.search("tac");
	}

	/**
	 * Test method for {@link library.Library#close()}.
	 */
	@Test(expected=RuntimeException.class)
	public void testClose() {
		library.open();
		library.close();
		library.issueCard("Steve"); // Illegal when library is closed
	}
	
	/*
	 * unit test for close()
	 */
	@Test
	public void testCloseAndClearsSearchedBookListAndCurrentPatron(){
		library.open();
		openAndServeDave();
		library.search("Equal Rites");
		assertTrue(library.getSearchedBooks() != null);
		assertTrue(library.getCurrentPatron() != null);
		library.close();
		assertTrue(library.getSearchedBooks() == null);
		assertTrue(library.getCurrentPatron() == null);
	}
	

	/***************
	 * Additional tests
	 * *****************
	 */

	/**
	 * Test method for isOverdue()
	 */
	@Test
	public void testisOverdue(){
		assertFalse(library.isOverdue(contact, 0));
		assertTrue(library.isOverdue(contact, -1));
	}

	/**
	 * Test method for inputErrorMessage()
	 */
	@Test
	public void testinputErrorMessage(){
		assertEquals("'#$' is not a valid input. Please re-enter.", library.inputErrorMessage("#$"));        
	}

	/**
	 * Test method for bookNumbersToIndex()
	 */
	@Test
	public void testbookNumbersToIndex(){
		int[] intList = {3, 5, 1};
		int[] newintList = library.bookNumbersToIndex(intList);
		assertEquals(2, newintList[0]);        
		assertEquals(4, newintList[1]);        
		assertEquals(0, newintList[2]);        
	}

	/**
	 * Test method for reverseSortList()
	 */
	@Test
	public void testreverseSortList(){
		int[] intList = {3, 5, 1};
		assertEquals(5, library.reverseSortList(intList)[0]);        
		assertEquals(3, library.reverseSortList(intList)[1]);        
		assertEquals(1, library.reverseSortList(intList)[2]);        
	}

	/**
	 * Test method for checkIn()
	 */
	@Test
	public void testcheckIn(){
		library.open();
		Patron dave = library.issueCard("Dave");
		dave.take(contact);
		dave.take(contact2);
		library.serve("Dave");
		ArrayList<Book> returnedBooks = new ArrayList<Book>();
		returnedBooks.add(contact);
		returnedBooks.add(contact2);
		assertEquals(returnedBooks, library.checkIn(1, 2));

	}

	/**
	 * Test method for checkIn (when patron doesn't have any books)
	 */
	@Test(expected=RuntimeException.class)
	public void testcheckInWhenPatronHasNoBooks() {
		library.open();
		Patron dave = library.issueCard("Dave");
		library.serve("Dave");
		library.checkIn(1);
	}

	/**
	 * Test method for checkIn (when input is out of range)
	 */
	@Test(expected=RuntimeException.class)
	public void testcheckInWhenInputIsOutOfRange() {
		library.open();
		Patron dave = library.issueCard("Dave");
		dave.take(contact);
		dave.take(contact2);
		library.serve("Dave");
		library.checkIn(3);
	}

	/**
	 * Test method for isInt
	 */
	@Test 
	public void testisInt() {
		assertTrue(library.isInt("3"));
		assertTrue(library.isInt("-3"));
		assertTrue(library.isInt("0"));
		assertFalse(library.isInt("0.0"));
		assertFalse(library.isInt("a"));
		assertFalse(library.isInt("0a"));
	}



	/**
	 * Test method for testisWithinRange
	 */        
	@Test 
	public void testisWithinRange() {
		int[] array1 = new int[3];
		array1[0] = 3;
		array1[1] = 7;
		array1[2] = 5;
		int[] array2 = new int[2];
		array2[0] = 0;
		array2[1] = 7;
		assertFalse(library.isWithinRange(array1, 5));
		assertTrue(library.isWithinRange(array1, 7));
		assertTrue(library.isWithinRange(array1, 8));
		assertFalse(library.isWithinRange(array2, 6));
	}

	/**
	 * Test method for parseToInt
	 */
	@Test
	public void testParseToInt() {
		assertEquals(1, library.parseToInt("1,2,3")[0]);
		assertEquals(2, library.parseToInt("1,2,3")[1]);
		assertEquals(3, library.parseToInt("1,2,3")[2]);
		assertEquals(2, library.parseToInt("2")[0]);
		assertEquals(2, library.parseToInt(" 2 ")[0]);
		assertEquals(1, library.parseToInt("1, 2, 4, 1")[0]);
		assertEquals(2, library.parseToInt("1, 2, 4, 1")[1]);
		assertEquals(4, library.parseToInt("1,2, 4, 1")[2]);
		assertEquals(1, library.parseToInt("1,2, 4, 1")[3]);
	}

	@Test(expected=NumberFormatException.class)
	public void testParseToIntWithNonNumber() {
		library.parseToInt("a");
	}
	
	
	/**
	 * Test method for isRepeatInputs
	 */
	@Test
	public void testisRepeatInputs() {
		int[] array1 = new int[3];
		array1[0] = 3;
		array1[1] = 5;
		array1[2] = 3;
		int[] array2 = new int[2];
		array2[0] = 1;
		array2[1] = 7;
		int[] array3 = new int[3];
		array3[0] = 5;
		array3[1] = 7;
		array3[2] = 7;
		assertTrue(library.isRepeatInputs(array1));
		assertFalse(library.isRepeatInputs(array2));
		assertTrue(library.isRepeatInputs(array3));
	}


	/**
	 * Test method for checkoutInputValid
	 */
	@Test
	public void testCheckInOutInputValid(){	
		Patron dave = openAndServeDave();
		assertTrue(library.checkInOutInputValid("2,3,1", 3, "out"));
		assertTrue(library.checkInOutInputValid("2,3,", 3, "out"));
		assertTrue(library.checkInOutInputValid(" 2,3 ", 3, "out")); 	
		assertTrue(library.checkInOutInputValid(" 2,1 ", 2, "out"));    	
		assertTrue(library.checkInOutInputValid("1", 2, "out"));    	
	}

	@Test(expected=RuntimeException.class)
	public void testCheckInOutInputValidOutOfRange(){
		library.checkInOutInputValid("2, 2", 3, "out");
		library.checkInOutInputValid(" 2,3 ", 2, "out");
		library.checkInOutInputValid("abba,1", 2, "out");
	}
	
	@Test
	public void testCheckInInputValidWithFullBookWithdrawal(){
		openAndGiveBooksToDave();
		assertTrue(library.checkInOutInputValid("1,2", 3, "in"));		
	}
	
	@Test(expected=RuntimeException.class)
	public void testCheckOutInputValidWithFullBookWithdrawal(){
		openAndGiveBooksToDave();
		assertTrue(library.checkInOutInputValid("1,2", 3, "out"));		
	}


	/**
	 * unit tests for toCapitalise
	 */
	@Test
	public void testtoCapitalise(){
		assertEquals("Robert", library.toCapitalise("robert"));
		assertEquals("Robert", library.toCapitalise("ROBERT"));
		assertEquals("Robert", library.toCapitalise("Robert"));
		assertEquals("R", library.toCapitalise("r"));
		assertEquals("Jo", library.toCapitalise("jo"));
	}

	/**
	 * unit test isFewerThanThreeBooks(inputIntegers)
	 */
	@Test
	public void testisFewerThanThreeBooks(){
		int[] inputIntegers = new int[2];
		inputIntegers[0] = 1;
		inputIntegers[1] = 2;
		Patron dave = openAndServeDave();
		dave.take(contact);
		assertFalse(library.isFewerThanThreeBooks(inputIntegers));
		dave.take(witches);
		assertTrue(library.isFewerThanThreeBooks(inputIntegers));
	}
	


}
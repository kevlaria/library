//Taorui Cui, Kevin Lee

package library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Library {

	private boolean okToPrint = false;
	private ArrayList<Book> books;
	private HashMap<String, Patron> patronList;
	private boolean isOpen = false;
	private Calendar calendar = new Calendar();
	private String closedLibraryMessage = "\n What are you thinking? The library's closed. Are you trying to hack the system? Come back when the library's open!";
	private Patron currentPatron = null;
	private HashMap<Integer, Book> searchedBooks;

	public static void main(String[] args){
		Library library = new Library();
		library.start();
	}
	

	/**
	 * Library constructor (for 1-time initiation only)
	 */
	public Library(){
		this.okToPrint = true;
		this.books = this.readBookCollection();
		this.patronList = new HashMap<String, Patron>();
	}


	/**
	 * Library constructor (for unit-tests - doesn't print)
	 * @param list of books (ArrayList<Book>)
	 */
	public Library(ArrayList<Book> collection){
		this.okToPrint = false;
		this.patronList = new HashMap<String, Patron>();
		this.books = collection;
	}


	/**
	 * Library starter
	 */
	public void start(){
		while (true){
			String input = this.mainMenu();
			this.implementMenuChoice(input);
		}
	}


	/**
	 * Based on menu choice, inputs function
	 * @param input
	 */
	public void implementMenuChoice(String input){
		char letter = input.charAt(0);
		switch(letter){
		case 'o':
			this.open();
			break;
		case 'q':
			this.quit();
			break;
		case 'i':
			this.patronInteractionMenu("issueCard");
			break;
		case 's':
			this.patronInteractionMenu("serve"); 
			break;
		case 'a':
			this.searchMenu(); 
			break;
		case 'c':
			this.close();
			break;
		case 'n':
			this.checkInOutMenu("in");
			break;
		case 'd':
			this.println("Today's date is " + this.calendar.getDate() + ".");
			break;
		}
	}


	/**
	 * Prints the start menu for the user
	 */
	public String mainMenu(){
		String input = "";
		this.println("\nMain Menu \nToday is day " + this.calendar.getDate() + "\n\nWhat would you like to do?");
		if (!isOpen){                // Library is closed
			this.println("(The library is currently closed.)\n");
			input = this.closedLibraryMenuInput();
			return input;
		}
		else {
			if (this.currentPatron == null){ // Library is open but no patron is being served
				this.println("(The library is open, but is not serving anyone.)\n");
				input = this.openMenuWithNoPatron();
			} else { // Library is open and patron is being served
				this.println("(The library is open, and is currently serving " + this.currentPatron.getName() + ".)\n");
				input = this.openMenuWithCurrentPatron(); 
			}
		}

		return input;
	}


	/**
	 * - Prints out menu list if library is open but with no patron.
	 * - Asks for user input
	 * - Validates input
	 * - Returns user input as a String
	 * @return user input (String)
	 */
	public String openMenuWithNoPatron() {
		String input = "";
		boolean valid = true;
		while (valid) {
			this.println("1. Issue card.");
			this.println("2. Serve patron.");
			this.println("3. Search.");
			this.println("4. Close library.");
			this.println("5. Get today's date.");
			this.println("6. Quit.");
			this.print("Please enter 1, 2, 3, 4, 5 or 6: ");
			Scanner scanner = new Scanner(System.in);
			input = scanner.nextLine().trim();
			if (input.equals("1") || input.equals("2") || input.equals("6") ||
					input.equals("3") || input.equals("4") || input.equals("5")) {
				valid = false;
			}
			else {
				this.println(inputErrorMessage(input));
			}
		}
		if (input.equals("1")) {
			return "i";
		}
		else if (input.equals("2")) {
			return "s";
		}
		else if (input.equals("3")) {
			return "a";
		}
		else if (input.equals("4")) {
			return "c";
		}
		else if (input.equals("5")){
			return "d";
		}
		else {
			return "q";
		}
	}


	/**
	 * - Prints out menu list if library is open but with no patron.
	 * - Asks for user input
	 * - Validates input
	 * - Returns user input as a String
	 * @return user input (String)
	 */
	public String openMenuWithCurrentPatron() {
		String input = "";
		boolean valid = true;
		while (valid) {
			this.println("1. Issue card.");
			this.println("2. Serve patron.");
			this.println("3. Check in books.");
			this.println("4. Search books.");
			this.println("5. Close library.");
			this.println("6. Get today's date.");
			this.println("7. Quit.");        		
			this.print("Please enter 1, 2, 3, 4, 5, 6 or 7: ");
			Scanner scanner = new Scanner(System.in);
			input = scanner.nextLine().trim();
			if (input.equals("1") || input.equals("2") || 
					input.equals("3") || input.equals("4") ||
					input.equals("5") || input.equals("6") ||
					input.equals("7")) {
				valid = false;
			}
			else {
				this.println(inputErrorMessage(input));
			}
		}
		if (input.equals("1")) {
			return "i";
		}
		else if (input.equals("2")) {
			return "s";
		}
		else if (input.equals("3")) {
			return "n";
		}
		else if (input.equals("4")) {
			return "a";
		}
		else if (input.equals("5")) {
			return "c";
		}
		else if (input.equals("6")){
			return "d";
		}
		else {
			return "q";
		}
	}


	/**
	 * - Prints out menu list if library is closed
	 * - Asks for user input
	 * - Validates input
	 * - Returns user input as a String
	 * @return user input (String)
	 */
	public String closedLibraryMenuInput(){
		String input = "";
		boolean valid = false;
		while (!valid){
			this.println("1. Open for business.");
			this.println("2. Quit.");
			this.print("Please enter 1 or 2: ");
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNext()){
				String unparsedText = scanner.next();
				if (unparsedText.equals("1") || unparsedText.equals("2")){
					if (unparsedText.equals("1")) {input = "o";}
					else if (unparsedText.equals("2")) {input = "q";}
					else {throw new RuntimeException("closeMenuInputError");}
					valid = true;
					return input;
				}
				else {
					this.println(inputErrorMessage(unparsedText));
				}
			}
		}
		return input;
	}


	/**
	 * Checks that input lies within the range of 1 to maxOptions (inclusive)
	 * @param input - Array of inputs
	 * @param max - max number
	 * @return true if input lies within the range of 1 to maxOptions (inclusive) (boolean)
	 */
	public boolean isWithinRange(int[] input, int max){
		int[] inputReverse = this.reverseSortList(input);
		int maxInput = inputReverse[0];
		if (maxInput < 1 || maxInput > max){
			return false;
		}
		return true;
	}


	/**
	 * check if the input (outside of slash) is an integer
	 * @param input - a user input string
	 * @return true if input can be converted into an integer, else return false
	 */
	public boolean isInt(String input){
		try{
			Integer.parseInt(input);
			return true;
		}
		catch(NumberFormatException e){
			return false;
		}                        
	}


	/**
	 * Prints message only if okToPrint is set to true (using print)
	 * @param message - the message that you want to print
	 */
	public void print(String message){
		if (this.okToPrint){
			System.out.print(message);
		}
	}


	/**
	 * Prints message only if okToPrint is set to true (using println)
	 * @param message - the message that you want to print         * 
	 */
	public void println(String message){
		if (this.okToPrint){
			System.out.println(message);
		}
	}


	/**
	 * - Starts the day (by advancing the calendar). 
	 * - Then sends overdue notices to all patrons with overdue books 
	 * (by calling the next method, and printing the results). 
	 * - Sets an instance variable to indicate that the library is now open. 
	 * Returns the list of notices that it got from calling createOverdueNotices.
	 */
	public ArrayList<OverdueNotice> open(){
		this.calendar.advance();
		isOpen = true;
		this.println("\nLibrary is now open!\n");
		ArrayList<OverdueNotice> overdueNoticeList = new ArrayList<OverdueNotice>();
		try {
			overdueNoticeList = this.createOverdueNotices();
			if (!overdueNoticeList.isEmpty()){        			
				for(OverdueNotice temp: overdueNoticeList) {
					this.println("***********Overdue Notice**************");
					this.println(temp.toString());        				
				}
			} else {
				this.println("No overdue notices were sent out today.");
			}
		}
		catch(RuntimeException exception) {
			this.println(exception.getMessage());
		}

		return overdueNoticeList;
	}


	/**
	 * Closes the library
	 */
	public void close(){
		if (!this.isOpen){
			throw new RuntimeException("You can't close a library that's already closed!");
		}
		else {                
			this.println("\n Library is now closed! Good night!");
			this.isOpen = false;
			this.currentPatron = null;
			this.searchedBooks = null;
		}
	}


	/**
	 * Quits the program
	 */
	public void quit(){
		this.println("Are you sure you want to shut down the libra... <library shut down...>");
		this.println("I'm now just a boring Java console.");
		System.exit(0);
	}


	/**
	 * Returns out an input error message
	 * @param input
	 * @return input error message
	 */
	public String inputErrorMessage(String input){
		return ("'" + input + "' is not a valid input. Please re-enter.");
	}

	/**
	 * Prompts user to input name of patron. Used for:
	 * - issueCard (String function = "issueCard")
	 * - serve (String function = "serve")
	 * @param the function that you want to execute
	 */
	public void patronInteractionMenu(String function){
		this.println("Please enter the name of your patron (enter 'r' to return to main menu): ");
		Scanner scanner = new Scanner(System.in);
		boolean valid = false;
		while (!valid){
			String input = scanner.nextLine().trim();
			if (input.equals("R") || input.equals("r")){
				return; // returns to main menu
			} 
			else if (function.equals("issueCard")) {
				try {
					input = this.toCapitalise(input);
					this.issueCard(input);
					valid = true;
				}	
				catch (RuntimeException exception){            		
					this.println(exception.getMessage());
					valid = true;
				}		
			} else if (function.equals("serve")){
				try {
					input = this.toCapitalise(input);
					this.serve(input);
					valid = true;
				}
				catch (RuntimeException exception){
					this.println(exception.getMessage());
					valid = true; // returns to main menu if input hasn't been issued a card
				}
			}
		}
	}


	/**
	 * Issues a library card to the person with the name. NB no two people can have the same name
	 * @param - name of person
	 * @return - if successful, creates a non-null Patron object. If not, raises a RuntimeException
	 */
	public Patron issueCard(String nameOfPatron){
		Patron newPatron = null;
		if(isOpen){
			if(nameOfPatron == ""){
				throw new RuntimeException(this.inputErrorMessage(nameOfPatron));
			}
			else if(!patronList.containsKey(nameOfPatron)){
				newPatron = new Patron(nameOfPatron, this);
				patronList.put(nameOfPatron, newPatron);
				this.println("\n Library card issued to " + nameOfPatron);
			} else {
				throw new RuntimeException("\n" + nameOfPatron + " already exists in the database. Please enter an alternative name.");
			}        
		} else {
			throw new RuntimeException(closedLibraryMessage);
		}        
		return newPatron;
	}


	/**
	 * Begin checking books out to (or in from) the named patron.
	 * Prints out the list of books patron has borrowed.
	 * @param nameOfPatron
	 * @return
	 */
	public Patron serve(String nameOfPatron){
		Patron servedPatron = null;
		if(isOpen){
			if(this.patronList.containsKey(nameOfPatron)){
				servedPatron = this.patronList.get(nameOfPatron);
				this.println("\nReady to serve " + nameOfPatron + ".");
				currentPatron = servedPatron; // set instance variable currentPatron to servedPatron
				this.println("\n" + currentPatron.getName() + " has borrowed the following books:");
				this.printBooks(this.currentPatron.getBooks());
			}
			else{
				throw new RuntimeException("Sorry, " + nameOfPatron + " hasn't been issued a card at this library. "
						+ "Please issue " + nameOfPatron + " a card first and try again.");
			}
		} else {
			throw new RuntimeException(closedLibraryMessage);
		}
		return servedPatron;
	}


	/**
	 * Prints out a list of all the books of an ArrayList of books
	 */
	public void printBooks(ArrayList<Book> books){
		if (!books.isEmpty()){
			Iterator<Book> bookIt = books.iterator();
			int itemNumber = 1; // initiates at 1 - this is the index of the book in the ArrayList + 1
			while (bookIt.hasNext()){
				Book book = bookIt.next();
				this.println(Integer.toString(itemNumber) + ": " + book.toString());
				itemNumber ++;
			}
		} else {
			this.println("The book list is empty.");
		}
	}


	/**
	 * The listed books are being returned by the current Patron (there must be one!), 
	 * so return them to the collection and remove them from the list of books currently checked out to the patron. 
	 * Also resets dueDate to -1
	 * Assumes that there are no repeated integers / invalid integers (error checked prior to this method call)
	 * @param bookNumbers
	 * @return
	 */
	public ArrayList<Book> checkIn(int... bookNumbers){        
		ArrayList<Book> checkedInBooks = new ArrayList<Book>();
		if (!isOpen){ // first check if the library is open or not
			throw new RuntimeException(closedLibraryMessage);
		}
		if (currentPatron == null){         // then check if there is a current Patron
			throw new RuntimeException("Patron hasn't been served yet - please serve your patron first!");
		}
		if (bookNumbers == null) {
			this.println("return to the main menu.");
		}
		else {
			ArrayList<Book> patronBooks = currentPatron.getBooks();
			bookNumbers = this.bookNumbersToIndex(bookNumbers);
			bookNumbers = this.reverseSortList(bookNumbers);
			for (int bookNumber : bookNumbers){        
				try {
					Book returnBook = patronBooks.get(bookNumber);                
					currentPatron.giveBack(returnBook);
					returnBook.checkIn();
					this.books.add(returnBook);
					checkedInBooks.add(returnBook);
					this.println("Successfully returned: " + returnBook.toString());
				}
				catch (IndexOutOfBoundsException exception){
					throw new RuntimeException(this.inputErrorMessage(Integer.toString(bookNumber)));
				}
			}
		}
		return checkedInBooks;
	}


	/**
	 * Takes in a string and turns it first letter into capitalized.
	 * @param input
	 * @return
	 */
	public String toCapitalise(String input){
		String newString = "";
		if (input.equals("")){
			throw new RuntimeException(this.inputErrorMessage(input));
		} else if (input.length() == 1){
			newString = input.toUpperCase();
			return newString;
		} else {
			input = input.toLowerCase();
			Character firstLetter = input.charAt(0);
			newString = firstLetter.toString().toUpperCase() + input.substring(1);
			return newString; 		
		}
	}


	/**
	 * Takes an array of integer inputs, and converts them into an array of index numbers
	 * (by subtracting 1)
	 * @param intList
	 * @return array of index numbers (int[])
	 */
	public int[] bookNumbersToIndex(int[] intList){
		int intListLength = intList.length;
		for (int i = 0; i < intListLength; i ++){
			int temp = intList[i];
			temp = temp - 1;
			intList[i] = temp;
		}
		return intList;
	}


	/**
	 * Reverse sorts an Array of integers
	 * @param intList - Array of integer
	 * @return list of integers sorted in reverse order (int[])
	 */
	public int[] reverseSortList(int[] intList){
		Arrays.sort(intList);
		int intListLength = intList.length;
		int[] reverseList = new int[intListLength];
		for (int i = 0; i < intListLength; i++){
			reverseList[intListLength - 1 -i] = intList[i];
		}        
		return reverseList;
	}


	/**
	 * Checks each patron to see if he/she has books due yesterday
	 * Creates and returns a list of overdue notices for patrons with overdue notices
	 * @return overdue notices (ArrayList<OverdueNotice>)
	 */
	public ArrayList<OverdueNotice> createOverdueNotices(){
		ArrayList<OverdueNotice> overdueNoticeList = new ArrayList<OverdueNotice>();
		if (!this.patronList.isEmpty()){
			Iterator<String> it = this.patronList.keySet().iterator();
			while(it.hasNext()){
				String patronName = it.next();
				Patron patron = this.patronList.get(patronName);
				OverdueNotice overdueNotice = createSinglePatronOverdueNotice(patron);
				if (overdueNotice != null){
					overdueNoticeList.add(overdueNotice);
				}
			} 
		} else {
			throw new RuntimeException("No overdue notices were sent out today.");
		}
		return overdueNoticeList;
	}


	/**
	 * Check a book list of a patron if there is a book overdue.
	 * Return a overdue notice.
	 * @param patron
	 * @return
	 */
	public OverdueNotice createSinglePatronOverdueNotice(Patron patron){
		ArrayList<Book> patronBooks = patron.getBooks();
		Iterator<Book> bookIt = patronBooks.iterator();
		int yesterday = calendar.getDate() - 1;
		OverdueNotice overdueNotice = null;
		while(bookIt.hasNext()){
			Book book = bookIt.next();
			if (isOverdue(book, yesterday)){
				overdueNotice = new OverdueNotice(patron, yesterday);
			}
		}
		return overdueNotice;
	}


	/**
	 * Check if a book is overdue. Only returns true on the due date itself.
	 * @param book
	 * @param date
	 * @return
	 */
	boolean isOverdue(Book book, int date){
		if (book.getDueDate() == date){
			return true;
		}
		return false;
	}


	/**
	 * Loads in data from books.txt
	 * @return a list of books (ArrayList<Book>)
	 */
	private ArrayList<Book> readBookCollection(){
		File file = new File("src/library/books.txt");
		ArrayList<Book> collection = new ArrayList<Book>();
		try{
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);

			while (true){
				String line = reader.readLine();
				if (line == null){
					break;
				}
				line = line.trim();
				if (line.equals("")){
					continue; // ignore possible blank lines
				}
				String[] bookInfo = line.split(" :: ");
				collection.add(new Book(bookInfo[0], bookInfo[1]));
			}
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		return collection;

	}

	/**
	 * Prints out, and saves in an instance variable, 
	 * a list of books whose title or author contains the key word.
	 * @param part
	 * @return
	 */
	public ArrayList<Book> search(String part) {
		this.searchedBooks = new HashMap<Integer, Book>();
		ArrayList<Book> result = new ArrayList<Book>();
		int  i = 0;
		if (part.length() >= 4) {
			if (isOpen) {
				for (Book temp: this.books) {                        
					if (temp.getAuthor().toLowerCase().contains(part.toLowerCase()) |
							temp.getTitle().toLowerCase().contains(part.toLowerCase())) {                                   	
						if(!result.contains(temp)) {
							i++;
							println(i + ". " + temp.toString() + ".");                                                
							this.searchedBooks.put(i, temp);
							result.add(temp);
						}
					}
				}
				if (this.searchedBooks.size() == 0) {
					println("Sorry, there were no results.");
				}
				return result;
			}
			else {
				throw new RuntimeException(closedLibraryMessage);
			}
		}
		else {
			throw new RuntimeException("The search term should be at least 4 characters long.");                
		}
	}


	/**
	 * ask user to input keywords for searching books.
	 * check the input validation and return the input as string.
	 * @return
	 */
	public String searchInput() {
		String input = "";
		this.print("\nPlease enter your search term (author name or book title, must be at least 4 characters long).");
		Scanner scanner = new Scanner(System.in);
		input = scanner.nextLine().trim();
		if (input.length() < 4) {
			this.println("The search term should be at least 4 characters long.");
			return searchInput();
		}
		else {
			return input;
		}
	}


	/**
	 * implement the menu of search.
	 */
	public void searchMenu() {        	
		String input = "";
		boolean valid = false;
		if (currentPatron != null) {
			if (this.search(this.searchInput()).size() != 0) {
				while(!valid) {
					this.println("\nYou can:");
					this.println("1. Check out books.");
					this.println("2. Return to the main menu.");
					this.print("Please enter 1 or 2: ");
					Scanner scanner = new Scanner(System.in);
					input = scanner.nextLine().trim();
					if (input.equals("1") || input.equals("2")) {
						valid = true;
					}
					else {
						this.println(inputErrorMessage(input));
					}
				}
			}
			if (input.equals("1")) {      		
				this.checkInOutMenu("out");
			}
		}
		else {
			this.search(this.searchInput());
		}
	}


	/**
	 * Either checks out the book to the Patron currently being served, 
	 * or tells why the operation is not permitted. 
	 * Checking out a Book will involve both telling the Book that it is checked out 
	 * and removing the Book from this Library's collection of available Books. 
	 * Returns a list of the books just checked out.
	 * @param bookNumbers
	 * @return
	 */
	public ArrayList<Book> checkOut(int... bookNumbers) {
		ArrayList<Book> checkedBooks = new ArrayList<Book>();
		if (!isOpen) { // first check if the library is open or not
			throw new RuntimeException(closedLibraryMessage);
		}
		if (currentPatron == null) { // then check if there is a current Patron
			throw new RuntimeException("Patron hasn't been served yet - please serve your patron first!");
		}
		if (this.searchedBooks.size() == 0) { // then check whether a search was made first
			throw new RuntimeException("Please search the books first.");            	
		}
		if (bookNumbers == null) {
		}
		else {
			for (int bookNumber: bookNumbers) {
				try { 
					if (currentPatron.getBooks().size() > 3) {
						throw new RuntimeException("You can only take out a maximum of 3 books.");
					}
					else {
						Book checkedBook = this.searchedBooks.get(bookNumber);
						this.books.remove(checkedBook);
						this.searchedBooks.remove(bookNumber);
						checkedBooks.add(checkedBook);
						checkedBook.checkOut(calendar.getDate() + 7);
						currentPatron.take(checkedBook);
						this.println("Successfully checked out: " + checkedBook.toString());
					}
				}
				catch (NullPointerException exception){
					this.println(this.inputErrorMessage(Integer.toString(bookNumber)));
					this.checkInOutMenu("out");
				}
			}
		}
		return checkedBooks;
	}


	/**
	 * implement the check-in & check-out menu.
	 * @param function - whether it's check-in ("in") or check-out ("out").
	 */
	public void checkInOutMenu(String function) {
		this.println("Below is the list of books you can check " + function + ": \n"); 
		ArrayList<Book> books = new ArrayList<Book>();
		if (function.equals("in")){
			books = this.currentPatron.getBooks();
		} else { 			// check out
			books = new ArrayList<Book>(this.searchedBooks.values());
		}
		this.printBooks(books);

		if (books.isEmpty()){
			return;			// exit sub-menu if list is empty
		} else {  	 
			if (function.equals("in")){
				this.checkIn(this.checkInOutInput(function, books)); 

			} else {
				this.checkOut(this.checkInOutInput(function, books)); 
			}
		}
	}


	/**
	 * ask user to input the index of books in the searching result
	 * to get checked out.
	 * check the validation and return a Integer
	 * @function - whether it's check-in ("in") or check-out ("out")
	 * @return
	 */
	public int[] checkInOutInput(String function, ArrayList<Book> books) {
		String input = "";
		Boolean valid = false;
		while (!valid) {
			this.println("\nPlease select the book(s) you want to check out according to the results above.");
			this.println("To select multiple books, please separate each entry with a comma (eg. 1, 2).");
			this.println("To return to the main menu, please enter 'r'."); // allow user to return to main menu
			int numBooks = books.size();
			Scanner scanner = new Scanner(System.in);
			input = scanner.nextLine();
			if (input.toLowerCase().equals("r")) {
				valid = true;
			}
			else {
				try {
					this.checkInOutInputValid(input, numBooks, function);
					valid = true;
				}
				catch (RuntimeException exception) {
					this.println(exception.getMessage());
				}
			}
		}
		return parseToInt(input);
	}


	/**
	 * check the validation of input for checkout books.
	 * @param function - whether it's check-in ("in") or check-out ("out").
	 * @param input - the input string
	 * @param numBooks - number of books either in the patron's possession 
	 * (if function = "in") or search results (if function = "out")
	 * @return
	 */
	public boolean checkInOutInputValid(String input, int numBooks, String function) {
		try {
			int[] inputIntegers = this.parseToInt(input);
			if (!this.isWithinRange(inputIntegers, numBooks)){
				throw new RuntimeException("You included a book not in the list. Please try again.");
			}
			if (this.isRepeatInputs(inputIntegers)){
				throw new RuntimeException("You can't select the same book twice. Please try again.");
			}
			
			if (function.equals("out")){
				if (this.isFewerThanThreeBooks(inputIntegers)){

					throw new RuntimeException("A patron can have no more than 3 books checked out at same time. "
							+ currentPatron.getName() + " has taken out " + currentPatron.getBooks().size() + " books. ");		
				}
				
			}
		}
		catch(NumberFormatException e) {
			throw new RuntimeException(this.inputErrorMessage(input));
		}
		return true;
	}


	/**
	 * Check if a patron wants to have more than 3 checkout book 
	 * at same time.
	 * @param input
	 * @return
	 */
	public boolean isFewerThanThreeBooks(int[] input) {
		if ((input.length + currentPatron.getBooks().size()) > 3) {
			return true;
		}
		else {
			return false;
		}
	}


	/**
	 * takes in a string and parse it into int[].
	 * @param input
	 * @return
	 */
	public int[] parseToInt(String input) {       	
		if (input.toLowerCase().equals("r")) {
			int[] temp = null;
			return temp;
		}
		else {
			input = input.replace(" ", "");
			String[] tempStr = input.split(",");
			int[] tempInt = new int[tempStr.length];
			int i = 0;
			for (String temp: tempStr) {
				temp = temp.trim();
				// if throws NumberFormatException, it will be caught in checkInOutInputValid().
				tempInt[i] = Integer.parseInt(temp); 
				i++;
			}  
			return tempInt;
		}
	}


	/**
	 * Checks an array of integers to see if any numbers are repeated.
	 * Returns true if integers are repeated
	 * @param inputs array of integers
	 * @return
	 */
	public boolean isRepeatInputs(int[] inputs){
		inputs = this.reverseSortList(inputs);
		int inputsLength = inputs.length;
		for (int i = 0; i < inputsLength - 1; i++){
			if (inputs[i] == inputs[i + 1]){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Getter for calendar
	 * @return Calendar
	 */
	public Calendar getCalendar(){
		return this.calendar;
	}
	
	/**
	 * Getter for patronList
	 * @return patron list (HashMap<String, Patron>)
	 */
	public HashMap<String, Patron> getPatronList(){
		return this.patronList;
	}
	
	/**
	 * Getter for current Patron
	 * @return Patron
	 */
	public Patron getCurrentPatron(){
		return this.currentPatron;
	}
	
	/**
	 * Getter for searchedBooks
	 * @return list of books (HashMap<Integer, Book>)
	 */
	public HashMap<Integer, Book> getSearchedBooks(){
		return this.searchedBooks;
	}
	
	/**
	 * Getter for books
	 * @return list of books (ArrayList<Book>)
	 */
	public ArrayList<Book> getBooks(){
		return this.books;
	}
	


}

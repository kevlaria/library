package library;

// testing 1123

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Library {
	
	private boolean okToPrint = false;
	private ArrayList<Book> books;
	private HashMap<String, Patron> patronList;
	private boolean isOpen = false;
	private Calendar calendar = new Calendar();
	private String closedLibraryMessage = "\n What are you thinking? The library's closed. Are you trying to hack the system? Come back when the library's open!";
	private Patron currentPatron = null;
	
	public static void main(String[] args){
		Library dummyLibrary = new Library();
		dummyLibrary.open();
		try {
		dummyLibrary.issueCard("Joe Blogs");
		} catch (RuntimeException exception){
			dummyLibrary.print(exception.getMessage());
		};
		try {
			dummyLibrary.issueCard("Joe Blogs");
		} catch (RuntimeException exception) {
			dummyLibrary.print(exception.getMessage());
		}
		dummyLibrary.open();
//		dummyLibrary.serve("Joe Blogs");
//        Book witches = new Book("Witches Abroad", "Terry Pratchett");
//        Book nightly = new Book("Disappearing Nightly", "Laura Resnick");
//		dummyLibrary.currentPatron.take(witches);
//		dummyLibrary.currentPatron.take(nightly);
//		dummyLibrary.printPatronBooks(dummyLibrary.currentPatron);
	}
	
	/*
	 * Library constructor (for 1-time initiation only)
	 */
	public Library(){
		this.okToPrint = true;
		this.books = this.readBookCollection();
		this.patronList = new HashMap<String, Patron>();
	}
	
	/*
	 * Library constructor (for unit-tests - doesn't print)
	 * @param list of books (ArrayList<Book>)
	 */
	public Library(ArrayList<Book> collection){
		this.okToPrint = false;
		this.patronList = new HashMap<String, Patron>();
	}
	
	/*
	 * Library starter
	 */
	void start(){
		// TO DO
	}
	
	/*
	 * Prints message only if okToPrint is set to true (using print)
	 * @param message - the message that you want to print
	 */
	void print(String message){
		if (this.okToPrint){
			System.out.print(message);
		}
	}
	
	/*
	 * Prints message only if okToPrint is set to true (using println)
	 * @param message - the message that you want to print	 * 
	 */
	void println(String message){
		if (this.okToPrint){
			System.out.println(message);
		}
	}
	
	/*
	 * - Starts the day (by advancing the calendar). 
	 * - Then sends overdue notices to all patrons with overdue books 
	 * (by calling the next method, and printing the results). 
	 * - Sets an instance variable to indicate that the library is now open. 
	 * Returns the list of notices that it got from calling createOverdueNotices.
	 */
	ArrayList<OverdueNotice> open(){
		this.calendar.advance();
		isOpen = true;
		this.println("\nLibrary is now open!");
		ArrayList<OverdueNotice> overdueNoticeList = new ArrayList<OverdueNotice>();
		try {
			overdueNoticeList = this.createOverdueNotices();
			if (!overdueNoticeList.isEmpty()){
				this.println(overdueNoticeList.toString());
			} else {
				this.println("No overdue notices sent out today");
			}
		}
		catch(RuntimeException exception) {
			this.println(exception.getMessage());
		}

		return overdueNoticeList;
	}
	
	/*
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
		}
	}
	
	/**
	 * Returns out an input error message
	 * @param input
	 * @return input error message
	 */
	public String inputErrorMessage(String input){
		return ("'" + input + "' is not a valid input. Please re-enter.");
	}
	
	/*
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
				this.printPatronBooks(servedPatron);
				currentPatron = servedPatron; // set instance variable currentPatron to servedPatron
			}
			else{
				throw new RuntimeException("Sorry, " + nameOfPatron + "hasn't been issued a card at this library. Please issue card first and try again.");
			}
		} else {
			throw new RuntimeException(closedLibraryMessage);
		}
		return servedPatron;
	}
	
	/**
	 * Prints out a list of all the books the patron has borrowed
	 * @param patron - the Patron object
	 */
	public void printPatronBooks(Patron patron){
		ArrayList<Book> patronBooks = patron.getBooks();
		if (!patronBooks.isEmpty()){
			Iterator<Book> bookIt = patronBooks.iterator();
			int itemNumber = 1; // initiates at 1 - this is the index of the book in the ArrayList + 1
			this.println("\n" + patron.getName() + " has borrowed the following books:");
			while (bookIt.hasNext()){
				Book book = bookIt.next();
				this.println(Integer.toString(itemNumber) + ": " + book.toString());
				itemNumber ++;
			}
		}
	}
	/**
	 * The listed books are being returned by the current Patron (there must be one!), 
	 * so return them to the collection and remove them from the list of books currently checked out to the patron. 
	 * @param bookNumbers
	 * @return
	 */
	public ArrayList<Book> checkIn(int... bookNumbers){	
		ArrayList<Book> checkedInBooks = new ArrayList<Book>();
		if (!isOpen){ // first check if the library is open or not
			throw new RuntimeException(closedLibraryMessage);
		}
		if (currentPatron == null){ 	// then check if there is a current Patron
			throw new RuntimeException("Patron hasn't been served yet - please serve your patron first!");
		}
		else {
			ArrayList<Book> patronBooks = currentPatron.getBooks();
			for (int bookNumber : bookNumbers){	
				try {
					Book returnBook = patronBooks.get(bookNumber);		
					if (returnBook == null){
						throw new RuntimeException("You've already returned that book.");
					}
					checkedInBooks.add(returnBook);
				}
				catch (IndexOutOfBoundsException exception){
					throw new RuntimeException(this.inputErrorMessage(Integer.toString(bookNumber)));
				}
			}
		}
		return checkedInBooks;
	}
	
	public 
	
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
			throw new RuntimeException("The library has no patrons!");
		}
		return overdueNoticeList;
	}
	
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
	
	boolean isOverdue(Book book, int date){
		if (book.getDueDate() >= date){
			return true;
		}
		return false;
	}

	/*
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
	 * an list of books whose title or author contains the key word.
	 * @param part
	 * @return
	 */
	public ArrayList<Book> search(String part) {
		ArrayList<Book> result = new  ArrayList<Book>();
		if (part.length() >= 4) {
		    if (isOpen) {
			    for (Book temp: this.books) {
				    if (temp.getAuthor().toLowerCase().contains(part.toLowerCase()) ||
				        temp.getTitle().toLowerCase().contains(part.toLowerCase())) {
				    	if (temp.getDueDate() == -1 && ! result.contains(temp)) {
				    		println(temp.toString());
					        result.add(temp);
				    	}
				    }
			    }
			    if (result.size() == 0) {
				    throw new RuntimeException("Sorry, no result.");
			    }
			    else {
				    return result;
			    }
		    }
		    else {
		       throw new RuntimeException(closedLibraryMessage);
		    }
	    }
		else {
			throw new RuntimeException("the key word should be at least 4 characters long.");		
		}
	}
	
	
	public ArrayList<Book> checkOut(int... bookNumbers) {
		return books; 
	}
	
} 

// hello taorui
// hello kevin
// hello taorui
// hello taorui again
// test 3wwefwe
// test 4

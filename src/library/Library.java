package library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        	Library test1 = new Library();
        	test1.start();
        	
//        	Patron a = test1.issueCard("a");
//        	test1.serve("a");
//        	test1.start();
//        	test1.searchMenu();
//        	test1.checkOutMenu();
//                Library dummyLibrary = new Library();
//                dummyLibrary.open();
//                try {
//                dummyLibrary.issueCard("Joe Blogs");
//                } catch (RuntimeException exception){
//                        dummyLibrary.print(exception.getMessage());
//                };
//                try {
//                        dummyLibrary.issueCard("Joe Blogs");
//                } catch (RuntimeException exception) {
//                        dummyLibrary.print(exception.getMessage());
//                }
//                dummyLibrary.open();
//                dummyLibrary.serve("Joe Blogs");
//        Book witches = new Book("Witches Abroad", "Terry Pratchett");
//        Book nightly = new Book("Disappearing Nightly", "Laura Resnick");
//                dummyLibrary.currentPatron.take(witches);
//                dummyLibrary.currentPatron.take(nightly);
//                dummyLibrary.printPatronBooks(dummyLibrary.currentPatron);
//                dummyLibrary.checkIn(1,2);
//                dummyLibrary.close();
//                dummyLibrary.start();
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
                this.books = collection;
        }
        
        /*
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
                        case 't':
                                this.checkInOutMenu("out"); 
                                break;
                        case 'r':
                        //        this.returnToMainMenu(); // not yet implemented
                                break;                
                }
        }
        
        /**
         * Prints the start menu for the user
         */
        public String mainMenu(){
                String input = "";
                this.println("\nWelcome! What would you like to do?");
                if (!isOpen){                // Library is closed
                		this.println("(The library is currently closed.)");
                        input = this.closedLibraryMenuInput();
                        return input;
                }
                else {
                        if (this.currentPatron == null){ // Library is open but no patron is being served
                        	this.println("(The library is open, but is not serving anyone.)");
                            input = this.openMenuWithNoPatron();
                        } else { // Library is open and patron is being served
                        	this.println("(The library is open, and is currently serving " + this.currentPatron.getName() + ".)");
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
        		this.println("5. Quit.");
        		this.print("Please enter 1, 2, 3, 4 or 5: ");
        		Scanner scanner = new Scanner(System.in);
        		input = scanner.next().trim();
        		if (input.equals("1") || input.equals("2") || 
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
        		this.println("6. Quit.");        		
        		this.print("Please enter 1, 2, 3, 4, 5 or 6: ");
        		Scanner scanner = new Scanner(System.in);
        		input = scanner.next().trim();
        		if (input.equals("1") || input.equals("2") || 
        		    input.equals("3") || input.equals("4") ||
        		    input.equals("5") || input.equals("6")) {
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
                        this.println("1. Open for business");
                        this.println("2. Quit");
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
        
        /*
         * Prints message only if okToPrint is set to true (using print)
         * @param message - the message that you want to print
         */
        public void print(String message){
                if (this.okToPrint){
                        System.out.print(message);
                }
        }
        
        /*
         * Prints message only if okToPrint is set to true (using println)
         * @param message - the message that you want to print         * 
         */
        public void println(String message){
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
        public ArrayList<OverdueNotice> open(){
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
        	this.println("Please enter the name of your patron (enter 'q' to return to main menu): ");
        	Scanner scanner = new Scanner(System.in);
        	boolean valid = false;
        	while (!valid){
               	String input = scanner.next().trim();
          		input = this.toCapitalise(input);
            	if (input.equals("Q")){
            			return; // returns to main menu
            	} else if (function.equals("issueCard")) {
            		try {
                		this.issueCard(input);
            			valid = true;
            		}	
            		catch (RuntimeException exception){            		
            			this.println(exception.getMessage());
                	}		
            	} else if (function.equals("serve")){
            		try {
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
                	this.println("There are no books in the list.");
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
                                        this.println("\nSuccessfully returned: " + returnBook.toString());
                                }
                                catch (IndexOutOfBoundsException exception){
                                        throw new RuntimeException(this.inputErrorMessage(Integer.toString(bookNumber)));
                                }
                        }
                }
                return checkedInBooks;
        }
        
        
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
            this.print("Please enter your search term (author name or book title, must be at least 4 characters long).");
            Scanner scanner = new Scanner(System.in);
            input = scanner.next().trim();
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
        	this.search(this.searchInput());
        	String input = "";
        	boolean valid = false;
        	while(!valid) {
        		this.println("You can: 1. Check out books.");
        		this.println("         2. Go back to previous page.");
        		this.print("Please enter 1 or 2: ");
        		Scanner scanner = new Scanner(System.in);
                input = scanner.next().trim();
                if (input.equals("1") || input.equals("2")) {
                	valid = true;
                }
                else {
                	this.println(inputErrorMessage(input));
                }
        	}
        	if (input.equals("1")) {
        		// TO DO - If I run a search and get no results, the program still 
        		// prompts me to check out books or go back to previous menu. It should ask me to try another search.
        		this.checkInOutMenu("out");
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
             // TO DO - allow user to return to main menu?
             int numBooks = books.size();
             Scanner scanner = new Scanner(System.in);
             input = scanner.next();
             try {
             	this.checkInOutInputValid(input, numBooks, function);
             	valid = true;
             }
             catch (RuntimeException exception) {
     	    	this.println(exception.getMessage());
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
	        			throw new RuntimeException("Input includes number not in list.");
        		}
        		if (this.isRepeatInputs(inputIntegers)){
        			throw new RuntimeException("Input includes repeated integers.");
        		}
//        		if (function.equals("out")){
//        			// TO DO this.isFewerThanThreeBooks(inputIntegers);
//        			throw new RuntimeException("");		
//        		}    		
        	}
        	catch(NumberFormatException e) {
    			throw new RuntimeException(this.inputErrorMessage(input));
        	}
        	return true;
        }
 
        
        /**
         * takes in a string and parse it into int[].
         * @param input
         * @return
         */
        public int[] parseToInt(String input) {  
        	String[] tempStr = input.trim().split(",");
        	System.out.println(tempStr.length);
        	int[] tempInt = new int[tempStr.length];
        	int i = 0;
        	for (String temp: tempStr) {
        		System.out.println(temp + " <- pre trim");
        		temp = temp.trim();
        		System.out.println(temp + " <- post trim");
        		// if throws NumberFormatException, it will be caught in checkInOutInputValid().
        		tempInt[i] = Integer.parseInt(temp); 
        		i++;
        	}  
        	System.out.print(tempInt.length);
        	return tempInt;
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
 
//      /***
//      * MAY NEED TO REMOVE
//      ***/
//        
//        /**
//         * ask user to input the index of books in the searching result
//         * to get checked out.
//         * check the validation and return a Integer
//         * @return
//         */
//        public int[] checkoutInput() {
//        	String input = "";
//        	Boolean valid = true;
//        	while (valid) {
//                this.println("Please choose which book or books you want to check out according to the searching result.");
//                this.println("To choose mutiple books at one time, please enter indexes split by the comma.");
//                Scanner scanner = new Scanner(System.in);
//                input = scanner.next();
//                if (checkoutInputValid(input)) {
//                	valid = false;
//                }
//                else {
//        	    	this.println(inputErrorMessage(input));
//        	    }
//        	}
//        	return parseToInt(input);
//        }
//  

//        
//        /**
//         * implement the menu of check out.
//         */
//        public void checkOutMenu() {
//        	boolean valid = false;
//        	while (!valid) {
//        		int[] temp = this.checkoutInput();
//        	}
//        	this.checkOut(this.checkoutInput());        	
//        }
//        
//        /***
//         * MAY NEED TO REMOVE
//         ***/
        
}
        
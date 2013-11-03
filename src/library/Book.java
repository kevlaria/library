package library;

public class Book {
	
	private String title;
	private String author;
	private int dueDate;
	
	/*
	 * Constructor for book object
	 */
	public Book(String title, String author){
		this.title = title;
		this.author = author;
		this.dueDate = -1;
	}
	
	/*
	 * Returns title of the book
	 * @return the title of the book (String)
	 */
	public String getTitle(){
		return this.title;
	}
	
	/*
	 * Returns the author of the book
	 * @return the author of the book (String)
	 */
	public String getAuthor(){
		return this.author;
	}
	
	/*
	 * Returns the due date of the book
	 * @return the due date of the book (int)
	 */
	public int getDueDate(){
		return dueDate;
	}
	
	/*
	 * sets the due date of this Book to the specified date.
	 * @param date - the new due date
	 */
	public void checkOut(int date){
		this.dueDate = date;
	}
	
	/*
	 * sets the due date back to -1 (ie book is returned)
	 */
	public void checkIn(){
		this.dueDate = -1;
	}
	
	@Override
	public String toString(){
		return this.title + " by " + this.author;
	}

}

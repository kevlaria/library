package library;

public class Book {
	
	private String title;
	private String author;
	private int dueDate;
	
	/**
	 * Constructor for book object
	 */
	public Book(String title, String author){
		this.title = title;
		this.author = author;
		this.dueDate = -1;
	}
	
	/**
	 * Returns title of the book
	 * @return the title of the book (String)
	 */
	public String getTitle(){
		return this.title;
	}
	
	/**
	 * Returns the author of the book
	 * @return the author of the book (String)
	 */
	public String getAuthor(){
		return this.author;
	}
	
	/**
	 * Returns the due date of the book
	 * @return the due date of the book (int)
	 */
	public int getDueDate(){
		return dueDate;
	}
	
	/**
	 * sets the due date of this Book to the specified date.
	 * @param date - the new due date
	 */
	public void checkOut(int date){
		this.dueDate = date;
	}
	
	/**
	 * sets the due date back to -1 (ie book is returned)
	 */
	public void checkIn(){
		this.dueDate = -1;
	}
	
	@Override
	public String toString(){
		return this.title + " by " + this.author;
	}

	/**
	 * For unit tests
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (dueDate != other.dueDate)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	

}

package library;

import java.util.ArrayList;

public class Patron {

	String name;
	Library library;
	ArrayList<Book> patronBooks;

	/**
	 * Library constructor
	 * @param name - name of patron
	 * @param library - reference to the Library object
	 */
	public Patron(String name, Library library){
		this.name = name;
		this.library = library;
		this.patronBooks = new ArrayList<Book>();
	}

	/**
	 * Returns the name of the patron
	 * @return name of the patron (String)
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * Adds this book to the list of books checked out by this Patron
	 * @param book - book object taken out
	 */
	public void take(Book book){
		patronBooks.add(book);
	}

	/**
	 * Removes Book object from the list of books checked out by the patron
	 * @param book - the book to be returned
	 */
	public void giveBack(Book book){
		patronBooks.remove(book);
	}

	/**
	 * Returns the list of Book objects checked out to this patron
	 * return list of books (ArrayList<Book>)
	 */
	public ArrayList<Book> getBooks(){
		return this.patronBooks;
	}

	/**
	 * Returns name of patron
	 */
	@Override
	public String toString(){
		return getName();
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
		Patron other = (Patron) obj;
		if (library == null) {
			if (other.library != null)
				return false;
		} else if (!library.equals(other.library))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (patronBooks == null) {
			if (other.patronBooks != null)
				return false;
		} else if (!patronBooks.equals(other.patronBooks))
			return false;
		return true;
	}



}

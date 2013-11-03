package library;

import java.util.ArrayList;

public class OverdueNotice {
	
	Patron patron;
	int todaysDate;
	
	/*
	 * Constructor for OverdueNotice object for a given patron
	 */
	public OverdueNotice(Patron patron, int todaysDate){
		this.patron = patron;
		this.todaysDate = todaysDate;
	}
	
	@Override
	public String toString() {
		ArrayList<Book> dueBooks = new ArrayList<Book>();
		String notice = "All the books checked out by the patron are: \n";
		for (Book temp: this.patron.getBooks()) {
			notice = notice + temp + "Due Date: " + temp.getDueDate() + "\n";
			if (temp.getDueDate() < this.todaysDate) {
				dueBooks.add(temp);
				notice = notice + "Please pay attention that this book is overdue!! \n";
			}
		}
	    return notice;
	}
}

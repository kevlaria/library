package library;

import java.util.ArrayList;

public class OverdueNotice {

	Patron patron;
	int todaysDate;

	/**
	 * Constructor for OverdueNotice object for a given patron
	 */
	public OverdueNotice(Patron patron, int todaysDate){
		this.patron = patron;
		this.todaysDate = todaysDate;
	}

	/**
	 * If a patron has a overdue checkout book, print out 
	 * all the books this patron currently have, alone with due date.
	 * Call attention to the overdue books.
	 */
	@Override
	public String toString() {
		ArrayList<Book> dueBooks = new ArrayList<Book>();
		String notice = "All the books checked out by " + this.patron + " are: \n";
		int i = 1;
		for (Book temp: this.patron.getBooks()) {
			notice = notice + i + ". " + temp + ". ";
			i++;
			if (temp.getDueDate() <= this.todaysDate) {
				dueBooks.add(temp);
				notice = notice + "\nThis book is overdue! " + "Due Date: " + temp.getDueDate() 
						+ ".\n                      Today's Date: " + (this.todaysDate + 1) + ".\n\n";
			}
			else {
				notice = notice + "Due Date: " + temp.getDueDate() + ".\n\n";
			}
		}
		return notice;
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
		OverdueNotice other = (OverdueNotice) obj;
		if (patron == null) {
			if (other.patron != null)
				return false;
		} else if (!patron.equals(other.patron))
			return false;
		if (todaysDate != other.todaysDate)
			return false;
		return true;
	}


}

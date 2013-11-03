package library;

public class Calendar {

	private int date;
	
	/*
	 * Constructor for Calendar object. Sets date as 0.
	 */
	public Calendar(){
		this.date = 0;
	}
	
	/*
	 * Returns the current date
	 * @return current date (int)
	 */
	public int getDate(){
		return this.date;
	}
	
	/*
	 * Increment the date by 1
	 */
	public void advance(){
		this.date = this.date + 1;
	}
}

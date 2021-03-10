package Entities;/*
  CMPT 270 A5
  @author Blake Stadnyk; 11195866 - BJS645
 */

import java.sql.Date;

/**
 * A Manager who extends from a Employee.
 */
public class Manager extends Employee {

  /**
   * Instantiates a new Manager with the given name.
   *
   * @param name the name
   */
  public Manager(String name, String number, String email, Date date, int stallNumber) {
    super(name, number, email, date, stallNumber);
  }

  /**
   * Get a string representation of the Manager
   * Useful for printing
   *
   * @return a string representation of the Manager
   */
  public String toString() {
    return "\nManager:\n" + super.toString();
  }

  /**
   * A method to test the class.
   *
   * @param args not used
   */
  public static void main(String[] args) {
    // Create a new Manager
    Employee d = new Manager("Bob", "123", null,null,0);

    // Testing constructor and getName() //
    if (! d.getName().equals("Bob")) {
      System.out.println("Problem with constructor or getName");
    }

    // Testing setName //
    d.setName("Joe");
    if (! d.getName().equals("Joe")) {
      System.out.println("Problem with setName");
    }

    // Testing addPassenger and hasPassenger
    Passenger p = new Passenger("Mike", "123", null, null, 0);



    // Testing toString
    String toStringExpected = "\nManager:\n" +
            "\nName: Joe\n" +
            "Passenger(s): Mike\t\n";

    if (! d.toString().equals(toStringExpected)) {
      System.out.println("Problem with toString");
    }



    System.out.println("Testing Complete");

  }

}
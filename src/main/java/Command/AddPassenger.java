package Command;


import Singleton.PassengerMapAccess;
import Entities.Passenger;

import java.sql.Date;

/**
 * Allows the system to add a passenger
 */
public class AddPassenger implements Command{

    Passenger passenger;

    public AddPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    @Override
    public void execute() {
        String name, number, email;
        int parkingStall;
        Date date;


        name = passenger.getName();
        number = passenger.getId();
        email = passenger.getEmail();
        date = passenger.getCheckInDate();
        parkingStall = passenger.getParkingStallLabel();


        if (PassengerMapAccess.getInstance().get(number) == null) {
            Passenger passenger = new Passenger(name, number, email, date, parkingStall);
            PassengerMapAccess.getInstance().put(number, passenger);

        }

    }

}

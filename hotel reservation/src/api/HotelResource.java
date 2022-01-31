package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;
import java.util.Collection;
import java.util.Date;

public class HotelResource {

    private static CustomerService customerService = CustomerService.getInstance();
    private static ReservationService reservationService = ReservationService.getInstance();

    public static Customer getCustomer(String email){
        Customer c =customerService.getCustomer(email);
        return c;
    }

    public static void createACustomer(String email,String firstName,String lastName){
        customerService.addCustomer(email,firstName,lastName);
    }

    public static IRoom getRoom(String roomNumber){
        return reservationService.getRoom(roomNumber);
    }

    public static Reservation bookARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        return reservationService.reservationARoom(customer,room,checkInDate,checkOutDate);
    }

    public static Collection<Reservation> getCustomerRevervation(String customerEmail){
        Customer customer = getCustomer(customerEmail.trim());
        Collection<Reservation> collection = reservationService.getCustomerReservation(customer);
        return collection;
    }

    public static Collection<IRoom> findARoom(Date checkInDate, Date checkOutDate){
        return reservationService.findRooms(checkInDate,checkOutDate);
    }
}

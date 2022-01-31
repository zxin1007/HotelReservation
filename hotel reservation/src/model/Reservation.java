package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Reservation {
    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return "Reservation: " +
                customer +
                room +
                ", checkInDate=" + sdf.format(checkInDate) +
                ", checkOutDate=" + sdf.format(checkOutDate) +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        if (this==obj){
            return true;
        }
        if (!(obj instanceof Reservation)){
            return false;
        }
        Reservation reservation =(Reservation) obj;
        return sdf.format(this.checkInDate).equals(sdf.format(reservation.checkInDate))&&sdf.format(this.checkOutDate).equals(sdf.format(reservation.checkOutDate))&&this.room.getRoomNumber().equals(room.getRoomNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, room, checkInDate, checkOutDate);
    }
}

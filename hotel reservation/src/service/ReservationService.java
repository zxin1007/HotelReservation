package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class ReservationService{

    private static ReservationService INSTANCE;
    private static Collection<Reservation> reservations;
    private static Collection<IRoom> rooms;

    private ReservationService(){
        reservations = new ArrayList<>();
        rooms = new ArrayList<>();
    }

    public static ReservationService getInstance(){
        if (INSTANCE == null){
            INSTANCE = new ReservationService();
        }
        return INSTANCE;
    }

    String serviceOn() {
        return "Service is on";
    }

    /**
     *
     * @return Collection of Reservation
     */
    public static Collection<Reservation> getReservations() {
        return reservations;
    }

    /**
     * aad the room to the collection
     * fail to add a room if the room number is existed
     * @param room
     */
    public static void addRoom(IRoom room){
        String roomNum = room.getRoomNumber();
        System.out.println(room.getRoomNumber());
        if (getRoom(roomNum)!=null) {
            throw new IllegalArgumentException("Room Number already existed");
        }else {
            rooms.add(room);
        }
    }

    /**
     *
     * @param roomId
     * @return room with given room id
     */
    public static IRoom getRoom(String roomId){
        for (IRoom room:rooms){
            if (room.getRoomNumber().equals(roomId)){
                return room;
            }
        }
        return null;
    }

    /**
     *Create the reservation and add to collection
     * @param customer
     * @param room
     * @param checkInDate
     * @param checkOutDate
     * @return the Reservation
     */
    public static Reservation reservationARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        for (Reservation reservation:reservations){
            if (dateConflict(reservation.getCheckInDate(),reservation.getCheckOutDate(),checkInDate,checkOutDate)
                    &&room.getRoomNumber().equals(reservation.getRoom().getRoomNumber())){
                System.out.println("the room is not available on "+ sdf.format(checkInDate)+" - "+sdf.format(checkOutDate)+
                        ", Please select another room");
                throw new IllegalArgumentException();
            }
        }
        Reservation r = new Reservation(customer,room,checkInDate,checkOutDate);
        reservations.add(r);
        return r;
    }

    /**
     * if conflict in time call findRoomHelper()
     * @param checkInDate
     * @param checkOutDate
     * @return room available in the given time range
     */
    public static Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate){
        Collection<IRoom> room = new ArrayList<>(rooms);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        for (Reservation reservation:reservations){
             if (dateConflict(reservation.getCheckInDate(),reservation.getCheckOutDate(),checkInDate,checkOutDate)){
                room.remove(reservation.getRoom());
            }
        }
        if (room.isEmpty()){
            findRoomHelper(checkInDate,checkOutDate);
        }
        return room;
    }

    /**
     * give suggestion to customer with closest available date
     * @param checkInDate
     * @param checkOutDate
     */
    public static void findRoomHelper(Date checkInDate, Date checkOutDate){
        long minDiff=Integer.MAX_VALUE;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        long minTimeRange = -1;
        Date minDate=null;
        /*
        for (Reservation reservation:reservations) {
            dates.add(reservation.getCheckInDate());
            long diff = reservation.getCheckInDate().getTime() - checkInDate.getTime();
            if ((dateConflict(reservation.getCheckInDate(), reservation.getCheckOutDate(), checkInDate, checkOutDate))){

                minTimeRange = (reservation.getCheckOutDate().getTime() - reservation.getCheckInDate().getTime()) / (1000 * 60 * 60 * 24);

                long date = (checkOutDate.getTime()-checkInDate.getTime())/(1000 * 60 * 60 * 24); //formula from online

                c.setTime(checkInDate);
                minTimeRange = minTimeRange<=7? 0:minTimeRange;
                c.add(Calendar.DATE, (int)minTimeRange+7); // Adding 7 days to the closet check in date + the range
                checkInDate = c.getTime();
                c.setTime(checkInDate); //
                c.add(Calendar.DATE, (int) date); // Adding days of customer given
                checkOutDate = c.getTime();
            }
        }
         */
        long date = (checkOutDate.getTime()-checkInDate.getTime())/(1000 * 60 * 60 * 24); //formula from online
        c.setTime(checkInDate);
        //minTimeRange = minTimeRange<=7? 0:minTimeRange;
        c.add(Calendar.DATE, 7); // Adding 7 days to the closet check in date
        checkInDate = c.getTime();
        c.setTime(checkInDate);
        c.add(Calendar.DATE, (int) date); // Adding days of customer given
        checkOutDate = c.getTime();
        Collection<IRoom> rooms = findRooms(checkInDate,checkOutDate);
        if (!rooms.isEmpty()) {
            System.out.println("Recommend Date " + sdf.format(checkInDate) + " - " + sdf.format(checkOutDate) + " with recommend rooms: ");
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
        else{
            System.out.println("no recommend room in the time range");
        }
        throw new IllegalArgumentException();
    }

    public static boolean dateConflict (Date rCheckInDate, Date rCheckOutDate, Date checkInDate,Date checkOutDate){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        if ((rCheckInDate.after(checkInDate)&&rCheckOutDate.after(checkInDate)&&!rCheckInDate.after(checkOutDate))
                ||(rCheckInDate.after(checkInDate)&&rCheckOutDate.before(checkOutDate))
                ||(rCheckInDate.before(checkInDate)&&rCheckOutDate.before(checkOutDate)&&!rCheckOutDate.before(checkInDate))
                ||(rCheckInDate.before(checkInDate)&&rCheckOutDate.after(checkOutDate))
                ||(sdf.format(rCheckInDate).equals(sdf.format(checkInDate))&&sdf.format(rCheckOutDate).equals(sdf.format(checkOutDate)))
                ||sdf.format(rCheckInDate).equals(sdf.format(checkInDate))
                ||sdf.format(rCheckInDate).equals(sdf.format(checkOutDate))
                ||sdf.format(rCheckOutDate).equals(sdf.format(checkOutDate))){
            return true;
        }
        return false;
    }

    /**
     *
     * @param customer
     * @return all the reservations of given customer
     */
    public static Collection<Reservation> getCustomerReservation(Customer customer){
        Collection<Reservation> r = new ArrayList<>();
        for (Reservation reservation:reservations){
            if(reservation.getCustomer().equals(customer)){
                r.add(reservation);
            }
        }
        return r;
    }

    /**
     * print all the reservation of all customer
     */
    public static void printAllReservation(){
        for (Reservation reservation:reservations){
            System.out.println(reservation);
        }
    }

    /**
     *
     * @return collection of Rooms
     */
    public static Collection<IRoom> getRooms() {
        return rooms;
    }

}

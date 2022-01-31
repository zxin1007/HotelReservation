package ui;

import api.AdminResource;
import api.HotelResource;
import model.*;

import java.util.*;

public class AdminMenu {

    public static void showMenu(Scanner sc) throws Exception {
        System.out.println("\nAdmin Menu\n--------------------------\n"+
                "1.See all Customer\n"+
                "2.See all Rooms\n"+
                "3.See all Reservation\n"+
                "4.Add a Room\n"+
                "5.Back to Main Menu");
        System.out.println("--------------------------\nPlease select a number for the menu option");
        int option = 0;
        boolean c = true;
        do {
            try {
                option = sc.nextInt();
                if (option<1||option>5){
                    throw new InputMismatchException();
                }
                if (option==5){
                    MainMenu.showMenu(sc);
                } else {
                    AdminMenu.serviceCall(option,sc);
                }
                c=false;
            } catch (InputMismatchException ex) {
                System.out.println("Enter 1-5, Please try again!");
                sc.next();
            }
        } while(c);
    }

    //1.See all Customer
    //2.See all Rooms
    //3.See all Reservation
    //4.Add a Room
    //5.Back to Main Menu
    private static void serviceCall(int option, Scanner sc) throws Exception {
        switch (option) {
            case 1:
                Collection<Customer> custList = AdminResource.getAllCustomers();
                for (Customer customer : custList) {
                    System.out.println(customer);
                }
                break;
            case 2:
                Collection<IRoom> roomList = AdminResource.getAllRooms();
                for (IRoom room : roomList) {
                    System.out.println(room);
                }
                break;
            case 3:
                AdminResource.displayAllReservations();
                break;
            case 4:
                addARoom(sc);
                break;
        }
        AdminMenu.showMenu(sc);
    }


    private static void addARoom(Scanner sc){
        List<IRoom> list = new ArrayList<>();
        boolean addRoom = true;
        String roomNum = null;
        Double price;
        int type;
        RoomType roomType = null;
        while (addRoom) {
            while (true) {
                try {
                    System.out.println("Enter a room number");
                    roomNum = sc.next();
                    if (HotelResource.getRoom(roomNum) != null) {
                        throw new IllegalArgumentException();
                    }
                    for (IRoom room : list) {
                        if (room.getRoomNumber().equals(roomNum)) {
                            throw new IllegalArgumentException();
                        }
                    }
                    break;
                } catch (IllegalArgumentException ex) {
                    System.out.println("Room number already existed");
                }
            }
            while (true) {
                try {
                    System.out.println("Enter price per night");
                    price = sc.nextDouble();
                    if (price < 0) {
                        throw new IllegalArgumentException();
                    }
                    break;
                } catch (IllegalArgumentException ex) {
                    System.out.println("price can not be negative");
                }
            }

            while (true) {
                try {
                    System.out.println("Enter room type: 1 for single bed, 2 for double bed");
                    type = sc.nextInt();
                    if (type == 1 || type == 2) {
                        roomType = type == 1 ? RoomType.SINGLE : RoomType.DOUBLE;
                    } else {
                        throw new IllegalArgumentException();
                    }
                    if (price != 0) {
                        list.add(new Room(roomNum, price, roomType));
                    } else if (price > 0) {
                        list.add(new FreeRoom(roomNum, roomType));
                    }
                    break;
                } catch (IllegalArgumentException ex) {
                    System.out.println("1 for single bed, 2 for double bed");
                }
            }

            String con;
            while (true) {
                try {
                    System.out.println("Would you like to add another room y/n");
                    con = sc.next();
                    if (!(con.equalsIgnoreCase("y") || con.equalsIgnoreCase("n"))) {
                        throw new IllegalArgumentException();
                    }
                    break;
                } catch (IllegalArgumentException ex) {
                    System.out.println("Please Enter Y (Yes) or N (No)");
                }
            }
            addRoom = con.equalsIgnoreCase("y") ? true : false;
        }
        AdminResource.addRoom(list);
    }
}


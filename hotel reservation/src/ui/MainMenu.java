package ui;

import api.AdminResource;
import api.HotelResource;
import model.IRoom;
import model.Reservation;
import service.ReservationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainMenu {

    public MainMenu(){
        HotelResource hotelResource = new HotelResource();
    }

    public static void showMenu(Scanner sc) throws Exception {
        System.out.println("Welcome to the Hotel Reservation Application\n\n"+
                "--------------------------\n"+
                "1.Find and reserve a room\n"+
                "2.See my reservation\n"+
                "3.Create an account\n"+
                "4.Admin\n"+
                "5.Exit");
        System.out.println("--------------------------\nPlease select a number for the menu option");
        int option = 0;
        boolean c = true;
        do {
            try {
                option = sc.nextInt();
                if (option<1||option>5){
                    throw new InputMismatchException();
                }
                if (option==4){
                    AdminMenu.showMenu(sc);
                } else {
                    MainMenu.serviceCall(option,sc);
                }
                c=false;
            } catch (InputMismatchException ex) {
                System.out.println("Enter 1-5 Please try again!");
                sc.next();
            }
        } while(c);
    }
    //1.Find and reserve a room
    //2.See my reservation
    //3.Create an account
    //4.Admin
    //5.Exit

    public static void serviceCall(int option,Scanner sc) throws Exception {
        String email = "";
        switch (option) {
            case 1:
                if (!ReservationService.getRooms().isEmpty()) {
                    bookARoom();
                } else {
                    System.out.println("no room available at this point\n");
                }
                break;
            case 2:
                email = emailInput(sc);
                if (HotelResource.getCustomer(email) != null) {
                    myReservation(email);
                } else {
                    System.out.println("Customer not exist\n");
                }
                break;
            case 3:
                createAnAccount(sc);
                System.out.println("Account successfully created\n");
                break;
            case 5:
                sc.close();
                System.exit(0);
        }
        MainMenu.showMenu(sc);

    }

    public static String emailInput (Scanner sc){
        String email="";
        while(true) {
            try {
                System.out.println("Enter your email");
                email  = sc.next();
                if (!emailReg(email)) {
                    throw new IllegalArgumentException("Format incorrect Please try again!");
                }
                break;
            }catch (IllegalArgumentException ex){
                ex.getMessage();
            }
        }
        return email;
    }

    public static void createAnAccount(Scanner sc) {
        String email = emailInput(sc);
        System.out.println("Enter your first name");
        String firstName = sc.next();
        System.out.println("Enter your last name");
        String lastName = sc.next();
        HotelResource.createACustomer(email, firstName, lastName);
    }

    public static boolean emailReg (String email){
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static void myReservation(String email){
        Collection<Reservation> reservations = HotelResource.getCustomerRevervation(email);
        if (!reservations.isEmpty()){
            for (Reservation reservation:reservations){
                System.out.println(reservation);
            }
        } else{
            System.out.println("no reservation made yet");
        }
    }

    public static void bookARoom() throws Exception {
        int room = -1;
        String email ="";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String checkInDateS, checkOutDateS;
        Date checkInDate = null, checkOutDate = null;
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Enter the check in date (MM/dd/yyyy)");
                checkInDateS = sc.next();
                System.out.println("Enter the check out date (MM/dd/yyyy)");
                checkOutDateS = sc.next();
                simpleDateFormat.setLenient(false);
                checkInDate = simpleDateFormat.parse(checkInDateS);
                checkOutDate = simpleDateFormat.parse(checkOutDateS);

                if (checkInDate.after(checkOutDate)) {
                    System.out.println("Check in date can't be after check out date, Please try again!");
                    throw new IllegalArgumentException();
                }
                Collection<IRoom> rooms = HotelResource.findARoom(checkInDate,checkOutDate);
                for (IRoom r:rooms){
                    System.out.println(r);
                }
                break;
            } catch (ParseException e) {
                System.out.println("Date format incorrect Please enter mm/dd/yyyy");
            } catch (IllegalArgumentException e){
                e.getMessage();
            }
        }


        String choice = "";
        while (true) {
            try {
                System.out.println("Do you want to reserve a room? y/n");
                choice = sc.next();
                if (!(choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("n"))) {
                    throw new IllegalArgumentException("Please Enter Y (Yes) or N (No)");
                }
                break;
            } catch (IllegalArgumentException e) {
                e.getMessage();
            }
        }

        while (true) {
            try {
                String option = "";
                if (choice.equalsIgnoreCase("y")) {
                    System.out.println("Do you have an account with us? y/n");
                    option = sc.next();
                    if (!(option.equalsIgnoreCase("y") || (option.equalsIgnoreCase("n")))) {
                        throw new IllegalArgumentException();
                    }

                    if (option.equalsIgnoreCase("y")) {
                        email= bookARoomHelper(email, sc);
                    } else if (option.equalsIgnoreCase("n")){
                        //create an account
                        createAnAccount(sc);
                        email = bookARoomHelper(email, sc);
                    }
                }
                break;
            } catch (IllegalArgumentException e) {
                e.getMessage();
            }
        }
        while (true) {
            try {
                if (choice.equalsIgnoreCase("y")) {
                    System.out.println("Enter the room number you want to reserve");
                    room = sc.nextInt();
                    if (HotelResource.getRoom(Integer.toString(room)) == null) {
                        throw new InputMismatchException();
                    }
                    HotelResource.bookARoom(HotelResource.getCustomer(email), HotelResource.getRoom(Integer.toString(room)), checkInDate, checkOutDate);
                }
                break;
            } catch (IllegalArgumentException e) {
                e.getMessage();
            } catch (InputMismatchException e){
                System.out.println("must enter valid room number");
                sc.next();
            }
        }

        MainMenu.showMenu(sc);

        }

        public static String bookARoomHelper (String email,Scanner sc){
        System.out.println("Enter email format name@domain.com");
        while (true) {
            try {
                email = sc.next();
                if (!emailReg(email)) {
                    throw new IllegalArgumentException("Email format incorrect name@domain.com");
                }
                if (HotelResource.getCustomer(email) == null) {
                    //throw a exception
                    throw new IllegalArgumentException("customer didn't exist, Please try again");
                }
                break;
            } catch (IllegalArgumentException ex) {
                ex.getMessage();
            }
        }
        return email;
    }

}

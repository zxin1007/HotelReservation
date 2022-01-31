package ui;

import java.util.*;

public class HotelApplication {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        MainMenu mainMenu = new MainMenu();
        mainMenu.showMenu(sc);
        sc.close();
    }

}

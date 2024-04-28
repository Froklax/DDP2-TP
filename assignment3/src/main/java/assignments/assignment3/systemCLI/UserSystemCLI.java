package assignments.assignment3.systemCLI;

import java.util.Scanner;
import assignments.assignment3.User;

public abstract class UserSystemCLI {
    protected Scanner input = new Scanner(System.in);
    // Menambahkan parameter userLoggedIn agar user bisa diakses di customerSystemCLI
    public void run(User userLoggedIn) {
        boolean isLoggedIn = true;
        while (isLoggedIn) {
            displayMenu();
            int command = input.nextInt();
            input.nextLine();
            isLoggedIn = handleMenu(command, userLoggedIn);
        }
    }
    abstract void displayMenu();
    abstract boolean handleMenu(int command, User userLoggedIn);
}

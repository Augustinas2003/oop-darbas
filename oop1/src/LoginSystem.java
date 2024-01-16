import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoginSystem {
    private List<User> users;

    public LoginSystem(List<User> users) {
        this.users = users;
    }

    public User login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Return the user if found
            }
        }

        return null; // Return null if the user is not found
    }

    private void initializeUsageFile(int userId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usage.txt", true))) {
            writer.write(userId + ",0,0,0,0,0"); // Default usage values
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserUsageRecorded(int userId) {
        try (BufferedReader br = new BufferedReader(new FileReader("usage.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    int id = Integer.parseInt(parts[0].trim());
                    if (id == userId) {
                        return true; // User's usage is already recorded
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // User's usage is not recorded
    }

    public static List<User> readUsersFromFile(String filename) {
        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) { // Update to 7 fields (including group)
                    String username = parts[0];
                    String password = parts[1];
                    String position = parts[2];
                    String name = parts[3];
                    String surname = parts[4];
                    String group = parts[5]; // New field
                    int id = Integer.parseInt(parts[6]);
                    users.add(new User(username, password, position, name, surname, group, id));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void main(String[] args) {
        List<User> users = readUsersFromFile("loginfo.txt");
        LoginSystem loginSystem = new LoginSystem(users);

        User loggedInUser = loginSystem.login();

        if (loggedInUser != null) {
            System.out.println("Login successful!");

            if (!loginSystem.isUserUsageRecorded(loggedInUser.getId())) {
                loginSystem.initializeUsageFile(loggedInUser.getId());
                System.out.println("Usage initialized for user with ID " + loggedInUser.getId());
            }

            UserMenu userMenu = new UserMenu(loggedInUser, users); // Pass the list of users
            userMenu.displayMenu();
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
    }
}

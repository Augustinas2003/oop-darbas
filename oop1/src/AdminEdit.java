import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AdminEdit {
    private User admin;
    private List<User> clients;
    private List<User> workers; // List for workers

    public AdminEdit(User admin, List<User> clients, List<User> workers) {
        this.admin = admin;
        this.clients = clients;
        this.workers = workers; // Initialize the workers list
    }


    public void addWorker() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the worker's name: ");
        String name = scanner.nextLine();

        System.out.print("Enter the worker's surname: ");
        String surname = scanner.nextLine();

        System.out.print("Enter the worker's group: ");
        String group = scanner.nextLine();

        System.out.print("Enter the worker's username (or 'auto' for automatic username): ");
        String usernameInput = scanner.nextLine();
        String username = (usernameInput.equalsIgnoreCase("auto")) ? name : usernameInput;

        System.out.print("Enter the worker's password (or 'auto' for automatic password): ");
        String passwordInput = scanner.nextLine();
        String password = (passwordInput.equalsIgnoreCase("auto")) ? surname : passwordInput;

        // Generate the next ID for the new worker
        int nextId = getNextId();

        // Create a new worker user
        User worker = new User(username, password, "worker", name, surname, group, nextId);

        // Add the worker to the list
        clients.add(worker); // Assuming you want to add workers to the 'clients' list

        // Update the loginfo.txt file
        updateLogInfoFile(worker);

        System.out.println("Worker added successfully!");
    }

    // Helper method to generate the next worker ID

    private int getNextId() {
        // Create a set to store all existing IDs
        Set<Integer> existingIds = new HashSet<>();

        // Add all client and worker IDs from memory
        for (User user : clients) {
            existingIds.add(user.getId());
        }

        // Add all IDs from the loginfo.txt file
        try (BufferedReader br = new BufferedReader(new FileReader("loginfo.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[5]);
                    existingIds.add(id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Find the smallest unused ID
        int nextId = 1;
        while (existingIds.contains(nextId)) {
            nextId++;
        }

        return nextId;
    }

    public void chooseWorker() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an option:");
        System.out.println("1. Search by ID");
        System.out.println("2. View workers list");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            // Search by ID
            System.out.print("Enter the worker's ID: ");
            int workerId = scanner.nextInt();
            User worker = getWorkerById(workerId);
            if (worker != null) {
                System.out.println("Worker found:");
                System.out.println("Name: " + worker.getName());
                System.out.println("Surname: " + worker.getSurname());
                System.out.println("Username: " + worker.getUsername());
            } else {
                System.out.println("Worker not found.");
            }
        } else if (choice == 2) {
            // View worker list
            System.out.println("List of Workers:");
            for (User worker : workers) {
                if ("worker".equals(worker.getPosition())) {
                    System.out.println("ID: " + worker.getId() + ", Name: " + worker.getName() + ", Surname: " + worker.getSurname() + ", Username: " + worker.getUsername());
                }
            }
            System.out.print("Enter the worker's ID: ");
            int workerId = scanner.nextInt();
        } else {
            System.out.println("Worker not found.");
        }
    }


    // Helper method to update the loginfo.txt file
    private void updateLogInfoFile(User client) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("loginfo.txt", true))) {
            writer.write(client.getUsername() + "," + client.getPassword() + "," +
                    client.getPosition() + "," + client.getName() + "," +
                    client.getSurname() + "," + client.getId());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get a client by ID
    private User getClientById(int clientId) {
        for (User client : clients) {
            if ("client".equals(client.getPosition()) && client.getId() == clientId) {
                return client;
            }
        }
        return null;
    }
    // Helper method to get a worker by ID
    private User getWorkerById(int workerId) {
        for (User worker : workers) {
            if ("worker".equals(worker.getPosition()) && worker.getId() == workerId) {
                return worker;
            }
        }
        return null;
    }
}
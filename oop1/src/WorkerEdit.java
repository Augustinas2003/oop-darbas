import java.io.*;
import java.util.List;
import java.util.Scanner;

public class WorkerEdit {
    private User worker;
    private List<User> clients;

    public WorkerEdit(User worker, List<User> clients) {
        this.worker = worker;
        this.clients = clients;
    }

    public void addClient() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the client's name: ");
        String name = scanner.nextLine();

        System.out.print("Enter the client's surname: ");
        String surname = scanner.nextLine();

        System.out.print("Enter the client's group: "); // Prompt for the "group"
        String group = scanner.nextLine();

        System.out.print("Enter the client's username (or 'auto' for automatic username): ");
        String usernameInput = scanner.nextLine();
        String username = (usernameInput.equalsIgnoreCase("auto")) ? name : usernameInput;

        System.out.print("Enter the client's password (or 'auto' for automatic password): ");
        String passwordInput = scanner.nextLine();
        String password = (passwordInput.equalsIgnoreCase("auto")) ? surname : passwordInput;

        // Generate the next ID for the new client
        int nextId = getNextClientId();

        // Create a new client user with the "group" field
        User client = new User(username, password, "client", name, surname, group, nextId);

        // Add the client to the list
        clients.add(client);

        // Update the loginfo.txt file
        updateLogInfoFile(client);

        System.out.println("Client added successfully!");
    }

    // Helper method to generate the next client ID
    private int getNextClientId() {
        int maxId = 0;
        for (User client : clients) {
            if ("client".equals(client.getPosition()) && client.getId() > maxId) {
                maxId = client.getId();
            }
        }

        // Check the existing IDs in the file
        try (BufferedReader br = new BufferedReader(new FileReader("loginfo.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[5]);
                    if ("client".equals(parts[2]) && id > maxId) {
                        maxId = id;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxId + 1;
    }
    public void changePrice() {
        try (BufferedReader br = new BufferedReader(new FileReader("pricelist.txt"))) {
            String line;
            int groupNumber = 1;

            System.out.println("Choose group:");

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    System.out.println(groupNumber + ". \"" + parts[0] + "\"");
                    groupNumber++;
                }
            }

            System.out.print("Enter the number of the group you want to choose: ");
            Scanner scanner = new Scanner(System.in);
            int selectedGroupNumber = scanner.nextInt();

            // Validate user input
            if (selectedGroupNumber >= 1 && selectedGroupNumber < groupNumber) {
                // Retrieve the selected group name
                String selectedGroupName = getGroupNameByNumber(selectedGroupNumber);

                // Display service prices for the selected group
                displayServicePrices(selectedGroupName, "pricelist.txt");

                // Prompt user for new prices
                updateServicePrices(selectedGroupName);

            } else {
                System.out.println("Invalid group number.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to display service prices for a selected group
    private void displayServicePrices(String groupName, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            System.out.println("Group \"" + groupName + "\"");

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(groupName)) {
                    String[] serviceNames = {"Hot water", "Cold water", "Day light", "Night light", "Gas"};

                    for (int i = 1; i < parts.length; i++) {
                        System.out.println((i) + ". " + serviceNames[i - 1] + " price: \"" + parts[i] + "\"");
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to update service prices for a selected group
    private void updateServicePrices(String groupName) {
        try (BufferedReader br = new BufferedReader(new FileReader("pricelist.txt"))) {
            String line;
            StringBuilder newPrices = new StringBuilder();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(groupName)) {
                    String[] serviceNames = {"Hot water", "Cold water", "Day light", "Night light", "Gas"};


                    System.out.print("Enter the number of the service you want to update: ");
                    Scanner scanner = new Scanner(System.in);
                    int selectedServiceNumber = scanner.nextInt();

                    // Validate user input
                    if (selectedServiceNumber >= 1 && selectedServiceNumber <= serviceNames.length) {
                        System.out.print("Enter new price for " + serviceNames[selectedServiceNumber - 1] + ": ");
                        String newPrice = scanner.next();

                        // Update the selected service's price
                        parts[selectedServiceNumber] = newPrice;
                    } else {
                        System.out.println("Invalid service number.");
                    }
                }
                newPrices.append(String.join(",", parts)).append(System.lineSeparator());
            }

            // Write the updated prices back to the pricelist.txt file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("pricelist.txt"))) {
                writer.write(newPrices.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Prices updated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get group name by number
    private String getGroupNameByNumber(int groupNumber) {
        try (BufferedReader br = new BufferedReader(new FileReader("pricelist.txt"))) {
            String line;
            int currentGroupNumber = 1;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    if (currentGroupNumber == groupNumber) {
                        return parts[0];
                    }
                    currentGroupNumber++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void newGroup() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the new group: ");
        String newGroupName = scanner.nextLine();

        // Check if the group already exists
        if (groupExists(newGroupName, "pricelist.txt")) {
            System.out.println("Group \"" + newGroupName + "\" already exists. Please choose a different name.");
            return;
        }

        System.out.println("Enter prices for the new group:");
        String[] serviceNames = {"Hot water", "Cold water", "Day light", "Night light", "Gas"};
        StringBuilder newGroupPrices = new StringBuilder(newGroupName);

        for (String serviceName : serviceNames) {
            System.out.print("Enter price for " + serviceName + ": ");
            String price = scanner.nextLine();
            newGroupPrices.append(",").append(price);
        }

        // Write the new group and prices to the pricelist.txt file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pricelist.txt", true))) {
            writer.write(newGroupPrices.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("New group added successfully!");
    }

    private boolean groupExists(String groupName, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(groupName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void chooseClient() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an option:");
        System.out.println("1. Search by ID");
        System.out.println("2. View client list");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            // Search by ID
            System.out.print("Enter the client's ID: ");
            int clientId = scanner.nextInt();
            User client = getClientById(clientId);
            if (client != null) {
                System.out.println("Client found:");
                System.out.println("Name: " + client.getName());
                System.out.println("Surname: " + client.getSurname());
                System.out.println("Username: " + client.getUsername());
                System.out.println("Group: " + client.getGroup());
            } else {
                System.out.println("Client not found.");
            }
        } else if (choice == 2) {
            // View client list
            System.out.println("List of Clients:");
            for (User client : clients) {
                if ("client".equals(client.getPosition())) {
                    System.out.println("ID: " + client.getId() + ", Name: " + client.getName() + ", Surname: " + client.getSurname() + ", Username: " + client.getUsername() + ", Group: " + client.getGroup());
                }

            }
            System.out.print("Enter the client's ID: ");
            int clientId = scanner.nextInt();
        }
        else {
            System.out.println("Client not found.");
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
}
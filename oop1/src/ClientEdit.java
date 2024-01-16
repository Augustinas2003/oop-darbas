import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Single Responsibility Principle (SRP): The class has a single responsibility,
// which is to handle the client's interactions related to usage declarations.
public class ClientEdit {

    private User client;
    private int userId;

    public ClientEdit(User client) {
        this.client = client;
        this.userId = client.getId();
    }


    public void viewDeclarations() {
        try {
            BufferedReader usageReader = new BufferedReader(new FileReader("usage.txt"));
            BufferedReader priceListReader = new BufferedReader(new FileReader("pricelist.txt"));

            String usageLine;
            String priceListLine;

            while ((usageLine = usageReader.readLine()) != null) {
                String[] usageParts = usageLine.split(",");
                int currentUserId = Integer.parseInt(usageParts[0]);

                if (currentUserId == userId) {
                    double hotWaterUsage = Double.parseDouble(usageParts[1]);
                    double coldWaterUsage = Double.parseDouble(usageParts[2]);
                    double dayLightUsage = Double.parseDouble(usageParts[3]);
                    double nightLightUsage = Double.parseDouble(usageParts[4]);
                    double gasUsage = Double.parseDouble(usageParts[5]);

                    // Display usages
                    System.out.println("Hot water usage: " + hotWaterUsage + " (Price: " + getUsagePrice("hot", hotWaterUsage) + ")");
                    System.out.println("Cold water usage: " + coldWaterUsage + " (Price: " + getUsagePrice("cold", coldWaterUsage) + ")");
                    System.out.println("Day light usage: " + dayLightUsage + " (Price: " + getUsagePrice("day", dayLightUsage) + ")");
                    System.out.println("Night light usage: " + nightLightUsage + " (Price: " + getUsagePrice("night", nightLightUsage) + ")");
                    System.out.println("Gas usage: " + gasUsage + " (Price: " + getUsagePrice("gas", gasUsage) + ")");
                    break;
                }
            }

            usageReader.close();
            priceListReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get the usage price based on the type and quantity
    private double getUsagePrice(String usageType, double quantity) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("pricelist.txt"));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String group = parts[0].trim();

                if (group.equalsIgnoreCase(client.getGroup())) {
                    int column;
                    switch (usageType) {
                        case "hot":
                            column = 1;
                            break;
                        case "cold":
                            column = 2;
                            break;
                        case "day":
                            column = 3;
                            break;
                        case "night":
                            column = 4;
                            break;
                        case "gas":
                            column = 5;
                            break;
                        default:
                            System.out.println("Invalid usage type.");
                            return 0.0;
                    }

                    double price = Double.parseDouble(parts[column]);
                    return price * quantity;
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Invalid user group.");
        return 0.0;
    }



    public void decelerate() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Water");
            System.out.println("2. Electricity");
            System.out.println("3. Gas");
            System.out.println("4. Back");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    waterMenu();
                    break;
                case 2:
                    electricityMenu();
                    break;
                case 3:
                    gasMenu();
                    break;
                case 4:
                    return; // Go back to the main menu
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void waterMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option for water:");
            System.out.println("1. Hot Water");
            System.out.println("2. Cold Water");
            System.out.println("3. Back");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    displayLastUsage("water", "hot");
                    enterWaterUsage("hot");
                    break;
                case 2:
                    displayLastUsage("water", "cold");
                    enterWaterUsage("cold");
                    break;
                case 3:
                    return; // Go back to the main menu
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void electricityMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option for electricity:");
            System.out.println("1. Day Usage");
            System.out.println("2. Night Usage");
            System.out.println("3. Back");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    displayLastUsage("electricity", "day");
                    enterElectricityUsage("day");
                    break;
                case 2:
                    displayLastUsage("electricity", "night");
                    enterElectricityUsage("night");
                    break;
                case 3:
                    return; // Go back to the main menu
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void gasMenu() {
        displayLastUsage("gas", "");
        enterGasUsage();
    }

    public void enterWaterUsage(String type) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your " + type + " water usage: ");
        double usage = scanner.nextDouble();

        int column;
        if ("hot".equals(type)) {
            column = 1;
        } else if ("cold".equals(type)) {
            column = 2;
        } else {
            System.out.println("Invalid water type.");
            return;
        }

        // Update water usage in usage.txt
        updateUsageFile(userId, column, usage);

        System.out.println("Water usage recorded.");
    }

    public void enterElectricityUsage(String time) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your " + time + " electricity usage: ");
        double usage = scanner.nextDouble();

        // Update electricity usage in usage.txt
        updateUsageFile(userId, "day".equals(time) ? 3 : 4, usage);

        System.out.println("Electricity usage recorded.");
    }

    public void enterGasUsage() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your gas usage: ");
        double usage = scanner.nextDouble();

        // Update gas usage in usage.txt
        updateUsageFile(userId, 5, usage);

        System.out.println("Gas usage recorded.");
    }

    // Helper method to display last recorded usage
    private void displayLastUsage(String usageType, String subType) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("usage.txt"));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int currentUserId = Integer.parseInt(parts[0]);

                if (currentUserId == userId) {
                    int column = getUsageColumn(usageType, subType);
                    if (column != -1) {
                        System.out.println("Your last " + usageType + " usage is: " + parts[column]);
                    } else {
                        System.out.println("Invalid usage type.");
                    }
                    break;
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get the column index for a given usage type and subtype
    private int getUsageColumn(String usageType, String subType) {
        switch (usageType) {
            case "electricity":
                return "day".equals(subType) ? 3 : 4;
            case "gas":
                return 5;
            case "water":
                return "hot".equals(subType) ? 1 : ("cold".equals(subType) ? 2 : -1);
            default:
                return -1;
        }
    }

    // Helper method to update usage in usage.txt
    private void updateUsageFile(int userId, int column, double usage) {
        try {
            File inputFile = new File("usage.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int currentUserId = Integer.parseInt(parts[0]);

                if (currentUserId == userId) {
                    // Update the specified column with the new usage
                    parts[column] = String.valueOf(usage);
                    line = String.join(",", parts);
                }

                writer.write(line + System.lineSeparator());
            }

            writer.close();
            reader.close();

            // Use Files.move instead of renameTo
            Files.move(Path.of(tempFile.getPath()), Path.of(inputFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

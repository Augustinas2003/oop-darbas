import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Pay {
    private int clientId;
    private List<User> users;

    public Pay(int clientId, List<User> users) {
        this.clientId = clientId;
        this.users = users;
    }

    public void calculatePayment() {
        // Find the user with the given client ID
        User client = findUserById(clientId);

        if (client != null) {
            // Get the group of the user
            String group = client.getGroup();

            // Find the corresponding row in pricelist.txt based on the group
            String priceListFileName = "pricelist.txt";
            double[] priceList = getPriceList(group, priceListFileName);

            // Find the corresponding row in usage.txt based on the client ID
            String usageFileName = "usage.txt";
            double[] usage = getUsage(clientId, usageFileName);

            // Perform the calculation
            double hotWaterPay = priceList[1] * usage[1];
            double coldWaterPay = priceList[2] * usage[2];
            double dayLightPay = priceList[3] * usage[3];
            double nightLightPay = priceList[4] * usage[4];
            double gasPay = priceList[5] * usage[5];

            // Display the results
            System.out.println("Hot water usage price: " + hotWaterPay);
            System.out.println("Cold water usage price: " + coldWaterPay);
            System.out.println("Day light usage price: " + dayLightPay);
            System.out.println("Night light usage price: " + nightLightPay);
            System.out.println("Gas usage price: " + gasPay);
        } else {
            System.out.println("Client ID not found.");
        }
    }

    private User findUserById(int clientId) {
        for (User user : users) {
            if (user.getId() == clientId) {
                return user;
            }
        }
        return null;
    }

    private double[] getPriceList(String group, String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(group)) {
                    // Convert the remaining columns to doubles
                    double[] priceList = new double[parts.length];
                    for (int i = 1; i < parts.length; i++) {
                        priceList[i] = Double.parseDouble(parts[i]);
                    }
                    return priceList;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new double[0]; // Return an empty array if the group is not found
    }

    private double[] getUsage(int clientId, String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && Integer.parseInt(parts[0]) == clientId) {
                    // Convert the remaining columns to doubles
                    double[] usage = new double[parts.length];
                    for (int i = 1; i < parts.length; i++) {
                        usage[i] = Double.parseDouble(parts[i]);
                    }
                    return usage;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new double[0]; // Return an empty array if the client ID is not found
    }
}

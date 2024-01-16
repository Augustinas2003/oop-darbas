import java.util.List;
import java.util.Scanner;


public class UserMenu {
    private User loggedInUser;
    private List<User> users;
    private ClientEdit clientEdit;
    private WorkerEdit workerEdit;
    private AdminEdit adminEdit;

    public UserMenu(User loggedInUser, List<User> users) {
        this.loggedInUser = loggedInUser;
        this.users = users;
        this.clientEdit = new ClientEdit(loggedInUser);
        this.workerEdit = new WorkerEdit(loggedInUser, users);
        this.adminEdit = new AdminEdit(loggedInUser, users, users); // Assuming the 'users' list contains both clients and workers
    }

    public void displayMenu() {
        if (loggedInUser != null) {
            String position = loggedInUser.getPosition();
            System.out.println("Login success! Welcome " + loggedInUser.getName() + " " + loggedInUser.getSurname());

            if ("client".equalsIgnoreCase(position)) {
                // Client-specific actions (declare, view)
                System.out.println("Choose an action:");
                System.out.println("1. Declare");
                System.out.println("2. View");
            } else if ("worker".equalsIgnoreCase(position)) {
                // Worker-specific actions (choose client, add client)
                System.out.println("Choose an action:");
                System.out.println("1. Choose Client");
                System.out.println("2. Add Client");
                System.out.println("3. Change price");
                System.out.println("4. Add new group");
            } else if ("admin".equalsIgnoreCase(position)) {
                // Admin-specific actions (choose client, choose worker, add client, add worker)
                System.out.println("Choose an action:");
                System.out.println("1. Choose Client");
                System.out.println("2. Choose Worker");
                System.out.println("3. Add Client");
                System.out.println("4. Add Worker");
                System.out.println("5. Change price");
                System.out.println("6. Add new group");
            }

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if ("client".equalsIgnoreCase(position)) {
                        // Implement client action
                        clientEdit.decelerate();
                    } else if ("worker".equalsIgnoreCase(position)) {
                        // Implement worker action (choose client)
                        workerEdit.chooseClient();
                    } else if ("admin".equalsIgnoreCase(position)) {
                        // Implement admin action (choose client)
                        workerEdit.chooseClient();
                    }
                    break;
                case 2:
                    if ("client".equalsIgnoreCase(position)) {
                        // Implement client action (view)
                        clientEdit.viewDeclarations();
                    } else if ("worker".equalsIgnoreCase(position)) {
                        // Implement worker action (add client)
                        workerEdit.addClient();
                    } else if ("admin".equalsIgnoreCase(position)) {
                        // Implement admin action (choose worker)
                        adminEdit.chooseWorker();
                    }
                    break;
                case 3:

                    if ("admin".equalsIgnoreCase(position)) {
                        // Implement worker action (add client)
                        workerEdit.addClient();
                    }else if ("worker".equalsIgnoreCase(position)) {
                        // Implement worker action (add client)
                        workerEdit.changePrice();
                    }
                case 4:
                    if ("admin".equalsIgnoreCase(position)) {
                        // Implement admin action (add worker)
                        adminEdit.addWorker();
                    }
                    else if ("worker".equalsIgnoreCase(position)) {
                        // Implement worker action (add client)
                        workerEdit.newGroup();
                    }
                    break;
                case 5:
                    if ("admin".equalsIgnoreCase(position)) {
                        // Implement admin action (change price)
                        workerEdit.changePrice();
                    }
                    break;
                case 6:
                    if ("admin".equalsIgnoreCase(position)) {
                        // Implement admin action (add new group)
                        workerEdit.newGroup();
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            scanner.close();
        } else {
            System.out.println("Login failed. Try again.");
        }
    }
}
import java.util.List;

public class Client {
    private List<User> clients;

    public Client(List<User> clients) {
        this.clients = clients;
    }

    public void addClient(User user) {
        clients.add(user);
    }

    // Implement client-specific actions (declare, view, pay) here
}

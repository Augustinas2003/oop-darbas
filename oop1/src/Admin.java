import java.util.List;

public class Admin {
    private List<User> clients;
    private List<User> workers;

    public Admin(List<User> clients, List<User> workers) {
        this.clients = clients;
        this.workers = workers;
    }

    public void addClient(User user) {
        clients.add(user);
    }

    public void addWorker(User user) {
        workers.add(user);
    }

    // Implement admin-specific actions (choose client, choose worker, add client, add worker) here
}

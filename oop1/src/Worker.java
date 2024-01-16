import java.util.List;

public class Worker {
    private List<User> workers;

    public Worker(List<User> workers) {
        this.workers = workers;
    }

    public void addClient(User user) {
        workers.add(user);
    }

    // Implement worker-specific actions (choose client, add client) here
}

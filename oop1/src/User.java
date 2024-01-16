public class User extends Person {
    private String username;
    private String password;
    private String position;
    private String group;

    // Constructor initializes the fields
    public User(String username, String password, String position, String name, String surname, String group, int id) {
        super(name, surname, id); // Call the constructor of the base class
        this.username = username;
        this.password = password;
        this.position = position;
        this.group = group;
    }

    // Getter methods specific to User
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPosition() {
        return position;
    }

    public String getGroup() {
        return group;
    }
}

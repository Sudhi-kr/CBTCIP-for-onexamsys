import java.util.*;
// User class to handle user data
class User {
    private String username;
    private String password;
    private String email;
    private Map<String, String> profile;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profile = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void updateProfile(String key, String value) {
        profile.put(key, value);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public Map<String, String> getProfile() {
        return profile;
    }
}

// Exam system to manage exam flow
class ExamSystem {
    private User loggedInUser;
    private List<User> users = new ArrayList<>();
    private Map<Integer, String> questions = new HashMap<>();
    private Map<Integer, String> answers = new HashMap<>();
    private Map<Integer, String> userResponses = new HashMap<>();
    private boolean examInProgress = false;
    private Timer timer;

    public ExamSystem() {
        // Initialize some questions
        questions.put(1, "What is the capital of France?");
        answers.put(1, "Paris");

        questions.put(2, "What is 2 + 2?");
        answers.put(2, "4");
    }

    public void registerUser(String username, String password, String email) {
        users.add(new User(username, password, email));
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                loggedInUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        loggedInUser = null;
    }

    public void startExam() {
        if (loggedInUser == null) {
            System.out.println("Please login first.");
            return;
        }
        examInProgress = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoSubmit();
            }
        }, 60 * 1000); // Auto-submit after 1 minute

        Scanner scanner = new Scanner(System.in);
        for (Map.Entry<Integer, String> entry : questions.entrySet()) {
            System.out.println("Q: " + entry.getValue());
            String response = scanner.nextLine();
            userResponses.put(entry.getKey(), response);
        }
        timer.cancel();
        autoSubmit();
    }

    private void autoSubmit() {
        examInProgress = false;
        System.out.println("Exam submitted.");
        evaluateExam();
    }

    private void evaluateExam() {
        int score = 0;
        for (Map.Entry<Integer, String> entry : answers.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(userResponses.get(entry.getKey()))) {
                score++;
            }
        }
        System.out.println("You scored: " + score + "/" + questions.size());
    }

    public void updateProfile(String key, String value) {
        if (loggedInUser != null) {
            loggedInUser.updateProfile(key, value);
        } else {
            System.out.println("Please login first.");
        }
    }

    public void changePassword(String newPassword) {
        if (loggedInUser != null) {
            loggedInUser.changePassword(newPassword);
        } else {
            System.out.println("Please login first.");
        }
    }

    public void closeSession() {
        if (examInProgress) {
            System.out.println("Exam in progress. Please finish the exam first.");
        } else {
            logout();
            System.out.println("Session closed.");
        }
    }
}

// Main class to run the program
public class OnlineExamSystem {
    public static void main(String[] args) {
        ExamSystem examSystem = new ExamSystem();

        // Register a user
        examSystem.registerUser("user1", "password", "user1@example.com");

        // Login the user
        if (examSystem.login("user1", "password")) {
            System.out.println("Login successful!");
            // Start the exam
            examSystem.startExam();

            // Update profile
            examSystem.updateProfile("phone", "1234567890");

            // Change password
            examSystem.changePassword("newPassword");

            // Logout
            examSystem.closeSession();
        } else {
            System.out.println("Invalid login.");
        }
    }
}

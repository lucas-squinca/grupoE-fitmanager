package ui;

public interface UserInterface {
    void showMessage(String msg);
    void showError(String msg);
    String getInput(String prompt);
    int showMenu(String title, String[] options);
}
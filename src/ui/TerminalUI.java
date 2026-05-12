package ui;

import java.util.Scanner;

public class TerminalUI implements UserInterface {
    private Scanner scanner;

    public TerminalUI() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void showMessage(String msg) {
        System.out.println("\n[INFO] " + msg);
    }

    @Override
    public void showError(String msg) {
        System.out.println("\n[ERRO] " + msg);
    }

    @Override
    public String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    @Override
    public int showMenu(String title, String[] options) {
        while (true) {
            System.out.println("\n==== " + title.toUpperCase() + " ====");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + " - " + options[i]);
            }
            System.out.print("Escolha uma opção: ");

            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                showError("Entrada inválida. Por favor, digite um número.");
            }
        }
    }
}
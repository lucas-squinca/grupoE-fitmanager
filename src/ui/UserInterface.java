package ui;

import java.util.Scanner;

public class UserInterface {
    // O Scanner é a ferramenta nativa do Java para ler o teclado (o stdin)
    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void showMessage(String msg) {
        System.out.println("\n[INFO] " + msg);
    }

    public void showError(String msg) {
        System.out.println("\n[ERRO] " + msg);
    }
    public String getInput(String prompt) {
        System.out.print(prompt + ": ");
        // nextLine() lê a linha inteira, inclusive espaços. Muito mais seguro que o scanf de C!
        return scanner.nextLine();
    }

    // Mostra um menu, lista as opções e garante que o usuário digite um número válido
    public int showMenu(String title, String[] options) {
        while (true) {
            System.out.println("\n==== " + title.toUpperCase() + " ====");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + " - " + options[i]);
            }
            System.out.print("Escolha uma opção: ");

            String input = scanner.nextLine();

            try {
                // Tenta converter o texto digitado para inteiro
                int choice = Integer.parseInt(input);
                return choice; // Se a conversão deu certo, retorna a opção e sai do loop
            } catch (NumberFormatException e) {
                // Se o usuário digitou uma letra, o Java não "quebra".
                showError("Entrada inválida. Por favor, digite um número.");
            }
        }
    }
}

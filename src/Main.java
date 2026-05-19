import application.FitManager;
import ui.MainMenu;
import ui.UserInterface;
import ui.TerminalUI;
import ui.JOptionPaneUI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserInterface ui = null;
        Scanner scanner = new Scanner(System.in);

        while (ui == null) {
            System.out.println("=== INICIALIZAÇÃO FITMANAGER ===");
            System.out.println("Escolha a interface do sistema:");
            System.out.println("1 - Modo Terminal (Console)");
            System.out.println("2 - Modo Gráfico (Janelas)");
            System.out.print("Opção: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                ui = new TerminalUI();
            } else if (choice.equals("2")) {
                ui = new JOptionPaneUI();
            } else {
                System.out.println("[ERRO] Opção inválida. Tente novamente.\n");
            }
        }

        FitManager fitManager = new FitManager();
        MainMenu mainMenu = new MainMenu(ui, fitManager);
        mainMenu.start();
    }
}
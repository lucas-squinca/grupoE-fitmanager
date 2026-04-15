import application.FitManager;
import ui.MainMenu;
import ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        // 1. Instancia a interface do usuário (a única que conversa com o console)
        UserInterface ui = new UserInterface();

        // 2. Instancia o FitManager do sistema (que já vai criar os serviços lá dentro dele)
        FitManager fitManager = new FitManager();

        // 3. Instancia o menu principal, conectando a interface e o FitManager
        MainMenu mainMenu = new MainMenu(ui, fitManager);

        // 4. Inicia o sistema
        mainMenu.start();
    }
}
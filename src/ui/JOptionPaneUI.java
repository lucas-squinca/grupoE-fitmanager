package ui;

import javax.swing.JOptionPane;

public class JOptionPaneUI implements UserInterface {

    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "FitManager - Info", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "FitManager - Erro", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public String getInput(String prompt) {
        String input = JOptionPane.showInputDialog(null, prompt, "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
        // Se o usuário fechar a janela no 'X'
        if (input == null) {
            return ""; // Retorna string vazia para as validações de serviço barrarem naturalmente
        }
        return input;
    }

    @Override
    public int showMenu(String title, String[] options) {
        while (true) {
            // Monta o texto do menu igual no terminal
            StringBuilder menu = new StringBuilder();
            for (int i = 0; i < options.length; i++) {
                menu.append(i + 1).append(" - ").append(options[i]).append("\n");
            }
            menu.append("\nEscolha uma opção:");

            String input = JOptionPane.showInputDialog(null, menu.toString(), title.toUpperCase(), JOptionPane.PLAIN_MESSAGE);

            // Se o usuário clicar em Cancelar ou no 'X' no menu principal/submenu
            if (input == null) {
                showError("Operação cancelada. Digite uma opção válida para prosseguir.");
                continue; // Volta para o loop do menu
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                showError("Entrada inválida. Por favor, digite um número.");
            }
        }
    }
}
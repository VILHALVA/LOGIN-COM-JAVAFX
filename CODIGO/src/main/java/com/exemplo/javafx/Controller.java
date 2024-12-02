package com.exemplo.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField senhaField;

    @FXML
    protected void handleCadastro() {
        String usuario = usuarioField.getText();
        String senha = senhaField.getText();

        if (usuario.isEmpty() || senha.isEmpty()) {
            mostrarMensagem("Por favor, preencha ambos os campos!", Alert.AlertType.WARNING);
            return;
        }

        boolean sucesso = Database.cadastrarUsuario(usuario, senha);
        if (sucesso) {
            mostrarMensagem("Cadastro realizado com sucesso!", Alert.AlertType.INFORMATION);
        } 
        else {
            mostrarMensagem("Usuário já cadastrado!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    protected void handleLogin() {
        String usuario = usuarioField.getText();
        String senha = senhaField.getText();

        if (usuario.isEmpty() || senha.isEmpty()) {
            mostrarMensagem("Por favor, preencha ambos os campos!", Alert.AlertType.WARNING);
            return;
        }

        boolean validado = Database.validarLogin(usuario, senha);
        if (validado) {
            mostrarMensagem("Bem-vindo, " + usuario + "!", Alert.AlertType.INFORMATION);
        } 
        else {
            mostrarMensagem("Usuário ou senha incorretos.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarMensagem(String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}

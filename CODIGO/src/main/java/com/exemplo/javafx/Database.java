package com.exemplo.javafx;

import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    private static final String DATABASE_NAME = "DATABASE.db";

    public static Connection connect() {
        try {
            File dbFile = new File(DATABASE_NAME);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
                createTables();
            }
            return DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    private static void createTables() {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT NOT NULL,
                senha TEXT NOT NULL
            )
        """;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar tabela", e);
        }
    }

    public static boolean cadastrarUsuario(String usuario, String senha) {
        String sqlCheck = "SELECT * FROM usuarios WHERE usuario = ?";
        String sqlInsert = "INSERT INTO usuarios (usuario, senha) VALUES (?, ?)";

        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
             PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {

            checkStmt.setString(1, usuario);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) return false;  

            String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());

            insertStmt.setString(1, usuario);
            insertStmt.setString(2, senhaCriptografada);
            insertStmt.executeUpdate();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar usuário", e);
        }
    }

    public static boolean validarLogin(String usuario, String senha) {
        String sql = "SELECT senha FROM usuarios WHERE usuario = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String senhaCriptografada = rs.getString("senha");
                return BCrypt.checkpw(senha, senhaCriptografada); 
            } 
            else {
                return false; 
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao validar login", e);
        }
    }
}

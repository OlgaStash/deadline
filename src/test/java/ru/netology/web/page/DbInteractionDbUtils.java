package ru.netology.web.page;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;

import java.sql.*;

public class DbInteractionDbUtils {
    @BeforeEach
    @SneakyThrows
    public static Connection getConnection() {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "olgastash", "!QAZcde30");
        return connection;
    }

    @SneakyThrows
    public static String getVerificationCode(String login) {
        String userId = null;
        String dataSQL = "SELECT id FROM users WHERE login = ?;";
        try (Connection connection = getConnection();
             PreparedStatement idStmt = connection.prepareStatement(dataSQL);
        ) {
            idStmt.setString(1,login);
            try (ResultSet rs = idStmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getString("id");
                }
            }
        }
        String code = null;
        String authCode = "SELECT code FROM auth_codes WHERE user_id = ? order by created desc limit 1;";
        try (Connection connection = getConnection();
             PreparedStatement codeStmt = connection.prepareStatement(authCode);
        ) {
            codeStmt.setString(1, userId);
            try (ResultSet rs = codeStmt.executeQuery()) {
                if (rs.next()) {
                    code = rs.getString("code");
                }
            }
        }
        return code;
    }

    @SneakyThrows
    public static String getStatusFromDb(String login) {
        String statusSQL = "SELECT status FROM users WHERE login = ?;";
        String status = null;
        try (Connection connection = getConnection();
             PreparedStatement statusStm = connection.prepareStatement(statusSQL);
        ) {
            statusStm.setString(1, login);
            try (ResultSet rs = statusStm.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }
        }
        return status;
    }

    @SneakyThrows
    public static void cleanDb() {
        String deleteCards = "DELETE FROM cards WHERE TRUE;";
        String deleteAuthCodes = "DELETE FROM auth_codes WHERE TRUE;";
        String deleteUsers = "DELETE FROM users WHERE TRUE;";
        try (Connection connection = DbInteractionDbUtils.getConnection();
             Statement deleteCardsStmt = connection.createStatement();
             Statement deleteAuthCodesStmt = connection.createStatement();
             Statement deleteUsersStmt = connection.createStatement();
        ) {
            deleteCardsStmt.executeUpdate(deleteCards);
            deleteAuthCodesStmt.executeUpdate(deleteAuthCodes);
            deleteUsersStmt.executeUpdate(deleteUsers);
        }
    }
}

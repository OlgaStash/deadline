package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DbInteractionDbUtils;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnterSystemTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLogin() {
        val loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DbInteractionDbUtils.getVerificationCode(authInfo.getLogin());
        val verify = verificationPage.validVerify(verificationCode);
        verify.shouldBeVisible();
    }


    @Test
    void shouldMake3LoginAndBlock() {
        val loginPage = new LoginPage();
        val InvalidAuthInfo = DataHelper.getInvalidAuthInfo();
        loginPage.invalidLogin(InvalidAuthInfo);
        loginPage.cleanLoginFields();
        loginPage.invalidLogin(InvalidAuthInfo);
        loginPage.cleanLoginFields();
        loginPage.invalidLogin(InvalidAuthInfo);
        val statusSQL = DbInteractionDbUtils.getStatusFromDb(InvalidAuthInfo.getLogin());
        assertEquals("blocked", statusSQL);
    }

    @AfterAll
    static void close() {
        DbInteractionDbUtils.cleanDb();
    }
}

package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;


public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    public void shouldBeVisible() {
        heading.shouldBe(Condition.visible);
    }
}

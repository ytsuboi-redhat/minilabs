package com.redhat.labsjp.sample.at;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.Condition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TodoStepDefinition {

    @Given("TODOのトップ画面を開く")
    public void todoのトップ画面を開く() {
        open("/todo");
    }

    @Then("TODOのトップ画面が表示される")
    public void todoのトップ画面が表示される() {
        $("h1").should(Condition.text("todo-app"));
    }

    @When("トップ画面のボタンをクリックする")
    public void トップ画面のボタンをクリックする() {
        $("#submitButton").click();
    }

    @Then("TODOリストが表示される")
    public void todoリストが表示される() {
        // TODO
        $$(".table tbody tr").shouldHaveSize(2);
    }
}
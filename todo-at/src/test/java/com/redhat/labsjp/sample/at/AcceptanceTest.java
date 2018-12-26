package com.redhat.labsjp.sample.at;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit.TextReport;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:target/cucumber-reports" }, monochrome = true, snippets = SnippetType.UNDERSCORE)
public class AcceptanceTest {

	@ClassRule
	public static TestRule report = new TextReport().onFailedTest(true).onSucceededTest(true);

	@BeforeClass
	public static void beforeClass() {
		// JUL -> SLF4J
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		// Configuration for Selenide
		Configuration.reportsFolder = "target/surefire-reports";
		if (System.getProperty("selenide.browser") == null) {
			Configuration.browser = WebDriverRunner.CHROME;
		}
		Configuration.headless = false;
	}
}

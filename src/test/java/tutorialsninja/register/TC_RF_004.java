package tutorialsninja.register;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC_RF_004 {
	WebDriver driver;

	@BeforeTest
	public void setUp() {
		String url = "https://tutorialsninja.com/demo/";

		WebDriverManager.chromedriver().setup();

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.get(url);
	}

	@Test
	public void register() throws Exception {
		WebElement myAccount = driver.findElement(By.xpath("//div[@id='top-links']//span[.='My Account']"));
		myAccount.click();
		Thread.sleep(3000);

		WebElement register = driver
				.findElement(By.xpath("//ul[@class='dropdown-menu dropdown-menu-right']//a[.='Register']"));
		register.click();
		Thread.sleep(3000);
		
		WebElement continueButton = driver.findElement(By.xpath("//input[@value='Continue']"));
		continueButton.click();
		Thread.sleep(1000);

		
		String firstNameWarningMessage = "First Name must be between 1 and 32 characters!";
		String lastNameWarningMessage = "Last Name must be between 1 and 32 characters!";
		String emailWarningMessage = "E-Mail Address does not appear to be valid!";
		String telephoneWarningMessage = "Telephone must be between 3 and 32 characters!";
		String passwordWarningMessage = "Password must be between 4 and 20 characters!";
		String privacyPolicydWarningMessage = "Warning: You must agree to the Privacy Policy!";
		
		Assert.assertEquals(driver.findElement(By.cssSelector("div[class*='alert']")).getText(), privacyPolicydWarningMessage);
		Assert.assertEquals(driver.findElement(By.xpath("//input[@name='firstname']/following-sibling::div[@class='text-danger']")).getText(), firstNameWarningMessage);
		Assert.assertEquals(driver.findElement(By.xpath("//input[@name='lastname']/following-sibling::div[@class='text-danger']")).getText(), lastNameWarningMessage);
		Assert.assertEquals(driver.findElement(By.xpath("//input[@name='email']/following-sibling::div[@class='text-danger']")).getText(), emailWarningMessage);
		Assert.assertEquals(driver.findElement(By.xpath("//input[@name='telephone']/following-sibling::div[@class='text-danger']")).getText(), telephoneWarningMessage);
		Assert.assertEquals(driver.findElement(By.xpath("//input[@name='password']/following-sibling::div[@class='text-danger']")).getText(), passwordWarningMessage);
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}
}

package tutorialsninja.register;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC_RF_006 {
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

		Faker faker = new Faker();

		WebElement firstname = driver.findElement(By.name("firstname"));
		firstname.clear();
		firstname.sendKeys(faker.name().firstName());
		Thread.sleep(1000);

		WebElement lastname = driver.findElement(By.name("lastname"));
		lastname.clear();
		lastname.sendKeys(faker.name().lastName());
		Thread.sleep(1000);

		WebElement email = driver.findElement(By.name("email"));
		email.clear();
		email.sendKeys(faker.internet().emailAddress());
		Thread.sleep(1000);

		WebElement telephone = driver.findElement(By.name("telephone"));
		telephone.clear();
		telephone.sendKeys(faker.phoneNumber().phoneNumber());
		Thread.sleep(1000);

		WebElement password = driver.findElement(By.name("password"));
		String passwordValue = faker.internet().password();

		password.clear();
		password.sendKeys(passwordValue);
		Thread.sleep(1000);

		WebElement confirmPassword = driver.findElement(By.name("confirm"));
		confirmPassword.clear();
		confirmPassword.sendKeys(passwordValue);
		Thread.sleep(1000);
		
        WebElement yesNewsletter = driver.findElement(By.xpath("//input[@type='radio'][@name='newsletter'][@value='0']"));
        yesNewsletter.click();
        Thread.sleep(1000);

		WebElement agreeCheckbox = driver.findElement(By.name("agree"));
		agreeCheckbox.click();
		Thread.sleep(1000);

		WebElement continueButton = driver.findElement(By.xpath("//input[@value='Continue']"));
		continueButton.click();
		Thread.sleep(1000);

		WebElement successHeader = driver.findElement(By.xpath("//div[@id='content']/h1"));
		Assert.assertTrue(successHeader.getText().equals("Your Account Has Been Created!"), "Registration failed");
		Thread.sleep(1000);

		WebElement successContinueButton = driver.findElement(By.xpath("//div[@class='buttons']/div/a"));
		successContinueButton.click();
		Thread.sleep(1000);

		Assert.assertEquals(driver.getTitle(), "My Account");

		WebElement subscribeUnsubscribeNewsletter = driver
				.findElement(By.linkText("Subscribe / unsubscribe to newsletter"));
		subscribeUnsubscribeNewsletter.click();
		Thread.sleep(1000);
		
		Assert.assertTrue(driver.findElement(By.xpath("//ul[@class='breadcrumb']//a[.='Newsletter']")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.xpath("//input[@name='newsletter'][@value='0']")).isSelected());
		
		continueButton = driver.findElement(By.xpath("//input[@value='Continue']"));
		continueButton.click();
		Thread.sleep(1000);

		WebElement logout = driver.findElement(By.xpath("//aside/div/a[.='Logout']"));
		logout.click();
		Thread.sleep(1000);

		WebElement logoutHeader = driver.findElement(By.xpath("//div[@id='content']/h1"));
		Assert.assertTrue(logoutHeader.getText().equals("Account Logout"), "Logout failed");
		Thread.sleep(1000);

		WebElement logoutContinueButton = driver.findElement(By.xpath("//div[@class='buttons']/div/a"));
		logoutContinueButton.click();
		Thread.sleep(1000);
	}

	@AfterTest
	public void tearDown() {
//		driver.quit();
	}
}

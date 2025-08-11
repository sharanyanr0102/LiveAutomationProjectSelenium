package tutorialsninja.register;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
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
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC_RF_002 {
	WebDriver driver;

	@BeforeTest
	public void setUp() {
		String url = "https://www.amazon.in/";

		WebDriverManager.chromedriver().setup();

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.get(url);
	}

	@Test
	public void register() throws Exception {
		String username = "sharanyanagoorravindra@gmail.com";

		List<WebElement> continueButtons = driver.findElements(By.className("a-button-text"));
		if (continueButtons.size() != 0)
			continueButtons.get(0).click();
		Thread.sleep(1000);

		Actions actions = new Actions(driver);
		WebElement signIn = driver.findElement(By.xpath("//span[.='Hello, sign in']"));
		actions.moveToElement(signIn).build().perform();

		WebElement signInButton = driver.findElement(By.xpath("//span[.='Sign in']"));
		signInButton.click();
		Thread.sleep(1000);

		WebElement emailInput = driver.findElement(By.id("ap_email_login"));
		emailInput.clear();
		emailInput.sendKeys(username);
		Thread.sleep(1000);

		WebElement continueButton = driver.findElement(By.className("a-button-input"));
		continueButton.click();
		Thread.sleep(1000);

		WebElement forgotPassword = driver.findElement(By.id("auth-fpp-link-bottom"));
		forgotPassword.click();
		Thread.sleep(1000);

		continueButton = driver.findElement(By.className("a-button-input"));
		continueButton.click();
		Thread.sleep(5000);

		String appPassword = "qxzd bpub pswz bjnc";
		String link = null;

		System.out.println("Halting the program intentionally for 10 seconds.");

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Gmail IMAP configuration
		String host = "imap.gmail.com";
		String port = "993";
		String expectedSubject = "amazon.in: Password recovery";
		String expectedFromEmail = "\"amazon.in\" <account-update@amazon.in>";
		String expectedBodyContent = "Someone is attempting to reset the password of your account.";

		try {
			// Mail server connection properties
			Properties properties = new Properties();
			properties.put("mail.store.protocol", "imaps");
			properties.put("mail.imap.host", host);
			properties.put("mail.imap.port", port);
			properties.put("mail.imap.ssl.enable", "true");

			// Connect to the mail server
			Session emailSession = Session.getDefaultInstance(properties);
			Store store = emailSession.getStore("imaps");
			store.connect(host, username, appPassword); // replace email password with App password

			// Open the inbox folder
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			// Search for unread emails
			Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

			boolean found = false;
			String verificationCode = null;
			for (int i = messages.length - 1; i >= 0; i--) {

				Message message = messages[i];

				if (message.getSubject().contains(expectedSubject)) {
					found = true;
					Assert.assertEquals(message.getSubject(), expectedSubject);
					Assert.assertEquals(message.getFrom()[0].toString(), expectedFromEmail);
					String actualEmailBody = getTextFromMessage(message);
					Assert.assertTrue(actualEmailBody.contains(expectedBodyContent));
					
					System.out.println(actualEmailBody);
					
					// Regular expression to find 6-digit code
			        Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
			        Matcher matcher = pattern.matcher(actualEmailBody);

			        if (matcher.find()) {
			            verificationCode = matcher.group();
			            System.out.println("Verification Code: " + verificationCode);
			        } else {
			            System.out.println("No 6-digit verification code found.");
			        }

					break;
				}
			}
			
			// Close the store and folder objects
			inbox.close(false);
			store.close();

			if (!found || verificationCode.equals(null)) {
				System.out.println("No confirmation email found or no verification code available");
			}
			
			else {
				WebElement otpBox = driver.findElement(By.id("input-box-otp"));
				otpBox.sendKeys(verificationCode);
				Thread.sleep(1000);
				
				WebElement submitButton = driver.findElement(By.className("a-button-input"));
				submitButton.click();
				Thread.sleep(1000);
			}
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

//		driver.navigate().to(link);

//		Assert.assertTrue(driver.findElement(By.name("customerResponseDenyButton")).isDisplayed());
	}

	@AfterTest
	public void tearDown() {
//		driver.quit();
	}

	private static String getTextFromMessage(Message message) throws Exception {
		String result = "";
		if (message.isMimeType("text/plain")) {
			result = message.getContent().toString();
		} else if (message.isMimeType("text/html")) {
			result = message.getContent().toString();
		} else if (message.isMimeType("multipart/*")) {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			result = getTextFromMimeMultipart(mimeMultipart);
		}
		return result;
	}

	private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
		StringBuilder result = new StringBuilder();
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result.append(bodyPart.getContent());
			} else if (bodyPart.isMimeType("text/html")) {
				result.append(bodyPart.getContent());
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
			}
		}
		return result.toString();
	}
}

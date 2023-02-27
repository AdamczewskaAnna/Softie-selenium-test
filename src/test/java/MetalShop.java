import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.By.*;

public class MetalShop {

    static WebDriver driver = new ChromeDriver();
    String password = "test123"; //deklaracja zmiennej
    String username = "user";

    @BeforeAll
    public static void setUp() {
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));
    driver.navigate().to("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772");
    }

    @AfterAll
    public static void closeBrowse() {
        driver.quit();
    }

    @BeforeEach
    public void goToHomePage() {
        driver.findElement(By.xpath("//a[text()='Softie Metal Shop']")).click();
    }


    @Test
    public void emptyLogin() {
        driver.findElement(linkText("Moje konto")).click();
        driver.findElement(id("password")).sendKeys(password);
        driver.findElement(name("login")).click();
        String error = driver.findElement(cssSelector(".woocommerce-error")).getText();
        Assertions.assertEquals("Błąd: Nazwa użytkownika jest wymagana.", error);
    }

    @Test
    public void emptyPassword() {
        driver.manage().window().maximize();
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772");
        driver.findElement(linkText("Moje konto")).click();
        driver.findElement(id("username")).sendKeys(username);
        driver.findElement(name("login")).click();
        String error = driver.findElement(cssSelector(".woocommerce-error")).getText();
        Assertions.assertEquals("Błąd: pole hasła jest puste.", error);
    }

    @Test
    public void registerSuccess() {
        driver.manage().window().maximize();
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772");
        driver.findElement(By.linkText("register")).click();
        Faker faker = new Faker();
        String registerUserName = faker.name().username();
        String email = registerUserName + faker.random().nextInt(1000) + "@gmail.com";
        driver.findElement(cssSelector("#user_login")).sendKeys(registerUserName);
        driver.findElement(cssSelector("#user_email")).sendKeys(email);
        driver.findElement(cssSelector("#user_pass")).sendKeys(password);
        driver.findElement(cssSelector("#user_confirm_password")).sendKeys(password);
        driver.findElement(cssSelector(".ur-submit-button")).click();
        Wait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(cssSelector(".user-registration-message")));
        WebElement error = driver.findElement(cssSelector(".user-registration-message"));
        Assertions.assertEquals("User successfully registered.", error.getText());
    }


    @Test
    public void checkLogo() {
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/");
        WebElement logo = driver.findElement(By.xpath("//*[@class ='site-branding']"));
        WebElement search = driver.findElement(By.xpath("//*[@id ='woocommerce-product-search-field-0']"));
        Assertions.assertTrue(logo.isDisplayed());
        Assertions.assertTrue(search.isDisplayed());
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/moje-konto/");
        WebElement logo2 = driver.findElement(By.xpath("//*[@class ='site-branding']"));
        WebElement search2 = driver.findElement(By.xpath("//*[@id ='woocommerce-product-search-field-0']"));
        Assertions.assertTrue(logo2.isDisplayed());
        Assertions.assertTrue(search2.isDisplayed());
    }

    @Test
    public void goToContact() {
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/");
        WebElement goToContact = driver.findElement(By.xpath("//a[text()='Kontakt']"));
        Assertions.assertTrue(goToContact.isDisplayed());
        goToContact.click();
        Assertions.assertEquals("Kontakt - Softie Metal Shop", driver.getTitle());
    }

    @Test
    public void goBackToMainPage() {
        driver.navigate().to("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/");
        driver.findElement(By.linkText("Moje konto")).click();
        Wait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.navigate().back();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("woocommerce-result-count")));
        String result = driver.findElement(By.className("woocommerce-result-count")).getText();
        Assertions.assertEquals("Wyświetlanie wszystkich wyników: 9", result);
    }

    @Test
    public void contact() {
        WebElement contact = driver.findElement(By.id("menu-item-132"));
        contact.click();
        Faker faker = new Faker();
        String name = faker.name().firstName() + faker.name().lastName();
        String email = name + faker.random().nextInt(1000) + "@gmail.com";
        String subject = faker.lorem().fixedString(5);
        String message = faker.bothify("???###aaaa");
        driver.findElement(By.name("your-name")).sendKeys(name);
        driver.findElement(By.name("your-email")).sendKeys(email);
        driver.findElement(By.name("your-subject")).sendKeys(subject);
        driver.findElement(By.tagName("textarea")).sendKeys(message);
        WebElement submitButton = driver.findElement((By.cssSelector(".wpcf7-submit")));
        submitButton.click();
        Wait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("wpcf7-response-output")));
        WebElement sendMessage = driver.findElement(By.className("wpcf7-response-output"));
        String sendMessageVerification = "Twoja wiadomość została wysłana. Dziękujemy!";
        Assertions.assertTrue(sendMessage.getText().contains(sendMessageVerification));

    }

    @Test
    public void addProductToCart() {
        driver.manage().window().maximize();
        driver.navigate().to("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/");
        driver.findElement(By.linkText("Koszyk")).click();
        WebElement emptyCart = driver.findElement(By.className("cart-empty"));
        String sendMessageVerification = "Twój koszyk aktualnie jest pusty.";
        Assertions.assertTrue(emptyCart.getText().contains(sendMessageVerification));
        driver.findElement(By.linkText("Wróć do sklepu")).click();
        driver.findElement(By.cssSelector("a[data-product_id]")).click();
        Wait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Zobacz koszyk")));
        driver.findElement(By.linkText("Zobacz koszyk")).click();
        String addProductToCart = driver.findElement(By.linkText("Srebrna moneta 5g - UK 1980")).getText();
        Assertions.assertEquals(addProductToCart, "Srebrna moneta 5g - UK 1980");
    }


    @Test
    public void removeProduct() {
        driver.manage().window().maximize();
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/");
        driver.findElement(By.cssSelector("a[data-product_id='24']")).click();
        Wait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Zobacz koszyk")));
        driver.findElement(By.linkText("Zobacz koszyk")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("remove")));
        driver.findElement(By.className("remove")).click();
        String removeProduct = driver.findElement(By.cssSelector(".woocommerce-message")).getText();
        Assertions.assertEquals(removeProduct, "Usunięto: „Srebrna moneta 5g - UK 1980”. Cofnij?");
    }
}












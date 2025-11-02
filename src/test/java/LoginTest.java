import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import utils.TestConfig;

public class LoginTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;
    private LoginPage loginPage;

//    BeforeClass -> chạy 1 lần duy nhất khi start project
    @BeforeClass
    public void setupClass(){
        playwright = Playwright.create();
        browser = TestConfig.getBrowserType(playwright)
                            .launch(TestConfig.getBrowserLauchOptions());
    }

    @BeforeMethod
    public void setup() {
        browserContext = browser.newContext(TestConfig.getNewContextOptions());
        page = browserContext.newPage();
        loginPage = new LoginPage(page);
    }

    @AfterMethod
    public void tearDownTest() {
        if(browserContext != null) {
            browserContext.close();
        }
    }

    @AfterClass
    public void tearDownClass() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    public void testLoginWithValidUser() {
//      Navigate vao page login
//        -------
//        page.navigate("https://demo6.cybersoft.edu.vn/login");
//        page.waitForLoadState();
//        => mang qua Login Page
        loginPage.navigateToLoginPage();
//        -------

//        -------
//        loginPage.enterUsername("string");
//        loginPage.enterPassword("123456");
//        => TestConfig
        String username = TestConfig.getValidUsername();
        String password = TestConfig.getValidPassword();
//        --------
//        loginPage.clickLoginButton();
//        => Login Page
        loginPage.login(username, password);

//        chờ vài giây de xu ly trang login
        page.waitForTimeout(3000);
        boolean isLoginSuccess = loginPage.isLoginSuccessfull();
        Assert.assertTrue(isLoginSuccess);
    }

//    test case voi invalid user
    @Test
    public void testLoginWithInvalidUser() {
        loginPage.navigateToLoginPage();

        String invalidUserName = TestConfig.getInvalidUsername();
        String invalidPassword = TestConfig.getInvalidPassword();

        loginPage.login(invalidUserName, invalidPassword);
        boolean hasError = loginPage.hasErrorMessage();
        page.waitForTimeout(3000);
        Assert.assertTrue(hasError);

    }
}

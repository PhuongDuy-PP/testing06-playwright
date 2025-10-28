package pages;

import com.microsoft.playwright.Page;
import utils.TestConfig;

public class LoginPage extends BasePage {
    private static final String USERNAME_INPUT = "input[name='username'], input[type='text']";
    private static final String PASSWORD_INPUT = "input[name='password']";
    private static final String LOGIN_BUTTON = "button[type='submit']";
    public LoginPage(Page page) {
        super(page);
    }

//    GROUP các function test LOGIN PAGE
//    tạo hàm navigate login page
    public void navigateToLoginPage() {
        String baseUrl = TestConfig.getBaseUrl();
        page.navigate(baseUrl + "/login");
        page.waitForLoadState();
    }

//    tạo hàm enter username
    public void enterUsername(String username) {
//        chờ load page login đến khi có element của username
        page.waitForSelector(USERNAME_INPUT);

//        fill data vào input selector
        page.fill(USERNAME_INPUT, username);
        System.out.println("Da fill username vao input");
    }

    public void enterPassword(String password) {
        page.waitForSelector(PASSWORD_INPUT);
        page.fill(PASSWORD_INPUT, password);
        System.out.println("Da fill password vao input");
    }

    public void clickLoginButton() {
        page.waitForSelector(LOGIN_BUTTON);
        page.click(LOGIN_BUTTON);
        System.out.println("Da click login button Dang nhap");
    }

//    viết hàm login để gom các logic như enter user, passoword, click button
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        page.waitForLoadState();
    }

}

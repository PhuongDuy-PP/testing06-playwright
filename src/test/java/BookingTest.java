import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.BookingPage;
import pages.LoginPage;
import utils.TestConfig;

public class BookingTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    private LoginPage loginPage;
    private BookingPage bookingPage;

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
        bookingPage = new BookingPage(page);
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

//    Test case booking appointment
    @Test
    public void testBookingFlow() {
//        B1: Login with patient1
//        Luu y: Vi da co object LoginPage nen se dung lai ham
//        navigateToLoginPage va login
        loginPage.navigateToLoginPage();
        String validPatientUser = TestConfig.getValidPatient();
        String validPatientPass = TestConfig.getValidPatientPass();
        loginPage.login(validPatientUser, validPatientPass);
        page.waitForTimeout(2000);

//        B2: Open booking page from menu
        bookingPage.openBookingFromMenu();
        page.waitForTimeout(2000);
//        B3: select branch "Chi nhanh trung tam"
        bookingPage.selectBranch("Chi nhánh trung tâm");
        page.waitForTimeout(4000);
//        B4: select doctor "User Fullname 6"

        bookingPage.selectDoctor("Dr. User Fullname 6 - Neurology");
        page.waitForTimeout(2000);
//        B5: Pick date November 2, 2025
        bookingPage.pickDateAppointment("November 2, 2025");

//        B6: Pick time slot: 06:00 - 17:00
        bookingPage.pickTimeSlot("06:00 - 17:00");

//        B7: Choose package
        bookingPage.choosePackage();

//        B8: submit appoinment
        bookingPage.submitBooking();

        page.waitForTimeout(4000);
        String currentUrl = page.url();
        Assert.assertTrue(currentUrl.endsWith("appointments"));
    }

//    test case scroll
    @Test
    public void testScrollUpAndDown() {
        loginPage.navigateToLoginPage();
        String validPatientUser = TestConfig.getValidPatient();
        String validPatientPass = TestConfig.getValidPatientPass();
        loginPage.login(validPatientUser, validPatientPass);
        page.waitForTimeout(2000);

        bookingPage.openBookingFromMenu();
        page.waitForTimeout(2000);

//        scroll down
//        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
//        scroll smooth
        page.evaluate("window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'})");
        page.waitForTimeout(3000);

//        scroll up
        page.evaluate("window.scrollTo(0,0)");
        page.waitForTimeout(3000);

//        scroll xuong giua trang
        page.evaluate("window.scrollTo(0, document.body.scrollHeight/2)");
        page.waitForTimeout(3000);

        page.evaluate("window.scrollTo(0,0)");
        page.waitForTimeout(3000);

//        scroll toi button Dat lich kham
        bookingPage.scrollToBookingButton();

        Assert.assertTrue(true);
    }
}

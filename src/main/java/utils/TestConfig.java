package utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class TestConfig {
    private static Properties properties;
    private static final String CONFIG_FILE = "src/main/resources/config.properties";
//    load config properties ngay khi tao xong class TestConfig
    static {
        loadProperties();
    }

//    Load properties tu config file
    private static void loadProperties() {
        properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(CONFIG_FILE);
            properties.load(input);
            input.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

//    Viet ham lay property
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

//    Viet ham lay browser type
    public static BrowserType getBrowserType(Playwright playwright) {
        String browserType = getProperty("browser");
        if(browserType.equals("chrome")) {
            return playwright.chromium();
        }
        return playwright.chromium();
    }

//    Viet ham setup width height browser test
    public static Browser.NewContextOptions getNewContextOptions() {
//        setViewportSize(null): tat setting view size co dinh, cho phep browser auto dung kich thuoc
//        man hinh that
//        setIgnoreHTTPSErrors(true): bo qua loi lien quan toi SSL/https
//        CHI DUOC DUNG TRONG MOI TRUONG TEST, KHONG DUOC DUNG TRONG PRODUCTION
        return new Browser.NewContextOptions()
                .setViewportSize(null)
                .setIgnoreHTTPSErrors(true);
    }

    public static BrowserType.LaunchOptions getBrowserLauchOptions() {
        return new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false)
                .setArgs(Arrays.asList(
                        "--start-maximized"
                ));
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getValidUsername() {
        return getProperty("test.username");
    }

    public static String getValidPassword() {
        return getProperty("test.password");
    }

    public static  String getInvalidUsername() {
        return getProperty("test.invalid.username");
    }

    public static String getInvalidPassword() {
        return getProperty("test.invalid.password");
    }

    public static String getValidPatient() {
        return getProperty("patient.username");
    }

    public static String getValidPatientPass() {
        return getProperty("patient.password");
    }
}

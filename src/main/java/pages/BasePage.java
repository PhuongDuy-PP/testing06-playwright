package pages;

import com.microsoft.playwright.Page;

// class truu tuonng (abstract class) chi de cho class con ke thua
// khong duoc dung de tao doi tuong
public abstract class BasePage {
   protected final Page page;
   protected final String baseUrl = "https://demo6.cybersoft.edu.vn";

   public BasePage(Page page) {
       this.page = page;
   }


}

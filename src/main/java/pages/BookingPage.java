package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

public class BookingPage extends BasePage {
//    <a class="nav-link text-secondary" href="/booking" data-discover="true">Đặt lịch khám</a>
    private static final String BOOKING_MENU_LINK = "a[href='/booking']";

//    <div class="mb-3">
//        <label class="form-label">Chi nhánh</label>
//        <select class="form-select">
//            <option value="">Chọn chi nhánh</option>
//            <option value="1">Chi nhánh trung tâm</option>
//        </select>
//    </div>
//    div.mb-3:has(> label.form-label:has-text('Chi nhánh')) select.form-select
//    Vi sao dung selector phuc tap:
//    1. Do page co nhieu dropdown
//    2. Cần tìm chính xác dropdown "Chi nhánh" qua label (label là duy nhất)
//    3. Tránh nham lan voi dropdown khac
//    div.mb-3: Thẻ div có class mb-3
//    :has(...): div phải chứa element bên trong
//    > label.form-label: Con trực tiếp là label có class form-label
//    :has-text('Chi nhánh'): Label phai chua text 'Chi nhanh'

    private static final String BRANCH_SELECT = "div.mb-3:has(> label.form-label:has-text('Chi nhánh')) select.form-select";

//    <div class="mb-3">
//        <label class="form-label">Bác sĩ</label>
//        <select class="form-select">
//            <option value="">Chọn bác sĩ</option>
//            <option value="3">Dr. User Fullname 6 - Neurology</option>
//        </select>
//    </div>
//    div.mb-3:has(> label.form-label:has-text('Bác sĩ')) select.form-select
    private static final String DOCTOR_SELECT = "div.mb-3:has(> label.form-label:has-text('Bác sĩ')) select.form-select";
//    locator dự phòng khi DOCTOR_SELECT không tìm thấy

//    locator appoinment date
//    %s: placeholder dung de truyen bien (variable) vao trong chuoi
    private static final String CALENDAR_DAY = "abbr[aria-label='%s']";

//    <button aria-label="" class="react-calendar__navigation__label" type="button" style="flex-grow: 1;">
//        <span class="react-calendar__navigation__label__labelText react-calendar__navigation__label__labelText--from">November 2025</span>
//    </button>
    private static final String CALENDAR_MONTH_LABEL = ".react-calendar__navigation__label__labelText--from";
    private static final String APPPOINTMENT_DATE = "//button[.//abbr[contains(@aria-label, '%s') and text()='%s']]";

    private static final String TIME_SLOT_DOCTOR_BUTTON = "//button[@class='text-start btn btn-outline-primary']";
    private static final String CHOOSE_PACKAGE_BUTTON = "button:has-text('Chọn gói này')";
    private static final String SUBMIT_BOOKING_BUTTON = "button[type='submit']:has-text('Đặt lịch khám')";
    public BookingPage(Page page) {
        super(page);
    }

//    viet ham openBookingFromMenu
    public void openBookingFromMenu() {
//        B1: Doi load trang den khi xuat hien element menu "Dat lich kham"
        page.waitForSelector(BOOKING_MENU_LINK);
//        B2: click vao menu "Dat lich kham"
        page.click(BOOKING_MENU_LINK);
//        B3: Doi load trang
        page.waitForTimeout(1000);
    }

//    viet ham selectBranch
    public void selectBranch(String branchOption) {
//        B1: Tìm locator select Chi nhanh
        Locator branchSelect = page.locator(BRANCH_SELECT);
//        B2: Doi select hien thi
//        Luu y: Vì các option trong select được lấy từ API nên phải đợi frontend
//        mapping data lên thẻ select
        branchSelect.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

//        B3: select vao option can chon (Chi nhanh trung tam)
        branchSelect.selectOption(new SelectOption().setLabel(branchOption));
//        B4: Doi load select
        page.waitForTimeout(2000);
    }

    public void selectDoctor(String doctorOption) {
//        B1: Tim dropdown
        Locator doctorSelect = page.locator(DOCTOR_SELECT);
//        doctorSelect.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
//        doctorSelect.selectOption(new SelectOption().setLabel(doctorOption));
//        page.waitForTimeout(2000);
//      B2: Doi dropdown xuat hien tren HTML
        doctorSelect.waitFor();

//        B3: Chon option theo ten
        doctorSelect.selectOption(doctorOption);

//        B4: Doi trang xu ly xong
        page.waitForLoadState();
    }

    public void pickDateAppointment(String dateText) {
//        dateText: November 2, 2025
//        B1: Tim thang, nam de chon ngay kham
//        convert November 2, 2025 => November 2025
//        Cach lam: tach ngay, thang, nam ra tung phan => split
        String[] parts = dateText.split(" ");
//        ["November", "2,", "2025"]
//        ["November", "2025"]
        String targetMonth = parts[0] + " " + parts[2];
//        trim(): xoa nhung khoang trang thua
        String dayNum = parts[1].replace(",","").trim();

//        do ket qua tra ve locator() la list nen dung first()
//        de lay ket qua dau tien
        Locator monthLabel = page.locator(CALENDAR_MONTH_LABEL).first();

        monthLabel.waitFor();

//        String date = String.format(APPPOINTMENT_DATE, targetMonth, dayNum);
//        System.out.println("");
//        Tim ngay dat lich kham
        Locator dateButton = page.locator(String.format(APPPOINTMENT_DATE, targetMonth, dayNum))
                .first();

        dateButton.waitFor();

        if(dateButton.count() == 0) {
            throw new RuntimeException("Khong tim thay ngay: " + dateText);
        }
        dateButton.click();
    }

    public void pickTimeSlot(String timeSlotText) {
        String timeSlotLocator = String.format(TIME_SLOT_DOCTOR_BUTTON, timeSlotText);
        System.out.println(timeSlotLocator);
        page.waitForSelector(timeSlotLocator);
        page.click(timeSlotLocator);
//        page.click("text='" + timeSlotText + "'");
        page.waitForTimeout(1000);
    }

    public void choosePackage() {
        page.waitForSelector(CHOOSE_PACKAGE_BUTTON);
        page.locator(CHOOSE_PACKAGE_BUTTON).first().click();
        page.waitForTimeout(1000);
    }

    public void submitBooking() {
        page.waitForSelector(SUBMIT_BOOKING_BUTTON);
        page.click(SUBMIT_BOOKING_BUTTON);
        page.waitForLoadState();
        page.waitForTimeout(2000);
    }

    public void scrollToBookingButton() {
        Locator bookingButton = page.locator(SUBMIT_BOOKING_BUTTON);
        bookingButton.evaluate("el => el.scrollIntoView({behavior: 'smooth', block: 'center'})");
        page.waitForTimeout(2000);
    }
}

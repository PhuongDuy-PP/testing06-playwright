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
    private static final String DOCTOR_SELECT_FALLBACK = "select.form-select";
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
        Locator doctorSelect = page.locator(DOCTOR_SELECT);
        doctorSelect.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        doctorSelect.selectOption(new SelectOption().setLabel(doctorOption));
        page.waitForTimeout(2000);
    }
}

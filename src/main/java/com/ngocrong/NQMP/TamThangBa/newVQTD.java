/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.NQMP.TamThangBa;

import com.ngocrong.item.Item;
import com.ngocrong.item.ItemOption;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;

/**
 *
 * @author Administrator
 */
public class newVQTD {

    public static boolean reward(Player player) {
        if (player == null) {
            return false;
        }

        // Kiểm tra xem có đủ chỗ trống trong túi đồ không
        if (player.getCountEmptyBag() < 1) {
            player.service.sendThongBao("Hành trang không đủ chỗ trống!");
            return false;
        }

        try {
            // Lựa chọn phần thưởng ngẫu nhiên theo tỉ lệ
            int randomValue = Utils.nextInt(10300); // Sử dụng 10000 để có thể xử lý tỉ lệ nhỏ chính xác hơn
            int cumulativeProbability = 0;

            // 1. Sao pha lê - 5%
            cumulativeProbability += 500;
            if (randomValue < cumulativeProbability) {
                Item spl = createSPL();
                player.boxCrackBall.add(spl);
                return true;
            }

            // 2. Ngọc Rồng 4-7 sao - 3%
            cumulativeProbability += 300;
            if (randomValue < cumulativeProbability) {
                Item nr = createNR();
                player.boxCrackBall.add(nr);
                return true;
            }

            // 3. Vàng 5-20tr - 67%
            cumulativeProbability += 6700;
            if (randomValue < cumulativeProbability) {
                Item goldItem = new Item(190);
                int gold = 5000000 + Utils.nextInt(15000001); // 5tr đến 20tr
                goldItem.quantity = gold;
                player.boxCrackBall.add(goldItem);
                return true;
            }

            // 4. Cải trang Bạch Ngọc Tiên - 2%
            cumulativeProbability += 200;
            if (randomValue < cumulativeProbability) {
                Item bachNgocTien = createBachNgocTien();
                player.boxCrackBall.add(bachNgocTien);
                return true;
            }

            // 5. Đá nâng cấp - 3%
            cumulativeProbability += 300;
            if (randomValue < cumulativeProbability) {
                Item dnc = createDNC();
                player.boxCrackBall.add(dnc);
                return true;
            }

            // 6. Cải trang Hắc Long Saiyan - 2%
            cumulativeProbability += 200;
            if (randomValue < cumulativeProbability) {
                Item hacLongSaiyan = createHacLongSaiyan();
                player.boxCrackBall.add(hacLongSaiyan);
                return true;
            }

            // 7. Cải trang Thiên Tài Công Nghệ - 2%
            cumulativeProbability += 200;
            if (randomValue < cumulativeProbability) {
                Item thienTaiCongNghe = createThienTaiCongNghe();
                player.boxCrackBall.add(thienTaiCongNghe);
                return true;
            }

            // 8. Thú cưỡi Hắc Kỳ Lân (Saiyan Cuồng Nộ) - 2%
            cumulativeProbability += 200;
            if (randomValue < cumulativeProbability) {
                Item hacKyLan = createHacKyLan();
                player.boxCrackBall.add(hacKyLan);
                return true;
            }

            // 9. Pet Tiểu Xà Vương (Thần trả hủy diệt) - 4%
            cumulativeProbability += 400;
            if (randomValue < cumulativeProbability) {
                Item tieuXaVuong = createTieuXaVuong();
                player.boxCrackBall.add(tieuXaVuong);
                return true;
            }

            // 10. Đeo lưng Cờ Ngọc Rồng 2 Sao - 3%
            cumulativeProbability += 300;
            if (randomValue < cumulativeProbability) {
                Item coNgocRong = createCoNgocRong();
                player.boxCrackBall.add(coNgocRong);
                return true;
            }

            // 11. Đeo lưng Khỉ con vui vẻ - 3%
            cumulativeProbability += 300;
            if (randomValue < cumulativeProbability) {
                Item khiConVuiVe = createKhiConVuiVe();
                player.boxCrackBall.add(khiConVuiVe);
                return true;
            }

            // 12. Nước giải khát - 3%
            cumulativeProbability += 300;
            if (randomValue < cumulativeProbability) {
                Item nuocGiaiKhat = createNuocGiaiKhat();
                player.boxCrackBall.add(nuocGiaiKhat);
                return true;
            }
            cumulativeProbability += 300;
            if (randomValue < cumulativeProbability) {
                Item kemTraiCay = createKemTraiCay();
                player.boxCrackBall.add(kemTraiCay);
                return true;
            }
            // 14. Linh thú Tiểu Miêu Linh - 1%
            cumulativeProbability += 100;
            if (randomValue < cumulativeProbability) {
                Item tieuMieuLinh = createTieuMieuLinh();
                player.boxCrackBall.add(tieuMieuLinh);
                return true;
            }

            // Trường hợp mặc định (nếu có lỗi trong tính toán tỉ lệ)
            Item goldItem = new Item(190);
            int gold = 5000000 + Utils.nextInt(15000001); // 5tr đến 20tr
            goldItem.quantity = gold;
            player.boxCrackBall.add(goldItem);
            return true;

        } catch (Exception e) {
            player.service.sendThongBao("Có lỗi xảy ra khi nhận phần thưởng!");
            return false;
        }
    }

    /**
     * Tạo cải trang Bạch Ngọc Tiên
     *
     * @return Cải trang Bạch Ngọc Tiên
     */
    private static Item createBachNgocTien() {
        // ID cải trang Bạch Ngọc Tiên
        final int BACH_NGOC_TIEN_ID = 2246;

        // ID options
        final int OPT_HP = 77;
        final int OPT_KI = 103;
        final int OPT_SD = 50;
        final int OPT_TNSM = 101; // Sửa từ 19 thành 101 theo yêu cầu

        Item bachNgocTien = new Item(BACH_NGOC_TIEN_ID);
        bachNgocTien.quantity = 1;

        // Thêm chỉ số
        bachNgocTien.options.add(new ItemOption(OPT_HP, 10)); // 10% HP
        bachNgocTien.options.add(new ItemOption(OPT_KI, 10)); // 10% KI
        bachNgocTien.options.add(new ItemOption(OPT_SD, 10)); // 10% SĐ
        bachNgocTien.options.add(new ItemOption(OPT_TNSM, 30 + Utils.nextInt(71))); // 30-100% TNSM

        // 100% HSD (1,3,5,7 ngày)
        int[] days = {1, 3, 5, 7};
        int dayIndex = Utils.nextInt(days.length);
        bachNgocTien.options.add(new ItemOption(93, days[dayIndex])); // Option 93: số ngày HSD

        return bachNgocTien;
    }

    /**
     * Tạo cải trang Hắc Long Saiyan
     *
     * @return Cải trang Hắc Long Saiyan
     */
    private static Item createHacLongSaiyan() {
        // ID cải trang Hắc Long Saiyan
        final int HAC_LONG_SAIYAN_ID = 2247;

        // ID options
        final int OPT_HP = 77;
        final int OPT_KI = 103;
        final int OPT_SD = 50;
        final int OPT_GIAP = 94;

        Item hacLongSaiyan = new Item(HAC_LONG_SAIYAN_ID);
        hacLongSaiyan.quantity = 1;

        // Thêm chỉ số
        hacLongSaiyan.options.add(new ItemOption(OPT_HP, 24 + Utils.nextInt(7))); // 24-30% HP
        hacLongSaiyan.options.add(new ItemOption(OPT_KI, 24 + Utils.nextInt(7))); // 24-30% KI
        hacLongSaiyan.options.add(new ItemOption(OPT_SD, 24 + Utils.nextInt(7))); // 24-30% SĐ
        hacLongSaiyan.options.add(new ItemOption(OPT_GIAP, 10 + Utils.nextInt(6))); // 10-15% Giáp
        // 10% VV, 90% HSD
        if (Utils.isTrue(10, 100)) {
            // Vĩnh viễn - không thêm option HSD
        } else {
            // HSD ngẫu nhiên 1-30 ngày
            hacLongSaiyan.options.add(new ItemOption(93, getDays())); // Option 93: số ngày HSD
        }

        return hacLongSaiyan;
    }

    /**
     * Tạo cải trang Thiên Tài Công Nghệ
     *
     * @return Cải trang Thiên Tài Công Nghệ
     */
    private static Item createThienTaiCongNghe() {
        // ID cải trang Thiên Tài Công Nghệ
        final int THIEN_TAI_CONG_NGHE_ID = 2248;

        // ID options
        final int OPT_HP = 77;
        final int OPT_KI = 103;
        final int OPT_SD = 50;
        final int OPT_CHI_MANG = 14;

        Item thienTaiCongNghe = new Item(THIEN_TAI_CONG_NGHE_ID);
        thienTaiCongNghe.quantity = 1;

        // Thêm chỉ số
        thienTaiCongNghe.options.add(new ItemOption(OPT_HP, 30 + Utils.nextInt(6))); // 24-30% HP
        thienTaiCongNghe.options.add(new ItemOption(OPT_KI, 30 + Utils.nextInt(6))); // 24-30% KI
        thienTaiCongNghe.options.add(new ItemOption(OPT_SD, 30 + Utils.nextInt(6))); // 24-30% SĐ
//        thienTaiCongNghe.options.add(new ItemOption(226, 2));
        // 10% VV, 90% HSD
        if (Utils.isTrue(10, 100)) {
            // Vĩnh viễn - không thêm option HSD
        } else {
            // HSD ngẫu nhiên 1-30 ngày
            thienTaiCongNghe.options.add(new ItemOption(93, getDays())); // Option 93: số ngày HSD
        }

        return thienTaiCongNghe;
    }

    /**
     * (Thú cưỡi Hắc Kỳ Lân)
     *
     * @return Hắc Kỳ Lân
     */
    private static Item createHacKyLan() {
        // ID Saiyan Cuồng Nộ
        final int HAC_KY_LAN_ID = 2259;

        // ID options
        final int OPT_HP = 77;
        final int OPT_KI = 103;
        final int OPT_SD = 50;

        Item saiyancuongno = new Item(HAC_KY_LAN_ID);
        saiyancuongno.quantity = 1;

        // Thêm chỉ số
        saiyancuongno.options.add(new ItemOption(OPT_HP, 4 + Utils.nextInt(5))); // 4-8% HP
        saiyancuongno.options.add(new ItemOption(OPT_KI, 4 + Utils.nextInt(5))); // 4-8% KI
        saiyancuongno.options.add(new ItemOption(OPT_SD, 4 + Utils.nextInt(5))); // 4-8% SĐ

        // 10% VV, 90% HSD
        if (Utils.isTrue(10, 100)) {
            // Vĩnh viễn - không thêm option HSD
        } else {
            // HSD ngẫu nhiên 1-30 ngày
            saiyancuongno.options.add(new ItemOption(93, getDays())); // Option 93: số ngày HSD
        }

        return saiyancuongno;
    }

    /**
     * Tạo Thần trả hủy diệt (Pet Tiểu Xà Vương)
     *
     * @return Thần trả hủy diệt
     */
    private static Item createTieuXaVuong() {
        // ID Thần trả hủy diệt
        final int THAN_TRA_HUY_DIET_ID = 2252;

        // ID options
        final int OPT_HP = 77;
        final int OPT_KI = 103;
        final int OPT_SD = 50;
        final int OPT_NE_DON = 108;

        Item thantrahuydiett = new Item(THAN_TRA_HUY_DIET_ID);
        thantrahuydiett.quantity = 1;

        // Thêm chỉ số
        thantrahuydiett.options.add(new ItemOption(OPT_HP, 12 + Utils.nextInt(5))); // 12-16% HP
        thantrahuydiett.options.add(new ItemOption(OPT_KI, 12 + Utils.nextInt(5))); // 12-16% KI
        thantrahuydiett.options.add(new ItemOption(OPT_SD, 12 + Utils.nextInt(5))); // 12-16% SĐ
        thantrahuydiett.options.add(new ItemOption(OPT_NE_DON, 10)); // 10% né đòn

        // 10% VV, 90% HSD
        if (Utils.isTrue(10, 100)) {
            // Vĩnh viễn - không thêm option HSD
        } else {
            // HSD ngẫu nhiên 1-30 ngày

            thantrahuydiett.options.add(new ItemOption(93, getDays())); // Option 93: số ngày HSD
        }

        return thantrahuydiett;
    }

    static int getDays() {
        int[] days = new int[]{1, 3, 5};
        return days[Utils.nextInt(days.length)];
    }

    /**
     * Tạo đeo lưng Cờ Ngọc Rồng 2 Sao
     *
     * @return Đeo lưng Cờ Ngọc Rồng 2 Sao
     */
    private static Item createCoNgocRong() {
        // ID đeo lưng Cờ Ngọc Rồng 2 Sao
        final int CO_NGOC_RONG_ID = 2249;

        // ID options
        final int OPT_HP = 77;
        final int OPT_KI = 103;
        final int OPT_SD = 50;
        final int OPT_HUT_HP = 95;
        final int OPT_HUT_KI = 96;

        Item coNgocRong = new Item(CO_NGOC_RONG_ID);
        coNgocRong.quantity = 1;

        // Thêm chỉ số
        coNgocRong.options.add(new ItemOption(OPT_HP, 10 + Utils.nextInt(6))); // 10-15% HP
        coNgocRong.options.add(new ItemOption(OPT_KI, 10 + Utils.nextInt(6))); // 10-15% KI
        coNgocRong.options.add(new ItemOption(OPT_SD, 10 + Utils.nextInt(6))); // 10-15% SĐ
        coNgocRong.options.add(new ItemOption(OPT_HUT_HP, 5 + Utils.nextInt(6))); // 5-10% hút HP
        coNgocRong.options.add(new ItemOption(OPT_HUT_KI, 5 + Utils.nextInt(6))); // 5-10% hút KI

        // 10% VV, 90% HSD
        if (Utils.isTrue(10, 100)) {
            // Vĩnh viễn - không thêm option HSD
        } else {
            // HSD ngẫu nhiên 1-30 ngày
            coNgocRong.options.add(new ItemOption(93, getDays())); // Option 93: số ngày HSD
        }

        return coNgocRong;
    }

    /**
     * Tạo đeo lưng Khỉ con vui vẻ
     *
     * @return Đeo lưng Khỉ con vui vẻ
     */
    private static Item createKhiConVuiVe() {
        // ID đeo lưng Khỉ con vui vẻ
        final int KHI_CON_VUI_VE_ID = 2250;

        // ID options
        final int OPT_HP = 77;
        final int OPT_KI = 103;
        final int OPT_SD = 50;
        final int OPT_GIAP = 94;

        Item khiConVuiVe = new Item(KHI_CON_VUI_VE_ID);
        khiConVuiVe.quantity = 1;

        // Thêm chỉ số
        khiConVuiVe.options.add(new ItemOption(OPT_HP, 10 + Utils.nextInt(6))); // 10-15% HP
        khiConVuiVe.options.add(new ItemOption(OPT_KI, 10 + Utils.nextInt(6))); // 10-15% KI
        khiConVuiVe.options.add(new ItemOption(OPT_SD, 10 + Utils.nextInt(6))); // 10-15% SĐ
        khiConVuiVe.options.add(new ItemOption(OPT_GIAP, 3 + Utils.nextInt(5))); // 3-7% Giáp

        // 10% VV, 90% HSD
        if (Utils.isTrue(10, 100)) {
            // Vĩnh viễn - không thêm option HSD
        } else {
            // HSD ngẫu nhiên 1-30 ngày
            khiConVuiVe.options.add(new ItemOption(93, getDays())); // Option 93: số ngày HSD
        }

        return khiConVuiVe;
    }

    /**
     * Tạo nước giải khát
     *
     * @return Nước giải khát
     */
    private static Item createKemTraiCay() {
        // ID nước giải khát
        final int KEM_TRAI_CAY_ID = 2261;

        Item nuocMaThuat = new Item(KEM_TRAI_CAY_ID);
        nuocMaThuat.quantity = 1;

        // Đây là vật phẩm tiêu thụ, không cần thêm option
        return nuocMaThuat;
    }

    private static Item createNuocGiaiKhat() {
        // ID nước giải khát
        final int NUOC_GIAI_KHAT_ID = 2251;

        Item nuocGiaiKhat = new Item(NUOC_GIAI_KHAT_ID);
        nuocGiaiKhat.quantity = 1;

        // Đây là vật phẩm tiêu thụ, không cần thêm option
        return nuocGiaiKhat;
    }

    /**
     * Tạo linh thú Tiểu Miêu Linh
     *
     * @return Linh thú Tiểu Miêu Linh
     */
    private static Item createTieuMieuLinh() {
        // ID linh thú Tiểu Miêu Linh
        final int TIEU_MIEU_LINH_ID = 2258;

        // ID options
        final int OPT_HP = 77;
        final int OPT_KI = 103;
        final int OPT_SD = 50;

        Item tieuMieuLinh = new Item(TIEU_MIEU_LINH_ID);
        tieuMieuLinh.quantity = 1;

        // Thêm chỉ số
        tieuMieuLinh.options.add(new ItemOption(OPT_HP, 4 + Utils.nextInt(5))); // 4-8% HP
        tieuMieuLinh.options.add(new ItemOption(OPT_KI, 4 + Utils.nextInt(5))); // 4-8% KI
        tieuMieuLinh.options.add(new ItemOption(OPT_SD, 4 + Utils.nextInt(5))); // 4-8% SĐ

        // 10% VV, 90% HSD
        if (Utils.isTrue(10, 100)) {
            // Vĩnh viễn - không thêm option HSD
        } else {
            // HSD ngẫu nhiên 1-30 ngày
            tieuMieuLinh.options.add(new ItemOption(93, getDays())); // Option 93: số ngày HSD
        }

        return tieuMieuLinh;
    }

    private static Item createSPL() {
        var itemID = Utils.nextInt(441, 447);
        Item item = new Item(itemID);
        item.setDefaultOptions();
        return item;
    }

    private static Item createNR() {
        var itemID = Utils.nextInt(17, 20);
        Item item = new Item(itemID);
        item.setDefaultOptions();
        return item;
    }

    private static Item createDNC() {
        var itemID = Utils.nextInt(220, 224);
        Item item = new Item(itemID);
        item.setDefaultOptions();
        return item;
    }

}

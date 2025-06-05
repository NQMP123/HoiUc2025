package com.ngocrong.combine;

import com.ngocrong.NQMP.TamThangBa.TachVatPham;
import static com.ngocrong.combine.CombineType.KICH_HOAT;
import static com.ngocrong.combine.CombineType.NANG_CAP;
import static com.ngocrong.combine.CombineType.PHA_LE_HOA;
import static com.ngocrong.combine.CombineType.TACH_DO_KICH_HOAT;
import static com.ngocrong.combine.CombineType.Tach_Vat_Pham;

public class CombineFactory {

    public static Combine getCombine(CombineType combineType) {
        switch (combineType) {
            case CHUYEN_HOA:
                return new ChuyenHoa();

            case NANG_CAP:
                return new NangCap();
            case NANG_CAP_2:
                return new NangCap_2();
            case EP_PHA_LE:
                return new EpPhaLe();

            case NANG_PORATA:
                return new NangPorata();

            case NANG_OPTION_PORATA:
                return new NangOptionPorata();

            case NHAP_DA:
                return new NhapDa();

            case NHAP_NGOC:
                return new NhapNgoc();

            case PHA_LE_HOA:
                return new PhaLeHoa();
            case HUY_DIET:
                return new DoiDoHuyDiet();
            case KICH_HOAT:
                return new DoiDoKichHoat();
            case GHEP_DA:
                return new GhepDaNangCap();
            case TACH_DO:
                return new TachDoThanLinh();
            case TACH_DO_KICH_HOAT:
                return new TachDoKichHoat();
            case Tach_Vat_Pham:
                return new TachVatPham();
            case Nang_Item_De_Tu:
                return new NangItemDeTu();
            default:
                throw new IllegalArgumentException("This combine type is unsupported");
        }
    }
}

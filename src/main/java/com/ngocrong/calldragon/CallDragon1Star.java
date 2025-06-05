package com.ngocrong.calldragon;

import com.ngocrong.item.ItemOption;
import com.ngocrong.bot.Disciple;
import com.ngocrong.combine.NangCap;
import com.ngocrong.consts.ItemName;
import com.ngocrong.consts.NpcName;
import com.ngocrong.item.Item;
import com.ngocrong.lib.KeyValue;
import com.ngocrong.skill.Skill;
import com.ngocrong.skill.SkillPet;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import org.apache.log4j.Logger;

public class CallDragon1Star extends CallDragon {

    private static final Logger logger = Logger.getLogger(CallDragon1Star.class);

    public static final int AVATAR_VIP = 0;
    public static final int CAPSULE_1_STAR = 1;
    public static final int GIAU_CO_1K5_GEM = 5;
    public static final int UPGRADE_GLOVE = 2;
    public static final int CHANGE_SKILL_PET = 3;
    public static final int UPGRADE_GLOVE_PET = 4;

    public CallDragon1Star(Player _c, short x, short y) {
        super(_c, x, y);
        this.id = NpcName.RONG_THIENG;
        this.avatar = 0;
        this.isRongNamek = false;
        this.say = "Ta sẽ ban cho ngươi 1 điều ước, ngươi có 5 phút, hãy suy nghĩ thật kỹ trước khi quyết định";
        wishList = new KeyValue[2][];
        wishList[0] = new KeyValue[3];
        wishList[0][0] = new KeyValue(20004, "Đẹp trai\nnhất\nVũ trụ", AVATAR_VIP);
        wishList[0][1] = new KeyValue(20004, "Giàu có\n+1.5 K\nNgọc xanh", GIAU_CO_1K5_GEM);
        wishList[0][2] = new KeyValue(20000, "Điều ước\nkhác");
        wishList[1] = new KeyValue[4];
        wishList[1][0] = new KeyValue(20004, "Găng tay\nđang mang\nlên 1 cấp", UPGRADE_GLOVE);
        wishList[1][1] = new KeyValue(20004, "Thay\nChiêu 2-3\nĐệ tử", CHANGE_SKILL_PET);
        wishList[1][2] = new KeyValue(20004, "Găng tay\nĐệ tử\nđang mang\nlên 1 cấp", UPGRADE_GLOVE_PET);
        wishList[1][3] = new KeyValue(20000, "Điều ước\nkhác");
    }

    @Override
    public void accept() {
        int type = ((Integer) select.elements[0]);
        switch (type) {
            case AVATAR_VIP: {
                if (_c.getSlotNullInBag() == 0) {
                    back("Hành trang đã đầy, vui lòng chọn điều ước khác");
                    return;
                }
                int itemID = ItemName.AVATAR_VIP_TRAI_DAT;
                if (_c.gender == 1) {
                    itemID = ItemName.AVATAR_VIP_NAMEC;
                } else if (_c.gender == 2) {
                    itemID = ItemName.AVATAR_VIP_XAYDA;
                }
                Item item = new Item(itemID);
                item.quantity = 1;
                item.addItemOption(new ItemOption(97, Utils.nextInt(5, 10)));
                item.addItemOption(new ItemOption(77, Utils.nextInt(10, 20)));
                _c.addItem(item);

            }
            break;

            case CAPSULE_1_STAR: {
                if (_c.getSlotNullInBag() == 0) {
                    back("Hành trang đã đầy, vui lòng chọn điều ước khác");
                    return;
                }
                Item item = new Item(ItemName.CAPSULE_1_SAO);
                item.setDefaultOptions();
                item.quantity = 1;
                _c.addItem(item);
            }
            break;

            case UPGRADE_GLOVE: {
                Item e = _c.itemBody[2];
                if (e != null) {
                    ItemOption o = null;
                    ItemOption u = null;
                    for (ItemOption o2 : e.options) {
                        if (o2.optionTemplate.id == 72) {
                            o = o2;
                        }
                        if (o2.optionTemplate.id == 0) {
                            u = o2;
                        }
                    }

                    if (o == null) {
                        o = new ItemOption(72, 0);
                        e.addItemOption(o);
                    }
                    if (o.param >= NangCap.MAX_UPGRADE) {
                        back("Trang bị đã đạt cấp tối đa, vui lòng chọn điều ước khác");
                        return;
                    }
                    o.param++;
                    u.param += u.param / 10;
                    _c.service.refreshItem((byte) 0, e);
                } else {
                    back("Bạn không có trang bị này, vui lòng chọn điều ước khác");
                    return;
                }
            }
            break;

            case CHANGE_SKILL_PET:
                if (_c.myDisciple != null) {
                    if (_c.myDisciple.skillOpened < 3) {
                        back("Không thể thực hiện, vui lòng chọn điều ước khác");
                        return;
                    }
                    try {
                        Disciple disciple = _c.myDisciple;
                        SkillPet skillPet = SkillPet.list.get(1);
                        byte skillID = skillPet.skills[Utils.nextInt(skillPet.skills.length)];
                        Skill skill = Skills.getSkill(skillID, (byte) 1);

                        if (skill != null) {
                            skill = skill.clone();
                            skill.coolDown = 1000;
                            disciple.skills.set(1, skill);
                        }
                        skill = null;
                        skillPet = SkillPet.list.get(2);
                        skillID = skillPet.skills[Utils.nextInt(skillPet.skills.length)];
                        skill = Skills.getSkill(skillID, (byte) 1);
                        if (skill != null) {
                            skill = skill.clone();
                            disciple.skills.set(2, skill);
                        }
                    } catch (Exception ex) {
                        com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                        logger.error("error", ex);
                    }
                } else {
                    back("Bạn không có đệ tử, vui lòng chọn điều ước khác");
                    return;
                }
                break;

            case UPGRADE_GLOVE_PET: {
                if (_c.myDisciple != null) {
                    Item e = _c.myDisciple.itemBody[2];
                    if (e != null) {
                        ItemOption o = null;
                        ItemOption u = null;
                        for (ItemOption o2 : e.options) {
                            if (o2.optionTemplate.id == 72) {
                                o = o2;
                            }
                            if (o2.optionTemplate.id == 0) {
                                u = o2;
                            }
                        }

                        if (o == null) {
                            o = new ItemOption(72, 0);
                            e.addItemOption(o);
                        }
                        if (o.param >= NangCap.MAX_UPGRADE) {
                            back("Trang bị đã đạt cấp tối đa, vui lòng chọn điều ước khác");
                            return;
                        }
                        o.param++;
                        u.param += u.param / 10;
                    } else {
                        back("Đệ tử bạn không có trang bị này, vui lòng chọn điều ước khác");
                        return;
                    }
                } else {
                    back("Bạn không có đệ tử, vui lòng chọn điều ước khác");
                    return;
                }
            }
            break;

            case GIAU_CO_1K5_GEM:
                _c.addDiamond(1500);
                break;
        }
        _c.service.openUISay(type, "Điều ước của ngươi đã thành sự thực\nHẹn gặp ngươi lần sau, ta đi ngủ đây, bái bai", avatar);
        close();
    }

}

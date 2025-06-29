package com.ngocrong.bot.boss.dhvt23;

import com.ngocrong.bot.Boss;
import com.ngocrong.skill.Skill;
import com.ngocrong.skill.Skills;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class SoiHecQuyn extends BossDHVT {

    public SoiHecQuyn(Player plAtt) {
        super();
        canAttack = false;
        this.plAtt = plAtt;
        this.limit = -1;
        this.name = "Sói Hẹc Quyn";
        setInfo(50000000, 20000, 5000, 3, 5);
        setLocation(plAtt.zone);
        Utils.setTimeout(() -> {
            canAttack = true;
        }, 14500);
        super.setDame(5, plAtt);
    }
    private static Logger logger = Logger.getLogger(SoiHecQuyn.class);

    @Override
    public void initSkill() {
        try {
            skills = new ArrayList();
            Skill skill;
            skill = Skills.getSkill((byte) 0, (byte) 1).clone();
            skills.add(skill);
        } catch (CloneNotSupportedException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            logger.error("init skill err", ex);
        }
    }

    @Override
    public void sendNotificationWhenAppear(String map) {
    }

    @Override
    public void sendNotificationWhenDead(String name) {
    }

    @Override
    public void setDefaultLeg() {
        this.setLeg((short) 396);
    }

    @Override
    public void setDefaultBody() {
        this.setBody((short) 395);
    }

    @Override
    public void setDefaultHead() {
        this.setHead((short) 394);
    }

}

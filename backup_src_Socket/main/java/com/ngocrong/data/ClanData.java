package com.ngocrong.data;

import com.ngocrong.user.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "nr_clan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClanData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "abbreviation")
    public String abbreviation;

    @Column(name = "leader_id")
    public Integer leaderId;

    @Column(name = "leader_name")
    public String leaderName;

    @Column(name = "slogan")
    public String slogan;

    @Column(name = "image_id")
    public Byte imageId;

    @Column(name = "level")
    public Byte level;

    @Column(name = "clan_point")
    public Integer clanPoint;

    @Column(name = "power_point")
    public Long powerPoint;

    @Column(name = "max_member")
    public Byte maxMember;

    @Column(name = "item_box")
    public String itemBox;

    @Column(name = "create_time")
    public Timestamp createTime;

    public ClanData(String name, byte imageId, Player _player) {
        this.imageId = imageId;
        this.leaderId = _player.id;
        leaderName = _player.name;
        this.name = name;
        clanPoint = 0;
        maxMember = 10;
        powerPoint = 0L;
        level = 1;
        slogan = "Chào mừng bạn đến với Hồi Ức Ngọc Rông";
        abbreviation = "HUNR-2025";
        itemBox = "[]";
        createTime = new Timestamp(System.currentTimeMillis());
    }
}

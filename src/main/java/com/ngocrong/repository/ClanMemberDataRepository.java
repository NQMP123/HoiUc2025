package com.ngocrong.repository;

import com.ngocrong.data.ClanMemberData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ClanMemberDataRepository extends JpaRepository<ClanMemberData, Long> {

    @Modifying
    @Query("UPDATE ClanMemberData m SET "
            + "m.role = :role, "
            + "m.head = :head, "
            + "m.body = :body, "
            + "m.leg = :leg, "
            + "m.powerPoint = :powerPoint, "
            + "m.donate = :donate, "
            + "m.receiveDonate = :receiveDonate, "
            + "m.clanPoint = :clanPoint, "
            + "m.currentPoint = :currentPoint, "
            + "m.clanReward = :clanReward "
            + "WHERE m.id = :id")
    void saveData(Long id, Byte role, Short head, Short body, Short leg,
            Long powerPoint, Integer donate, Integer receiveDonate, Integer clanPoint,
            Integer currentPoint, String clanReward);
}

package com.ngocrong.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nr_drop_rate")
public class DropRateData {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "mob_rate")
    private Integer mobRate;

    @Column(name = "boss_rate")
    private Integer bossRate;
}

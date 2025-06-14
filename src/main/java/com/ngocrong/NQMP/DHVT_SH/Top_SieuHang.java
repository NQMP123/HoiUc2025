/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.NQMP.DHVT_SH;

import com.ngocrong.consts.Cmd;
import com.ngocrong.network.FastDataOutputStream;
import com.ngocrong.network.Message;
import com.ngocrong.server.mysql.MySQLConnect;
import com.ngocrong.top.Top;
import com.ngocrong.top.TopInfo;
import com.ngocrong.top.TopPower;
import com.ngocrong.user.Player;
import com.ngocrong.util.Utils;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class Top_SieuHang {

    public int pid;
    public String name;
    public short head;
    public short body;
    public short leg;
    public int rank;
    public String info;
    public String info2 = "";
    public static List<Top_SieuHang> elements = new ArrayList<>();

    public static void setNewRank(int pid, int newRank) {
        for (Top_SieuHang top : elements) {
            if (top != null && top.pid == pid) {
                top.rank = newRank;
                break;
            }
        }
    }

    public static Top_SieuHang getTopbyRank(int rank) {
        for (Top_SieuHang top : elements) {
            if (top != null) {
                if (top.rank == rank) {
                    return top;
                }
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }

// Lấy đối tượng Top_SieuHang dựa vào ID người chơi (pid)
    public static Top_SieuHang getTopbyPid(int pid) {
        for (Top_SieuHang top : elements) {
            if (top != null && top.pid == pid) {
                return top;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }

    public static void show(Player player, List<Top_SieuHang> list) {
        try {
            // Sắp xếp danh sách theo rank trước khi gửi
            List<Top_SieuHang> sortedList = new ArrayList<>(list);
            sortedList.sort(Comparator.comparingInt(top -> top.rank));

            Message ms = new Message(Cmd.TOP);
            FastDataOutputStream ds = ms.writer();
            ds.writeByte(0);
            ds.writeUTF("Top 100 Cao Thủ");
            ds.writeByte(sortedList.size());

            int i = 1;
            for (var top : sortedList) {
                ds.writeInt(top.rank);
                ds.writeInt(top.pid);
                ds.writeShort(top.head);
                ds.writeShort(top.body);
                ds.writeShort(top.leg);
                ds.writeUTF(top.name);
                ds.writeUTF(top.info);
                ds.writeUTF(top.info2);
            }

            ds.flush();
            player.service.sendMessage(ms);
            ms.cleanup();
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 125");
        }
    }

    public static void show(Player player, int rank) {
        if (rank == -1) {
            show(player, elements.subList(0, Math.min(100, elements.size())));
        } else {
            List<Top_SieuHang> list = new ArrayList<>();
            if (getTopbyPid(player.id) != null) {
                list.add(getTopbyPid(player.id));
            }

            for (int i = rank - 1; i >= rank - 9; i--) {
                if (getTopbyRank(i) != null) {
                    list.add(getTopbyRank(i));
                }
            }
            if (getTopbyRank(getRandom(rank)) != null) {
                list.add(getTopbyRank(getRandom(rank)));
            }
            show(player, list);
            System.err.println("Rank :" + rank);
        }
    }

    public static int getRandom(int rank) {
        if (rank > 10000) {
            return Utils.nextInt(6666, 10000);
        } else if (rank > 6666) {
            return Utils.nextInt(3333, 6666);
        } else if (rank > 3333) {
            return Utils.nextInt(1000, 3333);
        } else if (rank > 1000) {
            return Utils.nextInt(666, 1000);
        } else if (rank > 666) {
            return Utils.nextInt(333, 666);
        } else if (rank > 110) {
            return Utils.nextInt(100, rank);
        }
        return -1;
    }

    public static void load() {
        synchronized (elements) {
            elements.clear();
        }
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(
                    "SELECT np.id, np.name , np.head2 , np.body , np.leg, neo.rank as rank from nr_player np "
                    + "inner join nr_super_rank neo on np.id  = neo.player_id "
                    + "group by np.id, np.name, np.head2, np.body, np.leg,neo.rank "
                    + "order by rank asc;");
            ResultSet rs = ps.executeQuery();
            try {
                while (rs.next()) {
                    Top_SieuHang info = new Top_SieuHang();
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    short head = rs.getShort("head2");
                    short body = rs.getShort("body");
                    short leg = rs.getShort("leg");
                    short rank = rs.getShort("rank");
                    info.rank = rank;
                    info.pid = id;
                    info.name = name;
                    info.head = head;
                    info.body = body;
                    info.leg = leg;
                    short goldReward;
                    if (rank == 1) {
                        goldReward = 200;
                        info.info = String.format("%s thỏi vàng/ngày", Utils.formatNumber(goldReward));
                    } else if (rank == 2 || rank == 3) {
                        goldReward = 100;
                        info.info = String.format("%s thỏi vàng/ngày", Utils.formatNumber(goldReward));
                    } else if (rank <= 9) {
                        goldReward = 50;
                        info.info = String.format("%s thỏi vàng/ngày", Utils.formatNumber(goldReward));
                    } else if (rank <= 20) {
                        goldReward = 10;
                        info.info = String.format("%s thỏi vàng/ngày", Utils.formatNumber(goldReward));
                    } else if (rank <= 50) {
                        goldReward = 5;
                        info.info = String.format("%s thỏi vàng/ngày", Utils.formatNumber(goldReward));
                    } else if (rank <= 100) {
                        goldReward = 2;
                        info.info = String.format("%s thỏi vàng/ngày", Utils.formatNumber(goldReward));
                    } else {
                        info.info = "";
                    }
                    elements.add(info);
                }
            } finally {
                rs.close();
                ps.close();
            }
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 124");
        }
    }

}

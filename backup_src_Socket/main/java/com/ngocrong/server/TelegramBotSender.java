/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.server;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Administrator
 */
public class TelegramBotSender {

    public static String BOT_TOKEN;
    public static String CHAT_ID;
    public static boolean isSendTele = false;

    public static void sendCCU() {
        if (!isSendTele) {
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);

        String text = "HOI_UC_NGOC_RONG-HUNR 01   -   " + formattedTime + "\n"
                + "Tổng CCU : " + SessionManager.sessions.size() + " Session\n"
                + "     -      " + SessionManager.getCountPlayer() + " Player Online";
        sendMessageTelegram(text);
        System.err.println(text);
    }

    public static void sendMessageTelegram(String message) {
        try {
            // Mã hóa message
            String encodedMessage = URLEncoder.encode(message, "UTF-8");

            // Tạo URL gửi tin nhắn
            String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";

            // Mở kết nối
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Cấu hình request
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Tạo dữ liệu gửi đi
            String data = "chat_id=" + CHAT_ID + "&text=" + encodedMessage;

            // Gửi dữ liệu
            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes());
                os.flush();
            }

            // Kiểm tra phản hồi
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("✅ Đã gửi tin nhắn thành công!");
            } else {
                System.out.println("❌ Gửi tin nhắn thất bại. Mã lỗi: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ngocrong.server;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.Proxy;
import java.net.InetSocketAddress;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
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
    
    // Thông tin proxy SOCKS5
    private static final String PROXY_HOST = "172.111.171.224";
    private static final int PROXY_PORT = 44012;
    private static final String PROXY_USERNAME = "muaproxy68430490addb0";
    private static final String PROXY_PASSWORD = "c8ri7hjrpizsz3yh";
    
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
            // Tạo proxy SOCKS5 với authentication
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
            
            // Set proxy authentication chỉ cho request này
            Authenticator proxyAuth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(PROXY_USERNAME, PROXY_PASSWORD.toCharArray());
                }
            };
            
            // Mã hóa message
            String encodedMessage = URLEncoder.encode(message, "UTF-8");
            
            // Tạo URL gửi tin nhắn
            String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
            URL url = new URL(urlString);
            
            // Mở kết nối qua proxy
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            
            // Set authenticator cho connection này
            Authenticator.setDefault(proxyAuth);
            
            // Cấu hình request
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            
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
                System.out.println("✅ Đã gửi tin nhắn thành công qua proxy!");
            } else {
                System.out.println("❌ Gửi tin nhắn thất bại. Mã lỗi: " + responseCode);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi gửi tin nhắn qua proxy: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
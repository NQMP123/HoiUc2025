package com.ngocrong.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ngocrong.lib.KeyValue;
import com.ngocrong.lib.RandomCollection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Utils {

    public static Random rand = new Random();
    private static Pattern pattern = Pattern.compile("^[a-z0-9]{6,15}$");
    private static Pattern patternAlphaNumeric = Pattern.compile("^[a-z0-9]$");

    public static String unaccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    public static boolean validateName(String name) {
        return pattern.matcher(name).matches();
    }

    public static String cutPng(String str) {
        String result = str;
        if (str.contains(".png")) {
            result = str.replace(".png", "");
        }
        return result;
    }

    public static ZonedDateTime getLocalDateTime() {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        return zonedNow;
    }

    public static long percentOf(long x, long p) {
        return x * p / 100;
    }

    public static String getAbbre(String Abbre) {
        List<String> strs = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        strs.add(Abbre);
        colors.add("white");
        return getRichText(colors, strs);
    }

    public static String getRichText(List<String> colors, List<String> strs) {
        if (strs.size() != colors.size()) {
            return "";
        }
        String richText = "";
        for (int i = 0; i < strs.size(); i++) {
            richText += String.format("<color=%s>%s</color>", colors.get(i), strs.get(i));
        }
        return richText;
    }

    public static long getFolderSize(File folder) {
        long length = 0;
        File[] files = folder.listFiles();
        int count = files.length;
        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            } else {
                length += getFolderSize(files[i]);
            }
        }
        return length;
    }

    public static int getCountDay(Timestamp date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int oldYear = cal.get(Calendar.YEAR);
        int oldDay = cal.get(Calendar.DAY_OF_YEAR);
        int nowYear = LocalDateTime.now().getYear();
        int nowDay = LocalDateTime.now().getDayOfYear();
        if (oldYear == nowYear) {
            return nowDay - oldDay;
        }
        if (oldYear > nowYear) {
            return -1;
        }
        return 365 * (nowYear - oldYear) + nowDay - oldDay;
    }

    public static int getItemGoldByQuantity(int gold) {
        int itemID = -1;
        if (gold < 200) {
            itemID = 76;
        } else if (gold < 1000) {
            itemID = 188;
        } else if (gold < 10000) {
            itemID = 189;
        } else {
            itemID = 190;
        }
        return itemID;
    }

    public static String formatNumber(long number) {
        String text = "";
        String text2 = "";
        if (number >= 1000000000L) {
            text2 = "tỉ";
            long num = number % 1000000000L / 10000000L;
            number /= 1000000000L;
            text = number + "";
            if (num >= 10L) {
                if (num % 10L == 0L) {
                    num /= 10L;
                }
                text = text + "," + num + text2;

            } else if (num > 0L) {
                text = text + ",0" + num + text2;
            } else {
                text += text2;
            }
        } else if (number >= 1000000L) {
            text2 = "tr";
            long num2 = number % 1000000L / 10000L;
            number /= 1000000L;
            text = number + "";
            if (num2 >= 10L) {
                if (num2 % 10L == 0L) {
                    num2 /= 10L;
                }
                text = text + "," + num2 + text2;
            } else if (num2 > 0L) {
                text = text + ",0" + num2 + text2;
            } else {
                text += text2;
            }
        } else if (number >= 10000L) {
            text2 = "k";
            long num3 = number % 1000L / 10L;
            number /= 1000L;
            text = number + "";
            if (num3 >= 10L) {
                if (num3 % 10L == 0L) {
                    num3 /= 10L;
                }
                text = text + "," + num3 + text2;
            } else if (num3 > 0L) {
                text = text + ",0" + num3 + text2;
            } else {
                text += text2;
            }
        } else {
            text = number + "";
        }
        return text;
    }

    public static String timeAgo(int seconds) {
        int minutes = seconds / 60;
        if (minutes > 0) {
            return minutes + " phút";
        } else {
            return seconds + " giây";
        }
    }

    public static byte getIndexWithArray(byte value, byte[] array, boolean isCorrect) {
        byte index = -1;
        for (byte i = 0; i < array.length; i++) {
            if (isCorrect) {
                if (array[i] == value) {
                    index = i;
                    break;
                }
            } else {
                if (array[i] != value) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public static int getDiagonalOfRectangle(int w, int h) {
        return (int) Math.sqrt((w * w) + (h * h));
    }

    public static void setTimeout(Runnable runnable, long delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception ignored) {
                ignored.printStackTrace();
                System.err.println("Error at 4");
            }
        }).start();
    }

    public static void setScheduled(Runnable runnable, long intervalSecond, int hour, int minute) {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        if (zonedNow.getHour() == hour && zonedNow.getMinute() > minute) {
            runnable.run();
        }
        ZonedDateTime zonedNext = zonedNow.withHour(hour).withMinute(minute).withSecond(0);
        if (zonedNow.compareTo(zonedNext) > 0) {
            zonedNext = zonedNext.plusSeconds(intervalSecond);
        }
        Duration duration = Duration.between(zonedNow, zonedNext);
        long delay = duration.getSeconds();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(runnable, delay, intervalSecond, TimeUnit.SECONDS);
    }

    public static boolean checkExistKey(Object key, ArrayList<KeyValue> list) {
        for (KeyValue keyValue : list) {
            if (keyValue.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static String replace(String text, String regex, String replacement) {
        return text.replace(regex, replacement);
    }

    public static String getTime(int timeRemainS) {
        int num = 0;
        if (timeRemainS > 60) {
            num = timeRemainS / 60;
            timeRemainS %= 60;
        }
        int num2 = 0;
        if (num > 60) {
            num2 = num / 60;
            num %= 60;
        }
        int num3 = 0;
        if (num2 > 24) {
            num3 = num2 / 24;
            num2 %= 24;
        }
        String text = "";
        if (num3 > 0) {
            text += num3;
            text += "d";
            text = text + num2 + "h";
        } else if (num2 > 0) {
            text += num2;
            text += "h";
            text = text + num + "'";
        } else {
            if (num > 9) {
                text += num;
            } else {
                text = text + "0" + num;
            }
            text += ":";
            if (timeRemainS > 9) {
                text += timeRemainS;
            } else {
                text = text + "0" + timeRemainS;
            }
        }
        return text;
    }

    public static String getTimeAgo(int timeRemainS) {
        int num = 0;
        if (timeRemainS > 60) {
            num = timeRemainS / 60;
            timeRemainS %= 60;
        }
        int num2 = 0;
        if (num > 60) {
            num2 = num / 60;
            num %= 60;
        }
        int num3 = 0;
        if (num2 > 24) {
            num3 = num2 / 24;
            num2 %= 24;
        }
        String text = "";
        if (num3 > 0) {
            text += num3;
            text += " ngày";
            text = text + num2 + " giờ";
        } else if (num2 > 0) {
            text += num2;
            text += " giờ";
            text = text + num + " phút";
        } else {
            if (num == 0) {
                num = 1;
            }
            text += num;
            text += " phút";
        }
        return text;
    }

    public static byte[] getFile(String url) {
        try {
            File file = new File(url);
            if (file.exists()) {
                return Files.readAllBytes(file.toPath());
            }
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            System.err.println("Error at 3");
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] compress(byte[] data) {
        if (data == null || data.length == 0) {
            return data;
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(data);
            gzip.finish();
            return bos.toByteArray();
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            return data;
        }
    }

    public static byte[] decompress(byte[] data) {
        if (data == null || data.length == 0) {
            return data;
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             GZIPInputStream gzip = new GZIPInputStream(bis);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzip.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            return data;
        }
    }

    public static byte[] applyNoiseGate(byte[] compressedData, float minThreshold) {
        try {
            byte[] raw = decompress(compressedData);
            int sampleCount = raw.length / 2;
            float sum = 0f;
            for (int i = 0; i < sampleCount; i++) {
                int low = raw[i * 2] & 0xFF;
                int high = raw[i * 2 + 1];
                short sample = (short) ((high << 8) | low);
                sum += Math.abs(sample) / 32767f;
            }
            float avg = sum / sampleCount;
            float threshold = Math.max(minThreshold, avg * 0.5f);
            for (int i = 0; i < sampleCount; i++) {
                int low = raw[i * 2] & 0xFF;
                int high = raw[i * 2 + 1];
                short sample = (short) ((high << 8) | low);
                float f = sample / 32767f;
                if (Math.abs(f) < threshold) {
                    sample = 0;
                }
                raw[i * 2] = (byte) (sample & 0xFF);
                raw[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
            }
            return compress(raw);
        } catch (Exception ex) {
            com.ngocrong.NQMP.UtilsNQMP.logError(ex);
            return compressedData;
        }
    }

    public static boolean checkPassword(String hashed, String plaintext) {
        if (hashed == null || hashed.isEmpty() || plaintext == null || plaintext.isEmpty()) {
            return false;
        }
        return BCrypt.verifyer().verify(plaintext.toCharArray(), hashed).verified;
    }

    public static String currencyFormat(long m) {
        String text = "";
        long num = m / 1000L + 1L;
        int num2 = 0;
        while ((long) num2 < num) {
            if (m < 1000L) {
                text = m + text;
                break;
            }
            long num3 = m % 1000L;
            if (num3 == 0L) {
                text = ".000" + text;
            } else if (num3 < 10L) {
                text = ".00" + num3 + text;
            } else if (num3 < 100L) {
                text = ".0" + num3 + text;
            } else {
                text = "." + num3 + text;
            }
            m /= 1000L;
            num2++;
        }
        return text;
    }

    public static int[] formatPercent(long quantity, long total) {
        long t = quantity * 10000 / total;
        int[] p = new int[2];
        p[0] = (int) (t / 100);
        p[1] = (int) (t - (p[0] * 100));
        return p;
    }

    public static void saveFile(String url, byte[] ab) {
        try {
            File f = new File(url);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(url);
            fos.write(ab);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 2");
            e.printStackTrace();
        }
    }

    public static boolean isTrue(double from, double to) {
        if (from >= to) {
            return true;
        }
        while (from < 10 && from != 0) {
            from *= 10;
            to *= 10;
        }
        long fromLong = (long) from;
        long toLong = (long) to;
        return rand.nextLong(toLong) <= (long) fromLong;
    }

    public static boolean isTrue(long from, long to) {
        if (from >= to) {
            return true;
        }
        return rand.nextLong(to + 1) <= from;
    }

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }

    public static long nextLong(long min, long max) {
        if (min >= max) {
            return max;
        }
        return min + (long) (Math.random() * (max - min));
    }

    public static int nextInt(int min, int max) {
        if (min >= max) {
            return max;
        }
        return rand.nextInt(max + 1 - min) + min;
    }

    public static int getDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static int getIntfromArrayRandom(int[] arrayRandom) {
        return arrayRandom[Utils.nextInt(arrayRandom.length)];
    }

    public static int getParambyRandom(int min, int max) {
        try {
            if (min >= max) {
                throw new IllegalArgumentException("from must be less than max");
            }

            int random1 = Utils.nextInt(min, Math.max(min + 1, (int) Math.ceil(max / 2.0)));
            int random2 = Utils.nextInt(random1, Math.max(random1 + 1, (int) Math.ceil(max / 1.67)));
            int random3 = Utils.nextInt(random2, Math.max(random2 + 1, (int) Math.ceil(max / 1.5)));
            int random4 = Utils.nextInt(random3, Math.max(random3 + 1, (int) Math.ceil(max / 1.4)));
            int random5 = Utils.nextInt(random4, Math.max(random4 + 1, (int) Math.ceil(max / 1.3)));
            int random6 = Utils.nextInt(random5, Math.max(random5 + 1, (int) Math.ceil(max / 1.2)));
            int random7 = Utils.nextInt(random6, Math.max(random6 + 1, (int) Math.ceil(max / 1.1)));
            RandomCollection<Integer> rd = new RandomCollection<>();
            rd.add(50, random1);
            rd.add(30, random2);
            rd.add(20, random3);
            rd.add(10, random4);
            rd.add(5, random5);
            rd.add(3, random6);
            rd.add(2, random7);
            rd.add(1, Utils.nextInt(random7, Math.max(random7, max)));
            return rd.next();
        } catch (Exception e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Error at 1");
            e.printStackTrace();
            return -1;
        }

    }
    
    /**
     * Apply noise gate filter to audio data to reduce background noise
     * @param audioData Raw audio bytes
     * @param threshold Threshold below which audio is considered noise (0.0-1.0)
     * @return Filtered audio data
     */
   
}

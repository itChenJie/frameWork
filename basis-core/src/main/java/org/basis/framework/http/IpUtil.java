package org.basis.framework.http;

import cn.hutool.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

/**
 *
 * @author ChenJie
 * @description ip 工具类
 */
public class IpUtil {
    private static Logger logger = LoggerFactory.getLogger(IpUtil.class);
    private static final int IP_LEN = 15;
    private static final String API_URL = "https://ipapi.co/";
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "127.0.0.1";
    // 客户端与服务器同为一台机器，获取的 ip 有时候是 ipv6 格式
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String SEPARATOR = ",";

    /**
     * 获取本机内网ip
     * @return
     */
    public static String getLocalIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 过滤掉回环接口和未启用的接口
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // 只获取内网地址（私有 IP）
                    if (inetAddress.isSiteLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }catch (SocketException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }
    /**
     * 获取外网ip城市
     * @param ip
     * @return
     */
    public static String getCityByIp(String ip) {
        String urlString = API_URL + ip + "/json";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();
            JSONObject json = new JSONObject(content.toString());
            return json.getStr("city");
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    /**
     * 获取外网IP
     * @return
     */
    public static String getExternalIp() {
        String externalIp = "";
        try {
            URL url = new URL(API_URL + "ip");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();

            externalIp = content.toString().trim();
        } catch (Exception e) {
            logger.error("获取外网ip失败:{}", e.fillInStackTrace());
        }
        return externalIp;
    }
}
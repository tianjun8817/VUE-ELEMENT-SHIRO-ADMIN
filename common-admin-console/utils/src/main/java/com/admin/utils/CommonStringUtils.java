package com.admin.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonStringUtils extends StringUtils {

    public static final String EMPTY = "";

    private static final String REX_IMAGE = ".*\\.(jpg|gif|png|bmp|jpge)$";

    private static final String REX_EMAIL = "^[0-9a-zA-Z]+([\\-_\\.][0-9a-zA-Z]+)*@[0-9a-zA-Z]+(\\-[0-9a-zA-Z]+)*(\\.[0-9a-zA-Z]+(\\-[0-9a-zA-Z]+)*)+$";

    private static final String REX_MAC = "[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]";

    private static final String REX_IPV4 = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";


    /**
     * 是否相等
     *
     * @param c1 字符串1
     * @param c2 字符串2
     * @return 是否
     */
    public static boolean isEquals(String c1, String c2) {
        if (c1 == null && c2 == null)
            return true;
        else if (c1 == null)
            return c2.equalsIgnoreCase(c1);
        else
            return c1.equalsIgnoreCase(c2);
    }

    /**
     * 获取内容，如果内容为空，则使用默认值
     *
     * @param c   内容
     * @param def 字符串
     * @return 转换后的数据
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Object c, T def) {
        if (isEmpty(c)) {
            return def;
        } else {
            if (def != null && !c.getClass().equals(def.getClass())) {
                if (def instanceof Integer)
                    c = (new Double(c.toString())).intValue();
                else if (def instanceof Double)
                    c = Double.parseDouble(c.toString());
                else if (def instanceof Long)
                    c = (new Double(c.toString())).longValue();
                else if (def instanceof Float)
                    c = Float.parseFloat(c.toString());
                else if (def instanceof Date) {
                    String v = rightPadding(c.toString(), 13, "0");
                    c = new Date(Long.parseLong(v));
                } else if (def instanceof String)
                    c = c.toString();
            }
            return (T) c;
        }
    }

    /**
     * 字符串右侧填充
     *
     * @param c       字符串
     * @param length  填充后最长度
     * @param padding 填充物
     * @return String
     */
    public static String rightPadding(String c, int length, String padding) {
        if (c == null)
            c = "";
        int csize = c.length();
        int psize = padding.length();
        while (csize < length) {
            c += padding;
            csize += psize;
        }
        return csize > length ? c.substring(0, length) : c;
    }

    public static String leftPadding(String c, int length, String padding) {
        if (c == null)
            return c;
        int csize = c.length();
        int psize = padding.length();
        while (csize < length) {
            c = padding + c;
            csize += psize;
        }
        return csize > length ? c.substring(0, length - 1) : c;
    }

    /**
     * 文件名判断是否为图片
     *
     * @param filename 文件名
     * @return T/F
     */
    public static boolean isValidImage(String filename) {
        if (isEmpty(filename)) return false;
        String s = filename.trim().toLowerCase();
        Pattern p = Pattern.compile(REX_IMAGE);
        return p.matcher(s).matches();
    }

    /**
     * 是否为数字
     *
     * @param str 字符串
     * @return T/F
     */
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        Matcher isNum = pattern.matcher(str); // matcher是全匹配
        return isNum.matches();
    }

    /**
     * 获取文件后缀名
     *
     * @param filename 文件名
     * @return 字符串
     */
    public static String getFileExtName(String filename) {
        String s = filename.trim().toLowerCase();
        int pos = s.lastIndexOf('.');
        if (pos != -1 && pos < s.length())
            return s.substring(pos + 1);
        else
            return null;
    }

    /**
     * 获取不含路径的文件名
     *
     * @param filename 文件名
     * @return String
     */
    public static String getFilename(String filename) {
        String s = filename.trim().replaceAll("\\\\", "\\/");
        int pos = s.lastIndexOf('/');
        if (pos != -1 && pos < s.length())
            return s.substring(pos + 1);
        else
            return filename;
    }

    /**
     * 去掉两头的换行符、空格
     *
     * @param c 字符串
     * @return String
     */
    public static String trim(String c) {
        if (isEmpty(c)) {
            return c;
        } else {
            return c.replaceAll("^[\\s\\t\\n\\r\\f　]+|[\\s\\t\\n\\r\\f　]+$", "");
        }
    }

    /**
     * 是否为空字符，为NULL或为空均为TURE
     *
     * @param c 字符
     * @return boolean
     */
    public static boolean isEmpty(Object c) {
        return c == null || c.toString().equals("");
    }

    /**
     * 是否相等
     *
     * @param a 可为NULL或其它
     * @param b 可为NULL或其它
     * @return boolean
     */
    public static boolean equals(String a, String b) {
        if (a == null && b == null)
            return true;
        else if (a == null)
            return false;
        else if (b == null)
            return false;
        else
            return a.equals(b);
    }


    /**
     * 判断是否相等
     *
     * @param a 可为NULL或其它
     * @param b 可为NULL或其它
     * @return boolean
     */
    public static boolean equals(Object a, Object b) {
        if (a == null && b == null)
            return true;
        else if (a == null)
            return false;
        else if (b == null)
            return false;
        else
            return a.toString().equals(b.toString());
    }

    /**
     * 是否为空白，在isEmpty基础上先trim再判断
     *
     * @param c 字符串
     * @return boolean
     */
    public static boolean isBlank(Object c) {
        return c == null || c.toString().trim().equals("");
    }

    /**
     * 是否为空白，在isEmpty基础上先trim再判断
     *
     * @param c 字符串
     * @return boolean
     */
    public static boolean isNotBlank(Object c) {
        return c != null && !c.toString().trim().equals("");
    }

    /**
     * 是否为有效的邮箱
     *
     * @param c 邮箱
     * @return boolean
     */
    public static boolean isValidEmail(String c) {
        if (!isEmpty(c)) {
            Pattern p = Pattern.compile(REX_EMAIL);
            return p.matcher(c).matches();
        } else {
            return false;
        }
    }

    /**
     * 是否是有效的MAC地址
     *
     * @param c 字符串
     * @return boolean
     */
    public static boolean isValidMAC(String c) {
        if (!isEmpty(c)) {
            Pattern p = Pattern.compile(REX_MAC);
            return p.matcher(c).matches();
        } else {
            return false;
        }
    }

    /**
     * 是否为有效的IPV4格式
     *
     * @param c 字符串
     * @return T/F
     */
    public static boolean isValidIPv4(String c) {
        if (CommonStringUtils.isEmpty(c))
            return false;
        Matcher match = Pattern.compile(REX_IPV4).matcher(c);
        if (match.matches()) {
            int ip1 = Integer.parseInt(match.group(1));
            int ip2 = Integer.parseInt(match.group(2));
            int ip3 = Integer.parseInt(match.group(3));
            int ip4 = Integer.parseInt(match.group(4));
            return ip1 >= 0 && ip1 <= 255
                    && ip2 >= 0 && ip2 <= 255
                    && ip3 >= 0 && ip3 <= 255
                    && ip4 >= 0 && ip4 <= 255;
        } else {
            return false;
        }
    }

    /**
     * 将IPV4转成数字数组
     *
     * @param c : IPv4 string
     * @return int[]
     */
    public static int[] ipv4ToIntArray(String c) {
        if (CommonStringUtils.isEmpty(c))
            return null;
        Matcher match = Pattern.compile(REX_IPV4).matcher(c);
        if (match.matches()) {
            int ip1 = Integer.parseInt(match.group(1));
            int ip2 = Integer.parseInt(match.group(2));
            int ip3 = Integer.parseInt(match.group(3));
            int ip4 = Integer.parseInt(match.group(4));
            if (ip1 < 0 || ip1 > 255
                    || ip2 < 0 || ip2 > 255
                    || ip3 < 0 || ip3 > 255
                    || ip4 < 0 || ip4 > 255)
                return null;
            else
                return new int[]{ip1, ip2, ip3, ip4};
        } else {
            return null;
        }
    }

    /**
     * 获取序列网卡上第一个IP地址, 外网地址优先返回
     *
     * @return String
     */
    public static String getFristIPv4() {
        String localip = null; // 本地IP，如果没有配置外网IP则返回它
        String netip = null; // 外网IP
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            boolean finded = false;// 是否找到外网IP
            while (netInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
                    if (!ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        if (!ip.isSiteLocalAddress()) {// 外网IP
                            netip = ip.getHostAddress();
                            finded = true;
                            break;
                        } else if (localip == null && ip.isSiteLocalAddress()) {// 内网IP
                            localip = ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    /**
     * 获取本地IP，以eth最小的为主
     *
     * @return String
     */
    public static String getLocalIPv4() {
        String localIp = null; // 本地IP，如果没有配置外网IP则返回它
        String localName = null;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
                    if (!ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")
                            && ip.isSiteLocalAddress()) {
                        if (CommonStringUtils.isEmpty(localName) && CommonStringUtils.isEmpty(localIp)) {
                            localName = ni.getName().toLowerCase();
                            localIp = ip.getHostAddress();
                        } else {
                            String niName = ni.getName().toLowerCase();
                            if (localName.startsWith("eth") && niName.startsWith("eth")
                                    && ((localName.length() == niName.length() && niName.compareTo(localName) <= 0) ||
                                    niName.length() < localName.length())) {
                                //这里是为了保证，同为eth开头时，取eth小的，比如eth5和eth15,取eth5
                                localName = niName;
                                localIp = ip.getHostAddress();
                            } else if (!localName.startsWith("eth") && niName.startsWith("eth")) {
                                //以eth为主
                                localName = niName;
                                localIp = ip.getHostAddress();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localIp;
    }


    /**
     * 获取指定长度的随机字符串
     *
     * @param len 数量
     * @return String
     */
    public static String randomHexString(int len) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            result.append(Integer.toHexString(new Random().nextInt(16)));
        }
        return result.toString().toLowerCase();
    }
}

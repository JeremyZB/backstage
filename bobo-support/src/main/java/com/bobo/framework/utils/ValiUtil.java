/*
 * 文件名：ValiUtil.java
 * 版权：CopyRight 2000-2010 Huawei Technologies Co., Ltd.All Righs Reserved.
 * 描述：
 * 修改人：g69866
 * 修改时间：2011-10-7
 * 修改内容：新增
 */
package com.bobo.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValiUtil
{
    
    /**
     * 判断字符串是否为空
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str)
    {
        if (str == null || str.trim().length() == 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * 校验是否为数字
     * 
     * @param str 校验的字符串
     * @return
     */
    public static boolean isNum(String str)
    {
        String regex = "^[0-9]+$";
        return str.matches(regex);
    }
    
    /**
     * 校验是否为指定长度的数字
     * 
     * @param str 校验的字符串
     * @param length 校验的长度
     * @return
     */
    public static boolean isNum(String str, int length)
    {
        String regex = "^[0-9]{" + length + "}$";
        return str.matches(regex);
    }
    
    /**
     * 校验浮点型数字是否在指定的长度区间内
     * 
     * @param str
     * @param min
     * @param max
     * @return
     */
    public static boolean isNum(String str, String min, String max)
    {
        String regex = "^[[0-9](\\.?)[0-9]]{" + min + "," + max + "}$";
        return str.matches(regex);
    }
    
    /**
     * 校验是否为高危密码
     * 
     * @param password
     * @return
     */
    public static boolean isDangerPassword(String password)
    {
        return password.matches("^1{6}|2{6}|3{6}|4{6}|5{6}|6{6}|7{6}|8{6}|9{6}|0{6}|123456|654321$");
    }
    
    /**
     * 验证是否为指定长度字母数字组合
     * 
     * @param str 校验的字符串
     * @param length 校验的长度
     * @return
     */
    public static boolean isCharacter(String str, int length)
    {
        String regex = "^[a-zA-Z0-9]{" + length + "}$";
        return str.matches(regex);
    }
    
    /**
     * 验证是否为指定字母数字和_组合 主要用于安全加固，防止用户输入特殊字符进行sql注入及跨站脚本注入.
     * 
     * @param str 校验的字符串
     * @param length 校验的长度
     * @return boolean
     */
    public static boolean isCharacter(String str)
    {
        if (str == null)
        {
            return false;
        }
        String regEx = "^[\\w|]+$";
        return str.matches(regEx);
    }
    
    /**
     * 验证是否为在指定长度区域的字母数字组合
     * 
     * @param str
     * @param min
     * @param max
     * @return
     */
    public static boolean isCharacter(String str, String min, String max)
    {
        String regex = "^[a-zA-Z0-9]{" + min + "," + max + "}$";
        return str.matches(regex);
    }
    
    /**
     * 判断是否为合法身份证号码，判断位数和年月
     * 
     * @param idcard
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean isRelaxIdCard(String idcard)
    {
        // Date thisDate = new Date();
        String regex = "^\\d{17}[\\d|X|x]$|^\\d{15}$";
        if (!idcard.matches(regex))
        {
            return false;
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        // 验证15位身份证号码出生日期
        if (idcard.length() == 15)
        {
            int year = Integer.parseInt(idcard.substring(6, 8));
            int month = Integer.parseInt(idcard.substring(8, 10));
            int day = Integer.parseInt(idcard.substring(10, 12));
            Date temp_date = new Date(year, month - 1, day);
            if (!valiYMD(Integer.parseInt(format1.format(temp_date).substring(0, 4)), month, day))
            {
                return false;
            }
            // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法
            if (temp_date.getYear() != year || temp_date.getMonth() != month - 1 || temp_date.getDate() != day)
            {
                return false;
            }
            return true;
        }
        // 验证18为身份证号码出生日期
        if (idcard.length() == 18)
        {
            int year = Integer.parseInt(idcard.substring(6, 10));
            int month = Integer.parseInt(idcard.substring(10, 12));
            int day = Integer.parseInt(idcard.substring(12, 14));
            if (!valiYMD(year, month, day))
            {
                return false;
            }
            Date temp_date = new Date(year, month - 1, day);
            if (temp_date.getYear() != year || temp_date.getMonth() != month - 1 || temp_date.getDate() != day)
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
    /**
     * 验证年月日
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    private static boolean valiYMD(int year, int month, int day)
    {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        if (year > Integer.parseInt(format1.format(new Date()).substring(0, 4)))
        {
            return false;
        }
        if (month < 1 || month > 12)
        {
            return false;
        }
        int dayMax = 31;
        if (month == 2)
        {
            if (year % 100 == 0)
            {
                if (year % 400 == 0)
                {
                    dayMax = 29;
                }
                else
                {
                    dayMax = 28;
                }
            }
            else
            {
                if (year % 4 == 0)
                {
                    dayMax = 29;
                }
                else
                {
                    dayMax = 28;
                }
            }
        }
        else if (month == 4 || month == 6 || month == 9 || month == 11)
        {
            dayMax = 30;
        }
        else
        {
            dayMax = 31;
        }
        if (day < 1 || day > dayMax)
        {
            return false;
        }
        return true;
    }
    
    /**
     * 判断是否为合法身份证号码_严格校验
     * 
     * @param idCard
     * @return
     */
    public static boolean isStrictIdCard(String idcard)
    {
        /*
         * String[][] codeProvince = { { "11", "北京" }, { "12", "天津" }, { "13", "河北" }, { "14", "山西" }, { "15", "内蒙古" }, {
         * "21", "辽宁" }, { "22", "吉林" }, { "23", "黑龙江" }, { "31", "上海" }, { "32", "江苏" }, { "33", "浙江" }, { "34", "安徽" }, {
         * "35", "福建" }, { "36", "江西" }, { "37", "山东" }, { "41", "河南" }, { "42", "湖北" }, { "43", "湖南" }, { "44", "广东" }, {
         * "45", "广西" }, { "46", "海南" }, { "50", "重庆" }, { "51", "四川" }, { "52", "贵州" }, { "53", "云南" }, { "54", "西藏" }, {
         * "61", "陕西" }, { "62", "甘肃" }, { "63", "青海" }, { "64", "宁夏" }, { "65", "新疆" }, { "71", "台湾" }, { "81", "香港" }, {
         * "82", "澳门" }, { "91", "国外" } };
         */
        String[] aCode = new String[] {"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35",
                "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64",
                "65", "71", "81", "82", "91"};
        List<String> alist = Arrays.asList(aCode);
        String JYM, M;
        int S, Y;
        String[] idcard_array = new String[idcard.length()];
        idcard_array = idcard.substring(1, idcard.length()).split("");
        idcard_array[0] = idcard.substring(0, 1);
        String ereg = "";
        if (!alist.contains(idcard.substring(0, 2)))
        {
            return false;
        }
        switch (idcard.length())
        {
            case 15:
                if ((Integer.parseInt(idcard.substring(6, 8)) + 1900) % 4 == 0
                        || ((Integer.parseInt(idcard.substring(6, 8)) + 1900) % 100 == 0 && (Integer.parseInt(idcard.substring(6,
                                8)) + 1900) % 4 == 0))
                {
                    ereg = "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$";// 测试出生日期的合法性
                }
                else
                {
                    ereg = "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$";// 测试出生日期的合法性
                }
                if (idcard.matches(ereg))
                    return true;
                else
                    return false;
            case 18:
                // 出生日期的合法性检查
                if (Integer.parseInt(idcard.substring(6, 10)) % 4 == 0
                        || (Integer.parseInt(idcard.substring(6, 10)) % 100 == 0 && Integer.parseInt(idcard.substring(6,
                                10)) % 4 == 0))
                {
                    ereg = "^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$";// 闰年出生日期的合法性正则表达式
                }
                else
                {
                    ereg = "^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$";// 平年出生日期的合法性正则表达式
                }
                if (idcard.matches(ereg))
                {// 测试出生日期的合法性
                    // 计算校验位
                    S = (Integer.parseInt(idcard_array[0]) + Integer.parseInt(idcard_array[10])) * 7
                            + (Integer.parseInt(idcard_array[1]) + Integer.parseInt(idcard_array[11])) * 9
                            + (Integer.parseInt(idcard_array[2]) + Integer.parseInt(idcard_array[12])) * 10
                            + (Integer.parseInt(idcard_array[3]) + Integer.parseInt(idcard_array[13])) * 5
                            + (Integer.parseInt(idcard_array[4]) + Integer.parseInt(idcard_array[14])) * 8
                            + (Integer.parseInt(idcard_array[5]) + Integer.parseInt(idcard_array[15])) * 4
                            + (Integer.parseInt(idcard_array[6]) + Integer.parseInt(idcard_array[16])) * 2
                            + Integer.parseInt(idcard_array[7]) * 1 + Integer.parseInt(idcard_array[8]) * 6
                            + Integer.parseInt(idcard_array[9]) * 3;
                    Y = S % 11;
                    M = "F";
                    JYM = "10X98765432";
                    M = JYM.substring(Y, Y + 1);// 判断校验位
                    if (M.equalsIgnoreCase(idcard_array[17]))
                        return true; // 检测ID的校验位
                    else
                        return false;
                }
                else
                    return false;
            default:
                return false;
        }
    }
    
    /**
     * 判断是否为合法身份证号码_严格校验
     * 支持年份从1900~1999改为1900~2099年
     * @param idCard
     * @return
     */
    public static boolean isStrictIdCardNew(String idcard)
    {
        /*
         * String[][] codeProvince = { { "11", "北京" }, { "12", "天津" }, { "13", "河北" }, { "14", "山西" }, { "15", "内蒙古" }, {
         * "21", "辽宁" }, { "22", "吉林" }, { "23", "黑龙江" }, { "31", "上海" }, { "32", "江苏" }, { "33", "浙江" }, { "34", "安徽" }, {
         * "35", "福建" }, { "36", "江西" }, { "37", "山东" }, { "41", "河南" }, { "42", "湖北" }, { "43", "湖南" }, { "44", "广东" }, {
         * "45", "广西" }, { "46", "海南" }, { "50", "重庆" }, { "51", "四川" }, { "52", "贵州" }, { "53", "云南" }, { "54", "西藏" }, {
         * "61", "陕西" }, { "62", "甘肃" }, { "63", "青海" }, { "64", "宁夏" }, { "65", "新疆" }, { "71", "台湾" }, { "81", "香港" }, {
         * "82", "澳门" }, { "91", "国外" } };
         */
        String[] aCode = new String[] {"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35",
                "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64",
                "65", "71", "81", "82", "91"};
        List<String> alist = Arrays.asList(aCode);
        String JYM, M;
        int S, Y;
        String[] idcard_array = new String[idcard.length()];
        idcard_array = idcard.substring(1, idcard.length()).split("");
        idcard_array[0] = idcard.substring(0, 1);
        String ereg = "";
        if (!alist.contains(idcard.substring(0, 2)))
        {
            return false;
        }
        switch (idcard.length())
        {
            case 15:
                if ((Integer.parseInt(idcard.substring(6, 8)) + 1900) % 4 == 0
                        || ((Integer.parseInt(idcard.substring(6, 8)) + 1900) % 100 == 0 && (Integer.parseInt(idcard.substring(6,
                                8)) + 1900) % 4 == 0))
                {
                    ereg = "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$";// 测试出生日期的合法性
                }
                else
                {
                    ereg = "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$";// 测试出生日期的合法性
                }
                if (idcard.matches(ereg))
                    return true;
                else
                    return false;
            case 18:
                // 出生日期的合法性检查
                if (Integer.parseInt(idcard.substring(6, 10)) % 4 == 0
                        || (Integer.parseInt(idcard.substring(6, 10)) % 100 == 0 && Integer.parseInt(idcard.substring(6,
                                10)) % 4 == 0))
                {
                    ereg = "^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$";// 闰年出生日期的合法性正则表达式
                }
                else
                {
                    ereg = "^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$";// 平年出生日期的合法性正则表达式
                }
                if (idcard.matches(ereg))
                {// 测试出生日期的合法性
                    // 计算校验位
                    S = (Integer.parseInt(idcard_array[0]) + Integer.parseInt(idcard_array[10])) * 7
                            + (Integer.parseInt(idcard_array[1]) + Integer.parseInt(idcard_array[11])) * 9
                            + (Integer.parseInt(idcard_array[2]) + Integer.parseInt(idcard_array[12])) * 10
                            + (Integer.parseInt(idcard_array[3]) + Integer.parseInt(idcard_array[13])) * 5
                            + (Integer.parseInt(idcard_array[4]) + Integer.parseInt(idcard_array[14])) * 8
                            + (Integer.parseInt(idcard_array[5]) + Integer.parseInt(idcard_array[15])) * 4
                            + (Integer.parseInt(idcard_array[6]) + Integer.parseInt(idcard_array[16])) * 2
                            + Integer.parseInt(idcard_array[7]) * 1 + Integer.parseInt(idcard_array[8]) * 6
                            + Integer.parseInt(idcard_array[9]) * 3;
                    Y = S % 11;
                    M = "F";
                    JYM = "10X98765432";
                    M = JYM.substring(Y, Y + 1);// 判断校验位
                    if (M.equalsIgnoreCase(idcard_array[17]))
                        return true; // 检测ID的校验位
                    else
                        return false;
                }
                else
                    return false;
            default:
                return false;
        }
    }
    
    
    /**
     * Email检查
     * 
     * @param email 要检查的字符串
     * @return boolean 检查结果
     */
    public static boolean isEmail(String email)
    {
        if (email == null)
        {
            return false;
        }
        String regEx = "([\\w[_-][\\.]]+@+[\\w[_-]]+\\.+[A-Za-z]{2,3})|([\\"
                + "w[_-][\\.]]+@+[\\w[_-]]+\\.+[\\w[_-]]+\\.+[A-Za-z]{2,3})|"
                + "([\\w[_-][\\.]]+@+[\\w[_-]]+\\.+[\\w[_-]]+\\.+[\\w[_-]]+" + "\\.+[A-Za-z]{2,3})";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(email);
        return matcher.matches();
    }
    
    /**
     * 特殊字符检查
     * 
     * @param str 被检查字符串
     * @return result 检查后的结果
     */
    public static boolean isSpecialChar(String str)
    {
        if (str == null)
        {
            return false;
        }
        String regEx = "^.*[%|&|'|>|<|\\\\|/]+.*$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(Pattern.compile("[\\r|\\n]").matcher(str).replaceAll(""));
        return matcher.matches();
    }
    
    /**
     * 日期检查
     * 
     * @param pInput 要检查的字符串
     * @return boolean 检查结果
     */
    public static boolean isDate(String date)
    {
        if (date == null)
        {
            return false;
        }
        String regEx = "^((\\d{2}(([02468][048])|([13579][26]))-((((0?[1357"
                + "8])|(1[02]))-((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])"
                + "|(11))-((0?[1-9])|([1-2][0-9])|(30)))|(0?2-((0?[1-9])|([1-"
                + "2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]"
                + "))[\\-\\/\\s]?((((0?[13578])|(1[02]))-((0?[1-9])|([1-2][0-"
                + "9])|(3[01])))|(((0?[469])|(11))-((0?[1-9])|([1-2][0-9])|(3"
                + "0)))|(0?2-((0?[1-9])|(1[0-9])|(2[0-8]))))))$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(date);
        return matcher.matches();
    }
    
    /**
     * 校验是否为手机号码
     * 
     * @param str 校验的字符串
     * @return
     */
    public static boolean isMobile(String str)
    {
        String regex = "^1\\d{10}$";
        String regex1 = "^15[0-9]\\d{8}$";
        return str.matches(regex) || str.matches(regex1);
    }
    
    /**
     * 特殊字符检查
     * 
     * @param str 被检查字符串
     * @return result 检查后的结果
     */
    public static boolean isNicName(String nicName)
    {
        if (nicName == null)
        {
            return false;
        }
        String regEx = "^[\\w|\u4e00-\u9fa5]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(nicName);
        return matcher.matches();
    }
    
    /**
     * 校验是否为sim卡卡号
     * 
     * @param str 校验的字符串
     * @return true or false
     */
    public static boolean isSimcardNumber(String str)
    {
        String regex = "^[0-9]{20}$";
        return str.matches(regex);
    }
    
	/**
	 * 判断字符串是否为浮点型数字
	 * 
	 * @param str
	 * @return boolean
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isNumeric(String str)
	{
		String regex = "^0|[1-9]\\d*|[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
		return str.matches(regex);

	}
	
    /**
     * 大小写字母数字和中文组成的姓名校验
     * @param userName
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkUserName(String userName) {
    	  String regex = "([a-z]|[A-Z]|[0-9]|[\\u4e00-\\u9fa5])+";
    	  Pattern p = Pattern.compile(regex);
    	  Matcher m = p.matcher(userName);
    	  return m.matches();
    }
    
    /**
     * 大小写字母数字和中文组成的姓名校验
     * @param userName
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isIP(String addr)
    {
        if(addr.length() < 7 || addr.length() > 15 || "".equals(addr))
        {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        
        Pattern pat = Pattern.compile(rexp);  
        
        Matcher mat = pat.matcher(addr);  
        
        boolean ipAddress = mat.find();

        return ipAddress;
    }
}

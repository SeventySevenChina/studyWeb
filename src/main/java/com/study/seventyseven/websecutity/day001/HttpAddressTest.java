package com.study.seventyseven.websecutity.day001;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.io.file.FileReader;

/**
 * @author SeventySeven
 * @Title: HttpAddressTest
 * @Package com.study.seventyseven.websecutity.day001;
 * @Description: 用于测试day001域名爆破原理测试
 * @email SeventySevenChina@protonmail.com
 * @date 2021/4/25 13:35
 */
public class HttpAddressTest {
    public static void main(String[] args) throws IOException {
        File file = new File("D://webSecurityTest/address.txt");
        //文件简单使用","风格这里就用,进行切割了
        String[] split = new FileReader(file).readString().split(",");
        Map<Integer, Set<String>> addressMap = new HashMap<>();
        //两个集合来装一会开两个线程来ping
        for (int i = 0; i < split.length; i++) {
            //这里之所以使用i%number 主要是如果文件较大 仅需要增加number的值即可创建多个set也就是开启多个线程提升效率
            Set<String> set = getSet(addressMap, i % 2);
            set.add(split[i]);
            addressMap.put(i % 2, set);
        }
        //新建两个线程出来ping
        addressMap.forEach((k, v) -> {
            tes(v);
        });
        /**
         * 运行结果 这里可以看到所有能够ping通的即为有效二级域名
         * news.baidu.com
         * a.baidu.com
         * a.baidu.com
         * i.baidu.com
         * u.baidu.com
         * baike.baidu.com
         * y.baidu.com
         * ..
         */
    }

    private static void tes(Set<String> set) {
        new Thread(() -> {
            String e = ".baidu.com";
            for (String s : set) {
                String result = s + e;
                boolean reachable = false;
                try {
                    reachable = InetAddress.getByName(result).isReachable(60);
                } catch (IOException ignored) {
                }
                if (reachable) {
                    System.out.println(result);
                }
            }
        }).start();
    }

    private static Set<String> getSet(Map<Integer, Set<String>> addressMap,
                                      Integer number) {
        Set<String> strings = addressMap.get(number);
        if (strings != null) {
            return strings;
        }
        return new HashSet<>();
    }
}

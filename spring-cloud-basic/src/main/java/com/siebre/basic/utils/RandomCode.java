package com.siebre.basic.utils;

import java.util.Random;

/**
 * @author Huang Tianci
 */
public class RandomCode {

    /**
     * 随机生成3位非零不同的数字
     * @return
     */
    public static String getRandomCode3(){
        String code = "";
        int n = 10;
        Random rand = new Random();
        boolean[] bool = new boolean[n];
        bool[0] = true;
        int num =0;
        for (int i = 0; i<3; i++){
            do{
                //如果产生的数相同继续循环
                num = rand.nextInt(n);
            }while(bool[num]);
            bool[num] =true;
            code = code + num;
        }
        return code;
    }

    /**
     * 随机生成6位不同的数字
     * @return
     */
    public static String getRandomCode6(){
        String code = "";
        int n = 10;
        Random rand = new Random();
        boolean[] bool = new boolean[n];
        int num =0;
        for (int i = 0; i<6; i++){
            do{
                //如果产生的数相同继续循环
                num = rand.nextInt(n);
            }while(bool[num]);
            bool[num] =true;
            code = code + num;
        }
        return code;
    }

    /**
     * 随机生成6位数字
     * @return
     */
    public static String getRandom(){
        String code = "";
        int n = 10;
        Random rand = new Random();
        int num =0;
        for (int i = 0; i<6; i++){
            num = rand.nextInt(n);
            code = code + num;
        }
        return code;
    }

    /*public static void main(String[] args) {
        for (int i = 0; i <12; i++) {

            System.out.println(RandomCode.getRandom());
        }
    }*/
}

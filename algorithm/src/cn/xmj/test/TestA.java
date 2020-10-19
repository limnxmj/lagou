package cn.xmj.test;

import java.util.Arrays;

/**
 * 判断数组中所有的数字是否只出现一次。
 * 给定一个数组array，
 * 判断数组 array 中是否所有的数字都只 出现过一次。
 * 例如，arr = {1, 2, 3}，输出 YES。
 * 又如，arr = {1, 2, 1}，输出 NO。
 * 约束时间复杂度为 O(n)。
 */
public class TestA {

    private static final String YES = "YES";
    private static final String NO = "NO";

    private static String isSingle(int[] arr) {
        if (arr == null || arr.length == 0) {
            return NO;
        }
        if (arr.length == 1) {
            return YES;
        }

        //找出最大值和最小值
        int max = arr[0];
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }

        //如果最大值和最小值相等，返回NO
        if (min == max) {
            return NO;
        }

        int diff = max - min;
        int[] temp = new int[diff + 1];
        for (int i = 0; i < arr.length; i++) {
            if (temp[arr[i] - min] != 0) {
                return NO;
            }
            temp[arr[i] - min]++;
        }
        return YES;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        int[] arr2 = {1, 1};
        System.out.println(isSingle(arr));
        System.out.println(isSingle(arr2));
    }
}

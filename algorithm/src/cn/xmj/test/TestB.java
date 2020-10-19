package cn.xmj.test;

import javafx.util.Pair;

import java.util.*;

/**
 * 很久很久以前，有一位国王拥有5座金矿，每座金矿的黄金储量不同，需要参与挖掘的工人人数也不同。
 * 例如有的金矿储量是500kg黄金，需要5个工人来挖掘；有的金矿储量是200kg黄金，需要3个工人 来挖掘……
 * 如果参与挖矿的工人的总数是10。每座金矿要么全挖，要么不挖，不能派出一半人挖取一半的金矿。
 * 要求用程序求出，要想得到尽可能多的黄金，应该选择挖取哪几座金矿？
 */
public class TestB {


    /**
     * 假设金矿和所需人数如下所示：
     * 金矿1    500kg   5人   100
     * 金矿2    200kg   3人   67
     * 金矿3    100kg   2人   50
     * 金矿4    300kg   4人   75
     * 金矿5    400kg   5人   80
     * 金矿要么挖，要么不挖
     */

    public static Pair<String, LinkedList> digGold(int[] goldWeights, int[] persons, int maxPerson) {

        if (goldWeights == null || goldWeights.length == 0
                || persons == null || persons.length == 0
                || goldWeights.length != persons.length
                || maxPerson <= 0) {
            return null;
        }
        Set<Integer> golds = new HashSet<>();

        Map<String, LinkedList> map = new HashMap<>();

        int[][] dp = new int[goldWeights.length + 1][maxPerson + 1];
        for (int i = 1; i <= goldWeights.length; i++) {
            for (int j = 1; j <= maxPerson; j++) {
                String key = i + "_" + j;
                LinkedList list = map.get(key);
                if (list == null) {
                    list = new LinkedList();
                    map.put(key, list);
                }

                if (j < persons[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                    addAll(list, map.get((i - 1) + "_" + j));
                } else {
                    int choose = goldWeights[i - 1] + dp[i - 1][j - persons[i - 1]];
                    int not = dp[i - 1][j];
                    if (choose >= not) {
                        golds.add(i - 1);
                        dp[i][j] = choose;
                        addAll(list, map.get((i - 1) + "_" + (j - persons[i - 1])));
                        list.add(i - 1);
                    } else {
                        dp[i][j] = not;
                        addAll(list, map.get((i - 1) + "_" + j));
                    }

                }
            }
        }
        return new Pair(dp[goldWeights.length][maxPerson], map.get(goldWeights.length + "_" + maxPerson));
    }

    private static void addAll(LinkedList list, LinkedList src) {
        if (src != null) {
            list.addAll(src);
        }
    }

    public static void main(String[] args) {
        int[] goldWeights = {500, 200, 100, 300, 400};
        int[] persons = {5, 3, 2, 4, 5};
        int maxPerson = 10;
        Pair result = digGold(goldWeights, persons, maxPerson);
        System.out.println("总共挖金矿：" + result.getKey() + "kg");
        System.out.println("挖金矿：" + result.getValue());
    }



}

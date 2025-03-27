package top.kloping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Demo04734_3 {

    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("请输入第一个文件路径：");
        String filePath1 = SCANNER.nextLine();
        System.out.println("请输入第二个文件路径：");
        String filePath2 = SCANNER.nextLine();
        String s1 = readFile(filePath1);
        String s2 = readFile(filePath2);

        String lcs = findLongestCommonSubsequence(s1, s2);
        System.out.println(lcs);
    }

    private static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    private static String findLongestCommonSubsequence(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        // 构建DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // 回溯获取LCS
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                lcs.append(s1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }

        return lcs.reverse().toString();
    }
}
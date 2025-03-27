package top.kloping;

import java.util.*;

public class Demo02327_3 {

    static class Result {
        List<List<Integer>> accessOrder;
        int missCount;

        public Result(List<List<Integer>> accessOrder, int missCount) {
            this.accessOrder = accessOrder;
            this.missCount = missCount;
        }
    }

    public static Result simulateFIFO(int n, List<Integer> pages) {
        Queue<Integer> queue = new LinkedList<>();
        List<List<Integer>> accessOrder = new ArrayList<>();
        int miss = 0;

        for (int page : pages) {
            if (queue.contains(page)) {
                accessOrder.add(new ArrayList<>(queue));
                continue;
            }

            miss++;
            if (queue.size() >= n) {
                queue.poll();
            }
            queue.offer(page);
            accessOrder.add(new ArrayList<>(queue));
        }

        return new Result(accessOrder, miss);
    }

    public static Result simulateLRU(int n, List<Integer> pages) {
        LinkedList<Integer> list = new LinkedList<>();
        List<List<Integer>> accessOrder = new ArrayList<>();
        int miss = 0;

        for (int page : pages) {
            int index = list.indexOf(page);
            if (index != -1) {
                list.remove(index);
                list.addLast(page);
                accessOrder.add(new ArrayList<>(list));
                continue;
            }

            miss++;
            if (list.size() >= n) {
                list.removeFirst();
            }
            list.addLast(page);
            accessOrder.add(new ArrayList<>(list));
        }

        return new Result(accessOrder, miss);
    }

    private static void printAccessOrder(List<List<Integer>> accessOrder) {
        for (int i = 0; i < accessOrder.size(); i++) {
            System.out.printf("第%d次访问后: %s%n", i + 1, accessOrder.get(i));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入物理块个数n: ");
        int n = scanner.nextInt();
        scanner.nextLine();

        System.out.print("请输入页面序列（空格分隔）: ");
        String[] input = scanner.nextLine().split(" ");
        List<Integer> pageSequence = new ArrayList<>();
        for (String s : input) {
            pageSequence.add(Integer.parseInt(s));
        }

        Result fifoResult = simulateFIFO(n, pageSequence);
        Result lruResult = simulateLRU(n, pageSequence);

        System.out.println("\nFIFO算法结果:");
        printAccessOrder(fifoResult.accessOrder);
        System.out.println("缺页次数: " + fifoResult.missCount);
        System.out.printf("缺页率: %.2f%%%n", (fifoResult.missCount * 100.0) / pageSequence.size());

        System.out.println("\nLRU算法结果:");
        printAccessOrder(lruResult.accessOrder);
        System.out.println("缺页次数: " + lruResult.missCount);
        System.out.printf("缺页率: %.2f%%%n", (lruResult.missCount * 100.0) / pageSequence.size());
    }
}
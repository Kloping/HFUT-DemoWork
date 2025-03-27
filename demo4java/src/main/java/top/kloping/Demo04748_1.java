package top.kloping;

import java.io.*;

/**
 * （1）文件处理
 * 设计 file1.dat、file2.dat、file3.dat 三个文件，在函数"work"中实
 * 现将 file1.dat 文件中的英文字母写入 file2.dat,数字写入 file3.dat 中，
 * 其他字符不处理。
 * 要求：文件流读取时使用 read 方法。
 * 例子：file1.dat 中内容为 aaaaa $1234.56 bbbbb，则 file2.dat 中
 * 内容为 aaaaabbbbb，file3.dat 中内容为 123456。
 */
public class Demo04748_1 {
    public static void main(String[] args) {

        work();
    }

    private static File file1 = new File("file1.dat");
    private static File file2 = new File("file2.dat");
    private static File file3 = new File("file3.dat");

    public static void work() {
        // TODO
        try {
            FileInputStream fis = new FileInputStream(file1);
            FileOutputStream fos2 = new FileOutputStream(file2);
            FileOutputStream fos3 = new FileOutputStream(file3);
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            while (true) {
                int read = fis.read();
                if (read == -1) {
                    break;
                }
                sb1.append((char) read);
                if (read >= 'a' && read <= 'z' || read >= 'A' && read <= 'Z') {
                    fos2.write(read);
                }
                if (read >= '0' && read <= '9') {
                    fos3.write(read);
                }
            }
            System.out.println("处理完毕!");
        } catch (FileNotFoundException e) {
            System.err.println("文件未发现报错");
        } catch (IOException ex) {
            System.err.println("IO错误");
        }
    }
}
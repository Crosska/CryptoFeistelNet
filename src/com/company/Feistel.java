package com.company;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Feistel {

    private final Scanner scan = new Scanner(System.in);
    private final int char_count = 4;
    private final int rounds = 8;
    private byte[] x1 = new byte[4];
    private byte[] x2 = new byte[4];
    private byte[] x3 = new byte[4];
    private byte[] x4 = new byte[4];
    private byte[] z1 = new byte[4];
    private byte[] z2 = new byte[4];
    private byte[] z3 = new byte[4];
    private byte[] z4 = new byte[4];

    public void EncryptEnterData() throws IOException {
        String data = "";
        while (data.isEmpty() || data.length() > 16) {
            System.out.println("\nВведите данные для шифрования (до 16 символов):");
            data = scan.nextLine();
        }

        while (data.length() != 16) {
            data = data + "\0";
        }

        String[] divided_data = data.split("(?<=\\G.{4})"); // Делим данные на 4 потока по 4 символа

        z1 = divided_data[0].getBytes();
        z2 = divided_data[1].getBytes();
        z3 = divided_data[2].getBytes();
        z4 = divided_data[3].getBytes();

        String key_string = "";
        while (key_string.length() != 4) {
            System.out.println("Введите ключ шифровки данных (4 символа):");
            key_string = scan.nextLine();
        }

        byte[] key = key_string.getBytes();
        for (int i = 0; i < 7; i++) {
            key = GenerateEncryptKey(key);
            Cryptor(key);
        }

        x1 = z4;
        x2 = z1;
        x3 = z2;
        x4 = z3;

        System.out.println("Зашифрованные данные: ");

        for (byte b : x1) {
            String bin = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
            System.out.println("" + bin);
        }
        System.out.println();
        for (byte b : x2) {
            String bin = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
            System.out.println("" + bin);
        }
        System.out.println();
        for (byte b : x3) {
            String bin = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
            System.out.println("" + bin);
        }
        System.out.println();
        for (byte b : x4) {
            String bin = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
            System.out.println("" + bin);
        }
        System.out.println();

        FileOutputStream os = new FileOutputStream("E:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\b.txt");
        os.write(x1);
        os.write(x2);
        os.write(x3);
        os.write(x4);
        os.close();

    }

    public void DecryptFileData() throws IOException {

        String key_string = "";
        while (key_string.length() != 4) {
            System.out.println("Введите ключ расшифровки данных:");
            key_string = scan.nextLine();
        }

        String file_data_path = "";
        while (file_data_path.isEmpty()) {
            System.out.print("\nВведите название файла с зашифрованными данными (.txt)\nE:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\");
            file_data_path = scan.nextLine();
        }

        String final_path = "E:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\" + file_data_path;

        File file = new File(final_path);
        if (file.exists()) {
            InputStream is = new FileInputStream(file);

            byte[] bytes1 = new byte[4];
            is.read(bytes1, 0, 4);

            byte[] bytes2 = new byte[4];
            is.read(bytes2, 0, 4);

            byte[] bytes3 = new byte[4];
            is.read(bytes3, 0, 4);

            byte[] bytes4 = new byte[4];
            is.read(bytes4, 0, 4);

            System.out.println("\nСчитанные байты из файла:\n");

            for (byte b : bytes1) {
                String bin = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
                System.out.println(bin);
            }
            System.out.println();
            for (byte b : bytes2) {
                String bin = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
                System.out.println(bin);
            }
            System.out.println();
            for (byte b : bytes3) {
                String bin = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
                System.out.println(bin);
            }
            System.out.println();
            for (byte b : bytes4) {
                String bin = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
                System.out.println(bin);
            }
            System.out.println();

            z1 = bytes1;
            z2 = bytes2;
            z3 = bytes3;
            z4 = bytes4;

            System.out.println("Прогонка ключа:");
            byte[] key = key_string.getBytes();
            for (int i = 0; i < 8; i++) {
                key = GenerateEncryptKey(key);
            }
            System.out.println("Прогонка ключа (конец)\n");

            for (int i = 0; i < 7; i++) {
                key = GenerateDecryptKey(key);
                Cryptor(key);
            }

            x1 = z2;
            x2 = z3;
            x3 = z4;
            x4 = z1;

            System.out.println("\nРасшифрованные данные:\n");

            Charset ch = Charset.forName("windows-1251");
            String out = new String(x1, ch);
            System.out.println(out);

            out = new String(x2, ch);
            System.out.println(out);

            out = new String(x3, ch);
            System.out.println(out);

            out = new String(x4, ch);
            System.out.println(out);

        }
    }

    public byte[] GenerateEncryptKey(byte[] key) {
        byte[] final_key = new byte[4];
        final_key[0] = (byte) (key[3]);
        final_key[1] = (byte) (~key[0]);
        final_key[2] = (byte) (key[1]);
        final_key[3] = (byte) (~key[2]);

        Charset ch = Charset.forName("windows-1251");
        String out = new String(final_key, ch);
        System.out.println(out);

        return final_key;
    }

    public byte[] GenerateDecryptKey(byte[] key) {
        byte[] final_key = new byte[4];
        final_key[0] = (byte) (~key[1]);
        final_key[1] = (byte) (key[2]);
        final_key[2] = (byte) (~key[3]);
        final_key[3] = (byte) (key[0]);

        Charset ch = Charset.forName("windows-1251");
        String out = new String(final_key, ch);
        System.out.println(out);

        return final_key;
    }

    public void Cryptor(byte[] key) {
        byte[] temp = new byte[4];

        x1 = z1;
        x2 = z2;
        x3 = z3;
        x4 = z4;

        z1[0] = (byte) (z1[0] ^ key[0]);
        z1[1] = (byte) (z1[1] ^ key[1]);
        z1[2] = (byte) (z1[2] ^ key[2]);
        z1[3] = (byte) (z1[3] ^ key[3]);

        z2[0] = (byte) (z2[0] ^ key[0]);
        z2[1] = (byte) (z2[1] ^ key[1]);
        z2[2] = (byte) (z2[2] ^ key[2]);
        z2[3] = (byte) (z2[3] ^ key[3]);

        z3[0] = (byte) (z3[0] ^ key[0]);
        z3[1] = (byte) (z3[1] ^ key[1]);
        z3[2] = (byte) (z3[2] ^ key[2]);
        z3[3] = (byte) (z3[3] ^ key[3]);

        z4[0] = (byte) (z4[0] ^ key[0]);
        z4[1] = (byte) (z4[1] ^ key[1]);
        z4[2] = (byte) (z4[2] ^ key[2]);
        z4[3] = (byte) (z4[3] ^ key[3]);

        // z4 = x1
        //z4 = x1;

        /*z4[0] = x1[0];
        temp[0] = (byte) (x1[0] ^ key[0]);
        z1[0] = (byte) (temp[0] ^ x2[0]);
        temp[0] = (byte) (x1[0] ^ key[0]);
        z2[0] = (byte) (temp[0] ^ x3[0]);
        temp[0] = (byte) (x1[0] ^ key[0]);
        z3[0] = (byte) (temp[0] ^ x4[0]);

        z4[1] = x1[1];
        temp[1] = (byte) (x1[1] ^ key[1]);
        z1[1] = (byte) (temp[1] ^ x2[1]);
        temp[1] = (byte) (x1[1] ^ key[1]);
        z2[1] = (byte) (temp[1] ^ x3[1]);
        temp[1] = (byte) (x1[1] ^ key[1]);
        z3[1] = (byte) (temp[1] ^ x4[1]);

        z4[2] = x1[2];
        temp[2] = (byte) (x1[2] ^ key[2]);
        z1[2] = (byte) (temp[2] ^ x2[2]);
        temp[2] = (byte) (x1[2] ^ key[2]);
        z2[2] = (byte) (temp[2] ^ x3[2]);
        temp[2] = (byte) (x1[2] ^ key[2]);
        z3[2] = (byte) (temp[2] ^ x4[2]);

        z4[3] = x1[3];
        temp[3] = (byte) (x1[3] ^ key[3]);
        z1[3] = (byte) (temp[3] ^ x2[3]);
        temp[3] = (byte) (x1[3] ^ key[3]);
        z2[3] = (byte) (temp[3] ^ x3[3]);
        temp[3] = (byte) (x1[3] ^ key[3]);
        z3[3] = (byte) (temp[3] ^ x4[3]);*/

        // z1 = (x1 xor key) xor x2
        /*for (int j = 0; j < 4; j++) {
            temp[j] = (byte) (x1[j] ^ key[j]);
        }
        System.out.println();
        for (int j = 0; j < 4; j++) {
            z1[j] = (byte) (temp[j] ^ x2[j]);
        }

        // z2 = (x1 xor key) xor x3
        for (int j = 0; j < 4; j++) {
            temp[j] = (byte) (x1[j] ^ key[j]);
        }
        System.out.println();
        for (int j = 0; j < 4; j++) {
            z2[j] = (byte) (temp[j] ^ x3[j]);
        }

        // z3 = (x1 xor key) xor x4
        for (int j = 0; j < 4; j++) {
            temp[j] = (byte) (x1[j] ^ key[j]);
        }
        System.out.println();
        for (int j = 0; j < 4; j++) {
            z3[j] = (byte) (temp[j] ^ x4[j]);
        }*/
    }

}

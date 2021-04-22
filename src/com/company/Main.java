package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Richelieu first = new Richelieu();
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("\n --- Шифр Ришелье --- " +
                    "\n1. Ввести свои данные" +
                    "\n2. Данные из файла" +
                    "\n3. Расшифровка данных" +
                    "\n4. Установить размер блока" +
                    "\n5. Переход к Фейстелю" +
                    "\nВыбор:");
            boolean error = false;
            while (!error) {
                if (scan.hasNextInt()) {
                    error = true;
                } else {
                    System.out.print("\nВы ввели не цифру из меню\nВыбор:");
                    scan.nextLine();
                }
            }
            switch (scan.nextInt()) {
                case 1 -> first.EncryptEnterData();
                case 2 -> first.EncryptFileData();
                case 3 -> first.DecryptData();
                case 4 -> first.ChangeBlockSize();
                case 5 -> secondChoice();
                default -> System.out.print("\nВы ввели не цифру из меню\nВыбор:");
            }
        }
    }

    public static void secondChoice() throws IOException {
        Feistel second = new Feistel();
        Scanner scan = new Scanner(System.in);
        boolean cycle = true;
        while (cycle) {
            System.out.print("\n --- Шифр Фейстель --- " +
                    "\n1. Ввести свои данные" +
                    "\n2. Данные из файла" +
                    "\n3. Расшифровка данных" +
                    "\n4. NOTHING" +
                    "\n0. Назад" +
                    "\nВыбор:");
            boolean error = false;
            while (!error) {
                if (scan.hasNextInt()) {
                    error = true;
                } else {
                    System.out.print("\nВы ввели не цифру из меню\nВыбор:");
                    scan.nextLine();
                }
            }
            switch (scan.nextInt()) {
                case 1:
                    second.EncryptEnterData();
                    break;
                case 2:
                    System.out.println();
                    break;
                case 3:
                    second.DecryptFileData();
                    break;
                case 4:
                    String key = "suka";
                    byte[] key_byte = key.getBytes();
                    key_byte = second.GenerateDecryptKey(key_byte);
                    key_byte = second.GenerateEncryptKey(key_byte);
                    Charset ch = Charset.forName("windows-1251");
                    key = new String(key_byte, ch);
                    System.out.println(key);
                    break;
                case 0:
                    cycle = false;
                    break;
                default:
                    System.out.print("\nВы ввели не цифру из меню\nВыбор:");
                    break;
            }
        }
    }
}

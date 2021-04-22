package com.company;

import java.io.*;
import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class Richelieu {

    private final Scanner scan = new Scanner(System.in);
    private final Random rand = new Random();

    private int block = 2; // размер блока

    public void DecryptData() throws IOException {
        String file_crypt_path = "";
        while (file_crypt_path.isEmpty()) {
            System.out.print("\nВведите название с алгоритмом шифрования (.txt)\nE:\\Documents\\4.1\\Безопасность\\labs\\Lab2--\\");
            file_crypt_path = scan.nextLine();
        }
        String final_path = "E:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\" + file_crypt_path;
        File file = new File(final_path);
        if (file.exists()) {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine(); // Считывание первой строки из алгоритма (размер блока)
            int block_readed = Integer.parseInt(line); // Перевод строки в число
            line = reader.readLine(); // Считывание первой строки из алгоритма (размер блока)
            String[][] crypto_map = new String[line.length()][line.length()];
            reader.close();
            fr.close();

            fr = new FileReader(file);
            reader = new BufferedReader(fr);
            reader.readLine();

            int i = 0;
            while (line != null) { // Считывание решетки Ришелье
                line = reader.readLine();
                try {
                    String[] temp_row = line.split("(?<=\\G.{1})");
                    for (int j = 0; j < crypto_map.length; j++) {
                        crypto_map[i][j] = temp_row[j];
                    }
                    i++;
                } catch (Exception ex) {
                }
            }

            System.out.println("\n\n --- Алгоритм распознан успешно --- \n\n");
            reader.close();
            fr.close();

            String file_data_path = "";
            while (file_data_path.isEmpty()) {
                System.out.print("\nВведите название файла с зашифрованными данными (.txt)\nE:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\");
                file_data_path = scan.nextLine();
            }

            final_path = "E:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\" + file_data_path;

            file = new File(final_path);
            if (file.exists()) {
                fr = new FileReader(file);
                reader = new BufferedReader(fr);
                String encrypted_line = reader.readLine(); // Считывание зашифрованной строки данных
                System.out.println("Зашифрованная строка: " + encrypted_line);

                reader.close();
                fr.close();

                String[] str = encrypted_line.split("(?<=\\G.{" + block_readed + "})");
                StringBuilder decrypted_line = new StringBuilder();

                for (i = 0; i < crypto_map.length; i++) {
                    int start_pos = crypto_map.length * i;
                    for (int j = 0; j < crypto_map.length; j++) {
                        if (crypto_map[i] != null && crypto_map[i][j].equals("1")) {
                            decrypted_line.append(str[start_pos + j]);
                        }
                    }
                }

                System.out.println("Расшифрованные данные: " + decrypted_line);
            } else {
                System.out.println("Указанный вами файл не существует!");
            }
        } else {
            System.out.println("Указанный вами файл не существует!");
        }
    }

    public void EncryptFileData() throws IOException {
        String file_data_path = "";
        while (file_data_path.isEmpty()) {
            System.out.print("\nВведите название файла с данными для шифровки (.txt)\nD:\\");
            file_data_path = scan.nextLine();
        }
        String final_path = "E:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\" + file_data_path;
        File file = new File(final_path);
        StringBuilder data_b = new StringBuilder();
        if (file.exists()) {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            String line = reader.readLine(); // Считывание первой строки из алгоритма (размер блока)
            while (line != null) {
                System.out.println(line);
                data_b.append(line);
                line = reader.readLine(); // Считывание первой строки из алгоритма (размер блока)
            }
            reader.close();
            fr.close();

            String data = data_b.toString();

            System.out.println("Считанные данные:" + data + "\nДлина данных:" + data.length());

            if (data.length() >= block) {
                System.out.println("\nРазмер блока = " +
                        block +
                        "\nДлина данных = " +
                        data.length() +
                        "\nКоличество блоков без остатка = " +
                        (data.length() / block));

                int data_block = data.length() / block; // Рассчет количества блоков для данных
                if (data.length() % block != 0) data_block++; // Добавление блока на остаток данных

                System.out.println("\nКоличество итоговых блоков = " + data_block);

                int[][] crypto_map = new int[data_block][data_block]; // Создание массива решетки ришелье
                String[] divided_data = data.split("(?<=\\G.{" + block + "})"); // Данные разбитые на блоки

                for (int i = 0; i < crypto_map.length; i++) { // Случайное создание решетки ришелье
                    int hole_in_map = rand.nextInt(data_block);
                    for (int j = 0; j < crypto_map.length; j++) {
                        if (j == hole_in_map) crypto_map[i][j] = 1;
                        else {
                            crypto_map[i][j] = 0;
                        }
                    }
                }

                System.out.println();
                for (int[] ints : crypto_map) { // Вывод решетки ришелье
                    for (int j = 0; j < crypto_map.length; j++) {
                        System.out.print(" " + ints[j]);
                    }
                    System.out.println();
                }

                try (FileWriter writer = new FileWriter("E:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\alg.txt", false)) {
                    writer.write(String.valueOf(block));
                    writer.write("\n");
                    for (int i = 0; i < crypto_map.length; i++) { // Запись решетки ришелье в файл
                        for (int j = 0; j < crypto_map.length; j++) {
                            writer.write(String.valueOf(crypto_map[i][j]));
                        }
                        writer.write('\n');
                    }
                    writer.flush();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

                StringBuilder encrypted_string = new StringBuilder(); // Шифрование данных методом Ришелье
                for (int i = 0; i < crypto_map.length; i++) {

                    for (int j = 0; j < crypto_map[i].length; j++) {

                        if (crypto_map[i][j] == 1) {
                            String data_to_write = divided_data[i];
                            if (divided_data[i].length() < block) {

                                for (int k = 0; k < (block - divided_data[i].length()); k++) { // Цикл добавляющий недостающие символы
                                    data_to_write = data_to_write + " ";
                                }

                            }

                            System.out.print("\nБлок: " + data_to_write);
                            encrypted_string.append(data_to_write);

                        } else {

                            String garbage_str = random_str();
                            encrypted_string.append(garbage_str);

                        }

                    }

                }

                System.out.println("\nЗашифрованные данные: " + encrypted_string.toString() +
                        "\nДанные и алгоритм были сохранены в data.txt и alg.txt соответственно.");

                try (FileWriter writer = new FileWriter("E:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\data.txt", false)) {
                    writer.write(encrypted_string.toString());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                System.out.println("В файле недостаточно данных!");
            }
        } else {
            System.out.println("Указанного вами файла нет!");
        }
    }

    public void EncryptEnterData() {
        String data = "";
        while (data.isEmpty() || data.length() <= block) {
            System.out.print("\nВведите данные для шифрования:\n");
            data = scan.nextLine();
        }
        System.out.println("\nРазмер блока = " +
                block +
                "\nДлина данных = " +
                data.length() +
                "\nКоличество блоков без остатка = " +
                (data.length() / block));
        int data_block = data.length() / block; // Рассчет количества блоков для данных
        if (data.length() % block != 0) data_block++; // Добавление блока на остаток данных
        System.out.println("\nКоличество итоговых блоков = " + data_block);
        int[][] crypto_map = new int[data_block][data_block]; // Создание массива решетки ришелье
        String[] divided_data = data.split("(?<=\\G.{" + block + "})"); // Данные разбитые на блоки
        for (int i = 0; i < crypto_map.length; i++) { // Случайное создание решетки ришелье
            int hole_in_map = rand.nextInt(data_block);
            for (int j = 0; j < crypto_map.length; j++) {
                if (j == hole_in_map) crypto_map[i][j] = 1;
                else {
                    crypto_map[i][j] = 0;
                }
            }
        }
        System.out.println();
        for (int[] ints : crypto_map) { // Вывод решетки ришелье
            for (int j = 0; j < crypto_map.length; j++) {
                System.out.print(" " + ints[j]);
            }
            System.out.println();
        }
        try (FileWriter writer = new FileWriter("E:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\alg.txt", false)) {
            writer.write(String.valueOf(block));
            writer.write("\n");
            for (int i = 0; i < crypto_map.length; i++) { // Запись решетки ришелье в файл
                for (int j = 0; j < crypto_map.length; j++) {
                    writer.write(String.valueOf(crypto_map[i][j]));
                }
                writer.write('\n');
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        StringBuilder encrypted_string = new StringBuilder(); // Шифрование данных методом Ришелье
        for (int i = 0; i < crypto_map.length; i++) {
            for (int j = 0; j < crypto_map[i].length; j++) {
                if (crypto_map[i][j] == 1) {
                    String data_to_write = divided_data[i];
                    if (divided_data[i].length() < block) {
                        for (int k = 0; k < (block - divided_data[i].length()); k++) { // Цикл добавляющий недостающие символы
                            data_to_write = data_to_write + " ";
                        }
                    }
                    System.out.print("\nБлок: " + data_to_write);
                    encrypted_string.append(data_to_write);
                } else {
                    String garbage_str = random_str();
                    encrypted_string.append(garbage_str);
                }
            }
        }
        try (FileWriter writer = new FileWriter("E:\\Documents\\4.1\\Безопасность\\labs\\Lab2\\data.txt", false)) {
            writer.write(encrypted_string.toString());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("\nЗашифрованные данные: " + encrypted_string.toString() +
                "\nДанные и алгоритм были сохранены в data.txt и alg.txt соответственно.");
    }

    public void ChangeBlockSize() {
        Scanner scan = new Scanner(System.in);
        int block_new = 0;
        while (block_new <= 1) {
            System.out.println("Укажите размер блока: ");
            block_new = scan.nextInt();
        }
        block = block_new;
    }

    public String random_str() {
        Random rand = new Random();
        return rand.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(block)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

}

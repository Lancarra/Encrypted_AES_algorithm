package org.example;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class TestTaker {
    static final Scanner scanner = new Scanner(System.in);
    static SecretKeySpec secretKey;
    static Cipher cipher;
    static String initializationVector;

    static String path = "file.txt";


    public static void main(String[] args) throws Exception {

        initializationVector = "abcdefghijklmnop";

        System.out.println("Enter text.");
        String plainText = scanner.nextLine();

        System.out.println("Enter key.");
        String key = scanner.nextLine();

        System.out.println("Enter method. (ECB, CBC, CFB)");
        String method = scanner.nextLine();

         byte[] encrypted = encrypt(plainText, key, method);
        write(encrypted);


        //  Base64.getEncoder().encodeToString() - это метод который принимает массив байтов и возвращает его в виде строки.
        String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);
        System.out.println("Encrypted text: " + encryptedBase64);


        byte[] b = read();

        String decrypted = decrypt(b, key, method);
        System.out.println("Decrypted text: " + decrypted);
    }

    public static byte[] encrypt(String plainText, String key, String method) throws Exception {
        secretKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher = Cipher.getInstance("AES/" + method + "/PKCS5Padding");
        if (method.equals("CBC") || method.equals("CFB")) {
            System.out.println(method);
            IvParameterSpec iv = new IvParameterSpec(initializationVector.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        }
        return cipher.doFinal(plainText.getBytes());
    }

    public static String decrypt(byte[] cipherText, String key, String method) throws Exception {
        secretKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher = Cipher.getInstance("AES/" + method + "/PKCS5Padding");
        if (method.equals("CBC") || method.equals("CFB")) {
            IvParameterSpec iv = new IvParameterSpec(initializationVector.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        }
        byte[] decryptedBytes = cipher.doFinal(cipherText);
        return new String(decryptedBytes);
    }

    public static void write(byte[] encrypted) {
        try {
            FileOutputStream writer = new FileOutputStream(path);
            writer.write(encrypted);
            System.out.println("Encrypted data written to file: " + path);
        } catch (IOException e) {
            System.out.println("File record error: " + e.getMessage());
        }
    }

    public static byte[] read() {
        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] byteArray = new byte[fis.available()];
            fis.read(byteArray);
            System.out.println("Bytes read from a file:");
            System.out.println(Arrays.toString(byteArray));
            return byteArray;
        } catch (IOException e) {
            System.out.println("Error when reading from a file: " + e.getMessage());
        }
        return new byte[0];
    }
}


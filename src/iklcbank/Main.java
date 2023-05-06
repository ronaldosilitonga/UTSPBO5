package iklcbank;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


class BankAccount {

    private String name;
    private String stambuk;
    private String accountNumber;
    private double balance;
    private String registrationDate;

    public BankAccount(String name,String stambuk, String accountNumber, double balance, String registrationDate) {
        this.name = name;
        this.stambuk = stambuk;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.registrationDate = registrationDate;
    }

    public String getName() {
        return name;
    }

    public String getStambuk() {
        return stambuk;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean send(BankAccount recipient, double amount) {
        if (balance >= amount) {
            balance -= amount;
            recipient.deposit(amount);
            return true;
        } else {
            return false;
        }
    }

}


public class Main {
    private static ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();

    static void garis1() {
        System.out.println("|==========================|");
    }
    
    static void garis2() {
        System.out.println("--------------------------");
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");  
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            clearScreen();
            garis1();
            System.out.println("|        IKLC  BANK        |");
            garis1();
            System.out.println("\n[1]. Register account");
            System.out.println("[2]. Send money");
            System.out.println("[3]. Deposit money");
            System.out.println("[4]. Show Accounts");
            System.out.println("[5]. Exit");
            System.out.print("\nSilahkan Pilih Opsi 1-5: ");
            int menuOption = input.nextInt();
            input.nextLine(); 

            switch (menuOption) {
                case 1:
                    registerAccount();
                    break;
                case 2:
                    sendMoney();
                    break;
                case 3:
                    depositMoney();
                    break;
                case 4:
                    showAccounts();
                    break;
                case 5:
                    System.out.println("\nThank you for using IKLC Bank");
                    return;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void registerAccount() {
        Scanner input = new Scanner(System.in);

        clearScreen(); 
        System.out.print("Nama      : ");
        String name = input.next();
        System.out.print("Stambuk   : ");
        String stambuk = input.next();

        String accountNumber;
        boolean unique;
        do {
            accountNumber = generateAccountNumber();
            unique = true;
            for (BankAccount account : accounts) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    unique = false;
                    break;
                }
            }
        } while (!unique);

        System.out.print("Saldo     : ");
        double balance = input.nextDouble();
        input.nextLine();

        LocalDateTime registrationDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = registrationDate.format(formatter);


        BankAccount account = new BankAccount(name, stambuk, accountNumber, balance, formattedDate);
        accounts.add(account);

        garis2();
        System.out.println("Registrasi Akun Berhasil");
        System.out.println("Nomor akun: " + accountNumber);
        System.out.println("Tanggal Registrasi: " + formattedDate);
        garis2();
        pressEnterToContinue();
    }

    private static void sendMoney() {
        Scanner input = new Scanner(System.in);

        clearScreen();
        System.out.print("Enter nomor akun anda: ");
        String senderAccountNumber = input.nextLine();

        BankAccount senderAccount = findAccount(senderAccountNumber);

        if (senderAccount == null) {
            System.out.println("\nAkun tidak ditemukan");
            pressEnterToContinue();
            return;
        }

        System.out.print("\nEnter nomor akun penerima: ");
        String recipientAccountNumber = input.nextLine();

        BankAccount recipientAccount = findAccount(recipientAccountNumber);

        if (recipientAccount == null) {

            System.out.println("\nAkun penerima tidak ditemukan !");
            pressEnterToContinue();
            return;
        }

        garis2();
        System.out.print("Input Jumlah: ");
        double amount = input.nextDouble();
        input.nextLine();

        if (amount <= 0) {
            System.out.println("\nJumlah Uang Tidak Sesuai");
            pressEnterToContinue();
            return;
        }
    
        if (senderAccount.getBalance() < amount) {
            System.out.println("\nSaldo rekening tidak mencukupi");
            pressEnterToContinue();
            return;
        }
    
        if (senderAccount.send(recipientAccount, amount)) {
            garis2();
            System.out.println("Uang Berhasil Terkirim");
            System.out.println("\nNama Pengirim   : " + senderAccount.getName());
            System.out.println("\nSaldo Pengirim  : " + formatBalance(senderAccount.getBalance()));
            System.out.println("Nama Penerima   : " + recipientAccount.getName());
            System.out.println("Saldo Penerima  : " + formatBalance(recipientAccount.getBalance()));
            garis2();
            pressEnterToContinue();
        } else {
            System.out.println("\nTransaksi Gagal");
            pressEnterToContinue();
        }
    }

    private static void depositMoney() {
        Scanner input = new Scanner(System.in);
        
        clearScreen(); 
        System.out.print("Input Nomor Akun: ");
        String accountNumber = input.nextLine();
    
        BankAccount account = findAccount(accountNumber);
    
        if (account == null) {
            System.out.println("\nAkun Tidak Ditemukan");
            pressEnterToContinue();
            return;
        }

        System.out.println("Nama:" + account.getName());
    
        System.out.print("\nJumlah Deposit: ");
        double amount = input.nextDouble();
        input.nextLine(); 
    
        if (amount <= 0) {
            System.out.println("\nJumlah Invalid");
            pressEnterToContinue();
            return;
        }
    
        account.deposit(amount);
        
        garis2();
        System.out.println("Deposit Saldo Berhasil");
        System.out.println("\nNama            : " + account.getName());
        System.out.println("Saldo Sekarang  : " + formatBalance(account.getBalance()));
        garis2();
        pressEnterToContinue();
    }

    private static void showAccounts() {
        clearScreen(); 
        garis2();
        System.out.println("List Akun:");
        garis2();

        if (accounts.size() == 0) {
            System.out.println("\nTidak Ada Akun Ditemukan.");
            pressEnterToContinue();
        } else {
            clearScreen(); 
            for (BankAccount account : accounts) {
                System.out.println("Nama                : " + account.getName());
                System.out.println("Stambuk             : "+ account.getStambuk());
                System.out.println("Nomor Akun          : " + account.getAccountNumber());
                System.out.println("Saldo               : " + formatBalance(account.getBalance()));
                System.out.println("Tanggal Registrasi  : " + account.getRegistrationDate());
                garis2();
            }
        }
        pressEnterToContinue();
    }

    private static String generateAccountNumber() {
        int number = (int) (Math.random() * 900000) + 100000;
        return Integer.toString(number);
    }
    
    private static BankAccount findAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public static String formatBalance(double balance) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(balance);
    }

    public static void pressEnterToContinue() {
        System.out.print("\n\nPress enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}

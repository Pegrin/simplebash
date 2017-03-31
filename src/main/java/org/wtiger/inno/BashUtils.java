package org.wtiger.inno;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Khayrutdinov Marat
 *         Email: mail@wtiger.org
 *         Created on 30.03.2017.
 */
public class BashUtils {
    static private String currentDir;

    public static void main(String[] args) {
        currentDir = System.getProperty("user.dir");
        System.out.println("Current directory: " + currentDir);
        String[] command;
        if (args.length > 0) {
            command = args;
            execute(command);
        } else {
            startCicle();
        }
    }

    private static void execute(String[] parsedCommandString) {
        System.out.println("Комманда: " + parsedCommandString[0]);
        for (int i = 1; i < parsedCommandString.length; i++) {
            System.out.println("Параметр" + i + ": " + parsedCommandString[i]);
        }
        Commands command = Commands.parseCommand(parsedCommandString);
        if (command == null) {
            return;
        }
        switch (command) {
            case ls: {
                ls(parsedCommandString);
            }
            break;
            case cd: {
                cd(parsedCommandString);
            }
            break;
            case cat: {
                cat(parsedCommandString, false, false);
            }
            break;
            case zip: {
                zip(parsedCommandString);
            }
            break;
            case sort: {
                sort(parsedCommandString);
            }
            break;
            case uniq: {
                uniq(parsedCommandString);
            }
            break;
            default: {
                defaultCommand();
            }
        }
    }

    private static void sort(String[] parsedCommandString) {
        cat(parsedCommandString, true, false);
    }

    private static void uniq(String[] parsedCommandString) {
        cat(parsedCommandString, false, true);
    }

    private static void zip(String[] parsedCommandString) {
        try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(parsedCommandString[1])))
        {
            for (int i = 2; i < parsedCommandString.length; i++) {
                try(FileInputStream fis = new FileInputStream(parsedCommandString[i])){
                    ZipEntry entry = new ZipEntry(parsedCommandString[i]);
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                    System.out.println("Архив успешно создан");
                }
            }
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
    }

    private static void cat(String[] parsedCommandString, boolean sort, boolean unique) {
        File file = new File(parsedCommandString[1]);
        ArrayList<String> fileOut = new ArrayList<>();
        HashSet<String> fileOutSet;

        if (file.exists() && file.isFile()) {
            BufferedReader in;
            try {
                in = new BufferedReader(new FileReader(file));
                String s;
                while ((s = in.readLine()) != null) {
                    fileOut.add(s);
                }
                if (sort) {
                    fileOut.sort(String::compareTo);
                }
                if (unique) {
                    fileOutSet = new HashSet<>(fileOut);
                    fileOutSet.forEach(System.out::println);
                }else {
                    fileOut.forEach(System.out::println);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Не удалось открыть файл:\n" +
                        parsedCommandString[1]);
            } catch (IOException e) {
                System.out.println("Ошибка чтения файла:\n" +
                        parsedCommandString[1]);
            }
        } else {
            System.out.println("Указан неверный файл:\n" +
                    parsedCommandString[1]);
        }
    }

    private static void cd(String[] parsedCommandString) {
        File dir = new File(parsedCommandString[1]);
        if (dir.isDirectory()) {
            currentDir = dir.getAbsolutePath();
            System.out.println("Текущая директория: " + currentDir);
        } else {
            System.out.println("Указан неверный путь к каталогу:\n" +
                    parsedCommandString[1]);
        }
    }

    private static void ls(String[] parsedCommandString) {
        String dirPath = (parsedCommandString.length < 2) ? currentDir : parsedCommandString[1];
        File dir = new File(dirPath);
        if (dir.isDirectory()) {
            ArrayList<File> files = new ArrayList<>();
            ArrayList<File> dirs = new ArrayList<>();
            for (File file :
                    dir.listFiles()) {
                if (file.isDirectory()) {
                    dirs.add(file);
                } else {
                    files.add(file);
                }
            }
            for (File dirsToOut : dirs) {
                System.out.println("d " + dirsToOut.getAbsolutePath());
            }
            for (File filesToOut : files) {
                System.out.println("f " + filesToOut.getAbsolutePath());
            }
        } else {
            System.out.println("Указан неверный путь к каталогу:\n" +
                    parsedCommandString[1]);
        }
    }

    private static void defaultCommand() {
        System.out.println("Такой функционал еще не реализован.");
    }

    private static void startCicle() {
        Scanner sc = new Scanner(System.in);
        Commands.showAllCommands();
        String commandLine = "";
        while (true) {
            commandLine = sc.nextLine();
            if (commandLine.equals("exit")) {
                break;
            }
            String[] parsedCommandString = parse(commandLine);
            execute(parsedCommandString);
        }
        System.out.println("Cya!");
    }

    private static String[] parse(String commandLine) {
        String[] result = commandLine.replaceFirst(" ", "<split>")
                .replaceAll("\" ", "<split>")
                .replaceAll("\"", "")
                .split("<split>");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }


}

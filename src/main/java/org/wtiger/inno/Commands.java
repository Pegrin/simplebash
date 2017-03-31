package org.wtiger.inno;

/**
 * @author Khayrutdinov Marat
 *         Email: mail@wtiger.org
 *         Created on 30.03.2017.
 */
enum Commands {
    ls{
        {
            description = "ls - вывод содержимого текущего каталога. " +
                    "Каждый элемент с новой строки, в одной строке выводится " +
                    "<тип> <имя>  (, где f - для файлов, d - для директорий);";
            minArgsCount = 0;
            maxArgsCount = 1;
        }
    }, cd{
        {
            description = "cd <имя_директории> - переход в директорию;";
            minArgsCount = 1;
            maxArgsCount = 1;
        }
    }, cat{
        {
            description = "cat <имя_файла> - вывод содержимого файла на экран;";
            minArgsCount = 1;
            maxArgsCount = 1;
        }
    }, zip{
        {
            description = "zip -r <имяархива> <имяфайла1> <имяфайла2> ... - создает " +
                    "архив, содержащий указанные файлы с наименованием <имяархива>;";
            minArgsCount = 1;
            maxArgsCount = 20;
        }
    }, sort{
        {
            description = "sort <имя_файла> -  выводит на экран содержимое файла, " +
                    "отсортированное построчно в лексикографическом порядке по возрастанию;";
            minArgsCount = 1;
            maxArgsCount = 1;
        }
    }, uniq{
        {
            description = "uniq <имя_файла> - выводит список уникальных строк в файле, " +
                    "порядок строк должен оставаться без изменения;";
            minArgsCount = 1;
            maxArgsCount = 1;
        }
    }, exit{
        {
            description = "exit - завершает работу приложения.";
            minArgsCount = 0;
            maxArgsCount = 0;
        }
    };

    protected String description;
    protected int minArgsCount, maxArgsCount;

    String getDescription() {
        return description;
    }

    int getMinArgsCount() {
        return minArgsCount;
    }

    int getMaxArgsCount() {
        return maxArgsCount;
    }

    static Commands parseCommand(String[] commandStrings){
        Commands command;
        try {
            command = Commands.valueOf(commandStrings[0]);
        } catch (IllegalArgumentException e) {
            System.out.println("Неверная команда: " + commandStrings[0] + "\n");
            showAllCommands();
            return null;
        }
        int argsCount = commandStrings.length - 1;
        if (argsCount < command.getMinArgsCount() || argsCount > command.getMaxArgsCount()){
            System.out.println("Неверное количество аргументов: ");
            System.out.println(command.getDescription());
        }
        return command;
    }

    static void showAllCommands() {
        System.out.println("Путь к файлам неободимо всегда указывать в двойных кавычках (\")" +
                "Увы, особенность текущего релиза.");
        for (Commands command :
                Commands.values()) {
            System.out.println(command.getDescription());
        }
    }
}

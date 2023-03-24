import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    /**
     * Читаем текст из заданного файла и разбиваем его на список слов.
     * Символами разделителями слов являются: любые пробельные символ, а также
     * символы '.', ',', ':', ';'.
     * @param file Полный путь к входному файлу
     * @return Список слов
     */
    private static List<String> reader(File file) {
        List<String> words = new LinkedList<>();
        try(BufferedReader br = new BufferedReader (new FileReader(file))) {
            final int BUF_SIZE = 1024;
            char[] buf = new char[BUF_SIZE];
            int c;
            while((c = br.read(buf))>0) {
                for (int cur = 0, beg = 0; cur < c;) {
                    if (!Character.isWhitespace(buf[cur])
                            && '.' != buf[cur]
                            && ',' != buf[cur]
                            && ':' != buf[cur]
                            && ';' != buf[cur]) {
                        ++cur;
                        continue;
                    }

                    // Встретили символ-разделитель СЛОВ

                    if (beg == cur) { // Значащие символы отсутствуют
                        ++beg;
                        ++cur;
                        continue;
                    }

                    String word = new String(buf, beg, cur - beg);
                    words.add(word);
                    ++cur;
                    beg = cur;

                }
            }
        }
        catch(IOException ex){
            System.err.println(ex.getMessage());
        }

        return words;
    }

    /**
     * Сортируем список слов и подсчитываем количество вхождений каждого слова
     * @param words Список слов
     * @return Словарь лексикографически отсортированных пар {"слово",
     * "количество данных слов в файле"}
     */
    private static TreeMap<String, Integer> sortAndCountWords(List<String> words) {
        TreeMap<String, Integer> sortedWords = new TreeMap<>();
        for (String word : words) {
            Integer val = sortedWords.putIfAbsent(word, 1);
            if (null != val) {
                sortedWords.put(word,val+1);
            }
        }

        return sortedWords;
    }

    /**
     * Печать слов с максимальным количеством вхождений
     * @param sortedWords Словарь лексикографически отсортированных пар
     * {"слово", "количество данных слов в тексте"}
     */
    private static void printWordsWithMaxCountOccurs(TreeMap<String,Integer> sortedWords) {
        int maxCountOccurs = -1;
        for(Map.Entry<String, Integer> entry: sortedWords.entrySet()) {
            maxCountOccurs = Math.max(entry.getValue(), maxCountOccurs);
        }
        for(Map.Entry<String, Integer> entry: sortedWords.entrySet()) {
            if (maxCountOccurs != entry.getValue()) {
                continue;
            }

            System.out.println("{\"" + entry.getKey() + "\", " + maxCountOccurs + "}");
        }
    }

    public static void main(String[] args) {
        String defFullFilePath = "data\\input.txt";
        File file = null;
        if (null != args && 1 == args.length) {
            file = new File(args[0]);
            if (!file.exists()) {
                System.err.println("Файл : \"" + args[0] + "\" НЕ существует.");
                file = null;
            }
            else {
                System.out.println("Используется файл: \"" + args[0] + "\".");
            }
        }
        if (null == file) {
            file = new File(defFullFilePath);
            if (!file.exists()) {
                System.err.println("Файл : \"" + defFullFilePath + "\" НЕ существует.");
                return;
            }

            System.out.println("Используется файл: \"" + defFullFilePath + "\".");
            System.out.println("Путь к входному файлу можно задать через параметры программы - первым параметром.");
        }
        System.out.println();

        // Читаем файл и разбиваем текст на слова
        List<String> words = reader(file);

        System.out.println("Статистика встречающихся в файле слов, отсортированных в алфавитном порядке:");
        TreeMap<String, Integer> sortedWords = sortAndCountWords(words);
        for(Map.Entry<String, Integer> entry: sortedWords.entrySet()) {
            System.out.println("{" + entry.getKey() + ",\t" + entry.getValue() + "}");
        }
        System.out.println();

        System.out.println("Слова, встречающиеся в тексте максимальное количество раз:");
        printWordsWithMaxCountOccurs(sortedWords);
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uniquewordintext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

/**
 *
 * @author YNZ
 */
public class UniqueWordinText {

    final private static String pattern = "[;:!?.,\\s]+";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();

        Map<String, Long> top10 = wordOccrence(new File("tempest.txt"));

        long end = System.currentTimeMillis();

        System.out.println("Top 10 word: " + top10.toString());
        System.out.println("Time cost  :" + (end - start));

    }

    public static Map<String, Long> wordOccrence(File textFile) throws IOException {
        String str = readText2String(textFile);

        List<String> wordsList = Stream.of(str.toLowerCase().split(pattern))
                .collect(toList());

        //all unique words
        List<String> wordsSet = wordsList.parallelStream().distinct()
                .collect(Collectors.toList());

        //counting each word
        Map<String, Long> wordNumber = new HashMap<>();
        for (String word : wordsSet) {
            wordNumber.put(word, wordsList.stream().parallel().filter(w -> w.equals(word)).count());
        }

        //all word occurences
        Map<String, Long> result = wordNumber.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        //top 10 
        Map<String, Long> top10 = result.entrySet().stream().limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return top10;

    }

    private static String readText2String(File file) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);) {

            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(" ");
            }
        }

        return sb.toString();
    }

}

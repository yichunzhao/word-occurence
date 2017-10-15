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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author YNZ
 */
public class UniqueWordinText {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        Map<String, Long> top10 = wordOccrence(new File("tempest.txt"));

        System.out.println("Top10 ; " + top10.toString());

    }

    public static Map<String, Long> wordOccrence(File textFile) throws IOException {
        String str = readText2String(textFile);
        //cut the text into small words
        String[] words = text2words(str.toLowerCase());

        //all words
        List<String> wordsList = Arrays.asList(words);

        //all unique words
        List<String> wordsSet = wordsList.parallelStream().distinct()
                .collect(Collectors.toList());

        //counting each word
        Map<String, Long> wordNumber = new HashMap<>();
        for (String word : wordsSet) {
            wordNumber.put(word, countWordParallel(word, wordsList));
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

    private static String[] text2words(String text) {
        String pattern = "[;:!?.,\\s]+";
        return text.split(pattern);
    }

    private static long countWordParallel(String word, List<String> words) {
        return words.parallelStream().filter(w -> w.equals(word)).count();
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

package com.jcourse.vlsnk;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        WordCounter wordCounter = new WordCounter();
        for (String s : args){
            wordCounter.addFile(s);
            File csv = wordCounter.getWords();
            System.out.println(csv);
        }

    }

    void streamAPI(String[] args){
        /*
        Пример с семинара
         */
        try(Stream<String> lines = Files.lines(Paths.get(args[0]));
            PrintWriter pw = new PrintWriter(new File("test.csv"))) {
            long wordCount = 0;
            long[] wordCount1 = {0};
            AtomicLong atomicLong = new AtomicLong(0);
            lines
                    .map(s -> s.split("[^\\pL\\pN]+")) //получим Array<String> для каждой строчки pL - любая буква на любом языке, pN - число, + - пустые символы будет пропускать
                    //stream для каждой строчки сделает один sream из всех строк? :: - method reference
                    .flatMap(Arrays::stream)
                    .filter(s -> !s.isEmpty())
//                      .peek(s -> wordCount++)
//                      .peek(s -> wordCount1[0]++)
                    .peek(s -> atomicLong.incrementAndGet())
//                    .flatMap(array -> Arrays.stream(array))
//                    .flatMap(s -> Arrays.stream(s.split("[^\\pL\\pN]+")))
//                      .collect(Collectors.toCollection(ArrayList::new))
//                      1- что является ключом, 2- что является значением, 3- что делать, если встретили 2 одинаковых ключа
                    .collect(Collectors.toMap(s -> s, s ->1L, (l1, l2) ->l1 + l2)) //Map<String, Long>
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparingLong((Map.Entry<String, Long> entry) -> entry.getValue())
                            .thenComparing((Map.Entry<String, Long> entry) -> entry.getKey())
                            .reversed())
                    .forEach((Map.Entry<String, Long> entry) -> {
                        pw.println(entry.getKey() + ", " + entry.getValue() +", " + (double) entry.getValue() / atomicLong.get());
                    });

        } catch (FileNotFoundException file) {
            file.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

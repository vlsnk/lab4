package com.jcourse.vlsnk;

import java.io.*;
import java.nio.CharBuffer;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class WordCounter {

    private TreeMap<String, Word> wordList = new TreeMap<>();
    private final static String separator = " | ";
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    Integer words = 0;

    public void addWord(StringBuilder sb) {
        if (sb.length() == 0 ) return;
        String s = sb.toString();

        words++;
        //ищет в map по ключу s, если находит, то к старому value добавляет 1(2й параметр), иначе
        //добавляет в map (s,1)
        wordList.merge(s, new Word(1), (oldVal, newVal) -> oldVal.add(newVal));

    }

    public File getWords() {
        File file = new File("test" + LocalDate.now() + random.nextInt(100) + ".csv");

        try (PrintWriter pw = new PrintWriter(file)){
            sortWordList();
            for (Map.Entry e : wordList.entrySet()) {
                Word val = (Word) e.getValue();
                sb.setLength(0);
                sb.append(e.getKey());
                sb.append(separator);
                sb.append(val);
                sb.append(separator);
                double l = val.getCount()/words.doubleValue()*100;
                sb.append(String.format("%.2f", l));
                sb.append("\n");
                pw.write(sb.toString());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void addFile(String fileName) {
        words = 0;

        try (Reader reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(fileName)))){
            CharBuffer charBuffer = CharBuffer.allocate(1024);
            while (true) {
                int i = reader.read(charBuffer);

                for (Character c : charBuffer.array()) {
                    if (Character.isLetterOrDigit(c)) {
                        sb.append(c);
                    } else {
                        addWord(sb);
                        sb.setLength(0);
                    }
                }
                if (reader.read(charBuffer) > 0) continue;
                else break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Map<String, Word> sortWordList() {
        //stream надо указывать с <Тип1, Тип2>, чтобы он понимал с какими объектами работает
        Stream<Map.Entry<String, Word>> stream = wordList.entrySet().stream();
        return
                stream.sorted(Map.Entry.<String, Word>comparingByValue().reversed())
                .collect(Collectors.toMap((Map.Entry<String, Word> x) -> x.getKey(), (Map.Entry<String, Word> x) -> x.getValue()));
}


}
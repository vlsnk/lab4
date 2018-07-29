package com.jcourse.vlsnk;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class Word implements Comparable, Serializable {

    int count;

    public Word(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Word add(Word i) {
        this.count = this.count + i.getCount();
        return this;
    }
    @Override
    public int compareTo(Object o) {
        Word newWord = (Word) o;
        int i = newWord.getCount();
        if (i > this.count) return -1;
        if (i == this.count) return 0;
        return 1;
    }

    @Override
    public String toString() {
        return "" + count;
    }


}

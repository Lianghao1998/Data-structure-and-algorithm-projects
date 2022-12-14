package deque.ArrayDeque;

import java.util.Arrays;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque{
    private int size;
    private T[] array;
    Comparator<T> c;

    public MaxArrayDeque(Comparator<T> c){
        this.c = c;
    }

    public T max(){
        Arrays.sort(array, c);
        return array[size-1];
    }

    public T max(Comparator<T> c){
        Arrays.sort(array, c);
        return array[size-1];
    }
}

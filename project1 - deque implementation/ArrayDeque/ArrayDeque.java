package deque.ArrayDeque;

import deque.Deque;

public class ArrayDeque<T> implements Deque {
//    -Circular array: Objects can be added in any direction
//    -Automatic resizing: Any amount of objects can be added/ removed
//    -Insertion Sequence retention
    private int size;
    private T[] array;
    private int pointer;
    @Override
    public void addFirst(Object item) {
        if(size == array.length){
            array = resize(array,2,pointer);
            pointer = array.length -1;
        }
        array[mod(pointer)] = (T) item;
        pointer--;
        size++;
    }

    @Override
    public void addLast(Object item) {
        if(size == array.length){
            array = resize(array,2,pointer);
            pointer = array.length -1;
        }
        int last = mod(pointer + size +1 );
        array[last] = (T) item;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for(int i = 0; i < array.length; i++){
            System.out.println(array[i]);
        }
    }

    @Override
    public Object removeFirst() {
        if(size == 0){
            return null;
        }
        array[mod(pointer+1)] = null;
        pointer ++;
        size --;
        if(size< 0.5*array.length){
            array = resize(array,0.5,pointer);
        }
        return null;
    }

    @Override
    public Object removeLast() {
        if(size == 0){
            return null;
        }
        array[mod(pointer +size )] = null;
        size --;
        if(size< 0.5*array.length){
            array = resize(array,0.5,pointer);
        }
        return null;
    }

    @Override
    public Object get(int index) {
        return array[pointer+index+1];
    }

        public ArrayDeque(){
        size = 0;
        array = (T[]) new Object[8];
    }

        private T[] resize(T[] input, double ratio, int pointer){
        int len = input.length;
        Object[] output = new Object[(int) (len*ratio)];
        int smaller = (int) (Math.min(ratio,1) * len); // help array expand and shrink cases to share this method
        for(int i = 0; i < smaller; i++){
            output[i] = input[mod(pointer+1 +i) ];
        }
        return (T[]) output;
    }

        //    for circular array index transformation
    private int mod(int index){
        int result = index % array.length;
        if(result< 0){
            result += array.length;
        }
        return result;
    }

}

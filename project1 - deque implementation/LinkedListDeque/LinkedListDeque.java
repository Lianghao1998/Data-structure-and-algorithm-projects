package deque.LinkedListDeque;

import deque.Deque;

public class LinkedListDeque<T> implements Deque {
    private int size;
    private Node sentinel;

    @Override
    public void addFirst(Object item){
        Node first = new Node(item);
        Node prevFirst = sentinel.next;
        link(sentinel,first);
        link(first, prevFirst);
        size++;
    }


    @Override
    public void addLast(Object content){
        Node last = new Node(content);
        Node prevLast = sentinel.prev;
        link(last,sentinel);
        link(prevLast,last);
        size++;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public void printDeque(){
        Node pointer = sentinel.next;
        while (pointer != sentinel){
            System.out.println(pointer.item);
            pointer = pointer.next;
        }
    }

    @Override
    public T removeFirst(){
        if (size>0){
            Node second = sentinel.next.next;
            link(sentinel, second);
            size--;
        }
        return null;
    }

    @Override
    public T removeLast(){
        if (size>0){
            Node secondLast = sentinel.prev.prev;
            link(secondLast, sentinel);
            size--;
        }
        return null;
    }

    @Override
    public T get(int index){
        Node pointer = sentinel;
        for(int i = 0; i<= index; i++){
            pointer = pointer.next;
        }
        return (T) pointer.item;
    }

    public LinkedListDeque(){
        size = 0;
        sentinel = new Node(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel.prev;
    }


    public T getRecursive(int index){
        return getter(sentinel.next,index);
    }

    private T getter(Node pointer, int index){
        if(index == 0){
            return (T) pointer.item;
        }
        else {
            index -=1;
            return getter(pointer.next,index);
        }
    };

//helper method
    public void link(Node previous, Node following){
        previous.next = following;
        following.prev = previous;
    }



}

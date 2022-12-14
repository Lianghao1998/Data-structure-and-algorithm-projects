package deque.LinkedListDeque;

public class Node<contentType> {
    contentType item;
    Node next;
    Node prev;

    public Node(contentType item) {
        this.item = item;
    }
}

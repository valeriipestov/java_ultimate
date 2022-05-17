package javaultimate.linkedlist;

import lombok.Data;

@Data
public class Node<T> {
    T element;

    Node<T> next;

    public Node(T element) {
        this.element = element;
    }
}

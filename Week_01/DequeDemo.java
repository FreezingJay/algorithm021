package com.freezing.leetcode.jike;

import java.util.Deque;
import java.util.LinkedList;

public class DequeDemo {
    public static void main(String[] args){
        Deque<String> deque = new LinkedList<>();
        deque.addFirst("a");
        deque.addFirst("b");
        deque.addFirst("c");
        System.out.println(deque);

        String str = deque.peekFirst();
        System.out.println(str);
        System.out.println(deque);

        while(!deque.isEmpty()){
            System.out.println(deque.pollFirst());
        }
        System.out.println(deque);

        System.out.println("-----------------------------------");

        Deque<String> deque1 = new LinkedList<>();
        deque1.push("a");
        deque1.push("b");
        deque1.push("c");
        System.out.println(deque1);

        String str1 = deque1.peek();
        System.out.println(str1);
        System.out.println(deque1);

        while(deque1.size() > 0){
            System.out.println(deque1.pop());
        }
        System.out.println(deque1);
    }
}

public class Queue {
    private int rear, front;
    private Object [] queue;

    public Queue(int capacity)
    {
        queue = new Object[capacity];
        rear = -1;
        front = 0;
    }

    void enqueue(Object data)
    {
        if(isFull())
            System.out.println("queue overflow");
        else
        {
            rear = (rear + 1) % queue.length;
            queue[rear] = data;
        }
    }

    Object dequeue() {
        if (isEmpty()) {
            System.out.println("queue is empty");
            return null;
        } else {
            Object temp = queue[front];
            queue[front] = null; // set dequeued element to null
            front = (front + 1) % queue.length;
            return temp;
        }
    }


    int size()
    {
        if (queue[front] == null)
            return 0;
        else {
            if(rear >= front)
                return rear - front +1;
            else
                return queue.length - (front - rear) + 1;
        }
    }

    boolean isFull()
    { return (front == (rear + 1) % queue.length) && queue[front] != null && queue[rear] != null; }

    boolean isEmpty()
    { return queue[front] == null; }

    Object Peek()
    {
        if (isEmpty()) {
            System.out.println("queue is empty");
            return null;
        }
        else
            return queue[front];
    }
}
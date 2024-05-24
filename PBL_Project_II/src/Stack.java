public class Stack {
    private int top;
    private Object[] element;

    public Stack(int capacity) {
        this.element = new Object[capacity];
        this.top = -1;
    }

    public void push(Object data) {
        if (isFull()) {
            System.out.println("Stack overflow");
        } else {
            top++;
            element[top] = data;
        }

    }
    public Object pop(){
        if(isEmpty()){
            System.out.println("Stack is Empty");
            return null;
        }
        else{
            Object retData = element[top];
            top--;
            return retData;

        }
    }

    public Object peek(){
        if(isEmpty()){
            System.out.println("Stack is Empty");
            return null;
        }
        else
            return element[top];
    }
    public boolean isEmpty(){
        return (top == -1);
    }
    public boolean isFull(){
        return (top + 1 == element.length);
    }
    public int size(){
        return top+1;
    }
}
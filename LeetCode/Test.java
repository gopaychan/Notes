public class Test extends Thread {
    int i;
    Thread previousThread; // 上一个线程

    String author = "gopaychan";

    public Test(Thread previousThread, int i) {
        this.previousThread = previousThread;
        this.i = i;
    }

    @Override
    public void run() {
        try {
            // 调用上一个线程的join方法，大家可以自己演示的时候可以把这行代码注释掉
            previousThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("num:" + i);
    }

    public static void main(String[] args) {
        Thread previousThread = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Test joinDemo = new Test(previousThread, i);
            joinDemo.start();
            previousThread = joinDemo;
        }
    }
}

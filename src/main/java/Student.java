import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Student extends Thread {

    private String name;
    private Semaphore semQueueAccess;
    private Semaphore semMonitor;
    private Semaphore semStudent;
    private Queue<Student> queue;
    private static final int NUM_CHAIRS = 3;
    private Random random;

    private boolean finished = false;

    public Student(String name, Semaphore semQueueAccess, Semaphore semMonitor, Semaphore semStudent, Queue<Student> queue) {
        this.name = name;
        this.semQueueAccess = semQueueAccess;
        this.semMonitor = semMonitor;
        this.semStudent = semStudent;
        this.queue = queue;
        this.random = new Random();
    }

    public void run() {
        while (!finished) {
            try {
                //System.out.println(name + ": (Voy a ir a monitoría a que me ayuden con una duda)");
                Thread.sleep(random.nextInt(5000));

                semQueueAccess.acquire();

                if (queue.size() == NUM_CHAIRS) {
                    System.out.println(name + ": Ups, la sala está llena, mejor me voy a programar.");
                    semQueueAccess.release();

                    Thread.sleep(random.nextInt(50000));

                } else {

                    if(queue.size() == 0){
                        System.out.println(name + ": Ufff, que suerte no hay nadie en las sillas");
                    }else{
                        System.out.println(name + ": Me voy a sentar a esperar");
                    }

                    queue.add(this);
                    semQueueAccess.release();

                    semMonitor.release();  // Despierta al monitor

                    semStudent.acquire();  // Espera que el monitor lo atienda

                    System.out.println(name + ": Muchas gracias por la ayuda!");

                    Thread.sleep(random.nextInt(5000));  // Simula el tiempo programando después de la ayuda

                    finished = true;

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getNombre(){
        return this.name;
    }
}
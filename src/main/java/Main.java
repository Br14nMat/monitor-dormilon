import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Main {

    private static Queue<Student> queue;
    private static Semaphore semMonitor;
    private static Semaphore semStudent;
    private static Semaphore semQueueAccess;

    private static Monitor monitor;

    public static void main(String[] args) throws InterruptedException {
        // Semáforo para acceso a la cola
        semQueueAccess = new Semaphore(1);
        semMonitor = new Semaphore(0);  // El monitor espera a los estudiantes
        semStudent = new Semaphore(0);  // Los estudiantes esperan al monitor

        queue = new LinkedList<>();

        monitor = new Monitor(semQueueAccess, semMonitor, semStudent, queue);
        monitor.start();

        int NUM_STUDENTS = 7;
        for (int i = 0; i < NUM_STUDENTS; i++) {
            new Student("Estudiante " + (i + 1), semQueueAccess, semMonitor, semStudent, queue).start();
        }
    }
}

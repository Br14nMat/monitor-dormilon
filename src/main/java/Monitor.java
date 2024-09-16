import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Monitor extends Thread {

    private Semaphore semQueueAccess;
    private Semaphore semMonitor;
    private Semaphore semStudent;
    private Queue<Student> queue;

    public Monitor(Semaphore semQueueAccess, Semaphore semMonitor, Semaphore semStudent, Queue<Student> queue){
        this.semQueueAccess = semQueueAccess;
        this.semMonitor = semMonitor;
        this.semStudent = semStudent;
        this.queue = queue;
    }

    public void run() {
        while (true) {
            try {

                if(queue.isEmpty()){
                    System.out.println("Monitor: no hay nadie mejor me voy a dormir");
                }

                semMonitor.acquire();  // Espera a que un estudiante lo despierte

                semQueueAccess.acquire();


                if (!queue.isEmpty()) {

                    if(queue.size() == 1){
                        System.out.println(queue.peek().getNombre() + ": Hola, que pena despertarlo, pero necesito ayuda");
                    }else{
                        System.out.println("Monitor: voy a mirar si hay alguien esperando");
                    }

                    Student student = queue.poll();


                    System.out.println("Monitor: Hola " + student.getNombre() + ", en qué te puedo ayudar?");
                    semQueueAccess.release();  // Libera la cola para otros estudiantes

                    Thread.sleep(new Random().nextInt(5000));  // Simula el tiempo de ayuda


                    semStudent.release();  // Notifica al estudiante que terminó

                    Thread.sleep(new Random().nextInt(1000));

                    System.out.println("Monitor: Listo, " + student.getNombre() + " espero haberte ayudado!");

                } else {
                    semQueueAccess.release();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

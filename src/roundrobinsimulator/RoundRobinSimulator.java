package roundrobinsimulator;
//Manuela Lopez
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class RoundRobinSimulator {
    
    public static void main(String[] args) {
        
       int n;
       int quantum;        
       String name;
       int size;
       int intercambio;
       int llegada;
       int rafaga;
       int time = 0;
       String opt;
        
       Scanner s = new Scanner(System.in);
       Random id = new Random(); 
       // Objeto de listas
       Lista listos = new Lista();
       Lista listos_aux = new Lista();
       Lista RAM = new Lista();
       Lista CPU = new Lista();
       //Objeto de RoundRobin
       RoundRobin RR = new RoundRobin();
       
       try{
           //Se piden los datos
            System.out.println("SIMULADOR DE PROCESOS ROUND ROBIN");
            System.out.print("¿Cantidad de procesos a simular?: ");
            n = s.nextInt();
            System.out.print("Tamaño del quantum: ");
            quantum = s.nextInt();
            System.out.print("Tamaño del intercambio: ");
            intercambio = s.nextInt();
            s.nextLine();
            System.out.print("¿Con tiempos de llegada? s/n: ");
            opt = s.nextLine();
            //Se declara número de procesos
            int num_proc = n;
            
            quantum = quantum + intercambio;
            //Se ejecuta mientras n sea diferente de 0
            while(n!=0){
                System.out.print("\nNombre del proceso: ");
                name = s.nextLine();
                //Se trabaja con ram, por defecto se le asigna 16
                size = 16;
                System.out.print("NCPU en  Quantum: ");
                rafaga = s.nextInt();
                //se multiplica los ncpu por los quantum para saber cuantos segundos se gasta en el proceso
                rafaga = rafaga * quantum;
                s.nextLine();
                // Se valida si el proceso tiene tiempos de llegada
                if("n".equals(opt)){
                    llegada = 0;
                }
                else{
                    System.out.print("Tiempo de llegada (s): ");
                    llegada = s.nextInt();   
                    s.nextLine();
                }
                //se envian los valores a la clase lista
                listos_aux.agregarProcesoDatos(((id.nextInt(100000) % 1000)), name, size, rafaga, llegada, 0);
                n = n-1;
            }

            System.out.print("\n*ROUND ROBIN, ");
            System.out.println("Q = " + (quantum- intercambio) );

            System.out.print("Procesos ingresados por el usuario a simular: ");
            
            RR.verListaProcesos(listos_aux);
            //Se ejecuta segundo a segundo hasta que se terminen los procesos
            while(!RR.terminoSimu(listos_aux, listos, RAM, CPU)){
                System.out.println("\n" +time + "s.");
                //envia a la clase RoundRobin la lista de procesos listos y el tiempo
                RR.auxAListos(listos_aux, listos, time);
                //valida si la lista de listos es vacia
                if(!listos.esVacia()){
                     RR.listosARAM(listos, RAM);  
                }
                if(!RR.cpuVacio(RAM) || !RR.cpuVacio(CPU)){ 
                     if(RR.cpuVacio(CPU)){             
                         RR.ramACPU(RAM, CPU, time);
                         RR.ejecutandoEnCPU(CPU, listos, quantum,RAM, time);
                    }
                     else{
                        int j = RR.ejecutandoEnCPU(CPU, listos, quantum, RAM, time);
                        if(j==1 && !RAM.esVacia()){ 
                            RR.ramACPU(RAM, CPU, time); 
                            RR.ejecutandoEnCPU_noshow(CPU, listos, quantum,RAM, time); 
                        }
                    }
                }
                    //imprime los resultados de segundo a segundo
                    System.out.print("\n  LISTOS->");
                    RR.verListaProcesos(listos);
                    System.out.print("\n  RAM->");
                    RR.verListaProcesos(RAM);
                    System.out.println("\n  " + RAM.getRam() + "MB libres.");
                    time = time + 1;
                    

                }
                // Trae los resultados del total de la ejecución de la clase RounRobin
                System.out.println("\nTiempo promedio de espera = " + ((double)RR.getTiempo_final_espera()/num_proc) + "s."); 
                System.out.println("Tiempo promedio de ejecución = " + ((double)RR.getTiempo_final_ejecución()/num_proc) + "s.");
                System.out.println("Tiempo promedio de respuesta = " + ((double)RR.getTiempo_final_respuesta()/num_proc) + "s.");     
            }
        catch(java.util.InputMismatchException i){
            //Valida que no se haya ingresado caracteres especiales
            System.out.println("Entrada inválida.");
        }
    }       
}

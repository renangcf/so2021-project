import java.util.Scanner;

public class Application {

    public static void main(String args[]){

        Scanner sc = new Scanner(System.in);
        System.out.println("Bem vindo ao sistema");
        System.out.println();

        int frames = 0;
        loop:
        while(true){
            System.out.println("Por favor, insira a quantidade de blocos do sistema simulado(Digitar apenas 32,64 ou 128).");
            frames = sc.nextInt();
            switch(frames){
                case 32,64,128:
                    System.out.println("Criando manipulador de memória....");
                    break loop;
                default:
                    System.out.println("Número invalido. Números válidos: 32, 64, 128.");
                    System.out.println();
                    break;
            }
        }

        MemoryManager memoryManager = new MemoryManager(frames);


    }
}

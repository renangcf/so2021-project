import Exceptions.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Scanner;

public class Application {

    public static void main(String args[]) throws InvalidProcessException, MemoryOverflowException, FileFormatException, NoSuchFileException, StackOverflowException, NoSuchMemoryException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Bem vindo ao sistema");
        System.out.println();
        int frames = chooseBlockSize();
        MemoryManager mm = new MemoryManager(frames);

        loop:while(true){
            System.out.println();
            System.out.println("1 - Carregar Processo na memória");
            System.out.println("2 - Alocar memória ao processo");
            System.out.println("3 - Liberar memória do processo");
            System.out.println("4 - Excluir processo da memória");
            System.out.println("5 - Criar novo bloco de memória");
            System.out.println("6 - Lista de Processos e seus IDs");
            System.out.println("X - Sair do programa\n");

            String uInput = sc.nextLine();
            int id = 0;
            int size = 0;
            int heapSize = 0;

            switch(uInput) {
                case "1":
                    try {
                        System.out.println("Digite o nome do arquivo: ");
                        String processName = sc.nextLine();
                        int processId = mm.loadProcessToMemory(processName);
                        //System.out.println(mm.getBitMap());
                        //System.out.println(mm.getPageTable(processId));
                        System.out.println();
                        System.out.println("ID do novo processo: " + processId);
                        break;
                    } catch(Exception e){
                        System.out.println(e);
                        break;
                    }

                case "2":
                    try{
                        System.out.println("Digite o id do processo: ");
                        id = Integer.parseInt(sc.nextLine());
                        System.out.println("Digite o tamanho do heap: ");
                        size = Integer.parseInt(sc.nextLine());
                        while (size < 0){
                            System.out.println("Tamanho Inválido. \n Deve ser maior ou igual a 0. \n Digite novamente o tamanho do heap:");
                            size = Integer.parseInt(sc.nextLine());
                            break;
                        }

                        heapSize = mm.allocateMemoryToProcess(id, size);
                        System.out.println("Heap Size alocado: " + heapSize);
                        System.out.println(mm.getBitMap());
                        System.out.println(mm.getPageTable(id));
                        break;
                    } catch (Exception e){
                        System.out.println(e);
                        break;
                    }


                case "3":
                    try{
                        System.out.println("Digite o id do processo: ");
                        id = Integer.parseInt(sc.nextLine());
                        System.out.println("Digite o tamanho do heap: ");
                        size = Integer.parseInt(sc.nextLine());
                        while (size < 0){
                            System.out.println("Tamanho Inválido. \n Deve ser maior ou igual a 0. \n Digite novamente o tamanho do heap:");
                            size = Integer.parseInt(sc.nextLine());
                            break;
                        }
                        heapSize = mm.freeMemoryFromProcess(id, size);
                        System.out.println("Heap Size desalocado: " + heapSize);
                        System.out.println(mm.getBitMap());
                        System.out.println(mm.getPageTable(id));
                    } catch(Exception e){
                        System.out.println(e);
                        break;
                    }

                    break;

                case "4":
                    break;
                case "5":
                    System.out.println("Limpando Memória...");
                    mm.resetMemory();
                    System.out.println("Memória limpa!!");
                    break;

                case "6":
                    try {
                        System.out.println("Lista de processos:");
                        mm.printProcessList(mm.getProcessList());
                        break;
                    } catch(Exception e){
                        System.out.println(e);
                        break;
                    }
                default:
                    System.out.println("Saindo...");
                    break loop;
            }
        }
    }

    private static int chooseBlockSize(){
        Scanner sc = new Scanner(System.in);
        int frames = 0;
        while(true){
            System.out.println("Por favor, insira a quantidade de blocos do sistema simulado(Digitar apenas 32,64 ou 128).");
            frames = sc.nextInt();
            switch(frames) {
                case 32, 64, 128:
                    System.out.println("Criando manipulador de memória....");
                    return frames;
                default:
                    System.out.println("Número invalido. Números válidos: 32, 64, 128.");
                    System.out.println();
                    break;
            }
        }
    }

}

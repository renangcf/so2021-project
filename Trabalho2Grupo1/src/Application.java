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
            System.out.println("1 - Carregar um processo na memória");
            System.out.println("2 - Alocar Heap de um processo");
            System.out.println("3 - Liberar Heap de um processo");
            System.out.println("4 - Excluir um processo da memória");
            System.out.println("5 - Listar processos e seus IDs");
            System.out.println("6 - Mostrar bitmap da memória física");
            System.out.println("7 - Mostrar tabela de página de um processo");
            System.out.println("8 - Mostrar endereço físico de um processo apartir do lógico");
            System.out.println("9 - Limpar bloco de memória");
            System.out.println("X - Sair do programa\n");

            String uInput = sc.nextLine();
            int id = 0;
            int size = 0;
            int heapSize = 0;
            int logAddress = 0;

            switch(uInput) {
                case "1":
                    try {
                        System.out.print("Digite o nome do arquivo: ");
                        String processName = sc.nextLine();
                        int processId = mm.loadProcessToMemory(processName);
                        System.out.println();
                        System.out.println("ID do novo processo: " + processId);
                        break;
                    } catch(Exception e){
                        System.out.println(e);
                        break;
                    }

                case "2":
                    try{
                        System.out.print("Digite o id do processo: ");
                        id = Integer.parseInt(sc.nextLine());
                        System.out.print("Digite o tamanho a adicionar ao heap: ");
                        size = Integer.parseInt(sc.nextLine());
                        while (size < 0){
                            System.out.println("Tamanho Inválido. \n Deve ser maior ou igual a 0. \n Digite novamente o tamanho a adicionar ao heap:");
                            size = Integer.parseInt(sc.nextLine());
                            break;
                        }

                        System.out.println();
                        heapSize = mm.allocateMemoryToProcess(id, size);
                        System.out.println("Tamanho atual do heap: " + heapSize);
                        break;
                    } catch (Exception e){
                        System.out.println(e);
                        break;
                    }


                case "3":
                    try{
                        System.out.print("Digite o id do processo: ");
                        id = Integer.parseInt(sc.nextLine());
                        System.out.print("Digite o tamanho a remover do heap: ");
                        size = Integer.parseInt(sc.nextLine());
                        while (size < 0){
                            System.out.println("Tamanho Inválido. \n Deve ser maior ou igual a 0. \n Digite novamente o tamanho a remover do heap:");
                            size = Integer.parseInt(sc.nextLine());
                            break;
                        }

                        System.out.println();
                        heapSize = mm.freeMemoryFromProcess(id, size);
                        System.out.println("Tamanho do heap removido: " + heapSize);
                    } catch(Exception e){
                        System.out.println(e);
                        break;
                    }

                    break;

                case "4":
                    try{
                        System.out.print("Digite o id do processo: ");
                        id = Integer.parseInt(sc.nextLine());
                        mm.excludeProcessFromMemory(id);
                        System.out.println();
                        break;
                    } catch(Exception e){
                        System.out.println(e);
                    }

                case "5":
                    try {
                        System.out.println("Lista de processos carregados: ");
                        mm.printProcessList(mm.getProcessList());
                        break;
                    } catch(Exception e){
                        System.out.println(e);
                        break;
                    }

                case "6":
                    try {
                        System.out.println("Bit map dos processos: ");
                        System.out.println(mm.getBitMap());
                        break;
                    } catch(Exception e){
                        System.out.println(e);
                        break;
                    }
                case "7":
                    try{
                        System.out.print("Digite o id do processo: ");
                        id = Integer.parseInt(sc.nextLine());
                        System.out.println("Tabela de página do processo " + id + ": \n ID|End.Fis.|Bit Validação");
                        System.out.println(mm.getPageTable(id));
                        break;
                    } catch(Exception e){
                        System.out.println(e);
                        break;
                    }

                case "8":
                    try{
                        System.out.print("Digite o id do processo: ");
                        id = Integer.parseInt(sc.nextLine());
                        System.out.print("Digite um endereço lógico: ");
                        logAddress = Integer.parseInt(sc.nextLine());
                        System.out.println( "\nO endereço lógico \"" + logAddress + "\" aponta para o endereço físico \"" + mm.getPhysicalAddress(id, logAddress) + "\"");

                        break;
                    }catch(Exception e){
                        System.out.println(e);
                        break;
                    }

                case "9":
                    System.out.println("Limpando Memória...");
                    mm.resetMemory();
                    System.out.println("Memória limpa!!");
                    break;

                case "X", "x":
                    System.out.println("Saindo...");
                    break loop;

                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }

    private static int chooseBlockSize(){
        Scanner sc = new Scanner(System.in);
        int frames = 0;
        while(true){
            System.out.println("Por favor, insira a quantidade de blocos do sistema simulado (Digitar apenas 32,64 ou 128).");
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

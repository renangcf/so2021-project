import Exceptions.FileFormatException;
import Exceptions.InvalidProcessException;
import Exceptions.MemoryOverflowException;
import Exceptions.NoSuchFileException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Scanner;

public class Application {

    public static void main(String args[]) throws InvalidProcessException, MemoryOverflowException, FileFormatException, NoSuchFileException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Bem vindo ao sistema");
        System.out.println();

        int frames = 0;
        loop:
        while(true){
            System.out.println("Por favor, insira a quantidade de blocos do sistema simulado(Digitar apenas 32,64 ou 128).");
            frames = sc.nextInt();
            switch(frames) {
                case 32, 64, 128:
                    System.out.println("Criando manipulador de memória....");
                    break loop;
                default:
                    System.out.println("Número invalido. Números válidos: 32, 64, 128.");
                    System.out.println();
                    break;
            }

        }
        System.out.println("Digite o nome do arquivo: ");
        String processName = sc.nextLine();
        processName = sc.nextLine();
        MemoryManager mm = new MemoryManager(frames);
        mm.loadProcessToMemory(processName);

        /*try {
            mm.loadProcessToMemory(processName);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        System.out.println(mm.getBitMap());
        System.out.println(mm.getPageTable(0));
    }
}

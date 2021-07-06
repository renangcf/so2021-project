import Exceptions.*;
import java.util.ArrayList;
import java.util.List;

public class MemoryManager implements ManagementInterface {
    int frames;
    List<PageTable> listPageTables;
    int[] frameMapping;


    public MemoryManager(int frames){
        this.frames = frames;
        this.listPageTables = new ArrayList<>();

        if(frames == 32){
            this.frameMapping = new int[32];
            for(int i = 0; i<32; i++){
                this.frameMapping[i] = 0;
            }

        }else if(frames == 64){
            this.frameMapping = new int[64];
            for(int i = 0; i<64; i++){
                this.frameMapping[i] = 0;
            }

        }else if(frames == 128){
            this.frameMapping = new int[128];
            for(int i = 0; i<128; i++){
                this.frameMapping[i] = 0;
            }
        }
    }

    @Override
    public int loadProcessToMemory(String processName) throws NoSuchFileException, FileFormatException, MemoryOverflowException {
        //TODO: 1.1. Ler o arquivo "processName.txt" e quebra-lo em partes: textSize,dataSize e talvez processName,não sei. Caso dê errado, jogar "NoSuchFileException ou FileFormatException, dependendo.

        //TODO: 1.2. Criar uma PageTable com os dados obtidos e adicioná-la em listPageTables (não esquecer de "alocar" textSize,dataSize E pilha!).
        // A busca pela alocação terá que ser feita por First-fit, e caso não caiba, procurar pelo maior buraco primeiro e ir alocando e direção ao menor buraco.
        // Jogar MemoryOverflowException caso não tenha mais espaço!

        //TODO: 1.3. Alterar no frameMapping os frames alterados!

        //TODO: 1.4. Retornar o idProcess gerado (implica em criar getProcessId em pageTable).

        return 0;
    }

    @Override
    public int allocateMemoryToProcess(int processId, int size) throws InvalidProcessException, StackOverflowException, MemoryOverflowException {
        //TODO: 2.1. Procurar em listPageTables qual pageTable tem o processo com id == processId. (implica em criar getProcessId em pageTable.).
        // Jogar InvalidProcessException caso não encontre esse processId.

        //TODO: 2.2. "Alocar" na memória o int size através de first-fit. Lembrar da memória dinâmica!
        // Jogar MemoryOverflowException caso não tenha mais memória sobrando
        // Jogar StackOverflowException quando ....... ? (nao entendi quando pelo comentario da interface)

        //TODO: 2.3. Alterar o frameMapping!

        //TODO: 2.4. Retornar a quantidade de memória alocada (size, espero)

        return 0;
    }

    @Override
    public int freeMemoryFromProcess(int processId, int size) throws InvalidProcessException, NoSuchMemoryException {
        //TODO: 3.1. Exato oposto da função acima. Procurar por processId.
        // Jogar InvalidProcessException caso não encontre esse processId.

        //TODO: 3.2. "Liberar" size (lembrar, só pode tirar do heap. Isso implica que temos que criar um "identificador" em Page pra ver se é
        // De texto/data/pilha ou se é de heap? não sei.

        //TODO: 3.3. Alterar o frameMapping!

        //TODO: 3.4. Retornar a quantidade de memória liberada (size, espero)

        return 0;
    }

    @Override
    public void excludeProcessFromMemory(int processId) throws InvalidProcessException {
        //TODO: 4.1. Procurar por pageTable com processId.
        // Jogar InvalidProcessException caso não encontre esse processId.

        //TODO: 4.2. Excluir esse cara,basicamente
        // Cuidado, não sei se vai ter que fazer + alguma verificação no caso de ter 2 processos "iguais" por causa que eles compartilham algumas partes.

        //TODO: 4.3. Alterar o frameMapping!

    }

    @Override
    public void resetMemory() {
        //TODO: 5.1. Acho que é só resetar listPageTables e frameMapping, fodase

    }

    @Override
    public int getPhysicalAddress(int processId, int logicalAddress) throws InvalidProcessException, InvalidAddressException {
        //TODO: 6.1. Procurar por pageTable com processId.
        // Jogar InvalidProcessException caso não encontre esse processId.

        //TODO: 6.2. Não entendi direito oq ele quis dizer com "endereço físico", supostamente ele pode estar quebrado e varias partes diferentes, não?
        // Jogar InvalidAddressException caso o endereço seja menor do que 0 ou maior do que 1023 ou endereco invalido dentro do processo.

        //TODO: 6.3. Retornar esse endereço físico calculado.

        return 0;
    }

    @Override
    public String getBitMap() {
        //TODO: 7.1. É só arranjar um jeito bonitinho de retornar frameMapping.
        // será que ele quer para qual processo cada quadro está alocado? tomara que não kk

        return null;
    }

    @Override
    public String getPageTable(int processId) throws InvalidProcessException {
        //TODO: 8.1. Retornar a pageTable baseada no processId. deboas.
        // Jogar InvalidProcessException caso não encontre esse processId.

        return null;
    }

    @Override
    public String[] getProcessList() {
        //TODO: 9.1. Retornar o idProcess e processName de todo item em listPageTable.

        return new String[0];
    }

    //TODO: 10. Fazer as Exceptions. (FEITO)

    //TODO: 11. Criar a "interface" do usuário bonitinha (lembrar de trata se o cara tá entrando só com 32 ou 64 ou 128 frames, entre outras verificações).
}

import Exceptions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
public class MemoryManager implements ManagementInterface {
    int frames;
    List<PageTable> listPageTables;
    int[] frameMapping;
    int processIdIterator;


    public MemoryManager(int frames){
        this.frames = frames;
        this.listPageTables = new ArrayList<>();
        int processIdIterator = 1;

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
    public int loadProcessToMemory(String programName) throws NoSuchFileException, FileFormatException, MemoryOverflowException {

        //TODO: 1.1. Ler o arquivo "processName.txt" e quebra-lo em partes: textSize,dataSize e talvez processName,não sei. Caso dê errado,
        // jogar "NoSuchFileException ou FileFormatException, (FEITO)

        Map prData;
        prData = getProcessData(programName);
        //System.out.println(prData.get("data"));

        //TODO: 1.2. Criar uma PageTable com os dados obtidos e adicioná-la em listPageTables (não esquecer de "alocar" textSize,dataSize E pilha!).
        // A busca pela alocação terá que ser feita por First-fit, e caso não caiba, procurar pelo maior buraco primeiro e ir alocando e direção ao menor buraco.
        // Jogar MemoryOverflowException caso não tenha mais espaço! (FEITO)

        try {
            String processName = (String) prData.get("program");

            if (!programName.equals(processName))
                throw new FileFormatException("Nome do programa diferente do nome do arquivo");

            int textSize = Integer.parseInt((String) prData.get("text"));
            if(textSize < 1 || textSize > 960) throw new MemoryOverflowException("Segmento de texto de tamanho inválido\nSegmento deve possuir entre 1 e 960 bytes");
            int dataSize = Integer.parseInt((String) prData.get("data"));
            if(dataSize < 1 || dataSize > 928) throw new MemoryOverflowException("Segmento de dados de tamanho inválido\nSegmento deve possuir entre 0 e 928 bytes");
            int totalSize = textSize + dataSize + 64; //64 é o tamanho da pilha
            if(totalSize > 1024) throw new MemoryOverflowException("Tamanho máximo ultrapassado\nProcesso deve ter ao máximo 1024 bytes");

            int freeSpace = 0;
            for(int i = 0;i<frames;i++){
                if(frameMapping[i]==0){freeSpace += 32;} //Como nunca usaremos o espaço extra no bloco de outras funções, contamos somente as paginas totalmente livres.
            }

            boolean checkSameProcess = false;
            Iterator iterator = listPageTables.listIterator();
            while(iterator.hasNext()){
                PageTable pt = (PageTable) iterator.next();
                String pN = pt.getProcessName();
                if(pN.equals(processName)){
                    checkSameProcess = true;
                }
            }
            int allocatedTextSize = (int)Math.ceil((float)textSize/32) * 32;
            int allocatedDataSize = (int)Math.ceil((float)dataSize/32) * 32;
            int ts;
            if(checkSameProcess){ts = 64 + allocatedDataSize;}
            else{ts = allocatedTextSize + 64 + allocatedDataSize;}

            if(ts > freeSpace) throw new MemoryOverflowException("Memória disponível excedida.");


            PageTable pageTable = new PageTable(processIdIterator, processName, textSize, dataSize);
            int processId = processIdIterator;
            processIdIterator++;

            // Já que os blocos reservados para texto,dados estáticos e pilha não podem se misturar, devemos aloca-lôs separadamente.
            loadTextMemory(textSize, pageTable);
            loadStaticData(dataSize, pageTable);
            loadStack(64, pageTable);

            //adicionando a pageTable na listPageTable
            listPageTables.add(pageTable);

            //TODO: 1.3. Alterar no frameMapping os frames alterados! (FEITO)

            //TODO: 1.4. Retornar o idProcess gerado (implica em criar getProcessId em pageTable). (FEITO)

            return processId;
        } catch(NumberFormatException ne){
            throw new FileFormatException("Tipo do argumento inválido. Deveria ser Int");
        }
    }

    /**
     *
     * @param processName
     * @return Map
     * @throws NoSuchFileException
     * @throws FileFormatException
     *
     * Manipulates the program file into a Map.
     */
    private Map getProcessData(String processName) throws NoSuchFileException,FileFormatException {
        try{
            File process = (new File(processName + ".txt"));
            String line;
            String[] words;
            Map<String, String> prData = new HashMap<String, String>();
            Scanner scan = new Scanner(process);

            if(process.length() == 0) throw new FileFormatException("Arquivo Vazio");

            int i = 0;
            while(scan.hasNextLine()) {
                line = scan.nextLine();
                words = line.split("\\s+");
                prData.put(words[0], words[1]);
                i++;

                if(i == 1 && !words[0].equals("program")) throw new FileFormatException(words[0] + ": Nome do 1º argumento inválido. Deveria ser 'program'");
                if(i == 2 && !words[0].equals("text")) throw new FileFormatException(words[0] + ": Nome do 2º argumento inválido. Deveria ser 'text'");
                if(i == 3 && !words[0].equals("data")) throw new FileFormatException(words[0] + ": Nome do 3º argumento inválido. Deveria ser 'data'");

            }
            if(i != 3) throw new FileFormatException("Número inválido de argumentos");

            return prData;

        }catch(FileNotFoundException e) {
            throw new NoSuchFileException("Arquivo nao encontrado");
        }catch(ArrayIndexOutOfBoundsException e){
            throw new FileFormatException("Formatação do arquivo incorreta");
        }
    }

    /**
     *
     * @param totalSize
     * @param pageTable
     *
     * Aloca a parte de segmento de texto de um programa (alocação simples)
     *
     */
    private void loadTextMemory(int totalSize,PageTable pageTable){
        //TODO mini: falta ver se já existe um processo igual. Se existir, só ignora esse role todo kk (FEITO)

        Iterator iterator = listPageTables.listIterator();
        while(iterator.hasNext()){
            PageTable pt = (PageTable) iterator.next();
            String processName = pt.getProcessName();
            if(processName.equals(pageTable.getProcessName())){
                //Pegar páginas de texto
                int framesTextPt = (int)Math.ceil((float)pt.getTextSize()/32);
                for(int i = 0;i<framesTextPt;i++){
                    pageTable.addNewPage(pt.listPages.get(i).getFirstBitOfFrame(),false,false,32);
                }
                return;
            }
        }

        int lastBiggestHoleSize = 0;
        int lastBiggestHoleStart = 0;

        int holeStart = 0; //Quadro inicial do buraco sendo analisado
        int holeSize = 0; //Tamanho em quantidade de quadros

        for(int i = 0; i < frames; i++){
            //quadro está livre, então soma-se 32 ao quadro.
            if(frameMapping[i] == 0){
                holeSize += 1;
            }else{ //Quadro não está livre, checa se o buraco atual é o novo maior e continua a busca.
                if(holeSize > lastBiggestHoleSize){
                    lastBiggestHoleSize = holeSize;
                    lastBiggestHoleStart = holeStart;
                }
                holeSize = 0;

                //Continuando a busca após o "buraco de alocados"
                for(int j = i; j < frames; j++){
                    if(frameMapping[j] == 0){
                        holeStart = j;
                        i = j-1;
                        break;
                    }
                }
            }

            //Encontrei o primeiro buraco >= totalSize
            if(holeSize * 32 >= totalSize){
                for(int j = holeStart; j < holeSize+holeStart; j++){
                    frameMapping[j] = 1;
                    pageTable.addNewPage(j*32,false,false,32);
                }
                break;
            }

            //Chegou no final e não encontrou buraco == totalSize
            if(i == frames - 1){
                int lastHole = lastBiggestHoleStart + lastBiggestHoleSize;

                //alocando os quadros do buraco....
                for(int j = lastBiggestHoleStart; j < lastHole;j++){
                    frameMapping[j] = 1;
                    pageTable.addNewPage(j*32,false,false,32);
                }

                //Resetando o processo procurando o novo maior buraco...
                i = 0;
                lastBiggestHoleStart = 0;
                lastBiggestHoleSize = 0;
                totalSize -= lastBiggestHoleSize;
            }

        }
    }

    /**
     *
     * @param totalSize
     * @param pageTable
     *
     * Aloca a parte de dados estáticos do programa (aloca notificando qual é a ultima página, a qual o resto poderá ser usada para o heap.)
     */
    private void loadStaticData(int totalSize,PageTable pageTable){
        int lastBiggestHoleSize = 0;
        int lastBiggestHoleStart = 0;

        int holeStart = 0; //Quadro inicial do buraco sendo analisado
        int holeSize = 0; //Tamanho em quantidade de quadros


        for(int i = 0; i < frames; i++){


            //quadro está livre, então soma-se 32 ao quadro.
            if(frameMapping[i] == 0){
                holeSize += 1;

            }else{ //Quadro não está livre, checa se o buraco atual é o novo maior e continua a busca.

                if(holeSize > lastBiggestHoleSize){
                    lastBiggestHoleSize = holeSize;
                    lastBiggestHoleStart = holeStart;


                }

                holeSize = 0;

                //Continuando a busca após o "buraco de alocados"
                for(int j = i; j < frames; j++){
                    if(frameMapping[j] == 0){
                        holeStart = j;
                        i = j-1;
                        break;
                    }
                }
            }

            //Encontrei buraco >= totalSize
            if(holeSize * 32 >= totalSize){
                for(int j = holeStart; j < holeSize+holeStart; j++){
                    frameMapping[j] = 1;

                    //Checa se é a ultima página, que poderá oferecer espaço para heap.
                    if(j == (holeSize + holeStart - 1)){
                        int allocatedSpaceOnFrame = totalSize%32;
                        pageTable.addNewPage(j*32,true,false,allocatedSpaceOnFrame);
                    }else{
                        pageTable.addNewPage(j*32,false,false,32);
                    }
                }
                break;
            }

            //Chegou no final e não encontrou buraco == totalSize
            if(i == frames - 1){
                int lastHole = lastBiggestHoleStart + lastBiggestHoleSize;

                //alocando os quadros do buraco....
                for(int j = lastBiggestHoleStart; j < lastHole;j++){
                    frameMapping[j] = 1;

                    //Neste caso, a página nunca será a última.
                    pageTable.addNewPage(j*32,false,false,32);
                }

                //Resetando o processo procurando o novo maior buraco...
                i = 0;
                lastBiggestHoleStart = 0;
                lastBiggestHoleSize = 0;
                totalSize -= lastBiggestHoleSize;
            }

        }
    }

    /**
     *
     * @param totalSize
     * @param pageTable
     *
     * Aloca a parte de pilha do programa, as páginas correspondentes sempre serão no final da tabela de paginas.
     */
    private void loadStack(int totalSize,PageTable pageTable){
        int lastBiggestHoleSize = 0;
        int lastBiggestHoleStart = 0;

        int holeStart = 0; //Quadro inicial do buraco sendo analisado
        int holeSize = 0; //Tamanho em quantidade de quadros


        for(int i = 0; i < frames; i++){


            //quadro está livre, então soma-se 32 ao quadro.
            if(frameMapping[i] == 0){
                holeSize += 1;

            }else{ //Quadro não está livre, checa se o buraco atual é o novo maior e continua a busca.

                if(holeSize > lastBiggestHoleSize){
                    lastBiggestHoleSize = holeSize;
                    lastBiggestHoleStart = holeStart;
                }

                holeSize = 0;

                //Continuando a busca após o "buraco de alocados"
                for(int j = i; j < frames; j++){
                    if(frameMapping[j] == 0){
                        holeStart = j;
                        i = j-1;
                        break;
                    }
                }
            }

            //Encontrei o primeiro buraco >= totalSize
            if(holeSize * 32 >= totalSize){
                for(int j = holeStart; j < holeSize+holeStart; j++){
                    frameMapping[j] = 1;
                    Page page = new Page(30+j-holeStart,1,j*32,false,false,32);
                    pageTable.listPages.add(page);
                }
                break;
            }

            //Chegou no final e não encontrou buraco == totalSize
            if(i == frames - 1){
                int lastHole = lastBiggestHoleStart + lastBiggestHoleSize;

                //alocando os quadros do buraco....
                for(int j = lastBiggestHoleStart; j < lastHole;j++){
                    frameMapping[j] = 1;
                    Page page = new Page(30+j-lastBiggestHoleStart,1,j*32,false,false,32);
                    pageTable.listPages.add(page);
                }

                //Resetando o processo procurando o novo maior buraco...
                i = 0;
                lastBiggestHoleStart = 0;
                lastBiggestHoleSize = 0;
                totalSize -= lastBiggestHoleSize;
            }

        }
    }

    @Override
    public int allocateMemoryToProcess(int processId, int size) throws InvalidProcessException, StackOverflowException, MemoryOverflowException {
        //TODO: 2.1. Procurar em listPageTables qual pageTable tem o processo com id == processId. (implica em criar getProcessId em pageTable.).
        // Jogar InvalidProcessException caso não encontre esse processId.

        PageTable pageTable = returnPageTable(processId);
        int totalSize = pageTable.getDataSize() + pageTable.getTextSize() + pageTable.getHeapSize() + 64 + size; // 64 == stackSize
        if(totalSize > 1024){throw new StackOverflowException("Tamanho máximo de processo excedido. Processo deve ter ao máximo 1024 bytes");}

        int freeSpace = 0;
        for(int i = 0;i<frames;i++){
            if(frameMapping[i]==0){
                freeSpace += 32;
            }else {
                Iterator iterator = pageTable.listPages.listIterator();
                while(iterator.hasNext()){
                    Page page = (Page) iterator.next();

                    if(page.getIsLastPageOfStaticData()){
                        freeSpace += 32 - page.getAllocatedSpaceOnFrame();

                    }if(page.getIsLastPageOfHeap()){
                        freeSpace += 32 - page.getAllocatedSpaceOnFrame();
                    }

                }
            }
        }

        if(freeSpace < totalSize){throw new MemoryOverflowException("Não há espaço restante na memória.");}

        //TODO: 2.2. "Alocar" na memória o int size através de first-fit. Lembrar da memória dinâmica!
        // Jogar MemoryOverflowException caso não tenha mais memória sobrando
        // Jogar StackOverflowException quando text+data+pilha+Heap > 1024.

        //TODO: 2.3. Alterar o frameMapping!

        loadHeap(size,pageTable);

        //TODO: 2.4. Retornar a quantidade de memória alocada (size, espero)

        return size;
    }


    /**
     *
     * @param heapSize
     * @param pageTable
     *
     * Aloca a parte de heap do programa (aloca notificando qual é a ultima página, a qual o resto poderá ser usada para o heap.)
     */
    private void loadHeap(int heapSize,PageTable pageTable){
        int lastBiggestHoleSize = 0;
        int lastBiggestHoleStart = 0;

        int holeStart = 0; //Quadro inicial do buraco sendo analisado
        int holeSize = 0; //Tamanho em quantidade de quadros

        if(pageTable.getLastPageOfStaticData().getAllocatedSpaceOnFrame() - 32 == 0 && pageTable.getDataSize()%32 + pageTable.getHeapSize() > 32){
            Page page = pageTable.getLastPageOfHeap();
            int freeSpaceOnPage = 32 - page.getAllocatedSpaceOnFrame();

            if(heapSize <= freeSpaceOnPage){
                page.setAllocatedSpaceOnFrame(page.getAllocatedSpaceOnFrame() + heapSize);
                pageTable.setHeapSize(heapSize);
                heapSize = 0;
            }else{
                page.setAllocatedSpaceOnFrame(page.getAllocatedSpaceOnFrame()+freeSpaceOnPage);
                heapSize -= freeSpaceOnPage;
            }
        }else if(pageTable.getLastPageOfStaticData().getAllocatedSpaceOnFrame() < 32){
            Page page = pageTable.getLastPageOfStaticData();
            int freeSpaceOnPage = 32 - page.getAllocatedSpaceOnFrame();

            if(heapSize <= freeSpaceOnPage){
                page.setAllocatedSpaceOnFrame(page.getAllocatedSpaceOnFrame() + heapSize);
                pageTable.setHeapSize(heapSize);
                heapSize = 0;
            }else{
                page.setAllocatedSpaceOnFrame(page.getAllocatedSpaceOnFrame()+freeSpaceOnPage);
                heapSize -= freeSpaceOnPage;
            }
        }

        for(int i = 0; i < frames; i++){

            //quadro está livre, então soma-se 32 ao quadro.
            if(frameMapping[i] == 0){
                holeSize += 1;

            }else{ //Quadro não está livre, checa se o buraco atual é o novo maior e continua a busca.

                if(holeSize > lastBiggestHoleSize){
                    lastBiggestHoleSize = holeSize;
                    lastBiggestHoleStart = holeStart;
                }

                holeSize = 0;

                //Continuando a busca após o "buraco de alocados"
                for(int j = i; j < frames; j++){
                    if(frameMapping[j] == 0){
                        holeStart = j;
                        i = j-1;
                        break;
                    }
                }
            }

            //Encontrei buraco >= heapSize
            if(holeSize * 32 >= heapSize){
                for(int j = holeStart; j < holeSize+holeStart; j++){
                    frameMapping[j] = 1;

                    //Checa se é a ultima página, que poderá oferecer espaço para heap.
                    if(j == (holeSize + holeStart - 1)){
                        int allocatedSpaceOnFrame = heapSize%32;
                        pageTable.addNewPage(j*32,false,true, allocatedSpaceOnFrame);
                        pageTable.setHeapSize(pageTable.getHeapSize()+holeSize*32);
                    }else{
                        pageTable.addNewPage(j*32,false,false,32);
                        pageTable.setHeapSize(pageTable.getHeapSize()+holeSize*32);
                    }
                }
                break;
            }

            //Chegou no final e não encontrou buraco == totalSize
            if(i == frames - 1){
                int lastHole = lastBiggestHoleStart + lastBiggestHoleSize;

                //alocando os quadros do buraco....
                for(int j = lastBiggestHoleStart; j < lastHole;j++){
                    frameMapping[j] = 1;

                    //Neste caso, a página nunca será a última.
                    pageTable.addNewPage(j*32,false,false,32);
                    pageTable.setHeapSize(pageTable.getHeapSize()+holeSize*32);
                }

                //Resetando o processo procurando o novo maior buraco...
                i = 0;
                lastBiggestHoleStart = 0;
                lastBiggestHoleSize = 0;
                heapSize -= lastBiggestHoleSize;
            }

        }
    }

    @Override
    public int freeMemoryFromProcess(int processId, int size) throws InvalidProcessException, NoSuchMemoryException {
        //TODO: 3.1. Exato oposto da função acima. Procurar por processId.
        // Jogar InvalidProcessException caso não encontre esse processId.

        PageTable pageTable = returnPageTable(processId);
        if(size > pageTable.getHeapSize()){throw new NoSuchMemoryException("Memória dinâmica alocada menor do que a quantidade inserida.");}

        freeHeap(size,pageTable);


        //TODO: 3.2. "Liberar" size (lembrar, só pode tirar do heap. Isso implica que temos que criar um "identificador" em Page pra ver se é
        // De texto/data/pilha ou se é de heap? não sei.

        //TODO: 3.3. Alterar o frameMapping!

        //TODO: 3.4. Retornar a quantidade de memória liberada (size, espero)

        return size;
    }

    /**
     *
     * @param freeHeap
     * @param pageTable
     *
     * libera a parte de heap do programa (aloca notificando qual é a ultima página, a qual o resto poderá ser usada para o heap.)
     */
    private void freeHeap(int freeHeap, PageTable pageTable){
        // Caso hajam páginas que abriguem somente heap
        while(freeHeap != 0){
            //  Caso NÃO hajam páginas que abriguem somente heap
            if(pageTable.getLastPageOfStaticData().getAllocatedSpaceOnFrame() < 32 || (32 - pageTable.getDataSize()%32) == pageTable.getHeapSize()){
                Page page = pageTable.getLastPageOfStaticData();
                page.setAllocatedSpaceOnFrame(page.getAllocatedSpaceOnFrame()-freeHeap);
                freeHeap = 0;
            }

            Page page = pageTable.getLastPageOfHeap();
            //Caso a página tenha alocada a quantidade restante que deseja-se liberar ou mais.
            if(freeHeap < page.getAllocatedSpaceOnFrame() ){
                page.setAllocatedSpaceOnFrame(page.getAllocatedSpaceOnFrame()-freeHeap);
                freeHeap = 0;
            }else{
                page.setAllocatedSpaceOnFrame(0);
                page.setValidationBit(0);
                int index = page.getFirstBitOfFrame()/32;
                frameMapping[index] = 0;

                //  Caso não seja primeira página exclusivamente de heap, seta a página anterior como a ultima.
                if(page.getIdPage() > pageTable.getLastPageOfStaticData().getIdPage()+1){
                   Page p = pageTable.getPageById(page.getIdPage()-1);
                   p.setIsLastPageOfHeapData(true);
               }

                freeHeap -= page.getAllocatedSpaceOnFrame();
                pageTable.setHeapSize(pageTable.heapSize-page.getAllocatedSpaceOnFrame());

                pageTable.removePage(page.getIdPage());
                page.setIsLastPageOfHeapData(false);
            }
        }
    }

    @Override
    public void excludeProcessFromMemory(int processId) throws InvalidProcessException {
        //TODO: 4.1. Procurar por pageTable com processId.
        // Jogar InvalidProcessException caso não encontre esse processId. (FEITO)

        PageTable pageTable = returnPageTable(processId);

        Iterator iterator = listPageTables.listIterator();


        //TODO: 4.2. Excluir esse cara,basicamente
        // Cuidado, não sei se vai ter que fazer + alguma verificação no caso de ter 2 processos "iguais" por causa que eles compartilham algumas partes.

        //TODO: 4.3. Alterar o frameMapping!

    }

    @Override
    public void resetMemory() {
        //TODO: 5.1. Acho que é só resetar listPageTables e frameMapping, fodase
        listPageTables = new ArrayList<>();
        frameMapping = new int[frames];
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
        String result = "";
        for(int i = 0;i < frames;i++){
            result = result + "[" + i + "]: " + frameMapping[i] + "\n";
        }

        return result;
    }

    @Override
    public String getPageTable(int processId) throws InvalidProcessException {
        //TODO: 8.1. Retornar a pageTable baseada no processId. deboas.
        // Jogar InvalidProcessException caso não encontre esse processId.

        PageTable pageTable = returnPageTable(processId);

        return pageTable.toString();
    }

    private PageTable returnPageTable(int processId) throws InvalidProcessException{
        Iterator iterator = listPageTables.listIterator();
        boolean token = false;
        PageTable returnPageTable = new PageTable(0,"a",1,1);
        while(iterator.hasNext() || token){
            PageTable pagetable = (PageTable) iterator.next();
            if(pagetable.getIdProcess() == processId){
                returnPageTable = pagetable;
                token = true;
                break;
            }
        }
        if(!token){throw new InvalidProcessException("Processo inexistente.");}
        return returnPageTable;
    }

    @Override
    public String[] getProcessList() {
        //TODO: 9.1. Retornar o idProcess e processName de todo item em listPageTable
        String[] lista = new String[listPageTables.size()];
        int i = 0;

        Iterator iterator = listPageTables.listIterator();
        do{
            PageTable pageTable = (PageTable) iterator.next();
            lista[i] = "ID: " + pageTable.getIdProcess() + " - " + pageTable.getProcessName();
            i++;
        } while(iterator.hasNext());

        return lista;
    }

    public void printProcessList(String[] processList){
        int processQuantity = listPageTables.size();
        for(int i = 0; i < processQuantity; i++){
            System.out.println(processList[i]);
        }

    }





    //TODO: 10. Fazer as Exceptions. (FEITO)

    //TODO: 11. Criar a "interface" do usuário bonitinha (lembrar de trata se o cara tá entrando só com 32 ou 64 ou 128 frames, entre outras verificações).
}

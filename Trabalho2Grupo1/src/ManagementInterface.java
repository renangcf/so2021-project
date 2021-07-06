/**
 * @author Clever Ricardo Guareis de Farias
 */
public interface ManagementInterface {

    /** 
	 * Carrega um processo para a momoria de acordo com a especificacao definida em arquivo 
	 * @param processName o nome do arquivo contendo os dados do processo
	 * @throws NoSuchFileException no caso de um arquivo invalido ou nao encontrado
	 * @throws FileFormatException no caso de um de violacao do formato de representacao do arquivo
	 * @throws MemoryOverflowException caso nao haja memoria suficiente para carregar o processo para a memoria
	 * @return o identificador do processo carregado na memoria
	 */
    public int loadProcessToMemory(String processName) throws NoSuchFileException, FileFormatException, MemoryOverflowException;

    /** 
	 * Aloca memoria dinamica (heap) para um processo virtual carregado na memoria principal 
	 * @param processId o identificador do processo
	 * @param size o tamanho do bloco de memoria a ser alocado
	 * @throws InvalidProcessException no caso de um identificador de processo invalido
	 * @throws StackOverflowException no caso de um tamanho de memoria maior do que a quantidade de memoria dinamica disponivel para o processo
	 * @throws MemoryOverflowException caso nao haja memoria suficiente para atender a solicitacao		
	 * @return a quantidade de memoria alocada
	 */
    public int allocateMemoryToProcess(int processId, int size) throws InvalidProcessException, StackOverflowException, MemoryOverflowException;
	
    /** 
	 * Libera um bloco de memoria dinâmica (heap) ocupado por um processo 
	 * @param processId o identificador do processo
	 * @param size o tamanho do bloco de memoria a ser liberado
	 * @throws InvalidProcessException no caso de um identificador de processo invalido
	 * @throws NoSuchMemoryException no caso de um tamanho de memoria maior do que a quantidade de memoria dinamica alocada para o processo
	 * @return a quantidade de memoria liberada
	 */	
    public int freeMemoryFromProcess(int processId, int size) throws InvalidProcessException, NoSuchMemoryException;

    /** 
	 * Exclui processo da memoria, liberando toda a memoria utilizada por esse processo de forma exclusiva
	 * @param processId o identificador do processo
	 * @throws InvalidProcessException no caso de um identificador de processo invalido
	 */
    public void excludeProcessFromMemory(int processId) throws InvalidProcessException;
	
    /** 
	 * Exclui todos os processo da memoria, liberando toda a memoria utilizada por esses processos
	 */
    public void resetMemory();
	
    /** 
	 * Traduz um endereco logico de um processo para um endereco fisico
	 * @param processId o identificador do processo
	 * @param logicalAddress o endereco logico do processo (entre 0 e 1023)
	 * @throws InvalidProcessException no caso de um identificador de processo invalido
	 * @throws InvalidAddressException no caso de um endereco logico invalido (menor do que 0 ou maior do que 1023 ou endereco invalido dentro do processo)
	 * @return o endereco fisico correspondente
	 */	
    public int getPhysicalAddress(int processId, int logicalAddress) throws InvalidProcessException, InvalidAddressException;
	
    /** 
	 * Obtem o mapa de bits dos quadros da memoria
	 * @return o mapa de bits da memoria como uma string
	 */	
	public String getBitMap();
	
    /** 
	 * Obtem a tabela de paginas de um processo
	 * @param processId o identificador do processo
	 * @throws InvalidProcessException no caso de um identificador de processo invalido
	 * @return o tabela de paginas do processo como uma string		
	 */
	public String getPageTable(int processId) throws InvalidProcessException;
	
    /** 
	 * Obtem informacoes sobre os processos carregados na memoria (nome e identificadores)
	 * @return a lista de processos carregados na memoria como um array de strings		
	 */
	public String[] getProcessList();

}


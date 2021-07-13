import Exceptions.InvalidProcessException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PageTable {
    //List<Page> listPages; //Max size == 32.
    Page[] listPages;

    int currentLatestPage; //Ultima página registrada, desconsiderando as páginas que alocam a pilha
    int idProcess;
    String processName;
    int textSize;
    int dataSize;
    int heapSize;


    public PageTable(int idProcess,String processName,int textSize,int dataSize){
        listPages = new Page[32];
        for(int i = 0; i < 32; i++){
            Page page = new Page(i,0,-1,false,false,0);
            listPages[i] = page;
        }

        this.currentLatestPage = 0;
        this.idProcess = idProcess;
        this.processName = processName;
        this.textSize = textSize;
        this.dataSize = dataSize;
        this.heapSize = 0;
    }

    public int getIdProcess(){
        return idProcess;
    }
    public String getProcessName(){return processName;}
    public int getTextSize(){return textSize;}
    public int getDataSize(){return dataSize;}
    public int getHeapSize(){return heapSize;}

    public void setHeapSize(int newHeapSize){this.heapSize = newHeapSize;}

    public String toString(){
        String lista = "";

        for(int i = 0; i < 32; i++){
            Page page = listPages[i];
            lista = lista + "\n" + page.toString();
        }
        return lista;
    }

    public void addNewPage(int firstBitOfFrame,boolean isLastPageOfStaticData,boolean isLastPageOfHeap,int allocatedSpaceOnFrame){
        Page page = new Page(currentLatestPage,1,firstBitOfFrame,isLastPageOfStaticData,isLastPageOfHeap,allocatedSpaceOnFrame);
        listPages[currentLatestPage] = page;
        currentLatestPage++;
    }

    public int getFirstBitOfFrameOfPage(int pageId){
        int result = -1;
        
        for(int i = 0; i < 32; i++){
            if(i == pageId){
                result = listPages[i].getFirstBitOfFrame();
                break;
            }
        }
        return result;
    }

    public Page getLastPageOfStaticData(){
        Page result = new Page(-1,-1,-1,false,false,32);
        for(int i = 0; i < 32; i++){
            if(listPages[i].getIsLastPageOfStaticData()){
                result = listPages[i];
                break;
            }
        }
        return result;
    }

    public Page getLastPageOfHeap(){
        Page result = new Page(-1,-1,-1,false,false,32);
        for(int i = 0; i < 32; i++){
            if(listPages[i].getIsLastPageOfHeap()){
                result = listPages[i];
                break;
            }
        }
        return result;
    }


    public void removePage(int idPage){

        for(int i = 0; i < 32; i++){
            if(i == idPage){
                listPages[i].setValidationBit(0);
                listPages[i].setFirstBitOfFrame(-1);
            }
        }

        currentLatestPage--;
    }

    public Page getPageById(int id){
        return  listPages[id];
    }

}

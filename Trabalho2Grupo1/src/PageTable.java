import Exceptions.InvalidProcessException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PageTable {
    List<Page> listPages; //Max size == 32.
    int currentLatestPage; //Ultima página registrada, desconsiderando as páginas que alocam a pilha
    int idProcess;
    String processName;
    int textSize;
    int dataSize;
    int heapSize;


    public PageTable(int idProcess,String processName,int textSize,int dataSize){
        listPages = new ArrayList<>();
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
        Iterator iterator = listPages.listIterator();

        Page page = (Page) iterator.next();
        lista = lista + page.toString();
        while (iterator.hasNext()) {
            page = (Page) iterator.next();
            lista = lista + "\n" + page.toString();
        }

        return lista;
    }

    public void addNewPage(int firstBitOfFrame,boolean isLastPageOfStaticData,boolean isLastPageOfHeap,int allocatedSpaceOnFrame){
        Page page = new Page(currentLatestPage,1,firstBitOfFrame,isLastPageOfStaticData,isLastPageOfHeap,allocatedSpaceOnFrame);
        currentLatestPage++;

        listPages.add(page);
    }

    public int getFirstBitOfFrameOfPage(int pageId){
        int result = -1;

        Iterator iterator = listPages.listIterator();
        do{
            Page page = (Page) iterator.next();

            if(pageId == page.getIdPage()){
                result = page.getFirstBitOfFrame();
            }

        }while(iterator.hasNext());

        return result;
    }

    public Page getLastPageOfStaticData(){
        Page result = new Page(-1,-1,-1,false,false,32);
        Iterator iterator = listPages.listIterator();
        while (iterator.hasNext()){
            Page page = (Page) iterator.next();
            if(page.getIsLastPageOfStaticData()){
                result = page;
                break;
            }
        }
        return result;
    }

    public Page getLastPageOfHeap(){
        Page result = new Page(-1,-1,-1,false,false,32);
        Iterator iterator = listPages.listIterator();
        do{
            Page page = (Page) iterator.next();
            if(page.getIsLastPageOfHeap()){
                result = page;
            }
        }while (iterator.hasNext());
        return result;
    }

    public void removePage(int idPage){
        Iterator iterator = listPages.listIterator();
        int i = 0;
        do{
            Page page = (Page) iterator.next();
            if(page.getIdPage()==idPage){
                listPages.remove(i);
                break;
            }
            i++;
            currentLatestPage--;
        }while(iterator.hasNext());
    }

    public Page getPageById(int id){
        Page result = new Page(-1,-1,-1,false,false,-1);
        Iterator iterator = listPages.listIterator();
        do{
            Page page = (Page) iterator.next();
            if(page.getIdPage()==id){
                result = page;
                break;
            }
        }while(iterator.hasNext());
        return  result;
    }

}

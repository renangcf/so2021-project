public class Page {
    int idPage;
    int validationBit;
    int firstBitOfFrame;
    boolean isLastPageOfStaticData;
    int allocatedSpaceOnFrame;

    public Page(int idPage,int validationBit,int firstBitOfFrame,boolean isLastPageOfStaticData,int allocatedSpaceOnFrame){
        this.idPage =idPage;
        this.validationBit = validationBit;
        this.firstBitOfFrame = firstBitOfFrame;
        this.isLastPageOfStaticData = isLastPageOfStaticData;
        this.allocatedSpaceOnFrame = allocatedSpaceOnFrame;
    }

    public String toString(){
        String stringPage = Integer.toString(idPage) + " " + Integer.toString(firstBitOfFrame) + " " + Integer.toString(validationBit);
        return stringPage;
    }
    public int getFirstBitOfFrame(){return firstBitOfFrame;}
    public int getIdPage(){return idPage;};
}

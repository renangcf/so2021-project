public class Page {
    int idPage;
    int validationBit;
    int baseByte;
    boolean isLastPageOfStaticData;
    int allocatedSpaceOnFrame;

    public Page(int idPage,int validationBit,int firstBitOfFrame,boolean isLastPageOfStaticData,int allocatedSpaceOnFrame){
        this.idPage =idPage;
        this.validationBit = validationBit;
        this.baseByte = firstBitOfFrame;
        this.isLastPageOfStaticData = isLastPageOfStaticData;
        this.allocatedSpaceOnFrame = allocatedSpaceOnFrame;
    }

    public String toString(){
        String stringPage = Integer.toString(idPage) + Integer.toString(baseByte) + Integer.toString(validationBit);
        return stringPage;
    }
}

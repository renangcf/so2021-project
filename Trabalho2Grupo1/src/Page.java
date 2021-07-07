public class Page {
    int idPage;
    int validationBit;
    int baseByte;

    public Page(int idPage,int validationBit,int firstBitOfFrame){
        this.idPage =idPage;
        this.validationBit = validationBit;
        this.baseByte = firstBitOfFrame;
    }

    public String toString(){
        String stringPage = Integer.toString(idPage) + Integer.toString(baseByte) + Integer.toString(validationBit);
        return stringPage;
    }
}

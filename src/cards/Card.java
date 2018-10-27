package cards;


public class Card implements Comparable<Card>{

    private String color;// 1,2,3,4,5
    //color=5 means jokers, color=6 means big king
    private String name;//	A,2,3,4,5,6,7,8,9,10,J,Q,K,big king,joker
    private int num;//the number of the card
    private boolean use;//use =false means has not been used
    public Card(String name,int number) {
        this.name = name;
        this.num = number;
        this.use = false;
    }
    public boolean getUse() {
        //check if the card has been used
        return use;
    }
    public void setUse() {
        //set the card as used
        this.use = true;
    }

    public String getColor() {
        return color==null?"":color;
    }
    public String getName() {
        return name;
    }
    public int getNum() {
        return num;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public boolean readCompare(String name) {
        if (name.equals(this.color+this.name))
            return true;
        else return false;
    }
    public boolean compareWithCard (Card newCard) {
        if (color.equals(newCard.getColor()) && name.equals(newCard.getName())) {
            return true;
        }
        else return false;
    }
    @Override
    public int compareTo(Card o) {
        return this.num - o.num;
    }



}

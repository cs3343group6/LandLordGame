package cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PlayPoker {
    public static void main(String[] args) {
        //first step init the shown table and set up poker's cards
        Init();
        List<Card> newPoker = newPoker();
        //step2 wash all the cards
        washPoker(newPoker);
        //step3: send out the cards to three players
        //set up the three players
        List<Player> playerList = playerInit();
        Player p1 = playerList.get(0);
        Player p2 = playerList.get(1);
        Player p3 = playerList.get(2);
        //lastCards
        List<Card> lastCards = sendPoker(newPoker,p1,p2,p3);

        //Here for convenience, I have just set the player1 as the landlord for future versions the functions will be added
        p1.setRoletType(Constant.LandLord);
        System.out.println("This round"+p1.getRoleName()+"is the landlord");
        p2.setRoletType(Constant.NotLandLord);
        p3.setRoletType(Constant.NotLandLord);
        //Give all the lastCards to the landlord
        List<Card> pList = p1.getPokerList();
        pList.addAll(lastCards);
        Collections.sort(pList);//reorder the cards
        p1.setPokerList(pList);//renew the Poker list
        System.out.println();
        //start the game
        startPlay(playerList);

    }

    public static void startPlay(List<Player> personList) {
        System.out.println("********************************开始斗地主***********************");
        System.out.println();
        //As the landlord set mechanism hasn't been coded yet, so there should be considered the first player as the landlor
        Scanner sc = new Scanner(System.in);
        //是否结束
        boolean isEnd = false;
        Player person = null;//玩家
        List<Card> pokerList = null;//玩家的牌
        List<Card> lastRoundCards = new ArrayList<Card>();//上一轮玩家的出牌
        List<Card> thisRoundCards = null;

        while (!isEnd) {
            for (int i = 0; i < personList.size(); i++) {
                person = personList.get(i);
                pokerList = person.getPokerList();
                System.out.println("\n"+person.getRoleName() + " starts to give the cards with" + pokerList.size()+" cards");
                System.out.println("\n Now these are the pokers which he have\n");
                printPoker(pokerList);
                System.out.println("\n Now please input the cards which you want to give out \n");
                System.out.println("\n If no cards can be given out please input \'no\' \n");

                String cardInputString;//input the whole line of cards
                //check the error, reads until all the input is right
                int flag1 = 0;
                int flag2 = 0;
                do {
                    flag1 = 0;
                    do {
                        cardInputString = sc.nextLine().replaceAll("\\s*", "");
                        if (cardInputString.equals("no")) {
                            flag1 = -1;
                            flag2 = -1;
                            break;
                        }
                        flag1 = errorHandler(cardInputString, pokerList);
                        if (flag1==0) {
                            System.out.println("You have just input a poker which you don't have \n");
                        }
                    } while (flag1 == 0);
                    if (flag2 != 0) { break;} //it means that means this round skips
                    //now generate the cards list
                    thisRoundCards = generateCards(cardInputString, pokerList);
                    Collections.sort(thisRoundCards);
                    System.out.println("These are the cards you want to output\n");
                    printPoker(thisRoundCards);
                    flag2 = CardControl.checkCards(thisRoundCards, lastRoundCards);
                    if (flag2 == 0) {
                        System.out.println("Your card is smaller\n");
                    }
                    //judge if thisRoundsCards is larger than last Round's cards

                }while (flag2 == 0);
                //正常出牌
                if ((flag2 == -1) && (flag1 == -1)) {continue;}
                System.out.println("You have succeeded output all the cards\n");
                lastRoundCards = thisRoundCards;
                removeCards(thisRoundCards, pokerList);//remove all the cards in this player
                System.out.println("Now is the cards which you left\n");
                printPoker(pokerList);
                //judge if anyone has no cards at all
                if (pokerList.size() == 0) {
                    isEnd = true;
                    System.out.println("Player" + i+1 + "win");
                }
            }
        }
    }
    public static void Init() {
        System.out.println("****************************Game starts********************");
        System.out.println();
    }

    public static void removeCards(List <Card> thisRoundCards, List <Card> allCards) {
        for (int iterCard1 = 0; iterCard1 < thisRoundCards.size(); iterCard1++)
            for (int iterCard2 = 0; iterCard2 < allCards.size(); iterCard2++)
                if (thisRoundCards.get(iterCard1).compareWithCard(allCards.get(iterCard2))) {
                    allCards.remove(iterCard2);
                    break;
                }
    }
    public static int errorHandler(String s, List <Card> pokerList) {
        String substring;
        //This function is for to detect which card has been inputted and actually not in the card's list
        int flag = 0; //flag marks if the String 1 means the pokerList contains the string
        for (int j = 0; j < s.length(); j = j + 2) {
            substring = s.substring(j, j+2);
            for (int i = 0; i < pokerList.size(); i++) {
                if (pokerList.get(i).readCompare(substring)) {
                    flag = 1;
                    break;
                }
            }
        }
        return flag;

    }

    public static List <Card> generateCards(String s, List <Card> pokerList) {
        String substring;
        //This function is for to generate a list of cards
        List <Card> generateCardsList = new ArrayList<Card>();
        for (int j = 0; j < s.length(); j = j + 2) {
            substring = s.substring(j, j+2);
            for (int i = 0; i < pokerList.size(); i++) {
                if ((!pokerList.get(i).getUse())&&pokerList.get(i).readCompare(substring)) {
                    generateCardsList.add(pokerList.get(i));
                    pokerList.get(i).setUse();
                    break;
                }
            }
        }
        return generateCardsList;
    }


    public static void printPoker(List<Card> list) {
        for (int i=0; i < list.size(); i++) {
            System.out.print(list.get(i).getColor()+list.get(i).getName()+" ");
            //Here as to the color define, it is a little point to improve
        }
    }


    public static List<Card> sendPoker(List<Card> list, Player p1, Player p2, Player p3){
        //first man's cards
        List<Card> pList1 = new ArrayList<Card>();
        //second man's cards
        List<Card> pList2 = new ArrayList<Card>();
        //third man's cards
        List<Card> pList3 = new ArrayList<Card>();
        //底牌
        List<Card> lastCards = new ArrayList<Card>();
        for (int i = 0; i < list.size(); i++) {
            if(i<list.size()-3){
                pList1.add(list.get(i));
                pList2.add(list.get(++i));
                pList3.add(list.get(++i));
            }else {
                lastCards.add(list.get(i));
            }
        }
        Collections.sort(pList1);
        Collections.sort(pList2);
        Collections.sort(pList3);
        p1.setPokerList(pList1);
        p2.setPokerList(pList2);
        p3.setPokerList(pList3);
        return lastCards;
    }


    public static List<Player> playerInit(){
        List<Player> list = new ArrayList<Player>();
        String P1name,P2name,P3name;
        System.out.println("*****************Input the first player's name**********");
        System.out.println();
        Scanner sc = new Scanner(System.in);
        P1name = sc.nextLine();
        System.out.println("*****************Input the second player's name**********");
        System.out.println();
        P2name = sc.nextLine();
        System.out.println("*****************Input the third player's name**********");
        System.out.println();
        P3name = sc.nextLine();
        Player p1 = new Player(P1name);
        list.add(p1);
        Player p2 = new Player(P2name);
        list.add(p2);
        Player p3 = new Player(P3name);
        list.add(p3);
        return list;
    }
    public static List<Card> newPoker(){
        List<Card> list = new ArrayList<Card>();
        Card poker = null;
        String [] pokerName = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "1", "2"};
        String [] pokerColor = {"1", "2", "3", "4", "5"};//1,2,3,4 黑桃,红桃,梅花,方块 5大小王
        for (int i = 0; i < pokerColor.length; i++) {
            for (int j = 0; j < pokerName.length; j++) {
                poker = new Card(pokerName[j],j);
                poker.setColor(pokerColor[i]);
                list.add(poker);
            }
        }
        //specially deal with the king and joker
        Card joker = new Card("s", 14);//s is the small king
        Card king = new Card("b", 15);//b is the bigger king
        joker.setColor("5");
        king.setColor("5");
        list.add(joker);
        list.add(king);
        return list;
    }
    public static void washPoker(List<Card> list){
        //利用集合工具类，随机打乱顺序
        Collections.shuffle(list);
    }


}

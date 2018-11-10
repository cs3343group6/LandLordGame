package cards;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Delayed;

public class CardControl {

    //得到最大相同数
    public static void getMax(Card_index card_index,List<Card> list){
        int count[]=new int[14];//1-13各算一种,王算第14种
        for(int i=0;i<14;i++)
            count[i]=0;
        for(int i=0,len=list.size();i<len;i++){
            if(list.get(i).getColor().equals("5"))
                count[13]++;
            else
                count[list.get(i).getNum()]++;
        }
        for(int i=0;i<14;i++)
        {
            switch (count[i]) {
                case 1:
                    card_index.a[0].add(i+1);
                    break;
                case 2:
                    card_index.a[1].add(i+1);
                    break;
                case 3:
                    card_index.a[2].add(i+1);
                    break;
                case 4:
                    card_index.a[3].add(i+1);
                    break;
            }
        }
    }
    //隐藏之前出过的牌
	/*
	public static void hideCards(List<Card> list){
		for(int i=0,len=list.size();i<len;i++){
			list.get(i).setVisible(false);
		}
	}*/
    //检查牌的是否能出
    public static int checkCards(List<Card> c,List<Card> lastRound){
        //compare between this round and lastround
        CardType cType = CardControl.judgeType(c);
        CardType lType = CardControl.judgeType(lastRound);
        if (lastRound.size() == 0)
            return 1;
        //if not c4 type and the size is different than can't pass
        if(cType!=CardType.c4 && c.size()!=lastRound.size())
            return 0;
        //compare the type of the cards
        if(cType!=CardType.c4 && cType!=lType)
        {

            return 0;
        }
        //now it's the same size, and check the level
        //王炸弹
        if(cType==CardType.c4)
        {
            if(c.size()==2)
                return 1;
            if(lastRound.size()==2)
                return 0;
        }
        //单牌,对子,3带,4炸弹
        if(cType==CardType.c1||cType==CardType.c2||cType==CardType.c3||cType==CardType.c4){
            if(c.get(0).getNum()<=(lastRound.get(0).getNum()))
            {
                return 0;
            }else {
                return 1;
            }
        }
        //顺子,连队，飞机裸
        if(cType==CardType.c123||cType==CardType.c1122||cType==CardType.c111222)
        {
            if(c.get(0).getNum()<=lastRound.get(0).getNum())
                return 0;
            else
                return 1;
        }
        //按重复多少排序
        //3带1,3带2 ,飞机带单，双,4带1,2,只需比较第一个就行，独一无二的
        if(cType==CardType.c31||cType==CardType.c32||cType==CardType.c411||cType==CardType.c422
                ||cType==CardType.c11122234||cType==CardType.c1112223344){
            List<Card> a1=CardControl.getOrder2(c); //我出的牌
            List<Card> a2=CardControl.getOrder2(lastRound);//当前最大牌
            if(a1.get(0).getNum()<a2.get(0).getNum())
                return 0;
        }
        return 1;
    }
    //按照重复次数排序
    public static List getOrder2(List<Card> list){
        List<Card> list2=new ArrayList<Card>(list);
        List<Card> list3=new ArrayList<Card>();
        List<Integer> list4=new ArrayList<Integer>();
        int len=list2.size();
        int a[]=new int[20];
        for(int i=0;i<20;i++)
            a[i]=0;
        for(int i=0;i<len;i++)
        {
            a[list2.get(i).getNum()]++;
        }
        int max=0;
        for(int i=0;i<20;i++){
            max=0;
            for(int j=19;j>=0;j--){
                if(a[j]>a[max])
                    max=j;
            }

            for(int k=0;k<len;k++){
                if(list2.get(k).getNum()==max){
                    list3.add(list2.get(k));
                }
            }
            list2.remove(list3);
            a[max]=0;
        }
        return list3;
    }





    public static  CardType judgeType(List<Card> list) {
        Collections.sort(list);
        int len=list.size();
        //单牌,对子，3不带，4个一样炸弹
        if(len<=4)
        {	//如果第一个和最后个相同，说明全部相同
            if(list.size()>0&&list.get(0).getNum()==list.get(len-1).getNum())
            {
                switch (len) {
                    case 1:
                        return CardType.c1;
                    case 2:
                        return CardType.c2;
                    case 3:
                        return CardType.c3;
                    case 4:
                        return CardType.c4;
                }
            }
            //双王,化为炸弹返回
            if(len==2&&list.get(1).getColor().equals("5"))
                return CardType.c4;
            //当第一个和最后个不同时,3带1
            if((len==4) &&((list.get(0).getNum()==list.get(len-2).getNum())||
                    list.get(1).getNum()==list.get(len-1).getNum()))
                return CardType.c31;
            else {
                return CardType.c0;
            }
        }
        //当5张以上时，连字，3带2，飞机，2顺，4带2等等
        if(len>=5)
        {//现在按相同数字最大出现次数
            Card_index card_index=new Card_index();
            for(int i=0;i<4;i++)
                card_index.a[i]=new ArrayList<Integer>();
            //求出各种数字出现频率
            CardControl.getMax(card_index,list); //a[0,1,2,3]分别表示重复1,2,3,4次的牌
            //3带2 -----必含重复3次的牌
            if(card_index.a[2].size()==1 &&card_index.a[1].size()==1 && len==5)
                return CardType.c32;
            //4带2(单,双)
            if(card_index.a[3].size()==1 && len==6)
                return CardType.c411;
            if(card_index.a[3].size()==1 && card_index.a[1].size()==2 &&len==8)
                return CardType.c422;
            //单连,保证不存在王
            if((!list.get(0).getColor().equals("5"))&&(card_index.a[0].size()==len) &&
                    ((list.get(0).getNum()-list.get(len-1).getNum())==len-1))
                return CardType.c123;
            //连队
            if(card_index.a[1].size()==len/2 && len%2==0 && len/2>=3
                    &&((list.get(0).getNum()-list.get(len-1).getNum())==(len/2-1)))
                return CardType.c1122;
            //飞机
            if(card_index.a[2].size()==len/3 && (len%3==0) &&
                    ((list.get(0).getNum()-list.get(len-1).getNum())==(len/3-1)))
                return CardType.c111222;
            //飞机带n单,n/2对
            if(card_index.a[2].size()==len/4 && (len%4==0) &&
                    ((Integer)(card_index.a[2].get(len/4-1))-(Integer)(card_index.a[2].get(0))==len/4-1))
                return CardType.c11122234;

            //飞机带n双
            if(card_index.a[2].size()==len/5 && (len%5==0) && card_index.a[1].size()==len/5 &&
                    ((Integer)(card_index.a[2].get(len/5-1))-(Integer)(card_index.a[2].get(0))==len/5-1))
                return CardType.c1112223344;

        }
        return CardType.c0;
    }


}
class Card_index{
    List a[]=new ArrayList[4];//单张
}
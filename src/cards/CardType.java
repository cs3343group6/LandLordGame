package cards;
public enum CardType {
    c1,//single
    c2,//double
    c3,//triple with out a pair
    c4,//bomb
    c31,//three with one
    c32,//three with a pair
    c411,//four 1,1 or four with a pair
    c422,//four with 2 pairs
    c123,//连子
    c1122,//consistent pairs
    c111222,//飞机
    c11122234,//飞机带单排
    c1112223344,//飞机带对子
    c0//不能出牌
}
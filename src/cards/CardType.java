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
    c123,//consistent card
    c1122,//consistent pairs
    c111222,//plane
    c11122234,//plane with a single pair
    c1112223344,//plane with pairs
    c0//can't go out
}
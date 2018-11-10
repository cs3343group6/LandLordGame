package cards;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String roleName;//the name of the Player
    private int roleType;//0 means the landlord,1 means the normal player
    private List<Card> cardList;

    //construct function
    public Player(String playerName)
    {
        this.roleName = playerName;
    }
    public String getRoleName() {
        return roleType==Constant.LandLord?roleName+"(地主)":roleName;
    }

    public int getRoletType() {
        return roleType;
    }
    public void setRoletType(int roletType) {
        this.roleType = roletType;
    }
    public List<Card> getPokerList() {
        return cardList;
    }
    public void setPokerList(List<Card> cardList) {
        this.cardList = cardList;
    }


}
package it.polimi.tiw.tiw_project_ria.packets;

import it.polimi.tiw.tiw_project_ria.beans.BankAccount;
import it.polimi.tiw.tiw_project_ria.beans.Transfer;

import java.util.List;

public class PacketAccount {

    private final  BankAccount account;
    private final List<Transfer> transfers;

    public PacketAccount(BankAccount account, List<Transfer> transfers){
        this.account = account;
        this.transfers = transfers;
    }

    public BankAccount getAccount(){
        return this.account;
    }

    public List<Transfer> getTransfers(){
        return this.transfers;
    }
}

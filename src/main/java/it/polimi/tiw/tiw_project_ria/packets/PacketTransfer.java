package it.polimi.tiw.tiw_project_ria.packets;


import it.polimi.tiw.tiw_project_ria.beans.BankAccount;
import it.polimi.tiw.tiw_project_ria.beans.Transfer;

public class PacketTransfer {

    private final BankAccount sourceAccount;
    private final BankAccount destAccount;
    private final Transfer transfer;

    public PacketTransfer(BankAccount sourceAccount, BankAccount destAccount, Transfer transfer) {
        super();
        this.sourceAccount = sourceAccount;
        this.destAccount = destAccount;
        this.transfer = transfer;
    }

    public BankAccount getSourceAccount() {
        return sourceAccount;
    }


    public BankAccount getDestAccount() {
        return destAccount;
    }



    public Transfer getTransfer() {
        return transfer;
    }






}

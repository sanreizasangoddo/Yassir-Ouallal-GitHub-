void setup() {
  BankAccount mijnBankAccount1 = new BankAccount();
  BankAccount mijnBankAccount2 = new BankAccount();
  BankAccount mijnBankAccount3 = new BankAccount();
  mijnBankAccount1.rekeningnummer = "NL 43 ABNA 2097 1246 16";
  mijnBankAccount2.saldo = 140.00;
  mijnBankAccount3.eigenaar = "Yassir Ouallal";
  println("Bank account van: " + mijnBankAccount3.eigenaar);
  println("Rekeningnummer: " + mijnBankAccount1.rekeningnummer);
  println("Saldo: € " + mijnBankAccount2.saldo);
  float oudBedrag;
  float geldErbijofEraf = - 7.00;
  oudBedrag = mijnBankAccount2.saldo + geldErbijofEraf;
  if (oudBedrag > mijnBankAccount2.saldo) {
    println();
    println("Er is: € " + geldErbijofEraf + " op de rekening gestord.");
    println("Nieuw bedrag: € "+ oudBedrag);
  } else if (oudBedrag < mijnBankAccount2.saldo) {
    println();
    println("Er is: € " + geldErbijofEraf + " op de rekening gestord.");
    println("Nieuw bedrag: € "+ oudBedrag);
  }
}
class BankAccount {
  String eigenaar;
  String rekeningnummer;
  float saldo;
}
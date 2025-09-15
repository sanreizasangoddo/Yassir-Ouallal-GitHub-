int mijnGetal = 5;

void setup() {
  mijnMethode(mijnGetal, 9);
}

void draw() {
}

void mijnMethode(int getal, int getaltwee) {
  int totaal = getal + getaltwee;
  println("Som: " + getal + "+" + getaltwee + "=" + totaal);
}
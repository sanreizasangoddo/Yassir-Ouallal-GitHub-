String[] namen = {"Bob", "Mike", "Pieter", "Sandra", "Dion", "Kaan", "Lisa", "Jan", "Chelsea", "Ray"};
boolean gevonden;
int input = 2; 

void setup() {
  gevonden = false;

  if (input >= 1 && input <= namen.length) {
    gevonden = true;
    println(gevonden + ": " + namen[input - 1]);
  } else {
    println("false: Naam niet gevonden");
  }
}
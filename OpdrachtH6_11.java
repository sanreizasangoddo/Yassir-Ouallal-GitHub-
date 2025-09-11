int steen1 = 1;
int steen2 = 1;
int steen3 = 1;
boolean damage = false;
double damageValue = (steen1 + steen2 + steen3) / 3.0;
String resultaat = "";

if (steen1 == 1 && steen2 == 1 && steen3 == 1) {
  damage = false;
  resultaat = "0 DMG Critical MISS...";
} else if (steen1 == 6 && steen2 == 6 && steen3 == 6) {
  damage = true;
  resultaat = damageValue + " DMG Critical HIT!";
} else if (steen1 == 1 || steen2 == 1 || steen3 == 1) {
  damage = false;
  resultaat = "0 DMG Miss...";
} else {
  damage = true;
  resultaat = damageValue + (" DMG HIT!");
}
println(resultaat);
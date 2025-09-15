int cijfer = 3;

switch(cijfer) {
case 1:
case 2:
case 3:
  println(cijfer + " Slecht...");
  break;
case 4:
  println(cijfer + " Onvoldoende...");
  break;
case 5:
  println(cijfer + " Matig");
  break;
case 6:
case 7:
  println(cijfer + " Voldoende");
  break;
case 8:
case 9:
case 10:
  println(cijfer + " Goed!");
  break;
default:
  println("Waarschuwing: Verkeerd cijfer ingevoerd!");
}

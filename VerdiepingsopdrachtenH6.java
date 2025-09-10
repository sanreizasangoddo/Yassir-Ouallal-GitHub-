float cijfer1 = 5.5;
float cijfer2 = 5.5;
boolean diploma = false;
boolean vrijstelling = false;
boolean cumlaude = false;
if (cijfer1 >= 5.5 && cijfer2 >= 5.5) {
  diploma = true;
}
if (diploma) {
  println("Gefeliciteerd!");
} else if (cijfer1 <= 5.5 || cijfer2 <= 5.5) {
  vrijstelling = true;
}
if (vrijstelling) {
  println("Vrijstelling...");
} else if (cijfer1 >= 8.0 && cijfer2 >= 8.0) {
  cumlaude = true;
}
if (cumlaude) {
  println("Een zeer indrukwekkende prestatie!");
}
float score = 3000;
boolean Quest1 = false;

float enemies_defeated = 10;
boolean Quest2 = false;

float keys_found = 5;
boolean Quest3 = false;

if (score >= 3000) {
  Quest1 = true;
}

if (enemies_defeated == 10) {
  Quest2 = true;
}

if (keys_found >= 5) {
  Quest3 = true;
}

if (Quest1 == true && Quest2 == true && Quest3 == true) {
  println("Game Clear!");
}else if(Quest1 == false || Quest2 == false || Quest3 == false)
  println("Game Incomplete! Complete Quests first!");
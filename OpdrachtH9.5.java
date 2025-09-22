int eerste = 1;
int tweede = 2;
int derde = 3;
int vierde = 0;
while (vierde < 1000000000) {
  println(vierde);
  vierde = vierde + (vierde + derde + eerste) / tweede;
}
size(60,260);
background(255,255,255);
for(int i = 0; i <= 10; i++){
rect(20,i * 20 + 20,20,20);
}

for (int i = 0; i <= 10; i++) {
  print(i * 20 + 20);
  print(" | ");
  print(i*20);
  print(" | ");
  println(i);
}

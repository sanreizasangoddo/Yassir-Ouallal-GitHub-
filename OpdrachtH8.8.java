int nummer = 0, nummer2 = 1;
for (int i = 1; i <= 19; i++){
print(nummer + ",");
int volgende = nummer + nummer2;
nummer = nummer2;
nummer2 = volgende;
}
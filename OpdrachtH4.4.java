int a = abs(153);
int b = abs(-15);
float c = abs(12.234);
float d = abs(-9.23);

println(Math.abs(127)); // Dit wordt 127
println(Math.abs(-42)); // Dit wordt 42
println(Math.abs(17.236)); // Dit wordt 17.236
println(Math.abs(-3.17)); // Dit wordt 3.17

float e = pow( 4, 2);
float f = pow( 2, 4);
float g = pow( 7, -6);
float h = pow(-7, 6);

println(Math.pow( 4, 2)); // Zet 'e' naar 4*4 = 16
println(Math.pow( 2, 4)); // Zet 'f' naar 2*2*2*2 = 16
println(Math.pow( 7, -6)); // Zet 'g' naar 1 / 7*7*7*7*7*7 = 1 / 243 = 8.4998
println(Math.pow(-7, 6)); // Zet 'g' naar -7*-7*-7*-7*-7*-7 = 117649

float value = 26;
float n = norm(value, 0, 50);
println(n);  // Print "0.52"
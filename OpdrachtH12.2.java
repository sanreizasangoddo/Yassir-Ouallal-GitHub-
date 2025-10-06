void setup(){
  Person mijnPersoon1 = new Person();
  Person mijnPersoon2 = new Person();
  Person mijnPersoon3 = new Person();
  mijnPersoon1.name = "Yassir";
  mijnPersoon2.age = 16;
  mijnPersoon3.gender = "jongen";
  println("Hoi, ik ben "+ mijnPersoon1.name + ". Ik ben " + 
  mijnPersoon2.age + " jaar oud en ik ben een " + mijnPersoon3.gender + ".");
}

class Person {
  String name;
  int age;
  String gender;
}
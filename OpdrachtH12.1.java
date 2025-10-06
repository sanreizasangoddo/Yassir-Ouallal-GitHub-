class Rectangle {
  float x1;
  float y1;
  float x2;
  float y2;

  Rectangle(float x, float y, float breedte, float hoogte) {
    this.x1 = x;
    this.y1 = y;
    this.x2 = breedte;
    this.y2 = hoogte;
  }

  void teken() {
    rect(x1, y1, x2, y2);
  }
}

void setup() {
  size(400, 400);
  Rectangle mijnRechthoek = new Rectangle(200, 100, 50, 20);
  mijnRechthoek.teken();
}
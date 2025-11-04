// Pong 2P [CHAOS MODE] DEMO

int goal1 = 0;
int goal2 = 0;

// Grenzen
float groundY = 498;
float ceilingY = -80;

// Tijd dat alles stopt na een goal (~2 seconden)
int pauseFrames = 120;

// Hoeveel punten nodig zijn om te winnen
int winScore = 20;
boolean gameOver = false;

// Twee spelers en een lijst met ballen
Speler[] spelers = new Speler[2];
Bal[] ballen = null;

// Laat zien of de besturing onderaan zichtbaar is
boolean showControls = true;

// Verschillende schermen van het spel
final int PLAYING = 1;
final int GAMEOVER = 2;
int gameState = PLAYING;

void setup() {
  size(800, 500);
  rectMode(CORNER);
  textAlign(CENTER, CENTER);

  // Maak spelers
  spelers[0] = new Speler(50, height/2 - 40, 20, 80);         // Links, W/S
  spelers[1] = new Speler(width - 70, height/2 - 40, 20, 80); // Rechts, pijltjes

  // Vaste standaard score
  winScore = 20;
  initBalls();
}

void draw() {
  background(0);

  // Stippellijn
  fill(197);
  for (int lineY = 5; lineY <= 480; lineY += 35) {
    rect(width/2 - 3, lineY, 6, 30);
  }

  // Spelers updaten en tekenen
  fill(255);
  for (int i = 0; i < spelers.length; i++) {
    spelers[i].update();
    spelers[i].display();
  }

  // Ballen updaten en tekenen
  if (ballen != null) {
    for (int i = 0; i < ballen.length; i++) {
      ballen[i].update(spelers);
      ballen[i].display();
    }
  }

  // Scores bovenaan
  textSize(100);
  fill(255);
  text(goal1, 200, 70);
  text(goal2, 600, 70);

  // Toon besturing onderaan
  drawControlsBottomCenter();

  // Als het spel voorbij is
  if (gameState == GAMEOVER) {
    noStroke();
    fill(0, 180);
    rect(0, 0, width, height);

    fill(255);
    textAlign(CENTER, CENTER);
    textSize(48);
    String winner = (goal1 >= winScore) ? "P1 wint!" : "P2 wint!";
    text(winner, width/2, height/2 - 20);

    textSize(20);
    text("Druk op 'R' om opnieuw te starten", width/2, height/2 + 20);
  }
}

void initBalls() {
  int count = max(1, winScore / 5);
  ballen = new Bal[count];
  for (int i = 0; i < count; i++) {
    ballen[i] = new Bal(width/2 - 6, height/2 - 6, 12, 12);
  }
  // Ballen afwisselend naar links en rechts serveren
  for (int i = 0; i < ballen.length; i++) {
    int dir = (i % 2 == 0) ? -1 : 1;
    ballen[i].reset(dir);
  }
}

// Kijkt of alle ballen stilstaan
boolean allBallsPaused() {
  if (ballen == null || ballen.length == 0) return true;
  for (int i = 0; i < ballen.length; i++) {
    if (!ballen[i].paused) return false;
  }
  return true;
}

// Zet alle ballen stil bij game-over
void pauseAllBalls() {
  if (ballen == null) return;
  for (int i = 0; i < ballen.length; i++) {
    ballen[i].paused = true;
    ballen[i].pauseCounter = 0;
    ballen[i].vx = 0;
    ballen[i].vy = 0;
  }
}

// Reset alles naar het begin
void resetGame() {
  goal1 = 0;
  goal2 = 0;
  gameOver = false;

  // Zet spelers terug in het midden
  float paddleStartY = height/2 - 40;
  spelers[0].y = paddleStartY;
  spelers[1].y = paddleStartY;

  spelers[0].upPressed = false;
  spelers[0].downPressed = false;
  spelers[1].upPressed = false;
  spelers[1].downPressed = false;

  // Maak ballen opnieuw aan als ze niet bestaan
  if (ballen == null) {
    initBalls();
  }

  // Reset elke bal
  for (int i = 0; i < ballen.length; i++) {
    int dir = (i % 2 == 0) ? -1 : 1;
    ballen[i].reset(dir);
  }
}

// Tekent de besturing onderaan het scherm
void drawControlsBottomCenter() {
  textSize(16);
  textAlign(CENTER, CENTER);

  String leftCtrl = "Links: W / S";
  String rightCtrl = "Rechts: ↑ / ↓";
  String middle = leftCtrl + "      " + rightCtrl;

  float paddingX = 12;
  float paddingY = 8;
  float txtW = textWidth(middle);
  float boxW = txtW + paddingX * 2;
  float boxH = 16 + paddingY * 2;

  float boxX = (width - boxW) / 2;
  float boxY = height - boxH - 10;

  if (showControls) {
    noStroke();
    fill(0, 160);
    rect(boxX, boxY, boxW, boxH, 6);

    fill(255);
    text(middle, width/2, boxY + boxH/2);
  }

  textSize(12);
  fill(200);
  float hintY = boxY - 12;
  if (hintY < 12) hintY = boxY - 6;
  text("Druk op 'C' om controls te verbergen of tonen", width/2, hintY);
}

class Speler {
  float x, y, w, h;
  boolean upPressed = false;
  boolean downPressed = false;
  float speed = 5;

  Speler(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  void update() {
    // Spelers bewegen niet tijdens pauze of na game-over
    if (gameState == GAMEOVER || allBallsPaused()) {
      speed = 0;
    } else {
      speed = 5;
    }

    if (upPressed) y -= speed;
    if (downPressed) y += speed;

    // Grenzen
    if (y < 0 + ceilingY + h) y = ceilingY + h;
    if (y + h > groundY) y = groundY - h;
  }

  void display() {
    stroke(0);
    rect(x, y, w, h);
  }
}

class Bal {
  float x, y, w, h;
  float vx = 3.5;
  float vy = 4;
  boolean paused = false;
  int pauseCounter = 0;
  int nextServeDir = 1;

  Bal(float bx, float by, float bw, float bh) {
    this.x = bx;
    this.y = by;
    this.w = bw;
    this.h = bh;
  }

  // Zet bal terug naar het midden
  void reset(int direction) {
    x = width/2 - w/2;
    y = height/2 - h/2;
    vx = 0;
    vy = 0;
    paused = true;
    pauseCounter = pauseFrames;
    nextServeDir = direction;

    // Als nu ALLE ballen stilstaan, zet hun timer gelijk
    boolean allPausedNow = true;
    for (int i = 0; i < ballen.length; i++) {
      if (!ballen[i].paused) {
        allPausedNow = false;
        break;
      }
    }
    if (allPausedNow) {
      for (int i = 0; i < ballen.length; i++) {
        ballen[i].paused = true;
        ballen[i].pauseCounter = pauseFrames;
      }
    }
  }

  void update(Speler[] spelers) {
    if (gameState == GAMEOVER) return;

    // Wacht voor de volgende serve
    if (paused) {
      pauseCounter--;
      if (pauseCounter <= 0) {
        paused = false;
        vx = 3.5 * nextServeDir;
        vy = random(-3, 3);
      } else {
        return;
      }
    }

    float nextX = x + vx;
    float nextY = y + vy;

    // Botsing met spelers
    for (int i = 0; i < spelers.length; i++) {
      Speler p = spelers[i];
      if (nextX + w > p.x && nextX < p.x + p.w && y + h > p.y && y < p.y + p.h) {
        if (vx > 0) x = p.x - w - 0.1;
        else x = p.x + p.w + 0.1;
        vx *= -1.05;
        float hitPos = (y + h/2) - (p.y + p.h/2);
        vy += hitPos * 0.02;
        return;
      }
    }

    // Botsing met boven- of onderkant
    if (nextY < 0 || nextY + h > height) vy *= -1;

    // Check of iemand scoort
    if (nextX + w > width + 12) {
      goal1++;
      if (goal1 >= winScore) {
        gameOver = true;
        gameState = GAMEOVER;
        pauseAllBalls();
      } else reset(-1); // Bal naar links
      return;
    } else if (nextX < -12) {
      goal2++;
      if (goal2 >= winScore) {
        gameOver = true;
        gameState = GAMEOVER;
        pauseAllBalls();
      } else reset(1); // Bal naar rechts
      return;
    }

    // Update positie
    x += vx;
    y += vy;
  }

  void display() {
    stroke(0);
    rect(x, y, w, h);
  }
}

void keyPressed() {
  // Besturing tijdens het spel
  if (key == 'w' || key == 'W') spelers[0].upPressed = true;
  if (key == 's' || key == 'S') spelers[0].downPressed = true;
  if (keyCode == UP) spelers[1].upPressed = true;
  if (keyCode == DOWN) spelers[1].downPressed = true;

  // Opnieuw starten met R na game-over
  if ((key == 'r' || key == 'R') && gameState == GAMEOVER) {
    initBalls();
    resetGame();
    gameState = PLAYING;
  }
  // Controls aan/uit
  if (key == 'c' || key == 'C') showControls = !showControls;
}

void keyReleased() {
  if (key == 'w' || key == 'W') spelers[0].upPressed = false;
  if (key == 's' || key == 'S') spelers[0].downPressed = false;
  if (keyCode == UP) spelers[1].upPressed = false;
  if (keyCode == DOWN) spelers[1].downPressed = false;
}
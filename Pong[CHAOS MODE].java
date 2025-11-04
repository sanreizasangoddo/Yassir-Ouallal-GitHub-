// Pong 2P [CHAOS MODE]

int goal1 = 0;
int goal2 = 0;

// Speelveldgrenzen (y-coördinaten)
float groundY = 498;
float ceilingY = -80;

int pauseFrames = 120;

// Punten om te winnen (wordt ingesteld via menu)
int winScore = 20;
boolean gameOver = false;

Speler[] spelers = new Speler[2];
Bal[] ballen = null;

// Toon of verberg de controls-hint onderin
boolean showControls = true;

// Schermstaten
final int MENU = 0;
final int PLAYING = 1;
final int GAMEOVER = 2;
final int PAUSED = 3;
int gameState = MENU;

int[] menuOptions = {5, 10, 20, 50, 100};
int selectedOptionIndex = 2; // Standaard: index van 20 punten

// Exact aantal ballen per optie
int[] ballsPerOption = {1, 2, 4, 10, 20};

// Forceer stroke (true) of niet (false)
boolean[] forceStrokePerOption = {true, true, true, true, false};

// Als true wordt er eenvoudiger getekend
boolean performanceMode = false;

final float BASE_BALL_SPEED = 3.5;
final float BASE_PADDLE_SPEED = 5.0;

// Gegevens voor menu-muis interactie
float[] optX, optY, optW, optH;
int hoverIndex = -1;

// Rect-positie en -grootte van de START-knop in het menu
float startX, startY, startW = 120, startH = 36;

void setup() {
  size(800, 500);
  rectMode(CORNER);
  textAlign(CENTER, CENTER);

  spelers[0] = new Speler(50, height/2 - 40, 20, 80);
  spelers[1] = new Speler(width - 70, height/2 - 40, 20, 80);

  // Stel standaard win-score en maak previewballen
  winScore = menuOptions[selectedOptionIndex];
  initBallsForWinScore(winScore);

  // Menu-optie rects
  optX = new float[menuOptions.length];
  optY = new float[menuOptions.length];
  optW = new float[menuOptions.length];
  optH = new float[menuOptions.length];
}

void draw() {
  background(0);

  if (gameState == MENU) {
    drawMenu();
    return;
  }

  // Midden-stippellijn
  fill(197);
  for (int lineY = 5; lineY <= 480; lineY += 35) {
    rect(width/2 - 3, lineY, 6, 30);
  }

  if (gameState == PLAYING) {
    if (!allBallsPaused()) {
      for (int i = 0; i < spelers.length; i++) {
        spelers[i].update();
      }
    }
    // Ball-update (pauseCounters lopen altijd door)
    if (ballen != null) {
      for (int i = 0; i < ballen.length; i++) {
        ballen[i].update(spelers);
      }
    }
  }

  // Teken spelers en ballen
  fill(255);
  for (int i = 0; i < spelers.length; i++) {
    spelers[i].display();
  }
  if (ballen != null) {
    for (int i = 0; i < ballen.length; i++) {
      ballen[i].display();
    }
  }

  // Scoreboard
  textSize(100);
  fill(255);
  text(goal1, 200, 70);
  text(goal2, 600, 70);

  // Toon besturing onderaan
  drawControlsBottomCenter();

  // Game-over overlay
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
    text("Druk op 'M' om terug te gaan naar het menu", width/2, height/2 + 50);
  }

  // Pauzemenu overlay
  if (gameState == PAUSED) {
    drawPauseMenu();
  }
}

// Maak ballen passend bij gekozen winScore
void initBallsForWinScore(int score) {
  int idx = -1;
  for (int i = 0; i < menuOptions.length; i++) {
    if (menuOptions[i] == score) {
      idx = i;
      break;
    }
  }

  int count;
  if (idx >= 0 && idx < ballsPerOption.length) {
    count = ballsPerOption[idx];
  } else {
    count = max(1, score / 5);
  }

  ballen = new Bal[count];

  performanceMode = (count >= 8);
  if (idx >= 0 && idx < forceStrokePerOption.length && forceStrokePerOption[idx]) {
    performanceMode = false;
  }

  for (int i = 0; i < count; i++) ballen[i] = new Bal(width/2 - 6, height/2 - 6, 12, 12);
  for (int i = 0; i < ballen.length; i++) {
    int dir = (i % 2 == 0) ? -1 : 1;
    ballen[i].reset(dir);
  }

  // Verhoog paddle-snelheid iets bij veel ballen
  float extraPaddleSpeed = (count > 1) ? min(8, (count - 1) * 0.7) : 0;
  for (int p = 0; p < spelers.length; p++) spelers[p].speedBase = BASE_PADDLE_SPEED + extraPaddleSpeed;
}

// Return true als alle ballen gepauzeerd zijn
boolean allBallsPaused() {
  if (ballen == null || ballen.length == 0) return true;
  for (int i = 0; i < ballen.length; i++) {
    if (!ballen[i].paused) return false;
  }
  return true;
}

// Pauzeer alle ballen
void pauseAllBalls() {
  if (ballen == null) return;
  for (int i = 0; i < ballen.length; i++) {
    ballen[i].paused = true;
    ballen[i].pauseCounter = 0;
    ballen[i].vx = 0;
    ballen[i].vy = 0;
  }
}

// Reset score, paddles en ballen
void resetGame() {
  goal1 = 0;
  goal2 = 0;
  gameOver = false;

  float paddleStartY = height/2 - 40;
  spelers[0].y = paddleStartY;
  spelers[1].y = paddleStartY;

  spelers[0].upPressed = false;
  spelers[0].downPressed = false;
  spelers[1].upPressed = false;
  spelers[1].downPressed = false;

  initBallsForWinScore(winScore);

  for (int i = 0; i < ballen.length; i++) {
    int dir = (i % 2 == 0) ? -1 : 1;
    ballen[i].reset(dir);
  }
}

// Teken de controls-hint onderaan
void drawControlsBottomCenter() {
  textSize(16);
  textAlign(CENTER, CENTER);

  String leftCtrl = "Links: W / S";
  String rightCtrl = "Rechts: ↑ / ↓";
  String middle = leftCtrl + "     " + rightCtrl;

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
  text("Druk 'C' = controls  •  'P' = pauze  •  'R' = herstart  •  'M' = menu", width/2, hintY);
}

// Pauze-overlay met knoppen
void drawPauseMenu() {
  noStroke();
  fill(0, 180);
  rect(0, 0, width, height);

  fill(255);
  textAlign(CENTER, CENTER);
  textSize(48);
  text("PAUZE", width/2, height/2 - 90);

  float bw = 180, bh = 40;
  float bx = width/2 - bw/2;
  float by = height/2 - 20;

  // Resume
  if (mouseX >= bx && mouseX <= bx + bw && mouseY >= by && mouseY <= by + bh) fill(200);
  else fill(240);
  rect(bx, by, bw, bh, 6);
  fill(0);
  textSize(16);
  text("Resume (P)", bx + bw/2, by + bh/2);

  // Restart
  float by2 = by + bh + 12;
  if (mouseX >= bx && mouseX <= bx + bw && mouseY >= by2 && mouseY <= by2 + bh) fill(200);
  else fill(240);
  rect(bx, by2, bw, bh, 6);
  fill(0);
  text("Restart (R)", bx + bw/2, by2 + bh/2);

  // Terug naar menu
  float by3 = by2 + bh + 12;
  if (mouseX >= bx && mouseX <= bx + bw && mouseY >= by3 && mouseY <= by3 + bh) fill(200);
  else fill(240);
  rect(bx, by3, bw, bh, 6);
  fill(0);
  text("Terug naar menu (M)", bx + bw/2, by3 + bh/2);

  textSize(12);
  fill(220);
  text("Druk P om te togglen", width/2, by3 + bh + 30);
}

class Speler {
  float x, y, w, h;
  boolean upPressed = false;
  boolean downPressed = false;
  float speedBase = BASE_PADDLE_SPEED;

  Speler(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  void update() {
    float speed = speedBase;
    if (upPressed) y -= speed;
    if (downPressed) y += speed;
    if (y < 0 + ceilingY + h) y = ceilingY + h;
    if (y + h > groundY) y = groundY - h;
  }

  void display() {
    if (!performanceMode) stroke(0);
    else noStroke();
    rect(x, y, w, h);
  }
}

class Bal {
  float x, y, w, h;
  float vx = BASE_BALL_SPEED;
  float vy = 4;
  boolean paused = false;
  int pauseCounter = 0;
  int nextServeDir = 1;

  Bal(float bx, float by, float bw, float bh) {
    x = bx;
    y = by;
    w = bw;
    h = bh;
  }

  void reset(int direction) {
    x = width/2 - w/2;
    y = height/2 - h/2;
    vx = 0;
    vy = 0;
    paused = true;
    pauseCounter = pauseFrames;
    nextServeDir = direction;
  }

  void update(Speler[] spelers) {
    if (gameState == GAMEOVER) return;
    if (paused) {
      pauseCounter--;
      if (pauseCounter <= 0) {
        paused = false;
        int bc = (ballen != null) ? ballen.length : 1;
        float speedMultiplier = constrain(1.0 - (bc - 1) * 0.03, 0.25, 1.0);
        vx = BASE_BALL_SPEED * nextServeDir * speedMultiplier;
        vy = random(-3, 3) * speedMultiplier;
      } else return;
    }

    float nextX = x + vx;
    float nextY = y + vy;

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

    if (nextY < 0 || nextY + h > height) vy *= -1;

    if (nextX + w > width + 12) {
      goal1++;
      if (goal1 >= winScore) {
        gameOver = true;
        gameState = GAMEOVER;
        pauseAllBalls();
      } else reset(-1);
      return;
    } else if (nextX < -12) {
      goal2++;
      if (goal2 >= winScore) {
        gameOver = true;
        gameState = GAMEOVER;
        pauseAllBalls();
      } else reset(1);
      return;
    }

    x += vx;
    y += vy;
  }

  void display() {
    if (!performanceMode) {
      stroke(0);
      rect(x, y, w, h);
    } else {
      noStroke();
      rect(x, y, w-2, h-2);
    }
  }
}

void keyPressed() {
  if (key == 'p' || key == 'P') {
    if (gameState == PLAYING) {
      gameState = PAUSED;
      return;
    } else if (gameState == PAUSED) {
      gameState = PLAYING;
      return;
    }
  }

  if (gameState == PAUSED) {
    if (key == 'r' || key == 'R') {
      initBallsForWinScore(winScore);
      resetGame();
      gameState = PLAYING;
      return;
    }
    if (key == 'm' || key == 'M') {
      gameState = MENU;
      return;
    }
  }

  if (gameState == MENU) {
    if (key == 'a' || key == 'A' || keyCode == LEFT) {
      selectedOptionIndex = max(0, selectedOptionIndex - 1);
      winScore = menuOptions[selectedOptionIndex];
      initBallsForWinScore(winScore);
    } else if (key == 'd' || key == 'D' || keyCode == RIGHT) {
      selectedOptionIndex = min(menuOptions.length - 1, selectedOptionIndex + 1);
      winScore = menuOptions[selectedOptionIndex];
      initBallsForWinScore(winScore);
    } else if (key >= '1' && key <= '0' + menuOptions.length) {
      int idx = key - '1';
      if (idx >= 0 && idx < menuOptions.length) {
        selectedOptionIndex = idx;
        winScore = menuOptions[selectedOptionIndex];
        initBallsForWinScore(winScore);
      }
    } else if (key == ENTER || key == RETURN || key == ' ') {
      winScore = menuOptions[selectedOptionIndex];
      initBallsForWinScore(winScore);
      resetGame();
      gameState = PLAYING;
    } else if (key == 'c' || key == 'C') showControls = !showControls;
    else if (key == 'v' || key == 'V') {
      forceStrokePerOption[selectedOptionIndex] = !forceStrokePerOption[selectedOptionIndex];
      initBallsForWinScore(winScore);
    }
    return;
  }

  // Spelbesturing
  if (key == 'w' || key == 'W') spelers[0].upPressed = true;
  if (key == 's' || key == 'S') spelers[0].downPressed = true;
  if (keyCode == UP) spelers[1].upPressed = true;
  if (keyCode == DOWN) spelers[1].downPressed = true;

  if ((key == 'r' || key == 'R') && gameState == GAMEOVER) {
    initBallsForWinScore(winScore);
    resetGame();
    gameState = PLAYING;
  }
  if ((key == 'm' || key == 'M') && gameState == GAMEOVER) {
    gameState = MENU;
    for (int i = 0; i < menuOptions.length; i++) if (menuOptions[i] == winScore) {
      selectedOptionIndex = i;
      break;
    }
    initBallsForWinScore(winScore);
  }

  if (key == 'c' || key == 'C') showControls = !showControls;
}

void keyReleased() {
  if (key == 'w' || key == 'W') spelers[0].upPressed = false;
  if (key == 's' || key == 'S') spelers[0].downPressed = false;
  if (keyCode == UP) spelers[1].upPressed = false;
  if (keyCode == DOWN) spelers[1].downPressed = false;
}

// Muisklik handling voor PAUSED en MENU
void mousePressed() {
  if (gameState == PAUSED) {
    float bw = 180, bh = 40;
    float bx = width/2 - bw/2;
    float by = height/2 - 20;
    float by2 = by + bh + 12;
    float by3 = by2 + bh + 12;

    // Resume
    if (mouseX >= bx && mouseX <= bx + bw && mouseY >= by && mouseY <= by + bh) {
      gameState = PLAYING;
      return;
    }
    // Restart
    if (mouseX >= bx && mouseX <= bx + bw && mouseY >= by2 && mouseY <= by2 + bh) {
      initBallsForWinScore(winScore);
      resetGame();
      gameState = PLAYING;
      return;
    }
    // Menu
    if (mouseX >= bx && mouseX <= bx + bw && mouseY >= by3 && mouseY <= by3 + bh) {
      gameState = MENU;
      return;
    }
  }

  if (gameState == MENU) {
    for (int i = 0; i < menuOptions.length; i++) {
      if (mouseX >= optX[i] && mouseX <= optX[i] + optW[i]
        && mouseY >= optY[i] && mouseY <= optY[i] + optH[i]) {
        if (selectedOptionIndex == i) {
          winScore = menuOptions[selectedOptionIndex];
          initBallsForWinScore(winScore);
          resetGame();
          gameState = PLAYING;
          return;
        } else {
          selectedOptionIndex = i;
          winScore = menuOptions[selectedOptionIndex];
          initBallsForWinScore(winScore);
          return;
        }
      }
    }
    // Start-knop
    if (mouseX >= startX && mouseX <= startX + startW
      && mouseY >= startY && mouseY <= startY + startH) {
      winScore = menuOptions[selectedOptionIndex];
      initBallsForWinScore(winScore);
      resetGame();
      gameState = PLAYING;
      return;
    }
  }
}

void drawMenu() {
  // Teken het keuze-menu
  background(10, 20, 30);
  fill(255);
  textAlign(CENTER, CENTER);

  textSize(48);
  text(" - PONG - ", width/2, 80);

  textSize(14);
  text("Gebruik ← → of A/D of klik om te kiezen. Klik Start of klik twee keer een optie om te beginnen.", width/2, 120);
  text("Je kunt ook 1 t/m "+menuOptions.length+" drukken voor snelle keuze. Druk V om stroke te togglen.", width/2, 140);

  float startXLocal = width/2 - (menuOptions.length * 80) / 2;
  float y = height/2 - 40;

  hoverIndex = -1;
  for (int i = 0; i < menuOptions.length; i++) {
    float x = startXLocal + i * 80;
    float w = 70;
    float h = 70;
    optX[i] = x - w/2;
    optY[i] = y - h/2;
    optW[i] = w;
    optH[i] = h;

    if (mouseX >= optX[i] && mouseX <= optX[i] + w && mouseY >= optY[i] && mouseY <= optY[i] + h) {
      hoverIndex = i;
    }

    if (i == selectedOptionIndex) {
      fill(255, 200, 0);
      rectMode(CENTER);
      rect(x, y, w, h, 8);
      rectMode(CORNER);
      fill(0);
      textSize(18);
      text(menuOptions[i], x, y);
    } else if (hoverIndex == i) {
      fill(200, 240, 255);
      rectMode(CENTER);
      rect(x, y, w, h, 8);
      rectMode(CORNER);
      fill(0);
      textSize(16);
      text(menuOptions[i], x, y);
    } else {
      fill(240);
      textSize(16);
      text(menuOptions[i], x, y);
    }
    fill(150);
    textSize(12);
    text("(" + (i+1) + ")", x, y + 42);
  }

  // Start knop
  startX = width/2 - startW/2;
  startY = height/2 + 70;
  if (mouseX >= startX && mouseX <= startX + startW && mouseY >= startY && mouseY <= startY + startH) {
    fill(180, 255, 200);
  } else {
    fill(120, 200, 140);
  }
  rect(startX, startY, startW, startH, 6);
  fill(0);
  textSize(16);
  text("START", startX + startW/2, startY + startH/2);

  // Preview info
  textSize(14);
  fill(220);
  String preview = "Aantal ballen: " + (ballen != null ? ballen.length : 0);
  if (performanceMode) preview += "  (Performance-mode)";
  if (forceStrokePerOption[selectedOptionIndex]) preview += "  (Stroke ON)";
  else preview += "  (Stroke OFF)";
  text(preview, width/2, startY + startH + 24);

  textSize(12);
  fill(180);
  text("Druk op 'V' om stroke voor de bal te togglen", width/2, height - 30);
}
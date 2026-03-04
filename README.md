<div align="center">

<!-- HEADER -->
<img src="https://capsule-render.vercel.app/api?type=cylinder&color=0:0d1b2a,100:0f9e8c&height=160&text=Sudoku%20Solver%20Game&fontSize=38&fontColor=ffffff&fontAlignY=55&desc=Recursive%20Backtracking%20%7C%20OOP%20Architecture%20%7C%20Performance%20Dashboard&descAlignY=75&descSize=13" width="100%"/>

<br/>

<!-- BADGES -->
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Architecture-38BDF8?style=for-the-badge&labelColor=0f172a)
![Algorithm](https://img.shields.io/badge/Recursive-Backtracking-a855f7?style=for-the-badge&labelColor=0f172a)
![Difficulty](https://img.shields.io/badge/Difficulty-3%20Levels-22c55e?style=for-the-badge&labelColor=0f172a)
![Dashboard](https://img.shields.io/badge/Performance-Dashboard-f59e0b?style=for-the-badge&labelColor=0f172a)

<br/>

> A fully-featured **Java-based Sudoku game** with automatic puzzle generation, recursive backtracking solver,
> performance analytics dashboard, achievement system, game history comparison, and much more —
> built with clean Object-Oriented Architecture.

<br/>

[✨ Features](#-features) • [🧠 How It Works](#-how-the-solver-works) • [📸 Screenshots](#-screenshots) • [🚀 Getting Started](#-getting-started) • [🏗️ Architecture](#️-project-architecture)

</div>

---

## ✨ Features

### 🎮 Core Game

| Feature | Description |
|---------|-------------|
| 🎯 **Interactive GUI Board** | Playable 9×9 Sudoku with real-time feedback |
| 🔢 **3 Difficulty Levels** | Easy (25 removals), Medium (45), Hard (57) |
| 🤖 **Auto Solver** | Recursive backtracking solves any puzzle instantly |
| 💡 **Hint System** | Reveals correct value for any selected cell |
| ❌ **Mistake Detection** | Highlights incorrect entries in red |
| ⏱️ **Timer** | Tracks time taken to solve each puzzle |
| 🔄 **Reset Board** | Clears user input while preserving original puzzle |

### 📊 Performance & Analytics

| Feature | Description |
|---------|-------------|
| 📈 **Performance Dashboard** | Completion time, accuracy rate, efficiency score |
| 🎨 **Performance Visualization** | Circular score chart with detailed breakdown |
| 📊 **Interactive Charts** | Hints, mistakes, time & accuracy bar charts |
| 🔬 **Advanced Statistics** | Mean/median accuracy, standard deviation, trend slope |
| 🔁 **Game History Comparison** | Compare current vs previous games side by side |

### 🏆 Achievements & Progress

| Feature | Description |
|---------|-------------|
| 🏅 **Achievement System** | Diamond, Gold, Platinum, Bronze tiers |
| ⭐ **Perfect Game** · ⚡ **Speed Demon** · 🎯 **Accuracy Master** · 🧠 **Hintless Hero** | Unlock by playing |
| 🏋️ **Practice Mode** | Targeted practice based on your performance stats |
| 📄 **Generate Report** | Save detailed performance report as PDF/TXT |
| 🔗 **Share Results** | Share your score summary |

---

## 🏗️ Project Architecture

```
SudokuGame/
├── SudokuBoard.java      # Core board state management
├── SudokuSolver.java     # Recursive backtracking solver & hint engine
├── PuzzleGenerator.java  # Random puzzle generation with difficulty control
├── SudokuGUI.java        # Main game interface & user interaction
└── finalWindow.java      # Performance dashboard & result display
```

---

## 🧠 How the Solver Works

The solver uses **recursive backtracking** — a classic algorithm that tries every possible number and undoes bad choices automatically.

```
1. Find next empty cell
2. Try numbers 1–9
3. Check validity (row, column, 3×3 box)
4. If valid → place number → recurse
5. If stuck → backtrack → try next number
6. Repeat until board is complete
```

<div align="center">

| Property | Detail |
|----------|--------|
| **Algorithm** | Recursive Backtracking |
| **Time Complexity** | O(9^m) worst case (m = empty cells) |
| **Space Complexity** | O(m) recursion stack |
| **Practical Speed** | Solves any valid puzzle in milliseconds |

</div>

---

## 📸 Screenshots

### 🎮 Game Board

<div align="center">

| Empty Board | Easy Puzzle |
|:---:|:---:|
| <img src="screenshots/01_game_board.png" width="420"> | <img src="screenshots/02_easy_board.png" width="420"> |

| Medium Puzzle | Hard Puzzle |
|:---:|:---:|
| <img src="screenshots/03_medium_board.png" width="420"> | <img src="screenshots/04_hard_board.png" width="420"> |

</div>

### 💡 Gameplay Features

<div align="center">

| Hint | Mistake |
|:---:|:---:|
| <img src="screenshots/05_hint.png" width="220"> | <img src="screenshots/06_mistake.png" width="220"> |

| Auto Solved |
|:---:|
| <img src="screenshots/07_solve_automatically.png" width="420"> |

</div>

### 📊 Performance Dashboard

<div align="center">

| KPI Overview | Performance Visualization | Interactive Charts |
|:---:|:---:|:---:|
| <img src="screenshots/08_performance_dashboard.png" width="400"> | <img src="screenshots/09_performance_visualization.png" width="400"> | <img src="screenshots/15_compare_games_charts.png" width="400"> |

</div>

### 🏆 Game History & Stats

<div align="center">

| Overview | Detailed Comparison | Advanced Stats |
|:---:|:---:|:---:|
| <img src="screenshots/12_compare_games_overview.png" width="400"> | <img src="screenshots/13_compare_games_detailed.png" width="400"> | <img src="screenshots/14_compare_games_stats.png" width="400"> |

</div>

### 🎖️ Extra Features

<div align="center">

| Achievements | Practice Mode | Share Results | Generate Report |
|:---:|:---:|:---:|:---:|
| <img src="screenshots/16_achievements.png" width="260"> | <img src="screenshots/17_practice_mode.png" width="260"> | <img src="screenshots/18_share_results.png" width="260"> | <img src="screenshots/11_generate_report.png" width="260"> |

</div>

---

## 🚀 Getting Started

### Prerequisites
- Java JDK 8 or higher

### Run the Game

```bash
# Clone the repository
git clone https://github.com/huda-usman/sudoku-solver-game.git

# Navigate to project
cd sudoku-solver-game

# Compile all Java files
javac *.java

# Run the game
java SudokuGUI
```

---

## 📁 Repository Structure

```
sudoku-solver-game/
│
├── SudokuBoard.java        # Core board state management
├── SudokuSolver.java       # Recursive backtracking solver & hint engine
├── PuzzleGenerator.java    # Random puzzle generation with difficulty control
├── SudokuGUI.java          # Main game interface & user interaction
├── finalWindow.java        # Performance dashboard & result display
│
├── 📂 screenshots/         # All UI screenshots
└── README.md
```

---

## 📄 License

This project is open-source under the [MIT License](LICENSE).

---

<div align="center">

<img src="https://capsule-render.vercel.app/api?type=cylinder&color=0:0f9e8c,100:0d1b2a&height=100&section=footer" width="100%"/>

### 🙋‍♀️ Connect with Me

Developed by **Huda Usman**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Huda%20Usman-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/hudausman010)

<br/>

⭐ **If you found this project interesting, please give it a star!** ⭐

</div>

# 🧩 Sudoku Solver Game

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Architecture-38BDF8?style=for-the-badge&labelColor=0f172a)
![Algorithm](https://img.shields.io/badge/Recursive-Backtracking-a855f7?style=for-the-badge&labelColor=0f172a)
![Difficulty](https://img.shields.io/badge/Difficulty-3%20Levels-22c55e?style=for-the-badge&labelColor=0f172a)
![Dashboard](https://img.shields.io/badge/Performance-Dashboard-f59e0b?style=for-the-badge&labelColor=0f172a)

A fully-featured **Java-based Sudoku game** with automatic puzzle generation, recursive backtracking solver, performance analytics dashboard, achievement system, game history comparison, and much more — built with clean Object-Oriented Architecture.

</div>

---

## ✨ Features

### 🎮 Core Game
- **Interactive GUI Board** — playable 9×9 Sudoku with real-time feedback
- **3 Difficulty Levels** — Easy (25 removals), Medium (45), Hard (57)
- **Auto Solver** — recursive backtracking solves any puzzle instantly
- **Hint System** — reveals correct value for any selected cell
- **Mistake Detection** — highlights incorrect entries in red
- **Timer** — tracks time taken to solve each puzzle
- **Reset Board** — clears user input while preserving original puzzle

### 📊 Performance & Analytics
- **Performance Dashboard** — completion time, accuracy rate, efficiency score
- **Performance Visualization** — circular score chart with detailed breakdown
- **Interactive Charts** — hints, mistakes, time & accuracy bar charts
- **Advanced Statistics** — mean/median accuracy, standard deviation, trend slope
- **Game History Comparison** — compare current vs previous games side by side

### 🏆 Achievements & Progress
- **Achievement System** — Diamond, Gold, Platinum, Bronze tiers
  - ⭐ Perfect Game · ⚡ Speed Demon · 🎯 Accuracy Master · 🧠 Hintless Hero
- **Practice Mode** — targeted practice based on your performance stats
- **Generate Report** — save detailed performance report as PDF/TXT
- **Share Results** — share your score summary

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

```
1. Find next empty cell
2. Try numbers 1–9
3. Check validity (row, column, 3×3 box)
4. If valid → place number → recurse
5. If stuck → backtrack → try next number
6. Repeat until board is complete
```

---

## 📸 Screenshots

### 🎮 Game Board

| Empty Board | Easy Puzzle |
|:---:|:---:|
| <img src="screenshots/01_game_board.png" width="420"> | <img src="screenshots/02_easy_board.png" width="420"> |

| Medium Puzzle | Hard Puzzle |
|:---:|:---:|
| <img src="screenshots/03_medium_board.png" width="420"> | <img src="screenshots/04_hard_board.png" width="420"> |

### 💡 Gameplay Features

| Hint | Mistake |
|:---:|:---:|
| <img src="screenshots/05_hint.png" width="220"> | <img src="screenshots/06_mistake.png" width="220"> |

| Auto Solved |
|:---:|
| <img src="screenshots/07_solve_automatically.png" width="420"> |

### 📊 Performance Dashboard

| KPI Overview | Performance Visualization | Interactive Charts |
|:---:|:---:|:---:|
| <img src="screenshots/08_performance_dashboard.png" width="400"> | <img src="screenshots/09_performance_visualization.png" width="400"> | <img src="screenshots/15_compare_games_charts.png" width="400"> |

### 🏆 Game History & Stats

| Overview | Detailed Comparison | Advanced Stats |
|:---:|:---:|:---:|
| <img src="screenshots/12_compare_games_overview.png" width="400"> | <img src="screenshots/13_compare_games_detailed.png" width="400"> | <img src="screenshots/14_compare_games_stats.png" width="400"> |

### 🎖️ Extra Features

| Achievements | Practice Mode | Share Results | Generate Report |
|:---:|:---:|:---:|:---:|
| <img src="screenshots/16_achievements.png" width="260"> | <img src="screenshots/17_practice_mode.png" width="260"> | <img src="screenshots/18_share_results.png" width="260"> | <img src="screenshots/11_generate_report.png" width="260"> |

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

# Compile
javac *.java

# Run
java SudokuGUI
```

---
### 🙋‍♀️ Connect with Me

Developed by **Huda Usman**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Huda%20Usman-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/hudausman010)

<br/>

⭐ **If you found this project interesting, please give it a star!** ⭐

</div>
